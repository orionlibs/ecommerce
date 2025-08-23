package de.hybris.platform.orderscheduling.impl;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.orderscheduling.OrderUtility;
import de.hybris.platform.orderscheduling.model.OrderScheduleCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class OrderScheduleJob extends AbstractJobPerformable<OrderScheduleCronJobModel>
{
    private OrderUtility orderUtility;


    public PerformResult perform(OrderScheduleCronJobModel cronJob)
    {
        this.orderUtility.runScheduledOrder(cronJob.getOrder());
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    public void setOrderUtility(OrderUtility orderUtility)
    {
        this.orderUtility = orderUtility;
    }
}
