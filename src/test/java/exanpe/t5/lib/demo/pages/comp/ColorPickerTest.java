package exanpe.t5.lib.demo.pages.comp;

import java.awt.Color;

import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.Property;

import fr.exanpe.t5.lib.components.Tooltip;

/**
 * The demo page for {@link Tooltip} component
 * 
 * @author lguerin
 */
public class ColorPickerTest
{
    @Property
    @Persist
    private Color color;

    @Property
    @Persist
    private Color color2;

    public void onSubmit()
    {
        if (color != null)
            System.out.println("submit//" + Integer.toHexString(color.getRGB()));
        else
            System.out.println("submit null");
    }

}
