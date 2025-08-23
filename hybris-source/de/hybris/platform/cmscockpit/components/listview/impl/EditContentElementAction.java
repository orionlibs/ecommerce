package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.session.UISessionUtils;
import org.apache.log4j.Logger;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class EditContentElementAction extends AbstractCmscockpitListViewAction
{
    private static final Logger LOG = Logger.getLogger(EditContentElementAction.class);
    private static final String EDIT_CONTENT_ELEMENT_ACTION_URL = "/cmscockpit/images/icon_func_edit_available.png";


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        boolean canEdit = getSystemService().checkPermissionOn(context.getItem().getType().getCode(), "change");
        if(!isEnabled() || isComponentLockedForUser(context.getItem()) || !canEdit)
        {
            return null;
        }
        return "/cmscockpit/images/icon_func_edit_available.png";
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("general.edit");
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        Object object = null;
        if(!isComponentLockedForUser(context.getItem()))
        {
            object = new Object(this, context);
        }
        return (EventListener)object;
    }


    public TypeService getTypeService()
    {
        return UISessionUtils.getCurrentSession().getTypeService();
    }
}
