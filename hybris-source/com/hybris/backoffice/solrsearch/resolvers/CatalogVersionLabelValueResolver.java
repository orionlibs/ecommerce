package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import java.util.Locale;

public class CatalogVersionLabelValueResolver extends ItemModelLabelValueResolver
{
    protected static final String CATALOG_VERSION_ATTRIBUTE_NAME = "catalogVersion";


    protected String resolveIndexKey(IndexedProperty indexedProperty, LanguageModel language)
    {
        if(!indexedProperty.isFacet())
        {
            return String.format("%s_%s_%s", new Object[] {indexedProperty.getName(), indexedProperty.getType(), language.getIsocode().toLowerCase(Locale.ENGLISH)});
        }
        return super.resolveIndexKey(indexedProperty, language);
    }


    protected ItemModel provideModel(ItemModel model)
    {
        ItemModel value = (ItemModel)getModelService().getAttributeValue(model, "catalogVersion");
        if(value == null)
        {
            LOG.warn("{} couldn't resolve target CatalogVersionModel for {}", getClass().getSimpleName(), model);
        }
        return value;
    }
}
