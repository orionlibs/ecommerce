package de.hybris.platform.customercouponservices.jalo;

import de.hybris.platform.basecommerce.jalo.site.BaseSite;
import de.hybris.platform.jalo.GenericItem;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.user.Customer;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCouponNotification extends GenericItem
{
    public static final String CUSTOMERCOUPON = "customerCoupon";
    public static final String CUSTOMER = "customer";
    public static final String STATUS = "status";
    public static final String BASESITE = "baseSite";
    public static final String LANGUAGE = "language";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("customerCoupon", Item.AttributeMode.INITIAL);
        tmp.put("customer", Item.AttributeMode.INITIAL);
        tmp.put("status", Item.AttributeMode.INITIAL);
        tmp.put("baseSite", Item.AttributeMode.INITIAL);
        tmp.put("language", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public BaseSite getBaseSite(SessionContext ctx)
    {
        return (BaseSite)getProperty(ctx, "baseSite");
    }


    public BaseSite getBaseSite()
    {
        return getBaseSite(getSession().getSessionContext());
    }


    public void setBaseSite(SessionContext ctx, BaseSite value)
    {
        setProperty(ctx, "baseSite", value);
    }


    public void setBaseSite(BaseSite value)
    {
        setBaseSite(getSession().getSessionContext(), value);
    }


    public Customer getCustomer(SessionContext ctx)
    {
        return (Customer)getProperty(ctx, "customer");
    }


    public Customer getCustomer()
    {
        return getCustomer(getSession().getSessionContext());
    }


    public void setCustomer(SessionContext ctx, Customer value)
    {
        setProperty(ctx, "customer", value);
    }


    public void setCustomer(Customer value)
    {
        setCustomer(getSession().getSessionContext(), value);
    }


    public CustomerCoupon getCustomerCoupon(SessionContext ctx)
    {
        return (CustomerCoupon)getProperty(ctx, "customerCoupon");
    }


    public CustomerCoupon getCustomerCoupon()
    {
        return getCustomerCoupon(getSession().getSessionContext());
    }


    public void setCustomerCoupon(SessionContext ctx, CustomerCoupon value)
    {
        setProperty(ctx, "customerCoupon", value);
    }


    public void setCustomerCoupon(CustomerCoupon value)
    {
        setCustomerCoupon(getSession().getSessionContext(), value);
    }


    public Language getLanguage(SessionContext ctx)
    {
        return (Language)getProperty(ctx, "language");
    }


    public Language getLanguage()
    {
        return getLanguage(getSession().getSessionContext());
    }


    public void setLanguage(SessionContext ctx, Language value)
    {
        setProperty(ctx, "language", value);
    }


    public void setLanguage(Language value)
    {
        setLanguage(getSession().getSessionContext(), value);
    }


    public EnumerationValue getStatus(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "status");
    }


    public EnumerationValue getStatus()
    {
        return getStatus(getSession().getSessionContext());
    }


    public void setStatus(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "status", value);
    }


    public void setStatus(EnumerationValue value)
    {
        setStatus(getSession().getSessionContext(), value);
    }
}
