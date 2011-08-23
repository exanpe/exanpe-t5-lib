package fr.exanpe.t5.lib.services;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.dom.Element;

/**
 * Service for exanpe component
 * 
 * @author jmaupoux
 */
public class ExanpeComponentService
{
    private static final String CSS_CLASS_ATTRIBUTE = "class";

    public void reorderCSSClassDeclaration(Element e, String componentRootCssClass)
    {
        String customCSSClass = e.getAttribute(CSS_CLASS_ATTRIBUTE);

        if (StringUtils.isNotEmpty(customCSSClass))
        {
            String cssclasses = componentRootCssClass + " " + customCSSClass;
            e.forceAttributes(CSS_CLASS_ATTRIBUTE, cssclasses);
        }
        else
        {
            e.attribute(CSS_CLASS_ATTRIBUTE, componentRootCssClass);
        }
    }

    /**
     * Get a message by escalade from the resources as parameter
     * 
     * @param resources
     * @param key
     * @return the message, of null if not found
     */
    public String getEscaladeMessage(ComponentResources resources, String key)
    {
        if (resources == null)
            return null;
        if (!resources.getMessages().contains(key))
            return getEscaladeMessage(resources.getContainerResources(), key);
        return resources.getMessages().get(key);
    }
}
