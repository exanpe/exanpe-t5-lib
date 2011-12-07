package fr.exanpe.t5.lib.exception;

import fr.exanpe.t5.lib.annotation.Authorize;

/**
 * Exception thrown if an access denied detected by {@link Authorize} annotation occurs.<br/>
 * User and Page are recorded to allow fine-grained management.
 * 
 * @author jmaupoux
 */
public class AuthorizeException extends RuntimeException
{
    /**
     * serial uid
     */
    private static final long serialVersionUID = -6093448612924317357L;

    private String page;
    private String username;

    public AuthorizeException(String page, String username)
    {
        super("Illegal access to page " + page + " for user " + username);
        this.page = page;
        this.username = username;
    }

    /**
     * The page which access has been denied
     * 
     * @return the page which access has been denied
     */
    public String getPage()
    {
        return page;
    }

    /**
     * The username that initiated the exception
     * 
     * @return the username that initiated the exception
     */
    public String getUsername()
    {
        return username;
    }
}
