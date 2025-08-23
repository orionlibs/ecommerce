package de.hybris.platform.ruleengineservices.rao.util;

import de.hybris.platform.ruleengineservices.rao.AbstractOrderRAO;
import de.hybris.platform.ruleengineservices.rao.CartRAO;
import de.hybris.platform.ruleengineservices.rao.DiscountRAO;
import de.hybris.platform.ruleengineservices.rao.EntriesSelectionStrategyRPD;
import de.hybris.platform.ruleengineservices.rao.OrderEntryRAO;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import org.springframework.util.Assert;

public class DefaultRaoService
{
    public void addCartDiscount(boolean absolute, double value, CartRAO cart)
    {
        DiscountRAO discount = createDiscount(value);
        if(absolute)
        {
            discount.setCurrencyIsoCode(cart.getCurrencyIsoCode());
        }
        addCartDiscount(discount, cart);
    }


    protected void addCartDiscount(DiscountRAO discount, CartRAO cart)
    {
        if(cart.getActions() == null)
        {
            cart.setActions(new LinkedHashSet());
        }
        cart.getActions().add(discount);
    }


    public void addEntry(OrderEntryRAO entry, CartRAO cart)
    {
        if(cart.getEntries() == null)
        {
            cart.setEntries(new LinkedHashSet());
        }
        cart.getEntries().add(entry);
        entry.setOrder((AbstractOrderRAO)cart);
    }


    public void addEntryDiscount(boolean absolute, double value, OrderEntryRAO entry)
    {
        DiscountRAO discount = createDiscount(value);
        if(absolute)
        {
            Assert.notNull(entry.getOrder());
            discount.setCurrencyIsoCode(entry.getOrder().getCurrencyIsoCode());
        }
        addEntryDiscount(discount, entry);
    }


    protected void addEntryDiscount(DiscountRAO discount, OrderEntryRAO entry)
    {
        if(entry.getActions() == null)
        {
            entry.setActions(new LinkedHashSet());
        }
        entry.getActions().add(discount);
    }


    public CartRAO createCart()
    {
        CartRAO cart = new CartRAO();
        cart.setOriginalTotal(BigDecimal.ZERO);
        cart.setActions(new LinkedHashSet());
        cart.setEntries(new LinkedHashSet());
        cart.setTotal(BigDecimal.ZERO);
        return cart;
    }


    public EntriesSelectionStrategyRPD createEntriesSelectionStrategyRPD()
    {
        EntriesSelectionStrategyRPD entriesSelectionStrategyRPD = new EntriesSelectionStrategyRPD();
        entriesSelectionStrategyRPD.setOrderEntries(new ArrayList());
        return entriesSelectionStrategyRPD;
    }


    public DiscountRAO createDiscount()
    {
        DiscountRAO discount = new DiscountRAO();
        discount.setValue(BigDecimal.ZERO);
        return discount;
    }


    protected DiscountRAO createDiscount(double value)
    {
        DiscountRAO discount = createDiscount();
        discount.setValue(BigDecimal.valueOf(value));
        return discount;
    }


    public OrderEntryRAO createOrderEntry()
    {
        OrderEntryRAO orderEntry = new OrderEntryRAO();
        orderEntry.setBasePrice(BigDecimal.ZERO);
        orderEntry.setPrice(BigDecimal.ZERO);
        orderEntry.setActions(new LinkedHashSet());
        return orderEntry;
    }


    public OrderEntryRAO createOrderEntry(AbstractOrderRAO order, String product, double basePrice, int quantity, int entryNumber)
    {
        OrderEntryRAO orderEntry = new OrderEntryRAO();
        orderEntry.setProductCode(product);
        orderEntry.setBasePrice(BigDecimal.valueOf(basePrice));
        orderEntry.setPrice(BigDecimal.valueOf(basePrice));
        orderEntry.setQuantity(quantity);
        orderEntry.setOrder(order);
        orderEntry.setEntryNumber(Integer.valueOf(entryNumber));
        return orderEntry;
    }


    public void addPromotedProduct(String productCode, int quantity, double basePrice, double value, CartRAO cart)
    {
        OrderEntryRAO discountedEntry = addProduct(productCode, quantity, basePrice, cart);
        addEntryDiscount(true, value, discountedEntry);
    }


    public OrderEntryRAO addProduct(String promotedProduct, int quantity, double basePrice, CartRAO cart)
    {
        OrderEntryRAO entry = createOrderEntry((AbstractOrderRAO)cart, promotedProduct, basePrice, quantity, getNewOrderEntryNumber(cart));
        addEntry(entry, cart);
        return entry;
    }


    protected int getNewOrderEntryNumber(CartRAO cart)
    {
        Integer maxEntryNumber = null;
        if(cart.getEntries() == null)
        {
            return 0;
        }
        for(OrderEntryRAO entry : cart.getEntries())
        {
            if(entry.getEntryNumber() != null)
            {
                if(maxEntryNumber == null)
                {
                    maxEntryNumber = Integer.valueOf(0);
                }
                if(maxEntryNumber.compareTo(entry.getEntryNumber()) < 0)
                {
                    maxEntryNumber = entry.getEntryNumber();
                }
            }
        }
        return (maxEntryNumber != null) ? maxEntryNumber.intValue() : 0;
    }
}
