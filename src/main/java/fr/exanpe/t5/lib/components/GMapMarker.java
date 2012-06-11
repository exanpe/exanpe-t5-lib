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
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.runtime.RenderCommand;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.model.GMapInternalModel;

import fr.exanpe.t5.lib.model.gmap.GMapMarkerModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * This component represents a Marker displayed into a Google Map.<br/>
 * It must be nested inside a {@link GMap} component.<br/>
 * The title and icons can be gathered from a resource file.<br />
 * The keys have to be named :
 * <ul>
 * <li>${id}-title</li>
 * <li>${id}-info</li>
 * <li>${id}-icon</li>
 * </ul>
 * <br/>
 * 
 * @see GMap
 * @author lguerin
 * @since 1.2
 */
public class GMapMarker implements ClientElement
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
     * The latitude position of the marker
     */
    @Property
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String latitude;

    /**
     * The longitude position of the marker
     */
    @Property
    @Parameter(required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private String longitude;

    /**
     * Info text of the item
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private RenderCommand info;

    private String infoMarkup;

    /**
     * title suffix
     */
    private static final String TITLE_SUFFIX = "-title";

    /**
     * info suffix
     */
    private static final String INFO_SUFFIX = "-info";

    /**
     * icon suffix
     */
    private static final String ICON_SUFFIX = "-icon";

    @Property
    private GMapInternalModel gmapModel;

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
    Object setupRender(MarkupWriter writer)
    {
        if (info != null)
        {
            // create a wrapper div to render the info RenderCommand
            // this will be removed from the DOM after render and will
            // be used to initialise the gmap marker in javascript
            writer.element("div");
            return info;
        }
        return true;
    }

    @AfterRender
    void afterRender(MarkupWriter writer)
    {
        if (info != null)
        {
            Element wrapper = writer.getElement();
            writer.end();
            infoMarkup = wrapper.getChildMarkup();
            wrapper.remove();
        }
        uniqueId = javaScriptSupport.allocateClientId(resources);

        consolidateFromId();

        gmapModel = environment.peek(GMapInternalModel.class);
        if (gmapModel == null)
            throw new IllegalStateException("GMapMarker component must be nested in a GMap");

        // fill in the model
        GMapMarkerModel model = new GMapMarkerModel();
        model.setId(getClientId());
        model.setTitle(title);
        model.setIcon(icon);
        model.setLatitude(latitude);
        model.setLongitude(longitude);
        model.setInfo(infoMarkup);

        gmapModel.addMarker(model);
        log.debug("Registering Google map marker id: {}", getClientId());
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

        if (StringUtils.isEmpty(infoMarkup))
        {
            String infoKey = getClientId() + INFO_SUFFIX;
            String message = exanpeService.getEscaladeMessage(resources, infoKey);

            log.debug("Checking info into resources file with key: {}", infoKey);

            if (StringUtils.isNotEmpty(message))
            {
                this.infoMarkup = message;
            }
            else
            {
                log.debug("Info key not found: {}", infoKey);
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
