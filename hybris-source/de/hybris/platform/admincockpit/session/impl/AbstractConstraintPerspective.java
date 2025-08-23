package de.hybris.platform.admincockpit.session.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.NewItemService;
import de.hybris.platform.cockpit.services.ObjectCollectionService;
import de.hybris.platform.cockpit.session.BrowserModel;
import de.hybris.platform.cockpit.session.NavigationAreaListener;
import de.hybris.platform.cockpit.session.SearchBrowserModel;
import de.hybris.platform.cockpit.session.UICockpitPerspective;
import de.hybris.platform.cockpit.session.impl.BaseUICockpitPerspective;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class AbstractConstraintPerspective extends BaseUICockpitPerspective
{
    private static final Logger LOG = LoggerFactory.getLogger(AbstractConstraintPerspective.class);
    private ObjectCollectionService objectCollectionService = null;
    private NewItemService newItemService;
    private final NavigationAreaListener listener = (NavigationAreaListener)new AbstactConstraintNavigatorListener(this, this);


    protected NavigationAreaListener getNavigationAreaListener()
    {
        return this.listener;
    }


    public void onShow()
    {
        getEditorArea().setManagingPerspective((UICockpitPerspective)this);
        createTemplateList("AbstractConstraint");
        BrowserModel browserModel = getBrowserArea().getFocusedBrowser();
        if(browserModel instanceof SearchBrowserModel)
        {
            SearchBrowserModel searchBrowser = (SearchBrowserModel)browserModel;
            if(searchBrowser.getResult() == null)
            {
                searchBrowser.updateItems(0);
            }
        }
        try
        {
            if(getActiveItem() != null)
            {
                activateItemInEditor(getActiveItem());
            }
        }
        catch(Exception e)
        {
            LOG.warn("Error occurred when trying to load active item (Reason: '" + e.getMessage() + "').");
        }
    }


    public void activateItemInEditorBasic(TypedObject activeItem)
    {
        activateItemInEditorArea(activeItem);
    }


    @Required
    public void setObjectCollectionService(ObjectCollectionService objectCollectionService)
    {
        this.objectCollectionService = objectCollectionService;
    }


    public ObjectCollectionService getObjectCollectionService()
    {
        return this.objectCollectionService;
    }


    @Required
    public void setNewItemService(NewItemService newItemService)
    {
        this.newItemService = newItemService;
    }


    public NewItemService getNewItemService()
    {
        return this.newItemService;
    }
}
