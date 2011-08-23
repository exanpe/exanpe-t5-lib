/**
 * 
 */
package fr.exanpe.t5.lib.constants;

import org.apache.tapestry5.ioc.annotations.Symbol;

/**
 * Exanpe custom {@link Symbol} elements.
 * 
 * @author lguerin
 */
public class ExanpeSymbols
{
    /**
     * Base path for Exanpe asset contents.
     */
    public static final String ASSET_BASE = "exanpe.asset-base";

    /**
     * Base YUI location<br/>
     * Override to import your own distribution of YUI.<br/>
     * The base must be defined according to the yui/build root folder in the distribution.<br/>
     * Example : if base is "com/test", then the path "com/test/yahoo-dom-event/yahoo-dom-event.js"
     * HAS to exists.
     */
    public static final String YUI2_BASE = "exanpe.yui2-base";
}
