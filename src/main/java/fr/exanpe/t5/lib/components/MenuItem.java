package fr.exanpe.t5.lib.components;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
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

import fr.exanpe.t5.lib.model.MenuInternalModel;
import fr.exanpe.t5.lib.model.MenuInternalModel.MenuRenderElement;
import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * This class defines a menu item.<br/>
 * Body can contain both raw content to display a label AND a {@link SubMenu} to allow a sub menu to
 * be
 * displayed.<br/>
 * Label can be specified through :
 * <ul>
 * <li>body content</li>
 * <li>label parameter</li>
 * <li>a property file with key named "${id}-label"</li>
 * </ul>
 * 
 * @since 1.1
 * @see MenuBar
 * @see SubMenu
 * @author jmaupoux
 */
public class MenuItem implements ClientElement
{

    /**
     * Specify the label of the item.
     */
    @Property
    @Parameter(required = false, defaultPrefix = BindingConstants.MESSAGE)
    private String label;

    /**
     * Specify the disabled state of the item.
     */
    @Property
    @Parameter(required = false, allowNull = false, defaultPrefix = BindingConstants.PROP, value = "false")
    private Boolean disabled;

    /**
     * Specify the rendered state of the item.
     */
    @Property
    @Parameter(required = false, allowNull = false, defaultPrefix = BindingConstants.PROP, value = "true")
    private Boolean visible;

    /**
     * Block to render depending on menu item location
     */
    @Property
    @SuppressWarnings("unused")
    private Block blockRendered;

    @Property
    private MenuInternalModel model;

    @Property
    private String uniqueId;

    /**
     * Set with real classname, if indeed disabled
     */
    @Property
    @SuppressWarnings("unused")
    private String disabledClass = "";

    /**
     * title suffix
     */
    private static final String LABEL_SUFFIX = "-label";

    /**
     * css class name for item disabled
     */
    private static final String CSS_ITEM_DISABLED = "yuimenuitem-disabled";

    /**
     * css class name for item bar disabled
     */
    private static final String CSS_ITEMBAR_DISABLED = "yuimenubaritem-disabled";

    /**
     * Block to render a menu item
     */
    @Inject
    private Block itemRender;

    /**
     * Block to render a menu bar item
     */
    @Inject
    private Block barItemRender;

    @Inject
    private Environment environment;

    @Inject
    private ExanpeComponentService exanpeService;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @Inject
    private ComponentResources resources;

    @Inject
    private Logger log;

    @SetupRender
    boolean init()
    {
        if (!visible) { return false; }

        uniqueId = javaScriptSupport.allocateClientId(resources);
        consolidateFromId();

        model = environment.peek(MenuInternalModel.class);
        if (model == null)
            throw new IllegalStateException("MenuBarItem component must be nested in a MenuBar");

        // render a baritem ?
        if (MenuRenderElement.ROOT.equals(model.getParent()))
        {
            blockRendered = barItemRender;
            if (disabled)
            {
                disabledClass = CSS_ITEMBAR_DISABLED;
            }
        }
        else
        {
            // render just a standard item ?
            blockRendered = itemRender;
            if (disabled)
            {
                disabledClass = CSS_ITEM_DISABLED;
            }
        }

        model.push(MenuRenderElement.MENUITEM);

        return true;
    }

    private void consolidateFromId()
    {
        String id = getClientId();

        if (StringUtils.isEmpty(label))
        {
            String labelKey = id + LABEL_SUFFIX;
            String label = exanpeService.getEscaladeMessage(resources, labelKey);

            log.debug("Checking label into resources file with key: {}", labelKey);

            if (StringUtils.isNotEmpty(label))
                this.label = label;
        }
    }

    @AfterRender
    void end()
    {
        model.endElement();
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
