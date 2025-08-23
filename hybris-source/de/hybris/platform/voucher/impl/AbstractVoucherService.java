package de.hybris.platform.voucher.impl;

import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.order.AbstractOrder;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.order.Order;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.servicelayer.internal.service.AbstractBusinessService;
import de.hybris.platform.voucher.jalo.Restriction;
import de.hybris.platform.voucher.jalo.Voucher;
import de.hybris.platform.voucher.model.RestrictionModel;
import de.hybris.platform.voucher.model.VoucherModel;

public abstract class AbstractVoucherService extends AbstractBusinessService
{
    protected Cart getCart(CartModel cart)
    {
        return (Cart)getModelService().getSource(cart);
    }


    protected Order getOrder(OrderModel order)
    {
        return (Order)getModelService().getSource(order);
    }


    protected AbstractOrder getAbstractOrder(AbstractOrderModel order)
    {
        return (AbstractOrder)getModelService().getSource(order);
    }


    protected Voucher getVoucher(VoucherModel voucher)
    {
        return (Voucher)getModelService().getSource(voucher);
    }


    protected Restriction getRestriction(RestrictionModel restriction)
    {
        return (Restriction)getModelService().getSource(restriction);
    }


    protected Product getProduct(ProductModel product)
    {
        return (Product)getModelService().getSource(product);
    }


    protected User getUser(UserModel user)
    {
        return (User)getModelService().getSource(user);
    }
}
