package de.hybris.platform.cms2.cloning.service.predicate;

import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminComponentService;
import de.hybris.platform.core.model.ItemModel;
import java.util.function.BiPredicate;
import java.util.function.Supplier;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "2105", forRemoval = true)
public class ComponentForUidAttributePresetPredicate implements BiPredicate<ItemModel, String>, Supplier<String>
{
    private CMSAdminComponentService cmsAdminComponentService;


    public boolean test(ItemModel component, String qualifier)
    {
        return (component instanceof de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel && "uid".equals(qualifier));
    }


    public String get()
    {
        return getCmsAdminComponentService().generateCmsComponentUid();
    }


    protected CMSAdminComponentService getCmsAdminComponentService()
    {
        return this.cmsAdminComponentService;
    }


    @Required
    public void setCmsAdminComponentService(CMSAdminComponentService cmsAdminComponentService)
    {
        this.cmsAdminComponentService = cmsAdminComponentService;
    }
}
