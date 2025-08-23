package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.product.VariantsService;
import de.hybris.platform.servicelayer.exceptions.AttributeNotSupportedException;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import de.hybris.platform.variants.model.VariantAttributeDescriptorModel;
import de.hybris.platform.variants.model.VariantProductModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class ModelPropertyFieldValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(ModelPropertyFieldValueProvider.class);
    private FieldNameProvider fieldNameProvider;
    private VariantsService variantsService;


    public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model) throws FieldValueProviderException
    {
        if(model == null)
        {
            throw new IllegalArgumentException("No model given");
        }
        Collection<FieldValue> fieldValues = new ArrayList<>();
        if(indexedProperty.isLocalized() && CollectionUtils.isNotEmpty(indexConfig.getLanguages()))
        {
            Locale sessionLocale = this.i18nService.getCurrentLocale();
            try
            {
                for(LanguageModel language : indexConfig.getLanguages())
                {
                    this.i18nService.setCurrentLocale(this.localeService.getLocaleByString(language.getIsocode()));
                    addFieldValues(fieldValues, (ItemModel)model, indexedProperty, language.getIsocode());
                }
            }
            finally
            {
                this.i18nService.setCurrentLocale(sessionLocale);
            }
        }
        else
        {
            addFieldValues(fieldValues, (ItemModel)model, indexedProperty, null);
        }
        return fieldValues;
    }


    protected void addFieldValues(Collection<FieldValue> fieldValues, ItemModel model, IndexedProperty indexedProperty, String language) throws FieldValueProviderException
    {
        Object value = getPropertyValue(model, indexedProperty);
        List<String> rangeNameList = getRangeNameList(indexedProperty, value, language);
        Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty,
                        (language == null) ? null : language.toLowerCase(Locale.ROOT));
        for(String fieldName : fieldNames)
        {
            if(rangeNameList.isEmpty())
            {
                fieldValues.add(new FieldValue(fieldName, value));
                continue;
            }
            for(String rangeName : rangeNameList)
            {
                fieldValues.add(new FieldValue(fieldName, (rangeName == null) ? value : rangeName));
            }
        }
    }


    protected Object getPropertyValue(Object model, IndexedProperty indexedProperty)
    {
        String qualifier = indexedProperty.getValueProviderParameter();
        if(qualifier == null || qualifier.trim().isEmpty())
        {
            qualifier = indexedProperty.getName();
        }
        Object result = null;
        try
        {
            result = this.modelService.getAttributeValue(model, qualifier);
            if(result == null && model instanceof VariantProductModel)
            {
                ProductModel baseProduct = ((VariantProductModel)model).getBaseProduct();
                result = this.modelService.getAttributeValue(baseProduct, qualifier);
            }
        }
        catch(AttributeNotSupportedException ae)
        {
            if(model instanceof VariantProductModel)
            {
                ProductModel baseProduct = ((VariantProductModel)model).getBaseProduct();
                for(VariantAttributeDescriptorModel att : baseProduct.getVariantType().getVariantAttributes())
                {
                    if(qualifier.equals(att.getQualifier()))
                    {
                        result = this.variantsService.getVariantAttributeValue((VariantProductModel)model, qualifier);
                        break;
                    }
                }
            }
            else
            {
                LOG.error(ae.getMessage());
            }
        }
        return result;
    }


    @Required
    public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
    {
        this.fieldNameProvider = fieldNameProvider;
    }


    @Required
    public void setVariantsService(VariantsService variantsService)
    {
        this.variantsService = variantsService;
    }
}
