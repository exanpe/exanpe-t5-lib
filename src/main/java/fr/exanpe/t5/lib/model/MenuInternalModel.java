package fr.exanpe.t5.lib.model;

import java.util.Stack;

/**
 * Internal mode for the menu
 * Used mainly to build a render stack and keep in mind the positioning state in the menu. This
 * positioning may have an impact on rendering.
 * This model is not kept after rendering. Elements are discarded after processed.
 * 
 * @author jmaupoux
 */
public class MenuInternalModel
{
    private Stack<MenuRenderElement> stack = new Stack<MenuRenderElement>();

    /**
     * Type of element managed
     * 
     * @author jmaupoux
     */
    public enum MenuRenderElement
    {
        ROOT, MENUITEM;
    }

    /**
     * Return the parent of the current state
     * 
     * @return the parent of the current state
     */
    public MenuRenderElement getParent()
    {
        return stack.peek();
    }

    /**
     * Push a new element under the current
     * 
     * @param element the element
     */
    public void push(MenuRenderElement element)
    {
        stack.push(element);
    }

    /**
     * End the current element. Discard it
     */
    public void endElement()
    {
        stack.pop();
    }

}
