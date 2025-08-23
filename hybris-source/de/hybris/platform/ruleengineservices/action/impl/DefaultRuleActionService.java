package de.hybris.platform.ruleengineservices.action.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.ruleengineservices.action.RuleActionService;
import de.hybris.platform.ruleengineservices.action.RuleActionStrategy;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.FreeProductRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRuleActionService implements RuleActionService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRuleActionService.class);
    private Map<String, RuleActionStrategy> actionStrategiesMapping;


    public List<ItemModel> applyAllActions(RuleEngineResultRAO ruleEngineResultRAO)
    {
        List<ItemModel> actionResults = Lists.newArrayList();
        if(ruleEngineResultRAO != null && ruleEngineResultRAO.getActions() != null)
        {
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("applyAllActions triggered for actions: [{}]", ruleEngineResultRAO
                                .getActions().stream().map(AbstractRuleActionRAO::getFiredRuleCode)
                                .collect(Collectors.joining(", ")));
            }
            for(AbstractRuleActionRAO action : ruleEngineResultRAO.getActions())
            {
                RuleActionStrategy strategy = getRuleActionStrategy(action.getActionStrategyKey());
                if(Objects.isNull(strategy))
                {
                    LOGGER.error("Strategy bean for key '{}' not found!", action.getActionStrategyKey());
                    continue;
                }
                if(isActionApplicable(action, ruleEngineResultRAO.getActions()))
                {
                    actionResults.addAll(strategy.apply(action));
                }
            }
        }
        else
        {
            LOGGER.warn("applyAllActions called for undefined action set!");
        }
        return actionResults;
    }


    protected boolean isActionApplicable(AbstractRuleActionRAO action, Set<AbstractRuleActionRAO> actions)
    {
        if(!(action instanceof DiscountRAO))
        {
            return true;
        }
        DiscountRAO discount = (DiscountRAO)action;
        if(discount.getValue() == null || discount.getValue().compareTo(BigDecimal.ZERO) > 0)
        {
            return true;
        }
        Objects.requireNonNull(FreeProductRAO.class);
        return (discount.getValue().intValue() == 0 && actions.stream().filter(FreeProductRAO.class::isInstance)
                        .anyMatch(d -> ((FreeProductRAO)d).getAddedOrderEntry().equals(action.getAppliedToObject())));
    }


    protected RuleActionStrategy getRuleActionStrategy(String strategyKey)
    {
        if(getActionStrategiesMapping() != null)
        {
            RuleActionStrategy strategy = getActionStrategiesMapping().get(strategyKey);
            if(strategy != null)
            {
                return strategy;
            }
            throw new IllegalArgumentException("cannot find RuleActionStrategy for given action: " + strategyKey);
        }
        throw new IllegalStateException("cannot call getActionStrategiesMapping(\"" + strategyKey + "\"), no strategy mapping defined! Please configure your DefaultRuleActionService bean to contain actionStrategiesMapping.");
    }


    public Map<String, RuleActionStrategy> getActionStrategiesMapping()
    {
        return this.actionStrategiesMapping;
    }


    public void setActionStrategiesMapping(Map<String, RuleActionStrategy> actionStrategiesMapping)
    {
        this.actionStrategiesMapping = actionStrategiesMapping;
    }
}
