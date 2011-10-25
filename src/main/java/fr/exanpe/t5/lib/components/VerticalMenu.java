package fr.exanpe.t5.lib.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.corelib.components.EventLink;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.internal.util.CaptureResultCallback;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.constants.ExanpeEventConstants;
import fr.exanpe.t5.lib.model.VerticalMenuInternalModel;
import fr.exanpe.t5.lib.model.VerticalMenuInternalModel.MenuItemModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * VerticalMenu component.<br/>
 * Items must be provided as body of this component (Loop of {@link VerticalMenuItem}).<br/>
 * JavaScript : This component is bound to a class Exanpe.VerticalMenu.<br/>
 * CSS : This component is bound to a class exanpe-vmenu.<br/>
 * 
 * @since 1.1
 * @see VerticalMenuItem
 * @author lguerin
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "js/exanpe-t5-lib.js" }, stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
@Events(ExanpeEventConstants.VERTICALMENU_EVENT)
public class VerticalMenu implements ClientElement
{
    /**
     * Defines the default active Menu item
     */
    @Property
    @Parameter(allowNull = false, required = true, defaultPrefix = BindingConstants.LITERAL)
    private String defaultActiveItem;

    /**
     * The current MenuItemModel used into loop
     */
    @Property
    private MenuItemModel current;

    /**
     * Internal model for VerticalMenu
     */
    @Property
    private VerticalMenuInternalModel model;

    /**
     * The selected menu item id
     */
    @Persist
    private String selectedMenuItemId;

    /**
     * Class name of closed menu item content
     */
    private static final String CSS_VMENU_CONTENT_CLOSED = "exanpe-vmenu-closed";

    /**
     * Class name of selected menu item title
     */
    private static final String CSS_VMENU_CONTENT_SELECTED = "exanpe-vmenu-selected";

    /**
     * Root class name of vertical menu
     */
    private static final String ROOT_CSS_CLASS = "exanpe-vmenu";

    /**
     * Event triggered when selecting menu item
     */
    @SuppressWarnings("unused")
    @Component(parameters =
    { "event=selectMenuItem", "context=current.id" })
    private EventLink eventLink;

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
        if (selectedMenuItemId == null)
        {
            selectedMenuItemId = defaultActiveItem;
        }

        model = new VerticalMenuInternalModel();
        model.setMenuId(getClientId());
        model.setSelectedMenuItemId(selectedMenuItemId);

        if (environment.push(VerticalMenuInternalModel.class, model) != null) { throw new IllegalStateException("Nested Vertical menus are not supported"); }
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
        environment.pop(VerticalMenuInternalModel.class);

        JSONObject data = buildJSONData(model);
        javaScriptSupport.addInitializerCall("verticalMenuBuilder", data);
    }

    private JSONObject buildJSONData(VerticalMenuInternalModel model)
    {
        JSONArray jsonItems = new JSONArray();
        for (MenuItemModel m : model.getMenuItems())
        {
            jsonItems.put(new JSONObject("id", m.getId()));
        }

        JSONObject data = new JSONObject();

        data.accumulate("id", getClientId());
        data.accumulate("items", jsonItems);
        data.accumulate("selectedId", selectedMenuItemId);

        return data;
    }

    /**
     * Catch the event of a menu item selection
     * 
     * @param menuItemId the menu item to select
     */
    @OnEvent(value = "selectMenuItem")
    Object selectMenu(String menuItemId)
    {
        this.selectedMenuItemId = menuItemId;
        CaptureResultCallback<Object> callback = new CaptureResultCallback<Object>();
        resources.triggerEvent(ExanpeEventConstants.VERTICALMENU_EVENT, new Object[]
        { menuItemId }, callback);
        return callback.getResult();
    }

    public String getClientId()
    {
        return resources.getId();
    }

    /**
     * Returns true if the current menu item is selected, false otherwise
     * 
     * @return true if the current menu item is selected, false otherwise
     */
    boolean isMenuItemSelected()
    {
        return selectedMenuItemId.equals(current.getId());
    }

    /**
     * Returns the current Menu item content classname
     * Internal rendering method.
     * 
     * @return the current Menu item classname
     */
    public String getCssContentClassName()
    {
        return isMenuItemSelected() ? "" : CSS_VMENU_CONTENT_CLOSED;
    }

    /**
     * Returns the current Menu item title classname
     * Internal rendering method.
     * 
     * @return the current Menu item classname
     */
    public String getCssTitleClassName()
    {
        return isMenuItemSelected() ? CSS_VMENU_CONTENT_SELECTED : "";
    }
}
