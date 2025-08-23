package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.AbstractSynchronizationAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;

public class PullSynchronizationAction extends AbstractSynchronizationAction
{
    protected static final String ICON_PULL_SYNC_ACTION_AVAILABLE = "cockpit/images/icon_func_sync_fromsource.png";
    protected static final String ICON_PULL_SYNC_ACTION_UNAVAILABLE = "cockpit/images/icon_func_sync_fromsource_unavailable.png";


    protected void doCreateContext(ListViewAction.Context context)
    {
        SynchronizationService.SyncContext syncCtx = getSynchronizationService().getSyncContext(context.getItem(), true);
        context.getMap().put("sourceItems", syncCtx.getSourceItemModels());
        context.getMap().put("currentStatus", Integer.valueOf(syncCtx.getPullSyncStatus()));
        context.getMap().put("affectedItems", syncCtx.getAffectedItems());
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        TypedObject item = getItem(context);
        if(item == null)
        {
            return null;
        }
        SynchronizationService.SyncContext syncCtx = getSynchronizationService().getSyncContext(item, true);
        if(syncCtx.getPullSyncStatus() != 1)
        {
            return null;
        }
        return (EventListener)new Object(this, context);
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        Object object = null;
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(CollectionUtils.isNotEmpty(selectedItems) && selectedItems.size() == 1)
        {
            SynchronizationService.SyncContext syncCtx = getSynchronizationService().getSyncContext(selectedItems.iterator().next(), true);
            if(syncCtx.getPullSyncStatus() == 1)
            {
                object = new Object(this, context, selectedItems);
            }
        }
        return (EventListener)object;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        List<TypedObject> selectedItems = getSelectedItems(context);
        if(CollectionUtils.isNotEmpty(selectedItems) && selectedItems.size() == 1)
        {
            SynchronizationService.SyncContext syncCtx = getSynchronizationService().getSyncContext(selectedItems.iterator().next(), true);
            if(syncCtx.getPullSyncStatus() != 1)
            {
                return "cockpit/images/icon_func_sync_fromsource_unavailable.png";
            }
            return "cockpit/images/icon_func_sync_fromsource.png";
        }
        return "cockpit/images/icon_func_sync_fromsource_unavailable.png";
    }


    public String getImageURI(ListViewAction.Context context)
    {
        TypedObject item = getItem(context);
        if(item != null)
        {
            SynchronizationService.SyncContext syncCtx = getSynchronizationService().getSyncContext(item, true);
            if(syncCtx.getPullSyncStatus() != 1)
            {
                return "cockpit/images/icon_func_sync_fromsource_unavailable.png";
            }
            return "cockpit/images/icon_func_sync_fromsource.png";
        }
        return "cockpit/images/icon_func_sync_fromsource_unavailable.png";
    }


    protected String getSyncInitImg()
    {
        return "cockpit/images/icon_func_sync_fromsource.png";
    }


    protected String getSyncNotOKImg()
    {
        return "cockpit/images/icon_func_sync_fromsource.png";
    }


    protected String getSyncOKImg()
    {
        return "cockpit/images/icon_func_sync_fromsource.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("sync.pull");
    }
}
