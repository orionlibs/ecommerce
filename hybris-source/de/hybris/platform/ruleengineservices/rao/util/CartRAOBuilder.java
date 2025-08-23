package de.hybris.platform.ruleengineservices.rao.util;

import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CartRAOBuilder
{
    public static final String DEFAULT_CURRENCY_ISO_CODE = "USD";
    private final CartRAO cartRAO;
    private String lastProduct;
    private OrderEntryRAO lastOrderEntry;
    private static final DefaultRaoService raoService = new DefaultRaoService();


    public CartRAOBuilder()
    {
        this("" + Math.random());
    }


    public CartRAOBuilder(String cartId)
    {
        this(cartId, "USD");
    }


    public CartRAOBuilder(String cartId, String currencyIsoCode)
    {
        this(raoService.createCart());
        this.cartRAO.setCode(cartId);
        this.cartRAO.setTotal(BigDecimal.ZERO);
        this.cartRAO.setActions(new LinkedHashSet());
        this.cartRAO.setEntries(new HashSet());
        this.cartRAO.setCurrencyIsoCode(currencyIsoCode);
    }


    public CartRAOBuilder(CartRAO cart)
    {
        this.cartRAO = cart;
    }


    public CartRAOBuilder addProductLine(String productCode, int quantity, double price, String... categories)
    {
        this.lastProduct = productCode;
        return addProductQuantity(this.lastProduct, quantity, price);
    }


    public CartRAOBuilder addProductQuantity(String product, int quantity, double price)
    {
        this.lastOrderEntry = raoService.createOrderEntry();
        this.lastOrderEntry.setProductCode(product);
        this.lastOrderEntry.setQuantity(quantity);
        this.lastOrderEntry.setBasePrice(BigDecimal.valueOf(price));
        this.lastOrderEntry.setPrice(BigDecimal.valueOf(price));
        return addEntry(this.lastOrderEntry);
    }


    public CartRAOBuilder addCartDiscount(boolean absolute, double value)
    {
        DiscountRAO discountRAO = createDiscount(value);
        if(absolute)
        {
            discountRAO.setCurrencyIsoCode(this.cartRAO.getCurrencyIsoCode());
        }
        this.cartRAO.getActions().add(discountRAO);
        return this;
    }


    private DiscountRAO createDiscount(double value)
    {
        DiscountRAO discountRAO = raoService.createDiscount();
        discountRAO.setValue(BigDecimal.valueOf(value));
        return discountRAO;
    }


    public CartRAOBuilder addProductDiscount(boolean absolute, double value)
    {
        DiscountRAO discountRAO = createDiscount(value);
        if(absolute)
        {
            discountRAO.setCurrencyIsoCode(this.cartRAO.getCurrencyIsoCode());
        }
        getLastOrderEntry().getActions().add(discountRAO);
        return this;
    }


    public CartRAOBuilder addEntry(OrderEntryRAO rao)
    {
        this.cartRAO.getEntries().add(rao);
        return this;
    }


    public CartRAO toCart()
    {
        return this.cartRAO;
    }


    public OrderEntryRAO getLastOrderEntry()
    {
        return this.lastOrderEntry;
    }


    public String getLastProduct()
    {
        return this.lastProduct;
    }


    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
