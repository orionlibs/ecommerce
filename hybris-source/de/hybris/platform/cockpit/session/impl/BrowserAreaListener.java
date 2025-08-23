package de.hybris.platform.cockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.BrowserModel;
import java.util.Collection;
import java.util.List;

public interface BrowserAreaListener
{
    void selectionChanged(BrowserModel paramBrowserModel);


    void itemActivated(TypedObject paramTypedObject);


    void browsersClosed(List<BrowserModel> paramList);


    void browserMinimized(BrowserModel paramBrowserModel);


    void browserOpened(BrowserModel paramBrowserModel);


    void browserAdded(BrowserModel paramBrowserModel);


    void browserChanged(BrowserModel paramBrowserModel);


    void browserFocused(BrowserModel paramBrowserModel);


    void browserQuerySaved(BrowserModel paramBrowserModel);


    void splitmodeChanged();


    void itemsDropped(BrowserModel paramBrowserModel, Collection<TypedObject> paramCollection);
}
