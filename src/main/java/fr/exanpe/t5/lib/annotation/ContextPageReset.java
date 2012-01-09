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

import fr.exanpe.t5.lib.constants.ExanpeSymbols;

/**
 * Annotation to mark a method as reseting the page depending on a marker sent in the context.
 * If {@link ExanpeSymbols#CONTEXT_PAGE_RESET_MARKER} is found in the request, every method
 * containing this annotation, or named "void contextReset()", will be triggered.<br/>
 * For Tapestry 5.3, a package protected method "void onActivate()" have to be defined in the class.
 * 
 * @author jmaupoux
 * @see ExanpeSymbols#CONTEXT_PAGE_RESET_MARKER
 * @since 1.2
 */
@Target(
{ ElementType.METHOD })
@Retention(RUNTIME)
@Documented
public @interface ContextPageReset
{

}
