package de.hybris.platform.promotionengineservices.promotionengine.impl;

import de.hybris.platform.promotionengineservices.promotionengine.PromotionMessageParameterResolutionStrategy;
import de.hybris.platform.promotions.model.PromotionResultModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Required;

public class DefaultListResolutionStrategy implements PromotionMessageParameterResolutionStrategy
{
    protected static final String LIST_ITEMS_SEPARATOR_KEY = "promotionengineservices.listresolutionstrategy.separator";
    private PromotionMessageParameterResolutionStrategy resolutionStrategy;
    private ConfigurationService configurationService;


    public String getValue(RuleParameterData data, PromotionResultModel promotionResult, Locale locale)
    {
        ServicesUtil.validateParameterNotNull(data, "parameter data must not be null");
        ServicesUtil.validateParameterNotNull(promotionResult, "parameter promotionResult must not be null");
        ServicesUtil.validateParameterNotNull(locale, "parameter locale must not be null");
        List<Object> items = (List<Object>)data.getValue();
        return items.stream()
                        .map(item -> createRuleParameterData(data, item))
                        .map(newData -> itemValue(promotionResult, locale, newData))
                        .collect(Collectors.joining(joiningSeparator()));
    }


    protected String itemValue(PromotionResultModel promotionResult, Locale locale, RuleParameterData newData)
    {
        return String.valueOf(getResolutionStrategy().getValue(newData, promotionResult, locale));
    }


    protected RuleParameterData createRuleParameterData(RuleParameterData source, Object value)
    {
        RuleParameterData data = new RuleParameterData();
        data.setType(source.getType());
        data.setValue(value);
        return data;
    }


    protected PromotionMessageParameterResolutionStrategy getResolutionStrategy()
    {
        return this.resolutionStrategy;
    }


    @Required
    public void setResolutionStrategy(PromotionMessageParameterResolutionStrategy resolutionStrategy)
    {
        this.resolutionStrategy = resolutionStrategy;
    }


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
    }


    protected String joiningSeparator()
    {
        return getConfigurationService().getConfiguration().getString("promotionengineservices.listresolutionstrategy.separator", ",") + " ";
    }
}
