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

import fr.exanpe.t5.lib.model.VerticalMenuInternalModel;
import fr.exanpe.t5.lib.model.VerticalMenuInternalModel.MenuItemModel;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * This component represents an item displayed into {@link VerticalMenu}.<br/>
 * The content of the VerticalMenuItem will be filled by the body tag of the component.
 * 
 * @since 1.1
 * @see VerticalMenu
 * @author lguerin
 */
public class VerticalMenuItem implements ClientElement
{
    /**
     * Title of the menu
     */
    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.MESSAGE)
    private String title;

    /**
     * Icon of the menu
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.ASSET)
    private Asset icon;

    /**
     * Target of the menu link (_blank for example)
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String target;

    @Property
    private VerticalMenuInternalModel menuModel;

    @Property
    private String uniqueId;

    /**
     * Title suffix
     */
    private static final String TITLE_SUFFIX = "-title";

    /**
     * Icon suffix
     */
    private static final String ICON_SUFFIX = "-icon";

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

        menuModel = environment.peek(VerticalMenuInternalModel.class);
        if (menuModel == null) { throw new IllegalStateException("VerticalMenuItem component must be nested in a VerticalMenu"); }

        // fill in the model of this menu item
        MenuItemModel model = new MenuItemModel();
        model.setId(getClientId());
        model.setTitle(title);
        model.setIcon(icon);
        model.setTarget(target);

        menuModel.addMenuItem(model);

        log.debug("Registering vertical  menu item id:" + getClientId());
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

    public String getClientId()
    {
        return uniqueId;
    }
}
