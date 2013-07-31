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
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.corelib.components.PasswordField;
import org.apache.tapestry5.internal.util.Holder;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.constants.ExanpeEventConstants;
import fr.exanpe.t5.lib.constants.PasswordStrengthCheckerTypeEnum;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * Mixin used to evaluate the strength of password strings.<br />
 * This mixin displays an instantaneous visual feedback related to the complexity of the password.<br />
 * Note that the mixin does not provide default rules to evaluate the password strength.<br />
 * <p>
 * The end user of the mixin must provide their own formulas, and can achieve this in two ways:
 * <ul>
 * <li>Client-side (Javascript), with the javascriptChecker parameter</li>
 * <li>Server-side (Ajax), if the ajax parameter is set to true</li>
 * </ul>
 * </p>
 * <p>
 * Finally, the strenght of the password is restituted with visual feedback according to the
 * PasswordStrengthCheckerTypeEnum result value.
 * </p>
 * <p>
 * JavaScript : This component is bound to a class Exanpe.PasswordStrengthChecker<br/>
 * CSS : This component is bound to a class exanpe-psc<br/>
 * </p>
 * 
 * @since 1.1
 * @see PasswordStrengthCheckerTypeEnum
 * @author lguerin
 */
@Import(stylesheet =
{ "${exanpe.asset-base}/css/exanpe-t5-lib-core.css", "${exanpe.asset-base}/css/exanpe-t5-lib-skin.css" })
@Events(ExanpeEventConstants.PASSWORDSTRENGTHCHECKER_EVENT)
public class PasswordStrengthChecker
{
    /**
     * Decide if password checking is made in Ajax mode, or not
     */
    @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private Boolean ajax;

    /**
     * The javascript function used client-side to check the password complexity.
     * This JS function MUST return an element of Exanpe.PasswordStrengthChecker.Complexity enum.
     */
    @Parameter(required = false, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String javascriptChecker;

    /**
     * Minimum number of caracters before triggering ajax event.
     */
    @Property
    @Parameter(value = "4", defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private Integer min;

    private static final String ROOT_CSS_CLASS = "exanpe-psc";
    private static final String MIXIN_ID_PREFIX = "exanpe-psc-";
    private static final String LABEL_PREFIX = "exanpe-password-strength-checker-";
    private static final String LABEL_SUFFIX = "-label";

    /**
     * Ajax event triggered client-side.
     */
    private static final String EVENT_NAME = "checkPasswordStrength";

    /**
     * Request param send (in Ajax) to check the password
     */
    public static final String PARAM_NAME = "pwd";

    /**
     * The password component this mixin is attached to.
     */
    @InjectContainer
    private PasswordField password;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private Logger log;

    @Inject
    private ExanpeComponentService exanpeService;

    @Inject
    private ComponentResources resources;

    @AfterRender
    void end(MarkupWriter writer)
    {
        JSONObject data = buildJSONData();
        generatePasswordStrengthMeterMarkup(writer);
        javaScriptSupport.require("exanpe/passwordStrengthChecker").invoke("init").with(data);
    }

    @OnEvent(value = EVENT_NAME)
    Object onPasswordSelect(@RequestParameter(PARAM_NAME)
    String pwd)
    {
        log.debug("Ajax value received: {}", pwd);
        final Holder<PasswordStrengthCheckerTypeEnum> holder = Holder.create();
        final ComponentEventCallback<PasswordStrengthCheckerTypeEnum> callback = new ComponentEventCallback<PasswordStrengthCheckerTypeEnum>()
        {
            public boolean handleResult(final PasswordStrengthCheckerTypeEnum result)
            {
                holder.put(result);
                return true;
            }
        };

        log.debug("Triggering event to container...");
        resources.triggerEvent(ExanpeEventConstants.PASSWORDSTRENGTHCHECKER_EVENT, new Object[]
        { pwd }, callback);

        log.debug("Sending result from container...");
        JSONObject response = generateAjaxResponse(holder.get());
        return response;
    }

    private JSONObject generateAjaxResponse(PasswordStrengthCheckerTypeEnum result)
    {
        JSONObject response = new JSONObject();
        response.put("result", result.toString());
        return response;
    }

    private JSONObject buildJSONData()
    {
        String id = password.getClientId();
        Link link = resources.createEventLink(EVENT_NAME);

        JSONObject data = new JSONObject();
        data.accumulate("id", id);
        data.accumulate("ajax", ajax);
        data.accumulate("javascriptChecker", javascriptChecker);
        data.accumulate("min", min);
        data.accumulate("url", link.toURI());

        // messages used by mixin
        JSONObject messages = new JSONObject();
        messages.accumulate("disclaimer", consolidateLabelFromId("disclaimer"));
        for (PasswordStrengthCheckerTypeEnum type : PasswordStrengthCheckerTypeEnum.values())
        {
            messages.accumulate(type.toString(), consolidateLabelFromId(type.toString()));
        }
        data.accumulate("messages", messages.toString());
        return data;
    }

    /**
     * Must be called @AfterRender
     */
    private String consolidateLabelFromId(String type)
    {
        String labelKey = LABEL_PREFIX + type + LABEL_SUFFIX;
        String label = exanpeService.getEscaladeMessage(resources, labelKey);

        log.debug("Checking label into resources file with key: {}", labelKey);

        if (StringUtils.isNotEmpty(label)) { return label; }
        return null;
    }

    private void generatePasswordStrengthMeterMarkup(MarkupWriter writer)
    {
        writer.element("div", "id", this.getMixinId(), "class", ROOT_CSS_CLASS);
        writer.element("span", "class", "wrapper");
        writer.element("p", "id", this.getMixinId() + "-msg");
        writer.end();
        for (int i = 0; i < PasswordStrengthCheckerTypeEnum.values().length; i++)
        {
            writer.element("div", "class", ROOT_CSS_CLASS + "-tag", "id", this.getMixinId() + "-tag-" + i);
            writer.end();
        }
        writer.end();
        writer.end();
    }

    private String getMixinId()
    {
        return MIXIN_ID_PREFIX + password.getClientId();
    }

}
