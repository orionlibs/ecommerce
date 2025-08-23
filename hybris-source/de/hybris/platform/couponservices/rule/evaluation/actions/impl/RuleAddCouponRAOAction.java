package de.hybris.platform.couponservices.rule.evaluation.actions.impl;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengineservices.rao.AbstractActionedRAO;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.AddCouponRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleExecutableSupport;
import java.util.Objects;

public class RuleAddCouponRAOAction extends AbstractRuleExecutableSupport
{
    public boolean performActionInternal(RuleActionContext context)
    {
        String couponId = (String)context.getParameter("value");
        addCoupon(context, couponId);
        return true;
    }


    protected void addCoupon(RuleActionContext context, String couponId)
    {
        Preconditions.checkArgument(Objects.nonNull(couponId), "Coupon ID argument must not be empty");
        AddCouponRAO addCouponRao = new AddCouponRAO();
        addCouponRao.setCouponId(couponId);
        CartRAO cart = (CartRAO)context.getValue(CartRAO.class);
        getRaoUtils().addAction((AbstractActionedRAO)cart, (AbstractRuleActionRAO)addCouponRao);
        RuleEngineResultRAO result = context.getRuleEngineResultRao();
        result.getActions().add(addCouponRao);
        setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)addCouponRao});
        context.scheduleForUpdate(new Object[] {cart, result});
        context.insertFacts(new Object[] {addCouponRao});
    }
}
