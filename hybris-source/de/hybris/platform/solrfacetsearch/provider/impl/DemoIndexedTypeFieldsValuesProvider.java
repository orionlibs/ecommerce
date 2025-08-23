package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.IndexedTypeFieldsValuesProvider;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DemoIndexedTypeFieldsValuesProvider implements IndexedTypeFieldsValuesProvider
{
    protected static final String NAME_PROPERTY = "name";
    protected static final String CODE_PROPERTY = "code";
    protected static final String CATEGORY_PROPERTY = "category";
    protected static final String MANUFACTURER_NAME_PROPERTY = "manufacturerName";
    protected static final String SUPER_CATEGORIES_ATTR_NAME = "supercategories";
    private ModelService modelService;


    public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, Object model) throws FieldValueProviderException
    {
        Collection<FieldValue> result = new ArrayList<>();
        result.addAll(getPropertyFieldsValues(model, "name"));
        result.addAll(getPropertyFieldsValues(model, "code"));
        result.addAll(getPropertyFieldsValues(model, "manufacturerName"));
        result.addAll(getCategoryFieldsValues(model));
        return result;
    }


    protected Collection<FieldValue> getCategoryFieldsValues(Object model)
    {
        Collection<CategoryModel> categories = null;
        if(model instanceof VariantProductModel)
        {
            ProductModel baseProduct = ((VariantProductModel)model).getBaseProduct();
            categories = (Collection<CategoryModel>)this.modelService.getAttributeValue(baseProduct, "supercategories");
        }
        else
        {
            categories = (Collection<CategoryModel>)this.modelService.getAttributeValue(model, "supercategories");
        }
        if(categories != null && !categories.isEmpty())
        {
            Collection<FieldValue> fieldValues = new ArrayList<>();
            for(CategoryModel category : categories)
            {
                fieldValues.add(new FieldValue(getFieldNamesMapping().get("category"), getCategoryValue(category)));
                for(CategoryModel superCategory : category.getAllSupercategories())
                {
                    fieldValues.add(new FieldValue(getFieldNamesMapping().get("category"), getCategoryValue(superCategory)));
                }
            }
            return fieldValues;
        }
        return Collections.emptyList();
    }


    protected Collection<FieldValue> getPropertyFieldsValues(Object model, String propertyName)
    {
        Collection<FieldValue> fieldValues = new ArrayList<>();
        Object value = getPropertyValue(model, propertyName);
        fieldValues.add(new FieldValue(getFieldNamesMapping().get(propertyName), value));
        return fieldValues;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public Set<String> getFacets()
    {
        Set<String> facets = new HashSet<>();
        facets.add("category");
        facets.add("manufacturerName");
        return facets;
    }


    public Map<String, String> getFieldNamesMapping()
    {
        Map<String, String> mapping = new HashMap<>();
        mapping.put("category", "category_string_mv");
        mapping.put("code", "code_string");
        mapping.put("name", "name_string");
        mapping.put("manufacturerName", "manufacturerName_string");
        return mapping;
    }


    protected Object getCategoryValue(CategoryModel category)
    {
        return getPropertyValue(category, "name");
    }


    protected Object getPropertyValue(Object model, String propertyName)
    {
        return this.modelService.getAttributeValue(model, propertyName);
    }
}
