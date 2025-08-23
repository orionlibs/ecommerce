package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.catalog.jalo.classification.ClassificationAttributeValue;
import de.hybris.platform.catalog.jalo.classification.util.Feature;
import de.hybris.platform.catalog.jalo.classification.util.FeatureContainer;
import de.hybris.platform.catalog.jalo.classification.util.FeatureValue;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class ClassificationPropertyValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
    private FieldNameProvider fieldNameProvider;


    public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model) throws FieldValueProviderException
    {
        if(model instanceof de.hybris.platform.core.model.product.ProductModel)
        {
            FeatureContainer cont = FeatureContainer.load((Product)this.modelService.getSource(model));
            if(cont.hasFeature(indexedProperty.getName()))
            {
                Feature feature = cont.getFeature(indexedProperty.getName());
                if(feature == null || feature.isEmpty())
                {
                    return Collections.emptyList();
                }
                return getFeaturesValues(indexConfig, feature, indexedProperty);
            }
            return Collections.emptyList();
        }
        throw new FieldValueProviderException("Cannot provide classification property of non-product item");
    }


    protected List<FieldValue> getFeaturesValues(IndexConfig indexConfig, Feature feature, IndexedProperty indexedProperty) throws FieldValueProviderException
    {
        List<FieldValue> result = new ArrayList<>();
        List<FeatureValue> featureValues = null;
        if(!feature.isLocalized())
        {
            featureValues = feature.getValues();
        }
        if(indexedProperty.isLocalized())
        {
            for(LanguageModel language : indexConfig.getLanguages())
            {
                Locale locale = this.i18nService.getCurrentLocale();
                try
                {
                    this.i18nService.setCurrentLocale(this.localeService.getLocaleByString(language.getIsocode()));
                    result.addAll(
                                    extractFieldValues(indexedProperty, language, feature.isLocalized() ? feature.getValues() : featureValues));
                }
                finally
                {
                    this.i18nService.setCurrentLocale(locale);
                }
            }
        }
        else
        {
            result.addAll(extractFieldValues(indexedProperty, null, feature.getValues()));
        }
        return result;
    }


    protected List<FieldValue> extractFieldValues(IndexedProperty indexedProperty, LanguageModel language, List<FeatureValue> list) throws FieldValueProviderException
    {
        List<FieldValue> result = new ArrayList<>();
        for(FeatureValue featureValue : list)
        {
            addFeatureValue(result, indexedProperty, language, featureValue);
        }
        return result;
    }


    protected void addFeatureValue(List<FieldValue> result, IndexedProperty indexedProperty, LanguageModel language, FeatureValue featureValue) throws FieldValueProviderException
    {
        Object value = featureValue.getValue();
        if(value instanceof ClassificationAttributeValue)
        {
            value = ((ClassificationAttributeValue)value).getName();
        }
        List<String> rangeNameList = getRangeNameList(indexedProperty, value);
        Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty,
                        (language == null) ? null : language.getIsocode());
        for(String fieldName : fieldNames)
        {
            if(rangeNameList.isEmpty())
            {
                result.add(new FieldValue(fieldName, value));
                continue;
            }
            for(String rangeName : rangeNameList)
            {
                result.add(new FieldValue(fieldName, (rangeName == null) ? value : rangeName));
            }
        }
    }


    public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
    {
        this.fieldNameProvider = fieldNameProvider;
    }


    public FieldNameProvider getFieldNameProvider()
    {
        return this.fieldNameProvider;
    }
}
