package de.hybris.platform.ruleengineservices.rule.strategies.impl.mappers;

import de.hybris.platform.core.model.enumeration.EnumerationValueModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapper;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueMapperException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import org.springframework.beans.factory.annotation.Required;

public class DynamicEnumRuleParameterValueMapper implements RuleParameterValueMapper<EnumerationValueModel>
{
    private TypeService typeService;
    private final String enumerationCode;


    public DynamicEnumRuleParameterValueMapper(String enumerationCode)
    {
        this.enumerationCode = enumerationCode;
    }


    public String toString(EnumerationValueModel value)
    {
        ServicesUtil.validateParameterNotNull(value, "EnumerationValueModel cannot be null");
        return value.getCode();
    }


    public EnumerationValueModel fromString(String code)
    {
        ServicesUtil.validateParameterNotNull(code, "String value cannot be null");
        try
        {
            return getTypeService().getEnumerationValue(this.enumerationCode, code);
        }
        catch(UnknownIdentifierException ex)
        {
            throw new RuleParameterValueMapperException("Could not find Enumeration Value with the code: " + code);
        }
    }


    protected TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
