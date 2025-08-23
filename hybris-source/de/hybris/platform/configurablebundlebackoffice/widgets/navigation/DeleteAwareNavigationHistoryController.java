package de.hybris.platform.configurablebundlebackoffice.widgets.navigation;

import com.hybris.cockpitng.annotations.GlobalCockpitEvent;
import com.hybris.cockpitng.core.events.CockpitEvent;
import com.hybris.cockpitng.widgets.navigation.NavigationHistoryController;
import de.hybris.platform.core.model.ItemModel;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DeleteAwareNavigationHistoryController extends NavigationHistoryController
{
    protected void restoreCurrentItem()
    {
        NavigationHistoryController.HistoryElement currentItemObject = (NavigationHistoryController.HistoryElement)getModel().getValue("currentItem", NavigationHistoryController.HistoryElement.class);
        if(Objects.nonNull(currentItemObject) && isValidElement(currentItemObject))
        {
            super.restoreCurrentItem();
        }
    }


    protected boolean isValidElement(NavigationHistoryController.HistoryElement currentItemObject)
    {
        return (currentItemObject.getMessage().getClass().isInstance(ItemModel.class) &&
                        !((ItemModel)currentItemObject.getMessage()).getItemModelContext().isRemoved());
    }


    @GlobalCockpitEvent(eventName = "objectsDeleted", scope = "session")
    public void handleObjectDeletedEvent(CockpitEvent event)
    {
        removeDeletedObjectsFromHistory(event);
        removeDeletedItemSelection(event);
    }


    protected void removeDeletedItemSelection(CockpitEvent event)
    {
        NavigationHistoryController.HistoryElement currentItemObject = (NavigationHistoryController.HistoryElement)getModel().getValue("currentItem", NavigationHistoryController.HistoryElement.class);
        if(Objects.nonNull(currentItemObject) && event.getData().equals(currentItemObject.getMessage()))
        {
            getModel().setValue("currentItem", null);
        }
    }


    protected void removeDeletedObjectsFromHistory(CockpitEvent event)
    {
        List<NavigationHistoryController.HistoryElement> history = (List<NavigationHistoryController.HistoryElement>)getModel().getValue("currentItem", List.class);
        if(Objects.nonNull(history))
        {
            Object data = event.getData();
            List<NavigationHistoryController.HistoryElement> newHistory = (List<NavigationHistoryController.HistoryElement>)history.stream().filter(item -> Objects.nonNull(item.getMessage())).filter(item -> !item.getMessage().equals(data)).collect(Collectors.toList());
            getModel().setValue("history", newHistory);
        }
    }
}
