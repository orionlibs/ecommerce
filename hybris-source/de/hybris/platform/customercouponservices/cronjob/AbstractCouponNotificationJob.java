package de.hybris.platform.customercouponservices.cronjob;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.enums.CronJobResult;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.customercouponservices.daos.CouponNotificationDao;
import de.hybris.platform.customercouponservices.daos.CustomerCouponDao;
import de.hybris.platform.customercouponservices.enums.CouponNotificationStatus;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.notificationservices.cronjob.AbstractNotificationJob;
import de.hybris.platform.notificationservices.enums.NotificationType;
import de.hybris.platform.notificationservices.enums.SiteMessageType;
import de.hybris.platform.notificationservices.model.SiteMessageModel;
import de.hybris.platform.servicelayer.cronjob.PerformResult;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.joda.time.ReadableInstant;

public abstract class AbstractCouponNotificationJob extends AbstractNotificationJob
{
    private static final String COUPON_EXPIRE_NOTIFICATION_DAYS = "coupon.expire.notification.days";
    private static final String COUPON_EFFECTIVE_NOTIFICAITON_DAYS = "coupon.effective.notification.days";
    private static final Integer ZERO = Integer.valueOf(0);
    private CustomerCouponDao customerCouponDao;
    private CouponNotificationDao couponNotificationDao;


    public PerformResult perform(CronJobModel job)
    {
        Integer configEffectiveDays = Integer.valueOf(Config.getInt("coupon.effective.notification.days", ZERO.intValue()));
        DateTime effectiveDay = (new DateTime()).plusDays(configEffectiveDays.intValue());
        Integer configExpireDays = Integer.valueOf(Config.getInt("coupon.expire.notification.days", ZERO.intValue()));
        DateTime expireDay = (new DateTime()).plusDays(configExpireDays.intValue());
        Map<CustomerCouponModel, SiteMessageModel> messages = new ConcurrentHashMap<>();
        getCouponNotificationDao().findAllCouponNotifications().forEach(notification -> {
            boolean isUnassignedCoupon = (getCustomerCouponDao().countAssignedCouponForCustomer(notification.getCustomerCoupon().getCouponId(), notification.getCustomer()) < 1);
            if((new DateTime(notification.getCustomerCoupon().getEndDate())).isBeforeNow() || isUnassignedCoupon)
            {
                this.modelService.remove(notification);
                return;
            }
            sendNotification(notification, effectiveDay, expireDay, messages);
        });
        return new PerformResult(CronJobResult.SUCCESS, CronJobStatus.FINISHED);
    }


    protected void sendNotification(CouponNotificationModel notification, DateTime effectiveDay, DateTime expireDay, Map<CustomerCouponModel, SiteMessageModel> messages)
    {
        CustomerCouponModel coupon = notification.getCustomerCoupon();
        if(notification.getStatus().equals(CouponNotificationStatus.INIT) && (new DateTime(coupon
                        .getStartDate())).isBefore((ReadableInstant)effectiveDay))
        {
            if(!messages.containsKey(coupon))
            {
                messages.put(coupon, createSiteMessage(notification, NotificationType.COUPON_EFFECTIVE));
            }
            sendCouponNotificaiton(notification, NotificationType.COUPON_EFFECTIVE, messages.get(coupon));
        }
        if((notification.getStatus().equals(CouponNotificationStatus.INIT) || notification
                        .getStatus().equals(CouponNotificationStatus.EFFECTIVESENT)) && (new DateTime(coupon
                        .getEndDate())).isBefore((ReadableInstant)expireDay))
        {
            if(!messages.containsKey(coupon))
            {
                messages.put(coupon, createSiteMessage(notification, NotificationType.COUPON_EXPIRE));
            }
            sendCouponNotificaiton(notification, NotificationType.COUPON_EXPIRE, messages.get(coupon));
        }
    }


    protected SiteMessageModel createSiteMessage(CouponNotificationModel notification, NotificationType notificationType)
    {
        CustomerCouponModel coupon = notification.getCustomerCoupon();
        String subject = Localization.getLocalizedMap("coupon.notification.sitemessage.subject").entrySet().stream().filter(map -> ((Language)map.getKey()).getIsocode().equals(notification.getLanguage().getIsocode())).map(map -> (String)map.getValue()).collect(Collectors.joining());
        String body = Localization.getLocalizedMap("coupon.notification.sitemessage." + notificationType.getCode() + ".body").entrySet().stream().filter(map -> ((Language)map.getKey()).getIsocode().equals(notification.getLanguage().getIsocode())).map(map -> (String)map.getValue())
                        .collect(Collectors.joining());
        body = MessageFormat.format(body, new Object[] {coupon.getCouponId()});
        return getSiteMessageService().createMessage(subject, body, SiteMessageType.SYSTEM, (ItemModel)coupon, notificationType);
    }


    protected void sendCouponNotificaiton(CouponNotificationModel couponNotification, NotificationType notificationType, SiteMessageModel message)
    {
        Map<String, ItemModel> data = new HashMap<>();
        data.put("language", couponNotification.getLanguage());
        data.put("Couponnotification", couponNotification);
        data.put("siteMessage", message);
        ItemModel notificationTypeItem = new ItemModel();
        notificationTypeItem.setProperty("notificationType", notificationType);
        data.put("notificationType", notificationTypeItem);
        getTaskExecutor().execute((Runnable)createTask(data));
        couponNotification.setStatus(CouponNotificationStatus.EFFECTIVESENT);
        if(NotificationType.COUPON_EXPIRE.equals(notificationType))
        {
            couponNotification.setStatus(CouponNotificationStatus.EXPIRESENT);
        }
        this.modelService.save(couponNotification);
    }


    protected CustomerCouponDao getCustomerCouponDao()
    {
        return this.customerCouponDao;
    }


    public void setCustomerCouponDao(CustomerCouponDao customerCouponDao)
    {
        this.customerCouponDao = customerCouponDao;
    }


    protected CouponNotificationDao getCouponNotificationDao()
    {
        return this.couponNotificationDao;
    }


    public void setCouponNotificationDao(CouponNotificationDao couponNotificationDao)
    {
        this.couponNotificationDao = couponNotificationDao;
    }


    protected abstract CouponNotificationTask createTask(Map<String, ItemModel> paramMap);
}
