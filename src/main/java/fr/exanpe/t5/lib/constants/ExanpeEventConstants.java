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

/**
 * 
 */
package fr.exanpe.t5.lib.constants;

import org.apache.tapestry5.SelectModel;

import fr.exanpe.t5.lib.components.AjaxLoader;
import fr.exanpe.t5.lib.components.Slider;
import fr.exanpe.t5.lib.mixins.AjaxValidation;
import fr.exanpe.t5.lib.mixins.PasswordStrengthChecker;
import fr.exanpe.t5.lib.mixins.SelectLoader;

/**
 * This class contains the events triggered by the Exanpe T5 Lib.
 * 
 * @author lguerin
 */
public class ExanpeEventConstants
{
    /**
     * Event triggered when the {@link Slider} component is used.
     */
    public static final String SLIDER_EVENT = "sliderEventAction";

    /**
     * Event triggered when {@link AjaxLoader} load a content.
     */
    public static final String AJAXLOADER_EVENT = "ajaxLoaderAction";

    /**
     * Event triggered when load a {@link SelectLoader} content.<br/>
     * A method handling this event have to return a class of type {@link SelectModel}
     */
    public static final String SELECTLOADER_EVENT = "selectLoaderAction";

    /**
     * Event triggered when {@link PasswordStrengthChecker} validate a password.<br/>
     * A method handling this event have to return a class of type
     * {@link PasswordStrengthCheckerTypeEnum}
     */
    public static final String PASSWORDSTRENGTHCHECKER_EVENT = "passwordStrengthCheckerAction";

    /**
     * Event triggered when validating from a {@link AjaxValidation} mixin.<br/>
     * A method handling this event have to return a class of type {@link AjaxValidationResult}
     */
    public static final String AJAXVALIDATION_EVENT = "ajaxValidationAction";
}
