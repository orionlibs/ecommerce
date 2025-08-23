package de.hybris.platform.cms2.cloning.service.predicate;

import de.hybris.platform.core.model.ItemModel;
import java.util.function.BiPredicate;

public class MediaContainerForMediaAttributePartOfPredicate implements BiPredicate<ItemModel, String>
{
    public boolean test(ItemModel component, String qualifier)
    {
        return (component instanceof de.hybris.platform.core.model.media.MediaContainerModel && "medias".equals(qualifier));
    }
}
