package fr.exanpe.t5.lib.internal;

import org.apache.tapestry5.services.ComponentMethodAdvice;
import org.apache.tapestry5.services.ComponentMethodInvocation;
import org.apache.tapestry5.services.RequestGlobals;

import fr.exanpe.t5.lib.annotation.Authorize;
import fr.exanpe.t5.lib.services.AuthorizeBusinessService;

/**
 * This class works with {@link AuthorizeWorker} and describe the rule to add to any method
 * containing the annotation {@link Authorize}
 * 
 * @author jmaupoux
 */
public class ComponentAuthorizeAdvice implements ComponentMethodAdvice
{
    private AuthorizeBusinessService authorizeBusinessService;
    private RequestGlobals requestGlobals;
    private String[] any;
    private String[] all;
    private String[] not;

    public ComponentAuthorizeAdvice(AuthorizeBusinessService abs, RequestGlobals requestGlobals, String[] any, String[] all, String[] not)
    {
        this.authorizeBusinessService = abs;
        this.requestGlobals = requestGlobals;
        this.any = any;
        this.all = all;
        this.not = not;
    }

    /*
     * (non-Javadoc)
     * @see
     * org.apache.tapestry5.services.ComponentMethodAdvice#advise(org.apache.tapestry5.services.
     * ComponentMethodInvocation)
     */
    public void advise(ComponentMethodInvocation invocation)
    {
        if (applyAny() && applyAll() && applyNot())
        {
            invocation.proceed();
        }
    }

    /**
     * Returns true if content to evaluate
     * 
     * @return true if content to evaluate
     */
    private boolean applyAny()
    {
        return authorizeBusinessService.applyAny(any, requestGlobals);
    }

    /**
     * Returns true if content to evaluate
     * 
     * @return true if content to evaluate
     */
    private boolean applyAll()
    {
        return authorizeBusinessService.applyAll(all, requestGlobals);
    }

    /**
     * Returns true if content to evaluate
     * 
     * @return true if content to evaluate
     */
    private boolean applyNot()
    {
        return authorizeBusinessService.applyNot(not, requestGlobals);
    }
}
