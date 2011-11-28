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
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.model.GoogleMapInternalModel;
import fr.exanpe.t5.lib.model.GoogleMapInternalModel.GoogleMapItemModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * GoogleMap component.
 * Displays a Google Map from some GoogleMapItem components declared in its body.<br/>
 * JavaScript : This component is bound to a class Exanpe.GoogleMap.<br/>
 * CSS : This component is bound to a class exanpe-gmap.<br/>
 * 
 * @see GoogleMapItem
 * @author lguerin
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/json/json-min.js", "js/exanpe-t5-lib.js" }, stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class GoogleMap implements ClientElement
{
    /**
     * Initial position used to center the map.
     * The position must be defined in Latitude/Longitude format (Ex. "48.869722,2.3075").
     */
    @Property
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String position;

    @Property
    private GoogleMapInternalModel model;

    /**
     * Current item looped by model
     */
    @Property
    private GoogleMapItemModel current;

    @Property
    private String uniqueId;

    /**
     * Root CSS class
     */
    private static final String ROOT_CSS_CLASS = "exanpe-gmap";

    /**
     * Google Map API
     */
    private static final String GOOGLE_MAP_API = "http://maps.googleapis.com/maps/api/js?sensor=true";

    @Inject
    private ComponentResources resources;

    @Inject
    private Environment environment;

    @Inject
    private JavaScriptSupport javascriptSupport;

    @Inject
    private ExanpeComponentService ecservice;

    @SetupRender
    void init()
    {
        uniqueId = javascriptSupport.allocateClientId(resources);
        model = new GoogleMapInternalModel();
        model.setMapId(uniqueId);

        if (environment.push(GoogleMapInternalModel.class, model) != null) { throw new IllegalStateException("Nested GoogleMap components are not supported"); }
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
        environment.pop(GoogleMapInternalModel.class);
        JSONObject data = buildJSONData(model);
        javascriptSupport.importJavaScriptLibrary(GOOGLE_MAP_API);
        javascriptSupport.addInitializerCall("googleMapBuilder", data);
    }

    private JSONObject buildJSONData(GoogleMapInternalModel model)
    {
        JSONArray gMapItems = new JSONArray();
        for (GoogleMapItemModel m : model.getItems())
        {
            JSONObject item = new JSONObject();
            item.accumulate("id", m.getId());
            item.accumulate("icon", m.getIcon() != null ? m.getIcon().toClientURL() : "");
            item.accumulate("position", m.getPosition());
            item.accumulate("title", m.getTitle());
            item.accumulate("info", m.getInfo());
            gMapItems.put(item);
        }
        JSONObject data = new JSONObject();
        data.accumulate("id", getClientId());
        data.accumulate("position", position);
        data.accumulate("items", gMapItems.toString());
        return data;
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
