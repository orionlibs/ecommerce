package de.hybris.platform.cockpit.components.mvc.listbox.view;

import de.hybris.platform.cockpit.components.mvc.listbox.listeners.DropListener;
import de.hybris.platform.cockpit.components.mvc.listbox.listeners.KeyStrokeListener;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public abstract class AbstractNodeRenderer implements ListitemRenderer
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


    public void render(Listitem item, Object data) throws Exception
    {
        renderListitem(item, data);
        enableDND(item);
        item.setCtrlKeys("#del");
        item.addEventListener("onCtrlKey", (EventListener)new KeyStrokeListener());
    }


    private void enableDND(Listitem listItem)
    {
        listItem.setDraggable("pageItem");
        listItem.setDroppable("pageItem");
        listItem.addEventListener("onDrop", this.dropListener);
    }


    public abstract Listitem renderListitem(Listitem paramListitem, Object paramObject);
}
