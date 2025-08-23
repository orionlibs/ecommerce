package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsSortExpression;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSortExpressionModel;
import de.hybris.platform.adaptivesearch.util.ContextAwareConverter;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class AsSortConfigurationPopulator implements ContextAwarePopulator<AbstractAsSortConfigurationModel, AbstractAsSortConfiguration, AsItemConfigurationConverterContext>
{
    private I18NService i18NService;
    private ContextAwareConverter<AsSortExpressionModel, AsSortExpression, AsItemConfigurationConverterContext> asSortExpressionConverter;


    public void populate(AbstractAsSortConfigurationModel source, AbstractAsSortConfiguration target, AsItemConfigurationConverterContext context)
    {
        target.setCode(source.getCode());
        target.setName(buildLocalizedName(source));
        target.setPriority(source.getPriority());
        target.setApplyPromotedItems(source.isApplyPromotedItems());
        target.setHighlightPromotedItems(source.isHighlightPromotedItems());
        target.setExpressions(this.asSortExpressionConverter.convertAll(source.getExpressions(), context));
    }


    protected Map<String, String> buildLocalizedName(AbstractAsSortConfigurationModel source)
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


    public ContextAwareConverter<AsSortExpressionModel, AsSortExpression, AsItemConfigurationConverterContext> getAsSortExpressionConverter()
    {
        return this.asSortExpressionConverter;
    }


    @Required
    public void setAsSortExpressionConverter(ContextAwareConverter<AsSortExpressionModel, AsSortExpression, AsItemConfigurationConverterContext> asSortExpressionConverter)
    {
        this.asSortExpressionConverter = asSortExpressionConverter;
    }
}
