package de.hybris.platform.couponservices.converters.populator;

import com.google.common.base.Preconditions;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.rao.CouponRAO;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class CouponRaoPopulator implements Populator<AbstractOrderModel, CartRAO>
{
    private CouponService couponService;


    public void populate(AbstractOrderModel cartModel, CartRAO cartRao)
    {
        Preconditions.checkNotNull(cartModel, "Cart model is not expected to be NULL here");
        Preconditions.checkNotNull(cartRao, "Cart RAO is not expected to be NULL here");
        Collection<String> appliedCouponCodes = cartModel.getAppliedCouponCodes();
        if(CollectionUtils.isNotEmpty(appliedCouponCodes))
        {
            cartRao.setCoupons((List)appliedCouponCodes.stream().map(this::getCouponRAO).filter(Objects::nonNull).collect(Collectors.toList()));
        }
    }


    protected CouponRAO getCouponRAO(String couponCode)
    {
        return getCouponService().getValidatedCouponForCode(couponCode).map(this::toCouponRAO).map(couponRao -> {
            couponRao.setCouponCode(couponCode);
            return couponRao;
        }).orElse(null);
    }


    protected CouponRAO toCouponRAO(AbstractCouponModel couponModel)
    {
        CouponRAO couponRao = new CouponRAO();
        couponRao.setCouponId(couponModel.getCouponId());
        return couponRao;
    }


    protected CouponService getCouponService()
    {
        return this.couponService;
    }


    @Required
    public void setCouponService(CouponService couponService)
    {
        this.couponService = couponService;
    }
}
