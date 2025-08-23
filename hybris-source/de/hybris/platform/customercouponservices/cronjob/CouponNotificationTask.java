package de.hybris.platform.customercouponservices.cronjob;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.service.NotificationService;
import java.util.Map;

public class CouponNotificationTask implements Runnable
{
    private final NotificationService notificationService;
    private final Map<String, ? extends ItemModel> data;
    private final CouponNotificationModel couponNotification;


    public CouponNotificationTask(NotificationService notificationService, Map<String, ? extends ItemModel> data)
    {
        this.notificationService = notificationService;
        this.data = data;
        this.couponNotification = (CouponNotificationModel)data.get("Couponnotification");
    }


    public void run()
    {
        ItemModel notifycationType = this.data.get("notificationType");
        this.notificationService.notifyCustomer((NotificationType)notifycationType.getProperty("notificationType"), this.couponNotification
                        .getCustomer(), this.data);
    }
}
