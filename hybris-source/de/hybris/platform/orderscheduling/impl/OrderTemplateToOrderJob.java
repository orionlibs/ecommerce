package de.hybris.platform.orderscheduling.impl;

import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.orderscheduling.OrderUtility;
import de.hybris.platform.orderscheduling.model.OrderTemplateToOrderCronJobModel;
import de.hybris.platform.servicelayer.cronjob.AbstractJobPerformable;
import de.hybris.platform.servicelayer.cronjob.PerformResult;

public class OrderTemplateToOrderJob extends AbstractJobPerformable<OrderTemplateToOrderCronJobModel>
{
    private OrderUtility orderUtility;


    public void setOrderUtility(OrderUtility orderUtility)
    {
        this.orderUtility = orderUtility;
    }


    public PerformResult perform(OrderTemplateToOrderCronJobModel cronJob)
    {
        this.orderUtility.createOrderFromOrderTemplate(cronJob.getOrderTemplate());
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }
}
