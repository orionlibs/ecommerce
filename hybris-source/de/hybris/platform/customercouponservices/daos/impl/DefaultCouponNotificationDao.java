package de.hybris.platform.customercouponservices.daos.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponservices.daos.CouponNotificationDao;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class DefaultCouponNotificationDao extends DefaultGenericDao<CouponNotificationModel> implements CouponNotificationDao
{
    private static final String FIND_COUPON_NOTIFICATION_FOR_CODE = "SELECT {pk} FROM {CouponNotification as r},{CustomerCoupon as s}  WHERE {r.customerCoupon} = {s.pk} AND {s.couponid} =?couponCode";
    private static final String FIND_COUPON_NOTIFICATION_FOR_CUSTOMER = "SELECT {pk} FROM {CouponNotification as r} WHERE {r:customer} =?customer";


    public DefaultCouponNotificationDao()
    {
        super("CouponNotification");
    }


    public List<CouponNotificationModel> findCouponNotificationByCouponCode(String couponCode)
    {
        ServicesUtil.validateParameterNotNull(couponCode, "Coupon code must not be null");
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {CouponNotification as r},{CustomerCoupon as s}  WHERE {r.customerCoupon} = {s.pk} AND {s.couponid} =?couponCode");
        query.addQueryParameter("couponCode", couponCode);
        return getFlexibleSearchService().search(query).getResult();
    }


    public List<CouponNotificationModel> findCouponNotificationsForCustomer(CustomerModel customer)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {pk} FROM {CouponNotification as r} WHERE {r:customer} =?customer");
        query.addQueryParameter("customer", customer);
        List<CouponNotificationModel> results = getFlexibleSearchService().search(query).getResult();
        if(CollectionUtils.isNotEmpty(results))
        {
            return results;
        }
        return Collections.emptyList();
    }
}
