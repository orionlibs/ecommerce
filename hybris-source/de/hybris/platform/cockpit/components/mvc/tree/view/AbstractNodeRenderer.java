package de.hybris.platform.cockpit.components.mvc.tree.view;

import de.hybris.platform.cockpit.components.mvc.tree.listeners.DoubleClickListener;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.DropListener;
import de.hybris.platform.cockpit.components.mvc.tree.listeners.KeyStrokeListener;
import de.hybris.platform.cockpit.util.UITools;
import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Treeitem;
import org.zkoss.zul.TreeitemRenderer;
import org.zkoss.zul.Treerow;

public abstract class AbstractNodeRenderer implements TreeitemRenderer
{
    private final EventListener dropListener;


    public AbstractNodeRenderer()
    {
        this.dropListener = (EventListener)new DropListener();
    }


    public AbstractNodeRenderer(EventListener dropListener)
    {
        this.dropListener = dropListener;
    }


    public void render(Treeitem item, Object data) throws Exception
    {
        Treerow treeRow = renderRow(item, data);
        treeRow.setDraggable("true");
        treeRow.setDroppable("true");
        treeRow.addEventListener("onDrop", this.dropListener);
        registerDefaultOnDoubleClickListeners(treeRow);
        treeRow.setCtrlKeys("#del");
        treeRow.addEventListener("onCtrlKey", (EventListener)new KeyStrokeListener());
        UITools.modifySClass((HtmlBasedComponent)treeRow.getFirstChild(), "tree_first_col", true);
        UITools.modifySClass((HtmlBasedComponent)treeRow.getLastChild(), "tree_last_col", true);
    }


    protected void registerDefaultOnDoubleClickListeners(Treerow treeRow)
    {
        treeRow.addEventListener("onDoubleClick", (EventListener)new DoubleClickListener());
    }


    public abstract Treerow renderRow(Treeitem paramTreeitem, Object paramObject);
}
