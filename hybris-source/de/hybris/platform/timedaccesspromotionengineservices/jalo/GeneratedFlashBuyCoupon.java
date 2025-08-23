package de.hybris.platform.timedaccesspromotionengineservices.jalo;

import de.hybris.platform.couponservices.jalo.SingleCodeCoupon;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.promotionengineservices.jalo.PromotionSourceRule;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedFlashBuyCoupon extends SingleCodeCoupon
{
    public static final String RULE = "rule";
    public static final String MAXPRODUCTQUANTITYPERORDER = "maxProductQuantityPerOrder";
    public static final String PRODUCT = "product";
    public static final String ORIGINALMAXORDERQUANTITY = "originalMaxOrderQuantity";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(SingleCodeCoupon.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("rule", Item.AttributeMode.INITIAL);
        tmp.put("maxProductQuantityPerOrder", Item.AttributeMode.INITIAL);
        tmp.put("product", Item.AttributeMode.INITIAL);
        tmp.put("originalMaxOrderQuantity", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Integer getMaxProductQuantityPerOrder(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "maxProductQuantityPerOrder");
    }


    public Integer getMaxProductQuantityPerOrder()
    {
        return getMaxProductQuantityPerOrder(getSession().getSessionContext());
    }


    public int getMaxProductQuantityPerOrderAsPrimitive(SessionContext ctx)
    {
        Integer value = getMaxProductQuantityPerOrder(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getMaxProductQuantityPerOrderAsPrimitive()
    {
        return getMaxProductQuantityPerOrderAsPrimitive(getSession().getSessionContext());
    }


    public void setMaxProductQuantityPerOrder(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "maxProductQuantityPerOrder", value);
    }


    public void setMaxProductQuantityPerOrder(Integer value)
    {
        setMaxProductQuantityPerOrder(getSession().getSessionContext(), value);
    }


    public void setMaxProductQuantityPerOrder(SessionContext ctx, int value)
    {
        setMaxProductQuantityPerOrder(ctx, Integer.valueOf(value));
    }


    public void setMaxProductQuantityPerOrder(int value)
    {
        setMaxProductQuantityPerOrder(getSession().getSessionContext(), value);
    }


    public Integer getOriginalMaxOrderQuantity(SessionContext ctx)
    {
        return (Integer)getProperty(ctx, "originalMaxOrderQuantity");
    }


    public Integer getOriginalMaxOrderQuantity()
    {
        return getOriginalMaxOrderQuantity(getSession().getSessionContext());
    }


    public int getOriginalMaxOrderQuantityAsPrimitive(SessionContext ctx)
    {
        Integer value = getOriginalMaxOrderQuantity(ctx);
        return (value != null) ? value.intValue() : 0;
    }


    public int getOriginalMaxOrderQuantityAsPrimitive()
    {
        return getOriginalMaxOrderQuantityAsPrimitive(getSession().getSessionContext());
    }


    public void setOriginalMaxOrderQuantity(SessionContext ctx, Integer value)
    {
        setProperty(ctx, "originalMaxOrderQuantity", value);
    }


    public void setOriginalMaxOrderQuantity(Integer value)
    {
        setOriginalMaxOrderQuantity(getSession().getSessionContext(), value);
    }


    public void setOriginalMaxOrderQuantity(SessionContext ctx, int value)
    {
        setOriginalMaxOrderQuantity(ctx, Integer.valueOf(value));
    }


    public void setOriginalMaxOrderQuantity(int value)
    {
        setOriginalMaxOrderQuantity(getSession().getSessionContext(), value);
    }


    public Product getProduct(SessionContext ctx)
    {
        return (Product)getProperty(ctx, "product");
    }


    public Product getProduct()
    {
        return getProduct(getSession().getSessionContext());
    }


    public void setProduct(SessionContext ctx, Product value)
    {
        setProperty(ctx, "product", value);
    }


    public void setProduct(Product value)
    {
        setProduct(getSession().getSessionContext(), value);
    }


    public PromotionSourceRule getRule(SessionContext ctx)
    {
        return (PromotionSourceRule)getProperty(ctx, "rule");
    }


    public PromotionSourceRule getRule()
    {
        return getRule(getSession().getSessionContext());
    }


    public void setRule(SessionContext ctx, PromotionSourceRule value)
    {
        setProperty(ctx, "rule", value);
    }


    public void setRule(PromotionSourceRule value)
    {
        setRule(getSession().getSessionContext(), value);
    }
}
