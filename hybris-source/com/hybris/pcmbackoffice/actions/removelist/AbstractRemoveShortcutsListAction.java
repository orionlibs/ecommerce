package com.hybris.pcmbackoffice.actions.removelist;

import com.hybris.backoffice.model.BackofficeObjectSpecialCollectionModel;
import com.hybris.backoffice.widgets.notificationarea.event.NotificationEvent;
import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.util.notifications.NotificationService;
import com.hybris.pcmbackoffice.widgets.shortcuts.ShortcutsUtilService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.pcmbackoffice.services.ShortcutsService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractRemoveShortcutsListAction implements CockpitAction<Object, Object>
{
    private ShortcutsService shortcutsService;
    private NotificationService notificationService;
    private ShortcutsUtilService shortcutsUtilService;


    public ActionResult<Object> perform(ActionContext<Object> ctx)
    {
        List<ItemModel> selectedItems = getDataAsCollection(ctx);
        BackofficeObjectSpecialCollectionModel collectionModel = getShortcutsService().initCollection(getQualifer());
        if(getShortcutsUtilService().isItemsAlreadyDeleted(selectedItems))
        {
            return new ActionResult("error");
        }
        List<ItemModel> itemsToRemove = getShortcutsUtilService().getAlreadyExistItems(selectedItems, collectionModel);
        for(ItemModel item : itemsToRemove)
        {
            getShortcutsService().deleteProductFromCollectionlist((ProductModel)item, collectionModel);
        }
        showSuccessNotification(getSuccessMessage());
        return new ActionResult("success", selectedItems);
    }


    protected abstract String getQualifer();


    protected abstract String getSuccessMessage();


    private List<ItemModel> getDataAsCollection(ActionContext<Object> ctx)
    {
        List<ItemModel> ctxObjects = new ArrayList<>();
        if(ctx.getData() instanceof Collection)
        {
            ctxObjects.addAll((Collection<? extends ItemModel>)ctx.getData());
        }
        else
        {
            ctxObjects.add((ItemModel)ctx.getData());
        }
        return ctxObjects;
    }


    private void showSuccessNotification(String source)
    {
        getNotificationService().notifyUser(source, "General", NotificationEvent.Level.SUCCESS, new Object[0]);
    }


    public boolean canPerform(ActionContext<Object> ctx)
    {
        if(ctx.getData() == null)
        {
            return false;
        }
        if(ctx.getData() instanceof Collection)
        {
            Collection<?> selectedItems = (Collection)ctx.getData();
            return CollectionUtils.isNotEmpty(selectedItems);
        }
        return true;
    }


    public ShortcutsService getShortcutsService()
    {
        return this.shortcutsService;
    }


    @Required
    public void setShortcutsService(ShortcutsService shortcutsService)
    {
        this.shortcutsService = shortcutsService;
    }


    @Required
    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    public NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    @Required
    public void setShortcutsUtilService(ShortcutsUtilService shortcutsUtilService)
    {
        this.shortcutsUtilService = shortcutsUtilService;
    }


    public ShortcutsUtilService getShortcutsUtilService()
    {
        return this.shortcutsUtilService;
    }
}
