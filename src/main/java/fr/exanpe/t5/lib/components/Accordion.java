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

import org.apache.commons.collections.CollectionUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.AfterRenderBody;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.constants.AccordionEventTypeEnum;
import fr.exanpe.t5.lib.model.AccordionInternalModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * Accordion component.<br/>
 * Items must be provided as body of this component.<br/>
 * JavaScript : This component is bound to a class Exanpe.Accordion.<br/>
 * CSS : This component is bound to a class exanpe-acc.<br/>
 * 
 * @see AccordionItem
 * @author jmaupoux
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/animation/animation-min.js", "js/exanpe-t5-lib.js" }, stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class Accordion implements ClientElement
{
    /**
     * The title of the accordion. If set, display a header with this title.
     */
    @SuppressWarnings("unused")
    @Property
    @Parameter(defaultPrefix = BindingConstants.MESSAGE)
    private String title;

    /**
     * The duration in seconds of the animation during the accordion opening/closing
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "1.0")
    private float duration;

    /**
     * Specify if a multiple items car be opened simultaneously
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true")
    private boolean multiple = true;

    /**
     * Specify the event to use for switching an item from a step to an other.
     * 
     * @see AccordionEventTypeEnum
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false, required = true, value = "click")
    private AccordionEventTypeEnum eventType;

    @Property
    private String uniqueId;

    private static final String ROOT_CSS_CLASS = "exanpe-acc";

    @Inject
    private ComponentResources resources;

    @Inject
    private Environment environment;

    @Inject
    private ExanpeComponentService ecservice;

    @Environmental
    private JavaScriptSupport javascriptSupport;

    @SetupRender
    void init()
    {
        uniqueId = javascriptSupport.allocateClientId(resources);

        if (environment.push(AccordionInternalModel.class, new AccordionInternalModel()) != null) { throw new IllegalStateException(
                "Nested accordions are not supported"); }
    }

    @BeginRender
    void begin(MarkupWriter writer)
    {
        Element e = writer.element("div");

        e.attribute("id", getClientId());
        resources.renderInformalParameters(writer);

        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);
    }

    @AfterRenderBody
    void checkItems()
    {
        if (CollectionUtils.isEmpty(environment.peek(AccordionInternalModel.class).getItemsId()))
            throw new IllegalArgumentException("The Accordion component must contain at least one item.");
    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        writer.end();

        AccordionInternalModel model = environment.pop(AccordionInternalModel.class);

        JSONObject data = buildJSONData(model);

        javascriptSupport.addInitializerCall("accordionBuilder", data);
    }

    private JSONObject buildJSONData(AccordionInternalModel model)
    {
        StringBuilder itemIdsToString = new StringBuilder();
        for (String s : model.getItemsId())
        {
            itemIdsToString.append(s).append(",");
        }
        itemIdsToString.setLength(itemIdsToString.length() - 1);

        JSONObject data = new JSONObject();

        data.accumulate("id", getClientId());
        data.accumulate("duration", duration);
        data.accumulate("multiple", multiple);
        data.accumulate("eventType", eventType.toString().toLowerCase());
        data.accumulate("items", itemIdsToString.toString());

        return data;
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
