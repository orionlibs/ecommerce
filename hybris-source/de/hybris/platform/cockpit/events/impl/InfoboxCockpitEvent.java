package de.hybris.platform.cockpit.events.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.List;

public abstract class InfoboxCockpitEvent extends AbstractCockpitEvent
{
    public InfoboxCockpitEvent(Object source)
    {
        super(source);
    }


    public abstract List<TypedObject> getRelatedItems();
}
