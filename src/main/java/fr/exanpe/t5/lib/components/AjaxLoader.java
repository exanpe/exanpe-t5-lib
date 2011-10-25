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

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.constants.ExanpeEventConstants;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * <code>AjaxLoader</code> component.<br />
 * Load the body of this component in asynchronous mode with Ajax request.<br />
 * JavaScript : This component is bound to a class Exanpe.AjaxLoader.<br/>
 * CSS : This component is bound to a class exanpe-ajaxloader.<br/>
 * 
 * @author lguerin
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/element/element-min.js", "${exanpe.yui2-base}/container/container-min.js",
        "${exanpe.yui2-base}/connection/connection-min.js", "${exanpe.yui2-base}/json/json-min.js", "${exanpe.yui2-base}/animation/animation-min.js",
        "js/exanpe-t5-lib.js" }, stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class AjaxLoader
{
    /**
     * Message displayed to the user during asynchronous loading.
     */
    @Parameter(value = "exanpe-ajaxloader-default-message", defaultPrefix = BindingConstants.MESSAGE)
    private String message;

    /**
     * Image displayed during loading.
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.ASSET, value = "img/ajaxloader/ajax-loader.gif")
    private Asset image;

    /**
     * The width of the panel, in pixel.
     */
    @Property
    @Parameter(value = "150", required = true, defaultPrefix = BindingConstants.LITERAL)
    private int width;

    /**
     * Decide if auto load the content on dom ready.
     */
    @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
    private Boolean autoLoad;

    /**
     * Decide to show the panel during the ajax load, or not.
     */
    @Property
    @Parameter(value = "true", defaultPrefix = BindingConstants.LITERAL)
    private Boolean showPanel;

    /**
     * Decide to highlight the result at the end of the loading, or not.
     */
    @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
    private Boolean highlight;

    @Property
    private Link link;

    @Property
    private String uniqueId;

    private static final String ROOT_CSS_CLASS = "exanpe-ajaxloader";

    @Inject
    private ComponentResources resources;

    @Inject
    private Logger log;

    @Inject
    private Block loading;

    @Inject
    private ExanpeComponentService ecservice;

    @Environmental
    private JavaScriptSupport javascriptSupport;

    @SetupRender
    void init()
    {
        uniqueId = javascriptSupport.allocateClientId(resources);
    }

    @BeginRender
    Block begin(MarkupWriter writer)
    {
        Element e = writer.element("div");
        e.attribute("id", getClientId());
        resources.renderInformalParameters(writer);

        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);

        link = resources.createEventLink(ExanpeEventConstants.AJAXLOADER_EVENT);
        JSONObject data = buildJSONData();
        javascriptSupport.addInitializerCall("ajaxLoaderBuilder", data);

        writer.end();
        return loading;
    }

    @Log
    @OnEvent(value = ExanpeEventConstants.AJAXLOADER_EVENT)
    Block asyncLoading()
    {
        log.debug("Returning body of component : {}", getClientId());
        return resources.getBody();
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();
        data.accumulate("id", getClientId());
        data.accumulate("message", message);
        data.accumulate("image", image.toClientURL());
        data.accumulate("width", width);
        data.accumulate("url", link.toURI());
        data.accumulate("autoLoad", autoLoad);
        data.accumulate("showPanel", showPanel);
        data.accumulate("highlight", highlight);
        return data;
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
