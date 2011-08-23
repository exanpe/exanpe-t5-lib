/**
 * 
 */
package fr.exanpe.t5.lib.constants;

import fr.exanpe.t5.lib.components.AjaxLoader;
import fr.exanpe.t5.lib.components.Slider;
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
     * Event triggered when load a {@link SelectLoader} content.
     */
    public static final String SELECTLOADER_EVENT = "selectLoaderAction";
}
