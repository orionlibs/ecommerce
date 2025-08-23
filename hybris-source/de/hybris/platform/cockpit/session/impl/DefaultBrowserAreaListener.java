package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.UIBrowserArea;
import de.hybris.platform.cockpit.session.UINavigationArea;
import java.util.Collection;
import java.util.List;

public class DefaultBrowserAreaListener implements BrowserAreaListener
{
    private final BaseUICockpitPerspective perspective;


    public DefaultBrowserAreaListener(BaseUICockpitPerspective perspective)
    {
        if(perspective == null)
        {
            throw new IllegalArgumentException("Perspective can not be null.");
        }
        this.perspective = perspective;
    }


    public void browsersClosed(List<BrowserModel> browsers)
    {
    }


    public void browserMinimized(BrowserModel browserModel)
    {
    }


    public void browserOpened(BrowserModel browserModel)
    {
    }


    public void selectionChanged(BrowserModel browserModel)
    {
    }


    public void browserAdded(BrowserModel browserModel)
    {
    }


    public void browserChanged(BrowserModel browserModel)
    {
        getPerspective().closeAreasIfOverlapped();
    }


    public void browserFocused(BrowserModel browserModel)
    {
        UINavigationArea navigationArea = getNavigationArea();
        if(navigationArea != null)
        {
            getNavigationArea().update();
        }
    }


    public void browserQuerySaved(BrowserModel browserModel)
    {
    }


    public void itemActivated(TypedObject activeItem)
    {
        getPerspective().activateItemInEditor(activeItem);
    }


    public void splitmodeChanged()
    {
    }


    public void itemsDropped(BrowserModel browserModel, Collection<TypedObject> items)
    {
    }


    protected UINavigationArea getNavigationArea()
    {
        return this.perspective.getNavigationArea();
    }


    protected UIBrowserArea getBrowserArea()
    {
        return this.perspective.getBrowserArea();
    }


    protected BaseUICockpitPerspective getPerspective()
    {
        return this.perspective;
    }
}
