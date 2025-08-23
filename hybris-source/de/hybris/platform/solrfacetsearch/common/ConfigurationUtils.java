package de.hybris.platform.solrfacetsearch.common;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.util.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfigurationUtils
{
    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationUtils.class);
    protected static final String MESSAGE_TEMPLATE = "Existing '{}.{}'[{}] value will be overridden by configuration property '{}'[{}]";


    public static Object getObject(ItemModel item, String property, String template, Object... args)
    {
        Object value = getValueFromConfig(item, property, template, args);
        return (value == null) ? item.getProperty(property) : value;
    }


    public static String getString(ItemModel item, String property, String template, Object... args)
    {
        String value = getValueFromConfig(item, property, template, args);
        return (value == null) ? (String)item.getProperty(property) : value;
    }


    public static Integer getInteger(ItemModel item, String property, String template, Object... args)
    {
        String value = getValueFromConfig(item, property, template, args);
        if(value != null)
        {
            return Integer.valueOf(value);
        }
        return (Integer)item.getProperty(property);
    }


    private static String getValueFromConfig(ItemModel item, String property, String template, Object... args)
    {
        String key = String.format(template, args);
        String value = Config.getString(key, null);
        if(value != null)
        {
            LOG.debug("Existing '{}.{}'[{}] value will be overridden by configuration property '{}'[{}]", new Object[] {item.getItemtype(), property, item.getProperty(property), key, value});
        }
        return value;
    }
}
