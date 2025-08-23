package de.hybris.platform.ruleengineservices.rule.services.impl.converters.populators;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionParameterModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleConverterException;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleParameterValueConverter;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import java.util.Collections;
import org.springframework.beans.factory.annotation.Required;

public class RuleConditionDefinitionParameterPopulator implements Populator<RuleConditionDefinitionParameterModel, RuleParameterDefinitionData>
{
    private RuleParameterValueConverter ruleParameterValueConverter;


    public RuleParameterValueConverter getRuleParameterValueConverter()
    {
        return this.ruleParameterValueConverter;
    }


    @Required
    public void setRuleParameterValueConverter(RuleParameterValueConverter ruleParameterValueConverter)
    {
        this.ruleParameterValueConverter = ruleParameterValueConverter;
    }


    public void populate(RuleConditionDefinitionParameterModel source, RuleParameterDefinitionData target)
    {
        target.setName(source.getName());
        target.setDescription(source.getDescription());
        target.setPriority(source.getPriority());
        target.setType(source.getType());
        target.setRequired(source.getRequired());
        target.setValidators(source.getValidators());
        target.setDefaultEditor(source.getDefaultEditor());
        target.setFilters((source.getFilters() != null) ? source.getFilters() : Collections.emptyMap());
        try
        {
            Object defaultValue = this.ruleParameterValueConverter.fromString(source.getValue(), source.getType());
            target.setDefaultValue(defaultValue);
        }
        catch(RuleConverterException e)
        {
            throw new ConversionException(e.getMessage(), e);
        }
    }
}
