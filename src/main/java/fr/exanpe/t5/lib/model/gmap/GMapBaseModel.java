/**
 * 
 */
package fr.exanpe.t5.lib.model.gmap;

/**
 * Base classe for GMap models
 * 
 * @author lguerin
 * @since 1.2
 */
public abstract class GMapBaseModel
{
    private String id;
    private String latitude;
    private String longitude;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getLatitude()
    {
        return latitude;
    }

    public void setLatitude(String latitude)
    {
        this.latitude = latitude;
    }

    public String getLongitude()
    {
        return longitude;
    }

    public void setLongitude(String longitude)
    {
        this.longitude = longitude;
    }
}
