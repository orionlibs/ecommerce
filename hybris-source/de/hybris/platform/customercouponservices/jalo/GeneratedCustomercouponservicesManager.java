package de.hybris.platform.customercouponservices.jalo;

import de.hybris.platform.customercouponservices.constants.GeneratedCustomercouponservicesConstants;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.jalo.user.Customer;
import de.hybris.platform.util.Utilities;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class GeneratedCustomercouponservicesManager extends Extension
{
    protected static String CUSTOMERCOUPON2CUSTOMER_SRC_ORDERED = "relation.CustomerCoupon2Customer.source.ordered";
    protected static String CUSTOMERCOUPON2CUSTOMER_TGT_ORDERED = "relation.CustomerCoupon2Customer.target.ordered";
    protected static String CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED = "relation.CustomerCoupon2Customer.markmodified";
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        DEFAULT_INITIAL_ATTRIBUTES = ttmp;
    }

    public Map<String, Item.AttributeMode> getDefaultAttributeModes(Class<? extends Item> itemClass)
    {
        Map<String, Item.AttributeMode> ret = new HashMap<>();
        Map<String, Item.AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
        if(attr != null)
        {
            ret.putAll(attr);
        }
        return ret;
    }


    public CouponNotification createCouponNotification(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCustomercouponservicesConstants.TC.COUPONNOTIFICATION);
            return (CouponNotification)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CouponNotification : " + e.getMessage(), 0);
        }
    }


    public CouponNotification createCouponNotification(Map attributeValues)
    {
        return createCouponNotification(getSession().getSessionContext(), attributeValues);
    }


    public CouponNotificationProcess createCouponNotificationProcess(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCustomercouponservicesConstants.TC.COUPONNOTIFICATIONPROCESS);
            return (CouponNotificationProcess)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating couponNotificationProcess : " + e.getMessage(), 0);
        }
    }


    public CouponNotificationProcess createCouponNotificationProcess(Map attributeValues)
    {
        return createCouponNotificationProcess(getSession().getSessionContext(), attributeValues);
    }


    public CustomerCoupon createCustomerCoupon(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedCustomercouponservicesConstants.TC.CUSTOMERCOUPON);
            return (CustomerCoupon)type.newInstance(ctx, attributeValues);
        }
        catch(JaloGenericCreationException e)
        {
            Throwable cause = e.getCause();
            throw (cause instanceof RuntimeException) ?
                            (RuntimeException)cause :
                            new JaloSystemException(cause, cause.getMessage(), e.getErrorCode());
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, "error creating CustomerCoupon : " + e.getMessage(), 0);
        }
    }


    public CustomerCoupon createCustomerCoupon(Map attributeValues)
    {
        return createCustomerCoupon(getSession().getSessionContext(), attributeValues);
    }


    public Collection<CustomerCoupon> getCustomerCoupons(SessionContext ctx, Customer item)
    {
        List<CustomerCoupon> items = item.getLinkedItems(ctx, false, GeneratedCustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER, "CustomerCoupon", null, false, false);
        return items;
    }


    public Collection<CustomerCoupon> getCustomerCoupons(Customer item)
    {
        return getCustomerCoupons(getSession().getSessionContext(), item);
    }


    public long getCustomerCouponsCount(SessionContext ctx, Customer item)
    {
        return item.getLinkedItemsCount(ctx, false, GeneratedCustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER, "CustomerCoupon", null);
    }


    public long getCustomerCouponsCount(Customer item)
    {
        return getCustomerCouponsCount(getSession().getSessionContext(), item);
    }


    public void setCustomerCoupons(SessionContext ctx, Customer item, Collection<CustomerCoupon> value)
    {
        item.setLinkedItems(ctx, false, GeneratedCustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER, null, value, false, false,
                        Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED));
    }


    public void setCustomerCoupons(Customer item, Collection<CustomerCoupon> value)
    {
        setCustomerCoupons(getSession().getSessionContext(), item, value);
    }


    public void addToCustomerCoupons(SessionContext ctx, Customer item, CustomerCoupon value)
    {
        item.addLinkedItems(ctx, false, GeneratedCustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED));
    }


    public void addToCustomerCoupons(Customer item, CustomerCoupon value)
    {
        addToCustomerCoupons(getSession().getSessionContext(), item, value);
    }


    public void removeFromCustomerCoupons(SessionContext ctx, Customer item, CustomerCoupon value)
    {
        item.removeLinkedItems(ctx, false, GeneratedCustomercouponservicesConstants.Relations.CUSTOMERCOUPON2CUSTOMER, null,
                        Collections.singletonList(value), false, false,
                        Utilities.getMarkModifiedOverride(CUSTOMERCOUPON2CUSTOMER_MARKMODIFIED));
    }


    public void removeFromCustomerCoupons(Customer item, CustomerCoupon value)
    {
        removeFromCustomerCoupons(getSession().getSessionContext(), item, value);
    }


    public String getName()
    {
        return "customercouponservices";
    }
}
