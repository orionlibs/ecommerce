package de.hybris.platform.persistence.c2l;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.ExtensibleItemEJB;
import de.hybris.platform.persistence.ItemCacheKey;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.property.ItemLocalizedPropertyCacheKey;
import de.hybris.platform.persistence.property.LocalizedPropertyAccess;
import de.hybris.platform.persistence.property.TypeInfoMap;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import de.hybris.platform.util.encryption.ValueEncryptor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.MapUtils;

public abstract class LocalizableItemEJB extends ExtensibleItemEJB implements LocalizableItemRemote
{
    protected void createFromTemplate(ItemRemote template)
    {
        super.createFromTemplate(template);
        copyLocalizablePropertiesFrom((LocalizableItemRemote)template);
    }


    protected void postCreateFromTemplate(ItemRemote template)
    {
        super.postCreateFromTemplate(template);
    }


    public void copyLocalizablePropertiesFrom(LocalizableItemRemote ext)
    {
        Collection<PK> languages = getAllLanguagePKs();
        for(PK pk : languages)
        {
            for(Iterator<Map.Entry> it = ext.getAllLocalizedProperties(pk).entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry e = it.next();
                setLocalizedProperty((String)e.getKey(), pk, e.getValue());
            }
        }
    }


    protected LocalizedPropertyAccess getLocPropertyAccess(String name, PK langPK, boolean forWriting)
    {
        if(langPK == null)
        {
            throw new JaloSystemException(null, "you cannot get localized property access for " + name + " with NULL language", 0);
        }
        return (LocalizedPropertyAccess)getPropertyAccess(name, langPK, forWriting);
    }


    public void hintPropertyCache(ItemLocalizedPropertyCacheKey cacheKey)
    {
        getOrAddCacheKey((ItemCacheKey)cacheKey);
    }


