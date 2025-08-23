package de.hybris.platform.cms2.cloning.service.preset.impl;

import de.hybris.platform.cms2.cloning.service.CMSItemDeepCloningService;
import de.hybris.platform.cms2.cloning.service.preset.AttributePresetHandler;
import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.core.model.ItemModel;

public class ComponentForUidAttributePresetHandler implements AttributePresetHandler<String>
{
    private CMSAdminComponentService cmsAdminComponentService;
    private CMSItemDeepCloningService cmsItemDeepCloningService;


    public ComponentForUidAttributePresetHandler(CMSAdminComponentService cmsAdminComponentService, CMSItemDeepCloningService cmsItemDeepCloningService)
    {
        this.cmsAdminComponentService = cmsAdminComponentService;
        this.cmsItemDeepCloningService = cmsItemDeepCloningService;
    }


    public boolean test(ItemModel component, String qualifier)
    {
        return (component instanceof de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel && "uid".equals(qualifier));
    }


    public String get()
    {
        return getCmsAdminComponentService().generateCmsComponentUid();
    }


    public String get(Object originalValue)
    {
        return getCmsItemDeepCloningService().generateCloneItemUid(originalValue.toString());
    }


    protected CMSAdminComponentService getCmsAdminComponentService()
    {
        return this.cmsAdminComponentService;
    }


    public void setCmsAdminComponentService(CMSAdminComponentService cmsAdminComponentService)
    {
        this.cmsAdminComponentService = cmsAdminComponentService;
    }


    public CMSItemDeepCloningService getCmsItemDeepCloningService()
    {
        return this.cmsItemDeepCloningService;
    }


    public void setCmsItemDeepCloningService(CMSItemDeepCloningService cmsItemDeepCloningService)
    {
        this.cmsItemDeepCloningService = cmsItemDeepCloningService;
    }
}
