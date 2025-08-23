package de.hybris.platform.cmscockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIEditorArea;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserArea;
import org.zkoss.util.resource.Labels;

public class NavigationNodeBrowserArea extends DefaultSearchBrowserArea implements SiteVersionAware
{
    private CMSSiteModel activeSite;
    private CatalogVersionModel activeCatalogVersion;


    public String getLabel()
    {
        return Labels.getLabel("naviagation.node.area");
    }


    public void setActiveSite(CMSSiteModel activeSite)
    {
        this.activeSite = activeSite;
    }


    public void setActiveCatalogVersion(CatalogVersionModel activeCatalogVersion)
    {
        this.activeCatalogVersion = activeCatalogVersion;
    }


    public CMSSiteModel getActiveSite()
    {
        return this.activeSite;
    }


    public CatalogVersionModel getActiveCatalogVersion()
    {
        return this.activeCatalogVersion;
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof ItemChangedEvent)
        {
            ItemChangedEvent itemChangedEvent = (ItemChangedEvent)event;
            BrowserModel focusedBrowserModel = getPerspective().getBrowserArea().getFocusedBrowser();
            UIEditorArea editorArea = getPerspective().getEditorArea();
            switch(null.$SwitchMap$de$hybris$platform$cockpit$events$impl$ItemChangedEvent$ChangeType[itemChangedEvent.getChangeType().ordinal()])
            {
                case 1:
                    if(focusedBrowserModel != null)
                    {
                        focusedBrowserModel.updateItems();
                    }
                    break;
                case 2:
                case 3:
                    if(focusedBrowserModel != null)
                    {
                        focusedBrowserModel.updateItems();
                        if(editorArea != null)
                        {
                            editorArea.update();
                        }
                    }
                    break;
            }
        }
    }
}
