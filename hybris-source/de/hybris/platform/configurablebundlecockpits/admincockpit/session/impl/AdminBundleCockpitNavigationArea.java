package de.hybris.platform.configurablebundlecockpits.admincockpit.session.impl;

import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import de.hybris.platform.configurablebundlecockpits.admincockpit.components.navigationarea.AdminBundleCockpitNavigationAreaModel;

public class AdminBundleCockpitNavigationArea extends BaseUICockpitNavigationArea
{
    public SectionPanelModel getSectionModel()
    {
        if(super.getSectionModel() == null)
        {
            AdminBundleCockpitNavigationAreaModel model = new AdminBundleCockpitNavigationAreaModel((AbstractUINavigationArea)this);
            model.initialize();
            setSectionModel((SectionPanelModel)model);
        }
        return super.getSectionModel();
    }
}
