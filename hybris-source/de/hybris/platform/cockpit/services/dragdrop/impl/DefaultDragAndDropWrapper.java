package de.hybris.platform.cockpit.services.dragdrop.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.services.dragdrop.DraggedItem;
import de.hybris.platform.cockpit.session.BrowserModel;
import org.zkoss.zk.ui.Component;

public class DefaultDragAndDropWrapper implements DragAndDropWrapper
{
    public void attachDraggedItem(DraggedItem item, Component component)
    {
        component.setAttribute("typedItem", item.getSingleTypedObject());
        component.setAttribute("superCategory", item.getSuperCategory());
        component.setAttribute("browser", item.getBrowser());
    }


    public DraggedItem getDraggedItem(Component component)
    {
        TypedObject typedObject = (TypedObject)component.getAttribute("typedItem");
        TypedObject superCategory = (TypedObject)component.getAttribute("superCategory");
        BrowserModel browserModel = (BrowserModel)component.getAttribute("browser");
        return (DraggedItem)new DefaultDraggedItem(typedObject, browserModel, superCategory);
    }
}
