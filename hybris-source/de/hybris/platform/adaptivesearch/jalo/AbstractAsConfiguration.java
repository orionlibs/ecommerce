package de.hybris.platform.adaptivesearch.jalo;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;

public class AbstractAsConfiguration extends GeneratedAbstractAsConfiguration
{
    public String generateItemIdentifier(Item item)
    {
        if(item == null)
        {
            return "null";
        }
        PK itemPk = item.getPK();
        if(itemPk == null)
        {
            throw new IllegalStateException("Could not generate identifier for item with unknown pk");
        }
        return itemPk.getLongValueAsString();
    }


    public String decorateIdentifier(String identifier)
    {
        if(identifier == null)
        {
            return "null";
        }
        return identifier;
    }
}
