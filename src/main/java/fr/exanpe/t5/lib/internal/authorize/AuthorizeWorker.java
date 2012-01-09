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

package fr.exanpe.t5.lib.internal.authorize;

import java.util.List;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.TransformMethod;

import fr.exanpe.t5.lib.annotation.Authorize;
import fr.exanpe.t5.lib.services.AuthorizeBusinessService;

/**
 * Worker applied on page invocation to control authorization.<br/>
 * This worker simply restrict access of pages along with the user authorizations
 * 
 * @author jmaupoux
 */
public class AuthorizeWorker implements ComponentClassTransformWorker
{
    private final AuthorizeBusinessService authorizeBusinessService;

    public AuthorizeWorker(AuthorizeBusinessService abs)
    {
        this.authorizeBusinessService = abs;
    }

    public void transform(ClassTransformation transformation, MutableComponentModel model)
    {
        final List<TransformMethod> methods = transformation.matchMethodsWithAnnotation(Authorize.class);

        if (methods.isEmpty())
            return;

        for (final TransformMethod method : methods)
        {
            Authorize annot = method.getAnnotation(Authorize.class);

            method.addAdvice(new ComponentAuthorizeAdvice(authorizeBusinessService, annot.any(), annot.all(), annot.not()));
        }
    }
}
