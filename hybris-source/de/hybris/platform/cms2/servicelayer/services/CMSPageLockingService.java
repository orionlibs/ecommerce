package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.core.model.user.UserModel;
import java.util.Collection;

public interface CMSPageLockingService
{
    Collection<UserModel> getComponentLockers(AbstractCMSComponentModel paramAbstractCMSComponentModel);


    Collection<UserModel> getSlotLockers(ContentSlotModel paramContentSlotModel);


    boolean isComponentLockedForUser(AbstractCMSComponentModel paramAbstractCMSComponentModel, UserModel paramUserModel);


    boolean isContentSlotLockedForUser(ContentSlotModel paramContentSlotModel, UserModel paramUserModel);


    boolean isPageLockedBy(AbstractPageModel paramAbstractPageModel, UserModel paramUserModel);


    boolean isPageLockedFor(AbstractPageModel paramAbstractPageModel, UserModel paramUserModel);


    void setPageLocked(AbstractPageModel paramAbstractPageModel, UserModel paramUserModel, boolean paramBoolean);
}
