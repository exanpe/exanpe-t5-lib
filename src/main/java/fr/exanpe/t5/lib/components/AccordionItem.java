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
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.model.AccordionInternalModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * AccordionItem component. Must be nested inside a Accordion component.<br/>
 * Initialized either by setting its content and title or by defining an id processed in the
 * container resources file.<br/>
 * If initialized through a resources file (.properties), the following key patterns will be
 * automatically checked :<br/>
 * <ul>
 * <li>${id}-title</li>
 * <li>${id}-content</li>
 * </ul>
 * <br/>
 * The content may also be the body of this item.
 * 
 * @see Accordion
 * @author jmaupoux
 */
public class AccordionItem implements ClientElement
{

    /**
     * CSS state opened
     */
    private static final String OPENED = "opened";

    /**
     * CSS state closed
     */
    private static final String CLOSED = "closed";

    /**
     * title suffix
     */
    private static final String TITLE_SUFFIX = "-title";

    /**
     * content suffix
     */
    private static final String CONTENT_SUFFIX = "-content";

    /**
     * The title of the item. May be initialized through a resource file.
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.MESSAGE)
    private String title;

    /**
     * The content of the item. May be initialized through a resource file.
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.MESSAGE)
    @SuppressWarnings("unused")
    private String content;

    /**
     * The default state of the item.
     */
    @Property
    @Parameter(value = "false", defaultPrefix = BindingConstants.LITERAL)
    private Boolean opened;

    @Property
    private String uniqueId;

    /**
     * the default CSS class name of the item
     */
    @Property
    @SuppressWarnings("unused")
    private String defaultClassName;

    @Inject
    private ComponentResources resources;

    @Inject
    private Environment environment;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ExanpeComponentService exanpeService;

    @Inject
    private Logger log;

    @SetupRender
    void init()
    {
        uniqueId = javaScriptSupport.allocateClientId(resources);
        consolidateFromId();

        if (opened)
            defaultClassName = OPENED;
        else
            defaultClassName = CLOSED;
    }

    private void consolidateFromId()
    {
        String id = getClientId();

        if (StringUtils.isEmpty(title))
        {
            String titleKey = id + TITLE_SUFFIX;
            String title = exanpeService.getEscaladeMessage(resources, titleKey);

            log.debug("Checking title into resources file with key:" + titleKey);

            if (StringUtils.isNotEmpty(title))
                this.title = title;
            else
                throw new IllegalArgumentException("The resource key " + titleKey + " could not be found in order to process the accordion item " + id);
        }

        String contentKey = id + CONTENT_SUFFIX;
        String content = exanpeService.getEscaladeMessage(resources, contentKey);

        log.debug("Checking content into resources file with key:" + contentKey);

        if (StringUtils.isNotEmpty(content))
            this.content = content;
    }

    @AfterRender
    void record()
    {
        log.debug("Recording item id :" + getClientId() + " to Accordion");
        environment.peek(AccordionInternalModel.class).getItemsId().add(getClientId());
    }

    public String getClientId()
    {
        return uniqueId;
    }

}
