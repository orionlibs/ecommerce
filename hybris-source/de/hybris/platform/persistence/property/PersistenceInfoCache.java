package de.hybris.platform.persistence.property;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.regioncache.ConcurrentHashSet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class PersistenceInfoCache implements Serializable
{
    static final long serialVersionUID = -2650015321638652779L;
    private static final int INITIAL_TYPE_COUNT = 20000;
    private final String systemID;
    private final long timeStamp;
    private final Map<Object, TypeInfoMap> persistenceInfoCacheMap;
    private final ConcurrentMap<PK, Collection<PK>> superTypeRelationCache;
    private final Set<String> rootRelationTypes;
    private final Map<PK, Set<PK>> externalTableTypesCache;
    private final Map<PK, Set<Integer>> typePKToTCsCacheMap;


    PersistenceInfoCache(String systemID, long ts)
    {
        this.systemID = systemID;
        this.timeStamp = ts;
        this.persistenceInfoCacheMap = new ConcurrentHashMap<>(20000);
        this.externalTableTypesCache = new ConcurrentHashMap<>(20000);
        this.superTypeRelationCache = new ConcurrentHashMap<>(12000);
        this.typePKToTCsCacheMap = new ConcurrentHashMap<>(20000);
        this.rootRelationTypes = (Set<String>)new ConcurrentHashSet(10000);
    }


    Set<Integer> getCachedBeanTypecodes(PK typePK)
    {
        return this.typePKToTCsCacheMap.get(typePK);
    }


    void putBeanTypeCodes(PK typePK, Set<Integer> typeCodes)
    {
        this.typePKToTCsCacheMap.put(typePK, new HashSet<>(typeCodes));
    }


    Set<PK> getCachedExternalTableTypes(PK typePK)
    {
        return (typePK != null) ? this.externalTableTypesCache.get(typePK) : null;
    }


    void putExternalTableTypes(PK typePK, Set<PK> externalTableTypes)
    {
        if(typePK != null)
        {
            this.externalTableTypesCache.put(typePK, new HashSet<>(externalTableTypes));
        }
    }


    boolean isCachedRootRelationType(String code)
    {
        return this.rootRelationTypes.contains(code.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    Collection<PK> getCachedSubtypes(PK typePK)
    {
        Collection<PK> subtypes = this.superTypeRelationCache.get(typePK);
        return (subtypes != null && !subtypes.isEmpty()) ? Collections.<PK>unmodifiableCollection(subtypes) : Collections.EMPTY_SET;
    }


    TypeInfoMap getCachedInfo(Object typePKorCode)
    {
        return this.persistenceInfoCacheMap.get(getCaseInsensitiveKey(typePKorCode));
    }


    boolean cachesInfoFor(PK typePK)
    {
        return this.persistenceInfoCacheMap.containsKey(getCaseInsensitiveKey(typePK));
    }


    void clearCachedInfo(PK typePK, String typeCode)
    {
        TypeInfoMap info = clearType(typePK, typeCode);
        if(info != null)
        {
            clearExternalTableTypeCacheFor(info);
            clearBeanTypeCodeCacheFor(info);
            clearFromSuperTypes(info);
            clearAsRelationType(info);
        }
    }


    private TypeInfoMap clearType(PK typePK, String typeCode)
    {
        TypeInfoMap info = this.persistenceInfoCacheMap.remove(getCaseInsensitiveKey(typePK));
        TypeInfoMap info2 = null;
        if(typeCode != null)
        {
            info2 = this.persistenceInfoCacheMap.remove(getCaseInsensitiveKey(typeCode));
        }
        else if(info != null)
        {
            info2 = this.persistenceInfoCacheMap.remove(getCaseInsensitiveKey(info.getCode()));
        }
        return (info == null) ? info2 : info;
    }


    private void clearFromSuperTypes(TypeInfoMap info)
    {
        PK superTypePK = (info != null) ? info.getSuperTypePK() : null;
        if(superTypePK != null)
        {
            Collection<PK> subtypes = this.superTypeRelationCache.get(superTypePK);
            if(subtypes != null)
            {
                subtypes.remove(info.getTypePK());
            }
        }
    }


    private void clearAsRelationType(TypeInfoMap info)
    {
        this.rootRelationTypes.remove(info.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    void putCachedInfo(TypeInfoMap info)
    {
        registerInfo(info);
        registerSubtype(info);
        registerAsRelation(info);
        clearExternalTableTypeCacheFor(info);
        clearBeanTypeCodeCacheFor(info);
    }


    private void registerInfo(TypeInfoMap info)
    {
        this.persistenceInfoCacheMap.put(getCaseInsensitiveKey(info.getTypePK()), info);
        this.persistenceInfoCacheMap.put(getCaseInsensitiveKey(info.getCode()), info);
    }


    private void registerSubtype(TypeInfoMap info)
    {
        PK st = info.getSuperTypePK();
        if(st != null)
        {
            Collection<PK> subtypePKs = this.superTypeRelationCache.get(st);
            if(subtypePKs == null)
            {
                subtypePKs = new CopyOnWriteArrayList<>();
                Collection<PK> previous = this.superTypeRelationCache.putIfAbsent(st, subtypePKs);
                if(previous != null)
                {
                    subtypePKs = previous;
                }
            }
            subtypePKs.add(info.getTypePK());
        }
    }


    private Collection<PK> getAllSuperTypes(TypeInfoMap info)
    {
        Collection<PK> ret = null;
        PK superTypePK = info.getSuperTypePK();
        while(superTypePK != null)
        {
            if(ret == null)
            {
                ret = new ArrayList<>();
            }
            ret.add(superTypePK);
            TypeInfoMap superInfo = getCachedInfo(superTypePK);
            superTypePK = (superInfo != null) ? superInfo.getSuperTypePK() : null;
        }
        return (ret != null) ? ret : Collections.EMPTY_LIST;
    }


    private void clearExternalTableTypeCacheFor(TypeInfoMap info)
    {
        this.externalTableTypesCache.remove(info.getTypePK());
        for(PK superTypePK : getAllSuperTypes(info))
        {
            this.externalTableTypesCache.remove(superTypePK);
        }
    }


    private void clearBeanTypeCodeCacheFor(TypeInfoMap info)
    {
        this.typePKToTCsCacheMap.remove(info.getTypePK());
        for(PK superTypePK : getAllSuperTypes(info))
        {
            this.typePKToTCsCacheMap.remove(superTypePK);
        }
    }


    private void registerAsRelation(TypeInfoMap info)
    {
        if(info.isRelationType())
        {
            this.rootRelationTypes.add(info.getCode().toLowerCase(LocaleHelper.getPersistenceLocale()));
        }
    }


    private Object getCaseInsensitiveKey(Object typePKorCode)
    {
        if(typePKorCode instanceof String)
        {
            return ((String)typePKorCode).toLowerCase(LocaleHelper.getPersistenceLocale());
        }
        return typePKorCode;
    }


    void clear()
    {
        this.persistenceInfoCacheMap.clear();
        this.rootRelationTypes.clear();
        this.superTypeRelationCache.clear();
        this.rootRelationTypes.clear();
        this.typePKToTCsCacheMap.clear();
        this.externalTableTypesCache.clear();
    }


    void notifyComposedTypeInvalidated(Object[] key, int invalidationType)
    {
        this.typePKToTCsCacheMap.clear();
    }


    public String getSystemID()
    {
        return this.systemID;
    }


    public long getTimeStamp()
    {
        return this.timeStamp;
    }


    public String toString()
    {
        return "PersistenceInfoCache(" + getSystemID() + "," + getTimeStamp() + ")";
    }
}
