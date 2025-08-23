package de.hybris.platform.promotionengineservices.action.strategies;

import com.google.common.base.Preconditions;
import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DisplayMessageRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.ActionSupplementStrategy;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.RAOConsumptionSupport;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Required;

public class QualifyingProductPotentialPromotionMessageActionSupplementStrategy implements ActionSupplementStrategy
{
    protected static final String PRODUCTS_QUANTITY_PARAMETER = "qualifying_products_quantity";
    protected static final String PRODUCTS_QUANTITY_PARAMETER_UUID = "qualifying_products_quantity_uuid";
    protected static final String PRODUCTS_PARAMETER = "qualifying_products";
    protected static final String PRODUCTS_PARAMETER_UUID = "qualifying_products_uuid";
    private RAOConsumptionSupport consumptionSupport;


    public boolean isActionProperToHandle(AbstractRuleActionRAO actionRao, RuleActionContext context)
    {
        return (MapUtils.isNotEmpty(context.getParameters()) && actionRao instanceof DisplayMessageRAO &&
                        isMessageForQualifiedProduct(context.getParameters()));
    }


    public boolean shouldPerformAction(AbstractRuleActionRAO actionRao, RuleActionContext context)
    {
        Preconditions.checkArgument(isActionProperToHandle(actionRao, context), "The strategy is not proper to handle the action.");
        int itemQuantityToDisplay = getValueToDisplay(context);
        if(itemQuantityToDisplay > 0)
        {
            DisplayMessageRAO displayMessageRAO = (DisplayMessageRAO)actionRao;
            displayMessageRAO.getParameters()
                            .put(context.getParameter("qualifying_products_quantity_uuid").toString(), Integer.valueOf(itemQuantityToDisplay));
            return true;
        }
        return false;
    }


    protected int getValueToDisplay(RuleActionContext context)
    {
        Integer targetItemQuantity = (Integer)context.getParameter("qualifying_products_quantity");
        List<String> conditionProduct = (List<String>)context.getParameter("qualifying_products");
        CartRAO cartRao = context.getCartRao();
        int actualItemQuantity = cartRao.getEntries().stream().filter(e -> conditionProduct.contains(e.getProductCode())).mapToInt(e -> e.getQuantity() - getConsumptionSupport().getConsumedQuantityForOrderEntry(e)).sum();
        return targetItemQuantity.intValue() - actualItemQuantity;
    }


    protected boolean isMessageForQualifiedProduct(Map<String, Object> parameters)
    {
        return (hasNotNullParameter(parameters, "qualifying_products_quantity") && parameters
                        .containsKey("qualifying_products_quantity_uuid") && hasNotNullParameter(parameters, "qualifying_products") && parameters
                        .containsKey("qualifying_products_uuid"));
    }


    protected boolean hasNotNullParameter(Map<String, Object> parameters, String paramName)
    {
        return (parameters.containsKey(paramName) && parameters.get(paramName) != null);
    }


    protected RAOConsumptionSupport getConsumptionSupport()
    {
        return this.consumptionSupport;
    }


    @Required
    public void setConsumptionSupport(RAOConsumptionSupport consumptionSupport)
    {
        this.consumptionSupport = consumptionSupport;
    }
}
