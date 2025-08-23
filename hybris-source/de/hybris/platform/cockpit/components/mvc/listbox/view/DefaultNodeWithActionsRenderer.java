package de.hybris.platform.cockpit.components.mvc.listbox.view;

import de.hybris.platform.cockpit.components.mvc.listbox.listeners.DeleteListener;
import org.zkoss.lang.Objects;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Toolbarbutton;

public class DefaultNodeWithActionsRenderer extends AbstractNodeRenderer
{
    protected static final String REMOVE_LABEL_KEY = "general.remove";


    public DefaultNodeWithActionsRenderer()
    {
    }


    public DefaultNodeWithActionsRenderer(EventListener dropListener)
    {
        super(dropListener);
    }


    public Listitem renderListitem(Listitem listItem, Object node)
    {
        Listcell labelCell = new Listcell(Objects.toString(node));
        Listcell actionsCell = new Listcell();
        listItem.setValue(node);
        labelCell.setParent((Component)listItem);
        actionsCell.setParent((Component)listItem);
        addActions(actionsCell);
        return listItem;
    }


    protected void addActions(Listcell actionsCell)
    {
        Toolbarbutton deleteButton = new Toolbarbutton("", "/cmscockpit/images/node_delete.png");
        deleteButton.setTooltiptext(Labels.getLabel("general.remove"));
        deleteButton.addEventListener("onClick", (EventListener)new DeleteListener());
        actionsCell.appendChild((Component)deleteButton);
    }
}
