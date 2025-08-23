package de.hybris.platform.ruleengineservices.rule.services.impl.converters.populators;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionCategoryModel;
import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionModel;
import de.hybris.platform.ruleengineservices.model.RuleConditionDefinitionParameterModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionCategoryData;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleConditionDefinitionPopulator implements Populator<RuleConditionDefinitionModel, RuleConditionDefinitionData>
{
    private Converter<RuleConditionDefinitionCategoryModel, RuleConditionDefinitionCategoryData> ruleConditionDefinitionCategoryConverter;
    private Converter<RuleConditionDefinitionParameterModel, RuleParameterDefinitionData> ruleConditionDefinitionParameterConverter;


    public void populate(RuleConditionDefinitionModel source, RuleConditionDefinitionData target)
    {
        target.setId(source.getId());
        target.setName(source.getName());
        target.setPriority(source.getPriority());
        target.setBreadcrumb(source.getBreadcrumb());
        target.setAllowsChildren(source.getAllowsChildren());
        target.setTranslatorId(source.getTranslatorId());
        target.setTranslatorParameters(MapUtils.isEmpty(source.getTranslatorParameters()) ? Collections.emptyMap() :
                        new HashMap<>(source.getTranslatorParameters()));
        target.setCategories(populateCategories(source));
        target.setParameters(populateParameters(source));
    }


    protected List<RuleConditionDefinitionCategoryData> populateCategories(RuleConditionDefinitionModel source)
    {
        if(CollectionUtils.isNotEmpty(source.getCategories()))
        {
            return Converters.convertAll(source.getCategories(), this.ruleConditionDefinitionCategoryConverter);
        }
        return Collections.emptyList();
    }


    protected Map<String, RuleParameterDefinitionData> populateParameters(RuleConditionDefinitionModel source)
    {
        Map<String, RuleParameterDefinitionData> parameters = new HashMap<>();
        if(CollectionUtils.isNotEmpty(source.getParameters()))
        {
            for(RuleConditionDefinitionParameterModel sourceParameter : source.getParameters())
            {
                String parameterId = sourceParameter.getId();
                RuleParameterDefinitionData parameter = (RuleParameterDefinitionData)this.ruleConditionDefinitionParameterConverter.convert(sourceParameter);
                parameters.put(parameterId, parameter);
            }
        }
        return parameters;
    }


    public Converter<RuleConditionDefinitionCategoryModel, RuleConditionDefinitionCategoryData> getRuleConditionDefinitionCategoryConverter()
    {
        return this.ruleConditionDefinitionCategoryConverter;
    }


    @Required
    public void setRuleConditionDefinitionCategoryConverter(Converter<RuleConditionDefinitionCategoryModel, RuleConditionDefinitionCategoryData> ruleConditionDefinitionCategoryConverter)
    {
        this.ruleConditionDefinitionCategoryConverter = ruleConditionDefinitionCategoryConverter;
    }


    public Converter<RuleConditionDefinitionParameterModel, RuleParameterDefinitionData> getRuleConditionDefinitionParameterConverter()
    {
        return this.ruleConditionDefinitionParameterConverter;
    }


    @Required
    public void setRuleConditionDefinitionParameterConverter(Converter<RuleConditionDefinitionParameterModel, RuleParameterDefinitionData> ruleConditionDefinitionParameterConverter)
    {
        this.ruleConditionDefinitionParameterConverter = ruleConditionDefinitionParameterConverter;
    }
}
