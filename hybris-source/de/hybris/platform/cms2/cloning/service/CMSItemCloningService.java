package de.hybris.platform.cms2.cloning.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import java.util.Map;
import java.util.Optional;

public interface CMSItemCloningService
{
    void cloneContentSlotComponents(ContentSlotModel paramContentSlotModel1, ContentSlotModel paramContentSlotModel2, CatalogVersionModel paramCatalogVersionModel);


    Optional<AbstractCMSComponentModel> cloneComponent(AbstractCMSComponentModel paramAbstractCMSComponentModel);


    @Deprecated(since = "2105", forRemoval = true)
    default boolean shouldCloneComponents(Map<String, Object> context)
    {
        return (context != null && context.get("shouldCloneComponents") != null &&
                        Boolean.parseBoolean(context.get("shouldCloneComponents").toString()));
    }
}
