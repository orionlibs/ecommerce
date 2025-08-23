package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class CurrencyRuleParameterValueMapper implements RuleParameterValueMapper<CurrencyModel>
{
    private CommonI18NService commonI18NService;


    public String toString(CurrencyModel currency)
    {
        ServicesUtil.validateParameterNotNull(currency, "Object cannot be null");
        return currency.getIsocode();
    }


    public CurrencyModel fromString(String value)
    {
        ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
        CurrencyModel currency = this.commonI18NService.getCurrency(value);
        if(currency == null)
        {
            throw new RuleParameterValueMapperException("Cannot find currency with the code: " + value);
        }
        return currency;
    }


    public CommonI18NService getCommonI18NService()
    {
        return this.commonI18NService;
    }


    @Required
    public void setCommonI18NService(CommonI18NService commonI18NService)
    {
        this.commonI18NService = commonI18NService;
    }
}
