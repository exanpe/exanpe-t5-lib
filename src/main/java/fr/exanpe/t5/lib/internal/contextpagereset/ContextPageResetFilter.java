package fr.exanpe.t5.lib.internal.contextpagereset;

import java.io.IOException;

import org.apache.commons.lang.ArrayUtils;
import org.apache.tapestry5.internal.URLEventContext;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.ComponentEventRequestParameters;
import org.apache.tapestry5.services.ComponentRequestFilter;
import org.apache.tapestry5.services.ComponentRequestHandler;
import org.apache.tapestry5.services.ContextValueEncoder;
import org.apache.tapestry5.services.PageRenderRequestParameters;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.constants.ExanpeSymbols;

/**
 * This filter is used to intercept the requests sent to the page and remove the
 * {@link ExanpeSymbols#CONTEXT_PAGE_RESET_MARKER} context.<br/>
 * 
 * @author jmaupoux
 * @since 1.2
 */
public class ContextPageResetFilter implements ComponentRequestFilter
{

    private final ContextValueEncoder contextValueEncoder;
    private final Logger logger;
    private final String contextMarker;

    public ContextPageResetFilter(ContextValueEncoder contextValueEncoder, Logger logger, @Symbol(ExanpeSymbols.CONTEXT_PAGE_RESET_MARKER)
    String contextMarker)
    {
        this.contextValueEncoder = contextValueEncoder;
        this.logger = logger;
        this.contextMarker = contextMarker;
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
        handler.handleComponentEvent(parameters);
    }

    /*
     * (non-Javadoc)
     * @see
     * org.apache.tapestry5.services.ComponentRequestFilter#handlePageRender(org.apache.tapestry5
     * .services.PageRenderRequestParameters, org.apache.tapestry5.services.ComponentRequestHandler)
     */
    public void handlePageRender(PageRenderRequestParameters parameters, ComponentRequestHandler handler) throws IOException
    {
        PageRenderRequestParameters p = parameters;
        if (parameters.getActivationContext() != null && parameters.getActivationContext().toStrings() != null
                && parameters.getActivationContext().toStrings().length > 0
                && ArrayUtils.contains(parameters.getActivationContext().toStrings(), contextMarker))
        {
            if (logger.isDebugEnabled())
            {
                logger.debug(
                        "Context page reset marker {} has been found for page activation {} and will be removed.",
                        contextMarker,
                        parameters.getActivationContext());
            }
            URLEventContext old = (URLEventContext) parameters.getActivationContext();
            p = new PageRenderRequestParameters(parameters.getLogicalPageName(), new URLEventContext(contextValueEncoder, (String[]) ArrayUtils.removeElement(
                    old.toStrings(),
                    contextMarker)), false);
        }

        handler.handlePageRender(p);
    }
}
