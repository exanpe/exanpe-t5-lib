/**
 * 
 */
package fr.exanpe.t5.lib.mixins;

import java.awt.TextArea;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.ClientElement;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * A mixin use to give {@link TextArea} rich text editing features.
 * JavaScript : This component is bound to a class Exanpe.RichTextEditor.<br/>
 * CSS : This component is bound to a class exanpe-rte.<br/>
 * 
 * @since 1.2
 * @author lguerin
 */
@Import(library =
{ "${exanpe.yui2-base}/yahoo-dom-event/yahoo-dom-event.js", "${exanpe.yui2-base}/element/element-min.js",
        "${exanpe.yui2-base}/container/container_core-min.js", "${exanpe.yui2-base}/menu/menu-min.js", "${exanpe.yui2-base}/button/button-min.js",
        "${exanpe.yui2-base}/editor/editor-min.js", "${exanpe.asset-base}/js/exanpe-t5-lib.js" }, stylesheet =
{ "${exanpe.asset-base}/css/exanpe-t5-lib-core.css", "${exanpe.asset-base}/css/editor.css", "${exanpe.asset-base}/css/exanpe-t5-lib-skin.css" })
public class RichTextEditor
{
    @Parameter(defaultPrefix = BindingConstants.MESSAGE)
    private String title;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean collapse;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "500")
    private String width;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "300")
    private String height;

    @Parameter(defaultPrefix = BindingConstants.LITERAL, value = "true")
    private Boolean autofocus;

    /**
     * Root CSS class of the mixin
     */
    private static final String ROOT_CSS_CLASS = "exanpe-rte";

    /**
     * YUI CSS class (sam skin)
     */
    private static final String YUI_CSS_CLASS = "yui-skin-sam";

    @InjectContainer
    private ClientElement container;

    @Environmental
    private JavaScriptSupport javascriptSupport;

    @Inject
    private ExanpeComponentService ecservice;

    @BeginRender
    void begin(MarkupWriter writer)
    {
        Element e = writer.element("class");
        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);
        Element e2 = writer.element("class");
        e2.addClassName(YUI_CSS_CLASS);
    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        JSONObject data = buildJSONData();
        javascriptSupport.addInitializerCall("richTextEditorBuilder", data);
        writer.end();
        writer.end();
    }

    private JSONObject buildJSONData()
    {
        JSONObject data = new JSONObject();
        data.accumulate("id", container.getClientId());
        data.accumulate("title", title);
        data.accumulate("width", width);
        data.accumulate("height", height);
        data.accumulate("autofocus", autofocus);
        data.accumulate("collape", collapse);
        return data;
    }
}
