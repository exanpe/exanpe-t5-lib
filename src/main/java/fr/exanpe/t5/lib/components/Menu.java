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

import org.apache.tapestry5.BindingConstants;
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

import fr.exanpe.t5.lib.model.MenuInternalModel;
import fr.exanpe.t5.lib.model.MenuInternalModel.MenuRenderElement;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * Menu component.<br/>
 * Items must be provided as body of this component.<br/>
 * JavaScript : This component is bound to a class Exanpe.Menu.<br/>
 * CSS : This component is bound to a class exanpe-menu.<br/>
 * This component wraps a YUI Menu component.<br/>
 * This component must be opened and closed through its JavaScript API.
 * 
 * @since 1.1
 * @see MenuItem
 * @see SubMenu
 * @author jmaupoux
 */
@Import(stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class Menu implements ClientElement
{
    /**
     * Defines an HTML id where the menu should be opened next to.
     * The top left corner of the menu will be bound to the bottom right html element provided.
     */
    @Parameter(required = false, defaultPrefix = BindingConstants.LITERAL)
    private String targetHtmlId;

    private static final String ROOT_CSS_CLASS = "exanpe-menu";

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
        MenuInternalModel model = new MenuInternalModel();
        model.push(MenuRenderElement.MENUITEM);

        if (environment.push(MenuInternalModel.class, model) != null) { throw new IllegalStateException("Nested Menu are not supported"); }
    }

    @BeginRender
    void begin(MarkupWriter writer)
    {
        Element e = writer.element("span");
        resources.renderInformalParameters(writer);

        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);
    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        writer.end();
        environment.pop(MenuInternalModel.class);
        JSONObject data = buildJSONData();
        javaScriptSupport.require("exanpe/menu").invoke("init").with(data);
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();

        data.accumulate("id", getClientId());
        data.accumulate("targetHtmlId", targetHtmlId);

        return data;
    }

    public String getClientId()
    {
        return resources.getId();
    }
}
