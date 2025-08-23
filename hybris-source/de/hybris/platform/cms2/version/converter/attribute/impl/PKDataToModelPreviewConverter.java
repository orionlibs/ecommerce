package de.hybris.platform.cms2.version.converter.attribute.impl;

import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.cms2.version.service.CMSVersionService;
import de.hybris.platform.core.model.ItemModel;
import org.springframework.beans.factory.annotation.Required;

public class PKDataToModelPreviewConverter extends AbstractPKDataToModelConverter
{
    private CMSVersionService cmsVersionService;


    public ItemModel getItemModelByVersion(CMSVersionModel cmsVersion)
    {
        return getCmsVersionService().createItemFromVersion(cmsVersion);
    }


    protected CMSVersionService getCmsVersionService()
    {
        return this.cmsVersionService;
    }


    @Required
    public void setCmsVersionService(CMSVersionService cmsVersionService)
    {
        this.cmsVersionService = cmsVersionService;
    }
}
