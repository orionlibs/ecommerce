package de.hybris.platform.core;

import com.google.common.base.Preconditions;
import de.hybris.platform.cache.Cache;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.JaloItemCacheUnit;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.Manager;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.persistence.EJBItemNotFoundException;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.PolyglotPersistenceGenericItemSupport;
import de.hybris.platform.persistence.SystemEJB;
import de.hybris.platform.persistence.polyglot.TypeInfoFactory;
import de.hybris.platform.persistence.polyglot.config.TypeInfo;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.ExposesRemote;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import de.hybris.platform.util.JaloPropertyContainerAdapter;
import de.hybris.platform.util.ObjectAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.log4j.Logger;

public abstract class WrapperFactory
{
    public static final String LAZYLOADITEMLIST_CHECK_HJMP_CACHE = "lazy.check.hjmp.cache";
    private static final Logger LOG = Logger.getLogger(WrapperFactory.class);
    private static HashMap itemWrapperCreator = new HashMap<>();
    private static HashMap objectWrapperCreator = new HashMap<>();


    public static void registerItemWrapperCreator(int typeCode, ItemWrapperCreator wrapper)
    {
        deregisterItemWrapperCreator(typeCode);
        itemWrapperCreator.put(Integer.valueOf(typeCode), wrapper);
    }


    public static void deregisterItemWrapperCreator(int typeCode)
    {
        itemWrapperCreator.remove(Integer.valueOf(typeCode));
    }


    public static ItemWrapperCreator getItemWrapperCreator(int typeCode)
    {
        return (ItemWrapperCreator)itemWrapperCreator.get(Integer.valueOf(typeCode));
    }


    public static boolean hasItemWrapperCreator(int typeCode)
    {
        return (itemWrapperCreator.get(Integer.valueOf(typeCode)) != null);
    }


    public static void registerObjectWrapperCreator(Class<?> srcClass, ObjectWrapperCreator wrapper)
    {
        deregisterObjectWrapperCreator(srcClass);
        objectWrapperCreator.put(srcClass, wrapper);
    }


    public static void deregisterObjectWrapperCreator(Class srcClass)
    {
        objectWrapperCreator.remove(srcClass);
    }


