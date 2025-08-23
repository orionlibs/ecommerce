package de.hybris.platform.timedaccesspromotionengineservices.jalo;

import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedFlashBuyCronJob extends CronJob
{
    public static final String FLASHBUYCOUPON = "flashBuyCoupon";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(CronJob.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("flashBuyCoupon", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public FlashBuyCoupon getFlashBuyCoupon(SessionContext ctx)
    {
        return (FlashBuyCoupon)getProperty(ctx, "flashBuyCoupon");
    }


    public FlashBuyCoupon getFlashBuyCoupon()
    {
        return getFlashBuyCoupon(getSession().getSessionContext());
    }


    public void setFlashBuyCoupon(SessionContext ctx, FlashBuyCoupon value)
    {
        setProperty(ctx, "flashBuyCoupon", value);
    }


    public void setFlashBuyCoupon(FlashBuyCoupon value)
    {
        setFlashBuyCoupon(getSession().getSessionContext(), value);
    }
}
