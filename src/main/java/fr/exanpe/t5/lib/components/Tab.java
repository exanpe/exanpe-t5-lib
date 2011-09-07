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
import org.apache.tapestry5.Block;
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

import fr.exanpe.t5.lib.model.TabViewInternalModel;
import fr.exanpe.t5.lib.model.TabViewInternalModel.TabModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * This component represents a Tab.<br/>
 * It must be nested inside a {@link TabView} component.<br/>
 * The title and icons can be gathered from a resource file. The keys have to be named :
 * <ul>
 * <li>${id}-title.</li>
 * <li>${id}-icon.</li>
 * </ul>
 * <br/>
 * The content of the tab will be the body of the tag.
 * 
 * @see TabView
 * @author jmaupoux
 */
public class Tab implements ClientElement
{

    /**
     * Specify the title of the tab
     */
    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.MESSAGE)
    private String title;

    /**
     * Specify the icon of the tab
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.ASSET)
    private Asset icon;

    @Property
    @SuppressWarnings("unused")
    private Block renderedBlock;

    /**
     * Whether the tab content is loaded
     */
    @Property
    private Boolean load;

    @Property
    private TabViewInternalModel viewModel;

    @Property
    private String uniqueId;

    @Property
    @SuppressWarnings("unused")
    private String cssClassName;

    /**
     * title suffix
     */
    private static final String TITLE_SUFFIX = "-title";

    /**
     * icon suffix
     */
    private static final String ICON_SUFFIX = "-icon";

    /**
     * The CSS class name for an hidden tab
     */
    private static final String CSS_TAB_HIDDEN = "exanpe-tab-hidden";

    @Inject
    private Logger log;

    @Inject
    private Block loadBlock;

    @Inject
    private Block noLoadBlock;

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

    /**
     * Whether the tab is selected
     */
    private boolean selected;

    @SetupRender
    void init()
    {
        uniqueId = javaScriptSupport.allocateClientId(resources);

        consolidateFromId();

        viewModel = environment.peek(TabViewInternalModel.class);
        if (viewModel == null)
            throw new IllegalStateException("Tab component must be nested in a TabView");

        // fill in the model of this tab
        TabModel model = new TabModel();
        model.setId(getClientId());
        model.setTitle(title);
        model.setIcon(icon);

        viewModel.addTab(model);

        log.debug("Registering tab id:" + getClientId());

        updateRenderValues();
    }

    private void consolidateFromId()
    {
        if (StringUtils.isEmpty(title))
        {

            String titleKey = getClientId() + TITLE_SUFFIX;
            String message = exanpeService.getEscaladeMessage(resources, titleKey);

            log.debug("Checking title into resources file with key:" + titleKey);

            if (StringUtils.isNotEmpty(message))
            {
                this.title = message;
            }
            else
            {
                throw new IllegalArgumentException("The resource key " + titleKey + " could not be found in order to process the tab title " + getClientId());
            }
        }

        if (icon == null)
        {
            String iconKey = getClientId() + ICON_SUFFIX;
            String icon = exanpeService.getEscaladeMessage(resources, iconKey);

            log.debug("Checking icon into resources file with key:" + iconKey);

            if (StringUtils.isNotEmpty(icon))
            {
                log.debug("Icon key found:" + iconKey);
                this.icon = assetSource.getAsset(null, icon, null);
            }
            else
            {
                log.debug("Icon key not found:" + iconKey);
            }
        }
    }

    private void updateRenderValues()
    {
        selected = viewModel.getSelectedTabId().equals(getClientId());
        // should we preload according to configuration ?
        load = selected || viewModel.isLoadAll();
        if (load)
            renderedBlock = loadBlock;
        else
            renderedBlock = noLoadBlock;

        cssClassName = selected ? "" : CSS_TAB_HIDDEN;
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
