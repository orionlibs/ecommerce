package de.hybris.platform.voucher;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.util.DiscountValue;
import de.hybris.platform.voucher.jalo.util.VoucherEntrySet;
import de.hybris.platform.voucher.jalo.util.VoucherValue;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;
import java.security.NoSuchAlgorithmException;
import java.util.List;

public interface VoucherModelService
{
    VoucherInvalidationModel createVoucherInvalidation(VoucherModel paramVoucherModel, String paramString, OrderModel paramOrderModel);


    boolean checkVoucherCode(VoucherModel paramVoucherModel, String paramString);


    VoucherEntrySet getApplicableEntries(VoucherModel paramVoucherModel, AbstractOrderModel paramAbstractOrderModel);


    VoucherValue getAppliedValue(VoucherModel paramVoucherModel, AbstractOrderModel paramAbstractOrderModel);


    DiscountValue getDiscountValue(VoucherModel paramVoucherModel, AbstractOrderModel paramAbstractOrderModel);


    List<RestrictionModel> getViolatedRestrictions(VoucherModel paramVoucherModel, AbstractOrderModel paramAbstractOrderModel);


    List<RestrictionModel> getViolatedRestrictions(VoucherModel paramVoucherModel, ProductModel paramProductModel);


    List<String> getViolationMessages(VoucherModel paramVoucherModel, AbstractOrderModel paramAbstractOrderModel);


    List<String> getViolationMessages(VoucherModel paramVoucherModel, ProductModel paramProductModel);


    VoucherValue getVoucherValue(VoucherModel paramVoucherModel, AbstractOrderModel paramAbstractOrderModel);


    boolean isApplicable(VoucherModel paramVoucherModel, AbstractOrderModel paramAbstractOrderModel);


    boolean isApplicable(VoucherModel paramVoucherModel, ProductModel paramProductModel);


    boolean isReservable(VoucherModel paramVoucherModel, String paramString, UserModel paramUserModel);


    boolean isReservable(VoucherModel paramVoucherModel, String paramString, AbstractOrderModel paramAbstractOrderModel);


    boolean redeem(VoucherModel paramVoucherModel, String paramString, CartModel paramCartModel) throws JaloPriceFactoryException;


    VoucherInvalidationModel redeem(VoucherModel paramVoucherModel, String paramString, OrderModel paramOrderModel);


    void release(VoucherModel paramVoucherModel, String paramString, OrderModel paramOrderModel) throws ConsistencyCheckException;


    void release(VoucherModel paramVoucherModel, String paramString, CartModel paramCartModel) throws JaloPriceFactoryException;


    VoucherInvalidationModel reserve(VoucherModel paramVoucherModel, String paramString, OrderModel paramOrderModel);


    String generateVoucherCode(VoucherModel paramVoucherModel) throws NoSuchAlgorithmException;
}
