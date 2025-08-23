package de.hybris.platform.cockpit.model.general;

import de.hybris.platform.cockpit.model.general.impl.ListDataEvent;

public interface ListModelDataListener
{
    void changed(ListDataEvent paramListDataEvent);
}
