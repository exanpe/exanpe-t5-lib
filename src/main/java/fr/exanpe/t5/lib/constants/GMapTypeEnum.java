/**
 * 
 */
package fr.exanpe.t5.lib.constants;

/**
 * Types of maps you can display using the Google Maps Javascript API.
 * 
 * @author lguerin
 * @since 1.2
 */
public enum GMapTypeEnum
{
    /**
     * Displays the normal, default 2D tiles of Google Maps
     */
    ROADMAP,
    /**
     * Displays photographic tiles
     */
    SATELLITE,
    /**
     * Displays a mix of photographic tiles and a tile layer for prominent features (roads, city
     * names)
     */
    HYBRID,
    /**
     * Displays physical relief tiles for displaying elevation and water features (mountains,
     * rivers, etc.)
     */
    TERRAIN;

    public String toString()
    {
        return super.toString().toUpperCase();
    };
}
