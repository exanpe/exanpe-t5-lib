package fr.exanpe.t5.lib.internal.authorize;

import java.util.List;

import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.TransformMethod;

import fr.exanpe.t5.lib.annotation.Authorize;
import fr.exanpe.t5.lib.services.AuthorizeBusinessService;

/**
 * Worker applied on page invocation to control authorization.<br/>
 * This worker simply restrict access of pages along with the user authorizations
 * 
 * @author jmaupoux
 */
public class AuthorizeWorker implements ComponentClassTransformWorker
{
    private final AuthorizeBusinessService authorizeBusinessService;

    public AuthorizeWorker(AuthorizeBusinessService abs)
    {
        this.authorizeBusinessService = abs;
    }

    public void transform(ClassTransformation transformation, MutableComponentModel model)
    {
        final List<TransformMethod> methods = transformation.matchMethodsWithAnnotation(Authorize.class);

        if (methods.isEmpty())
            return;

        for (final TransformMethod method : methods)
        {
            Authorize annot = method.getAnnotation(Authorize.class);

            method.addAdvice(new ComponentAuthorizeAdvice(authorizeBusinessService, annot.any(), annot.all(), annot.not()));
        }
    }
}
