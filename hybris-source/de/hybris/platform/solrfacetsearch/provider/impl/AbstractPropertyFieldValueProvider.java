package de.hybris.platform.solrfacetsearch.provider.impl;

import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.exceptions.FieldValueProviderException;
import de.hybris.platform.solrfacetsearch.provider.RangeNameProvider;
import java.util.List;

public abstract class AbstractPropertyFieldValueProvider
{
    protected I18NService i18nService;
    protected ModelService modelService;
    protected LocalizationService localeService;
    protected RangeNameProvider rangeNameProvider;


    @Deprecated(since = "5.2")
    public List<String> getRangeNameList(IndexedProperty property, Object value) throws FieldValueProviderException
    {
        return this.rangeNameProvider.getRangeNameList(property, value);
    }


    @Deprecated(since = "5.2")
    public List<String> getRangeNameList(IndexedProperty property, Object value, String qualifier) throws FieldValueProviderException
    {
        return this.rangeNameProvider.getRangeNameList(property, value, qualifier);
    }


    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setLocaleService(LocalizationService localeService)
    {
        this.localeService = localeService;
    }


    public void setRangeNameProvider(RangeNameProvider rangeNameProvider)
    {
        this.rangeNameProvider = rangeNameProvider;
    }
}
