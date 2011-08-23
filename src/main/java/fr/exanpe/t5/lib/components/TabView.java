package fr.exanpe.t5.lib.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.EventConstants;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.model.TabViewInternalModel;
import fr.exanpe.t5.lib.model.TabViewInternalModel.TabModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * TabView component. Must contain some {@link Tab} components in its body.<br/>
 * JavaScript : This component is bound to a class Exanpe.TabView.<br/>
 * CSS : This component is bound to a class exanpe-tab.<br/>
 * 
 * @see Tab
 * @author jmaupoux
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "js/exanpe-t5-lib.js" }, stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class TabView implements ClientElement
{

    /**
     * Defines the tab active by default.
     */
    @Property
    @Parameter(allowNull = false, required = true, defaultPrefix = BindingConstants.LITERAL)
    private String defaultActiveTabId;

    /**
     * Defines the loading behavior of all hidden tab contents.
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "false")
    private boolean loadAll;

    /**
     * TabModel Iterator
     */
    @Property
    private TabModel iteTabModel;

    @Property
    private TabViewInternalModel model;

    /**
     * Class name of hidden tabs
     */
    private static final String CSS_TAB_HIDDEN = "exanpe-tab-hidden";

    /**
     * Class name of hidden tabs
     */
    private static final String ROOT_CSS_CLASS = "exanpe-tab";

    @Inject
    private Block loadBlock;

    @Inject
    private Block noLoadBlock;

    @Inject
    private ComponentResources resources;

    @Inject
    private Environment environment;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ExanpeComponentService ecservice;

    @Persist
    private String selectedTabId;

    @SetupRender
    void init()
    {
        if (selectedTabId == null)
            selectedTabId = defaultActiveTabId;

        model = new TabViewInternalModel();
        model.setTabId(getClientId());
        model.setSelectedTabId(selectedTabId);
        model.setLoadAll(loadAll);

        if (environment.push(TabViewInternalModel.class, model) != null) { throw new IllegalStateException("Nested Tabs are not supported"); }
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

        environment.pop(TabViewInternalModel.class);

        JSONObject data = buildJSONData(model);

        javaScriptSupport.addInitializerCall("tabViewBuilder", data);
    }

    private JSONObject buildJSONData(TabViewInternalModel model)
    {
        JSONArray jsonTabs = new JSONArray();
        for (TabModel m : model.getTabs())
        {
            jsonTabs.put(new JSONObject("id", m.getId()));
        }

        JSONObject data = new JSONObject();

        data.accumulate("id", getClientId());
        data.accumulate("tabs", jsonTabs);
        data.accumulate("selectedId", selectedTabId);

        return data;
    }

    /**
     * Catch the event of a tab selection
     * 
     * @param tabId the tab to select
     */
    @OnEvent(value = EventConstants.ACTION, component = "switchTab")
    void switchTab(String tabId)
    {
        setSelectedTab(tabId);
    }

    /**
     * Reset the selected tab
     */
    public void resetSelectedTab()
    {
        selectedTabId = null;
    }

    /**
     * Set a tab as selected.
     * 
     * @param tabId the id of the tab to set as selected
     */
    public void setSelectedTab(String tabId)
    {
        selectedTabId = tabId;
    }

    /**
     * Returns true if the current iterated tab is selected, false otherwise
     * 
     * @return true if the current iterated tab is selected, false otherwise
     */
    boolean isTabSelected()
    {
        return selectedTabId.equals(iteTabModel.getId());
    }

    /**
     * Returns the current iterated tab classname
     * Internal rendering method.
     * 
     * @return the current iterated tab classname
     */
    public String getTabCssClassName()
    {
        if (isTabSelected())
            return "";
        return CSS_TAB_HIDDEN;
    }

    /**
     * return the current iterated tab rendered block
     * Internal rendering method.
     * 
     * @return the current iterated tab rendered block
     */
    public Block getRenderedBlock()
    {
        if (isTabSelected() || loadAll)
            return loadBlock;
        return noLoadBlock;
    }

    public String getClientId()
    {
        return resources.getId();
    }

}
