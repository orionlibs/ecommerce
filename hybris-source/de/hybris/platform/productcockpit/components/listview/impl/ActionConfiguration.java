package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import java.util.Collections;
import java.util.List;

public class ActionConfiguration
{
    private List<ListViewAction> actions;


    public void setActions(List<ListViewAction> actions)
    {
        this.actions = actions;
    }


    public List<ListViewAction> getActions()
    {
        return (this.actions == null) ? Collections.EMPTY_LIST : this.actions;
    }
}
