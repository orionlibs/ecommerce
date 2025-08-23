package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.jalo.order.price.PriceInformation;
import de.hybris.platform.product.PriceService;
import de.hybris.platform.solrfacetsearch.config.IndexConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.FieldNameProvider;
import de.hybris.platform.solrfacetsearch.provider.FieldValue;
import de.hybris.platform.solrfacetsearch.provider.FieldValueProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import org.apache.commons.collections4.CollectionUtils;

public class ProductPriceValueProvider extends AbstractPropertyFieldValueProvider implements FieldValueProvider
{
    private FieldNameProvider fieldNameProvider;
    private PriceService priceService;


    public Collection<FieldValue> getFieldValues(IndexConfig indexConfig, IndexedProperty indexedProperty, Object model) throws FieldValueProviderException
    {
        if(!(model instanceof ProductModel))
        {
            throw new FieldValueProviderException("Cannot evaluate price of non-product item");
        }
        Collection<FieldValue> fieldValues = new ArrayList<>();
        ProductModel product = (ProductModel)model;
        if(indexedProperty.isCurrency() && CollectionUtils.isNotEmpty(indexConfig.getCurrencies()))
        {
            CurrencyModel sessionCurrency = this.i18nService.getCurrentCurrency();
            try
            {
                for(CurrencyModel currency : indexConfig.getCurrencies())
                {
                    this.i18nService.setCurrentCurrency(currency);
                    addFieldValues(fieldValues, product, indexedProperty, currency.getIsocode());
                }
            }
            finally
            {
                this.i18nService.setCurrentCurrency(sessionCurrency);
            }
        }
        else
        {
            addFieldValues(fieldValues, product, indexedProperty, null);
        }
        return fieldValues;
    }


    protected void addFieldValues(Collection<FieldValue> fieldValues, ProductModel product, IndexedProperty indexedProperty, String currency) throws FieldValueProviderException
    {
        List<PriceInformation> prices = this.priceService.getPriceInformationsForProduct(product);
        if(CollectionUtils.isEmpty(prices))
        {
            return;
        }
        Double value = Double.valueOf(((PriceInformation)prices.get(0)).getPriceValue().getValue());
        List<String> rangeNameList = getRangeNameList(indexedProperty, value, currency);
        Collection<String> fieldNames = this.fieldNameProvider.getFieldNames(indexedProperty,
                        (currency == null) ? null : currency.toLowerCase(Locale.ROOT));
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


    public void setFieldNameProvider(FieldNameProvider fieldNameProvider)
    {
        this.fieldNameProvider = fieldNameProvider;
    }


    public void setPriceService(PriceService priceService)
    {
        this.priceService = priceService;
    }
}
