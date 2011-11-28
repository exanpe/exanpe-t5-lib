package fr.exanpe.t5.lib.mixins;

import org.apache.tapestry5.annotations.Mixin;

import fr.exanpe.t5.lib.base.BaseAuthorize;
import fr.exanpe.t5.lib.components.Authorize;

/**
 * A mixin used to get control of a component according the roles of a user.<br/>
 * Depending on role, the component this mixin is applied on will, or won't be processed.<br/>
 * {@link Mixin} version of the {@link Authorize} component.
 * 
 * @see BaseAuthorize
 * @see Authorize
 * @since 1.2
 * @author jmaupoux
 */
public class AuthorizeMixin extends BaseAuthorize
{

}
