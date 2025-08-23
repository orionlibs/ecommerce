package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsFacetConfiguration;
import de.hybris.platform.adaptivesearch.data.AsExcludedFacetValue;
import de.hybris.platform.adaptivesearch.data.AsFacetRange;
import de.hybris.platform.adaptivesearch.data.AsPromotedFacetValue;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsExcludedFacetValueModel;
import de.hybris.platform.adaptivesearch.model.AsFacetRangeModel;
import de.hybris.platform.adaptivesearch.model.AsPromotedFacetValueModel;
import de.hybris.platform.adaptivesearch.util.ContextAwareConverter;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class AsFacetConfigurationPopulator implements ContextAwarePopulator<AbstractAsFacetConfigurationModel, AbstractAsFacetConfiguration, AsItemConfigurationConverterContext>
{
    private ContextAwareConverter<AsPromotedFacetValueModel, AsPromotedFacetValue, AsItemConfigurationConverterContext> asPromotedFacetValueConverter;
    private ContextAwareConverter<AsExcludedFacetValueModel, AsExcludedFacetValue, AsItemConfigurationConverterContext> asExcludedFacetValueConverter;
    private ContextAwareConverter<AsFacetRangeModel, AsFacetRange, AsItemConfigurationConverterContext> asFacetRangeConverter;
    private I18NService i18NService;


    public void populate(AbstractAsFacetConfigurationModel source, AbstractAsFacetConfiguration target, AsItemConfigurationConverterContext context)
    {
        target.setIndexProperty(source.getIndexProperty());
        target.setFacetType(source.getFacetType());
        target.setName(buildLocalizedName(source));
        target.setPriority(source.getPriority());
        target.setValuesSortProvider(source.getValuesSortProvider());
        target.setValuesDisplayNameProvider(source.getValuesDisplayNameProvider());
        target.setTopValuesProvider(source.getTopValuesProvider());
        target.setTopValuesSize(source.getTopValuesSize());
        target.setPromotedValues(this.asPromotedFacetValueConverter.convertAll(source.getPromotedValues(), context));
        target.setExcludedValues(this.asExcludedFacetValueConverter.convertAll(source.getExcludedValues(), context));
        target.setSort(source.getSort());
        target.setRanged(source.isRanged());
        target.setRangeIncludeFrom(source.isRangeIncludeFrom());
        target.setRangeIncludeTo(source.isRangeIncludeTo());
        target.setRanges(this.asFacetRangeConverter.convertAll(source.getRanges(), context));
    }


    protected Map<String, String> buildLocalizedName(AbstractAsFacetConfigurationModel source)
    {
        Set<Locale> supportedLocales = this.i18NService.getSupportedLocales();
        Map<String, String> target = new LinkedHashMap<>();
        for(Locale locale : supportedLocales)
        {
            target.put(locale.toString(), source.getName(locale));
        }
        return target;
    }


    public ContextAwareConverter<AsPromotedFacetValueModel, AsPromotedFacetValue, AsItemConfigurationConverterContext> getAsPromotedFacetValueConverter()
    {
        return this.asPromotedFacetValueConverter;
    }


    @Required
    public void setAsPromotedFacetValueConverter(ContextAwareConverter<AsPromotedFacetValueModel, AsPromotedFacetValue, AsItemConfigurationConverterContext> asPromotedFacetValueConverter)
    {
        this.asPromotedFacetValueConverter = asPromotedFacetValueConverter;
    }


    public ContextAwareConverter<AsExcludedFacetValueModel, AsExcludedFacetValue, AsItemConfigurationConverterContext> getAsExcludedFacetValueConverter()
    {
        return this.asExcludedFacetValueConverter;
    }


    @Required
    public void setAsExcludedFacetValueConverter(ContextAwareConverter<AsExcludedFacetValueModel, AsExcludedFacetValue, AsItemConfigurationConverterContext> asExcludedFacetValueConverter)
    {
        this.asExcludedFacetValueConverter = asExcludedFacetValueConverter;
    }


    public ContextAwareConverter<AsFacetRangeModel, AsFacetRange, AsItemConfigurationConverterContext> getAsFacetRangeConverter()
    {
        return this.asFacetRangeConverter;
    }


    @Required
    public void setAsFacetRangeConverter(ContextAwareConverter<AsFacetRangeModel, AsFacetRange, AsItemConfigurationConverterContext> asFacetRangeConverter)
    {
        this.asFacetRangeConverter = asFacetRangeConverter;
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
