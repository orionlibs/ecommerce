package de.hybris.platform.adaptivesearch.converters.populators;

import de.hybris.platform.adaptivesearch.converters.AsItemConfigurationReverseConverterContext;
import de.hybris.platform.adaptivesearch.data.AbstractAsSortConfiguration;
import de.hybris.platform.adaptivesearch.data.AsSortExpression;
import de.hybris.platform.adaptivesearch.model.AbstractAsConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AbstractAsSortConfigurationModel;
import de.hybris.platform.adaptivesearch.model.AsSortExpressionModel;
import de.hybris.platform.adaptivesearch.util.ContextAwareConverter;
import de.hybris.platform.adaptivesearch.util.ContextAwarePopulator;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class AsSortConfigurationReversePopulator implements ContextAwarePopulator<AbstractAsSortConfiguration, AbstractAsSortConfigurationModel, AsItemConfigurationReverseConverterContext>
{
    private I18NService i18NService;
    private ContextAwareConverter<AsSortExpression, AsSortExpressionModel, AsItemConfigurationReverseConverterContext> asSortExpressionReverseConverter;


    public void populate(AbstractAsSortConfiguration source, AbstractAsSortConfigurationModel target, AsItemConfigurationReverseConverterContext context)
    {
        AsItemConfigurationReverseConverterContext newContext = new AsItemConfigurationReverseConverterContext();
        newContext.setCatalogVersion(context.getCatalogVersion());
        newContext.setParentConfiguration((AbstractAsConfigurationModel)target);
        target.setProperty("searchConfiguration", context.getParentConfiguration());
        target.setCode(source.getCode());
        Set<Locale> supportedLocales = this.i18NService.getSupportedLocales();
        for(Locale locale : supportedLocales)
        {
            target.setName((String)source.getName().get(locale.toString()), locale);
        }
        target.setPriority(source.getPriority());
        target.setApplyPromotedItems(source.isApplyPromotedItems());
        target.setHighlightPromotedItems(source.isHighlightPromotedItems());
        target.setExpressions(this.asSortExpressionReverseConverter.convertAll(source.getExpressions(), newContext));
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


    public ContextAwareConverter<AsSortExpression, AsSortExpressionModel, AsItemConfigurationReverseConverterContext> getAsSortExpressionReverseConverter()
    {
        return this.asSortExpressionReverseConverter;
    }


    @Required
    public void setAsSortExpressionReverseConverter(ContextAwareConverter<AsSortExpression, AsSortExpressionModel, AsItemConfigurationReverseConverterContext> asSortExpressionReverseConverter)
    {
        this.asSortExpressionReverseConverter = asSortExpressionReverseConverter;
    }
}
