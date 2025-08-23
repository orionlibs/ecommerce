package de.hybris.platform.promotionengineservices.compiler.listeners;

import de.hybris.platform.promotionengineservices.compiler.strategies.ConditionResolutionStrategy;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.ruleengine.dao.EngineRuleDao;
import de.hybris.platform.ruleengine.model.AbstractRuleEngineRuleModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerListener;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rao.WebsiteGroupRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;

public class PromotionRuleCompilerListener implements RuleCompilerListener
{
    private Map<String, ConditionResolutionStrategy> conditionResolutionStrategies;
    private ModelService modelService;
    private EngineRuleDao engineRuleDao;


    public void beforeCompile(RuleCompilerContext context)
    {
        if(context.getRule() instanceof PromotionSourceRuleModel)
        {
            context.generateVariable(CartRAO.class);
            context.generateVariable(RuleEngineResultRAO.class);
            context.generateVariable(WebsiteGroupRAO.class);
            cleanStoredParameterValues(context);
        }
    }


    protected void cleanStoredParameterValues(RuleCompilerContext context)
    {
        if(MapUtils.isNotEmpty(getConditionResolutionStrategies()))
        {
            for(ConditionResolutionStrategy strategy : getConditionResolutionStrategies().values())
            {
                strategy.cleanStoredParameterValues(context);
            }
        }
    }


    public void afterCompile(RuleCompilerContext context)
    {
        if(context.getRule() instanceof PromotionSourceRuleModel && MapUtils.isNotEmpty(getConditionResolutionStrategies()))
        {
            extractAndStoreParamValues((PromotionSourceRuleModel)context.getRule(), context.getModuleName(), context
                            .getRuleConditions());
        }
    }


    protected void extractAndStoreParamValues(PromotionSourceRuleModel rule, String moduleName, List<RuleConditionData> conditions)
    {
        if(CollectionUtils.isNotEmpty(conditions))
        {
            AbstractRuleEngineRuleModel engineRule = getEngineRuleDao().getRuleByCode(rule.getCode(), moduleName);
            for(RuleConditionData condition : conditions)
            {
                ConditionResolutionStrategy strategy = getConditionResolutionStrategies().get(condition.getDefinitionId());
                if(strategy != null)
                {
                    strategy.getAndStoreParameterValues(condition, rule, engineRule.getPromotion());
                }
            }
        }
    }


    public void afterCompileError(RuleCompilerContext context)
    {
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected EngineRuleDao getEngineRuleDao()
    {
        return this.engineRuleDao;
    }


    @Required
    public void setEngineRuleDao(EngineRuleDao engineRuleDao)
    {
        this.engineRuleDao = engineRuleDao;
    }


    protected Map<String, ConditionResolutionStrategy> getConditionResolutionStrategies()
    {
        return this.conditionResolutionStrategies;
    }


    @Required
    public void setConditionResolutionStrategies(Map<String, ConditionResolutionStrategy> conditionResolutionStrategies)
    {
        this.conditionResolutionStrategies = conditionResolutionStrategies;
    }
}
