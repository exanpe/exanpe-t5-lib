package exanpe.t5.lib.demo.pages.comp;

import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Log;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Zone;

import fr.exanpe.t5.lib.components.Slider;
import fr.exanpe.t5.lib.constants.ExanpeEventConstants;

/**
 * The demo page for {@link Slider} component
 * 
 * @author lguerin
 */
public class SliderTest
{
    @SuppressWarnings("unused")
    @Property
    @Persist
    private String sliderValue;

    @SuppressWarnings("unused")
    @Property
    @Persist
    private String sliderValue2;

    @SuppressWarnings("unused")
    @Property
    @Persist
    private String sliderValue3;

    @SuppressWarnings("unused")
    @Property
    @Persist
    private String sliderValue4;

    @InjectComponent
    private Zone zone;

    @Log
    @OnEvent(ExanpeEventConstants.SLIDER_EVENT)
    Object handleSliderAjaxEvent(String value)
    {
        System.out.println("Ajax value: " + value);
        return zone.getBody();
    }
}
