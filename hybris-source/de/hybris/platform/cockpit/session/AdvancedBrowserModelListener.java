package de.hybris.platform.cockpit.session;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import java.util.Collection;

public interface AdvancedBrowserModelListener extends BrowserModelListener
{
    void itemActivated(TypedObject paramTypedObject);


    void viewModeChanged(AdvancedBrowserModel paramAdvancedBrowserModel);


    void contextViewModeChanged(AdvancedBrowserModel paramAdvancedBrowserModel);


    void contextRootTypeChanged(AdvancedBrowserModel paramAdvancedBrowserModel);


    void contextSelectionChanged(AdvancedBrowserModel paramAdvancedBrowserModel);


    void contextVisibilityChanged(AdvancedBrowserModel paramAdvancedBrowserModel);


    void contextItemsChanged(AdvancedBrowserModel paramAdvancedBrowserModel);


    void itemsDropped(AdvancedBrowserModel paramAdvancedBrowserModel, Collection<TypedObject> paramCollection);
}
