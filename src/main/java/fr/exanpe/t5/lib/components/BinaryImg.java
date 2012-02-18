package fr.exanpe.t5.lib.components;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Persist;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.exanpe.t5.lib.internal.InputStreamResponse;

/**
 * Component rendering an &lt;img&gt; from an InputStream.<br/>
 * Do not create any physical file.<br/>
 * DO NOT CLOSE THE STREAM.<br/>
 * We strongly encourage a {@link ByteArrayInputStream} as parameter.
 * 
 * @author jmaupoux
 * @since 1.3
 */
@SupportsInformalParameters
public class BinaryImg
{
    private static final String EVENT = "render";

    /**
     * InputStream containing the image to display
     */
    @Parameter(required = true)
    private InputStream inputStream;

    /**
     * Saved inputstream
     */
    @Persist
    private InputStream _inputStream;

    /**
     * Content type of the image. Default to image/jpeg
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.LITERAL, value = "image/jpeg")
    private String contentType;

    /**
     * resources
     */
    @Inject
    private ComponentResources resources;

    @BeginRender
    void init(MarkupWriter writer)
    {

        _inputStream = inputStream;

        writer.element("img", "src", resources.createEventLink(EVENT).toURI());

        resources.renderInformalParameters(writer);

        writer.end();
    }

    /**
     * Event rendering the image
     * 
     * @return the stream response containing the image
     */
    @OnEvent(value = EVENT)
    public StreamResponse render()
    {
        StreamResponse response = new InputStreamResponse(_inputStream, contentType);

        _inputStream = null;

        return response;
    }

    /**
     * Return a persist stream
     * 
     * @return the persist stream, or null if consumed by the browser
     */
    public InputStream getInputStream()
    {
        return _inputStream;
    }

    /**
     * Clear the persisted stream.
     * Do NOT close it
     */
    public void clear()
    {
        _inputStream = null;
    }
}
