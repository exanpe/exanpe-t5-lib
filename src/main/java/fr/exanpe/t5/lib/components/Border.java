package fr.exanpe.t5.lib.components;

import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.annotations.AfterRender;
import org.apache.tapestry5.annotations.BeginRender;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.SupportsInformalParameters;
import org.apache.tapestry5.dom.Element;
import org.apache.tapestry5.ioc.annotations.Inject;

import fr.exanpe.t5.lib.services.ExanpeComponentService;

/**
 * A simple border. No t:id required.<br/>
 * The purpose of this component is to provide a border integrated to the graphic style.
 * CSS : This component is bound to a class exanpe-bor.<br/>
 * 
 * @author jmaupoux
 */
@Import(stylesheet =
{ "css/exanpe-t5-lib-core.css", "css/exanpe-t5-lib-skin.css" })
@SupportsInformalParameters
public class Border
{

    private static final String ROOT_CSS_CLASS = "exanpe-bor";

    @Inject
    private ComponentResources resources;

    @Inject
    private ExanpeComponentService ecservice;

    @BeginRender
    void begin(MarkupWriter writer)
    {
        Element e = writer.element("div");

        resources.renderInformalParameters(writer);

        ecservice.reorderCSSClassDeclaration(e, ROOT_CSS_CLASS);
    }

    @AfterRender
    void end(MarkupWriter writer)
    {
        writer.end();
    }
}
