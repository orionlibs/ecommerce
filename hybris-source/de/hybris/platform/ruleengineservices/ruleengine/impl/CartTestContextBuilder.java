package de.hybris.platform.ruleengineservices.ruleengine.impl;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.payment.PaymentModeModel;
import de.hybris.platform.core.model.order.price.DiscountModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.security.PrincipalGroupModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.PaymentModeRAO;
import de.hybris.platform.servicelayer.model.ItemContextBuilder;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.ArrayUtils;

public class CartTestContextBuilder
{
    private static final String CURRENCY_ISO_CODE = "USD";
    private final CartModel cartModel;
    private final CartRAO cartRAO;
    private PaymentModeModel paymentModeModel;
    private PaymentModeRAO paymentModeRAO;


    public CartTestContextBuilder()
    {
        this.cartModel = new CartModel();
        this.cartModel.setCode(UUID.randomUUID().toString());
        CurrencyModel currencyModel = new CurrencyModel();
        currencyModel.setIsocode("USD");
        this.cartModel.setCurrency(currencyModel);
        this.cartRAO = new CartRAO();
    }


    public CartModel getCartModel()
    {
        return this.cartModel;
    }


    public CartRAO getCartRAO()
    {
        return this.cartRAO;
    }


    public PaymentModeModel getPaymentModeModel()
    {
        return this.paymentModeModel;
    }


    public PaymentModeRAO getPaymentModeRAO()
    {
        return this.paymentModeRAO;
    }


    public CartTestContextBuilder withPaymentModeModel(String code)
    {
        this.paymentModeModel = new PaymentModeModel();
        this.paymentModeModel.setCode(code);
        this.cartModel.setPaymentMode(this.paymentModeModel);
        return this;
    }


    public CartTestContextBuilder withPaymentModeRAO(String code)
    {
        this.paymentModeRAO = new PaymentModeRAO();
        this.paymentModeRAO.setCode(code);
        this.cartRAO.setPaymentMode(this.paymentModeRAO);
        return this;
    }


    public CartTestContextBuilder withEntries(List<AbstractOrderEntryModel> entries)
    {
        this.cartModel.setEntries(entries);
        return this;
    }


    public CartTestContextBuilder addEntry(AbstractOrderEntryModel entry)
    {
        entry.setOrder((AbstractOrderModel)this.cartModel);
        List<AbstractOrderEntryModel> entries = this.cartModel.getEntries();
        if(CollectionUtils.isEmpty(entries))
        {
            entries = new ArrayList<>();
            this.cartModel.setEntries(entries);
        }
        entries.add(entry);
        return this;
    }


    public CartTestContextBuilder addNewEntry()
    {
        AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
        return addEntry(entry);
    }


    public CartTestContextBuilder addNewEntry(ProductModel product)
    {
        AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
        entry.setProduct(product);
        return addEntry(entry);
    }


    public CartTestContextBuilder addNewEntry(CategoryModel... categoryModels)
    {
        AbstractOrderEntryModel entry = new AbstractOrderEntryModel();
        ProductModel product = new ProductModel();
        entry.setProduct(product);
        if(ArrayUtils.isNotEmpty((Object[])categoryModels))
        {
            product.setSupercategories(Arrays.asList(categoryModels));
        }
        return addEntry(entry);
    }


    public CartTestContextBuilder withDiscounts(List<DiscountModel> discounts)
    {
        this.cartModel.setDiscounts(discounts);
        return this;
    }


    public CartTestContextBuilder addDiscount(DiscountModel discount)
    {
        List<DiscountModel> discounts = this.cartModel.getDiscounts();
        if(CollectionUtils.isEmpty(discounts))
        {
            discounts = new ArrayList<>();
            this.cartModel.setDiscounts(discounts);
        }
        discounts.add(discount);
        return this;
    }


    public CartTestContextBuilder withUser(String userId)
    {
        ItemContextBuilder customerModelBuilder = ItemContextBuilder.createMockContextBuilder(CustomerModel.class, null, Locale.ENGLISH,
                        Collections.emptyMap());
        UserModel userModel = new UserModel((ItemModelContext)customerModelBuilder.build());
        userModel.setUid(userId);
        return withUser(userModel);
    }


    public CartTestContextBuilder withUserGroups(String userId, PrincipalGroupModel... groups)
    {
        ItemContextBuilder customerModelBuilder = ItemContextBuilder.createMockContextBuilder(CustomerModel.class, null, Locale.ENGLISH,
                        Collections.emptyMap());
        UserModel userModel = new UserModel((ItemModelContext)customerModelBuilder.build());
        userModel.setUid(userId);
        userModel.setGroups(new HashSet(Arrays.asList((Object[])groups)));
        return withUser(userModel);
    }


    public CartTestContextBuilder withUser(UserModel user)
    {
        this.cartModel.setUser(user);
        return this;
    }


    public CartTestContextBuilder withTotalPrice(Double totalPrice)
    {
        this.cartModel.setTotalPrice(totalPrice);
        return this;
    }


    public CartTestContextBuilder withSubtotal(Double subTotal)
    {
        this.cartModel.setSubtotal(subTotal);
        return this;
    }


    public CartTestContextBuilder withDeliveryCost(Double deliveryCost)
    {
        this.cartModel.setDeliveryCost(deliveryCost);
        return this;
    }


    public CartTestContextBuilder withPaymentCost(Double paymentCost)
    {
        this.cartModel.setPaymentCost(paymentCost);
        return this;
    }


    public CartTestContextBuilder withCurrency(String currencyIsoCode)
    {
        CurrencyModel currencyModel = new CurrencyModel();
        currencyModel.setIsocode(currencyIsoCode);
        this.cartModel.setCurrency(currencyModel);
        return this;
    }
}
