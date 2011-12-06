package fr.exanpe.t5.lib.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import fr.exanpe.t5.lib.exception.AuthorizeException;

/**
 * Interface used to restrict a page's method invocation.<br/>
 * Simply annotate your method or class with \@Authorize and the restriction will be applied
 * depending on the current user authorities.<br/>
 * Note that on authorization failure, an {@link AuthorizeException} will be thrown. Catch it to
 * display a custom error page.
 * 
 * @see AuthorizeException
 * @author jmaupoux
 * @since 1.2
 */
@Target(
{ ElementType.METHOD, ElementType.TYPE })
@Retention(RUNTIME)
@Documented
public @interface Authorize
{
    /**
     * Any of these authorizations grant access
     */
    String[] any() default
    {};

    /**
     * All of these authorizations are required to grant access
     */
    String[] all() default
    {};

    /**
     * None of these authorizations grant access
     */
    String[] not() default
    {};
}
