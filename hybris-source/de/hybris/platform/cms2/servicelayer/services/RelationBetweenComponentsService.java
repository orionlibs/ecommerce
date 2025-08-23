package de.hybris.platform.cms2.servicelayer.services;

import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;

public interface RelationBetweenComponentsService
{
    void maintainRelationBetweenComponentsOnComponent(AbstractCMSComponentModel paramAbstractCMSComponentModel);


    void maintainRelationBetweenComponentsOnPage(AbstractPageModel paramAbstractPageModel);


    void maintainRelationBetweenComponentsOnSlot(ContentSlotModel paramContentSlotModel);


    void removeRelationBetweenComponentsOnModel(AbstractCMSComponentModel paramAbstractCMSComponentModel);
}
