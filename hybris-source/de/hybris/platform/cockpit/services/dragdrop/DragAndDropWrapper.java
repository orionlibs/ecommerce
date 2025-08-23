package de.hybris.platform.cockpit.services.dragdrop;

import org.zkoss.zk.ui.Component;

public interface DragAndDropWrapper
{
    DraggedItem getDraggedItem(Component paramComponent);


    void attachDraggedItem(DraggedItem paramDraggedItem, Component paramComponent);
}
