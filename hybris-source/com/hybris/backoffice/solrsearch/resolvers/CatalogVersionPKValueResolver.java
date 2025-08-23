package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.model.ItemModel;
import java.util.Optional;

public class CatalogVersionPKValueResolver extends ItemModelPKValueResolver
{
    protected static final String CATALOG_VERSION_ATTRIBUTE = "catalogVersion";


    protected Optional<ItemModel> getTargetModel(ItemModel sourceModel)
    {
        Optional<ItemModel> catalogVersion = Optional.ofNullable((ItemModel)getModelService().getAttributeValue(sourceModel, "catalogVersion"));
        if(!catalogVersion.isPresent())
        {
            LOG.warn("{} couldn't resolve target CatalogVersionModel for {}", getClass().getSimpleName(), sourceModel);
        }
        return catalogVersion;
    }
}
