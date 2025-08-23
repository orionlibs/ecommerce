package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.components.listview.impl.AbstractSynchronizationAction;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Window;

public class CmsElementSyncAction extends AbstractSynchronizationAction
{
    protected static final String IMAGE_URI_MULTISELECT_AVAILABLE = "/cockpit/images/icon_func_sync.png";
    protected static final String IMAGE_URI_MULTISELECT_UNAVAILABLE = "/cockpit/images/icon_func_sync_unavailable.png";
    private static final Logger LOG = Logger.getLogger(CmsElementSyncAction.class);
    private ListViewAction.Context context;


    protected void doCreateContext(ListViewAction.Context context)
    {
        SynchronizationService.SyncContext syncCtx = getSynchronizationService().getSyncContext(context.getItem());
        context.getMap().put("syncJobs", syncCtx.getSyncJobs());
        context.getMap().put("sourceCatalogVersion", getSynchronizationService().getCatalogVersionForItem(context.getItem()));
        context.getMap().put("currentStatus", Integer.valueOf(syncCtx.isProductSynchronized()));
        context.getMap().put("affectedItems", syncCtx.getAffectedItems());
        this.context = context;
    }


    protected String getSyncInitImg()
    {
        boolean canChange = UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(this.context.getItem().getType().getCode(), "change");
        if(isComponentLockedForUser(this.context.getItem()) || !canChange)
        {
            return null;
        }
        return "cockpit/images/synchronization_init.gif";
    }


    protected String getSyncNotOKImg()
    {
        boolean canChange = UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(this.context.getItem().getType().getCode(), "change");
        if(isComponentLockedForUser(this.context.getItem()) || !canChange)
        {
            return null;
        }
        return "cockpit/images/synchronization_notok.gif";
    }


    protected String getSyncOKImg()
    {
        boolean canChange = UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(this.context.getItem().getType().getCode(), "change");
        if(isComponentLockedForUser(this.context.getItem()) || !canChange)
        {
            return null;
        }
        return "cockpit/images/synchronization_ok.gif";
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    protected void detachDialog(Window dialog)
    {
        dialog.addEventListener("onOpen", (EventListener)new Object(this, dialog));
    }


    protected boolean isComponentLockedForUser(TypedObject item)
    {
        boolean result = false;
        if(UISessionUtils.getCurrentSession().getTypeService().getBaseType(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT)
                        .isAssignableFrom((ObjectType)item.getType()))
        {
            result = getCmsPageLockingService().isComponentLockedForUser((AbstractCMSComponentModel)item.getObject(),
                            UISessionUtils.getCurrentSession().getSystemService().getCurrentUser());
        }
        else if(UISessionUtils.getCurrentSession().getTypeService().getBaseType(GeneratedCms2Constants.TC.ABSTRACTPAGE)
                        .isAssignableFrom((ObjectType)item.getType()))
        {
            result = getCmsPageLockingService().isPageLockedFor((AbstractPageModel)item.getObject(),
                            UISessionUtils.getCurrentSession().getSystemService().getCurrentUser());
        }
        return result;
    }


    protected CMSPageLockingService getCmsPageLockingService()
    {
        return (CMSPageLockingService)SpringUtil.getBean("cmsPageLockingService");
    }


    protected CMSPageService getCmsPageService()
    {
        return (CMSPageService)SpringUtil.getBean("cmsPageService");
    }


    public String getMultiSelectImageURI(ListViewAction.Context context)
    {
        if(!context.getBrowserModel().getSelectedItems().isEmpty())
        {
            return "/cockpit/images/icon_func_sync.png";
        }
        return "/cockpit/images/icon_func_sync_unavailable.png";
    }


    public EventListener getMultiSelectEventListener(ListViewAction.Context context)
    {
        if(context.getBrowserModel().getSelectedItems().isEmpty())
        {
            return null;
        }
        return (EventListener)new Object(this, context);
    }


    protected boolean canUserChangeItem(TypedObject item)
    {
        boolean canChange = UISessionUtils.getCurrentSession().getSystemService().checkPermissionOn(item.getType().getCode(), "change");
        return (!isComponentLockedForUser(item) && canChange);
    }
}
