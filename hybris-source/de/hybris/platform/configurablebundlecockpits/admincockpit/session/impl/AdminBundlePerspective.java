package de.hybris.platform.configurablebundlecockpits.admincockpit.session.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;

public class AdminBundlePerspective extends BaseUICockpitPerspective
{
    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof ItemChangedEvent)
        {
            ItemChangedEvent itemChangedEvent = (ItemChangedEvent)event;
            if(itemChangedEvent.getItem().getObject() instanceof de.hybris.platform.configurablebundleservices.model.BundleSelectionCriteriaModel)
            {
                update();
            }
        }
    }
}
