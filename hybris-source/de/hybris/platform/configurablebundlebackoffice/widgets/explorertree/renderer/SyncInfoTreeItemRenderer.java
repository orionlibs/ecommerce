package de.hybris.platform.configurablebundlebackoffice.widgets.explorertree.renderer;

import com.hybris.backoffice.navigation.NavigationNode;
import com.hybris.backoffice.sync.renderers.SyncStatusCellRenderer;
import com.hybris.cockpitng.widgets.common.explorertree.ExplorerTreeController;
import com.hybris.cockpitng.widgets.common.explorertree.renderer.DefaultTreeitemRenderer;
import de.hybris.platform.core.model.ItemModel;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Span;
import org.zkoss.zul.Treecell;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.Treerow;

public class SyncInfoTreeItemRenderer extends DefaultTreeitemRenderer
{
    private SyncStatusCellRenderer syncStatusCellRenderer;


    public SyncInfoTreeItemRenderer(ExplorerTreeController controller)
    {
        super(controller);
    }


    public void render(Treeitem treeitem, NavigationNode navigationNode, int i)
    {
        super.render(treeitem, navigationNode, i);
        Treerow treeRow = (Treerow)treeitem.getFirstChild();
        Treecell treeCell = (Treecell)treeRow.getLastChild();
        Span sync = new Span();
        sync.setSclass("sync-status-container");
        treeCell.getFirstChild().appendChild((Component)sync);
        ItemModel data = (ItemModel)navigationNode.getData();
        if(!data.getItemModelContext().isRemoved())
        {
            getSyncStatusCellRenderer().render((Component)sync, null, data, null, getController().getWidgetInstanceManager());
        }
    }


    protected SyncStatusCellRenderer getSyncStatusCellRenderer()
    {
        return this.syncStatusCellRenderer;
    }


    public void setSyncStatusCellRenderer(SyncStatusCellRenderer syncStatusCellRenderer)
    {
        this.syncStatusCellRenderer = syncStatusCellRenderer;
    }
}
