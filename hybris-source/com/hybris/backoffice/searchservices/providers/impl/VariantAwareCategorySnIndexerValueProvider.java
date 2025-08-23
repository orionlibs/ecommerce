package com.hybris.backoffice.searchservices.providers.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.searchservices.core.SnException;
import de.hybris.platform.searchservices.indexer.SnIndexerException;
import de.hybris.platform.searchservices.indexer.service.SnIndexerContext;
import de.hybris.platform.searchservices.indexer.service.SnIndexerFieldWrapper;
import de.hybris.platform.searchservices.util.ParameterUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;

public class VariantAwareCategorySnIndexerValueProvider extends ProductAttributeSnIndexerValueProvider
{
    public static final String CATEGORY_SELECTOR_PARAM = "categorySelector";
    public static final String CATEGORY_SELECTOR_VALUE_CATEGORIES = "categories";
    public static final String CATEGORY_SELECTOR_VALUE_ALLCATEGORIES = "allCategories";
    public static final String CATEGORY_SELECTOR_PARAM_DEFAULT_VALUE = "categories";
    protected static final Set<Class<?>> SUPPORTED_QUALIFIER_CLASSES = Set.of(Locale.class);


    public Set<Class<?>> getSupportedQualifierClasses() throws SnIndexerException
    {
        return SUPPORTED_QUALIFIER_CLASSES;
    }


    protected Object getFieldValue(SnIndexerContext indexerContext, SnIndexerFieldWrapper fieldWrapper, ProductModel source, ProductAttributeSnIndexerValueProvider.ProductData data) throws SnIndexerException
    {
        try
        {
            String expression = resolveExpression(fieldWrapper);
            Set<CategoryModel> categories = collectCategories(fieldWrapper, data);
            if(categories == null || CollectionUtils.isEmpty(categories))
            {
                return null;
            }
            if(fieldWrapper.isLocalized())
            {
                List<Locale> locales = (List<Locale>)fieldWrapper.getQualifiers().stream().map(qualifier -> (Locale)qualifier.getAs(Locale.class)).collect(Collectors.toList());
                return this.snExpressionEvaluator.evaluate(categories, expression, locales);
            }
            return this.snExpressionEvaluator.evaluate(categories, expression);
        }
        catch(SnException e)
        {
            throw new SnIndexerException(e);
        }
    }


    protected Set<CategoryModel> collectCategories(SnIndexerFieldWrapper fieldWrapper, ProductAttributeSnIndexerValueProvider.ProductData data) throws SnIndexerException
    {
        String productSelector = resolveProductSelector(fieldWrapper);
        Set<ProductModel> products = (Set<ProductModel>)data.getProducts().get(productSelector);
        String categorySelector = resolveCategorySelector(fieldWrapper);
        switch(categorySelector)
        {
            case "categories":
                return collectDirectCategories(products);
            case "allCategories":
                return collectAllCategories(products);
        }
        throw new SnIndexerException("Invalid category selector: " + categorySelector);
    }


    protected Set<CategoryModel> collectDirectCategories(Set<ProductModel> products)
    {
        Set<CategoryModel> categories = new HashSet<>();
        for(ProductModel product : products)
        {
            categories.addAll(product.getSupercategories());
        }
        return categories;
    }


    protected Set<CategoryModel> collectAllCategories(Set<ProductModel> products)
    {
        Set<CategoryModel> categories = collectDirectCategories(products);
        Set<CategoryModel> result = new HashSet<>();
        for(CategoryModel category : categories)
        {
            result.addAll(category.getAllSupercategories());
        }
        categories.addAll(result);
        return categories;
    }


    protected String resolveCategorySelector(SnIndexerFieldWrapper fieldWrapper)
    {
        return ParameterUtils.getString(fieldWrapper.getValueProviderParameters(), "categorySelector", "categories");
    }
}
