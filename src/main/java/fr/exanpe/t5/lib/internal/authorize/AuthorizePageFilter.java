package fr.exanpe.t5.lib.internal.authorize;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

import org.apache.commons.lang.ClassUtils;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.ComponentSource;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.apache.tapestry5.services.RequestGlobals;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.annotation.Authorize;
import fr.exanpe.t5.lib.exception.AuthorizeException;
import fr.exanpe.t5.lib.services.AuthorizeBusinessService;

/**
 * This class handles the security for {@link Authorize} annotation declared at class level.<br/>
 * {@link AuthorizeException} is thrown on access denied.
 * 
 * @see AuthorizeException
 * @author jmaupoux
 */
public class AuthorizePageFilter implements ComponentRequestFilter
{
    private AuthorizeBusinessService authorizeBusinessService;
    private RequestGlobals requestGlobals;
    private ComponentSource componentSource;
    private Logger logger;

    public AuthorizePageFilter(AuthorizeBusinessService abs, RequestGlobals request, ComponentSource componentSource, Logger logger)
    {
        this.authorizeBusinessService = abs;
        this.requestGlobals = request;
        this.componentSource = componentSource;
        this.logger = logger;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.apache.tapestry5.services.ComponentRequestFilter#handleComponentEvent(org.apache.tapestry5
     * .services.ComponentEventRequestParameters,
     * org.apache.tapestry5.services.ComponentRequestHandler)
     */
    public void handleComponentEvent(ComponentEventRequestParameters parameters, ComponentRequestHandler handler) throws IOException
    {
        Authorize auth = process(parameters.getActivePageName());

        if (auth == null
                || (authorizeBusinessService.applyAll(auth.all()) && authorizeBusinessService.applyAny(auth.any()) && authorizeBusinessService.applyNot(auth
                        .not())))
        {
            handler.handleComponentEvent(parameters);
        }
        else
        {
            throwAuthorizeException(parameters.getActivePageName());
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * org.apache.tapestry5.services.ComponentRequestFilter#handlePageRender(org.apache.tapestry5
     * .services.PageRenderRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
     */
    public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler) throws IOException
    {
        Authorize auth = process(parameters.getLogicalPageName());

        if (auth == null
                || (authorizeBusinessService.applyAll(auth.all()) && authorizeBusinessService.applyAny(auth.any()) && authorizeBusinessService.applyNot(auth
                        .not())))
        {
            handler.handlePageRender(parameters);
        }
        else
        {
            throwAuthorizeException(parameters.getLogicalPageName());
        }
    }

    private void throwAuthorizeException(String page)
    {
        logger.debug("Illegal access to page {} for user {}", page, getUsername());
        throw new AuthorizeException(page, getUsername());
    }

    private String getUsername()
    {
        Principal p = requestGlobals.getHTTPServletRequest().getUserPrincipal();
        if (p == null) { return "-"; }
        return p.getName();
    }

    /**
     * Look for an {@link Authorize} annotation in the Page class or superclass
     * 
     * @param pageName the name of the page
     * @return the annotation found, or null
     */
    @SuppressWarnings("unchecked")
    private Authorize process(String pageName)
    {
        Component page = componentSource.getPage(pageName);

        Authorize auth = null;

        if ((auth = process(page.getClass())) != null) { return auth; }

        // handle inheritance
        for (Class<?> c : (List<Class<?>>) ClassUtils.getAllSuperclasses(page.getClass()))
        {
            if ((auth = process(c)) != null) { return auth; }
        }

        // handle inheritance
        for (Class<?> c : (List<Class<?>>) ClassUtils.getAllInterfaces(page.getClass()))
        {
            if ((auth = process(c)) != null) { return auth; }
        }

        return auth;
    }

    /**
     * Return the {@link Authorize} annotation if found
     * 
     * @param clazz the class to look for
     * @return the annotation, or null
     */
    private Authorize process(Class<?> clazz)
    {
        if (!clazz.isAnnotationPresent(Authorize.class)) { return null; }

        return clazz.getAnnotation(Authorize.class);
    }
}
