package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.cmscockpit.events.impl.CmsLiveEditEvent;
import de.hybris.platform.cmscockpit.events.impl.CmsNavigationEvent;
import de.hybris.platform.cmscockpit.events.impl.CmsUrlChangeEvent;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.CockpitEventAcceptor;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitNavigationArea;
import java.util.ArrayList;
import java.util.List;

public class LiveEditNavigationArea extends BaseUICockpitNavigationArea
{
    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof CmsLiveEditEvent)
        {
            for(Section section : getSectionModel().getSections())
            {
                if(section instanceof CockpitEventAcceptor)
                {
                    if(((CmsLiveEditEvent)event).getSite() != null)
                    {
                        ((CockpitEventAcceptor)section).onCockpitEvent(event);
                    }
                }
            }
        }
        else if(event instanceof CmsNavigationEvent)
        {
            List<Section> sections = new ArrayList<>(getSectionModel().getSections());
            for(Section section : sections)
            {
                if(section instanceof CockpitEventAcceptor)
                {
                    if(((CmsNavigationEvent)event).getSite() != null)
                    {
                        ((CockpitEventAcceptor)section).onCockpitEvent(event);
                    }
                }
            }
        }
        else if(event instanceof CmsUrlChangeEvent)
        {
            CmsUrlChangeEvent cmsUrlChangeEvent = (CmsUrlChangeEvent)event;
            BrowserModel browserModel = getPerspective().getBrowserArea().getFocusedBrowser();
            if(browserModel instanceof LiveEditBrowserModel)
            {
                LiveEditBrowserModel liveEditBrowserModel = (LiveEditBrowserModel)browserModel;
                liveEditBrowserModel.setFrontendAttributes(cmsUrlChangeEvent);
            }
            for(Section section : getSectionModel().getSections())
            {
                if(section instanceof CockpitEventAcceptor)
                {
                    ((CockpitEventAcceptor)section).onCockpitEvent(event);
                }
            }
        }
        else if(event instanceof de.hybris.platform.cmscockpit.events.impl.CmsPerspectiveInitEvent)
        {
            List<Section> sections = new ArrayList<>(getSectionModel().getSections());
            for(Section section : sections)
            {
                if(section instanceof CockpitEventAcceptor)
                {
                    ((CockpitEventAcceptor)section).onCockpitEvent(event);
                }
            }
        }
    }
}
