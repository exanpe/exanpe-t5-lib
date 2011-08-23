package fr.exanpe.t5.lib.model;

import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.Asset;

import fr.exanpe.t5.lib.components.Tab;
import fr.exanpe.t5.lib.components.TabView;

/**
 * TabView internal model to transmit data between the tab view and its tab
 * 
 * @see TabView
 * @see Tab
 * @author jmaupoux
 */
public class TabViewInternalModel
{
    private String tabId;
    private String selectedTabId;
    private List<TabModel> tabs = new LinkedList<TabModel>();
    private boolean loadAll;

    public String getSelectedTabId()
    {
        return selectedTabId;
    }

    public void setSelectedTabId(String selectedTabId)
    {
        this.selectedTabId = selectedTabId;
    }

    public List<TabModel> getTabs()
    {
        return tabs;
    }

    public void setTabs(List<TabModel> tabs)
    {
        this.tabs = tabs;
    }

    public void addTab(TabModel model)
    {
        tabs.add(model);
    }

    public boolean isLoadAll()
    {
        return loadAll;
    }

    public void setLoadAll(boolean loadAll)
    {
        this.loadAll = loadAll;
    }

    public String getTabId()
    {
        return tabId;
    }

    public void setTabId(String tabId)
    {
        this.tabId = tabId;
    }

    public static class TabModel
    {
        private String id;
        private Asset icon;
        private String title;

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public Asset getIcon()
        {
            return icon;
        }

        public void setIcon(Asset asset)
        {
            this.icon = asset;
        }

    }
}
