package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.restrictions.AbstractRestrictionModel;
import de.hybris.platform.cmscockpit.components.listview.CmsPageBrowserAction;
import de.hybris.platform.cmscockpit.services.CmsCockpitService;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menuitem;
import org.zkoss.zul.Menupopup;
import org.zkoss.zul.Menuseparator;

public class CmsRestrictionAction extends AbstractCmscockpitListViewAction implements CmsPageBrowserAction
{
    private static final Logger LOG = Logger.getLogger(CmsRestrictionAction.class);
    private CmsCockpitService cmsCockpitService = null;
    private boolean forceDisplay = false;


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    public Menupopup getContextPopup(ListViewAction.Context context)
    {
        return null;
    }


    public EventListener getEventListener(ListViewAction.Context context)
    {
        return null;
    }


    public String getImageURI(ListViewAction.Context context)
    {
        try
        {
            if(isForceDisplay() || CollectionUtils.isNotEmpty(getRestrictions(context.getItem())))
            {
                return "/cmscockpit/images/icon_func_restriction.png";
            }
        }
        catch(Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
        return null;
    }


    protected List<TypedObject> getRestrictions(TypedObject component)
    {
        Object object = component.getObject();
        if(object instanceof AbstractCMSComponentModel)
        {
            List<AbstractRestrictionModel> restrictions = ((AbstractCMSComponentModel)object).getRestrictions();
            if(restrictions != null)
            {
                return UISessionUtils.getCurrentSession().getTypeService().wrapItems(restrictions);
            }
        }
        else if(object instanceof AbstractPageModel)
        {
            List<AbstractRestrictionModel> restrictions = ((AbstractPageModel)object).getRestrictions();
            if(restrictions != null)
            {
                return UISessionUtils.getCurrentSession().getTypeService().wrapItems(restrictions);
            }
        }
        return Collections.EMPTY_LIST;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        if(isComponentLockedForUser(context.getItem()))
        {
            return null;
        }
        Menupopup ret = new Menupopup();
        for(TypedObject restriction : getRestrictions(context.getItem()))
        {
            Menuitem mitem = new Menuitem();
            String objectTextLabel = UISessionUtils.getCurrentSession().getLabelService().getObjectTextLabel(restriction);
            mitem.setLabel("- " + objectTextLabel);
            mitem.setTooltiptext(getCmsCockpitService().createRestrictionTooltip(restriction));
            mitem.addEventListener("onClick", (EventListener)new Object(this, restriction));
            ret.appendChild((Component)mitem);
        }
        if(CollectionUtils.isNotEmpty(ret.getChildren()))
        {
            ret.appendChild((Component)new Menuseparator());
        }
        Menuitem openWizardItem = new Menuitem(Labels.getLabel("cmscockpit.action.restriction.addWizard"));
        openWizardItem.setSclass("restriction_wizard_entry");
        openWizardItem.addEventListener("onClick", (EventListener)new Object(this, context));
        ret.appendChild((Component)openWizardItem);
        return ret;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return Labels.getLabel("cmscockpit.action.restriction.tooltip");
    }


    public void setForceDisplay(boolean forceDisplay)
    {
        this.forceDisplay = forceDisplay;
    }


    public boolean isForceDisplay()
    {
        return this.forceDisplay;
    }


    public String getPrefixLabel()
    {
        return Labels.getLabel("cmscockpit.action.restriction.pagePrefix");
    }


    public CmsCockpitService getCmsCockpitService()
    {
        if(this.cmsCockpitService == null)
        {
            this.cmsCockpitService = (CmsCockpitService)SpringUtil.getBean("cmsCockpitService");
        }
        return this.cmsCockpitService;
    }
}
