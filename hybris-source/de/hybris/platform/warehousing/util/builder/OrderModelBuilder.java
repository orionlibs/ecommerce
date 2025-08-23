package de.hybris.platform.warehousing.util.builder;

import com.google.common.collect.Lists;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.delivery.DeliveryModeModel;
import de.hybris.platform.core.model.order.payment.PaymentInfoModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.payment.model.PaymentTransactionModel;
import de.hybris.platform.store.BaseStoreModel;
import de.hybris.platform.util.TaxValue;
import java.util.Date;
import java.util.List;

public class OrderModelBuilder
{
    private final OrderModel model = new OrderModel();


    public static OrderModelBuilder aModel()
    {
        return new OrderModelBuilder();
    }


    private OrderModel getModel()
    {
        return this.model;
    }


    public OrderModel build()
    {
        return getModel();
    }


    public OrderModelBuilder withCode(String code)
    {
        getModel().setCode(code);
        return this;
    }


    public OrderModelBuilder withDeliveryMode(DeliveryModeModel deliveryMode)
    {
        getModel().setDeliveryMode(deliveryMode);
        return this;
    }


    public OrderModelBuilder withCurrency(CurrencyModel currency)
    {
        getModel().setCurrency(currency);
        return this;
    }


    public OrderModelBuilder withStore(BaseStoreModel store)
    {
        getModel().setStore(store);
        return this;
    }


    public OrderModelBuilder withDeliveryAddress(AddressModel deliveryAddress)
    {
        getModel().setDeliveryAddress(deliveryAddress);
        return this;
    }


    public OrderModelBuilder withDate(Date date)
    {
        getModel().setDate(date);
        return this;
    }


    public OrderModelBuilder withUser(UserModel user)
    {
        getModel().setUser(user);
        return this;
    }


    public OrderModelBuilder withCustomser(CustomerModel customer)
    {
        getModel().setUser((UserModel)customer);
        return this;
    }


    public OrderModelBuilder withBaseSite(BaseSiteModel baseSite)
    {
        getModel().setSite(baseSite);
        return this;
    }


    public OrderModelBuilder withEntries(AbstractOrderEntryModel... entries)
    {
        double deliveryCostTax;
        getModel().setEntries(Lists.newArrayList((Object[])entries));
        getModel().setTotalPrice(Double.valueOf(getModel().getEntries().stream().mapToDouble(AbstractOrderEntryModel::getTotalPrice).sum() + (
                        (getModel().getDeliveryCost() != null) ?
                                        getModel().getDeliveryCost().doubleValue() :
                                        0.0D)));
        if(getModel().getTotalTaxValues() != null && getModel().getTotalTaxValues().size() > 0)
        {
            deliveryCostTax = ((TaxValue)getModel().getTotalTaxValues().stream().findFirst().get()).getValue();
        }
        else
        {
            deliveryCostTax = 0.0D;
        }
        getModel().setTotalTax(
                        Double.valueOf(getModel().getEntries().stream().flatMap(entry -> entry.getTaxValues().stream()).mapToDouble(TaxValue::getValue).sum() + deliveryCostTax));
        return this;
    }


    public OrderModelBuilder withPaymentInfo(PaymentInfoModel paymentInfo)
    {
        getModel().setPaymentInfo(paymentInfo);
        return this;
    }


    public OrderModelBuilder withPaymentTransactions(List<PaymentTransactionModel> paymentTransactions)
    {
        getModel().setPaymentTransactions(paymentTransactions);
        return this;
    }
}
