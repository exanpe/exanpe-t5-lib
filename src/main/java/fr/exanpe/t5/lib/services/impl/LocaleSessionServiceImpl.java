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

import java.util.Locale;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.RequestGlobals;

import fr.exanpe.t5.lib.services.LocaleSessionService;

/**
 * Implementation of the service
 * 
 * @author jmaupoux
 */
public class LocaleSessionServiceImpl implements LocaleSessionService
{

    public static final String LOCALE_ATT_NAME = "__locale";

    @Inject
    private RequestGlobals request;

    public void setLocale(String locale)
    {
        innerSetLocale(locale);
    }

    public void setLocale(Locale locale)
    {
        if (locale == null)
        {
            innerSetLocale(null);
        }
        else
        {
            setLocale(locale.toString());
        }
    }

    private void innerSetLocale(String locale)
    {
        request.getRequest().getSession(true).setAttribute(LOCALE_ATT_NAME, locale);
    }

    public String getLocale()
    {
        return (String) request.getRequest().getSession(true).getAttribute(LOCALE_ATT_NAME);
    }

}
