package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.components.navigationarea.DefaultNavigationAreaModel;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;

public class UrlNavigationArea extends BaseUICockpitNavigationArea
{
    public SectionPanelModel getSectionModel()
    {
        if(super.getSectionModel() == null)
        {
            DefaultNavigationAreaModel defaultNavigationAreaModel = new DefaultNavigationAreaModel((AbstractUINavigationArea)this);
            defaultNavigationAreaModel.initialize();
            setSectionModel((SectionPanelModel)defaultNavigationAreaModel);
        }
        return super.getSectionModel();
    }
}
