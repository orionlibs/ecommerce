package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cmscockpit.components.listview.CmsPageBrowserAction;
import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.user.UserModel;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zkplus.spring.SpringUtil;
import org.zkoss.zul.Menupopup;

public class PageLockBrowserAction extends AbstractListViewAction implements CmsPageBrowserAction
{
    private boolean forceDisplay = false;
    public static final String RED_LOCKED_ICON = "/cmscockpit/images/icon-lock-red.gif";
    private ListViewAction.Context context;
    private CMSPageLockingService cmsPageLockingService;


    public String getImageURI(ListViewAction.Context context)
    {
        if(isForceDisplay())
        {
            return isPageLocked(context) ? "/cmscockpit/images/icon-lock-red.gif" : "/cockpit/images/icon_unlocked.png";
        }
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return (EventListener)new Object(this, context);
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        if(isPageLocked(context))
        {
            return Labels.getLabel("cmscockpit.action.pagelocking.unlockPage");
        }
        return Labels.getLabel("cmscockpit.action.pagelocking.lockPage");
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
        this.context = context;
    }


    public String getPrefixLabel()
    {
        if(isPageLocked(this.context))
        {
            return Labels.getLabel("cmscockpit.action.pagelocking.pageIsLocked", new Object[] {getContextPage(this.context).getLockedBy().getDisplayName()});
        }
        return null;
    }


    protected boolean isPageLocked(ListViewAction.Context context)
    {
        return (getContextPage(context).getLockedBy() != null);
    }


    protected boolean isOwnLock(ListViewAction.Context context)
    {
        return (getContextPage(context).getLockedBy() != null && getContextPage(context).getLockedBy().equals(getCurrentUser()));
    }


    protected boolean isUserAbleToUnlock(ListViewAction.Context context)
    {
        return (isOwnLock(context) || getCurrentUser().isAuthorizedToUnlockPages());
    }


    public CMSPageLockingService getCmsPageLockingService()
    {
        if(this.cmsPageLockingService == null)
        {
            this.cmsPageLockingService = (CMSPageLockingService)SpringUtil.getBean("cmsPageLockingService");
        }
        return this.cmsPageLockingService;
    }


    public void setForceDisplay(boolean forceDisplay)
    {
        this.forceDisplay = forceDisplay;
    }


    public boolean isForceDisplay()
    {
        return this.forceDisplay;
    }


    protected UserModel getCurrentUser()
    {
        return UISessionUtils.getCurrentSession().getSystemService().getCurrentUser();
    }


    protected AbstractPageModel getContextPage(ListViewAction.Context context)
    {
        return (AbstractPageModel)context.getItem().getObject();
    }
}
