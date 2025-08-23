package de.hybris.platform.cockpit.components.menu.impl;

import de.hybris.platform.cockpit.components.menu.AbstractAssignedListRenderer;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;
import org.zkoss.zul.ListitemRenderer;

public abstract class AbstractUsersAssignedListRenderer extends AbstractAssignedListRenderer<TypedObject>
{
    public AbstractUsersAssignedListRenderer(List assignedValuesList)
    {
        super(assignedValuesList);
    }


    public ListitemRenderer createAvailableCollectionMenuItemListRenderer()
    {
        return (ListitemRenderer)new Object(this);
    }
}
