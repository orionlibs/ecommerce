package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class LanguageRuleParameterValueMapper implements RuleParameterValueMapper<LanguageModel>
{
    private CommonI18NService commonI18NService;


    public String toString(LanguageModel language)
    {
        ServicesUtil.validateParameterNotNull(language, "Object cannot be null");
        return language.getIsocode();
    }


    public LanguageModel fromString(String value)
    {
        ServicesUtil.validateParameterNotNull(value, "String value cannot be null");
        LanguageModel language = this.commonI18NService.getLanguage(value);
        if(language == null)
        {
            throw new RuleParameterValueMapperException("Cannot find language with the code: " + value);
        }
        return language;
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
