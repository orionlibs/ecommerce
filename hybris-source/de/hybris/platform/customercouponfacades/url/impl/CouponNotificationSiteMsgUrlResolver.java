package de.hybris.platform.customercouponfacades.url.impl;

import de.hybris.platform.customercouponservices.model.CustomerCouponModel;
import de.hybris.platform.notificationfacades.url.SiteMessageUrlResolver;

public class CouponNotificationSiteMsgUrlResolver extends SiteMessageUrlResolver<CustomerCouponModel>
{
    public String resolve(CustomerCouponModel source)
    {
        return getDefaultUrl();
    }
}
