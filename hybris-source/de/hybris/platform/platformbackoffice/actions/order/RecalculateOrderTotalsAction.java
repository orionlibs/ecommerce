package de.hybris.platform.platformbackoffice.actions.order;

import com.hybris.cockpitng.actions.ActionContext;
import com.hybris.cockpitng.actions.ActionResult;
import com.hybris.cockpitng.actions.CockpitAction;
import com.hybris.cockpitng.core.impl.DefaultWidgetModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.order.CalculationService;
import de.hybris.platform.order.exceptions.CalculationException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RecalculateOrderTotalsAction implements CockpitAction<OrderModel, Object>
{
    private static final Logger LOG = LoggerFactory.getLogger(RecalculateOrderTotalsAction.class);
    private static final String CURRENT_OBJECT = "currentObject";
    @Resource
    private ModelService modelService;
    @Resource
    private CalculationService calculationService;


    public ActionResult<Object> perform(ActionContext<OrderModel> ctx)
    {
        OrderModel order = (OrderModel)ctx.getData();
        if(LOG.isInfoEnabled())
        {
            LOG.info(String.format("Recalculating order totals %s from Backoffice!", new Object[] {order.toString()}));
        }
        if(isForcedToUseJalo())
        {
            Order jaloOrder = (Order)this.modelService.getSource(order);
            try
            {
                jaloOrder.calculateTotals(false);
            }
            catch(JaloPriceFactoryException e)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e.getMessage(), (Throwable)e);
                }
                return new ActionResult("success");
            }
        }
        try
        {
            this.calculationService.calculateTotals((AbstractOrderModel)order, true);
        }
        catch(CalculationException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage(), (Throwable)e);
            }
            return new ActionResult("success");
        }
        DefaultWidgetModel widget = (DefaultWidgetModel)ctx.getParameter("parentWidgetModel");
        widget.setValue("currentObject", order);
        return new ActionResult("success");
    }


    private boolean isForcedToUseJalo()
    {
        return Config.getBoolean("hmc.force.jalo.order.calculations", false);
    }


    public boolean canPerform(ActionContext<OrderModel> ctx)
    {
        return true;
    }


    public boolean needsConfirmation(ActionContext<OrderModel> ctx)
    {
        return true;
    }


    public String getConfirmationMessage(ActionContext<OrderModel> ctx)
    {
        return ctx.getLabel("perform.recalculate");
    }
}
