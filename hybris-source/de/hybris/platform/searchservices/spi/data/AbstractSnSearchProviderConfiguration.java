package de.hybris.platform.searchservices.spi.data;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class AbstractSnSearchProviderConfiguration implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private Map<Locale, String> name;
    private List<String> listeners;


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setName(Map<Locale, String> name)
    {
        this.name = name;
    }


    public Map<Locale, String> getName()
    {
        return this.name;
    }


    public void setListeners(List<String> listeners)
    {
        this.listeners = listeners;
    }


    public List<String> getListeners()
    {
        return this.listeners;
    }


    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }
        if(getClass() != o.getClass())
        {
            return false;
        }
        AbstractSnSearchProviderConfiguration other = (AbstractSnSearchProviderConfiguration)o;
        return (Objects.equals(getId(), other.getId()) &&
                        Objects.equals(getName(), other.getName()) &&
                        Objects.equals(getListeners(), other.getListeners()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<Locale, String> attribute = (Object<Locale, String>)this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<Locale, String>)this.name;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        List<String> list = this.listeners;
        result = 31 * result + ((list == null) ? 0 : list.hashCode());
        return result;
    }
}
