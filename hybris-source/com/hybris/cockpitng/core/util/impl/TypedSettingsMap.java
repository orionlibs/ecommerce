/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.core.util.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Manages widget settings provided via a widget's definition.xml or at runtime.
 */
public class TypedSettingsMap extends HashMap<String, Object>
{
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(TypedSettingsMap.class);
    private static final char BOOLEAN_PREFIX = 'b';
    private static final char INTEGER_PREFIX = 'i';
    private static final char STRING_PREFIX = 's';
    private static final char DOUBLE_PREFIX = 'd';
    private final Map<String, List<String>> enumMap = new HashMap<String, List<String>>();
    private final Map<String, Class> keyToTypeMap = new HashMap<>();


    /**
     * Add an entry and assign type info to its value as prefix.
     *
     * @return the value that has been assigned to the specified key.
     */
    @Override
    public Object put(final String key, final Object value)
    {
        String stringValue = null;
        if(value instanceof Boolean)
        {
            stringValue = BOOLEAN_PREFIX + String.valueOf(value);
        }
        else if(value instanceof Integer)
        {
            stringValue = INTEGER_PREFIX + String.valueOf(((Integer)value).intValue());
        }
        else if(value instanceof Double)
        {
            stringValue = DOUBLE_PREFIX + String.valueOf(((Double)value).doubleValue());
        }
        else if(value instanceof String)
        {
            stringValue = STRING_PREFIX + (String)value;
        }
        else if(value != null)
        {
            LOG.warn("Values of type '" + value.getClass().getName() + "' not supported");
        }
        return super.put(key, stringValue);
    }


    public Object put(final String key, final Object value, final Class type)
    {
        keyToTypeMap.put(key, type);
        return put(key, value);
    }


    @Override
    public Object get(final Object key)
    {
        return getTyped(key);
    }


    @Override
    public Object getOrDefault(final Object key, final Object defaultValue)
    {
        final Object result = get(key);
        return result != null ? result : defaultValue;
    }


    public Object getTyped(final Object key)
    {
        final String value = (String)super.get(key);
        if(value != null)
        {
            switch(value.charAt(0))
            {
                case STRING_PREFIX:
                    return value.substring(1);
                case BOOLEAN_PREFIX:
                    return Boolean.valueOf(Boolean.parseBoolean(value.substring(1)));
                case INTEGER_PREFIX:
                    return Integer.valueOf(Integer.parseInt(value.substring(1)));
                case DOUBLE_PREFIX:
                    return Double.valueOf(Double.parseDouble(value.substring(1)));
                default:
                    return null;
            }
        }
        return null;
    }


    public boolean isEnum(final String key)
    {
        return enumMap.containsKey(key);
    }


    public List<String> getAvailableValues(final String key)
    {
        return enumMap.get(key);
    }


    public void setAvailableValues(final String key, final List<String> values)
    {
        enumMap.put(key, values);
    }


    public Map<String, Object> getAll()
    {
        if(isEmpty())
        {
            return Collections.emptyMap();
        }
        else
        {
            final Map<String, Object> all = new HashMap<String, Object>(size());
            for(final String key : keySet())
            {
                all.put(key, getTyped(key));
            }
            return all;
        }
    }


    public String getRaw(final Object key)
    {
        return (String)super.get(key);
    }


    /**
     * Add entry without performing any transformations.
     *
     * @return the value that has been assigned to key
     */
    public Object putRaw(final String key, final Object value)
    {
        return super.put(key, value);
    }


    public boolean getBoolean(final Object key)
    {
        final Object object = get(key);
        if(object instanceof Boolean)
        {
            return ((Boolean)object).booleanValue();
        }
        return false;
    }


    public String getString(final Object key)
    {
        final Object object = get(key);
        if(object instanceof String)
        {
            return (String)object;
        }
        return null;
    }


    public int getInt(final Object key)
    {
        final Object object = get(key);
        if(object instanceof Integer)
        {
            return ((Integer)object).intValue();
        }
        return 0;
    }


    public double getDouble(final Object key)
    {
        final Object object = get(key);
        if(object instanceof Double)
        {
            return ((Double)object).doubleValue();
        }
        return 0;
    }


    /**
     * Checks if the 'key' represents a component's (editor, action) setting defined in definition.xml.
     * If so, tries to parse the 'val' into the appropriate class (like Integer, Double, Boolean, etc) if it is a string.
     * If it is not a setting, or is already of the required class, returns the 'val'.
     */
    public Object parseIfSetting(final String component, final String key, final Object val)
    {
        final boolean isSetting = containsKey(key);
        if(isSetting)
        {
            return parseSetting(component, key, val);
        }
        else
        {
            return val;
        }
    }


    private Object parseSetting(final String component, final String setting, final Object value)
    {
        if(value == null || (value instanceof String && "null".equalsIgnoreCase((String)value)))
        {
            return null;
        }
        final Class settingClass = keyToTypeMap.get(setting);
        // if value is of correct class - no need to parse
        if(settingClass == null || settingClass.equals(value.getClass()))
        {
            return value;
        }
        // value is of wrong class - check if it's string, as we only support parsing strings
        if(!(value instanceof String))
        {
            LOG.error("Parsing '{}' into '{}' not supported. Component {}, setting '{}'. Using default value.", value.getClass(),
                            settingClass, component, setting);
            return get(setting);
        }
        // value is string but setting class is not - parse
        final String stringValue = (String)value;
        if(settingClass.equals(Boolean.class))
        {
            return Boolean.valueOf(stringValue);
        }
        else if(settingClass.equals(Integer.class))
        {
            try
            {
                return Integer.valueOf(stringValue);
            }
            catch(final NumberFormatException e)
            {
                LOG.error("Invalid value '{}' for setting '{}' of component '{}', using default value.", value, setting, component);
                return get(setting);
            }
        }
        else if(settingClass.equals(Double.class))
        {
            try
            {
                return Double.valueOf(stringValue);
            }
            catch(final NumberFormatException e)
            {
                LOG.error("Invalid value '{}' for setting '{}' of component '{}', using default value.", value, setting, component);
                return get(setting);
            }
        }
        else
        {
            LOG.warn("Could not parse value '{}' of setting '{}' of component '{}'. Class '{}' not supported.", value, setting,
                            component, settingClass);
            return get(setting);
        }
    }


    public Class getSettingClass(final String key)
    {
        return keyToTypeMap.get(key);
    }
}
