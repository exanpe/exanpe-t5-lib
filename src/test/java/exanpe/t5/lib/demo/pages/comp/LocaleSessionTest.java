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

package exanpe.t5.lib.demo.pages.comp;

import java.util.Locale;

import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.exanpe.t5.lib.services.LocaleSessionService;

public class LocaleSessionTest
{
    @Inject
    private LocaleSessionService localeSessionService;

    @Inject
    private Messages msg;

    void onActionFromFr()
    {
        localeSessionService.setLocale(Locale.FRANCE);
        System.out.println(msg.get("test"));
    }

    void onActionFromEn()
    {
        localeSessionService.setLocale("en");
        System.out.println(msg.get("test"));
    }
}
