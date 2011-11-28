package fr.exanpe.t5.lib.services.impl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.tapestry5.services.RequestGlobals;

import fr.exanpe.t5.lib.services.AuthorizeBusinessService;

/**
 * Implementation of the interface {@link AuthorizeBusinessService}
 * 
 * @author jmaupoux
 */
public class AuthorizeBusinessServiceImpl implements AuthorizeBusinessService
{
    /*
     * (non-Javadoc)
     * @see fr.exanpe.t5.lib.services.AuthorizeBusinessService#applyAny(java.lang.String[],
     * org.apache.tapestry5.services.RequestGlobals)
     */
    public boolean applyAny(String[] any, RequestGlobals request)
    {
        if (ArrayUtils.isEmpty(any)) { return true; }

        String[] roles = any;
        for (String r : roles)
        {
            if (r != null && request.getHTTPServletRequest().isUserInRole(r.trim())) { return true; }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * @see fr.exanpe.t5.lib.services.AuthorizeBusinessService#applyAll(java.lang.String[],
     * org.apache.tapestry5.services.RequestGlobals)
     */
    public boolean applyAll(String[] all, RequestGlobals request)
    {
        if (ArrayUtils.isEmpty(all)) { return true; }

        String[] roles = all;
        for (String r : roles)
        {
            if (r != null && !request.getHTTPServletRequest().isUserInRole(r.trim())) { return false; }
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * @see fr.exanpe.t5.lib.services.AuthorizeBusinessService#applyNot(java.lang.String[],
     * org.apache.tapestry5.services.RequestGlobals)
     */
    public boolean applyNot(String[] not, RequestGlobals request)
    {
        if (ArrayUtils.isEmpty(not)) { return true; }

        String[] roles = not;
        for (String r : roles)
        {
            if (r != null && request.getHTTPServletRequest().isUserInRole(r.trim())) { return false; }
        }

        return true;
    }
}
