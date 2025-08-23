package de.hybris.platform.persistence;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.PasswordsReEncoder;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.c2l.C2LManager;
import de.hybris.platform.persistence.property.AbstractPropertyAccess;
import de.hybris.platform.persistence.property.EJBPropertyCache;
import de.hybris.platform.persistence.property.EJBPropertyContainer;
import de.hybris.platform.persistence.property.EJBPropertyRowCache;
import de.hybris.platform.persistence.property.ItemLocalizedPropertyCacheKey;
import de.hybris.platform.persistence.property.ItemOldPropertyCacheKey;
import de.hybris.platform.persistence.property.ItemPropertyCacheKey;
import de.hybris.platform.persistence.property.OldPropertyJDBC;
import de.hybris.platform.persistence.property.PropertyAccess;
import de.hybris.platform.persistence.property.PropertyJDBC;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.persistence.type.ComposedTypeRemote;
import de.hybris.platform.util.EJBTools;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.encryption.ValueEncryptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

public abstract class ExtensibleItemEJB extends ItemEJB implements ExtensibleItemRemote
{
    private static final Logger LOG = Logger.getLogger(ExtensibleItemEJB.class.getName());
    private transient TypeInfoMap infoCache;


    public abstract void setPropertyTimestampInternal(long paramLong);


    public abstract long getPropertyTimestampInternal();


    protected final Collection<PK> getAllLanguagePKs()
    {
        return EJBTools.toPKList(C2LManager.getInstance().getAllLanguages());
    }


    protected Collection<PropertyAccess> getAllPropertyAccesses(boolean forWriting)
    {
        Collection<PropertyAccess> ret = new ArrayList<>();
        TypeInfoMap infoMap = getTypeInfoMap();
        PropertyAccess acc = null;
        if(infoMap.hasInfos(false))
        {
            acc = getPropertyAccessInternal(infoMap, 0, forWriting, "-");
            if(acc != null)
            {
                ret.add(acc);
            }
        }
        if(infoMap.hasInfos(true))
        {
            for(Iterator<PK> it = getAllLanguagePKs().iterator(); it.hasNext(); )
            {
                acc = getPropertyAccessInternal(infoMap, 1, it.next(), forWriting, "-");
                if(acc != null)
                {
                    ret.add(acc);
                }
            }
        }
        acc = getPropertyAccessInternal(infoMap, 3, forWriting, "-");
        if(acc != null)
        {
            ret.add(acc);
        }
        return ret;
    }


