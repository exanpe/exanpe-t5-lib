package fr.exanpe.t5.lib.model;

import java.util.LinkedList;
import java.util.List;

import fr.exanpe.t5.lib.components.Accordion;
import fr.exanpe.t5.lib.components.AccordionItem;

/**
 * Internal model to allow the accordion items to register to the main accordion component
 * 
 * @see Accordion
 * @see AccordionItem
 * @author jmaupoux
 */
public class AccordionInternalModel
{
    private List<String> itemsId;

    public AccordionInternalModel()
    {
        itemsId = new LinkedList<String>();
    }

    public List<String> getItemsId()
    {
        return itemsId;
    }

    public void setItemsId(List<String> itemsId)
    {
        this.itemsId = itemsId;
    }

}
