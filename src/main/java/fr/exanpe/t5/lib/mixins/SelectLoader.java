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

package fr.exanpe.t5.lib.mixins;

import org.apache.commons.collections.CollectionUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.ContentType;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.corelib.components.Select;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.internal.util.SelectModelRenderer;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.MarkupWriterFactory;
import org.apache.tapestry5.services.ResponseRenderer;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.util.TextStreamResponse;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.constants.ExanpeEventConstants;

/**
 * A mixin for a select component that populates related data in an other select via Ajax on change.<br/>
 * As an example, a first select displays country, while a second one will automatically populate
 * its cities regarding the country selected. <br/>
 * The container is responsible for providing an event handler for event
 * {@link ExanpeEventConstants#SELECTLOADER_EVENT} (or "selectLoaderAction"). The context will be
 * the value of the select sent from the client. The return value should be a {@link SelectModel}
 * object holding the data to display, null is nothing to populate.<br/>
 * JavaScript : This component is bound to a class Exanpe.SelectLoader.<br/>
 * 
 * @author jmaupoux
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/connection/connection-min.js", "${exanpe.asset-base}/js/exanpe-t5-lib.js" })
@Events(ExanpeEventConstants.SELECTLOADER_EVENT)
public class SelectLoader
{
    /**
     * Event sent by the client
     */
    private static final String EVENT_NAME = "loadSelect";

    /**
     * Param containing the value sent by the client
     */
    private static final String PARAM_NAME = "value";

    /**
     * Defines the target select id to populate
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false)
    private String targetId;

    /**
     * Defines a specific value encoder for the target select
     */
    @Parameter
    @SuppressWarnings("rawtypes")
    private ValueEncoder targetEncoder;

    /**
     * The select component to which this mixin is attached.
     */
    @InjectContainer
    private Select select;

    @Inject
    private Logger log;

    @Inject
    private ComponentResources resources;

    @Inject
    private ValueEncoderSource valueEncoderSource;

    @Inject
    private MarkupWriterFactory factory;

    @Inject
    private ResponseRenderer responseRenderer;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @AfterRender
    void end()
    {
        JSONObject data = buildJSONData();

        javaScriptSupport.addInitializerCall("selectLoaderBuilder", data);
    }

    private JSONObject buildJSONData()
    {
        String id = select.getClientId();

        Link link = resources.createEventLink(EVENT_NAME);

        JSONObject data = new JSONObject();

        data.accumulate("id", id);
        data.accumulate("targetId", targetId);
        data.accumulate("url", link.toURI());

        return data;
    }

    @OnEvent(value = EVENT_NAME)
    public Object populateSelect(@RequestParameter(value = PARAM_NAME, allowBlank = true)
    String value)
    {
        log.debug("Ajax value received : {}", value);
        final Holder<SelectModel> holder = Holder.create();

        ComponentEventCallback<SelectModel> callback = new ComponentEventCallback<SelectModel>()
        {
            public boolean handleResult(SelectModel result)
            {
                holder.put(result);
                return true;
            }
        };

        log.debug("Triggering event to container...");
        resources.triggerEvent(ExanpeEventConstants.SELECTLOADER_EVENT, new Object[]
        { value }, callback);

        ContentType contentType = responseRenderer.findContentType(this);

        SelectModel model = holder.get();
        if (model == null || CollectionUtils.isEmpty(model.getOptions()))
        {
            log.debug("Received null SelectModel. Sending empty select to client.");
            return new TextStreamResponse(contentType.toString(), "");
        }

        MarkupWriter writer = factory.newPartialMarkupWriter(contentType);

        writer.element("root");

        model.visit(new SelectModelRenderer(writer, getValueEncoder(model.getOptions().get(0).getValue())));

        writer.end();

        String reponse = writer.toString();
        if (log.isDebugEnabled())
        {
            log.debug("Sending options to client : {}", reponse);
        }
        // substract the prefix <root> and suffix </root>
        return new TextStreamResponse(contentType.toString(), reponse);
    }

    ValueEncoder<?> getValueEncoder(Object o)
    {
        if (targetEncoder != null)
            return targetEncoder;
        return valueEncoderSource.getValueEncoder(o.getClass());
    }
}
