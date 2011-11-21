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

package fr.exanpe.t5.lib.model;

import java.util.LinkedList;
import java.util.List;

import org.apache.tapestry5.Asset;

import fr.exanpe.t5.lib.components.VerticalMenu;
import fr.exanpe.t5.lib.components.VerticalMenuItem;

/**
 * VerticalMenu internal model used to transmit data between the main menu and its various items.
 * 
 * @see VerticalMenu
 * @see VerticalMenuItem
 * @author lguerin
 */
public class VerticalMenuInternalModel
{
    private String menuId;
    private String selectedMenuItemId;
    private List<MenuItemModel> menuItems = new LinkedList<MenuItemModel>();

    public String getSelectedMenuItemId()
    {
        return selectedMenuItemId;
    }

    public void setSelectedMenuItemId(String selectedMenuItemId)
    {
        this.selectedMenuItemId = selectedMenuItemId;
    }

    public List<MenuItemModel> getMenuItems()
    {
        return menuItems;
    }

    public void setMenuItems(List<MenuItemModel> menuItems)
    {
        this.menuItems = menuItems;
    }

    public void addMenuItem(MenuItemModel model)
    {
        menuItems.add(model);
    }

    public String getMenuId()
    {
        return menuId;
    }

    public void setMenuId(String menuId)
    {
        this.menuId = menuId;
    }

    public static class MenuItemModel
    {
        private String id;
        private Asset icon;
        private String title;
        private String target;

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

        public String getTarget()
        {
            return target;
        }

        public void setTarget(String target)
        {
            this.target = target;
        }
    }
}
