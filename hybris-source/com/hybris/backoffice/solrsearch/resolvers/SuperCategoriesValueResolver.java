package com.hybris.backoffice.solrsearch.resolvers;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;

public class SuperCategoriesValueResolver extends CollectionValueResolver
{
    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ItemModel model, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext) throws FieldValueProviderException
    {
        if(model instanceof ProductModel)
        {
            Collection<CategoryModel> supercategories = ((ProductModel)model).getSupercategories();
            document.addField(String.format("%s_%s", new Object[] {indexedProperty.getName(), indexedProperty.getType()}), Boolean.valueOf(CollectionUtils.isEmpty(supercategories)));
        }
    }
}
