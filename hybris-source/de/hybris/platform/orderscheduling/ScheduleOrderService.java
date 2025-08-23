package de.hybris.platform.orderscheduling;

import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.cronjob.model.TriggerModel;
import de.hybris.platform.orderscheduling.model.CartToOrderCronJobModel;
import de.hybris.platform.orderscheduling.model.OrderScheduleCronJobModel;
import de.hybris.platform.orderscheduling.model.OrderTemplateToOrderCronJobModel;
import java.util.List;

public interface ScheduleOrderService
{
    OrderTemplateToOrderCronJobModel createOrderFromOrderTemplateCronJob(OrderModel paramOrderModel, List<TriggerModel> paramList);


    CartToOrderCronJobModel createOrderFromCartCronJob(CartModel paramCartModel, AddressModel paramAddressModel1, AddressModel paramAddressModel2, PaymentInfoModel paramPaymentInfoModel, List<TriggerModel> paramList);


    OrderScheduleCronJobModel createScheduledOrderCronJob(OrderModel paramOrderModel, List<TriggerModel> paramList);
}
