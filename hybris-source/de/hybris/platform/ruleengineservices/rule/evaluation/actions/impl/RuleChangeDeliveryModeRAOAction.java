package de.hybris.platform.ruleengineservices.rule.evaluation.actions.impl;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DeliveryModeRAO;
import de.hybris.platform.ruleengineservices.rao.RuleEngineResultRAO;
import de.hybris.platform.ruleengineservices.rao.ShipmentRAO;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.ruleengineservices.rule.evaluation.actions.AbstractRuleExecutableSupport;
import java.util.Optional;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RuleChangeDeliveryModeRAOAction extends AbstractRuleExecutableSupport
{
    private static final Logger LOG = LoggerFactory.getLogger(RuleChangeDeliveryModeRAOAction.class);


    public boolean performActionInternal(RuleActionContext context)
    {
        String deliveryModeCode = (String)context.getParameter("delivery_mode", String.class);
        return performAction(context, deliveryModeCode);
    }


    protected boolean performAction(RuleActionContext context, String deliveryModeCode)
    {
        boolean isPerformed = false;
        CartRAO cartRao = context.getCartRao();
        RuleEngineResultRAO result = context.getRuleEngineResultRao();
        Optional<DeliveryModeRAO> deliveryMode = getRaoLookupService().lookupRAOByType(DeliveryModeRAO.class, context, new Predicate[] {getDeliveryModeRAOFilter(deliveryModeCode)});
        if(deliveryMode.isPresent())
        {
            isPerformed = true;
            changeDeliveryMode(cartRao, deliveryMode.get(), result, context);
        }
        else
        {
            LOG.error("no delivery mode found for code {} in rule {}, cannot apply rule action.", deliveryModeCode,
                            getRuleCode(context));
        }
        return isPerformed;
    }


    protected Predicate<DeliveryModeRAO> getDeliveryModeRAOFilter(String deliveryModeCode)
    {
        return o -> isFactDeliveryAndHasCode(o, deliveryModeCode);
    }


    public void changeDeliveryMode(CartRAO cartRao, DeliveryModeRAO mode, RuleEngineResultRAO result, RuleActionContext context)
    {
        validateRule(context);
        ShipmentRAO shipment = getRuleEngineCalculationService().changeDeliveryMode(cartRao, mode);
        result.getActions().add(shipment);
        setRAOMetaData(context, new AbstractRuleActionRAO[] {(AbstractRuleActionRAO)shipment});
        context.scheduleForUpdate(new Object[] {cartRao, result});
        context.insertFacts(new Object[] {shipment});
        cartRao.getEntries().forEach(e -> getConsumptionSupport().consumeOrderEntry(e, (AbstractRuleActionRAO)shipment));
    }


    protected boolean isFactDeliveryAndHasCode(Object fact, String deliveryModeCode)
    {
        return (fact instanceof DeliveryModeRAO && deliveryModeCode.equals(((DeliveryModeRAO)fact).getCode()));
    }
}
