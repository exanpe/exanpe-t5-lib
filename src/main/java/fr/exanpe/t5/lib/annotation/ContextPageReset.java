package fr.exanpe.t5.lib.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import fr.exanpe.t5.lib.constants.ExanpeSymbols;

/**
 * Annotation to mark a method as reseting the page depending on a marker sent in the context.
 * If {@link ExanpeSymbols#CONTEXT_PAGE_RESET_MARKER} is found in the request, every method
 * containing this annotation, or named "void contextPageReset()", will be triggered
 * 
 * @author jmaupoux
 * @see ExanpeSymbols#CONTEXT_PAGE_RESET_MARKER
 * @since 1.2
 */
@Target(
{ ElementType.METHOD })
@Retention(RUNTIME)
@Documented
public @interface ContextPageReset
{

}
