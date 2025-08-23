package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Required;

public class CategoryNameValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
    private String categoriesQualifier;
    private FieldNameProvider fieldNameProvider;


    public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model) throws FieldValueProviderException
    {
        Collection<CategoryModel> categories = null;
        if(model instanceof VariantProductModel)
        {
            ProductModel baseProduct = ((VariantProductModel)model).getBaseProduct();
            categories = (Collection<CategoryModel>)this.modelService.getAttributeValue(baseProduct, this.categoriesQualifier);
        }
        else
        {
            categories = (Collection<CategoryModel>)this.modelService.getAttributeValue(model, this.categoriesQualifier);
        }
        if(categories != null && !categories.isEmpty())
        {
            return doGetFieldValues(indexConfig, indexedProperty, categories);
        }
        return Collections.emptyList();
    }


    public Collection<FieldValue> doGetFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Collection<CategoryModel> categories)
    {
        Collection<FieldValue> fieldValues = new ArrayList<>();
        for(CategoryModel category : categories)
        {
            if(indexedProperty.isLocalized())
            {
                Collection<LanguageModel> languages = indexConfig.getLanguages();
                for(LanguageModel language : languages)
                {
                    addValuesForCategory(fieldValues, indexConfig, indexedProperty, category, language);
                }
                continue;
            }
            addValuesForCategory(fieldValues, indexConfig, indexedProperty, category, null);
        }
        return fieldValues;
    }


    public void addValuesForCategory(Collection<FieldValue> fieldValues, IndexConfig indexConfig, IndexedProperty indexedProperty, CategoryModel category, LanguageModel language)
    {
        fieldValues.addAll(createFieldValue(category, language, indexedProperty));
        for(CategoryModel superCategory : category.getAllSupercategories())
        {
            fieldValues.addAll(createFieldValue(superCategory, language, indexedProperty));
        }
    }


    protected List<FieldValue> createFieldValue(CategoryModel category, LanguageModel language, IndexedProperty indexedProperty)
    {
        List<FieldValue> fieldValues = new ArrayList<>();
        Object value = null;
        if(language != null)
        {
            Locale locale = this.i18nService.getCurrentLocale();
            try
            {
                this.i18nService.setCurrentLocale(this.localeService.getLocaleByString(language.getIsocode()));
                value = getPropertyValue(category, "name");
            }
            finally
            {
                this.i18nService.setCurrentLocale(locale);
            }
            Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, language.getIsocode());
            for(String fieldName : fieldNames)
            {
                fieldValues.add(new FieldValue(fieldName, value));
            }
        }
        else
        {
            value = getPropertyValue(category, "name");
            Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty, null);
            for(String fieldName : fieldNames)
            {
                fieldValues.add(new FieldValue(fieldName, value));
            }
        }
        return fieldValues;
    }


    protected Object getPropertyValue(Object model, String propertyName)
    {
        return this.modelService.getAttributeValue(model, propertyName);
    }


    @Required
    public void setCategoriesQualifier(String categoriesQualifier)
    {
        this.categoriesQualifier = categoriesQualifier;
    }


    @Required
    public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
    {
        this.fieldNameProvider = fieldNameProvider;
    }
}
