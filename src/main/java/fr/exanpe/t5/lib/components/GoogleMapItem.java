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
import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.model.GoogleMapInternalModel;
import fr.exanpe.t5.lib.model.GoogleMapInternalModel.GoogleMapItemModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * This component represents an item displayed into {@link GoogleMap} component.<br/>
 * It must be nested inside a {@link GoogleMap} component.<br/>
 * The title and icons can be gathered from a resource file. The keys have to be named :
 * The content of the item will be the description of the element.
 * 
 * @see GoogleMap
 * @author lguerin
 */
public class GoogleMapItem implements ClientElement
{
    /**
     * Specify the title of the item
     */
    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.MESSAGE)
    private String title;

    /**
     * Specify the icon of the item
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.ASSET)
    private Asset icon;

    /**
     * The position of the element, in Latitude and Longitude format (Ex. "")
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String position;

    /**
     * Info text of the item
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String info;

    /**
     * title suffix
     */
    private static final String TITLE_SUFFIX = "-title";

    /**
     * icon suffix
     */
    private static final String ICON_SUFFIX = "-icon";

    @Property
    private GoogleMapInternalModel viewModel;

    @Property
    private String uniqueId;

    @Property
    @SuppressWarnings("unused")
    private String cssClassName;

    @Inject
    private Logger log;

    @Inject
    private ComponentResources resources;

    @Inject
    private Environment environment;

    @Inject
    private AssetSource assetSource;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ExanpeComponentService exanpeService;

    @SetupRender
    void init()
    {
        uniqueId = javaScriptSupport.allocateClientId(resources);

        consolidateFromId();

        viewModel = environment.peek(GoogleMapInternalModel.class);
        if (viewModel == null)
            throw new IllegalStateException("GoogleMapItem component must be nested in a GoogleMap");

        // fill in the model
        GoogleMapItemModel model = new GoogleMapItemModel();
        model.setId(getClientId());
        model.setTitle(title);
        model.setIcon(icon);
        model.setPosition(position);
        model.setInfo(info);

        viewModel.addItem(model);
        log.debug("Registering Google map item id: {}", getClientId());
    }

    private void consolidateFromId()
    {
        if (StringUtils.isEmpty(title))
        {

            String titleKey = getClientId() + TITLE_SUFFIX;
            String message = exanpeService.getEscaladeMessage(resources, titleKey);

            log.debug("Checking title into resources file with key: {}", titleKey);

            if (StringUtils.isNotEmpty(message))
            {
                this.title = message;
            }
            else
            {
                throw new IllegalArgumentException("The resource key " + titleKey + " could not be found in order to process the item title " + getClientId());
            }
        }

        if (icon == null)
        {
            String iconKey = getClientId() + ICON_SUFFIX;
            String icon = exanpeService.getEscaladeMessage(resources, iconKey);

            log.debug("Checking icon into resources file with key: {}", iconKey);

            if (StringUtils.isNotEmpty(icon))
            {
                log.debug("Icon key found: {}", iconKey);
                this.icon = assetSource.getAsset(null, icon, null);
            }
            else
            {
                log.debug("Icon key not found: {}", iconKey);
            }
        }
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
