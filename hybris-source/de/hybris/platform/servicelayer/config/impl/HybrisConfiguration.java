package de.hybris.platform.servicelayer.config.impl;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import de.hybris.platform.util.config.ConfigIntf;
import java.util.Iterator;
import org.apache.commons.configuration.AbstractConfiguration;
import org.apache.commons.lang.text.StrSubstitutor;

public class HybrisConfiguration extends AbstractConfiguration
{
    private final ConfigIntf config;
    private final Supplier<StrSubstitutor> substitutor = Suppliers.memoize(() -> super.getSubstitutor());


    HybrisConfiguration(ConfigIntf config)
    {
        this.config = config;
    }


    protected void addPropertyDirect(String key, Object value)
    {
        getHybrisConfig().setParameter(key, (String)value);
    }


    public boolean containsKey(String key)
    {
        return (getHybrisConfig().getParameter(key) != null);
    }


    public Iterator<String> getKeys()
    {
        return getHybrisConfig().getAllParameters().keySet().iterator();
    }


    public Object getProperty(String key)
    {
        return getHybrisConfig().getParameter(key);
    }


    public boolean isEmpty()
    {
        return false;
    }


    public String getString(String key)
    {
        return getString(key, "");
    }


    public String getString(String key, String defaultValue)
    {
        return getHybrisConfig().getString(key, defaultValue);
    }


    protected void clearPropertyDirect(String key)
    {
        getHybrisConfig().removeParameter(key);
    }


    public StrSubstitutor getSubstitutor()
    {
        return (StrSubstitutor)this.substitutor.get();
    }


    private ConfigIntf getHybrisConfig()
    {
        return this.config;
    }
}
