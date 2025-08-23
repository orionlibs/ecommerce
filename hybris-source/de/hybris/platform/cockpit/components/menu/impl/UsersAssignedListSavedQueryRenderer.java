package de.hybris.platform.cockpit.components.menu.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UISavedQuery;
import de.hybris.platform.cockpit.services.query.SavedQueryService;
import de.hybris.platform.cockpit.services.query.SavedQueryUserRightsService;
import de.hybris.platform.cockpit.services.query.impl.DummySavedQueryUserRightsServiceImpl;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.List;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listitem;

public class UsersAssignedListSavedQueryRenderer extends AbstractUsersAssignedListRenderer
{
    private UISavedQuery query = null;
    private String right = "";


    public UsersAssignedListSavedQueryRenderer(List<TypedObject> assignedValuesList, UISavedQuery query, String right)
    {
        super(assignedValuesList);
        this.query = query;
        this.right = right;
    }


    public EventListener getRemoveImageListener(Listitem item, Object data)
    {
        return (EventListener)new Object(this, data, item);
    }


    protected UISavedQuery getQuery()
    {
        return this.query;
    }


    protected void setQuery(UISavedQuery query)
    {
        this.query = query;
    }


    protected String getRight()
    {
        return this.right;
    }


    protected void setRight(String right)
    {
        this.right = right;
    }


    private SavedQueryUserRightsService getSavedQueryService()
    {
        SavedQueryService savedQueryService = UISessionUtils.getCurrentSession().getCurrentPerspective().getNavigationArea().getSavedQueryService();
        if(savedQueryService instanceof SavedQueryUserRightsService)
        {
            return (SavedQueryUserRightsService)savedQueryService;
        }
        return (SavedQueryUserRightsService)new DummySavedQueryUserRightsServiceImpl(savedQueryService);
    }
}