    public Map getAllLocalizedProperties(PK langPK)
    {
        Map<String, Object> ret = new HashMap<>();
        for(Iterator<LocalizedPropertyAccess> it = getAllPropertyAccesses(false).iterator(); it.hasNext(); )
        {
            ret.putAll(((LocalizedPropertyAccess)it.next()).getAllProperties(langPK));
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


    public Object getLocalizedProperty(String name, PK langPK)
    {
        if(langPK == null)
        {
            throw new NullPointerException("lang pk was null");
        }
        LocalizedPropertyAccess acc = getLocPropertyAccess(name, langPK, false);
        Object ret = (acc != null) ? acc.getProperty(name, langPK) : null;
        return decryptIfNecessaryWithFallback(name, ret);
    }


    public Map<ItemPropertyValue, Object> setAllLocalizedProperties(String name, Map<ItemPropertyValue, Object> props)
    {
        Map<ItemPropertyValue, Object> ret = null;
        if(MapUtils.isNotEmpty(props))
        {
            for(Map.Entry<ItemPropertyValue, Object> e : props.entrySet())
            {
                if(e.getValue() == null)
                {
                    Object object = removeLocalizedProperty(name, ((ItemPropertyValue)e.getKey()).getPK());
                    if(object != null)
                    {
                        if(ret == null)
                        {
                            ret = new HashMap<>(props.size());
                        }
                        ret.put(e.getKey(), object);
                    }
                    continue;
                }
                Object val = setLocalizedProperty(name, ((ItemPropertyValue)e.getKey()).getPK(), e.getValue());
                if(val != null)
                {
                    if(ret == null)
                    {
                        ret = new HashMap<>(props.size());
                    }
                    ret.put(e.getKey(), val);
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_MAP;
    }


    public Map<ItemPropertyValue, Object> getAllLocalizedProperties(String name, ItemPropertyValueCollection languages)
    {
        Map<ItemPropertyValue, Object> ret = new HashMap<>(languages.size());
        for(ItemPropertyValue langPK : languages)
        {
            Object val = getLocalizedProperty(name, langPK.getPK());
            if(val != null)
            {
                ret.put(langPK, val);
            }
        }
        if(getTypeInfoMap().isEncrypted(name))
        {
            ValueEncryptor enc = null;
            TypeInfoMap info = null;
            for(Map.Entry<ItemPropertyValue, Object> e : ret.entrySet())
            {
                if(e.getValue() instanceof String)
                {
                    if(info == null)
                    {
                        info = getTypeInfoMap();
                    }
                    if(enc == null)
                    {
                        enc = Registry.getMasterTenant().getValueEncryptor();
                    }
                    e.setValue(decryptValueWithFallback(enc, name, (String)e.getValue()));
                }
            }
        }
        return ret;
    }


    public Object setLocalizedProperty(String name, PK langPK, Object value)
    {
        if(langPK == null)
        {
            throw new NullPointerException("lang pk was null");
        }
        LocalizedPropertyAccess pc = getLocPropertyAccess(name, langPK, true);
        Object ret = null;
        if(pc != null)
        {
            if(value instanceof String && getTypeInfoMap().isEncrypted(name))
            {
                ValueEncryptor enc = Registry.getMasterTenant().getValueEncryptor();
                try
                {
                    ret = pc.setProperty(name, langPK, enc.encrypt((String)value));
                }
                catch(Exception e)
                {
                    throw new JaloSystemException(e, "Invalid Key", 1234);
                }
                if(ret instanceof String)
                {
                    ret = decryptValueWithFallback(enc, name, (String)ret);
                }
            }
            else
            {
                ret = pc.setProperty(name, langPK, value);
            }
        }
        return ret;
    }


    public Object removeLocalizedProperty(String name, PK langPK)
    {
        if(langPK == null)
        {
            throw new NullPointerException("lang pk was null");
        }
        LocalizedPropertyAccess pc = getLocPropertyAccess(name, langPK, true);
        Object ret = (pc != null) ? pc.removeProperty(name, langPK) : null;
        return ret;
    }


    public Set getLocalizedPropertyNames(PK langPK)
    {
        Set ret = new HashSet();
        for(Iterator<LocalizedPropertyAccess> it = getAllPropertyAccesses(false).iterator(); it.hasNext(); )
        {
            ret.addAll(((LocalizedPropertyAccess)it.next()).getPropertyNames(langPK));
        }
        return ret;
    }


    public void setLocPropertiesFromContainer(Map<PK, Map<String, Object>> values)
    {
        if(values != null && !values.isEmpty())
        {
            LocalizedPropertyAccess oldPA = null;
            TypeInfoMap infoMap = getTypeInfoMap();
            ValueEncryptor enc = null;
            for(Map.Entry<PK, Map<String, Object>> e : values.entrySet())
            {
                PK langPK = e.getKey();
                LocalizedPropertyAccess locPA = null;
                for(Map.Entry<String, Object> e2 : (Iterable<Map.Entry<String, Object>>)((Map)e.getValue()).entrySet())
                {
                    String name = e2.getKey();
                    Object value = e2.getValue();
                    int type = infoMap.getPropertyType(name);
                    LocalizedPropertyAccess pa = null;
                    if(type == 1)
                    {
                        pa = locPA;
                        if(pa == null)
                        {
                            pa = locPA = (LocalizedPropertyAccess)getPropertyAccessInternal(infoMap, type, langPK, true, name);
                        }
                    }
                    else
                    {
                        pa = oldPA;
                        if(pa == null)
                        {
                            pa = oldPA = (LocalizedPropertyAccess)getPropertyAccessInternal(infoMap, type, langPK, true, name);
                        }
                    }
                    if(pa != null)
                    {
                        if(value instanceof String && infoMap.isEncrypted(name))
                        {
                            if(enc == null)
                            {
                                enc = Registry.getMasterTenant().getValueEncryptor();
                            }
                            try
                            {
                                pa.setProperty(name, langPK, enc.encrypt((String)value));
                            }
                            catch(Exception exception)
                            {
                                throw new JaloSystemException(exception, "Invalid Key", 1234);
                            }
                            continue;
                        }
                        pa.setProperty(name, langPK, value);
                    }
                }
            }
        }
    }
}
