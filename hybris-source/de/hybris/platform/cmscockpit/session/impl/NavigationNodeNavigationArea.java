package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cmscockpit.components.navigationarea.NavigationNodeNavigationAreaModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;

public class NavigationNodeNavigationArea extends BaseUICockpitNavigationArea
{
    public SectionPanelModel getSectionModel()
    {
        if(super.getSectionModel() == null)
        {
            NavigationNodeNavigationAreaModel model = new NavigationNodeNavigationAreaModel();
            model.initialize();
            setSectionModel((SectionPanelModel)model);
        }
        return super.getSectionModel();
    }
}
