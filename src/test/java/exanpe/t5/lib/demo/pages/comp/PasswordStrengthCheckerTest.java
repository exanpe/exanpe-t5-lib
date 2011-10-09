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
package exanpe.t5.lib.demo.pages.comp;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;

import fr.exanpe.t5.lib.constants.ExanpeEventConstants;
import fr.exanpe.t5.lib.constants.PasswordStrengthCheckerTypeEnum;
import fr.exanpe.t5.lib.mixins.PasswordStrengthChecker;

/**
 * The demo page for the {@link PasswordStrengthChecker} mixin.
 * 
 * @author lguerin
 */
@Import(library = "PasswordStrengthCheckerTest.js")
public class PasswordStrengthCheckerTest
{
    @Property
    private String pwd;

    @Property
    private String pwd2;

    @Property
    private String password;

    @OnEvent(value = ExanpeEventConstants.PASSWORDSTRENGTHCHECKER_EVENT)
    PasswordStrengthCheckerTypeEnum checkPassword(String pwd)
    {
        System.out.println("Check password:" + pwd);
        Integer size = pwd.length();
        PasswordStrengthCheckerTypeEnum result = PasswordStrengthCheckerTypeEnum.VERYWEAK;
        if (size >= 5)
        {
            result = PasswordStrengthCheckerTypeEnum.WEAK;
        }
        if (size >= 6)
        {
            result = PasswordStrengthCheckerTypeEnum.STRONG;
        }
        if (size >= 7)
        {
            result = PasswordStrengthCheckerTypeEnum.STRONGEST;
        }
        return result;
    }

}
