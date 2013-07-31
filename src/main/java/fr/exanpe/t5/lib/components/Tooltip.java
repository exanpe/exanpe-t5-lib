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
import org.apache.tapestry5.Block;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * A <code>Tooltip</code> component.<br />
 * The Tooltip component use different parameters to determine the message displayed to the user.<br />
 * First, we look at the "message" parameter; if message is bound, the content is displayed from
 * properties file.
 * Then, we look if "blockId" parameter is bound. If so, the content of the Tooltip is displayed
 * from the block identified with "blockId".
 * Finally, if neither the "message" parameter, nor "blockId" parameter is bound, by convention, we
 * use the message key consolidated from the tooltip id and the suffix "-tooltip".
 * JavaScript : This component is bound to a class Exanpe.Tooltip.<br/>
 * CSS : This component is bound to a class exanpe-tooltip.<br/>
 * 
 * @author lguerin
 */
@Import(stylesheet =
{ "css/exanpe-t5-lib-core.css", "${exanpe.asset-base}/css/exanpe-t5-lib-skin.css" })
public class Tooltip
{
    /**
     * Message used for displaying Tooltip content
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.MESSAGE, allowNull = false)
    private String message;

    /**
     * The id of the {@link Block} used for displaying Tooltip content
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, allowNull = false)
    private String blockId;

    @Property
    private boolean isBuildFromBlock;

    @Property
    private String uniqueId;

    @Property
    @SuppressWarnings("unused")
    private Block textBlock;

    /**
     * The default suffix used by convention to get content message of the tooltip from properties
     */
    private static final String TOOLTIP_DEFAULT_SUFFIX = "-tooltip";

    @Inject
    private ComponentResources resources;

    @Inject
    private ExanpeComponentService exanpeService;

    @Environmental
    private JavaScriptSupport javascriptSupport;

    @SetupRender
    void begin()
    {
        uniqueId = javascriptSupport.allocateClientId(resources);

        isBuildFromBlock = resources.isBound("blockId") ? true : false;
        if (isBuildFromBlock)
        {
            textBlock = resources.getContainer().getComponentResources().getBlock(blockId);
        }
    }

    @AfterRender
    void end()
    {
        JSONObject data = buildJSONData();
        javascriptSupport.require("exanpe/tooltip").invoke("init").with(data);
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();
        data.accumulate("id", getClientId());
        data.accumulate("context", getClientId());

        // Force tooltip text if message param is bound
        if (resources.isBound("message"))
        {
            data.accumulate("text", message);
        }

        if (!resources.isBound("blockId") && !resources.isBound("message"))
        {
            data.accumulate("text", exanpeService.getEscaladeMessage(resources, getClientId() + TOOLTIP_DEFAULT_SUFFIX));
        }
        return data;
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
