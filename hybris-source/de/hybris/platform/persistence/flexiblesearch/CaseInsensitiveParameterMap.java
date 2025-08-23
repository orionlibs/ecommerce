package de.hybris.platform.persistence.flexiblesearch;

import de.hybris.bootstrap.util.LocaleHelper;
import java.util.Map;
import org.apache.commons.collections.map.AbstractHashedMap;

public class CaseInsensitiveParameterMap<K, V> extends AbstractHashedMap
{
    public CaseInsensitiveParameterMap(Map<? extends K, ? extends V> map)
    {
        super(map);
    }


    protected Object convertKey(Object key)
    {
        if(key instanceof String)
        {
            return ((String)key).toLowerCase(LocaleHelper.getPersistenceLocale());
        }
        if(key == null)
        {
            return AbstractHashedMap.NULL;
        }
        return super.convertKey(key);
    }
}
