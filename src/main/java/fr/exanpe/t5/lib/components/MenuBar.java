package fr.exanpe.t5.lib.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.constants.MenuEventTypeEnum;
import fr.exanpe.t5.lib.model.MenuInternalModel;
import fr.exanpe.t5.lib.model.MenuInternalModel.MenuRenderElement;

/**
 * MenuBar component.<br/>
 * Items must be provided as body of this component.<br/>
 * JavaScript : This component is bound to a class Exanpe.MenuBar.<br/>
 * CSS : This component is bound to a class exanpe-menu.<br/>
 * This component wraps a YUI MenuBar component.
 * 
 * @see MenuItem
 * @see SubMenu
 * @author jmaupoux
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/container/container-min.js", "${exanpe.yui2-base}/menu/menu-min.js",
        "js/exanpe-t5-lib.js" }, stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class MenuBar implements ClientElement
{
    /**
     * Defines the event triggering the menu.
     * 
     * @see MenuEventTypeEnum
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false, value = "hover")
    private MenuEventTypeEnum eventType;

    @Inject
    private ComponentResources resources;

    @Inject
    private Environment environment;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @SetupRender
    void init()
    {
        MenuInternalModel model = new MenuInternalModel();
        model.push(MenuRenderElement.ROOT);

        if (environment.push(MenuInternalModel.class, model) != null) { throw new IllegalStateException("Nested MenuBar are not supported"); }
    }

    @AfterRender
    void end()
    {
        environment.pop(MenuInternalModel.class);

        javaScriptSupport.addInitializerCall("menuBarBuilder", buildJSONData());
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();

        data.accumulate("id", getClientId());
        data.accumulate("eventType", eventType.toString().toLowerCase());

        return data;
    }

    public String getClientId()
    {
        return resources.getId();
    }
}
