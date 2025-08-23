package de.hybris.platform.customercouponservices.strategies;

import de.hybris.platform.acceleratorservices.process.strategies.impl.AbstractProcessContextStrategy;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponservices.model.CouponNotificationProcessModel;
import de.hybris.platform.processengine.model.BusinessProcessModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Optional;

public class CouponNotificationProcessContextStrategy extends AbstractProcessContextStrategy
{
    public BaseSiteModel getCmsSite(BusinessProcessModel businessProcessModel)
    {
        ServicesUtil.validateParameterNotNull(businessProcessModel, "businessProcess must not be null");
        return Optional.<BusinessProcessModel>of(businessProcessModel)
                        .filter(businessProcess -> businessProcess instanceof CouponNotificationProcessModel)
                        .map(businessProcess -> ((CouponNotificationProcessModel)businessProcess).getCouponNotification().getBaseSite())
                        .orElse(null);
    }


    protected CustomerModel getCustomer(BusinessProcessModel businessProcess)
    {
        return Optional.<BusinessProcessModel>of(businessProcess).filter(bp -> bp instanceof CouponNotificationProcessModel)
                        .map(bp -> ((CouponNotificationProcessModel)businessProcess).getCouponNotification().getCustomer()).orElse(null);
    }


    protected LanguageModel computeLanguage(BusinessProcessModel businessProcess)
    {
        return Optional.<BusinessProcessModel>of(businessProcess).filter(bp -> bp instanceof CouponNotificationProcessModel)
                        .map(bp -> ((CouponNotificationProcessModel)businessProcess).getCouponNotification().getLanguage())
                        .orElse(super.computeLanguage(businessProcess));
    }
}
