package fr.exanpe.t5.lib.services;

import org.apache.tapestry5.services.RequestGlobals;

/**
 * This interface defines the services offered to all the library authorization elements
 * (component/mixin/worker).
 * 
 * @author jmaupoux
 */
public interface AuthorizeBusinessService
{
    /**
     * Apply the "any" rule
     * 
     * @param any the roles to restrict on
     * @param request the request service
     * @return true to grant access, false otherwise
     */
    public boolean applyAny(String[] any, RequestGlobals request);

    /**
     * Apply the "all" rule
     * 
     * @param any the roles to restrict on
     * @param request the request service
     * @return true to grant access, false otherwise
     */
    public boolean applyAll(String[] all, RequestGlobals request);

    /**
     * Apply the "any" rule
     * 
     * @param any the roles to restrict on
     * @param request the request service
     * @return true to grant access, false otherwise
     */
    public boolean applyNot(String[] not, RequestGlobals request);
}
