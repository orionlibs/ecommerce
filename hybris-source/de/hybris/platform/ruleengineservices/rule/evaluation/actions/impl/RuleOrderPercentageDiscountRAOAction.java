package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleExecutableSupport;
import java.math.BigDecimal;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleOrderPercentageDiscountRAOAction extends AbstractRuleExecutableSupport
{
    private static final Logger LOG = LoggerFactory.getLogger(RuleOrderPercentageDiscountRAOAction.class);


    public boolean performActionInternal(RuleActionContext context)
    {
        return ((Boolean)extractAmountForCurrency(context, context.getParameter("value")).map(a -> Boolean.valueOf(performAction(context, a))).orElseGet(() -> {
            LOG.error("no matching discount amount specified for rule {}, cannot apply rule action.", getRuleCode(context));
            return Boolean.valueOf(false);
        })).booleanValue();
    }


    protected boolean performAction(RuleActionContext context, BigDecimal amount)
    {
        CartRAO cartRao = context.getCartRao();
        if(CollectionUtils.isNotEmpty(cartRao.getEntries()))
        {
            DiscountRAO discount = getRuleEngineCalculationService().addOrderLevelDiscount(cartRao, false, amount);
            RuleEngineResultRAO result = context.getRuleEngineResultRao();
            result.getActions().add(discount);
            setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)discount});
            context.scheduleForUpdate(new Object[] {cartRao, result});
            context.insertFacts(new Object[] {discount});
            return true;
        }
        return false;
    }
}