    protected PK doCreateInternal(PK pk, ComposedTypeRemote type, ItemRemote template, EJBPropertyContainer props)
    {
        PK ret = doCreateInternal(pk, type, template);
        if(props != null)
        {
            try
            {
                setAllProperties(props);
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
        return ret;
    }


    protected boolean preLoadUnlocalizedProperties()
    {
        return true;
    }


    public TypeInfoMap getTypeInfoMap()
    {
        if(this.infoCache == null)
        {
            PK typePK = getTypePkString();
            if((typePK == null || PK.NULL_PK.equals(typePK)) && typeCode() != 55)
            {
                getComposedType();
                typePK = getTypePkString();
                if(typePK == null)
                {
                    throw new JaloSystemException(null, "typePKString not set", 0);
                }
            }
            boolean dontLoadInfo = (getPK().equals(typePK) && !getEntityContext().getPersistencePool().getPersistenceManager().cachesInfoFor(typePK));
            this
                            .infoCache = dontLoadInfo ? TypeInfoMap.EMPTY_INFOMAP : getEntityContext().getPersistencePool().getPersistenceManager().getPersistenceInfo(typePK);
        }
        return this.infoCache;
    }


    protected PropertyAccess getPropertyAccess(String name, boolean forWriting)
    {
        TypeInfoMap infoMap = getTypeInfoMap();
        int type = infoMap.getPropertyType(name);
        checkProp(infoMap, type, name);
        return getPropertyAccessInternal(infoMap, type, forWriting, name);
    }


    protected PropertyAccess getPropertyAccess(String name, PK langPK, boolean forWriting)
    {
        TypeInfoMap infoMap = getTypeInfoMap();
        int type = infoMap.getPropertyType(name);
        checkProp(infoMap, type, name);
        return getPropertyAccessInternal(infoMap, type, langPK, forWriting, name);
    }


    private final void checkProp(TypeInfoMap infoMap, int type, String name)
    {
        if(type == 2)
        {
            throw new JaloSystemException(null, "qualifier '" + name + "' is mapped to a core field so you cannot access it like 'normal' properties", 0);
        }
    }


    protected final PropertyAccess getPropertyAccessInternal(TypeInfoMap info, int type, PK langPK, boolean forWriting, String name)
    {
        PropertyAccess ret = null;
        switch(type)
        {
            case 1:
                ret = forWriting ? (PropertyAccess)getCachedValueForModification((ItemCacheKey)new ItemLocalizedPropertyCacheKey(info, langPK)) : (PropertyAccess)getCachedValueForReading((ItemCacheKey)new ItemLocalizedPropertyCacheKey(info, langPK));
                break;
            case 3:
                ret = forWriting ? (PropertyAccess)getCachedValueForModification((ItemCacheKey)new ItemOldPropertyCacheKey()) : (PropertyAccess)getCachedValueForReading((ItemCacheKey)new ItemOldPropertyCacheKey());
                break;
            default:
                throw new JaloSystemException(null, "cannot find localized property access for '" + name + "' (type was " + type + "). this happens e.g. if you try to access an unlocalized property with getLocalizedProperty()", 0);
        }
        if(ret == null)
        {
            System.err.println("------------------------------------------------------------------------");
            System.err.println("--- inconsistent property " + name + " - cannot get access ");
            System.err.println("itemPK=" + getPK());
            System.err.println("typePK=" + getTypePkString());
            System.err.println("type=" + type);
            System.err.println("modify=" + forWriting);
            System.err.println("infoMap=" + info);
            System.err.println("stack:");
            (new Exception()).printStackTrace(System.err);
            System.err.println("------------------------------------------------------------------------");
        }
        return ret;
    }


    private final PropertyAccess getPropertyAccessInternal(TypeInfoMap info, int type, boolean forWriting, String name)
    {
        PropertyAccess ret = null;
        switch(type)
        {
            case 0:
                ret = forWriting ? (PropertyAccess)getCachedValueForModification((ItemCacheKey)new ItemPropertyCacheKey(info)) : (PropertyAccess)getCachedValueForReading((ItemCacheKey)new ItemPropertyCacheKey(info));
                break;
            case 3:
                ret = forWriting ? (PropertyAccess)getCachedValueForModification((ItemCacheKey)new ItemOldPropertyCacheKey()) : (PropertyAccess)getCachedValueForReading((ItemCacheKey)new ItemOldPropertyCacheKey());
                break;
            default:
                throw new JaloSystemException(null, "cannot find unlocalized property access for '" + name + "' (type was " + type + "). this happens e.g. if you try to access an localized property with getProperty()", 0);
        }
        if(ret == null)
        {
            System.err.println("------------------------------------------------------------------------");
            System.err.println("--- inconsistent property " + name + " - cannot get access ");
            System.err.println("itemPK=" + getPK());
            System.err.println("typePK=" + getTypePkString());
            System.err.println("type=" + type);
            System.err.println("modify=" + forWriting);
            System.err.println("infoMap=" + info);
            System.err.println("stack:");
            (new Exception()).printStackTrace(System.err);
            System.err.println("------------------------------------------------------------------------");
        }
        return ret;
    }


    public Map<String, Object> getAllProperties()
    {
        Map<String, Object> ret = new HashMap<>();
        for(Iterator<PropertyAccess> it = getAllPropertyAccesses(false).iterator(); it.hasNext(); )
        {
            PropertyAccess access = it.next();
            Map<String, Object> all = access.getAllProperties();
            ret.putAll(all);
        }
        ValueEncryptor enc = null;
        TypeInfoMap info = null;
        for(Map.Entry<String, Object> e : ret.entrySet())
        {
            if(e.getValue() instanceof String)
            {
                if(info == null)
                {
                    info = getTypeInfoMap();
                }
                if(info.isEncrypted(e.getKey()))
                {
                    if(enc == null)
                    {
                        enc = Registry.getMasterTenant().getValueEncryptor();
                    }
                    e.setValue(decryptValueWithFallback(enc, e.getKey(), (String)e.getValue()));
                }
            }
        }
        return ret;
    }


    public void setAllProperties(EJBPropertyContainer propertyContainer) throws ConsistencyCheckException
    {
        propertyContainer.applyTo(this);
    }


    public Object setProperty(String name, Object value)
    {
        PropertyAccess propertyAccess = getPropertyAccess(name, true);
        Object ret = null;
        if(propertyAccess != null)
        {
            if(value instanceof String && getTypeInfoMap().isEncrypted(name))
            {
                ValueEncryptor enc = Registry.getMasterTenant().getValueEncryptor();
                try
                {
                    ret = propertyAccess.setProperty(name, enc.encrypt((String)value));
                }
                catch(Exception exception)
                {
                    throw new JaloSystemException(exception, exception.getMessage(), 1234);
                }
                if(ret instanceof String)
                {
                    ret = decryptValueWithFallback(enc, name, (String)ret);
                }
            }
            else
            {
                ret = propertyAccess.setProperty(name, value);
            }
        }
        return ret;
    }


    public void setPropertiesFromContainer(Map<String, Object> values)
    {
        if(values != null && !values.isEmpty())
        {
            PropertyAccess unloc = null;
            PropertyAccess old = null;
            TypeInfoMap infoMap = getTypeInfoMap();
            ValueEncryptor enc = null;
            for(Map.Entry<String, Object> e : values.entrySet())
            {
                String name = e.getKey();
                Object value = e.getValue();
                int type = infoMap.getPropertyType(name);
                PropertyAccess propertyAccess = null;
                if(type == 0)
                {
                    propertyAccess = unloc;
                    if(propertyAccess == null)
                    {
                        propertyAccess = unloc = getPropertyAccessInternal(infoMap, type, true, name);
                    }
                }
                else
                {
                    propertyAccess = old;
                    if(propertyAccess == null)
                    {
                        propertyAccess = old = getPropertyAccessInternal(infoMap, type, true, name);
                    }
                }
                if(propertyAccess != null)
                {
                    if(value instanceof String && infoMap.isEncrypted(name))
                    {
                        if(enc == null)
                        {
                            enc = Registry.getMasterTenant().getValueEncryptor();
                        }
                        try
                        {
                            propertyAccess.setProperty(name, enc.encrypt((String)value));
                        }
                        catch(Exception exception)
                        {
                            throw new JaloSystemException(exception, "Invalid Key", 1234);
                        }
                        continue;
                    }
                    propertyAccess.setProperty(name, value);
                }
            }
        }
    }


    public Object getProperty(String name)
    {
        return decryptIfNecessaryWithFallback(name, getPropertyFromPropertyAccess(name, true));
    }


    protected Object decryptIfNecessaryWithFallback(String name, Object original)
    {
        Object ret = original;
        if(original instanceof String && getTypeInfoMap().isEncrypted(name))
        {
            try
            {
                ret = Registry.getMasterTenant().getValueEncryptor().decrypt((String)original);
            }
            catch(Exception e)
            {
                warnOnDecryptionError(name, e);
            }
        }
        return ret;
    }


    protected void warnOnDecryptionError(String name, Exception e)
    {
        if(PasswordsReEncoder.isReEncodingInProgress())
        {
            LOG.info("Problems with decrypting property '" + name + "' of item " + getPkString() + " - probably not encrypted? Using raw value! Re-encoding is in progress.");
        }
        else if(LOG.isDebugEnabled())
        {
            LOG.warn("Error decrypting property '" + name + "' of item " + getPkString() + " - probably not encrypted? Using raw value!", e);
        }
        else
        {
            LOG.warn("Error decrypting property '" + name + "' of item " + getPkString() + " - probably not encrypted? Using raw value!");
        }
    }


    protected String decryptValueWithFallback(ValueEncryptor vc, String name, String original)
    {
        String ret = original;
        try
        {
            ret = vc.decrypt(original);
        }
        catch(Exception e)
        {
            warnOnDecryptionError(name, e);
        }
        return ret;
    }


    public Object getPropertyRaw(String name)
    {
        return decryptIfNecessaryWithFallback(name, getPropertyFromPropertyAccess(name, false));
    }


    public Object removeProperty(String name)
    {
        PropertyAccess propertyAccess = getPropertyAccess(name, true);
        Object ret = (propertyAccess != null) ? propertyAccess.removeProperty(name) : null;
        return ret;
    }


    public Set<String> getPropertyNames()
    {
        Set<String> ret = new HashSet<>();
        for(Iterator<PropertyAccess> it = getAllPropertyAccesses(false).iterator(); it.hasNext(); )
        {
            ret.addAll(((PropertyAccess)it.next()).getPropertyNames());
        }
        return ret;
    }


    private Object getPropertyFromPropertyAccess(String name, boolean instantiateItemRefs)
    {
        PropertyAccess acc = getPropertyAccess(name, false);
        Object value = (acc != null) ? acc.getProperty(name) : null;
        if(instantiateItemRefs && value instanceof ItemPropertyValue)
        {
            PK valuePK = ((ItemPropertyValue)value).getPK();
            ItemRemote item = EJBTools.instantiatePK(valuePK);
            if(item == null)
            {
                value = null;
                PropertyAccess propertyAccess = getPropertyAccess(name, true);
                propertyAccess.removeProperty(name);
            }
        }
        return value;
    }


    protected void createFromTemplate(ItemRemote template)
    {
        super.createFromTemplate(template);
        copyPropertiesFrom((ExtensibleItemRemote)template);
    }


    protected void postCreateFromTemplate(ItemRemote template)
    {
        super.postCreateFromTemplate(template);
    }


    public void copyPropertiesFrom(ExtensibleItemRemote ext)
    {
        for(Iterator<Map.Entry> it = ext.getAllProperties().entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry mapEntry = it.next();
            String name = (String)mapEntry.getKey();
            Object value = mapEntry.getValue();
            setProperty(name, value);
        }
    }


    protected EJBPropertyRowCache getModifiedUnlocalizedPropertyCache()
    {
        EJBPropertyRowCache prc = (EJBPropertyRowCache)getCachedValueForReadingIfAvailable((ItemCacheKey)new ItemPropertyCacheKey(null));
        return (prc != null && prc.hasChanged()) ? prc : null;
    }


    protected boolean hasModifiedCaches()
    {
        Collection caches = getCachedValuesStartingWith("item.property");
        if(caches != null && !caches.isEmpty())
        {
            for(Iterator it = caches.iterator(); it.hasNext(); )
            {
                Object cacheObj = it.next();
                if(cacheObj instanceof EJBPropertyRowCache)
                {
                    if(((EJBPropertyRowCache)cacheObj).hasChanged())
                    {
                        return true;
                    }
                    continue;
                }
                if(cacheObj instanceof EJBPropertyCache)
                {
                    if(((EJBPropertyCache)cacheObj).needsUpdate())
                    {
                        return true;
                    }
                }
            }
        }
        return super.hasModifiedCaches();
    }


    protected final void updatePropertyVersion(long newTS)
    {
        Collection propertyCaches = getCachedValuesStartingWith("item.property");
        if(propertyCaches != null && !propertyCaches.isEmpty())
        {
            for(Iterator<AbstractPropertyAccess> it = propertyCaches.iterator(); it.hasNext(); )
            {
                AbstractPropertyAccess propertyAccess = it.next();
                propertyAccess.setVersion(newTS);
            }
        }
        setPropertyTimestampInternal(newTS);
    }


    public String ejbHomeGetPropertyTableName()
    {
        return getPropertyTableNameImpl();
    }


    public abstract String getPropertyTableNameImpl();


    protected final boolean writePropertyCaches()
    {
        Collection caches = getCachedValuesStartingWith("item.property");
        if(caches == null || caches.isEmpty())
        {
            return false;
        }
        TypeInfoMap infoMap = getTypeInfoMap();
        PK itemPK = getPK();
        PK typePK = getTypePkString();
        boolean changed = false;
        for(Iterator it = caches.iterator(); it.hasNext(); )
        {
            Object cacheObj = it.next();
            if(cacheObj instanceof EJBPropertyRowCache)
            {
                EJBPropertyRowCache prc = (EJBPropertyRowCache)cacheObj;
                boolean localized = (prc.getLangPK() != null && !PK.NULL_PK.equals(prc.getLangPK()));
                if(prc.hasChanged())
                {
                    if(localized)
                    {
                        PropertyJDBC.writeProperties(prc, itemPK, typePK, infoMap, localized);
                        changed = true;
                    }
                }
                continue;
            }
            if(cacheObj instanceof EJBPropertyCache)
            {
                EJBPropertyCache propertyCache = (EJBPropertyCache)cacheObj;
                if(propertyCache.needsUpdate())
                {
                    OldPropertyJDBC.writeOldProperties(propertyCache, itemPK, typePK, getPropertyTableNameImpl());
                    changed = true;
                }
            }
        }
        return changed;
    }


    protected final void removePropertyData()
    {
        TypeInfoMap infoMap = getTypeInfoMap();
        if(infoMap.hasInfos(false) || infoMap.hasInfos(true))
        {
            PropertyJDBC.removeAllPropertyData(getPK(), getTypePkString());
        }
        OldPropertyJDBC.removeAllPropertyData(getPK(), getPropertyTableNameImpl());
    }


    private void updatePropertyCaches(TypeInfoMap newMap)
    {
        Collection caches = getCachedValuesStartingWith("item.property");
        for(Iterator it = caches.iterator(); it.hasNext(); )
        {
            Object cacheObj = it.next();
            if(cacheObj instanceof EJBPropertyRowCache)
            {
                EJBPropertyRowCache prc = (EJBPropertyRowCache)cacheObj;
                boolean localized = (prc.getLangPK() != null && !PK.NULL_PK.equals(prc.getLangPK()));
                prc.changeColumns(newMap.getSortedNames(localized));
            }
        }
    }


    protected void typeChanged(PK oldTypePK, PK newTypePK)
    {
        if(oldTypePK == newTypePK || (oldTypePK != null && oldTypePK.equals(newTypePK)))
        {
            return;
        }
        TypeInfoMap newTypeInfoMap = getEntityContext().getPersistencePool().getPersistenceManager().getPersistenceInfo(newTypePK);
        updatePropertyCaches(newTypeInfoMap);
        this.infoCache = null;
        PropertyJDBC.updatePropertyTypePKs(newTypeInfoMap, getPK(), newTypePK);
        OldPropertyJDBC.updatePropertyTypePKs(getPK(), newTypePK, getPropertyTableNameImpl());
    }


    protected void clearEntityCaches()
    {
        super.clearEntityCaches();
        this.infoCache = null;
    }


    public void ejbRemove()
    {
        removePropertyData();
        super.ejbRemove();
    }


    public long getPropertyTimestamp()
    {
        return getPropertyTimestampInternal();
    }
}
