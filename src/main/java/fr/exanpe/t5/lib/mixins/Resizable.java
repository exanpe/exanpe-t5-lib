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

package fr.exanpe.t5.lib.mixins;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.components.Any;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

/**
 * This mixin allow the resizing of any HTML div.<br/>
 * To declare on a div, add a t:type="any" to the div tag.<br/>
 * Resize can be declared for any 8 sides (4 borders, 4 corners).<br/>
 * <br/>
 * You can access a YUI Resize on JavaScript side using its id with Exanpe.Resizable.${id}.
 * 
 * @since 1.3
 * @author jmaupoux
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/dragdrop/dragdrop-min.js", "${exanpe.yui2-base}/element/element-min.js",
        "${exanpe.yui2-base}/resize/resize-min.js", "${exanpe.asset-base}/js/exanpe-t5-lib.js" }, stylesheet =
{ "${exanpe.asset-base}/css/exanpe-t5-lib-core.css", "${exanpe.asset-base}/css/exanpe-t5-lib-skin.css" })
public class Resizable
{
    /**
     * Comma separated of sides that will be resizeabled.
     * 8 values are possible : t,r,b,l,tr,tl,br,bl (for top/right/bottom/left). Default is "b,br,r"
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "b,br,r", allowNull = false, required = true)
    private String sides;

    /**
     * Specify if the resizers are always visible or only visible on hover
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true", allowNull = false, required = true)
    private boolean alwaysVisible;

    /**
     * Javascript support
     */
    @Inject
    private JavaScriptSupport javascriptSupport;

    /**
     * Container
     */
    @InjectContainer
    private Any container;

    @AfterRender
    void end()
    {
        javascriptSupport.addInitializerCall("resizableBuilder", buildJSONData());
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();
        data.accumulate("id", container.getClientId());
        data.accumulate("alwaysVisible", alwaysVisible);

        JSONArray array = new JSONArray((Object[]) StringUtils.deleteWhitespace(sides).split(","));
        data.accumulate("sides", array);

        return data;
    }
}
