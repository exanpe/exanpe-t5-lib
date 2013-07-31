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

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Flow;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.func.Predicate;
import org.apache.tapestry5.func.Worker;
import org.apache.tapestry5.internal.transform.PageResetAnnotationWorker;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.MethodAdvice;
import org.apache.tapestry5.plastic.MethodHandle;
import org.apache.tapestry5.plastic.MethodInvocation;
import org.apache.tapestry5.plastic.PlasticClass;
import org.apache.tapestry5.plastic.PlasticMethod;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.TransformConstants;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;

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
public class ContextPageResetWorker implements ComponentClassTransformWorker2
{

    private final RequestGlobals requestGlobals;
    private final String contextMarker;

    public ContextPageResetWorker(RequestGlobals requestGlobals, @Symbol(ExanpeSymbols.CONTEXT_PAGE_RESET_MARKER)
    String contextMarker, @Symbol(SymbolConstants.TAPESTRY_VERSION)
    String version)
    {
        this.requestGlobals = requestGlobals;
        this.contextMarker = contextMarker;
    }

    private final Mapper<PlasticMethod, MethodHandle> TO_HANDLE = new Mapper<PlasticMethod, MethodHandle>()
    {
        public MethodHandle map(PlasticMethod method)
        {
            return method.getHandle();
        }
    };

    private final Predicate<PlasticMethod> METHOD_MATCHER = new Predicate<PlasticMethod>()
    {
        public boolean accept(PlasticMethod method)
        {
            return method.getDescription().methodName.equalsIgnoreCase("contextReset") || method.hasAnnotation(ContextPageReset.class);
        }
    };

    public void transform(final PlasticClass plasticClass, TransformationSupport transformation, MutableComponentModel model)
    {
        Flow<PlasticMethod> methods = findResetMethods(plasticClass);

        if (methods.isEmpty())
            return;

        // add the cleanup on attach phase or on onActivate
        invokeMethodsOnPageReset(plasticClass, methods);
    }

    private void invokeMethodsOnPageReset(PlasticClass plasticClass, Flow<PlasticMethod> methods)
    {
        final MethodHandle[] handles = methods.map(TO_HANDLE).toArray(MethodHandle.class);

        plasticClass.introduceMethod(TransformConstants.CONTAINING_PAGE_DID_RESET_DESCRIPTION).addAdvice(new MethodAdvice()
        {

            private boolean matchContext()
            {
                return requestGlobals.getRequest().getPath().matches(".+/" + contextMarker + "($|[?/].*)");
            }

            public void advise(MethodInvocation invocation)
            {
                invocation.proceed();

                Object instance = invocation.getInstance();
                if (matchContext())
                {
                    for (MethodHandle handle : handles)
                    {
                        handle.invoke(instance);
                    }
                }
            }
        });
    }

    private final Worker<PlasticMethod> METHOD_VALIDATOR = new Worker<PlasticMethod>()
    {
        public void work(PlasticMethod method)
        {
            boolean valid = method.isVoid() && method.getParameters().isEmpty();

            if (!valid) { throw new RuntimeException(String.format(
                    "Method %s is invalid: methods with the @ContextPageReset annotation must return void, and have no parameters.",
                    method.getMethodIdentifier())); }
        }
    };

    private Flow<PlasticMethod> findResetMethods(PlasticClass plasticClass)
    {
        return F.flow(plasticClass.getMethods()).filter(METHOD_MATCHER).each(METHOD_VALIDATOR);
    }
}
