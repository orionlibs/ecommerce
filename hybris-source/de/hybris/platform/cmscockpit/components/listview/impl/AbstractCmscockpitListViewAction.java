package de.hybris.platform.cmscockpit.components.listview.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cockpit.components.listview.AbstractListViewAction;
import de.hybris.platform.cockpit.components.listview.ListViewAction;
import de.hybris.platform.cockpit.events.CockpitEvent;
import de.hybris.platform.cockpit.events.impl.ItemChangedEvent;
import de.hybris.platform.cockpit.model.general.impl.DefaultListModel;
import de.hybris.platform.cockpit.model.listview.MutableTableModel;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.listview.impl.SectionTableModel;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.security.UIAccessRightService;
import de.hybris.platform.cockpit.session.BrowserSectionModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;
import org.zkoss.zkplus.spring.SpringUtil;

public abstract class AbstractCmscockpitListViewAction extends AbstractListViewAction
{
    private CMSPageLockingService cmsPageLockingService;
    private CMSPageService cmsPageService;
    private UIAccessRightService uiAccessRightService;
    private TypeService typeService;
    private SystemService systemService;
    private static final Logger LOG = Logger.getLogger(AbstractCmscockpitListViewAction.class);


    protected boolean isComponentLockedForUser(TypedObject item)
    {
        boolean result = false;
        if(UISessionUtils.getCurrentSession().getTypeService().getBaseType("AbstractCMSComponent")
                        .isAssignableFrom((ObjectType)item.getType()))
        {
            result = getCmsPageLockingService().isComponentLockedForUser((AbstractCMSComponentModel)item.getObject(),
                            UISessionUtils.getCurrentSession().getSystemService().getCurrentUser());
        }
        else if(UISessionUtils.getCurrentSession().getTypeService().getBaseType("AbstractPage")
                        .isAssignableFrom((ObjectType)item.getType()))
        {
            result = getCmsPageLockingService().isPageLockedFor((AbstractPageModel)item.getObject(),
                            UISessionUtils.getCurrentSession().getSystemService().getCurrentUser());
        }
        return result;
    }


    protected boolean isEnabled()
    {
        if(getSystemService().checkPermissionOn("ContentSlot", "change"))
        {
            return true;
        }
        return isAlwaysEnabled();
    }


    protected boolean isWritableCatalog(TypedObject item)
    {
        boolean result = false;
        if(UISessionUtils.getCurrentSession().getTypeService().getBaseType("AbstractCMSComponent")
                        .isAssignableFrom((ObjectType)item.getType()))
        {
            result = getUIAccessRightService().isWritable((ObjectType)item.getType(),
                            getTypeService().wrapItem(((AbstractCMSComponentModel)item.getObject()).getCatalogVersion()));
        }
        else if(UISessionUtils.getCurrentSession().getTypeService().getBaseType("AbstractPage")
                        .isAssignableFrom((ObjectType)item.getType()))
        {
            result = getUIAccessRightService().isWritable((ObjectType)item.getType(),
                            getTypeService().wrapItem(((AbstractPageModel)item.getObject()).getCatalogVersion()));
        }
        return result;
    }


    protected void move(ListViewAction.Context context, int fromIndex, int toIndex)
    {
        TableModel model = context.getModel();
        if(model.getListComponentModel().isEditable() && model.getListComponentModel().getListModel() instanceof DefaultListModel)
        {
            BrowserSectionModel sectionModel = ((SectionTableModel)model).getModel();
            ((DefaultListModel)model.getListComponentModel().getListModel()).moveElement(fromIndex, toIndex);
            List<? extends TypedObject> listRows = ((MutableTableModel)model).getListComponentModel().getListModel().getElements();
            ContentSlotModel contentSlotModel = null;
            Object rootObject = sectionModel.getRootItem();
            if(rootObject instanceof TypedObject)
            {
                contentSlotModel = (ContentSlotModel)((TypedObject)sectionModel.getRootItem()).getObject();
            }
            else if(sectionModel.getRootItem() instanceof ContentSlotModel)
            {
                contentSlotModel = (ContentSlotModel)sectionModel.getRootItem();
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


    protected CMSPageLockingService getCmsPageLockingService()
    {
        if(this.cmsPageLockingService == null)
        {
            this.cmsPageLockingService = (CMSPageLockingService)SpringUtil.getBean("cmsPageLockingService");
        }
        return this.cmsPageLockingService;
    }


    protected CMSPageService getCmsPageService()
    {
        if(this.cmsPageService == null)
        {
            this.cmsPageService = (CMSPageService)SpringUtil.getBean("cmsPageService");
        }
        return this.cmsPageService;
    }


    protected UIAccessRightService getUIAccessRightService()
    {
        if(this.uiAccessRightService == null)
        {
            this.uiAccessRightService = (UIAccessRightService)SpringUtil.getBean("uiAccessRightService");
        }
        return this.uiAccessRightService;
    }


    protected TypeService getTypeService()
    {
        if(this.typeService == null)
        {
            return this.typeService = UISessionUtils.getCurrentSession().getTypeService();
        }
        return this.typeService;
    }


    protected SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            return this.systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }
}
