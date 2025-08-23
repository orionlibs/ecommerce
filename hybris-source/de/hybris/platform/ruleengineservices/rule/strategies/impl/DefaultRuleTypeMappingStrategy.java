package de.hybris.platform.ruleengineservices.rule.strategies.impl;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.model.AbstractRuleTemplateModel;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleTypeMappingException;
import de.hybris.platform.ruleengineservices.rule.strategies.RuleTypeMappingStrategy;
import de.hybris.platform.servicelayer.type.TypeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultRuleTypeMappingStrategy implements RuleTypeMappingStrategy
{
    private static final String TEMPLATE_SUFFIX = "Template";
    private TypeService typeService;


    public Class<? extends AbstractRuleModel> findRuleType(Class<? extends AbstractRuleTemplateModel> templateType)
    {
        if(templateType == null)
        {
            throw new RuleTypeMappingException("Template type cannot be null");
        }
        ComposedTypeModel composedType = this.typeService.getComposedTypeForClass(templateType);
        String typeCode = composedType.getCode();
        if(!StringUtils.endsWith(typeCode, "Template"))
        {
            throw new RuleTypeMappingException("Template name does not follow a convention");
        }
        String potentialRuleTypeCode = StringUtils.removeEnd(typeCode, "Template");
        return this.typeService.getModelClass(potentialRuleTypeCode);
    }


    public TypeService getTypeService()
    {
        return this.typeService;
    }


    @Required
    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