    public static ObjectWrapperCreator getObjectWrapperCreator(Class srcClass)
    {
        return (ObjectWrapperCreator)objectWrapperCreator.get(srcClass);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static Object wrap(Object object)
    {
        return wrap(null, Registry.getCurrentTenant().getCache(), object, null);
    }


    public static Object wrap(Cache cache, Object object)
    {
        return wrap(null, cache, object, null);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public static Object wrap(SessionContext ctx, Object object, ItemPropertyWrappingListener listener)
    {
        return wrap(ctx, Registry.getCurrentTenant().getCache(), object, listener);
    }


    public static Object wrap(SessionContext ctx, Cache cache, Object object, ItemPropertyWrappingListener listener)
    {
        if(object == null)
        {
            return null;
        }
        if(object instanceof ItemPropertyValue)
        {
            return wrap(cache, (ItemPropertyValue)object, listener);
        }
        if(object instanceof ItemRemote)
        {
            PK pk = EJBTools.getPK((ItemRemote)object);
            return getCachedItem(cache, pk);
        }
        if(object instanceof de.hybris.platform.persistence.ManagerEJB)
        {
            LOG.info("Stack trace", new Exception());
            throw new RuntimeException();
        }
        ObjectWrapperCreator wrapper = objectWrapperCreator.isEmpty() ? null : (ObjectWrapperCreator)objectWrapperCreator.get(object.getClass());
        if(wrapper != null)
        {
            return wrapper.create(object);
        }
        if(object instanceof Collection)
        {
            return wrap(ctx, cache, (Collection)object, listener);
        }
        if(object instanceof Map)
        {
            return wrap(ctx, cache, (Map)object, listener);
        }
        return object;
    }


    private static final Item wrap(Cache cache, ItemPropertyValue ipv, ItemPropertyWrappingListener listener)
    {
        PK pk = ipv.getPK();
        try
        {
            return getCachedItem(cache, pk);
        }
        catch(JaloItemNotFoundException e)
        {
            LOG.debug(e);
            if(listener != null)
            {
                listener.propertyValueWasInvalid(ipv);
            }
            return null;
        }
    }


    public static Set<PK> getPrefetchLanguages(SessionContext ctx)
    {
        Set<Language> languages = (ctx != null) ? (Set<Language>)ctx.getAttribute("session.prefetch.languages") : null;
        if(languages == null)
        {
            Language current = (ctx != null) ? ctx.getLanguage() : (JaloSession.hasCurrentSession() ? JaloSession.getCurrentSession().getSessionContext().getLanguage() : null);
            languages = (current != null) ? Collections.<Language>singleton(current) : Collections.EMPTY_SET;
        }
        if(languages.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<PK> ret = new HashSet<>(languages.size());
        for(Language l : languages)
        {
            ret.add(l.getPK());
        }
        return ret;
    }


    public static final Collection<Item> getCachedItems(Cache cache, Collection<PK> pks, Set<PK> prefetchLanguagePKs, boolean ignoreMissing) throws JaloItemNotFoundException
    {
        return getCachedItems(cache, pks, prefetchLanguagePKs, ignoreMissing, false);
    }


    public static final Collection<Item> getCachedItems(Cache cache, Collection<PK> pks, Set<PK> prefetchLanguagePKs, boolean ignoreMissing, boolean returnMissingAsNull) throws JaloItemNotFoundException
    {
        ArrayList<ComputingJaloItemCacheUnit> cacheUnits = (ArrayList<ComputingJaloItemCacheUnit>)pks.stream().map(pk -> (pk == null) ? null : new ComputingJaloItemCacheUnit(cache, pk)).collect(Collectors.toCollection(() -> new ArrayList(pks.size())));
        List<PK> uncachedPKs = (List<PK>)cacheUnits.stream().filter(Objects::nonNull).filter(ComputingJaloItemCacheUnit::isUncached).map(JaloItemCacheUnit::getPk).collect(Collectors.toList());
        Map<PK, ItemImplCreationResult> implementationsForUncachedPKs = getItemImplForPKs(cache.getTenant(), uncachedPKs);
        Set<PK> missingPKs = ignoreMissing ? Collections.<PK>emptySet() : new HashSet<>(1 + pks.size() / 2);
        ArrayList<Item> result = new ArrayList<>(pks.size());
        for(JaloItemCacheUnit cUnit : cacheUnits)
        {
            Item item = null;
            PK pk = null;
            if(cUnit != null)
            {
                PrecomputedJaloItemCacheUnit precomputedJaloItemCacheUnit;
                pk = cUnit.getPk();
                JaloItemCacheUnit cUnitToUse = cUnit;
                ItemImplCreationResult possibleImpl = implementationsForUncachedPKs.get(pk);
                if(possibleImpl != null && possibleImpl.isFailed())
                {
                    throw possibleImpl.toJaloSystemException();
                }
                if(possibleImpl != null && possibleImpl.hasItemImpl())
                {
                    precomputedJaloItemCacheUnit = new PrecomputedJaloItemCacheUnit(cache, possibleImpl.toItemImpl());
                }
                try
                {
                    item = (Item)precomputedJaloItemCacheUnit.getCached();
                }
                catch(JaloItemNotFoundException e)
                {
                    LOG.debug(e);
                    item = null;
                }
            }
            if(item != null)
            {
                result.add(item);
                continue;
            }
            if(returnMissingAsNull)
            {
                result.add((Item)null);
            }
            if(!ignoreMissing && pk != null)
            {
                missingPKs.add(pk);
            }
        }
        if(!missingPKs.isEmpty())
        {
            JaloItemNotFoundException exception = new JaloItemNotFoundException("Couldn't instantiate item for given PKs " + missingPKs, 0);
            exception.setInvalidPKs(missingPKs);
            throw exception;
        }
        return result;
    }


    public static final Item getCachedItem(Cache cache, PK pk) throws JaloItemNotFoundException
    {
        try
        {
            return (Item)(new ComputingJaloItemCacheUnit(cache, pk)).getCached();
        }
        catch(JaloInternalException e)
        {
            throw new JaloItemNotFoundException(e, 0);
        }
    }


    public static final Item rewrap(Item item)
    {
        (new Object(item.getTenant().getCache(), item))
                        .invalidate(2);
        return (Item)wrap(item.getTenant().getCache(), new ItemPropertyValue(item.getPK()));
    }


    private static final Map wrap(SessionContext ctx, Cache cache, Map map, ItemPropertyWrappingListener listener)
    {
        return wrap(ctx, cache, map, listener, null);
    }


    private static final Map wrap(SessionContext ctx, Cache cache, Map map, ItemPropertyWrappingListener listener, ItemPropertyWrapper dwr)
    {
        Map<Object, Object> ret;
        if(map.equals(Collections.EMPTY_MAP))
        {
            return map;
        }
        if(map instanceof HashMap)
        {
            ret = new HashMap<>();
        }
        else if(map instanceof SortedMap)
        {
            Comparator comp = ((SortedMap)map).comparator();
            ret = (comp != null) ? new TreeMap<>((Comparator<?>)new WrapperComparator(cache, comp)) : new TreeMap<>();
        }
        else
        {
            ret = new HashMap<>();
        }
        if(dwr == null && Config.getBoolean("unwrap.use.collection.finder", true))
        {
            dwr = new ItemPropertyWrapper(ctx, cache, map, listener);
        }
        for(Map.Entry entry : map.entrySet())
        {
            Object wrappedKey = wrapDelayed(ctx, cache, entry.getKey(), listener, dwr);
            if(entry.getKey() == null || wrappedKey != null)
            {
                Preconditions.checkArgument(!(wrappedKey instanceof ItemRemote), "wrapped item " + wrappedKey + " is still instance of ItemRemote");
                Object wrappedValue = wrapDelayed(ctx, cache, entry.getValue(), listener, dwr);
                Preconditions.checkArgument(!(wrappedValue instanceof ItemRemote), "wrapped item " + wrappedValue + " is still instance of ItemRemote");
                ret.put(wrappedKey, wrappedValue);
            }
        }
        return ret;
    }


    private static final Collection wrap(SessionContext ctx, Cache cache, Collection coll, ItemPropertyWrappingListener listener)
    {
        return wrap(ctx, cache, coll, listener, null);
    }


    private static final Collection wrap(SessionContext ctx, Cache cache, Collection coll, ItemPropertyWrappingListener listener, ItemPropertyWrapper dwr)
    {
        Collection<Object> ret;
        if(coll instanceof LazyLoadItemList || coll instanceof LazyLoadItemSet)
        {
            return coll;
        }
        if(coll instanceof ItemPropertyValueCollection)
        {
            Comparator comp;
            switch(((ItemPropertyValueCollection)coll).getWrapedCollectionType())
            {
                case 3:
                    comp = ((SortedSet)coll).comparator();
                    ret = (comp != null) ? new TreeSet((Comparator<?>)new WrapperComparator(cache, comp)) : new TreeSet();
                    break;
                case 2:
                    ret = new LinkedHashSet(coll.size());
                    break;
                default:
                    ret = new ArrayList(coll.size());
                    break;
            }
        }
        else if(coll instanceof SortedSet)
        {
            Comparator comp = ((SortedSet)coll).comparator();
            ret = (comp != null) ? new TreeSet((Comparator<?>)new WrapperComparator(cache, comp)) : new TreeSet();
        }
        else if(coll instanceof ArrayList)
        {
            ret = new ArrayList(coll.size());
        }
        else if(coll instanceof LinkedList)
        {
            ret = new LinkedList();
        }
        else if(coll instanceof Set)
        {
            ret = new LinkedHashSet(coll.size());
        }
        else
        {
            ret = new ArrayList(coll.size());
        }
        if(dwr == null && Config.getBoolean("unwrap.use.collection.finder", true))
        {
            dwr = new ItemPropertyWrapper(ctx, cache, coll, listener);
        }
        for(Object value : coll)
        {
            Object wrapped = wrapDelayed(ctx, cache, value, listener, dwr);
            if(value == null || wrapped != null)
            {
                if(wrapped instanceof ItemRemote)
                {
                    throw new IllegalArgumentException("wrapped item " + wrapped + " is still instance of ItemRemote");
                }
                ret.add(wrapped);
            }
        }
        return ret;
    }


    private static final Object wrapDelayed(SessionContext ctx, Cache cache, Object value, ItemPropertyWrappingListener listener, ItemPropertyWrapper dwr)
    {
        if(dwr != null)
        {
            if(value instanceof ItemPropertyValue)
            {
                return dwr.get((ItemPropertyValue)value);
            }
            if(value instanceof Collection)
            {
                return wrap(ctx, cache, (Collection)value, listener, dwr);
            }
            if(value instanceof Map)
            {
                return wrap(ctx, cache, (Map)value, listener, dwr);
            }
            return wrap(ctx, cache, value, listener);
        }
        return wrap(ctx, cache, value, listener);
    }


    public static Object unwrap(Cache cache, Object object)
    {
        return unwrap(cache, object, false);
    }


    public static Object unwrap(Cache cache, Object object, boolean itemsAsPropertyValue)
    {
        return unwrap(cache, object, itemsAsPropertyValue, false);
    }


    private static Object unwrap(Cache cache, Object object, boolean itemsAsPropertyValue, boolean isNested)
    {
        if(object == null)
        {
            return null;
        }
        if(object instanceof de.hybris.platform.util.BridgeAbstraction)
        {
            Item item = (Item)object;
            Item.ItemImpl itemImpl = item.getImplementation();
            try
            {
                if(itemImpl instanceof ExposesRemote && !itemsAsPropertyValue)
                {
                    return ((ExposesRemote)itemImpl).getRemote();
                }
                if(itemsAsPropertyValue)
                {
                    return new ItemPropertyValue(((Item)object).getPK());
                }
                throw new UnsupportedOperationException(String.format("Cannot unwrap the Remote. '%s' doesn't expose the Remote.", new Object[] {itemImpl
                                .getClass().getSimpleName()}));
            }
            catch(ClassCastException e)
            {
                LOG.debug(e);
                throw new JaloInternalException(null, "WrapperFactory.wrap: BridgeAbstraction.getImplementation() MUST be a EJBWrapper.", 0);
            }
        }
        if(object instanceof JaloPropertyContainerAdapter)
        {
            return ((JaloPropertyContainerAdapter)object).unwrap();
        }
        if(object instanceof PK)
        {
            return object;
        }
        if(object instanceof Map)
        {
            if(((Map)object).isEmpty())
            {
                return Collections.EMPTY_MAP;
            }
            Map<Object, Object> ret = new HashMap<>();
            for(Iterator<Map.Entry> it = ((Map)object).entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = it.next();
                ret.put(unwrap(cache, entry.getKey(), itemsAsPropertyValue, true),
                                unwrap(cache, entry.getValue(), itemsAsPropertyValue, true));
            }
            return ret;
        }
        if(object instanceof Collection)
        {
            Collection<Object> tmp, ret;
            boolean ipvcAllowed = (itemsAsPropertyValue && !isNested);
            if(object instanceof List)
            {
                if(((List)object).isEmpty())
                {
                    return Collections.EMPTY_LIST;
                }
                tmp = new ArrayList(((List)object).size());
            }
            else if(object instanceof Set)
            {
                if(((Set)object).isEmpty())
                {
                    return Collections.EMPTY_SET;
                }
                tmp = new HashSet(((Set)object).size());
            }
            else
            {
                tmp = new ArrayList();
            }
            for(Iterator it = ((Collection)object).iterator(); it.hasNext(); )
            {
                Object value = unwrap(cache, it.next(), itemsAsPropertyValue, true);
                ipvcAllowed &= value instanceof ItemPropertyValue;
                tmp.add(value);
            }
            if(ipvcAllowed && !tmp.isEmpty())
            {
                ItemPropertyValueCollection itemPropertyValueCollection = new ItemPropertyValueCollection(tmp);
                itemPropertyValueCollection.setWrapedCollectionType(
                                ItemPropertyValueCollection.getWrapedTypeOfCollection((Collection)object));
            }
            else
            {
                ret = tmp;
            }
            return ret;
        }
        if(object instanceof ObjectAdapter)
        {
            return ((ObjectAdapter)object).getObject();
        }
        if(object instanceof Manager && !itemsAsPropertyValue)
        {
            return ((Manager)object).getRemote();
        }
        return object;
    }


    private static Item.ItemImpl getItemImplForPK(Tenant tenant, PK pk)
    {
        Map<PK, ItemImplCreationResult> implementations = getItemImplForPKs(tenant, Collections.singletonList(pk));
        ItemImplCreationResult result = implementations.get(pk);
        if(!result.hasItemImpl())
        {
            throw result.toJaloSystemException();
        }
        return result.toItemImpl();
    }


    private static Map<PK, ItemImplCreationResult> getItemImplForPKs(Tenant tenant, Collection<PK> pks)
    {
        HashMap<PK, ItemImplCreationResult> result = new HashMap<>(pks.size());
        Map<Integer, Set<PK>> typeCodeToPKs = SystemEJB.groupByTypeCode(pks);
        typeCodeToPKs.forEach((typeCode, setOfPKs) -> fillFromPersistenceLayer(tenant, typeCode, setOfPKs, result));
        return result;
    }


    private static void fillFromPersistenceLayer(Tenant tenant, Integer typeCode, Set<PK> pks, HashMap<PK, ItemImplCreationResult> result)
    {
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfo(typeCode.intValue());
        if(PolyglotPersistenceGenericItemSupport.isFullyBackedByTheEJBPersistence(tenant, typeInfo) && pks.size() > 1)
        {
            fillFromEJB(tenant, typeCode, pks, result);
        }
        else
        {
            fillItemByItem(tenant, typeCode, pks, result);
        }
    }


    private static void fillItemByItem(Tenant tenant, Integer typeCode, Set<PK> setOfPKs, HashMap<PK, ItemImplCreationResult> result)
    {
        ItemWrapperCreator wrapperCreator = getMatchingWrapperCreator(tenant.getPersistenceManager(), typeCode.intValue());
        setOfPKs.forEach(pk -> result.put(pk, wrapperCreator.create(tenant, pk)));
    }


    private static void fillFromEJB(Tenant tenant, Integer typeCode, Set<PK> pks, HashMap<PK, ItemImplCreationResult> result)
    {
        ItemWrapperCreator wrapperCreator = getMatchingWrapperCreator(tenant.getPersistenceManager(), typeCode.intValue());
        try
        {
            tenant.getSystemEJB().findRemoteObjectsByPK(pks, Collections.emptySet(), true).forEach((pk, remote) -> {
                Item.ItemImpl impl = wrapperCreator.create(tenant, remote);
                result.put(pk, ItemImplCreationResult.existing(impl));
            });
        }
        catch(EJBItemNotFoundException | de.hybris.platform.persistence.EJBInvalidParameterException e)
        {
            throw new JaloInternalException(e);
        }
    }


    private static ItemWrapperCreator getMatchingWrapperCreator(PersistenceManager persistenceManager, int typeCode)
    {
        ItemWrapperCreator wrapper = getItemWrapperCreator(typeCode);
        if(wrapper == null)
        {
            ItemDeployment myDepl = persistenceManager.getItemDeployment(typeCode);
            if(myDepl == null)
            {
                throw new IllegalArgumentException("Unknown typecode " + typeCode);
            }
            if(myDepl.isGeneric())
            {
                ItemDeployment depl = persistenceManager.getItemDeployment(myDepl
                                .getSuperDeploymentName());
                for(; wrapper == null && depl != null; depl = persistenceManager.getItemDeployment(depl.getSuperDeploymentName()))
                {
                    wrapper = getItemWrapperCreator(depl.getTypeCode());
                }
            }
        }
        if(wrapper == null)
        {
            throw new JaloInternalException(null, "No ItemRemote<->ItemEJBImpl Wrapper installed for typecode " + typeCode + ".", 0);
        }
        return wrapper;
    }
}
