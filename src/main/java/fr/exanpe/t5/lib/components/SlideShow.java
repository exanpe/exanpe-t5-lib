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

import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.model.SlideShowInternalModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * This component displays some items as a slide show view.<br/>
 * Must contains some {@link SlideShowItem} components in its body.<br/>
 * JavaScript : This component is bound to a class Exanpe.SlideShow.<br/>
 * CSS : This component is bound to a class exanpe-sls.<br/>
 * 
 * @see SlideShowItem
 * @author jmaupoux
 */
@Import(stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class SlideShow implements ClientElement
{

    /**
     * Defines whether this slide show is circular. Circular means that once displayed, the content
     * goes back to the begining.
     */
    @Parameter(required = false, value = "true")
    private boolean circular;

    /**
     * Defines the number of visible elements in this slide show.
     */
    @Parameter(required = true, value = "1")
    private int numVisible;

    /**
     * Defines the auto play behavior of the component by setting a milliseconds interval. 0 means
     * no autoplay.
     */
    @Parameter(required = true, value = "0")
    private int autoPlayMillis;

    /**
     * Defines the height, in pixels, of items in the slideShow
     */
    @Parameter(required = true)
    private int itemHeight;

    private static final String ROOT_CSS_CLASS = "exanpe-sls";

    @Inject
    private ComponentResources resources;

    @Inject
    private Environment environment;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ExanpeComponentService ecservice;

    @SetupRender
    void init()
    {
        SlideShowInternalModel marker = new SlideShowInternalModel();
        marker.setHeight(itemHeight);

        if (environment.push(SlideShowInternalModel.class, marker) != null) { throw new IllegalArgumentException("Nesting SlideShow components is disallowed"); }
    }

    @BeginRender
    void begin(MarkupWriter writer)
    {
        Element e = writer.element("div");

        e.attribute("id", getClientId());
        resources.renderInformalParameters(writer);

        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);
    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        writer.end();
        environment.pop(SlideShowInternalModel.class);
        JSONObject data = buildJSONData();
        javaScriptSupport.require("exanpe/slideShow").invoke("init").with(data);
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();

        data.accumulate("id", getClientId());
        data.accumulate("circular", circular);
        data.accumulate("numVisible", numVisible);
        data.accumulate("autoPlayMillis", autoPlayMillis);

        return data;
    }

    public String getClientId()
    {
        return resources.getId();
    }
}
