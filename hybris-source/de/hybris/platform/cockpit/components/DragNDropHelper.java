package de.hybris.platform.cockpit.components;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.dragdrop.DragAndDropWrapper;
import de.hybris.platform.cockpit.services.dragdrop.DraggedItem;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.List;
import org.zkoss.zk.ui.Component;

public class DragNDropHelper
{
    public static List<TypedObject> getDraggedItems(Component itemFromEvent)
    {
        DraggedItem draggedItem = getDDWrapper().getDraggedItem(itemFromEvent);
        List<TypedObject> selItems = UISessionUtils.getCurrentSession().getCurrentPerspective().getBrowserArea().getFocusedBrowser().getSelectedItems();
        List<TypedObject> itemsToAdd = new ArrayList<>();
        if(selItems != null && !selItems.isEmpty() && selItems.contains(draggedItem.getSingleTypedObject()))
        {
            itemsToAdd.addAll(selItems);
        }
        else
        {
            itemsToAdd.add(draggedItem.getSingleTypedObject());
        }
        return itemsToAdd;
    }


    protected static DragAndDropWrapper getDDWrapper()
    {
        return UISessionUtils.getCurrentSession().getCurrentPerspective().getDragAndDropWrapperService().getWrapper();
    }
}
