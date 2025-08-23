package com.hybris.backoffice.solrsearch.resolvers;

import com.hybris.backoffice.solrsearch.providers.ProductCategoryAssignmentResolver;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.indexer.IndexerBatchContext;
import de.hybris.platform.solrfacetsearch.indexer.spi.InputDocument;
import de.hybris.platform.solrfacetsearch.provider.impl.AbstractValueResolver;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class VariantAwareCategoryCodeValueResolver extends AbstractValueResolver<ProductModel, Object, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(VariantAwareCategoryCodeValueResolver.class);
    private ProductCategoryAssignmentResolver categoryAttributeValueProvider;


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ProductModel product, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext)
    {
        Collection<CategoryModel> categories = getCategoryAttributeValueProvider().getIndexedCategories(product);
        categories.forEach(category -> {
            try
            {
                document.addField(indexedProperty, category.getCode());
            }
            catch(FieldValueProviderException exc)
            {
                LOG.warn("Could not resolve index property: " + indexedProperty, (Throwable)exc);
            }
        });
    }


    public ProductCategoryAssignmentResolver getCategoryAttributeValueProvider()
    {
        return this.categoryAttributeValueProvider;
    }


    @Required
    public void setCategoryAttributeValueProvider(ProductCategoryAssignmentResolver categoryAttributeValueProvider)
    {
        this.categoryAttributeValueProvider = categoryAttributeValueProvider;
    }
}
