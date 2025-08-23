package de.hybris.platform.personalizationsampledataaddon.setup.impl;

import de.hybris.platform.addonsupport.setup.impl.GenericAddOnSampleDataEventListener;
import de.hybris.platform.servicelayer.event.events.AbstractEvent;
import de.hybris.platform.util.Config;

public class CxAddOnSampleDataEventListener extends GenericAddOnSampleDataEventListener
{
    protected void onEvent(AbstractEvent event)
    {
        if(Config.getBoolean("personalizationsampledataaddon.import.active", true))
        {
            super.onEvent(event);
        }
    }
}
