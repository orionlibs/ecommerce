package de.hybris.platform.cms2.servicelayer.daos;

import de.hybris.platform.cms2.model.CMSComponentTypeModel;
import de.hybris.platform.cms2.model.pages.PageTemplateModel;
import java.util.List;

public interface CMSTypeRestrictionDao
{
    List<CMSComponentTypeModel> getTypeRestrictionsForPageTemplate(PageTemplateModel paramPageTemplateModel);
}
