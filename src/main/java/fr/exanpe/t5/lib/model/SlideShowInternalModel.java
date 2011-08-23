package fr.exanpe.t5.lib.model;

import fr.exanpe.t5.lib.components.SlideShow;
import fr.exanpe.t5.lib.components.SlideShowItem;

/**
 * Internal model to pass value from SlideShow to its items.
 * 
 * @see SlideShow
 * @see SlideShowItem
 * @author jmaupoux
 */
public class SlideShowInternalModel
{

    private int height;

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

}
