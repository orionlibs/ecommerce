package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.impl.DefaultBrowserSectionListViewListener;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.session.ListBrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.spring.SpringUtil;

public class CmsBrowserSectionListViewlListener extends DefaultBrowserSectionListViewListener
{
    private static final Logger LOG = Logger.getLogger(CmsBrowserSectionListViewlListener.class);
    private CMSPageService cmsPageService;


    public CmsBrowserSectionListViewlListener(ListBrowserSectionModel sectionModel, MutableTableModel model)
    {
        super(sectionModel, model);
    }


    public void move(int fromIndex, int toIndex)
    {
        if(this.model.getListComponentModel().isEditable() && this.model.getListComponentModel().getListModel() instanceof DefaultListModel)
        {
            ((DefaultListModel)this.model.getListComponentModel().getListModel()).moveElement(fromIndex, toIndex);
            List<? extends TypedObject> listRows = this.model.getListComponentModel().getListModel().getElements();
            ContentSlotModel contentSlotModel = null;
            Object rootObject = this.sectionModel.getRootItem();
            if(rootObject instanceof TypedObject)
            {
                contentSlotModel = (ContentSlotModel)((TypedObject)this.sectionModel.getRootItem()).getObject();
            }
            else if(this.sectionModel.getRootItem() instanceof ContentSlotModel)
            {
                contentSlotModel = (ContentSlotModel)this.sectionModel.getRootItem();
            }
            if(contentSlotModel != null)
            {
                List<AbstractCMSComponentModel> elements = new ArrayList<>();
                for(TypedObject typedObject : listRows)
                {
                    if(typedObject.getObject() instanceof AbstractCMSComponentModel)
                    {
                        elements.add((AbstractCMSComponentModel)typedObject.getObject());
                        continue;
                    }
                    LOG.warn("Move encountered a problem. Unexpected element type.");
                }
                contentSlotModel.setCmsComponents(elements);
            }
            UISessionUtils.getCurrentSession().getModelService().save(contentSlotModel);
            if(rootObject instanceof TypedObject)
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null, (TypedObject)rootObject, Collections.EMPTY_LIST));
            }
            Collection<AbstractPageModel> pages = getCmsPageService().getPagesForContentSlots(
                            Collections.singletonList(contentSlotModel));
            for(AbstractPageModel abstractPageModel : pages)
            {
                UISessionUtils.getCurrentSession().sendGlobalEvent((CockpitEvent)new ItemChangedEvent(null,
                                UISessionUtils.getCurrentSession().getTypeService().wrapItem(abstractPageModel), Collections.EMPTY_LIST));
            }
            LOG.info("move [from: " + fromIndex + "] -> [to: " + toIndex + "]");
        }
    }


    public CMSPageService getCmsPageService()
    {
        if(this.cmsPageService == null)
        {
            return this.cmsPageService = (CMSPageService)SpringUtil.getBean("cmsPageService");
        }
        return this.cmsPageService;
    }
}
