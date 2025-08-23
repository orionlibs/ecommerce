package de.hybris.platform.util;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;

public class PartOfItemAlreadyAssignedToTheParentException extends JaloInvalidParameterException
{
    public PartOfItemAlreadyAssignedToTheParentException(Item toLink, Item key, Item prev)
    {
        super("cannot assign partOf item " + toLink + " to " + key + " since it already belongs to " + prev, 0);
    }
}
