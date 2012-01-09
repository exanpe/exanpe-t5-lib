//
// Copyright 2011 EXANPE <exanpe@gmail.com>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package fr.exanpe.t5.lib.internal.contextpagereset;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.internal.InternalConstants;
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
    private final String version;

    public ContextPageResetWorker(RequestGlobals requestGlobals, @Symbol(ExanpeSymbols.CONTEXT_PAGE_RESET_MARKER)
    String contextMarker, @Symbol(SymbolConstants.TAPESTRY_VERSION)
    String version)
    {
        this.requestGlobals = requestGlobals;
        this.contextMarker = contextMarker;
        this.version = version;
    }

    private static final TransformMethodSignature ON_ACTIVATE_SIGNATURE = new TransformMethodSignature(0x00000000, "void", "onActivate",
            InternalConstants.EMPTY_STRING_ARRAY, InternalConstants.EMPTY_STRING_ARRAY);

    private static final TransformMethodSignature ON_CONTEXT_PAGE_RESET_SIGNATURE = new TransformMethodSignature(0x00000000, "void", "contextReset",
            InternalConstants.EMPTY_STRING_ARRAY, InternalConstants.EMPTY_STRING_ARRAY);

    public void transform(final ClassTransformation transformation, MutableComponentModel model)
    {
        List<TransformMethod> methods = matchPageResetMethods(transformation);

        if (methods.isEmpty())
            return;

        // add the cleanup on attach phase or on onActivate
        addContextPageResetToContainingPageDidAttachMethod(transformation, methods);
    }

    private void addContextPageResetToContainingPageDidAttachMethod(ClassTransformation transformation, List<TransformMethod> methods)
    {
        List<MethodAccess> methodAccess = convertToMethodAccess(methods);

        ComponentInstanceOperation advice = createMethodAccessAdvice(methodAccess);

        if (isv52x())
        {
            // after attach
            transformation.getOrCreateMethod(TransformConstants.CONTAINING_PAGE_DID_ATTACH_SIGNATURE).addOperationAfter(advice);
        }
        else
        {
            // or before onActivate
            transformation.getOrCreateMethod(ON_ACTIVATE_SIGNATURE).addOperationBefore(advice);
        }
    }

    private final boolean isv52x()
    {
        return version.startsWith("5.2");
    }

    private ComponentInstanceOperation createMethodAccessAdvice(final List<MethodAccess> methodAccess)
    {
        return new ComponentInstanceOperation()
        {

            private boolean matchContext()
            {
                return requestGlobals.getRequest().getPath().matches(".+/" + contextMarker + "($|[?/].*)");
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
        List<TransformMethod> result = new ArrayList<TransformMethod>(transformation.matchMethodsWithAnnotation(ContextPageReset.class));

        if (transformation.isDeclaredMethod(ON_CONTEXT_PAGE_RESET_SIGNATURE))
        {
            for (TransformMethod t : result)
            {
                if (t.getSignature().equals(ON_CONTEXT_PAGE_RESET_SIGNATURE)) { return result; }
            }
            result.add(transformation.getOrCreateMethod(ON_CONTEXT_PAGE_RESET_SIGNATURE));
        }

        return result;
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
