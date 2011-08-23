/**
 * 
 */
package fr.exanpe.t5.lib.constants;

import fr.exanpe.t5.lib.components.Slider;

/**
 * The {@link Slider} component orientation.<br />
 * 
 * @author lguerin
 */
public enum SliderOrientationTypeEnum
{
    HORIZONTAL, VERTICAL;

    public String toString()
    {
        return super.toString().toLowerCase();
    };
}
