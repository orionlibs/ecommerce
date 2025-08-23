package de.hybris.platform.cms2.cloning.service.preset.impl;

import de.hybris.platform.cms2.cloning.service.CMSItemDeepCloningService;
import de.hybris.platform.cms2.cloning.service.preset.AttributePresetHandler;
import de.hybris.platform.core.model.ItemModel;

public class MediaContainerForQualifierAttributePresetHandler implements AttributePresetHandler<String>
{
    private CMSItemDeepCloningService cmsItemDeepCloningService;


    public MediaContainerForQualifierAttributePresetHandler(CMSItemDeepCloningService cmsItemDeepCloningService)
    {
        this.cmsItemDeepCloningService = cmsItemDeepCloningService;
    }


    public boolean test(ItemModel component, String qualifier)
    {
        return (component instanceof de.hybris.platform.core.model.media.MediaContainerModel && "qualifier".equals(qualifier));
    }


    public String get()
    {
        return getCmsItemDeepCloningService().generateCloneItemUid();
    }


    public String get(Object originalValue)
    {
        return getCmsItemDeepCloningService().generateCloneItemUid(originalValue.toString());
    }


    protected CMSItemDeepCloningService getCmsItemDeepCloningService()
    {
        return this.cmsItemDeepCloningService;
    }


    public void setCmsItemDeepCloningService(CMSItemDeepCloningService cmsItemDeepCloningService)
    {
        this.cmsItemDeepCloningService = cmsItemDeepCloningService;
    }
}
