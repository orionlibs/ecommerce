package de.hybris.platform.jalo.c2l;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.util.localization.Localization;
import java.io.ObjectStreamException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class C2LManager extends Manager
{
    public static final String BEAN_NAME = "core.c2lManager";
    private static final String LOC_REMOVE_ACTIVE_LANG = "exception.core.c2lmanager.remactivelang";
    private static final String LOC_REMOVE_ACTIVE_CURR = "exception.core.c2lmanager.remactivecurr";
    private static final String LOC_REMOVE_BASE_CURR = "exception.core.c2lmanager.rembasecurr";
    private static final C2LManagerInvalidationListener invalidationListener = new C2LManagerInvalidationListener();


    public static C2LManager getInstance()
    {
        return Registry.getCurrentTenant().getJaloConnection().getC2LManager();
    }


    public void init()
    {
        registerInvalidationListener();
    }


    private void registerInvalidationListener()
    {
        InvalidationTopic topic = InvalidationManager.getInstance().getInvalidationTopic((Object[])new String[] {Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY});
        topic.addInvalidationListener((InvalidationListener)invalidationListener);
    }


    @Deprecated(since = "ages", forRemoval = false)
    protected void checkBeforeItemRemoval(SessionContext ctx, Item item) throws ConsistencyCheckException
    {
        if(item instanceof Currency)
        {
            Currency citem = (Currency)item;
            if(citem.isActive().booleanValue() && getActiveCurrencies().size() == 1)
            {
                throw new ConsistencyCheckException(Localization.getLocalizedString("exception.core.c2lmanager.remactivecurr", new String[] {citem
                                .getIsoCode()}), 0);
            }
            if(citem.isBase().booleanValue())
            {
                throw new ConsistencyCheckException(Localization.getLocalizedString("exception.core.c2lmanager.rembasecurr", new String[] {citem
                                .getIsoCode()}), 0);
            }
        }
        else if(item instanceof Language)
        {
            Language citem = (Language)item;
            if(citem.isActive().booleanValue())
            {
                throw new ConsistencyCheckException(Localization.getLocalizedString("exception.core.c2lmanager.remactivelang", new String[] {citem
                                .getIsoCode()}), 0);
            }
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Language getLanguageByIsoCode(String iso) throws JaloItemNotFoundException
    {
        return (Language)(new Object(this, getTenant().getCache(), "LANGUAGEBYISO_" + iso, iso))
                        .getCached();
    }


    private String convertLanguageCode(String oldCode)
    {
        String languageCode = oldCode.toLowerCase();
        if("iw".equals(languageCode))
        {
            languageCode = "he";
        }
        else if("ji".equals(languageCode))
        {
            languageCode = "yi";
        }
        else if("in".equals(languageCode))
        {
            languageCode = "id";
        }
        return languageCode;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Language createLanguage(String isoCode) throws ConsistencyCheckException
    {
        return createLanguage(null, isoCode);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Language createLanguage(PK pk, String isoCode) throws ConsistencyCheckException
    {
        try
        {
            return (Language)ComposedType.newInstance(getSession().getSessionContext(), Language.class, new Object[] {Item.PK, pk, "isocode", isoCode});
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<Language> getAllLanguages()
    {
        return (Set<Language>)(new Object(this, getTenant().getCache(), "ALLLANGUAGES"))
                        .getCached();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<Language> getActiveLanguages()
    {
        return (Set<Language>)(new Object(this, getTenant().getCache(), "ALLACTIVELANGUAGES"))
                        .getCached();
    }


    public Language getDefaultLanguageForTenant(Tenant t)
    {
        return (Language)(new Object(this, getTenant().getCache(), "DEFLANG", t))
                        .getCached();
    }


    public Currency getDefaultCurrencyForTenant(Tenant t)
    {
        return (Currency)(new Object(this, getTenant().getCache(), "DEFCURR"))
                        .getCached();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Currency getCurrencyByIsoCode(String iso) throws JaloItemNotFoundException
    {
        String isoUpper = iso.toUpperCase();
        return (Currency)(new Object(this, getTenant().getCache(), "CURRENCYBYISO_" + isoUpper, isoUpper, iso))
                        .getCached();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Currency createCurrency(String isoCode) throws ConsistencyCheckException
    {
        return createCurrency(null, isoCode);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Currency createCurrency(PK pkBase, String isoCode) throws ConsistencyCheckException
    {
        try
        {
            return (Currency)ComposedType.newInstance(getSession().getSessionContext(), Currency.class, new Object[] {Item.PK, pkBase, "isocode", isoCode});
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<Currency> getAllCurrencies()
    {
        return (Set<Currency>)(new Object(this, getTenant().getCache(), "ALLCURRENCIES"))
                        .getCached();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<Currency> getActiveCurrencies()
    {
        return (Set<Currency>)(new Object(this, getTenant().getCache(), "ALLACTIVECURRENCIES"))
                        .getCached();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Currency getBaseCurrency()
    {
        ImmutableMap immutableMap = ImmutableMap.of("base", Boolean.TRUE);
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {Currency} WHERE {base} = ?base", (Map)immutableMap,
                        Collections.singletonList(Currency.class), false, true, 0, -1);
        Collection<Currency> coll = res.getResult();
        if(!coll.isEmpty())
        {
            return coll.iterator().next();
        }
        return null;
    }


    @Deprecated(since = "ages", forRemoval = false)
    public void setBaseCurrency(Currency currency)
    {
        currency.setBase();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Country getCountryByIsoCode(String iso) throws JaloItemNotFoundException
    {
        ImmutableMap immutableMap = ImmutableMap.of("isocode", iso);
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {Country} WHERE {isocode} = ?isocode", (Map)immutableMap,
                        Collections.singletonList(Country.class), false, true, 0, -1);
        Collection<Country> coll = res.getResult();
        if(coll.size() < 1)
        {
            throw new JaloItemNotFoundException("country with isocode '" + iso + "' not found.", 0);
        }
        return coll.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Country createCountry(String isoCode) throws ConsistencyCheckException
    {
        return createCountry(null, isoCode);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Country createCountry(PK pkBase, String isoCode) throws ConsistencyCheckException
    {
        try
        {
            return (Country)ComposedType.newInstance(getSession().getSessionContext(), Country.class, new Object[] {Item.PK, pkBase, "isocode", isoCode});
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<Country> getAllCountries()
    {
        return new HashSet<>(getSession()
                        .getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {Country}", null,
                                        Collections.singletonList(Currency.class), true, true, 0, -1)
                        .getResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<Country> getActiveCountries()
    {
        return new HashSet<>(getSession()
                        .getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {Country} WHERE {active} = ?active",
                                        Collections.singletonMap("active", Boolean.TRUE),
                                        Collections.singletonList(Country.class), true, true, 0, -1)
                        .getResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Region getRegionByCode(String code) throws JaloItemNotFoundException, JaloSystemException
    {
        Collection<Region> coll = getRegionsByCode(code);
        if(coll.isEmpty())
        {
            throw new JaloItemNotFoundException("No region with code '" + code + "'!", 0);
        }
        if(coll.size() > 1)
        {
            throw new JaloSystemException(null, "Code '" + code + "' is not unique!", 0);
        }
        return coll.iterator().next();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Region> getRegionsByCode(String code)
    {
        ImmutableMap immutableMap = ImmutableMap.of("isocode", code);
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {Region} WHERE {isocode} = ?isocode", (Map)immutableMap,
                        Collections.singletonList(Region.class), true, true, 0, -1);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Region getRegionByCode(Country country, String code) throws JaloItemNotFoundException
    {
        ImmutableMap immutableMap = ImmutableMap.of("isocode", code, "country", country);
        SearchResult res = getSession().getFlexibleSearch().search("SELECT {" + Item.PK + "} FROM {Region} WHERE {isocode} = ?isocode AND {country} = ?country", (Map)immutableMap,
                        Collections.singletonList(Region.class), true, true, 0, -1);
        Collection<Region> coll = res.getResult();
        if(!coll.isEmpty())
        {
            return coll.iterator().next();
        }
        throw new JaloItemNotFoundException("Region with code " + code + " for country " + country + " not found", 0);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Region createRegion(String code, Country country) throws ConsistencyCheckException
    {
        return createRegion(null, code, country);
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Region createRegion(PK pkBase, String code, Country country) throws ConsistencyCheckException
    {
        try
        {
            return (Region)ComposedType.newInstance(getSession().getSessionContext(), Region.class, new Object[] {Item.PK, pkBase, "isocode", code, "country", country});
        }
        catch(JaloGenericCreationException e)
        {
            JaloGenericCreationException jaloGenericCreationException1;
            Throwable cause = e.getCause();
            if(cause == null)
            {
                jaloGenericCreationException1 = e;
            }
            if(jaloGenericCreationException1 instanceof RuntimeException)
            {
                throw (RuntimeException)jaloGenericCreationException1;
            }
            if(jaloGenericCreationException1 instanceof ConsistencyCheckException)
            {
                throw (ConsistencyCheckException)jaloGenericCreationException1;
            }
            throw new JaloSystemException(jaloGenericCreationException1);
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e);
        }
        catch(JaloItemNotFoundException e)
        {
            throw new JaloSystemException(e);
        }
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Collection<Region> getRegions(Country country)
    {
        return country.getRegions();
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<Region> getAllRegions()
    {
        return new HashSet<>(getSession().getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {Region}", null,
                                        Collections.singletonList(Region.class), true, true, 0, -1)
                        .getResult());
    }


    @Deprecated(since = "ages", forRemoval = false)
    public Set<Region> getActiveRegions()
    {
        return new HashSet<>(getSession()
                        .getFlexibleSearch()
                        .search("SELECT {" + Item.PK + "} FROM {Region} WHERE {active} = ?active",
                                        Collections.singletonMap("active", Boolean.TRUE),
                                        Collections.singletonList(Region.class), true, true, 0, -1)
                        .getResult());
    }


    public Object writeReplace() throws ObjectStreamException
    {
        return new C2LManagerSerializableDTO(getTenant());
    }


    protected void notifyItemRemoval(SessionContext ctx, Item item)
    {
    }
}
