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

import org.apache.tapestry5.services.ComponentMethodAdvice;
import org.apache.tapestry5.services.ComponentMethodInvocation;

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
    private String[] any;
    private String[] all;
    private String[] not;

    public ComponentAuthorizeAdvice(AuthorizeBusinessService abs, String[] any, String[] all, String[] not)
    {
        this.authorizeBusinessService = abs;
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
        return authorizeBusinessService.applyAny(any);
    }

    /**
     * Returns true if content to evaluate
     * 
     * @return true if content to evaluate
     */
    private boolean applyAll()
    {
        return authorizeBusinessService.applyAll(all);
    }

    /**
     * Returns true if content to evaluate
     * 
     * @return true if content to evaluate
     */
    private boolean applyNot()
    {
        return authorizeBusinessService.applyNot(not);
    }
}
