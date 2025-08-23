package de.hybris.platform.cockpit.components.mvc.tree.view;

import org.zkoss.lang.Objects;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

public class DefaultNodeRenderer extends AbstractNodeRenderer
{
    public DefaultNodeRenderer(EventListener dropListener)
    {
        super(dropListener);
    }


    public Treerow renderRow(Treeitem treeItem, Object node)
    {
        Treecell treeCell = new Treecell(Objects.toString(node));
        enchanceTreecell(treeCell);
        Treerow treeRow = null;
        treeItem.setValue(node);
        if(treeItem.getTreerow() == null)
        {
            treeRow = new Treerow();
            treeRow.setParent((Component)treeItem);
        }
        else
        {
            treeRow = treeItem.getTreerow();
            treeRow.getChildren().clear();
        }
        treeCell.setParent((Component)treeRow);
        return treeRow;
    }


    private void enchanceTreecell(Treecell treeCell)
    {
    }
}
