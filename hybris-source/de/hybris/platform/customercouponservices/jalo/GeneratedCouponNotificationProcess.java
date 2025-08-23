package de.hybris.platform.customercouponservices.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.processengine.jalo.BusinessProcess;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class GeneratedCouponNotificationProcess extends BusinessProcess
{
    public static final String LANGUAGE = "language";
    public static final String COUPONNOTIFICATION = "couponNotification";
    public static final String NOTIFICATIONTYPE = "notificationType";
    protected static final Map<String, Item.AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;

    static
    {
        Map<String, Item.AttributeMode> tmp = new HashMap<>(BusinessProcess.DEFAULT_INITIAL_ATTRIBUTES);
        tmp.put("language", Item.AttributeMode.INITIAL);
        tmp.put("couponNotification", Item.AttributeMode.INITIAL);
        tmp.put("notificationType", Item.AttributeMode.INITIAL);
        DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
    }

    protected Map<String, Item.AttributeMode> getDefaultAttributeModes()
    {
        return DEFAULT_INITIAL_ATTRIBUTES;
    }


    public CouponNotification getCouponNotification(SessionContext ctx)
    {
        return (CouponNotification)getProperty(ctx, "couponNotification");
    }


    public CouponNotification getCouponNotification()
    {
        return getCouponNotification(getSession().getSessionContext());
    }


    public void setCouponNotification(SessionContext ctx, CouponNotification value)
    {
        setProperty(ctx, "couponNotification", value);
    }


    public void setCouponNotification(CouponNotification value)
    {
        setCouponNotification(getSession().getSessionContext(), value);
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


    public EnumerationValue getNotificationType(SessionContext ctx)
    {
        return (EnumerationValue)getProperty(ctx, "notificationType");
    }


    public EnumerationValue getNotificationType()
    {
        return getNotificationType(getSession().getSessionContext());
    }


    public void setNotificationType(SessionContext ctx, EnumerationValue value)
    {
        setProperty(ctx, "notificationType", value);
    }


    public void setNotificationType(EnumerationValue value)
    {
        setNotificationType(getSession().getSessionContext(), value);
    }
}
