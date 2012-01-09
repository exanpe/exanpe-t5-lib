//
// Copyright 2011 EXANPE <exanpe@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package fr.exanpe.t5.lib.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentAction;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * The list sorter allow you to sort some elements displayed into a vertical list.<br/>
 * Technically, it works with a basic loop enriched.<br/>
 * The body of the component will be the render of the list.<br/>
 * The list given as parameter will be directly modified by the component.<br/>
 * The component HAS to be nested into a {@link Form} component, its modification can be submitted
 * as any form component.
 * 
 * @author jmaupoux
 * @since 1.2
 * @param <T> the type of elements
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/animation/animation-min.js", "${exanpe.yui2-base}/dragdrop/dragdrop-min.js",
        "${exanpe.yui2-base}/json/json-min.js", "${exanpe.yui2-base}/connection/connection-min.js", "js/exanpe-t5-lib.js" }, stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class ListSorter<T> implements ClientElement, Serializable
{
    /**
     * serial uid
     */
    private static final long serialVersionUID = 5770144702289313359L;

    /**
     * Source list parameter.
     */
    @Parameter(required = true, allowNull = false, autoconnect = true)
    @Property
    private List<T> source;

    /**
     * Value to iterate over. Mainly used to bind to a property of the page, then design the list
     * content.
     */
    @Parameter(required = true, autoconnect = true)
    @Property
    private T value;

    /**
     * Whether to use a default skin for the list sorter. True by default. Turn to false to design
     * each element your own way.
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, value = "true")
    @Property
    private boolean useDefaultSkin;

    /**
     * Index of the iteration
     */
    @Property
    private int index;

    /**
     * Save event
     */
    private static final String EVENT_NAME = "save";

    /**
     * Save param
     */
    private static final String PARAM_PERMUTATIONS = "permutations";

    /**
     * Save param
     */
    private static final String ROOT_CSS_CLASS = "exanpe-lst";

    private static final String DEFAULT_SKIN_CLASS = "exanpe-lst-skin";

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private Logger log;

    @Inject
    private Request request;

    @Inject
    private FormSupport formSupport;

    @Inject
    private ExanpeComponentService ecservice;

    // unique id generated
    private String uniqueId;

    /**
     * Class allowing the processing of the new order on form submission
     * 
     * @author jmaupoux
     */
    static class ListSorterProcessSubmission implements ComponentAction<ListSorter<?>>
    {
        /**
         * Serial uid
         */
        private static final long serialVersionUID = -4346426414137434418L;

        /**
         * Name of the element, as present in the input name attribute
         */
        private final String elementName;

        public ListSorterProcessSubmission(String elementId)
        {

            this.elementName = elementId;
        }

        public void execute(ListSorter<?> component)
        {
            component.processSubmission(elementName);
        }

        @Override
        public String toString()
        {
            return "ListSorter.ListSorterProcessSubmission";
        }
    }

    @SetupRender
    void init()
    {
        uniqueId = javaScriptSupport.allocateClientId(resources);

        if (formSupport == null)
            throw new RuntimeException(String.format("Component %s must be enclosed by a Form component.", resources.getCompleteId()));

        // add the list processor
        formSupport.store(this, new ListSorterProcessSubmission(getInputElementName()));
    }

    @BeginRender
    void begin(MarkupWriter writer)
    {
        Element e = writer.element("div");

        e.attribute("id", getClientId());
        resources.renderInformalParameters(writer);

        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);

        if (useDefaultSkin)
        {
            e.addClassName(DEFAULT_SKIN_CLASS);
        }
    }

    /**
     * Submission processing. Compute the new list order.
     * 
     * @param elementName the parameter name
     */
    void processSubmission(String elementName)
    {
        String rawValue = request.getParameter(elementName);

        JSONArray array = new JSONArray(rawValue);

        reorder(source, array);
    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        writer.end();

        JSONObject data = buildJSONData();

        javaScriptSupport.addInitializerCall("listSorterBuilder", data);
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();

        Link link = resources.createEventLink(EVENT_NAME);

        data.accumulate("id", getClientId());
        data.accumulate("urlSave", link.toURI());

        return data;
    }

    @OnEvent(value = EVENT_NAME)
    public void save(@RequestParameter(value = PARAM_PERMUTATIONS)
    String newOrder)
    {
        if (log.isDebugEnabled())
        {
            log.debug("Ajax save request received");
        }

        if (source == null)
        {
            log.debug("Source list null. Stopping");
            return;
        }

        JSONArray array = new JSONArray(newOrder);

        reorder(source, array);

        if (log.isDebugEnabled())
        {
            log.debug("Saved order is: {}", StringUtils.join(source, ","));
        }

        if (log.isDebugEnabled())
        {
            log.debug("Ajax save done");
        }

        return;
    }

    /**
     * Reorder the element in the list, depending on the order of the JSONArray
     * 
     * @param destination the list to modify
     * @param array the ordering array
     */
    private void reorder(List<T> destination, JSONArray array)
    {
        List<T> source = new ArrayList<T>(destination);

        destination.clear();

        for (int i = 0; i < array.length(); i++)
        {
            destination.add(source.get(array.getInt(i)));
        }
    }

    /**
     * The name of the input used for submission
     * 
     * @return the name of the input used for submission
     */
    public String getInputElementName()
    {
        return uniqueId + "_input";
    }

    public String getClientId()
    {
        return uniqueId;
    }

}
