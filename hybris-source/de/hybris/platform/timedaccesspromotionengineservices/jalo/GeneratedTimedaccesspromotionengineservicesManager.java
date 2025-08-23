package de.hybris.platform.timedaccesspromotionengineservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.order.Cart;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.timedaccesspromotionengineservices.constants.GeneratedTimedaccesspromotionengineservicesConstants;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedTimedaccesspromotionengineservicesManager extends Extension
{
    protected static final Map<String, Map<String, Item.AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Map<String, Item.AttributeMode>> ttmp = new HashMap<>();
        Map<String, Item.AttributeMode> tmp = new HashMap<>();
        tmp.put("processingFlashBuyOrder", Item.AttributeMode.INITIAL);
        ttmp.put("de.hybris.platform.jalo.order.Cart", Collections.unmodifiableMap(tmp));
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


    public FlashBuyCoupon createFlashBuyCoupon(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTimedaccesspromotionengineservicesConstants.TC.FLASHBUYCOUPON);
            return (FlashBuyCoupon)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating FlashBuyCoupon : " + e.getMessage(), 0);
        }
    }


    public FlashBuyCoupon createFlashBuyCoupon(Map attributeValues)
    {
        return createFlashBuyCoupon(getSession().getSessionContext(), attributeValues);
    }


    public FlashBuyCronJob createFlashBuyCronJob(SessionContext ctx, Map attributeValues)
    {
        try
        {
            ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType(GeneratedTimedaccesspromotionengineservicesConstants.TC.FLASHBUYCRONJOB);
            return (FlashBuyCronJob)type.newInstance(ctx, attributeValues);
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
            throw new JaloSystemException(e, "error creating FlashBuyCronJob : " + e.getMessage(), 0);
        }
    }


    public FlashBuyCronJob createFlashBuyCronJob(Map attributeValues)
    {
        return createFlashBuyCronJob(getSession().getSessionContext(), attributeValues);
    }


    public String getName()
    {
        return "timedaccesspromotionengineservices";
    }


    public Boolean isProcessingFlashBuyOrder(SessionContext ctx, Cart item)
    {
        return (Boolean)item.getProperty(ctx, GeneratedTimedaccesspromotionengineservicesConstants.Attributes.Cart.PROCESSINGFLASHBUYORDER);
    }


    public Boolean isProcessingFlashBuyOrder(Cart item)
    {
        return isProcessingFlashBuyOrder(getSession().getSessionContext(), item);
    }


    public boolean isProcessingFlashBuyOrderAsPrimitive(SessionContext ctx, Cart item)
    {
        Boolean value = isProcessingFlashBuyOrder(ctx, item);
        return (value != null) ? value.booleanValue() : false;
    }


    public boolean isProcessingFlashBuyOrderAsPrimitive(Cart item)
    {
        return isProcessingFlashBuyOrderAsPrimitive(getSession().getSessionContext(), item);
    }


    public void setProcessingFlashBuyOrder(SessionContext ctx, Cart item, Boolean value)
    {
        item.setProperty(ctx, GeneratedTimedaccesspromotionengineservicesConstants.Attributes.Cart.PROCESSINGFLASHBUYORDER, value);
    }


    public void setProcessingFlashBuyOrder(Cart item, Boolean value)
    {
        setProcessingFlashBuyOrder(getSession().getSessionContext(), item, value);
    }


    public void setProcessingFlashBuyOrder(SessionContext ctx, Cart item, boolean value)
    {
        setProcessingFlashBuyOrder(ctx, item, Boolean.valueOf(value));
    }


    public void setProcessingFlashBuyOrder(Cart item, boolean value)
    {
        setProcessingFlashBuyOrder(getSession().getSessionContext(), item, value);
    }
}
