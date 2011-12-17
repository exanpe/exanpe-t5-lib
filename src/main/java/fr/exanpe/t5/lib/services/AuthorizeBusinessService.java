package fr.exanpe.t5.lib.services;


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
     * @return true to grant access, false otherwise
     */
    public boolean applyAny(String[] any);

    /**
     * Apply the "all" rule
     * 
     * @param any the roles to restrict on
     * @return true to grant access, false otherwise
     */
    public boolean applyAll(String[] all);

    /**
     * Apply the "any" rule
     * 
     * @param any the roles to restrict on
     * @return true to grant access, false otherwise
     */
    public boolean applyNot(String[] not);
}
