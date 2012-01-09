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

package fr.exanpe.t5.lib.services.impl;

import org.apache.commons.lang.ArrayUtils;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import fr.exanpe.t5.lib.services.AuthorizeBusinessService;

/**
 * Implementation of the interface {@link AuthorizeBusinessService}
 * 
 * @author jmaupoux
 */
public class AuthorizeBusinessServiceImpl implements AuthorizeBusinessService
{
    @Inject
    private RequestGlobals request;

    /*
     * (non-Javadoc)
     * @see fr.exanpe.t5.lib.services.AuthorizeBusinessService#applyAny(java.lang.String[])
     */
    public boolean applyAny(String[] any)
    {
        if (ArrayUtils.isEmpty(any)) { return true; }

        String[] roles = any;
        for (String r : roles)
        {
            if (r != null && request.getHTTPServletRequest().isUserInRole(r.trim())) { return true; }
        }

        return false;
    }

    /*
     * (non-Javadoc)
     * @see fr.exanpe.t5.lib.services.AuthorizeBusinessService#applyAll(java.lang.String[])
     */
    public boolean applyAll(String[] all)
    {
        if (ArrayUtils.isEmpty(all)) { return true; }

        String[] roles = all;
        for (String r : roles)
        {
            if (r != null && !request.getHTTPServletRequest().isUserInRole(r.trim())) { return false; }
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * @see fr.exanpe.t5.lib.services.AuthorizeBusinessService#applyNot(java.lang.String[])
     */
    public boolean applyNot(String[] not)
    {
        if (ArrayUtils.isEmpty(not)) { return true; }

        String[] roles = not;
        for (String r : roles)
        {
            if (r != null && request.getHTTPServletRequest().isUserInRole(r.trim())) { return false; }
        }

        return true;
    }
}
