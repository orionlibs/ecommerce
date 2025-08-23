package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.provider.Qualifier;
import de.hybris.platform.solrfacetsearch.provider.QualifierProvider;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class CurrencyQualifierProvider implements QualifierProvider
{
    private CommonI18NService commonI18NService;
    private final Set<Class<?>> supportedTypes = Collections.singleton(CurrencyModel.class);


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }


    public Set<Class<?>> getSupportedTypes()
    {
        return this.supportedTypes;
    }


    public Collection<Qualifier> getAvailableQualifiers(FacetSearchConfig facetSearchConfig, IndexedType indexedType)
    {
        Objects.requireNonNull(facetSearchConfig, "facetSearchConfig is null");
        Objects.requireNonNull(indexedType, "indexedType is null");
        List<Qualifier> qualifiers = new ArrayList<>();
        for(CurrencyModel currency : facetSearchConfig.getIndexConfig().getCurrencies())
        {
            CurrencyQualifier currencyQualifier = new CurrencyQualifier(currency);
            qualifiers.add(currencyQualifier);
        }
        return Collections.unmodifiableList(qualifiers);
    }


    public boolean canApply(IndexedProperty indexedProperty)
    {
        Objects.requireNonNull(indexedProperty, "indexedProperty is null");
        return indexedProperty.isCurrency();
    }


    public void applyQualifier(Qualifier qualifier)
    {
        Objects.requireNonNull(qualifier, "qualifier is null");
        if(!(qualifier instanceof CurrencyQualifier))
        {
            throw new IllegalArgumentException("provided qualifier is not of expected type");
        }
        this.commonI18NService.setCurrentCurrency(((CurrencyQualifier)qualifier).getCurrency());
    }


    public Qualifier getCurrentQualifier()
    {
        CurrencyModel currency = this.commonI18NService.getCurrentCurrency();
        if(currency == null)
        {
            return null;
        }
        return (Qualifier)new CurrencyQualifier(currency);
    }
}
