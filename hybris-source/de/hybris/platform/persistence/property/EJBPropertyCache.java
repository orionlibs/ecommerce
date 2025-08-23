package de.hybris.platform.persistence.property;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.EJBInternalException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public final class EJBPropertyCache extends AbstractPropertyAccess implements LocalizedPropertyAccess, Cloneable
{
    public static EJBPropertyCache create(long timestamp)
    {
        return new EJBPropertyCache(timestamp);
    }


    public static EJBPropertyCache load(long timestamp, Collection<EJBProperty> allProps)
    {
        return new EJBPropertyCache(timestamp, allProps);
    }


    private Map<Object, EJBProperty> cacheMap = null;
    private boolean needsUpdateFlag = false;


    private EJBPropertyCache(long timestamp)
    {
        this(timestamp, Collections.EMPTY_LIST);
    }


    private EJBPropertyCache(long timestamp, Collection<EJBProperty> allProps)
    {
        super(timestamp);
        for(Iterator<EJBProperty> it = allProps.iterator(); it.hasNext(); )
        {
            EJBProperty p = it.next();
            getCacheMap().put(p.getKey(), p);
            registerProperty(p);
        }
    }


    public boolean needsUpdate()
    {
        return this.needsUpdateFlag;
    }


    public Collection<EJBProperty> getUpdateableProperties()
    {
        if(!this.needsUpdateFlag)
        {
            return Collections.EMPTY_LIST;
        }
        Collection<EJBProperty> updateableProperties = new ArrayList<>();
        for(Iterator<Map.Entry<Object, EJBProperty>> iter = getCacheMap().entrySet().iterator(); iter.hasNext(); )
        {
            EJBProperty next = (EJBProperty)((Map.Entry)iter.next()).getValue();
            if(next.hasChanged())
            {
                updateableProperties.add(next);
            }
        }
        return updateableProperties;
    }


    private boolean hasUpdateableProperties()
    {
        for(Iterator<Map.Entry<Object, EJBProperty>> iter = getCacheMap().entrySet().iterator(); iter.hasNext(); )
        {
            EJBProperty next = (EJBProperty)((Map.Entry)iter.next()).getValue();
            if(next.hasChanged())
            {
                return true;
            }
        }
        return false;
    }


    private Map<Object, EJBProperty> getCacheMap()
    {
        if(this.cacheMap == null)
        {
            this.cacheMap = new HashMap<>();
        }
        return this.cacheMap;
    }


    public Collection<EJBProperty> getEJBProperties()
    {
        return getCacheMap().values();
    }


    private void markModified(boolean checkAll)
    {
        this.needsUpdateFlag = (!checkAll || hasUpdateableProperties());
        firePropertyDataChanged();
    }


    private void cleanRemovedAndNonExisting()
    {
        for(Iterator<Map.Entry<Object, EJBProperty>> iter = getCacheMap().entrySet().iterator(); iter.hasNext(); )
        {
            Map.Entry<Object, EJBProperty> e = iter.next();
            if(!((EJBProperty)e.getValue()).exists())
            {
                iter.remove();
            }
        }
    }


    public void wroteChanges()
    {
        this.needsUpdateFlag = false;
        cleanRemovedAndNonExisting();
    }


    public void setAllProperties(EJBPropertyContainer container)
    {
        Iterator<Map.Entry<EJBPropertyContainer.PropertyKey, Object>> iter = container.getPropertyMap().entrySet().iterator();
        if(iter.hasNext())
        {
            Collection<EJBProperty> modified = new ArrayList<>();
            while(iter.hasNext())
            {
                Map.Entry<EJBPropertyContainer.PropertyKey, Object> next = iter.next();
                EJBPropertyContainer.PropertyKey key = next.getKey();
                setPropertyInternal(modified, key.getName(), key.getLanguagePK(), next.getValue());
            }
            markModified(modified.isEmpty());
        }
    }


    private void setPropertyInternal(Collection<EJBProperty> modified, String name, PK langPK, Object value)
    {
        EJBProperty p = getPropertyInternal(name, langPK);
        if(p == null && value != null)
        {
            p = addNewPropertyInternal(name, langPK, value);
        }
        else if(p != null)
        {
            if(value != null)
            {
                p.setValue(value);
            }
            else
            {
                p.remove();
            }
        }
        if(p != null && p.hasChanged())
        {
            modified.add(p);
        }
    }


    public Map<String, Object> getAllProperties()
    {
        return getPropertyValues(null);
    }


    public Object setProperty(String name, Object value)
    {
        return (value != null) ? setPropertyValue(name, null, value) : removePropertyValue(name, null);
    }


    public Object getProperty(String name)
    {
        return getPropertyValue(name, null);
    }


    public Object removeProperty(String name)
    {
        return removePropertyValue(name, null);
    }


    public Set<String> getPropertyNames()
    {
        return getPropertyNamesInternal(null);
    }


    public boolean hasModifiedProperties()
    {
        return hasCommitableProperties();
    }


    public void commitProperties()
    {
        Collection<EJBProperty> doCommitOn = getCommitableProperties();
        if(!doCommitOn.isEmpty())
        {
            for(Iterator<EJBProperty> it = doCommitOn.iterator(); it.hasNext(); )
            {
                EJBProperty p = it.next();
                p.commit();
            }
        }
        markModified(doCommitOn.isEmpty());
    }


    public void rollbackProperties()
    {
        Collection<EJBProperty> doCommitOn = getCommitableProperties();
        if(!doCommitOn.isEmpty())
        {
            for(Iterator<EJBProperty> it = doCommitOn.iterator(); it.hasNext(); )
            {
                EJBProperty p = it.next();
                p.rollback();
            }
        }
        markModified(doCommitOn.isEmpty());
    }


    public Map<String, Object> getAllProperties(PK langPK)
    {
        return getPropertyValues(langPK);
    }


    public Object setProperty(String name, PK langPK, Object value)
    {
        return (value != null) ? setPropertyValue(name, langPK, value) : removePropertyValue(name, langPK);
    }


    public Object getProperty(String name, PK langPK)
    {
        return getPropertyValue(name, langPK);
    }


    public Object removeProperty(String name, PK langPK)
    {
        return removePropertyValue(name, langPK);
    }


    public Set<String> getPropertyNames(PK langPK)
    {
        return getPropertyNamesInternal(langPK);
    }


    protected Object clone() throws CloneNotSupportedException
    {
        if(this.needsUpdateFlag)
        {
            throw new EJBInternalException(null, "you cannot clone a modified EJBPropertyCache object", 0);
        }
        boolean cloneProps = (this.cacheMap != null);
        Collection<EJBProperty> newProps = cloneProps ? new ArrayList<>() : Collections.EMPTY_LIST;
        if(cloneProps)
        {
            for(Iterator<Map.Entry<Object, EJBProperty>> it = this.cacheMap.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry<Object, EJBProperty> e = it.next();
                EJBProperty prop = e.getValue();
                newProps.add((EJBProperty)prop.clone());
            }
        }
        return new EJBPropertyCache(getVersion(), newProps);
    }


    private void registerProperty(EJBProperty prop)
    {
    }


    private EJBProperty getPropertyInternal(String name, PK langPK)
    {
        EJBProperty prop = getCacheMap().get(EJBProperty.constructKey(name, langPK));
        return prop;
    }


    private Object getPropertyValue(String name, PK langPK)
    {
        EJBProperty p = getPropertyInternal(name, langPK);
        return (p != null) ? p.getValue() : null;
    }


    private Object setPropertyValue(String name, PK langPK, Object value)
    {
        if(name == null || value == null)
        {
            throw new IllegalArgumentException("name or value was NULL");
        }
        Object ret = null;
        EJBProperty p = getPropertyInternal(name, langPK);
        if(p == null)
        {
            p = addNewPropertyInternal(name, langPK, value);
        }
        else
        {
            ret = p.setValue(value);
        }
        markModified(!p.hasChanged());
        return ret;
    }


    private Object removePropertyValue(String name, PK langPK)
    {
        if(name == null)
        {
            throw new IllegalArgumentException("name was NULL");
        }
        EJBProperty p = getPropertyInternal(name, langPK);
        if(p == null)
        {
            return null;
        }
        Object ret = p.remove();
        markModified(!p.hasChanged());
        if(!p.exists())
        {
            getCacheMap().remove(p.getKey());
        }
        return ret;
    }


    private EJBProperty addNewPropertyInternal(String name, PK langPK, Object value)
    {
        EJBProperty p = EJBProperty.create(name, langPK, value);
        getCacheMap().put(p.getKey(), p);
        registerProperty(p);
        return p;
    }


    private Set<String> getPropertyNamesInternal(PK langPK)
    {
        if(this.cacheMap == null || getCacheMap().isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<String> result = new HashSet<>();
        for(Iterator<Map.Entry<Object, EJBProperty>> it = getCacheMap().entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<?, EJBProperty> e = it.next();
            EJBProperty p = e.getValue();
            Object v = p.getValue();
            PK l = p.getLang();
            if(((l == null && langPK == null) || (l != null && l.equals(langPK))) && v != null)
            {
                result.add(p.getName());
            }
        }
        return result;
    }


    public Map<String, Object> getPropertyValues(PK langPK)
    {
        if(this.cacheMap == null || getCacheMap().isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        Map<String, Object> result = new HashMap<>();
        for(Iterator<Map.Entry<Object, EJBProperty>> it = getCacheMap().entrySet().iterator(); it.hasNext(); )
        {
            Map.Entry<Object, EJBProperty> e = it.next();
            EJBProperty p = e.getValue();
            Object v = p.getValue();
            PK l = p.getLang();
            if((l == langPK || (l != null && l.equals(langPK))) && v != null)
            {
                result.put(p.getName(), v);
            }
        }
        return result;
    }


    private boolean hasCommitableProperties()
    {
        if(this.cacheMap == null || getCacheMap().isEmpty())
        {
            return false;
        }
        for(Iterator<Map.Entry<Object, EJBProperty>> it = getCacheMap().entrySet().iterator(); it.hasNext(); )
        {
            EJBProperty p = (EJBProperty)((Map.Entry)it.next()).getValue();
            if(p.isCommitable())
            {
                return true;
            }
        }
        return false;
    }


    private Collection<EJBProperty> getCommitableProperties()
    {
        if(this.cacheMap == null || getCacheMap().isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        Collection<EJBProperty> ret = new ArrayList<>();
        for(Iterator<Map.Entry<Object, EJBProperty>> it = getCacheMap().entrySet().iterator(); it.hasNext(); )
        {
            EJBProperty p = (EJBProperty)((Map.Entry)it.next()).getValue();
            if(p.isCommitable())
            {
                ret.add(p);
            }
        }
        return ret;
    }
}
