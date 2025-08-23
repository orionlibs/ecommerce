package de.hybris.platform.searchservices.admin.data;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SnLanguage implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private Map<Locale, String> name;


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
        SnLanguage other = (SnLanguage)o;
        return (Objects.equals(getId(), other.getId()) &&
                        Objects.equals(getName(), other.getName()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<Locale, String> attribute = (Object<Locale, String>)this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<Locale, String>)this.name;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
