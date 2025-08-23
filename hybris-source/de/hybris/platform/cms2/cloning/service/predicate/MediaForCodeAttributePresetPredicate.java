package de.hybris.platform.cms2.cloning.service.predicate;

import de.hybris.platform.cms2.cloning.service.CMSItemDeepCloningService;
import de.hybris.platform.core.model.ItemModel;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class MediaForCodeAttributePresetPredicate implements BiPredicate<ItemModel, String>, Supplier<String>
{
    private CMSItemDeepCloningService cmsItemDeepCloningService;


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


    @Required
    public void setCmsItemDeepCloningService(CMSItemDeepCloningService cmsItemDeepCloningService)
    {
        this.cmsItemDeepCloningService = cmsItemDeepCloningService;
    }
}
