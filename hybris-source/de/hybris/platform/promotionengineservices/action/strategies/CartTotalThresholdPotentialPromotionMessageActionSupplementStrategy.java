package de.hybris.platform.promotionengineservices.action.strategies;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DisplayMessageRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.ActionSupplementStrategy;
import java.math.BigDecimal;
import java.util.Map;
import org.apache.commons.collections.MapUtils;

public class CartTotalThresholdPotentialPromotionMessageActionSupplementStrategy implements ActionSupplementStrategy
{
    protected static final String CART_TOTAL_THRESHOLD_PARAMETER = "cart_total_threshold_parameter";
    protected static final String CART_TOTAL_THRESHOLD_PARAMETER_UUID = "cart_total_threshold_parameter_uuid";


    public boolean isActionProperToHandle(AbstractRuleActionRAO actionRao, RuleActionContext context)
    {
        return (MapUtils.isNotEmpty(context.getParameters()) && actionRao instanceof DisplayMessageRAO &&
                        isMessageForCartTotalThreshold(context.getParameters()));
    }


    public boolean shouldPerformAction(AbstractRuleActionRAO actionRao, RuleActionContext context)
    {
        Preconditions.checkArgument(isActionProperToHandle(actionRao, context), "The strategy is not proper to handle the action.");
        BigDecimal valueToDisplay = getValueToDisplay(context);
        if(valueToDisplay.compareTo(BigDecimal.ZERO) > 0)
        {
            DisplayMessageRAO displayMessageRAO = (DisplayMessageRAO)actionRao;
            displayMessageRAO.getParameters()
                            .put(context.getParameter("cart_total_threshold_parameter_uuid").toString(), valueToDisplay);
            return true;
        }
        return false;
    }


    protected BigDecimal getValueToDisplay(RuleActionContext context)
    {
        Map<String, BigDecimal> cartTotalThresholdParameters = (Map<String, BigDecimal>)context.getParameter("cart_total_threshold_parameter");
        CartRAO cartRao = context.getCartRao();
        BigDecimal cartSubTotal = cartRao.getSubTotal();
        BigDecimal cartTotalThresholdParameter = cartTotalThresholdParameters.get(cartRao.getCurrencyIsoCode());
        return cartTotalThresholdParameter.subtract(cartSubTotal);
    }


    protected boolean isMessageForCartTotalThreshold(Map<String, Object> parameters)
    {
        return (parameters.containsKey("cart_total_threshold_parameter") && parameters
                        .containsKey("cart_total_threshold_parameter_uuid") && parameters
                        .get("cart_total_threshold_parameter") != null);
    }
}
