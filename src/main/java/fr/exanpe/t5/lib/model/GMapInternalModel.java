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

import fr.exanpe.t5.lib.model.gmap.GMapMarkerModel;
import fr.exanpe.t5.lib.model.gmap.GMapPolyPointModel;

/**
 * GMap internal model to transmit data between items and map
 * 
 * @author lguerin
 */
public class GMapInternalModel
{
    /**
     * Map unique Id
     */
    private String mapId;

    /**
     * List of GMap Markers
     */
    private List<GMapMarkerModel> markers = new ArrayList<GMapMarkerModel>();

    /**
     * List of GMap Polyline points
     */
    private List<GMapPolyPointModel> polyPoints = new ArrayList<GMapPolyPointModel>();

    public List<GMapMarkerModel> getMarkers()
    {
        return markers;
    }

    public void setMarkers(List<GMapMarkerModel> markers)
    {
        this.markers = markers;
    }

    public List<GMapPolyPointModel> getPolyPoints()
    {
        return polyPoints;
    }

    public void setPolyPoints(List<GMapPolyPointModel> polyPoints)
    {
        this.polyPoints = polyPoints;
    }

    public void addMarker(GMapMarkerModel marker)
    {
        markers.add(marker);
    }

    public void addPolyPoint(GMapPolyPointModel point)
    {
        polyPoints.add(point);
    }

    public String getMapId()
    {
        return mapId;
    }

    public void setMapId(String mapId)
    {
        this.mapId = mapId;
    }
}
