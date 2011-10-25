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

/**
 * 
 */
package fr.exanpe.t5.lib.mixins;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.InitializationPriority;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.constants.DialogRenderModeEnum;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * A <code>Dialog</code> box mixin.<br />
 * Display a dialog box that can take different rendering modes :
 * <ul>
 * <li>confirm : a "Yes/No" dialog box ;</li>
 * <li>info : a "Ok" dialog box ;</li>
 * </ul>
 * The dialog title and message can automatically be gathered from properties file according to the
 * container id.<br/>
 * The following key patterns will be automatically checked :<br/>
 * <ul>
 * <li>${id}-title</li>
 * <li>${id}-message</li>
 * </ul>
 * <br/>
 * <p>
 * The Dialog box could be disabled if the CSS class "exanpe-dialog-disable" is present on the
 * container element. In a such case, the container action (from link or submit) is executed without
 * interception.
 * </p>
 * JavaScript : This component is bound to a class Exanpe.Dialog.<br/>
 * CSS : This component is bound to a class exanpe-dialog.<br/>
 * 
 * @see DialogRenderModeEnum
 * @author lguerin
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/element/element-min.js", "${exanpe.yui2-base}/dragdrop/dragdrop-min.js",
        "${exanpe.yui2-base}/container/container-min.js", "${exanpe.yui2-base}/connection/connection-min.js", "${exanpe.yui2-base}/button/button-min.js",
        "${exanpe.asset-base}/js/exanpe-t5-lib.js" }, stylesheet =
{ "${exanpe.asset-base}/css/exanpe-t5-lib-core.css", "${exanpe.asset-base}/css/exanpe-t5-lib-skin.css" })
public class Dialog
{
    /**
     * The message of the Dialog box.
     */
    @Parameter(value = "exanpe-dialog-default-message", defaultPrefix = BindingConstants.MESSAGE)
    private String message;

    /**
     * The title of the Dialog box.
     */
    @Parameter(value = "exanpe-dialog-default-title", defaultPrefix = BindingConstants.MESSAGE)
    private String title;

    /**
     * The label of the yes button
     */
    @Parameter(value = "exanpe-dialog-yes-button-label", allowNull = false, defaultPrefix = BindingConstants.MESSAGE)
    private String yesLabelButton;

    /**
     * The label of the no button
     */
    @Parameter(value = "exanpe-dialog-no-button-label", allowNull = false, defaultPrefix = BindingConstants.MESSAGE)
    private String noLabelButton;

    /**
     * The label of the ok button
     */
    @Parameter(value = "exanpe-dialog-ok-button-label", allowNull = false, defaultPrefix = BindingConstants.MESSAGE)
    private String okLabelButton;

    /**
     * Specify the render mode to used for displaying Dialog.
     * 
     * @see DialogRenderModeEnum
     */
    @Parameter(value = "confirm", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private DialogRenderModeEnum renderMode;

    /**
     * Width of the Dialog, in pixel
     */
    @Parameter(value = "300", required = false, defaultPrefix = BindingConstants.LITERAL)
    private Integer width;

    /**
     * CSS class used to disable dialog box
     */
    public static final String DISABLE_CLASS = "exanpe-dialog-disable";

    /**
     * Root CSS class of the mixin
     */
    private static final String ROOT_CSS_CLASS = "exanpe-dialog";

    /**
     * Dialog component Id prefix
     */
    private static final String DIALOG_ID_PREFIX = "exanpe-dialog-";

    /**
     * title suffix
     */
    private static final String TITLE_SUFFIX = "-title";

    /**
     * message suffix
     */
    private static final String MESSAGE_SUFFIX = "-message";

    @InjectContainer
    private ClientElement container;

    @Environmental
    private JavaScriptSupport javascriptSupport;

    @Inject
    private Logger log;

    @Inject
    private ExanpeComponentService exanpeService;

    @Inject
    private ComponentResources resources;

    /**
     * Must be called @AfterRender
     */
    private String consolidateTitleFromId()
    {
        String id = container.getClientId();

        /** title **/
        String titleKey = id + TITLE_SUFFIX;
        String title = exanpeService.getEscaladeMessage(resources, titleKey);

        log.debug("Checking title into resources file with key: {}", titleKey);

        if (StringUtils.isNotEmpty(title)) { return title; }
        return null;
    }

    /**
     * Must be called @AfterRender
     */
    private String consolidateMessageFromId()
    {
        String id = container.getClientId();

        /** message **/
        String messageKey = id + MESSAGE_SUFFIX;
        String message = exanpeService.getEscaladeMessage(resources, messageKey);

        log.debug("Checking title into resources file with key: {}", messageKey);
        if (StringUtils.isNotEmpty(message)) { return message; }
        return null;

    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        Element e = writer.element("span");
        e.attribute("id", DIALOG_ID_PREFIX + container.getClientId());
        e.addClassName(ROOT_CSS_CLASS);

        JSONObject data = buildJSONData();
        javascriptSupport.addInitializerCall(InitializationPriority.NORMAL, "dialogBuilder", data);
        writer.end();
    }

    private JSONObject buildJSONData()
    {
        String titleFromId = consolidateTitleFromId();
        String messageFromId = consolidateMessageFromId();

        JSONObject data = new JSONObject();
        data.accumulate("id", container.getClientId());
        data.accumulate("message", messageFromId == null ? message : messageFromId);
        data.accumulate("title", titleFromId == null ? title : titleFromId);
        data.accumulate("renderMode", renderMode.toString());
        data.accumulate("width", width);
        data.accumulate("yesLabelButton", yesLabelButton);
        data.accumulate("noLabelButton", noLabelButton);
        data.accumulate("okLabelButton", okLabelButton);
        return data;
    }
}
