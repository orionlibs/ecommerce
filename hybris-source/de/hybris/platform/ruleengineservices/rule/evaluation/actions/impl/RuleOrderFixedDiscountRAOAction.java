package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleExecutableSupport;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.collections.CollectionUtils;

public class RuleOrderFixedDiscountRAOAction extends AbstractRuleExecutableSupport
{
    public boolean performActionInternal(RuleActionContext context)
    {
        Map<String, BigDecimal> values = (Map<String, BigDecimal>)context.getParameter("value");
        CartRAO cartRao = context.getCartRao();
        BigDecimal discountValueForCartCurrency = values.get(cartRao.getCurrencyIsoCode());
        return (Objects.nonNull(discountValueForCartCurrency) && performAction(context, discountValueForCartCurrency));
    }


    protected boolean performAction(RuleActionContext context, BigDecimal amount)
    {
        CartRAO cartRao = context.getCartRao();
        if(CollectionUtils.isNotEmpty(cartRao.getEntries()))
        {
            RuleEngineResultRAO result = context.getRuleEngineResultRao();
            DiscountRAO discount = getRuleEngineCalculationService().addOrderLevelDiscount(cartRao, true, amount);
            result.getActions().add(discount);
            setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)discount});
            context.scheduleForUpdate(new Object[] {cartRao, result});
            context.insertFacts(new Object[] {discount});
            return true;
        }
        return false;
    }
}
