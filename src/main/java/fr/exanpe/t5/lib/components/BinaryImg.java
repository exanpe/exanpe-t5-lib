package fr.exanpe.t5.lib.components;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.Asset;
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
import org.slf4j.Logger;

import fr.exanpe.t5.lib.internal.InputStreamResponse;

/**
 * Component rendering an &lt;img&gt; from an InputStream.
 * Do not create any physical file.<br/>
 * The binary image will be copied in session before rendered to the user. The inputStream parameter
 * will be automatically closed by the component once in session.<br/>
 * Once consumed by the browser, the image is automatically cleared.
 * A clear() method allow you to clear the inputstream data if not consumed by the browser (you can
 * also discard persistent fields on your page).<br/>
 * <br/>
 * If an error occurs with the stream parameter, an error is logged and the image of parameter
 * "errorImage" is displayed.
 * 
 * @author jmaupoux
 * @since 1.3
 */
@SupportsInformalParameters
public class BinaryImg
{
    /**
     * Render event
     */
    private static final String EVENT = "render";

    /**
     * Logger
     */
    @Inject
    private Logger logger;

    /**
     * InputStream containing the image to display
     */
    @Parameter(required = true)
    private InputStream inputStream;

    /**
     * Error image if error occurs with the stream
     */
    @Parameter(required = true, defaultPrefix = BindingConstants.ASSET, allowNull = false, value = "img/binimg/error.png")
    private Asset errorImage;

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
    void init(MarkupWriter writer) throws IOException
    {
        String src = null;

        try
        {
            _inputStream = new ByteArrayInputStream(IOUtils.toByteArray(inputStream));
            src = resources.createEventLink(EVENT).toURI();
        }
        catch (IOException e)
        {
            logger.error("Impossible to consume stream", e);
            src = errorImage.toClientURL();
        }

        // ensure the stream is closed
        if (inputStream != null)
        {
            IOUtils.closeQuietly(inputStream);
        }

        writer.element("img", "src", src);

        resources.renderInformalParameters(writer);

        writer.end();
    }

    /**
     * Event rendering the image
     * 
     * @return the stream response containing the image
     */
    @OnEvent(value = EVENT)
    StreamResponse render()
    {
        StreamResponse response = new InputStreamResponse(_inputStream, contentType);

        _inputStream = null;

        return response;
    }

    /**
     * Return a persist stream
     * 
     * @return the persist stream (actually a ByteArrayInputStream), or null if consumed by the
     *         browser
     */
    public InputStream getInputStream()
    {
        return _inputStream;
    }

    /**
     * Clear the persisted stream.
     */
    public void clear()
    {
        _inputStream = null;
    }
}
