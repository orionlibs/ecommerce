package de.hybris.platform.cmscockpit.components.navigationarea;

import de.hybris.platform.cmscockpit.session.impl.LiveEditBrowserModel;
import de.hybris.platform.cockpit.components.navigationarea.AbstractNavigationAreaModel;
import de.hybris.platform.cockpit.components.navigationarea.DefaultSectionSelectorSection;
import de.hybris.platform.cockpit.components.sectionpanel.Section;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.core.model.user.UserModel;

public class LiveEditNavigationAreaModel extends AbstractNavigationAreaModel
{
    public void initialize()
    {
    }


    public void update()
    {
        for(Section s : getSections())
        {
            if(s instanceof DefaultSectionSelectorSection)
            {
                ((DefaultSectionSelectorSection)s).updateItems();
            }
            sectionUpdated(s);
        }
    }


    public String retriveCurrentFrontendSessionId()
    {
        String frontentSessionId = "";
        UIBrowserArea browserArea = getNavigationArea().getPerspective().getBrowserArea();
        BrowserModel browserModel = browserArea.getFocusedBrowser();
        if(browserModel instanceof LiveEditBrowserModel)
        {
            frontentSessionId = ((LiveEditBrowserModel)browserModel).getFrontentSessionId();
        }
        return frontentSessionId;
    }


    public UserModel retriveCurrentFrontendUser()
    {
        UserModel frontendUser = null;
        UIBrowserArea browserArea = getNavigationArea().getPerspective().getBrowserArea();
        BrowserModel browserModel = browserArea.getFocusedBrowser();
        if(browserModel instanceof LiveEditBrowserModel)
        {
            frontendUser = ((LiveEditBrowserModel)browserModel).getFrontendUser();
        }
        return frontendUser;
    }
}
