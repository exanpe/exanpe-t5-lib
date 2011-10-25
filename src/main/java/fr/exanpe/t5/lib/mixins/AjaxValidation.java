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

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentEventCallback;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.corelib.components.TextField;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.FormSupport;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.constants.AjaxValidationResult;
import fr.exanpe.t5.lib.constants.ExanpeEventConstants;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * A mixin for a textfield component that validate the content using an Ajax request.<br/>
 * The container is responsible for providing an event handler for event
 * {@link ExanpeEventConstants#AJAXVALIDATION_EVENT} (or "ajaxValidationAction"). The context will
 * be the value of the input sent from the client. The return value should be a
 * {@link AjaxValidationResult} object defining if the value is correct (TRUE) or not (FALSE or
 * null).<br/>
 * As any validator, the message displayed can be defined in a property file, with
 * <i>ajaxValidator</i> as validator name.<br/>
 * This component fully rely on Tapestry core to manage error messages.
 * JavaScript : This component is bound to a class Exanpe.AjaxValidation.<br/>
 * 
 * @author jmaupoux
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/connection/connection-min.js", "${exanpe.yui2-base}/json/json-min.js",
        "${exanpe.asset-base}/js/exanpe-t5-lib.js" })
@Events(ExanpeEventConstants.AJAXVALIDATION_EVENT)
public class AjaxValidation
{
    /**
     * Default error message
     */
    private static final String DEFAULT_MESSAGE = "This field is incorrect";

    /**
     * "message" String
     */
    private static final String MESSAGE_STRING = "message";

    /**
     * Name of the validator
     */
    private static final String VALIDATOR_NAME = "ajaxValidator";

    /**
     * Event sent by the client
     */
    private static final String EVENT_NAME = "ajaxValidate";

    /**
     * Param containing the value sent by the client
     */
    private static final String PARAM_NAME = "value";

    /**
     * Specify the message displayed for the validation.
     * 
     * @see AjaxValidationEventTypeEnum
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.MESSAGE, required = false)
    private String message;

    private String finalMessage = DEFAULT_MESSAGE;

    /**
     * The textfield component this mixin is attached.
     */
    @InjectContainer
    private TextField textField;

    @Inject
    private Logger log;

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private FormSupport formSupport;

    @Inject
    private ExanpeComponentService exanpeService;

    // BeginRender because we need the textfield generated id
    @BeginRender
    void init()
    {
        if (!formSupport.isClientValidationEnabled()) { return; }

        consolidateFromId();
    }

    private void consolidateFromId()
    {
        if (!StringUtils.isEmpty(message))
        {
            finalMessage = message;
            return;
        }

        String formKey = formatLongKey();
        String message = exanpeService.getEscaladeMessage(resources, formKey);

        log.debug("Checking title into resources file with key: {}", formKey);

        if (StringUtils.isNotEmpty(message))
        {
            this.finalMessage = message;
            return;
        }

        formKey = formatShortKey();
        message = exanpeService.getEscaladeMessage(resources, formKey);

        log.debug("Checking title into resources file with key: {}", formKey);

        if (StringUtils.isNotEmpty(message))
        {
            this.finalMessage = message;
        }

    }

    String formatLongKey()
    {
        return formSupport.getFormComponentId() + "-" + textField.getClientId() + "-" + VALIDATOR_NAME + "-" + MESSAGE_STRING;
    }

    String formatShortKey()
    {
        return textField.getClientId() + "-" + VALIDATOR_NAME + "-" + MESSAGE_STRING;
    }

    @AfterRender
    void end()
    {
        if (!formSupport.isClientValidationEnabled()) { return; }

        JSONObject data = buildJSONData();

        formSupport.addValidation(textField, VALIDATOR_NAME, finalMessage, null);
        javaScriptSupport.addInitializerCall("ajaxValidationBuilder", data);
    }

    private JSONObject buildJSONData()
    {
        String id = textField.getClientId();

        Link link = resources.createEventLink(EVENT_NAME);

        JSONObject data = new JSONObject();

        data.accumulate("id", id);
        data.accumulate("url", link.toURI());
        data.accumulate("message", finalMessage);

        return data;
    }

    @OnEvent(value = EVENT_NAME)
    public Object ajaxValidate(@RequestParameter(value = PARAM_NAME, allowBlank = true)
    String value)
    {
        if (log.isDebugEnabled())
        {
            log.debug("Ajax value received : {}", value);
        }
        final Holder<AjaxValidationResult> holder = Holder.create();

        ComponentEventCallback<AjaxValidationResult> callback = new ComponentEventCallback<AjaxValidationResult>()
        {
            public boolean handleResult(AjaxValidationResult result)
            {
                holder.put(result);
                return true;
            }
        };

        log.debug("Triggering event to container...");
        resources.triggerEvent(ExanpeEventConstants.AJAXVALIDATION_EVENT, new String[]
        { value }, callback);

        Boolean result = (holder.get() == null) ? Boolean.FALSE : holder.get().toBoolean();

        if (log.isDebugEnabled())
        {
            log.debug("Sending response to client : {}", result);
        }

        return new JSONObject().accumulate("result", result);
    }
}
