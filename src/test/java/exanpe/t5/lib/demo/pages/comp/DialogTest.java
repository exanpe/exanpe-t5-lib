package exanpe.t5.lib.demo.pages.comp;

import org.apache.tapestry5.annotations.Import;

import fr.exanpe.t5.lib.mixins.Dialog;

/**
 * The demo page for {@link Dialog} mixin
 * 
 * @author lguerin
 */
@Import(stylesheet =
{ "${exanpe.asset-base}/css/exanpe-t5-lib-skin.css" })
public class DialogTest
{

    public String getDisableClass()
    {
        return Dialog.DISABLE_CLASS;
    }

    void onAction()
    {
        System.out.println("ActionLink clicked !");
    }
}
