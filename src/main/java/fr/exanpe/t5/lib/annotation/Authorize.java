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

package fr.exanpe.t5.lib.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import fr.exanpe.t5.lib.exception.AuthorizeException;

/**
 * Interface used to restrict a page's method invocation.<br/>
 * Simply annotate your method or class with \@Authorize and the restriction will be applied
 * depending on the current user authorities.<br/>
 * Note that on authorization failure, an {@link AuthorizeException} will be thrown. Catch it to
 * display a custom error page.
 * 
 * @see AuthorizeException
 * @author jmaupoux
 * @since 1.2
 */
@Target(
{ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
public @interface Authorize
{
    /**
     * Any of these authorizations grant access
     */
    String[] any() default
    {};

    /**
     * All of these authorizations are required to grant access
     */
    String[] all() default
    {};

    /**
     * None of these authorizations grant access
     */
    String[] not() default
    {};
}
