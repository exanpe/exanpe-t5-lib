package fr.exanpe.t5.lib.model;

import java.util.Stack;

public class MenuInternalModel
{
    private Stack<MenuRenderElement> stack = new Stack<MenuRenderElement>();

    public enum MenuRenderElement
    {
        ROOT, MENUITEM;
    }

    public MenuRenderElement getParent()
    {
        return stack.peek();
    }

    public void push(MenuRenderElement element)
    {
        stack.push(element);
    }

    public void endElement()
    {
        stack.pop();
    }

}
