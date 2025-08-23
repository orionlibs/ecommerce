package de.hybris.platform.voucher.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.VoucherModelService;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.jalo.util.VoucherValue;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DefaultVoucherModelService extends AbstractVoucherService implements VoucherModelService
{
    public boolean checkVoucherCode(VoucherModel voucher, String voucherCode)
    {
        return getVoucher(voucher).checkVoucherCode(voucherCode);
    }


    public VoucherInvalidationModel createVoucherInvalidation(VoucherModel voucher, String voucherCode, OrderModel order)
    {
        return (VoucherInvalidationModel)getModelService().get(getVoucher(voucher).createVoucherInvalidation(voucherCode, getOrder(order)));
    }


    public String generateVoucherCode(VoucherModel voucher) throws NoSuchAlgorithmException
    {
        return getVoucher(voucher).generateVoucherCode();
    }


    public VoucherEntrySet getApplicableEntries(VoucherModel voucher, AbstractOrderModel order)
    {
        return getVoucher(voucher).getApplicableEntries(getAbstractOrder(order));
    }


    public VoucherValue getAppliedValue(VoucherModel voucher, AbstractOrderModel order)
    {
        return getVoucher(voucher).getAppliedValue(getAbstractOrder(order));
    }


    public DiscountValue getDiscountValue(VoucherModel voucher, AbstractOrderModel order)
    {
        return getVoucher(voucher).getDiscountValue(getAbstractOrder(order));
    }


    public List<RestrictionModel> getViolatedRestrictions(VoucherModel voucher, AbstractOrderModel order)
    {
        return (List<RestrictionModel>)getModelService().getAll(getVoucher(voucher).getViolatedRestrictions(getAbstractOrder(order)), new ArrayList());
    }


    public List<RestrictionModel> getViolatedRestrictions(VoucherModel voucher, ProductModel product)
    {
        return (List<RestrictionModel>)getModelService().getAll(getVoucher(voucher).getViolatedRestrictions(getProduct(product)), new ArrayList());
    }


    public List<String> getViolationMessages(VoucherModel voucher, AbstractOrderModel order)
    {
        return getVoucher(voucher).getViolationMessages(getAbstractOrder(order));
    }


    public List<String> getViolationMessages(VoucherModel voucher, ProductModel product)
    {
        return getVoucher(voucher).getViolationMessages(getProduct(product));
    }


    public VoucherValue getVoucherValue(VoucherModel voucher, AbstractOrderModel order)
    {
        return getVoucher(voucher).getVoucherValue(getAbstractOrder(order));
    }


    public boolean isApplicable(VoucherModel voucher, AbstractOrderModel order)
    {
        return getVoucher(voucher).isApplicable(getAbstractOrder(order));
    }


    public boolean isApplicable(VoucherModel voucher, ProductModel product)
    {
        return getVoucher(voucher).isApplicable(getProduct(product));
    }


    public boolean isReservable(VoucherModel voucher, String voucherCode, UserModel user)
    {
        return getVoucher(voucher).isReservable(voucherCode, getUser(user));
    }


    public boolean isReservable(VoucherModel voucher, String voucherCode, AbstractOrderModel order)
    {
        return getVoucher(voucher).isReservable(voucherCode, getAbstractOrder(order));
    }


    public boolean redeem(VoucherModel voucher, String voucherCode, CartModel cart) throws JaloPriceFactoryException
    {
        return getVoucher(voucher).redeem(voucherCode, getCart(cart));
    }


    public VoucherInvalidationModel redeem(VoucherModel voucher, String voucherCode, OrderModel order)
    {
        return (VoucherInvalidationModel)getModelService().get(getVoucher(voucher).redeem(voucherCode, getOrder(order)));
    }


    public void release(VoucherModel voucher, String voucherCode, OrderModel order) throws ConsistencyCheckException
    {
        getVoucher(voucher).release(voucherCode, getOrder(order));
    }


    public void release(VoucherModel voucher, String voucherCode, CartModel cart) throws JaloPriceFactoryException
    {
        getVoucher(voucher).release(voucherCode, getCart(cart));
    }


    public VoucherInvalidationModel reserve(VoucherModel voucher, String voucherCode, OrderModel order)
    {
        return (VoucherInvalidationModel)getModelService().get(getVoucher(voucher).reserve(voucherCode, getOrder(order)));
    }
}
