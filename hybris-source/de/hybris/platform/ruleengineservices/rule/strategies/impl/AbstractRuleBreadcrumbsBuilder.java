package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleMessageFormatStrategy;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleMessageParameterDecorator;
import de.hybris.platform.security.XssEncodeService;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class AbstractRuleBreadcrumbsBuilder
{
    protected static final String PARAMETER_CLASS = "rule-parameter";
    protected static final String SEPARATOR_CLASS = "rule-separator";
    private RuleMessageFormatStrategy ruleMessageFormatStrategy;
    private XssEncodeService xssEncodeService;


    protected String formatBreadcrumb(String breadcrumb, Map<String, RuleParameterData> parameters, Locale locale, boolean styled, boolean decorated)
    {
        if(StringUtils.isBlank(breadcrumb))
        {
            return "";
        }
        RuleMessageParameterDecorator parameterDecorator = (formattedValue, parameter) -> {
            if(styled)
            {
                String escapedValue = getXssEncodeService().encodeHtml(formattedValue);
                return decorated ? decorateValue(escapedValue, "rule-parameter", true) : escapedValue;
            }
            return formattedValue;
        };
        return this.ruleMessageFormatStrategy.format(breadcrumb, parameters, locale, parameterDecorator);
    }


    protected String decorateValue(String value, String styleClass, boolean styled)
    {
        if(styled)
        {
            return "<span class=\"" + styleClass + "\">" + value + "</span>";
        }
        return value;
    }


    public RuleMessageFormatStrategy getRuleMessageFormatStrategy()
    {
        return this.ruleMessageFormatStrategy;
    }


    @Required
    public void setRuleMessageFormatStrategy(RuleMessageFormatStrategy ruleMessageFormatStrategy)
    {
        this.ruleMessageFormatStrategy = ruleMessageFormatStrategy;
    }


    protected XssEncodeService getXssEncodeService()
    {
        return this.xssEncodeService;
    }


    @Required
    public void setXssEncodeService(XssEncodeService xssEncodeService)
    {
        this.xssEncodeService = xssEncodeService;
    }
}
