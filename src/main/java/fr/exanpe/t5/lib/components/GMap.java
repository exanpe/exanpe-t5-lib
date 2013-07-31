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

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.constants.ExanpeSymbols;
import fr.exanpe.t5.lib.constants.GMapTypeEnum;
import fr.exanpe.t5.lib.model.GMapInternalModel;
import fr.exanpe.t5.lib.model.gmap.GMapMarkerModel;
import fr.exanpe.t5.lib.model.gmap.GMapPolyPointModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * GoogleMap component.<br />
 * Displays a Google Map centered with latitude and longitude positions provided by user.<br />
 * {@link GMapMarker} and/or {@link GMapPolyPoint} components can be nested in its body in order to
 * display custom markers or draw polylines or polygons.<br />
 * <p>
 * JavaScript : This component is bound to a class Exanpe.GMap.<br/>
 * CSS : This component is bound to a class exanpe-gmap.<br/>
 * </p>
 * 
 * @since 1.2
 * @see GMapMarker
 * @see GMapPolyPoint
 * @author lguerin
 */
@Import(stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class GMap implements ClientElement
{
    /**
     * Initial latitude used to center the map.
     */
    @Property
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String latitude;

    /**
     * Initial longitude used to center the map.
     */
    @Property
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String longitude;

    /**
     * Specify the type of map you want to display. Default is ROADMAP.
     * 
     * @see GMapTypeEnum
     */
    @Parameter(value = "ROADMAP", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private GMapTypeEnum mapType;

    /**
     * Initial Map zoom level
     */
    @Parameter(value = "15", allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private Integer zoom;

    /**
     * If false, prevents the map from being dragged. Default value is true.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean draggable;

    /**
     * If true, displays a side bar with marker links. Default is false.
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean sidebar;

    /**
     * Used with {@link GMapPolyPoint} component in order to specify the hexadecimal HTML color of
     * a line.
     * Default is black (#000);
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "#000")
    private String polyStrokeColor;

    /**
     * Used with {@link GMapPolyPoint} component in order to specify numerical fractional value
     * between 0.0 and 1.0 of the opacity of the line's color.
     * Default is 1.0
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "1.0")
    private float polyStrokeOpacity;

    /**
     * Used with {@link GMapPolyPoint} component in order to specify the weight of the line's
     * stroke in pixels.
     * Default is 3.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "3")
    private Integer polyStrokeWeight;

    /**
     * Used with {@link GMapPolyPoint} component in order to draw a Javascript GMap Polygon instead
     * of a Polyline.
     * Default is false.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean polygon;

    /**
     * Loading Google Maps API over HTTPS or not.
     * Default is false.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "false")
    private Boolean secure;

    @Property
    private GMapInternalModel model;

    /**
     * Current marker looped by model
     */
    @Property
    private GMapMarkerModel current;

    @Property
    private String uniqueId;

    /**
     * Root CSS class
     */
    private static final String ROOT_CSS_CLASS = "exanpe-gmap";

    /**
     * Google Maps API
     */
    private static final String GOOGLE_MAP_API_BASE_URL = "http://maps.googleapis.com/maps/api/js?";

    @Inject
    @Symbol(ExanpeSymbols.GMAP_V3_BUSINESS_CLIENT_ID)
    private String gmapClientId;

    @Inject
    @Symbol(ExanpeSymbols.GMAP_V3_VERSION)
    private String gmapApiVersion;

    @Inject
    private ComponentResources resources;

    @Inject
    private Environment environment;

    @Inject
    private JavaScriptSupport javascriptSupport;

    @Inject
    private ExanpeComponentService ecservice;

    @Inject
    private Logger log;

    @SetupRender
    void init()
    {
        uniqueId = javascriptSupport.allocateClientId(resources);
        model = new GMapInternalModel();
        model.setMapId(uniqueId);

        if (environment.push(GMapInternalModel.class, model) != null) { throw new IllegalStateException("Nested GMap components are not supported"); }
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
        environment.pop(GMapInternalModel.class);
        JSONObject data = buildJSONData(model);
        javascriptSupport.importJavaScriptLibrary(this.buildGMapApiUrl());
        javascriptSupport.require("exanpe/gMap").invoke("init").with(data);
    }

    private JSONObject buildJSONData(GMapInternalModel model)
    {
        // Markers
        JSONArray gMapMarkers = buildJSONMarkersArray();

        // Polyline points
        JSONArray gMapPolyPoints = buildJSONPolyPointsArray();

        JSONObject data = new JSONObject();
        data.accumulate("id", getClientId());
        data.accumulate("latitude", latitude);
        data.accumulate("longitude", longitude);
        data.accumulate("mapType", mapType.toString());
        data.accumulate("zoom", zoom);
        data.accumulate("draggable", draggable);
        data.accumulate("markers", gMapMarkers.toString());
        data.accumulate("polyPoints", gMapPolyPoints.toString());
        data.accumulate("polyStrokeColor", polyStrokeColor);
        data.accumulate("polyStrokeOpacity", polyStrokeOpacity);
        data.accumulate("polyStrokeWeight", polyStrokeWeight);
        data.accumulate("polygon", polygon);
        return data;
    }

    private JSONArray buildJSONMarkersArray()
    {
        JSONArray gMapMarkers = new JSONArray();
        for (GMapMarkerModel m : model.getMarkers())
        {
            JSONObject marker = new JSONObject();
            marker.accumulate("id", m.getId());
            marker.accumulate("icon", m.getIcon() != null ? m.getIcon().toClientURL() : "");
            marker.accumulate("latitude", m.getLatitude());
            marker.accumulate("longitude", m.getLongitude());
            marker.accumulate("title", m.getTitle());
            marker.accumulate("info", m.getInfo());
            gMapMarkers.put(marker);
        }
        return gMapMarkers;
    }

    private JSONArray buildJSONPolyPointsArray()
    {
        JSONArray gMapPolyPoints = new JSONArray();
        for (GMapPolyPointModel m : model.getPolyPoints())
        {
            JSONObject point = new JSONObject();
            point.accumulate("id", m.getId());
            point.accumulate("latitude", m.getLatitude());
            point.accumulate("longitude", m.getLongitude());
            gMapPolyPoints.put(point);
        }
        return gMapPolyPoints;
    }

    private String buildGMapApiUrl()
    {
        String url = GOOGLE_MAP_API_BASE_URL;

        // GMap version
        if (!gmapApiVersion.matches("^3(\\.[0-9]{1})?$")) { throw new RuntimeException(
                String.format("The GMap API version: %s is not correct.", gmapApiVersion)); }
        url += "v=" + gmapApiVersion;

        // GMap business client ID
        if (!StringUtils.isEmpty(gmapClientId))
        {
            if (!StringUtils.startsWith(gmapClientId, "gme-")) { throw new RuntimeException(String.format(
                    "Your GMap clientID: %s is not correct, it must start with 'gme-'.",
                    gmapClientId)); }
            url += "&client=" + gmapClientId;
        }

        // Sensor parameter
        url += "&sensor=true";

        // Secure access
        if (secure)
        {
            url = url.replace("http", "https");
        }

        log.debug("Google Maps API URL: {}", url);
        return url;
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
