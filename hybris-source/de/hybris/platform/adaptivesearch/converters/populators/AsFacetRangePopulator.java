package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AsFacetRange;
import de.hybris.platform.adaptivesearch.model.AsFacetRangeModel;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class AsFacetRangePopulator implements ContextAwarePopulator<AsFacetRangeModel, AsFacetRange, AsItemConfigurationConverterContext>
{
    private I18NService i18NService;


    public void populate(AsFacetRangeModel source, AsFacetRange target, AsItemConfigurationConverterContext context)
    {
        target.setQualifier(source.getQualifier());
        target.setId(source.getId());
        target.setName(buildLocalizedName(source));
        target.setFrom(source.getFrom());
        target.setTo(source.getTo());
    }


    protected Map<String, String> buildLocalizedName(AsFacetRangeModel source)
    {
        Set<Locale> supportedLocales = this.i18NService.getSupportedLocales();
        Map<String, String> target = new LinkedHashMap<>();
        for(Locale locale : supportedLocales)
        {
            target.put(locale.toString(), source.getName(locale));
        }
        return target;
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
