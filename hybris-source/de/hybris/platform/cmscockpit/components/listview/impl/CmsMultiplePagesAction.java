package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSContentSlotService;
import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.Collection;
import java.util.HashSet;
import javax.annotation.Resource;
import org.apache.log4j.Logger;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Menupopup;

public class CmsMultiplePagesAction extends AbstractListViewAction
{
    private static final Logger LOG = Logger.getLogger(CmsMultiplePagesAction.class);
    @Resource(name = "cmsContentSlotService")
    private CMSContentSlotService cmsContentSlotService;


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
            Collection<TypedObject> pagesForElement = getPagesForElement((AbstractCMSComponentModel)context
                            .getItem().getObject());
            if(pagesForElement.size() > 1)
            {
                return "/cmscockpit/images/component_partof_template.gif";
            }
        }
        catch(Exception e)
        {
            LOG.error("Failed to retrieve the image URI", e);
        }
        return null;
    }


    public Menupopup getPopup(ListViewAction.Context context)
    {
        return null;
    }


    public String getTooltip(ListViewAction.Context context)
    {
        return null;
    }


    public boolean isAlwaysEnabled()
    {
        return true;
    }


    public void setCmsContentSlotService(CMSContentSlotService cmsContentSlotService)
    {
        this.cmsContentSlotService = cmsContentSlotService;
    }


    protected void doCreateContext(ListViewAction.Context context)
    {
    }


    protected Collection<TypedObject> getPagesForElement(AbstractCMSComponentModel componentModel)
    {
        Collection<TypedObject> ret = new HashSet<>();
        Collection<ContentSlotModel> slots = componentModel.getSlots();
        for(ContentSlotModel contentSlotModel : slots)
        {
            Collection<AbstractPageModel> pages = this.cmsContentSlotService.getPagesForContentSlot(contentSlotModel);
            for(AbstractPageModel abstractPage : pages)
            {
                try
                {
                    ret.add(UISessionUtils.getCurrentSession().getTypeService().wrapItem(abstractPage));
                }
                catch(Exception e)
                {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return ret;
    }
}
