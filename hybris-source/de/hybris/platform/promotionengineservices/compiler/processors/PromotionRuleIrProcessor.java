package de.hybris.platform.promotionengineservices.compiler.processors;

import com.google.common.collect.Maps;
import de.hybris.platform.promotionengineservices.model.PromotionSourceRuleModel;
import de.hybris.platform.promotions.PromotionsService;
import de.hybris.platform.promotions.model.PromotionGroupModel;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerContext;
import de.hybris.platform.ruleengineservices.compiler.RuleCompilerException;
import de.hybris.platform.ruleengineservices.compiler.RuleIr;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrAttributeOperator;
import de.hybris.platform.ruleengineservices.compiler.RuleIrCondition;
import de.hybris.platform.ruleengineservices.compiler.RuleIrExecutableAction;
import de.hybris.platform.ruleengineservices.compiler.RuleIrProcessor;
import de.hybris.platform.ruleengineservices.compiler.RuleIrTypeCondition;
import de.hybris.platform.ruleengineservices.configuration.Switch;
import de.hybris.platform.ruleengineservices.configuration.SwitchService;
import de.hybris.platform.ruleengineservices.model.AbstractRuleModel;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rao.WebsiteGroupRAO;
import de.hybris.platform.ruleengineservices.rule.data.RuleConditionData;
import de.hybris.platform.ruleengineservices.rule.data.RuleParameterData;
import de.hybris.platform.ruleengineservices.util.SharedParametersProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

public class PromotionRuleIrProcessor implements RuleIrProcessor
{
    public static final String WEBSITE_GROUP_RAO_ID_ATTRIBUTE = "id";
    protected static final String ORDER_CONSUMED_RAO_CART_ATTRIBUTE = "cart";
    protected static final String AVAILABLE_QUANTITY_PARAM = "availableQuantity";
    protected static final String CART_RAO_CURRENCY_ATTRIBUTE = "currencyIsoCode";
    protected static final String CART_RAO_TOTAL_ATTRIBUTE = "total";
    private PromotionsService promotionsService;
    private SharedParametersProvider sharedParametersProvider;
    private SwitchService switchService;


    public void process(RuleCompilerContext context, RuleIr ruleIr)
    {
        AbstractRuleModel sourceRule = context.getRule();
        if(sourceRule instanceof PromotionSourceRuleModel)
        {
            String cartRaoVariable = context.generateVariable(CartRAO.class);
            RuleIrTypeCondition irCartCondition = new RuleIrTypeCondition();
            irCartCondition.setVariable(cartRaoVariable);
            List<RuleIrCondition> conditions = ruleIr.getConditions();
            ruleIr.getConditions().add(irCartCondition);
            String resultRaoVariable = context.generateVariable(RuleEngineResultRAO.class);
            RuleIrTypeCondition irResultCondition = new RuleIrTypeCondition();
            irResultCondition.setVariable(resultRaoVariable);
            ruleIr.getConditions().add(irResultCondition);
            if(getSwitchService().isEnabled(Switch.GENERATOR_WEBSITEGROUP))
            {
                PromotionGroupModel website = ((PromotionSourceRuleModel)sourceRule).getWebsite();
                if(website == null)
                {
                    website = getPromotionsService().getPromotionGroup("default");
                }
                if(website == null)
                {
                    throw new RuleCompilerException("Website associated with the promotion cannot be null or empty.");
                }
                String websiteGroupRaoVariable = context.generateVariable(WebsiteGroupRAO.class);
                RuleIrAttributeCondition irWebsiteGroupCondition = new RuleIrAttributeCondition();
                irWebsiteGroupCondition.setVariable(websiteGroupRaoVariable);
                irWebsiteGroupCondition.setAttribute("id");
                irWebsiteGroupCondition.setOperator(RuleIrAttributeOperator.EQUAL);
                irWebsiteGroupCondition.setValue(website.getIdentifier());
                conditions.add(irWebsiteGroupCondition);
            }
            Map<String, Object> sharedParameters = getSharedParameters(context.getRuleConditions());
            if(!sharedParameters.isEmpty())
            {
                ruleIr.getActions()
                                .stream()
                                .filter(action -> action instanceof RuleIrExecutableAction)
                                .forEach(action -> {
                                    Map<String, Object> actionParameters = Maps.newHashMap(((RuleIrExecutableAction)action).getActionParameters());
                                    actionParameters.putAll(sharedParameters);
                                    ((RuleIrExecutableAction)action).setActionParameters(actionParameters);
                                });
            }
        }
    }


    protected Map<String, Object> getSharedParameters(List<RuleConditionData> ruleConditionDataList)
    {
        Map<String, Object> result = new HashMap<>();
        for(RuleConditionData ruleConditionData : ruleConditionDataList)
        {
            Map<String, RuleParameterData> parameters = ruleConditionData.getParameters();
            for(String parameterName : this.sharedParametersProvider.getAll())
            {
                if(parameters.containsKey(parameterName))
                {
                    RuleParameterData ruleParameterData = parameters.get(parameterName);
                    result.put(parameterName, ruleParameterData.getValue());
                }
            }
        }
        return result;
    }


    public PromotionsService getPromotionsService()
    {
        return this.promotionsService;
    }


    @Required
    public void setPromotionsService(PromotionsService promotionsService)
    {
        this.promotionsService = promotionsService;
    }


    @Required
    public void setSharedParametersProvider(SharedParametersProvider sharedParametersProvider)
    {
        this.sharedParametersProvider = sharedParametersProvider;
    }


    protected SwitchService getSwitchService()
    {
        return this.switchService;
    }


    @Required
    public void setSwitchService(SwitchService switchService)
    {
        this.switchService = switchService;
    }
}
