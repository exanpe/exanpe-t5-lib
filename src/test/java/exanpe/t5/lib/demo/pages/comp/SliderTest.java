package exanpe.t5.lib.demo.pages.comp;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import fr.exanpe.t5.lib.components.Slider;

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
}
