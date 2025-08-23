package de.hybris.platform.ruleengineservices.rule.services.impl.converters.populators;

import de.hybris.platform.converters.Converters;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionCategoryModel;
import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionModel;
import de.hybris.platform.ruleengineservices.model.RuleActionDefinitionParameterModel;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionCategoryData;
import de.hybris.platform.ruleengineservices.rule.data.RuleActionDefinitionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterDefinitionData;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class RuleActionDefinitionPopulator implements Populator<RuleActionDefinitionModel, RuleActionDefinitionData>
{
    private Converter<RuleActionDefinitionCategoryModel, RuleActionDefinitionCategoryData> ruleActionDefinitionCategoryConverter;
    private Converter<RuleActionDefinitionParameterModel, RuleParameterDefinitionData> ruleActionDefinitionParameterConverter;


    public void populate(RuleActionDefinitionModel source, RuleActionDefinitionData target)
    {
        target.setId(source.getId());
        target.setName(source.getName());
        target.setPriority(source.getPriority());
        target.setBreadcrumb(source.getBreadcrumb());
        target.setTranslatorId(source.getTranslatorId());
        target.setTranslatorParameters(new HashMap<>(source.getTranslatorParameters()));
        target.setCategories(populateCategories(source));
        target.setParameters(populateParameters(source));
    }


    protected List<RuleActionDefinitionCategoryData> populateCategories(RuleActionDefinitionModel source)
    {
        if(CollectionUtils.isNotEmpty(source.getCategories()))
        {
            return Converters.convertAll(source.getCategories(), this.ruleActionDefinitionCategoryConverter);
        }
        return Collections.emptyList();
    }


    protected Map<String, RuleParameterDefinitionData> populateParameters(RuleActionDefinitionModel source)
    {
        Map<String, RuleParameterDefinitionData> parameters = new HashMap<>();
        if(CollectionUtils.isNotEmpty(source.getParameters()))
        {
            for(RuleActionDefinitionParameterModel sourceParameter : source.getParameters())
            {
                String parameterId = sourceParameter.getId();
                RuleParameterDefinitionData parameter = (RuleParameterDefinitionData)this.ruleActionDefinitionParameterConverter.convert(sourceParameter);
                parameters.put(parameterId, parameter);
            }
        }
        return parameters;
    }


    public Converter<RuleActionDefinitionCategoryModel, RuleActionDefinitionCategoryData> getRuleActionDefinitionCategoryConverter()
    {
        return this.ruleActionDefinitionCategoryConverter;
    }


    @Required
    public void setRuleActionDefinitionCategoryConverter(Converter<RuleActionDefinitionCategoryModel, RuleActionDefinitionCategoryData> ruleActionDefinitionCategoryConverter)
    {
        this.ruleActionDefinitionCategoryConverter = ruleActionDefinitionCategoryConverter;
    }


    public Converter<RuleActionDefinitionParameterModel, RuleParameterDefinitionData> getRuleActionDefinitionParameterConverter()
    {
        return this.ruleActionDefinitionParameterConverter;
    }


    @Required
    public void setRuleActionDefinitionParameterConverter(Converter<RuleActionDefinitionParameterModel, RuleParameterDefinitionData> ruleActionDefinitionParameterConverter)
    {
        this.ruleActionDefinitionParameterConverter = ruleActionDefinitionParameterConverter;
    }
}
