package de.hybris.platform.cmscockpit.services.security.impl;

import de.hybris.platform.cms2.constants.GeneratedCms2Constants;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.security.impl.DefaultUIAccessRightService;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.log4j.Logger;

public class CMSCockpitUIAccessRightService extends DefaultUIAccessRightService
{
    protected static final Logger LOG = Logger.getLogger(CMSCockpitUIAccessRightService.class.getName());
    private CMSPageLockingService cmsPageLockingService;
    private CMSPageService cmsPageService;


    public boolean isWritable(ObjectType type, TypedObject item)
    {
        boolean accessOk = super.isWritable(type, item);
        return (accessOk && isObjectWritable(item));
    }


    public boolean isWritable(ObjectType type, TypedObject item, PropertyDescriptor propDescr, boolean creationMode)
    {
        boolean accessOk = super.isWritable(type, item, propDescr, creationMode);
        return (accessOk && isObjectWritable(item));
    }


    public boolean isWritable(ObjectType type, PropertyDescriptor propDescr, boolean creationMode)
    {
        return isWritable(type, null, propDescr, creationMode);
    }


    protected boolean isObjectWritable(TypedObject item)
    {
        boolean locked = false;
        if(item != null)
        {
            List<AbstractPageModel> pagesToCheck = new ArrayList<>();
            if(getTypeService().getBaseType(GeneratedCms2Constants.TC.ABSTRACTPAGE).isAssignableFrom((ObjectType)item.getType()))
            {
                pagesToCheck.add((AbstractPageModel)item.getObject());
            }
            else if(getTypeService().getBaseType(GeneratedCms2Constants.TC.CONTENTSLOTFORPAGE).isAssignableFrom((ObjectType)item.getType()))
            {
                ContentSlotForPageModel contentSlotForPageModel = (ContentSlotForPageModel)item.getObject();
                pagesToCheck.add(contentSlotForPageModel.getPage());
            }
            else if(getTypeService().getBaseType(GeneratedCms2Constants.TC.CONTENTSLOT).isAssignableFrom((ObjectType)item.getType()))
            {
                pagesToCheck.addAll(getCmsPageService().getPagesForContentSlots(
                                Collections.singletonList((ContentSlotModel)item.getObject())));
            }
            else if(getTypeService().getBaseType(GeneratedCms2Constants.TC.ABSTRACTCMSCOMPONENT).isAssignableFrom((ObjectType)item.getType()))
            {
                pagesToCheck.addAll(getCmsPageService().getPagesForComponent((AbstractCMSComponentModel)item.getObject()));
            }
            for(AbstractPageModel page : pagesToCheck)
            {
                if(getCmsPageLockingService().isPageLockedFor(page, getUserService().getCurrentUser()))
                {
                    locked = true;
                    break;
                }
            }
        }
        return !locked;
    }


    public void setCmsPageLockingService(CMSPageLockingService cmsPageLockingService)
    {
        this.cmsPageLockingService = cmsPageLockingService;
    }


    public CMSPageLockingService getCmsPageLockingService()
    {
        return this.cmsPageLockingService;
    }


    public CMSPageService getCmsPageService()
    {
        return this.cmsPageService;
    }


    public void setCmsPageService(CMSPageService cmsPageService)
    {
        this.cmsPageService = cmsPageService;
    }
}
