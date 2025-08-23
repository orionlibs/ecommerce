package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AsExcludedFacetValue;
import de.hybris.platform.adaptivesearch.data.AsFacetRange;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacetValue;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetValueModel;
import de.hybris.platform.adaptivesearch.model.AsFacetRangeModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetValueModel;
import de.hybris.platform.adaptivesearch.util.ContextAwareConverter;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Required;

public class AsFacetConfigurationReversePopulator implements ContextAwarePopulator<AbstractAsFacetConfiguration, AbstractAsFacetConfigurationModel, AsItemConfigurationReverseConverterContext>
{
    private ContextAwareConverter<AsPromotedFacetValue, AsPromotedFacetValueModel, AsItemConfigurationReverseConverterContext> asPromotedFacetValueReverseConverter;
    private ContextAwareConverter<AsExcludedFacetValue, AsExcludedFacetValueModel, AsItemConfigurationReverseConverterContext> asExcludedFacetValueReverseConverter;
    private ContextAwareConverter<AsFacetRange, AsFacetRangeModel, AsItemConfigurationReverseConverterContext> asFacetRangeReverseConverter;
    private I18NService i18NService;


    public void populate(AbstractAsFacetConfiguration source, AbstractAsFacetConfigurationModel target, AsItemConfigurationReverseConverterContext context)
    {
        AsItemConfigurationReverseConverterContext newContext = new AsItemConfigurationReverseConverterContext();
        newContext.setCatalogVersion(context.getCatalogVersion());
        newContext.setParentConfiguration((AbstractAsConfigurationModel)target);
        target.setProperty("searchConfiguration", context.getParentConfiguration());
        target.setIndexProperty(source.getIndexProperty());
        target.setFacetType(source.getFacetType());
        if(MapUtils.isNotEmpty(source.getName()))
        {
            Set<Locale> supportedLocales = this.i18NService.getSupportedLocales();
            for(Locale locale : supportedLocales)
            {
                target.setName((String)source.getName().get(locale.toString()), locale);
            }
        }
        target.setPriority(source.getPriority());
        target.setValuesSortProvider(source.getValuesSortProvider());
        target.setValuesDisplayNameProvider(source.getValuesDisplayNameProvider());
        target.setTopValuesProvider(source.getTopValuesProvider());
        target.setTopValuesSize(source.getTopValuesSize());
        target.setPromotedValues(this.asPromotedFacetValueReverseConverter.convertAll(source.getPromotedValues(), newContext));
        target.setExcludedValues(this.asExcludedFacetValueReverseConverter.convertAll(source.getExcludedValues(), newContext));
        target.setSort(source.getSort());
        target.setRanged(source.isRanged());
        target.setRangeIncludeFrom(source.isRangeIncludeFrom());
        target.setRangeIncludeTo(source.isRangeIncludeTo());
        target.setRanges(this.asFacetRangeReverseConverter.convertAll(source.getRanges(), newContext));
    }


    public ContextAwareConverter<AsPromotedFacetValue, AsPromotedFacetValueModel, AsItemConfigurationReverseConverterContext> getAsPromotedFacetValueReverseConverter()
    {
        return this.asPromotedFacetValueReverseConverter;
    }


    @Required
    public void setAsPromotedFacetValueReverseConverter(ContextAwareConverter<AsPromotedFacetValue, AsPromotedFacetValueModel, AsItemConfigurationReverseConverterContext> asPromotedFacetValueReverseConverter)
    {
        this.asPromotedFacetValueReverseConverter = asPromotedFacetValueReverseConverter;
    }


    public ContextAwareConverter<AsExcludedFacetValue, AsExcludedFacetValueModel, AsItemConfigurationReverseConverterContext> getAsExcludedFacetValueReverseConverter()
    {
        return this.asExcludedFacetValueReverseConverter;
    }


    @Required
    public void setAsExcludedFacetValueReverseConverter(ContextAwareConverter<AsExcludedFacetValue, AsExcludedFacetValueModel, AsItemConfigurationReverseConverterContext> asExcludedFacetValueReverseConverter)
    {
        this.asExcludedFacetValueReverseConverter = asExcludedFacetValueReverseConverter;
    }


    public ContextAwareConverter<AsFacetRange, AsFacetRangeModel, AsItemConfigurationReverseConverterContext> getAsFacetRangeReverseConverter()
    {
        return this.asFacetRangeReverseConverter;
    }


    @Required
    public void setAsFacetRangeReverseConverter(ContextAwareConverter<AsFacetRange, AsFacetRangeModel, AsItemConfigurationReverseConverterContext> asFacetRangeReverseConverter)
    {
        this.asFacetRangeReverseConverter = asFacetRangeReverseConverter;
    }


    public I18NService getI18NService()
    {
        return this.i18NService;
    }


    @Required
    public void setI18NService(I18NService i18NService)
    {
        this.i18NService = i18NService;
    }
}
