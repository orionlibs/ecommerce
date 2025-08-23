package de.hybris.platform.customercouponservices.retention.hook;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.customercouponservices.CustomerCouponService;
import de.hybris.platform.customercouponservices.model.CouponNotificationModel;
import de.hybris.platform.retention.hook.ItemCleanupHook;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;

public class CouponNotificationCleanupHook implements ItemCleanupHook<CustomerModel>
{
    private ModelService modelService;
    private CustomerCouponService customerCouponService;


    public void cleanupRelatedObjects(CustomerModel customer)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("customer", customer);
        List<CouponNotificationModel> couponNotifications = this.customerCouponService.getCouponNotificationsForCustomer(customer);
        if(CollectionUtils.isNotEmpty(couponNotifications))
        {
            getModelService().removeAll(couponNotifications);
        }
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected CustomerCouponService getCustomerCouponService()
    {
        return this.customerCouponService;
    }


    public void setCustomerCouponService(CustomerCouponService customerCouponService)
    {
        this.customerCouponService = customerCouponService;
    }
}
