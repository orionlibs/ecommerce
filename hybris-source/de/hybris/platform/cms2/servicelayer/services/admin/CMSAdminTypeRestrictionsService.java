package de.hybris.platform.cms2.servicelayer.services.admin;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import java.util.Set;

public interface CMSAdminTypeRestrictionsService
{
    Set<CMSComponentTypeModel> getTypeRestrictionsContentSlotForPage(AbstractPageModel paramAbstractPageModel, ContentSlotModel paramContentSlotModel) throws CMSItemNotFoundException;


    Set<CMSComponentTypeModel> getTypeRestrictionsContentSlotForTemplate(PageTemplateModel paramPageTemplateModel, ContentSlotModel paramContentSlotModel) throws CMSItemNotFoundException;


    Set<CMSComponentTypeModel> getTypeRestrictionsForContentSlot(AbstractPageModel paramAbstractPageModel, ContentSlotModel paramContentSlotModel) throws CMSItemNotFoundException;


    default Set<CMSComponentTypeModel> getTypeRestrictionsForPage(AbstractPageModel page)
    {
        throw new UnsupportedOperationException("CMSAdminTypeRestrictionsService.getTypeRestrictionsForPage is not implemented.");
    }
}
