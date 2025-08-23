package de.hybris.platform.searchservices.admin.data;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SnSynonymDictionary implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private Map<Locale, String> name;
    private List<String> languageIds;
    private List<SnSynonymEntry> entries;


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


    public void setLanguageIds(List<String> languageIds)
    {
        this.languageIds = languageIds;
    }


    public List<String> getLanguageIds()
    {
        return this.languageIds;
    }


    public void setEntries(List<SnSynonymEntry> entries)
    {
        this.entries = entries;
    }


    public List<SnSynonymEntry> getEntries()
    {
        return this.entries;
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
        SnSynonymDictionary other = (SnSynonymDictionary)o;
        return (Objects.equals(getId(), other.getId()) &&
                        Objects.equals(getName(), other.getName()) &&
                        Objects.equals(getLanguageIds(), other.getLanguageIds()) &&
                        Objects.equals(getEntries(), other.getEntries()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<Locale, String> attribute = (Object<Locale, String>)this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<Locale, String>)this.name;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        List<String> list1 = this.languageIds;
        result = 31 * result + ((list1 == null) ? 0 : list1.hashCode());
        List<SnSynonymEntry> list = this.entries;
        result = 31 * result + ((list == null) ? 0 : list.hashCode());
        return result;
    }
}
