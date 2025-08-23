package de.hybris.platform.cockpit.components.menu;

import org.zkoss.zk.ui.HtmlBasedComponent;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.ListitemRenderer;

public interface AssignedListRenderer
{
    HtmlBasedComponent createMenuListViewComponent();


    ListitemRenderer createAvailableCollectionMenuItemListRenderer();


    EventListener getRemoveImageListener(Listitem paramListitem, Object paramObject);
}
