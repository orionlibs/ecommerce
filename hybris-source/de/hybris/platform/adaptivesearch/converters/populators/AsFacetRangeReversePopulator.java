package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AsFacetRange;
import de.hybris.platform.adaptivesearch.model.AbstractAsFacetConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsFacetRangeModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class AsFacetRangeReversePopulator implements ContextAwarePopulator<AsFacetRange, AsFacetRangeModel, AsItemConfigurationReverseConverterContext>
{
    private I18NService i18NService;


    public void populate(AsFacetRange source, AsFacetRangeModel target, AsItemConfigurationReverseConverterContext context)
    {
        target.setFacetConfiguration((AbstractAsFacetConfigurationModel)context.getParentConfiguration());
        target.setId(source.getId());
        Set<Locale> supportedLocales = this.i18NService.getSupportedLocales();
        for(Locale locale : supportedLocales)
        {
            target.setName((String)source.getName().get(locale.toString()), locale);
        }
        target.setCatalogVersion(context.getCatalogVersion());
        target.setQualifier(source.getQualifier());
        target.setFrom(source.getFrom());
        target.setTo(source.getTo());
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
