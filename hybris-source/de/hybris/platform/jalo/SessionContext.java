package de.hybris.platform.jalo;

import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.c2l.Currency;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.order.price.PriceFactory;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.Utilities;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SessionContext implements Serializable
{
    private static final Logger LOG = LoggerFactory.getLogger(SessionContext.class);
    public static final String USER = "user";
    public static final String LANGUAGE = "language";
    public static final String CURRENCY = "currency";
    public static final String PRICEFACTORY = "pricefactory";
    public static final String LOCALE = "locale";
    public static final String TIMEZONE = "timezone";
    public static final Long ZERO_TIME_OFFSET = Long.valueOf(0L);
    public static final String TIMEOFFSET = "timeoffset";
    public static final String CURRENTDATE = "currentdate";
    public static final String CURRENTDATE_VALID_TO = "currentdateValidTo";
    public static final String TRANSACTION_IN_CREATE_DISABLED = "transaction_in_create_disabled";
    public static final String TRANSACTION_4_ALLATTRIBUTES = "all.attributes.use.ta";
    private final ContextMap<String, Object> contextMap;
    private JaloSession session;
    private Tenant sessionTenant;


    public SessionContext()
    {
        this(null);
    }


    public SessionContext(SessionContext context)
    {
        this(context, false);
    }


    protected SessionContext(SessionContext context, boolean asLocal)
    {
        if(context == null)
        {
            this.contextMap = (ContextMap<String, Object>)new GlobalContextMap();
        }
        else if(asLocal)
        {
            this.contextMap = (ContextMap<String, Object>)new LocalContextMap(context.contextMap, 4);
            this.sessionTenant = context.sessionTenant;
        }
        else
        {
            this.contextMap = (ContextMap<String, Object>)new GlobalContextMap();
            this.contextMap.putAll((Map)context.contextMap);
        }
    }


    public boolean equals(Object object)
    {
        if(this == object)
        {
            return true;
        }
        if(object == null || getClass() != object.getClass())
        {
            return false;
        }
        SessionContext that = (SessionContext)object;
        if(!this.contextMap.equals(that.contextMap))
        {
            return false;
        }
        if((this.session != null) ? !this.session.equals(that.session) : (that.session != null))
        {
            return false;
        }
        if((this.sessionTenant != null) ? !this.sessionTenant.equals(that.sessionTenant) : (that.sessionTenant != null))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = this.contextMap.hashCode();
        result = 31 * result + ((this.session != null) ? this.session.hashCode() : 0);
        result = 31 * result + ((this.sessionTenant != null) ? this.sessionTenant.hashCode() : 0);
        return result;
    }


    protected void setSession(JaloSession session)
    {
        if(session == null)
        {
            throw new NullPointerException("session was null");
        }
        if(this.session != null)
        {
            throw new IllegalStateException("session already assigned");
        }
        this.session = session;
        this.sessionTenant = session.getTenant();
    }


    protected void initializeOnSessionStartup(User user, Language language, Currency currency, TimeZone timeZone, PriceFactory injectedPriceFactory)
    {
        if(user != null)
        {
            this.contextMap.put("user", user);
        }
        if(currency != null)
        {
            this.contextMap.put("currency", currency);
        }
        if(language != null)
        {
            this.contextMap.put("language", language);
        }
        if(timeZone != null)
        {
            this.contextMap.put("timezone", timeZone);
        }
        if(injectedPriceFactory != null)
        {
            this.contextMap.put("pricefactory", injectedPriceFactory);
        }
    }


    public String toString()
    {
        return (this.session != null) ? ("ctx<<" + this.session.getTenant().getTenantID() + ">>") : "ctx<<null>>";
    }


    public void setSessionContextValues(SessionContext ctx)
    {
        if(ctx != null)
        {
            setAttributes(ctx.getAttributes());
        }
    }


    public void setLanguage(Language language)
    {
        setAttribute("language", language);
    }


    public Language getLanguage()
    {
        Language language = (Language)this.contextMap.get("language");
        return (language == null) ? null : (Language)language.getCacheBoundItem();
    }


    public Locale getLocale()
    {
        Locale ret = (Locale)this.contextMap.get("locale");
        if(ret == null)
        {
            Language language = getLanguage();
            if(language != null)
            {
                ret = language.getLocale();
            }
            else if(Registry.hasCurrentTenant())
            {
                ret = Registry.getCurrentTenant().getTenantSpecificLocale();
            }
            else
            {
                ret = Locale.getDefault();
            }
        }
        return ret;
    }


    public void setLocale(Locale loc)
    {
        setAttribute("locale", loc);
    }


    public TimeZone getTimeZone()
    {
        TimeZone timeZone = (TimeZone)this.contextMap.get("timezone");
        return (timeZone != null) ? timeZone : (
                        Registry.hasCurrentTenant() ? Registry.getCurrentTenant().getTenantSpecificTimeZone() : TimeZone.getDefault());
    }


    public long getTimeOffset()
    {
        Long timeOffset = (Long)this.contextMap.get("timeoffset");
        return (timeOffset == null) ? 0L : timeOffset.longValue();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setTimeZome(TimeZone timezone)
    {
        setAttribute("timezone", timezone);
    }


    public void setTimeZone(TimeZone timezone)
    {
        setAttribute("timezone", timezone);
    }


    public void setCurrency(Currency currency)
    {
        setAttribute("currency", currency);
    }


    public Currency getCurrency()
    {
        Currency currency = (Currency)this.contextMap.get("currency");
        return (currency == null) ? null : (Currency)currency.getCacheBoundItem();
    }


    public void setUser(User user)
    {
        setAttribute("user", user);
    }


    public User getUser()
    {
        User user = (User)this.contextMap.get("user");
        return (user == null) ? null : (User)user.getCacheBoundItem();
    }


    public void setPriceFactory(PriceFactory pricefactory)
    {
        setAttribute("pricefactory", pricefactory);
    }


    public PriceFactory getPriceFactory()
    {
        return (PriceFactory)this.contextMap.get("pricefactory");
    }


    public void setAttributes(Map<String, ? extends Object> attributes)
    {
        for(Map.Entry<String, ? extends Object> entry : attributes.entrySet())
        {
            setAttribute(entry.getKey(), entry.getValue());
        }
    }


    public Map<String, Object> getAttributes()
    {
        fixStaleItemsInContext();
        return Collections.unmodifiableMap((Map<? extends String, ?>)this.contextMap);
    }


    protected void fixStaleItemsInContext()
    {
        for(String name : this.contextMap.getAttributesContainingItems())
        {
            Object currentValue = this.contextMap.get(name);
            if(currentValue != null)
            {
                Object cacheBoundVersion = Utilities.getCacheBoundVersion(currentValue);
                Object withoutInvalidJaloItems = removeInvalidJaloItems(cacheBoundVersion);
                if(cacheBoundVersion instanceof Item && withoutInvalidJaloItems == null)
                {
                    LOG.warn("An item stored in a session is no longer valid. Removing the item.");
                    this.contextMap.remove(name);
                    continue;
                }
                if(cacheBoundVersion != currentValue || isCollectionAndSizeDiffers(cacheBoundVersion, withoutInvalidJaloItems))
                {
                    this.contextMap.putNoItemCheck(name, withoutInvalidJaloItems);
                }
            }
        }
    }


    private boolean isCollectionAndSizeDiffers(Object obj, Object obj2)
    {
        if(obj instanceof Collection && obj2 instanceof Collection)
        {
            return (((Collection)obj).size() != ((Collection)obj2).size());
        }
        if(obj instanceof Map && obj2 instanceof Map)
        {
            return (((Map)obj).size() != ((Map)obj2).size());
        }
        return false;
    }


    public <T> T getAttribute(String name)
    {
        return fixStaleItemsInValue(name, (T)this.contextMap.get(name));
    }


    protected <T> T fixStaleItemsInValue(String name, T currentValue)
    {
        if(currentValue != null && this.contextMap.isAttributeHoldingItems(name))
        {
            T cacheBoundVersion = (T)Utilities.getCacheBoundVersion(currentValue);
            if(cacheBoundVersion != currentValue)
            {
                T fixedValue = removeInvalidJaloItems(cacheBoundVersion);
                this.contextMap.putNoItemCheck(name, fixedValue);
                return fixedValue;
            }
        }
        return removeInvalidJaloItems(currentValue);
    }


    private <T> T removeInvalidJaloItems(T value)
    {
        if(value instanceof Item)
        {
            Item i = (Item)value;
            if(!i.isAlive())
            {
                LOG.warn("An item stored in a session is no longer valid. Returning null instead.");
                return null;
            }
            return i.isAlive() ? (T)i : null;
        }
        if((value instanceof java.util.List || value instanceof java.util.Set) && !((Collection)value).isEmpty())
        {
            Collection<?> newCollection = (value instanceof java.util.Set) ? new LinkedHashSet((Collection)value) : new ArrayList((Collection)value);
            Iterator<?> iterator = newCollection.iterator();
            while(iterator.hasNext())
            {
                Object currentObject = iterator.next();
                if(currentObject instanceof Item)
                {
                    Item currentItem = (Item)currentObject;
                    if(!currentItem.isAlive())
                    {
                        LOG.warn("An item stored in a session collection is no longer valid. Removing it from the collection.");
                        iterator.remove();
                    }
                }
            }
            return (T)newCollection;
        }
        if(value instanceof Map)
        {
            Map<Object, Object> newMap = new HashMap<>((Map<?, ?>)value);
            for(Map.Entry<Object, Object> entry : newMap.entrySet())
            {
                if(entry.getValue() instanceof Item)
                {
                    Item item = (Item)entry.getValue();
                    if(!item.isAlive())
                    {
                        LOG.warn("An item stored in a session map with a key '{}' is no longer valid. Removing it from the map.", entry
                                        .getKey());
                        newMap.remove(entry.getKey());
                    }
                }
            }
            return (T)newMap;
        }
        return value;
    }


    public Object setAttribute(String name, Object value)
    {
        Object previousValue;
        if(value == null)
        {
            previousValue = removeAttribute(name);
        }
        else
        {
            previousValue = this.contextMap.put(name, checkSpecialAttributes(name, value));
        }
        sendSessionAttrChangeNotification(name, value);
        return previousValue;
    }


    private final Object checkSpecialAttributes(String name, Object value)
    {
        if("currency".equalsIgnoreCase(name))
        {
            if(value == null)
            {
                throw new JaloInvalidParameterException("currency must not be null", 0);
            }
            if(this.session != null && this.session.hasCart())
            {
                if(!value.equals(this.contextMap.get("currency")))
                {
                    this.session.getCart().setCurrency((Currency)value);
                }
            }
            return value;
        }
        if("language".equalsIgnoreCase(name))
        {
            this.contextMap.put("language", value);
            if(value != null)
            {
                Locale newOne = ((Language)value).getLocale();
                if(newOne == null)
                {
                    newOne = Registry.hasCurrentTenant() ? Registry.getCurrentTenant().getTenantSpecificLocale() : null;
                }
                setLocale(newOne);
            }
            return value;
        }
        if("locale".equalsIgnoreCase(name))
        {
            if(value == null)
            {
                Language language = (Language)this.contextMap.get("language");
                Locale fallback = (language != null) ? language.getLocale() : null;
                if(fallback != null)
                {
                    return fallback;
                }
                if(Registry.hasCurrentTenant())
                {
                    return Registry.getCurrentTenant().getTenantSpecificLocale();
                }
                return null;
            }
            return value;
        }
        if("timezone".equalsIgnoreCase(name))
        {
            if(value != null)
            {
                return value;
            }
            return Registry.hasCurrentTenant() ? Registry.getCurrentTenant().getTenantSpecificTimeZone() : null;
        }
        if("timeoffset".equalsIgnoreCase(name))
        {
            this.contextMap.remove("currentdateValidTo");
            this.contextMap.remove("currentdate");
            if(value != null)
            {
                return value;
            }
            return ZERO_TIME_OFFSET;
        }
        return value;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Enumeration getAttributeNames()
    {
        return this.contextMap.keys();
    }


    public Collection<String> getAllAttributeNames()
    {
        return Collections.unmodifiableCollection(this.contextMap.keySet());
    }


    public void addAllAttributes(Map<String, ? extends Object> attributes)
    {
        if(attributes != null)
        {
            for(Map.Entry<String, ? extends Object> e : attributes.entrySet())
            {
                String key = e.getKey();
                Object value = e.getValue();
                if("language".equalsIgnoreCase(key) && value == null)
                {
                    continue;
                }
                if("currency".equalsIgnoreCase(key) && value == null)
                {
                    continue;
                }
                setAttribute(key, value);
            }
        }
    }


    public Object removeAttribute(String name)
    {
        checkSpecialAttributes(name, null);
        Object result = this.contextMap.remove(name);
        sendSessionAttrChangeNotification(name, null);
        return result;
    }


    public void setCurrentTime(Date timeInstance)
    {
        setAttribute("timeoffset", Long.valueOf(timeInstance.getTime() - getCurrentTimeMillis()));
    }


    protected long getCurrentTimeMillis()
    {
        return System.currentTimeMillis();
    }


    public Date getAdjustedCurrentTime()
    {
        return new Date(getAdjustedCurrentTimeMillis());
    }


    protected long getAdjustedCurrentTimeMillis()
    {
        Long timeOffset = (Long)this.contextMap.get("timeoffset");
        return getCurrentTimeMillis() + ((timeOffset == null) ? 0L : timeOffset.longValue());
    }


    public void setCurrentTimeSystem()
    {
        setAttribute("timeoffset", ZERO_TIME_OFFSET);
    }


    public Date getAdjustedCurrentDate()
    {
        return new Date(getAdjustedCurrentDateMillis());
    }


    protected long getAdjustedCurrentDateMillis()
    {
        Long timeOffset = (Long)this.contextMap.get("timeoffset");
        long now = getCurrentTimeMillis() + ((timeOffset == null) ? 0L : timeOffset.longValue());
        Long cachedDateValidTo = (Long)this.contextMap.get("currentdateValidTo");
        Long cachedCurrentDate = (Long)this.contextMap.get("currentdate");
        if(cachedCurrentDate == null || cachedDateValidTo == null || cachedDateValidTo.longValue() < now)
        {
            Calendar cal = Utilities.getDefaultCalendar();
            cal.setTimeInMillis(now);
            cal.set(14, 0);
            cal.set(13, 0);
            cal.set(12, 0);
            cal.set(10, 0);
            cal.set(9, 0);
            cachedCurrentDate = Long.valueOf(cal.getTimeInMillis());
            cal.roll(5, true);
            cachedDateValidTo = Long.valueOf(cal.getTimeInMillis());
            this.contextMap.put("currentdate", cachedCurrentDate);
            this.contextMap.put("currentdateValidTo", cachedDateValidTo);
        }
        return cachedCurrentDate.longValue();
    }


    private void sendSessionAttrChangeNotification(String attributeName, Object attributeValue)
    {
        if(this.session == null)
        {
            return;
        }
        for(JaloSessionListener l : JaloConnection.getInstance().getJaloSessionListeners())
        {
            l.afterSessionAttributeChange(this.session, attributeName, attributeValue);
        }
    }


    public static SessionContextAttributeSetter setSessionContextAttributesLocally(SessionContext context, Map<String, Object> contextAttributes)
    {
        return new SessionContextAttributeSetter(context, contextAttributes);
    }


    public SessionContextAttributeSetter setSessionContextAttributesLocally(Map<String, Object> contextAttributes)
    {
        return new SessionContextAttributeSetter(this, contextAttributes);
    }
}
