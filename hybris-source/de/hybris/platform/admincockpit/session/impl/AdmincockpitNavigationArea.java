package de.hybris.platform.admincockpit.session.impl;

import de.hybris.platform.admincockpit.components.navigationarea.AdmincockpitNavigationAreaModel;
import de.hybris.platform.cockpit.components.navigationarea.NavigationPanelSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.components.sectionpanel.SectionPanelModel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.session.impl.AbstractUINavigationArea;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;

public class AdmincockpitNavigationArea extends BaseUICockpitNavigationArea
{
    public SectionPanelModel getSectionModel()
    {
        if(super.getSectionModel() == null)
        {
            AdmincockpitNavigationAreaModel model = new AdmincockpitNavigationAreaModel((AbstractUINavigationArea)this);
            model.initialize();
            setSectionModel((SectionPanelModel)model);
        }
        return super.getSectionModel();
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        if(event instanceof ItemChangedEvent)
        {
            ItemChangedEvent itemChangedEvent = (ItemChangedEvent)event;
            if(itemChangedEvent.getChangeType() == ItemChangedEvent.ChangeType.REMOVED)
            {
                for(Section sectionElement : getSectionModel().getSections())
                {
                    if(sectionElement instanceof NavigationPanelSection)
                    {
                        NavigationPanelSection navigationPanelSection = (NavigationPanelSection)sectionElement;
                        if(navigationPanelSection.getRenderer() instanceof de.hybris.platform.admincockpit.components.navigationarea.renderer.ConstraintsSectionRenderer || navigationPanelSection
                                        .getRenderer() instanceof de.hybris.platform.admincockpit.components.navigationarea.renderer.ConstraintGroupsSectionRenderer)
                        {
                            getSectionModel().update();
                        }
                    }
                }
            }
        }
    }
}
