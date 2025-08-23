package de.hybris.platform.personalizationcms.cloning;

import de.hybris.platform.core.model.ItemModel;
import java.util.function.BiPredicate;

public class CxCmsComponentContainerDefaultComponentPredicate implements BiPredicate<ItemModel, String>
{
    public boolean test(ItemModel item, String attribute)
    {
        return (item instanceof de.hybris.platform.personalizationcms.model.CxCmsComponentContainerModel && "defaultCmsComponent".equals(attribute));
    }
}
