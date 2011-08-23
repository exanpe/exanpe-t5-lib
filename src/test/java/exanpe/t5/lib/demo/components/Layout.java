package exanpe.t5.lib.demo.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Minimal default Layout for Exanpe library demo pages.
 * 
 * @author lguerin
 */
public class Layout
{
    @Inject
    private ComponentResources resources;

    @SuppressWarnings("unused")
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String title;

    String defaulTitle()
    {
        return resources.getPageName();
    }

    /**
     * Dscard fields tagged by Persist
     */
    void onActionFromReset()
    {
        resources.discardPersistentFieldChanges();
    }
}
