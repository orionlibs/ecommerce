package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageLockingService;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.core.model.user.UserModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.apache.log4j.Logger;

public class DefaultCMSPageLockingService extends AbstractCMSService implements CMSPageLockingService
{
    protected static final Logger LOG = Logger.getLogger(DefaultCMSPageLockingService.class.getName());
    private CMSPageService cmsPageService;


    public CMSPageService getCmsPageService()
    {
        return this.cmsPageService;
    }


    public Collection<UserModel> getComponentLockers(AbstractCMSComponentModel componentModel)
    {
        Collection<UserModel> userModels = new HashSet<>();
        List<AbstractPageModel> pagesToCheck = new ArrayList<>();
        pagesToCheck.addAll(getCmsPageService().getPagesForComponent(componentModel));
        pagesToCheck.addAll(getCmsPageService().getPagesForPageTemplateComponent(componentModel));
        for(AbstractPageModel page : pagesToCheck)
        {
            if(page.getLockedBy() != null)
            {
                userModels.add(page.getLockedBy());
            }
        }
        return userModels;
    }


    public Collection<UserModel> getSlotLockers(ContentSlotModel contentSlotModel)
    {
        Collection<UserModel> userModels = new HashSet<>();
        List<AbstractPageModel> pagesToCheck = new ArrayList<>();
        pagesToCheck.addAll(getCmsPageService().getPagesForContentSlots(Collections.singletonList(contentSlotModel)));
        pagesToCheck.addAll(getCmsPageService().getPagesForPageTemplateContentSlots(Collections.singletonList(contentSlotModel)));
        for(AbstractPageModel page : pagesToCheck)
        {
            if(page.getLockedBy() != null)
            {
                userModels.add(page.getLockedBy());
            }
        }
        return userModels;
    }


    public boolean isComponentLockedForUser(AbstractCMSComponentModel componentModel, UserModel userModel)
    {
        Collection<UserModel> lockers = getComponentLockers(componentModel);
        return ((lockers.size() == 1 && !lockers.contains(userModel)) || lockers.size() > 1);
    }


    public boolean isContentSlotLockedForUser(ContentSlotModel contentSlotModel, UserModel userModel)
    {
        Collection<UserModel> lockers = getSlotLockers(contentSlotModel);
        return ((lockers.size() == 1 && !lockers.contains(userModel)) || lockers.size() > 1);
    }


    public boolean isPageLockedBy(AbstractPageModel pageModel, UserModel userModel)
    {
        if(pageModel == null || userModel == null)
        {
            LOG.warn("Cannot check page lock for arguments: pageModel " + pageModel + ", userModel " + userModel);
            return false;
        }
        return (pageModel.getLockedBy() != null && userModel.equals(pageModel.getLockedBy()));
    }


    public boolean isPageLockedFor(AbstractPageModel pageModel, UserModel userModel)
    {
        if(pageModel == null || userModel == null)
        {
            LOG.warn("Cannot check page lock for arguments: pageModel " + pageModel + ", userModel " + userModel);
            return false;
        }
        return (pageModel.getLockedBy() != null && !userModel.equals(pageModel.getLockedBy()));
    }


    public void setCmsPageService(CMSPageService cmsPageService)
    {
        this.cmsPageService = cmsPageService;
    }


    public void setPageLocked(AbstractPageModel pageModel, UserModel userModel, boolean lock)
    {
        if(pageModel == null || userModel == null)
        {
            LOG.warn("Cannot set/unset page lock for arguments: pageModel " + pageModel + ", userModel " + userModel);
        }
        if(pageModel != null)
        {
            if(lock)
            {
                lockPage(pageModel, userModel);
            }
            else
            {
                unlockPage(pageModel, userModel);
            }
        }
    }


    protected void lockPage(AbstractPageModel pageModel, UserModel userModel)
    {
        UserModel locker = pageModel.getLockedBy();
        if(locker == null)
        {
            pageModel.setLockedBy(userModel);
            getModelService().save(pageModel);
        }
        else
        {
            LOG.warn("Page " + pageModel + " is already locked for user " + userModel);
        }
    }


    protected void unlockPage(AbstractPageModel pageModel, UserModel userModel)
    {
        UserModel locker = pageModel.getLockedBy();
        if(locker == null)
        {
            LOG.warn("Try to unlock page " + pageModel + " which is not locked by any user.");
        }
        else if(userModel != null)
        {
            if(userModel.equals(locker))
            {
                pageModel.setLockedBy(null);
                getModelService().save(pageModel);
            }
            else if(userModel.isAuthorizedToUnlockPages())
            {
                pageModel.setLockedBy(null);
                getModelService().save(pageModel);
                LOG.debug("Page " + pageModel + " unlock forced by user " + userModel);
            }
            else
            {
                LOG.warn("No permission to unlock page " + pageModel + " for user " + userModel);
            }
        }
    }
}
