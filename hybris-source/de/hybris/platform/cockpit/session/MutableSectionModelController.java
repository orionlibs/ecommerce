package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelEvent;

public interface MutableSectionModelController
{
    void onSectionRemoved(SectionPanelEvent paramSectionPanelEvent);


    void onSectionAdded(SectionPanelEvent paramSectionPanelEvent);


    void onSectionRenamed(SectionPanelEvent paramSectionPanelEvent);
}
