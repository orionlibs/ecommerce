package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.components.sectionpanel.RowlayoutSectionPanelModel;

public interface EditorSectionPanelModel extends RowlayoutSectionPanelModel
{
    boolean isCreateMode();


    void setCreateMode(boolean paramBoolean);
}
