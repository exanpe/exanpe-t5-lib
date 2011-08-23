/**
 * 
 */
package fr.exanpe.t5.lib.constants;

/**
 * Dialog render mode :
 * - CONFIRM : Confirm box with Yes/No buttons
 * - INFO : Info box: stop the current event
 * 
 * @author lguerin
 */
public enum DialogRenderModeEnum
{
    CONFIRM, INFO;

    public String toString()
    {
        return super.toString().toLowerCase();
    };
}
