package de.hybris.platform.voucher;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.SerialVoucherModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;
import java.util.Collection;

public interface VoucherService
{
    void afterOrderCreation(OrderModel paramOrderModel, CartModel paramCartModel);


    Collection<VoucherModel> getAllVouchers();


    Collection<String> getAppliedVoucherCodes(CartModel paramCartModel);


    Collection<String> getAppliedVoucherCodes(OrderModel paramOrderModel);


    Collection<DiscountModel> getAppliedVouchers(AbstractOrderModel paramAbstractOrderModel);


    VoucherModel getVoucher(String paramString);


    Collection<PromotionVoucherModel> getPromotionVouchers(String paramString);


    Collection<SerialVoucherModel> getSerialVouchers(String paramString);


    boolean redeemVoucher(String paramString, CartModel paramCartModel) throws JaloPriceFactoryException;


    VoucherInvalidationModel redeemVoucher(String paramString, OrderModel paramOrderModel);


    boolean createVoucherInvalidation(String paramString, OrderModel paramOrderModel);


    void releaseVoucher(String paramString, CartModel paramCartModel) throws JaloPriceFactoryException;


    void releaseVoucher(String paramString, OrderModel paramOrderModel) throws ConsistencyCheckException;


    VoucherInvalidationModel reserveVoucher(String paramString, OrderModel paramOrderModel);


    void save(VoucherModel paramVoucherModel);


    void delete(VoucherModel paramVoucherModel);
}
