package de.hybris.platform.couponservices.order.eventlistener;

import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.couponservices.dao.CouponRedemptionDao;
import de.hybris.platform.couponservices.model.CouponRedemptionModel;
import de.hybris.platform.ordercancel.events.CancelFinishedEvent;
import de.hybris.platform.servicelayer.event.impl.AbstractEventListener;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.Collection;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CouponRedemptionOrderCancelFinishedEventListener extends AbstractEventListener<CancelFinishedEvent>
{
    private static final Logger LOG = LoggerFactory.getLogger(CouponRedemptionOrderCancelFinishedEventListener.class);
    private CouponRedemptionDao couponRedemptionDao;
    private ModelService modelService;


    protected void onEvent(CancelFinishedEvent cancelFinishedEvent)
    {
        OrderModel order = cancelFinishedEvent.getCancelRequestRecordEntry().getModificationRecord().getOrder();
        Collection<String> couponCodes = order.getAppliedCouponCodes();
        if(order.getStatus().equals(OrderStatus.CANCELLED) && CollectionUtils.isNotEmpty(couponCodes))
        {
            couponCodes.forEach(code -> {
                List<CouponRedemptionModel> redemptions = getCouponRedemptionDao().findCouponRedemptionsByCodeAndOrder(code, (AbstractOrderModel)order);
                if(!redemptions.isEmpty())
                {
                    redemptions.forEach(());
                    LOG.info("Coupon redemption of coupon code {} order {} has been deleted.", code, order.getCode());
                }
            });
        }
    }


    protected CouponRedemptionDao getCouponRedemptionDao()
    {
        return this.couponRedemptionDao;
    }


    public void setCouponRedemptionDao(CouponRedemptionDao couponRedemptionDao)
    {
        this.couponRedemptionDao = couponRedemptionDao;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
