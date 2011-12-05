package fr.exanpe.t5.lib.internal.contextpagereset;

import java.util.List;

import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.internal.transform.PageResetAnnotationWorker;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.runtime.Component;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.ComponentInstanceOperation;
import org.apache.tapestry5.services.MethodAccess;
import org.apache.tapestry5.services.MethodInvocationResult;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.TransformConstants;
import org.apache.tapestry5.services.TransformMethod;
import org.apache.tapestry5.services.TransformMethodSignature;

import fr.exanpe.t5.lib.annotation.ContextPageReset;
import fr.exanpe.t5.lib.constants.ExanpeSymbols;

/**
 * This Worker is used to trigger the {@link ContextPageReset} method depending on a marker in the
 * URL context.
 * It is a derived implementation of {@link PageResetAnnotationWorker} except that every cleanup
 * decision is up to the context match.
 * The context marker is the one defined by {@link ExanpeSymbols#CONTEXT_PAGE_RESET_MARKER}
 * 
 * @see ContextPageReset
 * @since 1.2
 * @author jmaupoux
 */
public class ContextPageResetWorker implements ComponentClassTransformWorker
{

    private final RequestGlobals requestGlobals;
    private final String contextMarker;

    public ContextPageResetWorker(RequestGlobals requestGlobals, @Symbol(ExanpeSymbols.CONTEXT_PAGE_RESET_MARKER)
    String contextMarker)
    {
        this.requestGlobals = requestGlobals;
        this.contextMarker = contextMarker;
    }

    public void transform(final ClassTransformation transformation, MutableComponentModel model)
    {
        List<TransformMethod> methods = matchPageResetMethods(transformation);

        if (methods.isEmpty())
            return;

        // add the cleanup on attach phase
        addContextPageResetToContainingPageDidAttachMethod(transformation, methods);
    }

    private void addContextPageResetToContainingPageDidAttachMethod(ClassTransformation transformation, List<TransformMethod> methods)
    {
        List<MethodAccess> methodAccess = convertToMethodAccess(methods);

        ComponentInstanceOperation advice = createMethodAccessAdvice(methodAccess);

        transformation.getOrCreateMethod(TransformConstants.CONTAINING_PAGE_DID_ATTACH_SIGNATURE).addOperationAfter(advice);
    }

    private ComponentInstanceOperation createMethodAccessAdvice(final List<MethodAccess> methodAccess)
    {
        return new ComponentInstanceOperation()
        {

            private boolean matchContext()
            {
                return requestGlobals.getRequest().getPath().matches(".+/" + contextMarker + "($|[ ?].*)");
            }

            public void invoke(Component instance)
            {
                if (matchContext())
                {
                    for (MethodAccess access : methodAccess)
                    {
                        MethodInvocationResult result = access.invoke(instance);

                        result.rethrow();
                    }
                }
            }
        };
    }

    private List<MethodAccess> convertToMethodAccess(List<TransformMethod> methods)
    {
        List<MethodAccess> result = CollectionFactory.newList();

        for (TransformMethod method : methods)
        {
            result.add(toMethodAccess(method));
        }

        return result;
    }

    private List<TransformMethod> matchPageResetMethods(final ClassTransformation transformation)
    {
        return transformation.matchMethods(new Predicate<TransformMethod>()
        {
            public boolean accept(TransformMethod method)
            {
                return method.getName().equalsIgnoreCase("contextPageReset") || method.getAnnotation(ContextPageReset.class) != null;
            }
        });
    }

    private MethodAccess toMethodAccess(TransformMethod method)
    {
        TransformMethodSignature sig = method.getSignature();

        boolean valid = sig.getParameterTypes().length == 0 && sig.getReturnType().equals("void");

        if (!valid)
            throw new RuntimeException(String.format(
                    "Method %s is invalid: methods with the @ContextPageReset annotation must return void, and have no parameters.",
                    method.getMethodIdentifier()));

        return method.getAccess();
    }
}
