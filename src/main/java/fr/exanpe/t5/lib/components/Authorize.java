package fr.exanpe.t5.lib.components;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

/**
 * This component displays its body/block according to authorizations rules declared.<br/>
 * All rules will be applied if provided. If one fails, the body/block won't be rendered.<br/>
 * any/all/not must be filled with comma separated roles, as set into your session principal.<br/>
 * For easier maintenance, we advise you to fill only one of any/all/not parameter.
 * 
 * @author jmaupoux
 */
public class Authorize
{
    /**
     * Comma separated role values
     * Any of these roles are required to allow rendering
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String any;

    /**
     * Comma separated role values
     * All of these roles are required to allow rendering
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String all;

    /**
     * Comma separated role values
     * None of these roles are required to allow rendering (if one is present in the session, no
     * rendering)
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String not;

    /**
     * Block to render authorization. If not provided, the body of the component is rendered.
     */
    @SuppressWarnings("unused")
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private Block block;

    /**
     * requestGlobals
     */
    @Inject
    private RequestGlobals requestGlobals;

    /**
     * Returns true if content to evaluate
     * 
     * @return true if content to evaluate
     */
    @SetupRender
    boolean init()
    {
        System.out.println(requestGlobals.getHTTPServletRequest().isUserInRole("ROLE_USER"));
        return applyAny() && applyAll() && applyNot();
    }

    /**
     * Returns true if content to evaluate
     * 
     * @return true if content to evaluate
     */
    private boolean applyAny()
    {
        if (StringUtils.isEmpty(any)) { return true; }

        String[] roles = any.split(",");
        for (String r : roles)
        {
            if (requestGlobals.getHTTPServletRequest().isUserInRole(r.trim())) { return true; }
        }

        return false;
    }

    /**
     * Returns true if content to evaluate
     * 
     * @return true if content to evaluate
     */
    private boolean applyAll()
    {
        if (StringUtils.isEmpty(all)) { return true; }

        String[] roles = all.split(",");
        for (String r : roles)
        {
            if (!requestGlobals.getHTTPServletRequest().isUserInRole(r.trim())) { return false; }
        }

        return true;
    }

    /**
     * Returns true if content to evaluate
     * 
     * @return true if content to evaluate
     */
    private boolean applyNot()
    {
        if (StringUtils.isEmpty(not)) { return true; }

        String[] roles = not.split(",");
        for (String r : roles)
        {
            if (requestGlobals.getHTTPServletRequest().isUserInRole(r.trim())) { return false; }
        }

        return true;
    }

}
