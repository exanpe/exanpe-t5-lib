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
