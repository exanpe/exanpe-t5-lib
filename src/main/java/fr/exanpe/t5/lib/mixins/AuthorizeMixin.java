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

package fr.exanpe.t5.lib.mixins;

import org.apache.tapestry5.annotations.Mixin;

import fr.exanpe.t5.lib.base.BaseAuthorize;
import fr.exanpe.t5.lib.components.Authorize;

/**
 * A mixin used to get control of a component according the roles of a user.<br/>
 * Depending on role, the component this mixin is applied on will, or won't be processed.<br/>
 * {@link Mixin} version of the {@link Authorize} component.
 * 
 * @see BaseAuthorize
 * @see Authorize
 * @since 1.2
 * @author jmaupoux
 */
public class AuthorizeMixin extends BaseAuthorize
{

}
