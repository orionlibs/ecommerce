package de.hybris.platform.orderscheduling.impl;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.order.InvalidCartException;
import de.hybris.platform.orderscheduling.OrderUtility;
import de.hybris.platform.orderscheduling.ScheduleOrderService;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import org.apache.log4j.Logger;

public class CartToOrderJob extends AbstractJobPerformable<CartToOrderCronJobModel>
{
    private OrderUtility orderUtility;


    public PerformResult perform(CartToOrderCronJobModel cronJob)
    {
        try
        {
            this.orderUtility.createOrderFromCart(cronJob.getCart(), cronJob.getDeliveryAddress(), cronJob.getPaymentAddress(), cronJob
                            .getPaymentInfo());
        }
        catch(InvalidCartException e)
        {
            Logger.getLogger(ScheduleOrderService.class).error(e.getMessage());
            Logger.getLogger(ScheduleOrderService.class).error(e.getStackTrace());
            if(Logger.getLogger(ScheduleOrderService.class).isDebugEnabled())
            {
                Logger.getLogger(ScheduleOrderService.class).debug(e);
            }
            return new PerformResult(CronJobResult.FAILURE, CronJobStatus.FINISHED);
        }
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    public void setOrderUtility(OrderUtility orderUtility)
    {
        this.orderUtility = orderUtility;
    }
}
