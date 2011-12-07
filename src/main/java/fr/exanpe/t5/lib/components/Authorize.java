package fr.exanpe.t5.lib.components;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;

import fr.exanpe.t5.lib.base.BaseAuthorize;
import fr.exanpe.t5.lib.mixins.AuthorizeMixin;

/**
 * This component displays its body/block according to authorizations rules declared.<br/>
 * All rules will be applied if provided. If one fails, the body/block won't be rendered.<br/>
 * any/all/not must be filled with comma separated roles, as set into your session principal.<br/>
 * For easier maintenance, we advise you to fill only one of any/all/not parameter.
 * 
 * @since 1.2
 * @see BaseAuthorize
 * @see AuthorizeMixin
 * @author jmaupoux
 */
public class Authorize extends BaseAuthorize
{
    /**
     * Block to render authorization. If not provided, the body of the component is rendered.
     */
    @SuppressWarnings("unused")
    @Property
    @Parameter(defaultPrefix = BindingConstants.PROP)
    private Block block;

}
