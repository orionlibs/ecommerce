package de.hybris.platform.customercouponservices.jalo;

import de.hybris.platform.couponservices.jalo.AbstractCoupon;
import de.hybris.platform.customercouponservices.constants.GeneratedCustomercouponservicesConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCustomerCoupon extends AbstractCoupon
{
    public static final String ASSIGNABLE = "assignable";
    public static final String DESCRIPTION = "description";
    public static final String CUSTOMERS = "customers";
    protected static String CUSTOMERCOUPON2CUSTOMER_SRC_ORDERED = "relation.CustomerCoupon2Customer.source.ordered";
    protected static String CUSTOMERCOUPON2CUSTOMER_TGT_ORDERED = "relation.CustomerCoupon2Customer.target.ordered";
    protected static String CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED = "relation.CustomerCoupon2Customer.markmodified";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(AbstractCoupon.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("assignable", Item.AttributeMode.INITIAL);
        tmp.put("description", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public Boolean isAssignable(SessionContext ctx)
    {
        return (Boolean)getProperty(ctx, "assignable");
    }


    public Boolean isAssignable()
    {
        return isAssignable(getSession().getSessionContext());
    }


    public boolean isAssignableAsPrimitive(SessionContext ctx)
    {
        Boolean value = isAssignable(ctx);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isAssignableAsPrimitive()
    {
        return isAssignableAsPrimitive(getSession().getSessionContext());
    }


    public void setAssignable(SessionContext ctx, Boolean value)
    {
        setProperty(ctx, "assignable", value);
    }


    public void setAssignable(Boolean value)
    {
        setAssignable(getSession().getSessionContext(), value);
    }


    public void setAssignable(SessionContext ctx, boolean value)
    {
        setAssignable(ctx, Boolean.valueOf(value));
    }


    public void setAssignable(boolean value)
    {
        setAssignable(getSession().getSessionContext(), value);
    }


    public Collection<Customer> getCustomers(SessionContext ctx)
    {
        List<Customer> items = getLinkedItems(ctx, true, GeneratedCustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER, "Customer", null, false, false);
        return items;
    }


    public Collection<Customer> getCustomers()
    {
        return getCustomers(getSession().getSessionContext());
    }


    public long getCustomersCount(SessionContext ctx)
    {
        return getLinkedItemsCount(ctx, true, GeneratedCustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER, "Customer", null);
    }


    public long getCustomersCount()
    {
        return getCustomersCount(getSession().getSessionContext());
    }


    public void setCustomers(SessionContext ctx, Collection<Customer> value)
    {
        setLinkedItems(ctx, true, GeneratedCustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED));
    }


    public void setCustomers(Collection<Customer> value)
    {
        setCustomers(getSession().getSessionContext(), value);
    }


    public void addToCustomers(SessionContext ctx, Customer value)
    {
        addLinkedItems(ctx, true, GeneratedCustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED));
    }


    public void addToCustomers(Customer value)
    {
        addToCustomers(getSession().getSessionContext(), value);
    }


    public void removeFromCustomers(SessionContext ctx, Customer value)
    {
        removeLinkedItems(ctx, true, GeneratedCustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED));
    }


    public void removeFromCustomers(Customer value)
    {
        removeFromCustomers(getSession().getSessionContext(), value);
    }


    public String getDescription(SessionContext ctx)
    {
        if(ctx == null || ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCustomerCoupon.getDescription requires a session language", 0);
        }
        return (String)getLocalizedProperty(ctx, "description");
    }


    public String getDescription()
    {
        return getDescription(getSession().getSessionContext());
    }


    public Map<Language, String> getAllDescription(SessionContext ctx)
    {
        return getAllLocalizedProperties(ctx, "description", C2LManager.getInstance().getAllLanguages());
    }


    public Map<Language, String> getAllDescription()
    {
        return getAllDescription(getSession().getSessionContext());
    }


    public void setDescription(SessionContext ctx, String value)
    {
        if(ctx == null)
        {
            throw new JaloInvalidParameterException("ctx is null", 0);
        }
        if(ctx.getLanguage() == null)
        {
            throw new JaloInvalidParameterException("GeneratedCustomerCoupon.setDescription requires a session language", 0);
        }
        setLocalizedProperty(ctx, "description", value);
    }


    public void setDescription(String value)
    {
        setDescription(getSession().getSessionContext(), value);
    }


    public void setAllDescription(SessionContext ctx, Map<Language, String> value)
    {
        setAllLocalizedProperties(ctx, "description", value);
    }


    public void setAllDescription(Map<Language, String> value)
    {
        setAllDescription(getSession().getSessionContext(), value);
    }


    @Deprecated(since = "2105", forRemoval = true)
    public boolean isMarkModifiedDisabled(Item referencedItem)
    {
        ComposedType relationSecondEnd0 = TypeManager.getInstance().getComposedType("Customer");
        if(relationSecondEnd0.isAssignableFrom((Type)referencedItem.getComposedType()))
        {
            return Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED);
        }
        return true;
    }
}
