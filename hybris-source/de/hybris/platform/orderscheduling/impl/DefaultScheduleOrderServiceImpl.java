package de.hybris.platform.orderscheduling.impl;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.orderscheduling.ScheduleOrderService;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.orderscheduling.model.OrderScheduleCronJobModel;
import de.hybris.platform.orderscheduling.model.OrderTemplateToOrderCronJobModel;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import javax.annotation.Resource;

public class DefaultScheduleOrderServiceImpl implements ScheduleOrderService
{
    @Resource
    private ModelService modelService;
    @Resource
    private CronJobService cronJobService;


    public CartToOrderCronJobModel createOrderFromCartCronJob(CartModel cart, AddressModel deliveryAddress, AddressModel paymentAddress, PaymentInfoModel paymentInfo, List<TriggerModel> triggers)
    {
        CartToOrderCronJobModel cartToOrderCronJob = (CartToOrderCronJobModel)this.modelService.create(CartToOrderCronJobModel.class);
        cartToOrderCronJob.setCart(cart);
        cartToOrderCronJob.setDeliveryAddress(deliveryAddress);
        cartToOrderCronJob.setPaymentAddress(paymentAddress);
        cartToOrderCronJob.setPaymentInfo(paymentInfo);
        setCronJobToTrigger((CronJobModel)cartToOrderCronJob, triggers);
        cartToOrderCronJob.setJob(this.cronJobService.getJob("cartToOrderJob"));
        this.modelService.save(cartToOrderCronJob);
        return cartToOrderCronJob;
    }


    public OrderTemplateToOrderCronJobModel createOrderFromOrderTemplateCronJob(OrderModel template, List<TriggerModel> triggers)
    {
        OrderTemplateToOrderCronJobModel orderTemplateCronJob = (OrderTemplateToOrderCronJobModel)this.modelService.create(OrderTemplateToOrderCronJobModel.class);
        orderTemplateCronJob.setOrderTemplate(template);
        setCronJobToTrigger((CronJobModel)orderTemplateCronJob, triggers);
        orderTemplateCronJob.setJob(this.cronJobService.getJob("orderTemplateToOrderJob"));
        this.modelService.save(orderTemplateCronJob);
        return orderTemplateCronJob;
    }


    public OrderScheduleCronJobModel createScheduledOrderCronJob(OrderModel order, List<TriggerModel> triggers)
    {
        OrderScheduleCronJobModel orderScheduleJob = (OrderScheduleCronJobModel)this.modelService.create(OrderScheduleCronJobModel.class);
        orderScheduleJob.setOrder(order);
        setCronJobToTrigger((CronJobModel)orderScheduleJob, triggers);
        orderScheduleJob.setJob(this.cronJobService.getJob("orderScheduleJob"));
        this.modelService.save(orderScheduleJob);
        return orderScheduleJob;
    }


    protected void setCronJobToTrigger(CronJobModel cronJob, List<TriggerModel> triggers)
    {
        for(TriggerModel trigger : triggers)
        {
            trigger.setCronJob(cronJob);
        }
        cronJob.setTriggers(triggers);
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setCronJobService(CronJobService cronJobService)
    {
        this.cronJobService = cronJobService;
    }


    public CronJobService getCronJobService()
    {
        return this.cronJobService;
    }
}
