package de.hybris.platform.admincockpit.session.impl;

import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.EditorArea;

public class AdmincockpitEditorArea extends EditorArea
{
    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof ItemChangedEvent)
        {
            ItemChangedEvent itemChangedEvent = (ItemChangedEvent)event;
            if(itemChangedEvent.getChangeType() == ItemChangedEvent.ChangeType.REMOVED)
            {
                if(getPerspective() instanceof BaseUICockpitPerspective)
                {
                    ((BaseUICockpitPerspective)getPerspective()).collapseEditorArea();
                    reset();
                    getEditorAreaController().resetSectionPanelModel();
                }
            }
        }
    }
}
