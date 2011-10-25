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

package fr.exanpe.t5.lib.constants;

import fr.exanpe.t5.lib.mixins.AjaxValidation;

/**
 * Possible ajax validation result. Required as event methods cannot return Boolean values.
 * <ul>
 * <li>true</li>
 * <li>false</li>
 * </ul>
 * 
 * @see AjaxValidation
 * @see ExanpeEventConstants#AJAXVALIDATION_EVENT
 * @author jmaupoux
 */
public enum AjaxValidationResult
{
    TRUE(Boolean.TRUE), FALSE(Boolean.FALSE);

    private Boolean bool;

    private AjaxValidationResult(Boolean bool)
    {
        this.bool = bool;
    }

    public Boolean toBoolean()
    {
        return bool;
    }
}
