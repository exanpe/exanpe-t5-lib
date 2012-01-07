package fr.exanpe.t5.lib.services;

import java.util.Locale;

import fr.exanpe.t5.lib.internal.localesession.LocaleSessionRequestFilter;

/**
 * This interface defines the services offered to manipulate the locale in session (instead of in
 * url).
 * Note that your Locale have to be defined as a contribution of SymbolConstants#SUPPORTED_LOCALES
 * 
 * @author jmaupoux
 * @since 1.2
 */
public interface LocaleSessionService
{
    /**
     * Set a locale in the session from name.
     * Automatically restored on request by contributing with {@link LocaleSessionRequestFilter}
     * 
     * @param locale the locale String. null is allowed.
     */
    public void setLocale(String locale);

    /**
     * Set a locale in the session. Uses the toString() value of the Locale class.<br/>
     * 
     * @param locale the Locale. null is allowed to reset the locale
     */
    public void setLocale(Locale locale);

    /**
     * Get a locale from the current session
     * 
     * @return the locale String, as set by {@link #setLocale(String)}, or null if none
     */
    public String getLocale();
}
