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

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.Asset;

/**
 * GoogleMap internal model to transmit data between items and map
 * 
 * @author lguerin
 */
public class GoogleMapInternalModel
{
    private String mapId;
    private List<GoogleMapItemModel> items = new ArrayList<GoogleMapItemModel>();

    public List<GoogleMapItemModel> getItems()
    {
        return items;
    }

    public void setItems(List<GoogleMapItemModel> items)
    {
        this.items = items;
    }

    public void addItem(GoogleMapItemModel model)
    {
        items.add(model);
    }

    public String getMapId()
    {
        return mapId;
    }

    public void setMapId(String mapId)
    {
        this.mapId = mapId;
    }

    public static class GoogleMapItemModel
    {
        private String id;
        private Asset icon;
        private String title;
        private String info;
        private String position;

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

        public String getInfo()
        {
            return info;
        }

        public void setInfo(String info)
        {
            this.info = info;
        }

        public String getPosition()
        {
            return this.position;
        }

        public void setPosition(String position)
        {
            this.position = position;
        }

    }
}
