package de.hybris.platform.configurablebundlecockpits.productcockpit.session.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.BrowserModelListener;
import de.hybris.platform.cockpit.session.impl.AbstractBrowserArea;
import de.hybris.platform.cockpit.session.impl.DefaultSearchBrowserArea;
import org.zkoss.zk.ui.Component;

public class BundleNavigationNodeBrowserArea extends DefaultSearchBrowserArea implements CatalogVersionAware
{
    public static final String INFO_AREA_CONTAINER = "infoAreaContainer3";
    private CatalogVersionModel activeCatalogVersion;
    private final BundleNavigationNodeBrowserListener browserListener = new BundleNavigationNodeBrowserListener(this, (AbstractBrowserArea)this);


    public String getLabel()
    {
        return "testlabel";
    }


    public void setActiveCatalogVersion(CatalogVersionModel activeCatalogVersion)
    {
        this.activeCatalogVersion = activeCatalogVersion;
    }


    public CatalogVersionModel getActiveCatalogVersion()
    {
        return this.activeCatalogVersion;
    }


    public Component getInfoArea()
    {
        if(getContainerComponent() != null && getContainerComponent().getSpaceOwner() != null)
        {
            return getContainerComponent().getSpaceOwner().getFellowIfAny("infoAreaContainer3");
        }
        return null;
    }


    public void onCockpitEvent(CockpitEvent event)
    {
        super.onCockpitEvent(event);
        if(event instanceof ItemChangedEvent)
        {
            ItemChangedEvent itemChangedEvent = (ItemChangedEvent)event;
            BrowserModel focusedBrowserModel = getPerspective().getBrowserArea().getFocusedBrowser();
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
                    }
                    break;
            }
        }
    }


    public BrowserModelListener getBrowserListener()
    {
        return (BrowserModelListener)this.browserListener;
    }
}
