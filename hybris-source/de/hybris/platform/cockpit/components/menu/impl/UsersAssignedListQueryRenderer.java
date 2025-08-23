package de.hybris.platform.cockpit.components.menu.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.model.query.impl.UICollectionQuery;
import java.util.List;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listitem;

public class UsersAssignedListQueryRenderer extends AbstractUsersAssignedListRenderer
{
    private UICollectionQuery query = null;
    private String right = "";


    public UsersAssignedListQueryRenderer(List<TypedObject> assignedValuesList, UICollectionQuery query, String right)
    {
        super(assignedValuesList);
        this.query = query;
        this.right = right;
    }


    public EventListener getRemoveImageListener(Listitem item, Object data)
    {
        return (EventListener)new Object(this, data, item);
    }


    protected UICollectionQuery getQuery()
    {
        return this.query;
    }


    protected void setQuery(UICollectionQuery query)
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
}
