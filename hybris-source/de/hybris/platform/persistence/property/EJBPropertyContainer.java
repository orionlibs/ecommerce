package de.hybris.platform.persistence.property;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.persistence.ExtensibleItemEJB;
import de.hybris.platform.persistence.c2l.LocalizableItemEJB;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.Utilities;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class EJBPropertyContainer implements Cloneable
{
    private Map<PropertyKey, Object> properties;


    public Map<PropertyKey, Object> getPropertyMap()
    {
        return (this.properties != null) ? this.properties : Collections.EMPTY_MAP;
    }


    public EJBPropertyContainer()
    {
    }


    private EJBPropertyContainer(Map<PropertyKey, Object> props)
    {
        if(props != null)
        {
            this.properties = new HashMap<>(props.size());
            for(Map.Entry<PropertyKey, Object> e : props.entrySet())
            {
                this.properties.put(new PropertyKey(((PropertyKey)e.getKey()).getName(), ((PropertyKey)e.getKey()).getLanguagePK()),
                                Utilities.copyViaSerialization(e.getValue()));
            }
        }
    }


    public Object clone() throws CloneNotSupportedException
    {
        return new EJBPropertyContainer(this.properties);
    }


    private PropertyKey getKey(String name, PK language)
    {
        return new PropertyKey(name, language);
    }


    private Map<PropertyKey, Object> getProperties(boolean create)
    {
        return (this.properties != null) ? this.properties : (create ? (this.properties = new HashMap<>()) : Collections.EMPTY_MAP);
    }


    public void setProperty(String name, Object value)
    {
        getProperties(true).put(getKey(name, null), value);
    }


    public void setLocalizedProperty(String name, PK languagePK, Object value)
    {
        getProperties(true).put(getKey(name, languagePK), value);
    }


    public void applyTo(ExtensibleItemEJB item) throws ConsistencyCheckException
    {
        boolean localizable = item instanceof LocalizableItemEJB;
        Map<String, Object> unlocProps = null;
        Map<PK, Map<String, Object>> locProps = null;
        for(Map.Entry<PropertyKey, Object> next : getProperties(false).entrySet())
        {
            Object value = next.getValue();
            PropertyKey key = next.getKey();
            String name = key.getName();
            if(Item.OWNER.equalsIgnoreCase(name))
            {
                item.setOwnerPkString((value != null) ? ((ItemPropertyValue)value).getPK() : null);
                continue;
            }
            if(Item.CREATION_TIME.equalsIgnoreCase(name))
            {
                item.setCreationTime((Date)value);
                continue;
            }
            PK langPK = key.getLanguagePK();
            if(langPK == null)
            {
                if(unlocProps == null)
                {
                    unlocProps = new HashMap<>(getProperties(false).size());
                }
                unlocProps.put(name, value);
                continue;
            }
            if(!localizable)
            {
                throw new ConsistencyCheckException(null, "cannot set localized property '" + key + "' in non-localizable item " + item
                                .getPkString(), 0);
            }
            if(locProps == null)
            {
                locProps = new HashMap<>();
            }
            Map<String, Object> vMap = locProps.get(langPK);
            if(vMap == null)
            {
                locProps.put(langPK, vMap = new HashMap<>());
            }
            vMap.put(name, value);
        }
        if(unlocProps != null)
        {
            item.setPropertiesFromContainer(unlocProps);
        }
        if(locProps != null)
        {
            ((LocalizableItemEJB)item).setLocPropertiesFromContainer(locProps);
        }
    }


    public String toPropertyString()
    {
        StringBuilder buffer = new StringBuilder();
        Iterator<Map.Entry<PropertyKey, Object>> iter = getProperties(false).entrySet().iterator();
        while(iter.hasNext())
        {
            Map.Entry<PropertyKey, Object> next = iter.next();
            PropertyKey nextKey = next.getKey();
            Object nextValue = next.getValue();
            buffer.append(nextKey.getName() + "/" + nextKey.getName() + "->" + (
                            (nextKey.getLanguagePK() != null) ?
                                            nextKey.getLanguagePK().toString() : "<langPK:null>"));
            if(iter.hasNext())
            {
                buffer.append(",");
            }
        }
        return buffer.toString();
    }


    public String toString()
    {
        return "EJBPropertyContainer(" + toPropertyString() + ")";
    }
}
