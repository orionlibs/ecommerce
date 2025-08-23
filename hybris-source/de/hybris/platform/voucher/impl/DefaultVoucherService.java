package de.hybris.platform.voucher.impl;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.order.price.JaloPriceFactoryException;
import de.hybris.platform.voucher.VoucherService;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.jalo.VoucherInvalidation;
import de.hybris.platform.voucher.jalo.VoucherManager;
import de.hybris.platform.voucher.model.PromotionVoucherModel;
import de.hybris.platform.voucher.model.SerialVoucherModel;
import de.hybris.platform.voucher.model.VoucherInvalidationModel;
import de.hybris.platform.voucher.model.VoucherModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class DefaultVoucherService extends AbstractVoucherService implements VoucherService
{
    public void afterOrderCreation(OrderModel order, CartModel cart)
    {
        VoucherManager.getInstance().afterOrderCreation(getOrder(order), getCart(cart));
    }


    public boolean createVoucherInvalidation(String voucherCode, OrderModel order)
    {
        Voucher voucher = VoucherManager.getInstance().getVoucher(voucherCode);
        Order jaloOrder = (Order)getModelService().getSource(order);
        VoucherInvalidation vi = voucher.createVoucherInvalidation(voucherCode, jaloOrder);
        if(vi != null)
        {
            vi.setStatus("confirmed");
            return true;
        }
        return false;
    }


    public Collection<VoucherModel> getAllVouchers()
    {
        return getModelService().getAll(VoucherManager.getInstance().getAllVouchers(), new ArrayList());
    }


    public Collection<String> getAppliedVoucherCodes(CartModel cart)
    {
        return VoucherManager.getInstance().getAppliedVoucherCodes(getCart(cart));
    }


    public Collection<String> getAppliedVoucherCodes(OrderModel order)
    {
        return VoucherManager.getInstance().getAppliedVoucherCodes(getOrder(order));
    }


    public Collection<DiscountModel> getAppliedVouchers(AbstractOrderModel order)
    {
        return getModelService().getAll(VoucherManager.getInstance().getAppliedVouchers(getAbstractOrder(order)), new ArrayList());
    }


    public Collection<PromotionVoucherModel> getPromotionVouchers(String voucherCode)
    {
        return getModelService().getAll(VoucherManager.getInstance().getPromotionVouchers(voucherCode), new ArrayList());
    }


    public Collection<SerialVoucherModel> getSerialVouchers(String voucherCode)
    {
        return getModelService().getAll(VoucherManager.getInstance().getSerialVouchers(voucherCode), new ArrayList());
    }


    public VoucherModel getVoucher(String voucherCode)
    {
        Voucher voucher = VoucherManager.getInstance().getVoucher(voucherCode);
        if(voucher == null)
        {
            return null;
        }
        return (VoucherModel)getModelService().get(voucher);
    }


    public boolean redeemVoucher(String voucherCode, CartModel cart) throws JaloPriceFactoryException
    {
        saveIfModified((AbstractOrderModel)cart);
        boolean changed = VoucherManager.getInstance().redeemVoucher(voucherCode, getCart(cart));
        if(changed)
        {
            refresh((AbstractOrderModel)cart);
        }
        return changed;
    }


    public VoucherInvalidationModel redeemVoucher(String voucherCode, OrderModel order)
    {
        saveIfModified((AbstractOrderModel)order);
        VoucherInvalidation voucherInvalidation = VoucherManager.getInstance().redeemVoucher(voucherCode, getOrder(order));
        if(voucherInvalidation == null)
        {
            return null;
        }
        refresh((AbstractOrderModel)order);
        return (VoucherInvalidationModel)getModelService().get(voucherInvalidation);
    }


    public void releaseVoucher(String voucherCode, CartModel cart) throws JaloPriceFactoryException
    {
        saveIfModified((AbstractOrderModel)cart);
        VoucherManager.getInstance().releaseVoucher(voucherCode, getCart(cart));
        refresh((AbstractOrderModel)cart);
    }


    public void releaseVoucher(String voucherCode, OrderModel order) throws ConsistencyCheckException
    {
        saveIfModified((AbstractOrderModel)order);
        VoucherManager.getInstance().releaseVoucher(voucherCode, getOrder(order));
        refresh((AbstractOrderModel)order);
    }


    public VoucherInvalidationModel reserveVoucher(String voucherCode, OrderModel order)
    {
        saveIfModified((AbstractOrderModel)order);
        VoucherInvalidation reserveVoucher = VoucherManager.getInstance().reserveVoucher(voucherCode, getOrder(order));
        if(reserveVoucher == null)
        {
            return null;
        }
        refresh((AbstractOrderModel)order);
        return (VoucherInvalidationModel)getModelService().get(reserveVoucher);
    }


    public void delete(VoucherModel voucher)
    {
        getModelService().remove(voucher);
    }


    public void save(VoucherModel voucher)
    {
        getModelService().save(voucher);
    }


    protected void saveIfModified(AbstractOrderModel order)
    {
        if(getModelService().isModified(order))
        {
            getModelService().save(order);
        }
        if(order.getEntries() != null)
        {
            Collection<AbstractOrderEntryModel> orderEntries = (Collection<AbstractOrderEntryModel>)order.getEntries().stream().filter(oe -> getModelService().isModified(oe)).collect(Collectors.toList());
            if(!orderEntries.isEmpty())
            {
                getModelService().saveAll(orderEntries);
            }
        }
    }


    protected void refresh(AbstractOrderModel order)
    {
        getModelService().refresh(order);
    }
}
