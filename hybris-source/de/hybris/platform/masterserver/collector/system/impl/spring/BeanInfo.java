package de.hybris.platform.masterserver.collector.system.impl.spring;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

class BeanInfo
{
    private final String beanName;
    private final String extensionName;
    private final Class<?> type;
    private final Set<String> aliases;


    public BeanInfo(String beanName, String extensionName, Class<?> type, Collection<String> aliases)
    {
        this.beanName = Objects.<String>requireNonNull(beanName);
        this.extensionName = Objects.<String>requireNonNull(extensionName);
        this.type = Objects.<Class<?>>requireNonNull(type);
        this.aliases = Set.copyOf(aliases);
    }


    public String getBeanName()
    {
        return this.beanName;
    }


    public String getExtensionName()
    {
        return this.extensionName;
    }


    public Class<?> getType()
    {
        return this.type;
    }


    public Set<String> getAliases()
    {
        return this.aliases;
    }


    public String toString()
    {
        return "BeanInfo{beanName='" + this.beanName + "', extensionName='" + this.extensionName + "', type=" + this.type + ", aliases=" + this.aliases + "}";
    }
}
