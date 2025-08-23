package de.hybris.platform.productcockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.AbstractSynchronizationAction;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import org.zkoss.zk.ui.event.EventListener;

public class ProductSynchronizationStatus extends AbstractSynchronizationAction
{
    protected static final String ICON_STATUS_IN_SYNC = "cockpit/images/icon_status_sync.png";
    protected static final String ICON_STATUS_IN_SYNC_NOT = "cockpit/images/icon_status_unsync.png";
    protected static final String ICON_STATUS_SYNC_INITIAL = "cockpit/images/icon_status_sync_initial.png";


    protected String getTypeRestriction()
    {
        return "Product";
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
        SynchronizationService.SyncContext syncCtx = getSynchronizationService().getSyncContext(context.getItem());
        context.getMap().put("syncJobs", syncCtx.getSyncJobs());
        context.getMap().put("sourceCatalogVersion", getSynchronizationService().getCatalogVersionForItem(context.getItem()));
        context.getMap().put("currentStatus", Integer.valueOf(syncCtx.isProductSynchronized()));
        context.getMap().put("affectedItems", syncCtx.getAffectedItems());
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        return "cockpit/images/icon_status_unsync.png";
    }


    protected String getSyncInitImg()
    {
        return "cockpit/images/icon_status_sync_initial.png";
    }


    protected String getSyncNotOKImg()
    {
        return "cockpit/images/icon_status_unsync.png";
    }


    protected String getSyncOKImg()
    {
        return "cockpit/images/icon_status_sync.png";
    }
}
