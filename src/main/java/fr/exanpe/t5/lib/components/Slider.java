/**
 * 
 */
package fr.exanpe.t5.lib.components;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.Events;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.RequestParameter;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.constants.ExanpeEventConstants;
import fr.exanpe.t5.lib.constants.SliderOrientationTypeEnum;

/**
 * <code>Slider</code> component.<br />
 * The Slider component enables the user to adjust values in a finite range along an horizontal axe
 * or a vertical axe.
 * JavaScript : This component is bound to a class Exanpe.Slider.<br/>
 * CSS : This component is bound to a class exanpe-slider.<br/>
 * 
 * @author lguerin
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/dragdrop/dragdrop-min.js", "${exanpe.yui2-base}/slider/slider-min.js",
        "${exanpe.yui2-base}/connection/connection-min.js", "js/exanpe-t5-lib.js" }, stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@Events(ExanpeEventConstants.SLIDER_EVENT)
public class Slider
{
    /**
     * Minimum value
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false, value = "0")
    private Float min;

    /**
     * Maximum value
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = true, allowNull = false, value = "100")
    private Float max;

    /**
     * Specify the orientation of the slider
     * 
     * @see SliderOrientationTypeEnum
     */
    @Property
    @Parameter(value = "horizontal", required = true, allowNull = false, defaultPrefix = BindingConstants.LITERAL)
    private SliderOrientationTypeEnum orientation;

    /**
     * Value to read or update
     */
    @Property
    @Parameter(required = true, autoconnect = true)
    private Object value;

    /**
     * Optional parameter for the interval : used to set the unit on the slider move.<br/>
     * For example, an interval of 10 force the selection value from ten to ten.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "1")
    private Float interval;

    /**
     * Display the current value or not
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true")
    private boolean displayCurrentValue;

    /**
     * If displayCurrentValue=true, specify a default html element id to display the value into.<br/>
     * By default, display into a span just upside of the slider.
     */
    @Parameter(defaultPrefix = BindingConstants.LITERAL, required = false, allowNull = false)
    private String displayId;

    /**
     * Image used for horizontal cursor
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.ASSET, value = "img/slider/thumb-n.gif")
    private Asset horizontalCursor;

    /**
     * Image used for vertical cursor
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.ASSET, value = "img/slider/thumb-e.gif")
    private Asset verticalCursor;

    @Property
    private String uniqueId;

    @Property
    @SuppressWarnings("unused")
    private boolean displayEmbeddedValue;

    /**
     * Request param send (in Ajax) at the end of the slide operation.
     */
    public static final String PARAM_NAME = "value";

    /**
     * Prefix for default display value container
     */
    private static final String SLIDER_LABEL_PREFIX = "slider-label-";

    @Inject
    private ComponentResources resources;

    @Inject
    private JavaScriptSupport javascriptSupport;

    @SetupRender
    void init()
    {
        uniqueId = javascriptSupport.allocateClientId(resources);

        displayEmbeddedValue = displayCurrentValue && displayId == null;
        if (value == null)
        {
            value = min;
        }
    }

    @AfterRender
    void end()
    {
        JSONObject data = buildJSONData();
        javascriptSupport.addInitializerCall("sliderBuilder", data);
    }

    @OnEvent(value = ExanpeEventConstants.SLIDER_EVENT)
    void onSlideEnd(@RequestParameter(PARAM_NAME)
    Object newValue)
    {
        value = newValue;
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();
        data.accumulate("id", getClientId());
        data.accumulate("min", min);
        data.accumulate("max", max);
        data.accumulate("interval", interval);

        if (displayCurrentValue)
        {
            data.accumulate("displayId", displayId != null ? displayId : SLIDER_LABEL_PREFIX + getClientId());
        }
        data.accumulate("orientation", orientation.toString());
        data.accumulate("horizontalCursorImage", horizontalCursor.toClientURL());
        data.accumulate("verticalCursorImage", verticalCursor.toClientURL());
        return data;
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
