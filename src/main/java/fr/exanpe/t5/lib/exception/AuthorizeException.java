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
