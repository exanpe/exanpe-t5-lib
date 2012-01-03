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
