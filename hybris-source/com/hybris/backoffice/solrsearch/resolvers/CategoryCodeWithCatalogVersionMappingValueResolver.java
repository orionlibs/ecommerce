package com.hybris.backoffice.solrsearch.resolvers;

import com.hybris.backoffice.search.utils.CategoryCatalogVersionMapper;
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

public class CategoryCodeWithCatalogVersionMappingValueResolver extends VariantAwareCategoryCodeValueResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(CategoryCodeWithCatalogVersionMappingValueResolver.class);
    private CategoryCatalogVersionMapper categoryCatalogVersionMapper;


    protected void addFieldValues(InputDocument document, IndexerBatchContext batchContext, IndexedProperty indexedProperty, ProductModel product, AbstractValueResolver.ValueResolverContext<Object, Object> resolverContext)
    {
        Collection<CategoryModel> categories = getCategoryAttributeValueProvider().getIndexedCategories(product);
        categories.forEach(category -> addCategoryToSolrDocument(document, indexedProperty, category));
    }


    protected void addCategoryToSolrDocument(InputDocument document, IndexedProperty indexedProperty, CategoryModel category)
    {
        try
        {
            document.addField(indexedProperty, this.categoryCatalogVersionMapper.encode(category));
        }
        catch(FieldValueProviderException exc)
        {
            LOG.warn("Could not resolve index property: " + indexedProperty, (Throwable)exc);
        }
    }


    public CategoryCatalogVersionMapper getCategoryCatalogVersionMapper()
    {
        return this.categoryCatalogVersionMapper;
    }


    @Required
    public void setCategoryCatalogVersionMapper(CategoryCatalogVersionMapper categoryCatalogVersionMapper)
    {
        this.categoryCatalogVersionMapper = categoryCatalogVersionMapper;
    }
}
