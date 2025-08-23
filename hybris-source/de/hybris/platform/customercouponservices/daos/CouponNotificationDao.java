package de.hybris.platform.customercouponservices.daos;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.servicelayer.internal.dao.GenericDao;
import java.util.List;

public interface CouponNotificationDao extends GenericDao<CouponNotificationModel>
{
    default List<CouponNotificationModel> findAllCouponNotifications()
    {
        return find();
    }


    List<CouponNotificationModel> findCouponNotificationByCouponCode(String paramString);


    List<CouponNotificationModel> findCouponNotificationsForCustomer(CustomerModel paramCustomerModel);
}
