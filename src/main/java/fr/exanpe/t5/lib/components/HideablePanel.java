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
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * This component renders a left panel to be shown/hidden on will.<br/>
 * JavaScript : This component is bound to a class Exanpe.HideablePanel<br/>
 * CSS : This component is bound to a class exanpe-hdp.<br/>
 * 
 * @author Jul
 */
@Import(library =
{ "js/exanpe-t5-lib.js", "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/animation/animation-min.js" }, stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class HideablePanel implements ClientElement
{
    /**
     * Bloc hideable, disposed on left
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    @SuppressWarnings("unused")
    private Block hideable;

    /**
     * Bloc always visible, on right
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    @SuppressWarnings("unused")
    private Block visible;

    /**
     * Width in pixel for the hideable panel
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL)
    private int hideWidth;

    /**
     * Duration of show
     */
    @Property
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, value = "1.0")
    private float duration;

    /**
     * Image displayed to hide the panel
     */

    @Property
    @Parameter(defaultPrefix = BindingConstants.ASSET, value = "img/hdp/hide.png")
    @SuppressWarnings("unused")
    private Asset hideImg;

    /**
     * Image displayed to show the panel
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.ASSET, value = "img/hdp/show.png")
    @SuppressWarnings("unused")
    private Asset showImg;

    private static final String ROOT_CSS_CLASS = "exanpe-hdp";

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport javascriptSupport;

    @Inject
    private ExanpeComponentService ecservice;

    @BeginRender
    void begin(MarkupWriter writer)
    {
        Element e = writer.element("table");

        e.attribute("id", getClientId());
        resources.renderInformalParameters(writer);

        e.attribute("cellspacing", "0");
        e.attribute("cellpadding", "0");

        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);
    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        writer.end();

        JSONObject data = buildJSONData();

        javascriptSupport.addInitializerCall("hideablePanelBuilder", data);
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();

        data.accumulate("id", getClientId());
        data.accumulate("hideWidth", hideWidth);
        data.accumulate("duration", this.duration);

        return data;
    }

    public String getClientId()
    {
        return resources.getId();
    }
}
