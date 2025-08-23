package de.hybris.platform.customercouponservices.sms.process.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.notificationservices.enums.NotificationChannel;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.processor.Processor;
import de.hybris.platform.notificationservices.service.NotificationService;
import de.hybris.platform.notificationservices.strategies.SendSmsMessageStrategy;
import de.hybris.platform.util.localization.Localization;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class DefaultCouponNotificationSmsProcessor implements Processor
{
    private static final Logger LOG = Logger.getLogger(DefaultCouponNotificationSmsProcessor.class.getName());
    private SendSmsMessageStrategy sendSmsMessageStrategy;
    private NotificationService notificationService;


    public void process(CustomerModel customer, Map<String, ? extends ItemModel> dataMap)
    {
        CouponNotificationModel couponNotification = (CouponNotificationModel)dataMap.get("Couponnotification");
        NotificationType notificationType = (NotificationType)((ItemModel)dataMap.get("notificationType")).getProperty("notificationType");
        String message = Localization.getLocalizedString("coupon.notification.sms." + notificationType
                        .getCode() + ".content", new Object[] {couponNotification
                        .getCustomerCoupon().getCouponId()});
        String phoneNumber = getNotificationService().getChannelValue(NotificationChannel.SMS, customer);
        if(StringUtils.isEmpty(phoneNumber))
        {
            LOG.warn("No phone number found for customer, message[" + message + "] will not be sent.");
            return;
        }
        getSendSmsMessageStrategy().sendMessage(phoneNumber, message);
    }


    protected NotificationService getNotificationService()
    {
        return this.notificationService;
    }


    public void setNotificationService(NotificationService notificationService)
    {
        this.notificationService = notificationService;
    }


    protected SendSmsMessageStrategy getSendSmsMessageStrategy()
    {
        return this.sendSmsMessageStrategy;
    }


    public void setSendSmsMessageStrategy(SendSmsMessageStrategy sendSmsMessageStrategy)
    {
        this.sendSmsMessageStrategy = sendSmsMessageStrategy;
    }
}
