package de.hybris.platform.cms2.cloning.service.preset.impl;

import de.hybris.platform.cms2.cloning.service.CMSItemDeepCloningService;
import de.hybris.platform.cms2.cloning.service.preset.AttributePresetHandler;
import de.hybris.platform.core.model.ItemModel;

public class MediaForCodeAttributePresetHandler implements AttributePresetHandler<String>
{
    private CMSItemDeepCloningService cmsItemDeepCloningService;


    public MediaForCodeAttributePresetHandler(CMSItemDeepCloningService cmsItemDeepCloningService)
    {
        this.cmsItemDeepCloningService = cmsItemDeepCloningService;
    }


    public boolean test(ItemModel component, String qualifier)
    {
        return (component instanceof de.hybris.platform.core.model.media.MediaModel && "code".equals(qualifier));
    }


    public String get()
    {
        return getCmsItemDeepCloningService().generateCloneItemUid();
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
