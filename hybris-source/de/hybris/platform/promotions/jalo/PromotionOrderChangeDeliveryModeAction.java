package de.hybris.platform.promotions.jalo;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.delivery.DeliveryMode;
import de.hybris.platform.jalo.order.delivery.JaloDeliveryModeException;
import de.hybris.platform.util.Config;
import org.apache.log4j.Logger;

public class PromotionOrderChangeDeliveryModeAction extends GeneratedPromotionOrderChangeDeliveryModeAction
{
    private static final Logger LOG = Logger.getLogger(PromotionOrderChangeDeliveryModeAction.class);


    public boolean apply(SessionContext ctx)
    {
        boolean calculate = false;
        if(!isMarkedAppliedAsPrimitive(ctx))
        {
            AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
            if(order != null)
            {
                DeliveryMode orderDeliveryMode = order.getDeliveryMode(ctx);
                PromotionsManager pm = PromotionsManager.getInstance();
                DeliveryMode newDeliveryMode = getDeliveryMode(ctx);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("(" + getPK() + ") apply: Applying ChangeDeliveryMode action. orderDeliveryMode=[" +
                                    deliveryModeToString(ctx, orderDeliveryMode) + "] newDeliveryMode=[" +
                                    deliveryModeToString(ctx, newDeliveryMode) + "]");
                }
                pm.setPreviousDeliveryMode(ctx, order, orderDeliveryMode);
                order.setDeliveryMode(newDeliveryMode);
                updateDeliveryCost(order, newDeliveryMode);
                calculate = true;
                setMarkedApplied(ctx, true);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("(" + getPK() + ") apply: After apply orderDeliveryMode=[" +
                                    deliveryModeToString(ctx, order.getDeliveryMode(ctx)) + "]");
                }
            }
        }
        return calculate;
    }


    public boolean undo(SessionContext ctx)
    {
        boolean calculate = false;
        if(isMarkedAppliedAsPrimitive(ctx))
        {
            AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
            if(order != null)
            {
                DeliveryMode orderDeliveryMode = order.getDeliveryMode(ctx);
                PromotionsManager pm = PromotionsManager.getInstance();
                DeliveryMode previousDeliveryMode = pm.getPreviousDeliveryMode(ctx, order);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("(" + getPK() + ") undo: Undoing ChangeDeliveryMode action. orderDeliveryMode=[" +
                                    deliveryModeToString(ctx, orderDeliveryMode) + "] previousDeliveryMode=[" +
                                    deliveryModeToString(ctx, previousDeliveryMode) + "]");
                }
                order.setDeliveryMode(ctx, previousDeliveryMode);
                pm.setPreviousDeliveryMode(ctx, order, null);
                updateDeliveryCost(order, previousDeliveryMode);
                calculate = true;
                setMarkedApplied(ctx, false);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("(" + getPK() + ") undo: After undo orderDeliveryMode=[" +
                                    deliveryModeToString(ctx, order.getDeliveryMode(ctx)) + "]");
                }
            }
        }
        return calculate;
    }


    public double getValue(SessionContext ctx)
    {
        return 0.0D;
    }


    public boolean isAppliedToOrder(SessionContext ctx)
    {
        AbstractOrder order = getPromotionResult(ctx).getOrder(ctx);
        if(order != null)
        {
            DeliveryMode orderDeliveryMode = order.getDeliveryMode(ctx);
            DeliveryMode newDeliveryMode = getDeliveryMode(ctx);
            if(orderDeliveryMode != null && orderDeliveryMode.equals(newDeliveryMode))
            {
                return true;
            }
        }
        return false;
    }


    protected String deliveryModeToString(SessionContext ctx, DeliveryMode deliveryMode)
    {
        if(deliveryMode == null)
        {
            return "(null)";
        }
        return deliveryMode.getClass().getSimpleName() + " '" + deliveryMode.getClass().getSimpleName() + "' (" + deliveryMode.getCode(ctx) + ")";
    }


    protected void updateDeliveryCost(AbstractOrder order, DeliveryMode deliveryMode)
    {
        if(Boolean.parseBoolean(Config.getParameter("orderThresholdChangeDeliveryMode.updateDeliveryCost")))
        {
            try
            {
                double deliveryCost = deliveryMode.getCost(order).getValue();
                order.setDeliveryCosts(deliveryCost);
            }
            catch(JaloDeliveryModeException e)
            {
                LOG.error("Delivery mode error for mode " + deliveryMode.getCode(), (Throwable)e);
            }
        }
    }
}
