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

import java.awt.Color;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
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
import fr.exanpe.t5.lib.services.ExanpeLibraryModule;

/**
 * Color Picker component.<br/>
 * This component provides the selection of a color inside a popup, and displays the value into a
 * read only input field.<br/>
 * Has to be enclosed inside a t:form.<br/>
 * Coercion have been defined to allow the value attribute to be bound to a {@link Color} type
 * attribute in your Page.<br/>
 * JavaScript : This component is bound to a class Exanpe.ColorPicker.<br/>
 * CSS : This component is bound to a class exanpe-cpk.<br/>
 * 
 * @see ExanpeLibraryModule#contributeTypeCoercer(org.apache.tapestry5.ioc.Configuration)
 * @author jmaupoux
 */
@Import(stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class ColorPicker implements ClientElement
{

    /**
     * Define the name of the value to set the color in the container.
     */
    @Property
    @Parameter(autoconnect = true)
    @SuppressWarnings("unused")
    private String value;

    /**
     * Define the icon to open the color picker.
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.ASSET, value = "img/cpk/color_wheel.png")
    @SuppressWarnings("unused")
    private Asset icon;

    /**
     * Define the text to close the component.
     */
    @Property
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.MESSAGE, value = "literal:Close")
    private String closeText;

    private static final String ROOT_CSS_CLASS = "exanpe-cpk";

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ExanpeComponentService ecservice;

    @BeginRender
    void begin(MarkupWriter writer)
    {
        Element e = writer.element("span");

        e.attribute("id", getClientId());
        resources.renderInformalParameters(writer);

        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);
    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        writer.end();

        JSONObject data = buildJSONData();
        javaScriptSupport.require("exanpe/colorPicker").invoke("init").with(data);
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();

        data.accumulate("id", getClientId());
        data.accumulate("closeText", closeText);

        return data;
    }

    public String getClientId()
    {
        return resources.getId();
    }

}
