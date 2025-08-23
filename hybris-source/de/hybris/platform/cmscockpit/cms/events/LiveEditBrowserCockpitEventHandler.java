package de.hybris.platform.cmscockpit.cms.events;

import de.hybris.platform.cockpit.events.CockpitEvent;

public interface LiveEditBrowserCockpitEventHandler<UIBROWSERAREA extends de.hybris.platform.cockpit.session.UIBrowserArea>
{
    void handleCockpitEvent(CockpitEvent paramCockpitEvent, UIBROWSERAREA paramUIBROWSERAREA);


    boolean canHandleEvent(CockpitEvent paramCockpitEvent, UIBROWSERAREA paramUIBROWSERAREA);
}
