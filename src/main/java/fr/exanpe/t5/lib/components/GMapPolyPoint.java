/**
 * 
 */
package fr.exanpe.t5.lib.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.Environment;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.slf4j.Logger;

import fr.exanpe.t5.lib.model.GMapInternalModel;
import fr.exanpe.t5.lib.model.gmap.GMapPolyPointModel;

/**
 * This component represents a Polyline Point displayed into {@link GMap} component.<br/>
 * It must be nested inside a {@link GMap} component.<br/>
 * 
 * @author lguerin
 * @since 1.2
 */
public class GMapPolyPoint implements ClientElement
{
    /**
     * The latitude position of the point
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String latitude;

    /**
     * The longitude position of the point
     */
    @Property
    @Parameter(defaultPrefix = BindingConstants.LITERAL)
    private String longitude;

    @Property
    private GMapInternalModel gmapModel;

    @Property
    private String uniqueId;

    @Inject
    private Logger log;

    @Inject
    private ComponentResources resources;

    @Inject
    private Environment environment;

    @Inject
    private JavaScriptSupport javaScriptSupport;

    @SetupRender
    void init()
    {
        uniqueId = javaScriptSupport.allocateClientId(resources);

        gmapModel = environment.peek(GMapInternalModel.class);
        if (gmapModel == null)
            throw new IllegalStateException("GMapPolyPoint component must be nested in a GMap");

        // fill in the model
        GMapPolyPointModel model = new GMapPolyPointModel();
        model.setId(getClientId());
        model.setLatitude(latitude);
        model.setLongitude(longitude);
        gmapModel.addPolyPoint(model);
        log.debug("Registering Google map Polyline point id: {}", getClientId());
    }

    public String getClientId()
    {
        return uniqueId;
    }
}
