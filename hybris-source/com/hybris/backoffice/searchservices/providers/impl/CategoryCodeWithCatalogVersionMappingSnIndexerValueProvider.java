package com.hybris.backoffice.searchservices.providers.impl;

import com.hybris.backoffice.search.utils.CategoryCatalogVersionMapper;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import java.util.Set;
import java.util.stream.Collectors;

public class CategoryCodeWithCatalogVersionMappingSnIndexerValueProvider extends VariantAwareCategorySnIndexerValueProvider
{
    private CategoryCatalogVersionMapper categoryCatalogVersionMapper;


    protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, ProductModel source, ProductAttributeSnIndexerValueProvider.ProductData data) throws SnIndexerException
    {
        Set<CategoryModel> fieldValue = collectCategories(fieldWrapper, data);
        return fieldValue.stream().map(item -> this.categoryCatalogVersionMapper.encode(item)).collect(Collectors.toList());
    }


    public CategoryCatalogVersionMapper getCategoryCatalogVersionMapper()
    {
        return this.categoryCatalogVersionMapper;
    }


    public void setCategoryCatalogVersionMapper(CategoryCatalogVersionMapper categoryCatalogVersionMapper)
    {
        this.categoryCatalogVersionMapper = categoryCatalogVersionMapper;
    }
}
