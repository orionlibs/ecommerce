package de.hybris.platform.servicelayer.internal.i18n.impl;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.servicelayer.internal.i18n.LocalizationService;
import de.hybris.platform.servicelayer.internal.service.AbstractService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.util.Utilities;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated(since = "ages", forRemoval = true)
public class DefaultLocalizationService extends AbstractService implements LocalizationService, InvalidationListener
{
    private static final String LANGUAGE_TYPECODE = String.valueOf(32);
    private final Map<String, Locale> localeCache = new ConcurrentHashMap<>();
    private final Map<PK, Locale> localeLanguageCache = new ConcurrentHashMap<>();
    private final Map<Locale, Locale[]> localeMatchKeyCache = (Map)new ConcurrentHashMap<>();


    public Locale getLocaleByString(String isoCodes)
    {
        Locale ret = this.localeCache.get(isoCodes);
        if(ret == null)
        {
            String[] loc = Utilities.parseLocaleCodes(isoCodes);
            ret = new Locale(loc[0], loc[1], loc[2]);
            this.localeCache.put(isoCodes, ret);
        }
        return ret;
    }


    protected Locale getLocaleByLanguage(Language lang)
    {
        PK pk = lang.getPK();
        Locale ret = this.localeLanguageCache.get(pk);
        if(ret == null)
        {
            this.localeLanguageCache.put(pk, ret = getLocaleByString(lang.getIsoCode()));
        }
        return ret;
    }


    public Locale getCurrentLocale()
    {
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        Locale loc = ctx.getLocale();
        if(loc == null)
        {
            Language lang = ctx.getLanguage();
            if(lang != null)
            {
                loc = getLocaleByLanguage(lang);
                ctx.setLocale(loc);
            }
        }
        return loc;
    }


    public void setCurrentLocale(Locale loc)
    {
        ServicesUtil.validateParameterNotNull(loc, "locale was null");
        Locale currentLocale = getCurrentLocale();
        SessionContext ctx = JaloSession.getCurrentSession().getSessionContext();
        if((currentLocale != loc && (currentLocale == null || !currentLocale.equals(loc))) || (currentLocale != null && ctx
                        .getLanguage() == null))
        {
            DataLocale dataloc = matchDataLocale(loc, true);
            ctx.setLanguage(dataloc.getLang());
            ctx.setLocale(loc);
        }
    }


    public Locale getCurrentDataLocale()
    {
        try
        {
            return getLocaleByLanguage(JaloSession.getCurrentSession().getSessionContext().getLanguage());
        }
        catch(NullPointerException e)
        {
            throw new IllegalStateException("no current data locale set");
        }
    }


    public Locale getDataLocale(Locale loc)
    {
        return matchDataLocale(loc, true).getLoc();
    }


    public String getDataLanguageIsoCode(Locale locale)
    {
        return matchDataLocale(locale, true).getIsoCode();
    }


    public Locale[] getAllLocales(Locale loc)
    {
        Locale[] ret;
        DataLocale dataloc = matchDataLocale(loc, false);
        if(dataloc == null)
        {
            return new Locale[] {loc};
        }
        Locale[] fallbacks = dataloc.getFallbacks();
        if(dataloc.getLoc().equals(loc))
        {
            ret = new Locale[fallbacks.length + 1];
            ret[0] = loc;
            if(fallbacks.length > 0)
            {
                System.arraycopy(fallbacks, 0, ret, 1, fallbacks.length);
            }
        }
        else
        {
            ret = new Locale[fallbacks.length + 2];
            ret[0] = loc;
            ret[1] = dataloc.getLoc();
            if(fallbacks.length > 0)
            {
                System.arraycopy(fallbacks, 0, ret, 2, fallbacks.length);
            }
        }
        return ret;
    }


    public Locale[] getFallbackLocales(Locale loc)
    {
        DataLocale dataloc = matchDataLocale(loc, false);
        return (dataloc == null) ? null : dataloc.getFallbacks();
    }


    protected Locale[] getLocaleMatchKey(Locale requested)
    {
        Locale[] ret = this.localeMatchKeyCache.get(requested);
        if(ret == null)
        {
            List<Locale> all = new ArrayList<>(3);
            all.add(requested);
            if(requested.getVariant().length() > 0)
            {
                all.add(getLocaleByString(requested.getLanguage() + "_" + requested.getLanguage()));
            }
            if(requested.getCountry().length() > 0)
            {
                all.add(getLocaleByString(requested.getLanguage()));
            }
            this.localeMatchKeyCache.put(requested, ret = all.toArray(new Locale[all.size()]));
        }
        return ret;
    }


    public PK getMatchingPkForDataLocale(Locale locale)
    {
        DataLocale dataLocale = matchDataLocale(locale, true);
        return dataLocale.getLangPK();
    }


    protected DataLocale matchDataLocale(Locale loc, boolean throwError)
    {
        Map<Locale, DataLocale> availableLocales = getSupportedLocales();
        Locale[] keys = getLocaleMatchKey(loc);
        for(Locale key : keys)
        {
            DataLocale match = availableLocales.get(key);
            if(match != null)
            {
                return match;
            }
        }
        if(throwError)
        {
            throw new IllegalArgumentException("No matching DataLocale for " + loc + " ( tried " + Arrays.toString(keys) + " on available locales " + availableLocales + " )");
        }
        return null;
    }


    public Set<Locale> getSupportedDataLocales()
    {
        return Collections.unmodifiableSet(getSupportedLocales().keySet());
    }


    protected Map<Locale, DataLocale> getSupportedLocales()
    {
        return (Map<Locale, DataLocale>)(new Object(this, Registry.getCurrentTenant().getCache(), 32, "supportedLocales_" +
                        JaloSession.getCurrentSession()
                                        .getUser()
                                        .getPK()))
                        .getCached();
    }


    public boolean isLocalizationFallbackEnabled()
    {
        return (Boolean.TRUE.equals(JaloSession.getCurrentSession().getAttribute("enable.language.fallback.serviceLayer")) && Boolean.TRUE
                        .equals(JaloSession.getCurrentSession().getAttribute("enable.language.fallback")));
    }


    public void setLocalizationFallbackEnabled(boolean enabled)
    {
        JaloSession.getCurrentSession().setAttribute("enable.language.fallback.serviceLayer",
                        Boolean.valueOf(enabled));
        JaloSession.getCurrentSession().setAttribute("enable.language.fallback", Boolean.valueOf(enabled));
    }


    public void afterPropertiesSet() throws Exception
    {
        super.afterPropertiesSet();
        InvalidationTopic topic = InvalidationManager.getInstance().getOrCreateInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY, LANGUAGE_TYPECODE});
        topic.addInvalidationListener(this);
    }


    public void keyInvalidated(Object[] key, int invalidationType, InvalidationTarget target, RemoteInvalidationSource remoteSrc)
    {
        if(invalidationType == 1 || invalidationType == 2)
        {
            this.localeLanguageCache.clear();
        }
    }
}
