/**
 * 
 */
package fr.exanpe.t5.lib.model.gmap;

import org.apache.tapestry5.Asset;

/**
 * Model class for Google Map Marker.
 * 
 * @author lguerin
 * @since 1.2
 */
public class GMapMarkerModel extends GMapBaseModel
{
    private Asset icon;
    private String title;
    private String info;

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Asset getIcon()
    {
        return icon;
    }

    public void setIcon(Asset asset)
    {
        this.icon = asset;
    }

    public String getInfo()
    {
        return info;
    }

    public void setInfo(String info)
    {
        this.info = info;
    }
}
