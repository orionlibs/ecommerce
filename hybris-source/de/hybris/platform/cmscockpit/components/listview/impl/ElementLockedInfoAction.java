package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class ElementLockedInfoAction extends AbstractCmscockpitListViewAction
{
    public final String RED_LOCKED_ICON = "/cmscockpit/images/icon-lock-red.gif";


    public String getImageURI(ListViewAction.Context context)
    {
        if(isComponentLockedForUser(context.getItem()))
        {
            return "/cmscockpit/images/icon-lock-red.gif";
        }
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
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
        if(isComponentLockedForUser(context.getItem()))
        {
            return Labels.getLabel("cmscockpit.pagelocking.componentLocked", new Object[] {getLockersNames(getCmsPageLockingService().getComponentLockers((AbstractCMSComponentModel)context
                            .getItem().getObject()))});
        }
        return null;
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    protected String getLockersNames(Collection<UserModel> users)
    {
        StringBuffer buffer = new StringBuffer();
        for(UserModel user : users)
        {
            buffer.append(user.getDisplayName()).append(", ");
        }
        return buffer.substring(0, buffer.length() - 2).toString();
    }
}
