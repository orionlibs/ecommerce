package de.hybris.platform.couponfacades.facades.impl;

import de.hybris.platform.commercefacades.voucher.VoucherFacade;
import de.hybris.platform.commercefacades.voucher.data.VoucherData;
import de.hybris.platform.commercefacades.voucher.exceptions.VoucherOperationException;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.couponfacades.CouponFacadeIllegalStateException;
import de.hybris.platform.couponservices.model.AbstractCouponModel;
import de.hybris.platform.couponservices.service.data.CouponResponse;
import de.hybris.platform.couponservices.services.CouponService;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCouponFacade implements VoucherFacade
{
    public static final String COUPON_CODE = "coupon code";
    private CouponService couponService;
    private CartService cartService;
    private AbstractPopulatingConverter<String, VoucherData> couponCodeModelConverter;
    private static final String CARTNOTFOUND = "No cart was found in session";
    private Converter<AbstractCouponModel, VoucherData> couponModelConverter;


    public boolean checkVoucherCode(String voucherCode)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("coupon code", voucherCode);
        CouponResponse couponResponse = applyIfCartExists(c -> getCouponService().verifyCouponCode(voucherCode, c));
        return BooleanUtils.isTrue(couponResponse.getSuccess());
    }


    public VoucherData getVoucher(String voucherCode) throws VoucherOperationException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("voucher code", voucherCode);
        Objects.requireNonNull(getCouponModelConverter());
        return (VoucherData)getCouponService().getCouponForCode(voucherCode).map(getCouponModelConverter()::convert).map(coupon -> {
            coupon.setVoucherCode(voucherCode);
            return coupon;
        }).orElseThrow(() -> new VoucherOperationException("cannot create voucher data for given code:" + voucherCode));
    }


    public void applyVoucher(String voucherCode) throws VoucherOperationException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("coupon code", voucherCode);
        Objects.requireNonNull(getCouponService());
        CouponResponse couponResponse = applyIfCartExists(voucherCode, getCouponService()::redeemCoupon);
        if(BooleanUtils.isNotTrue(couponResponse.getSuccess()))
        {
            throw new VoucherOperationException(couponResponse.getMessage());
        }
    }


    public void releaseVoucher(String voucherCode) throws VoucherOperationException
    {
        ServicesUtil.validateParameterNotNullStandardMessage("coupon code", voucherCode);
        Objects.requireNonNull(getCouponService());
        acceptIfCartExists(voucherCode, getCouponService()::releaseCouponCode);
    }


    public List<VoucherData> getVouchersForCart()
    {
        return applyIfCartExists(this::getCouponsForOrder);
    }


    protected List<VoucherData> getCouponsForOrder(AbstractOrderModel order)
    {
        Collection<String> couponCodesForOrder = order.getAppliedCouponCodes();
        if(CollectionUtils.isNotEmpty(couponCodesForOrder))
        {
            Objects.requireNonNull(getCouponCodeModelConverter());
            return (List<VoucherData>)couponCodesForOrder.stream().map(getCouponCodeModelConverter()::convert).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    protected void acceptIfCartExists(String code, BiConsumer<String, AbstractOrderModel> orderConsumer) throws VoucherOperationException
    {
        CartModel cart = getCartService().getSessionCart();
        if(Objects.nonNull(cart))
        {
            orderConsumer.accept(code, cart);
        }
        else
        {
            throw new VoucherOperationException("No cart was found in session");
        }
    }


    protected <R> R applyIfCartExists(String code, BiFunction<String, CartModel, R> orderConsumer) throws VoucherOperationException
    {
        CartModel cart = getCartService().getSessionCart();
        if(Objects.nonNull(cart))
        {
            return orderConsumer.apply(code, cart);
        }
        throw new VoucherOperationException("No cart was found in session");
    }


    protected <R> R applyIfCartExists(Function<AbstractOrderModel, R> orderFunction)
    {
        CartModel cart = getCartService().getSessionCart();
        if(Objects.nonNull(cart))
        {
            return orderFunction.apply(cart);
        }
        throw new CouponFacadeIllegalStateException("No cart was found in session");
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


    protected CartService getCartService()
    {
        return this.cartService;
    }


    @Required
    public void setCartService(CartService cartService)
    {
        this.cartService = cartService;
    }


    protected AbstractPopulatingConverter<String, VoucherData> getCouponCodeModelConverter()
    {
        return this.couponCodeModelConverter;
    }


    @Required
    public void setCouponCodeModelConverter(AbstractPopulatingConverter<String, VoucherData> couponCodeModelConverter)
    {
        this.couponCodeModelConverter = couponCodeModelConverter;
    }


    protected Converter<AbstractCouponModel, VoucherData> getCouponModelConverter()
    {
        return this.couponModelConverter;
    }


    @Required
    public void setCouponModelConverter(Converter<AbstractCouponModel, VoucherData> couponModelConverter)
    {
        this.couponModelConverter = couponModelConverter;
    }
}
