package de.hybris.platform.couponfacades.facades.impl;

import de.hybris.platform.commercefacades.coupon.CouponDataFacade;
import de.hybris.platform.commercefacades.coupon.data.CouponData;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCouponDataFacade implements CouponDataFacade
{
    private CouponService couponService;
    private Converter<AbstractCouponModel, CouponData> couponConverter;


    public Optional<CouponData> getCouponDetails(String couponCode)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("coupon code", couponCode);
        Objects.requireNonNull(getCouponConverter());
        return getCouponService().getCouponForCode(couponCode).map(getCouponConverter()::convert).map(couponData -> {
            couponData.setCouponCode(couponCode);
            return couponData;
        });
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


    protected Converter<AbstractCouponModel, CouponData> getCouponConverter()
    {
        return this.couponConverter;
    }


    @Required
    public void setCouponConverter(Converter<AbstractCouponModel, CouponData> couponConverter)
    {
        this.couponConverter = couponConverter;
    }
}
