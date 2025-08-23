package de.hybris.platform.customercouponservices.cronjob;

import de.hybris.platform.core.model.ItemModel;
import java.util.Map;

public class CouponNotificationJob extends AbstractCouponNotificationJob
{
    protected CouponNotificationTask createTask(Map<String, ItemModel> data)
    {
        return new CouponNotificationTask(getNotificationService(), data);
    }
}
