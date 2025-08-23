package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import java.util.Optional;

public class CatalogPKValueResolver extends ItemModelPKValueResolver
{
    protected static final String CATALOG_VERSION_ATTRIBUTE = "catalogVersion";


    protected Optional<ItemModel> getTargetModel(ItemModel sourceModel)
    {
        CatalogVersionModel catalogVersionModel = (CatalogVersionModel)getModelService().getAttributeValue(sourceModel, "catalogVersion");
        if(catalogVersionModel != null)
        {
            return (Optional)Optional.ofNullable(catalogVersionModel.getCatalog());
        }
        LOG.warn("{} couldn't resolve target CatalogVersionModel for {}", getClass().getSimpleName(), sourceModel);
        return Optional.empty();
    }
}
