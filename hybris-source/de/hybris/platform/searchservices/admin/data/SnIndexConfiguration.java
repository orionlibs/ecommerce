package de.hybris.platform.searchservices.admin.data;

import de.hybris.platform.searchservices.spi.data.AbstractSnSearchProviderConfiguration;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SnIndexConfiguration implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private Map<Locale, String> name;
    private String user;
    private List<SnLanguage> languages;
    private List<SnCurrency> currencies;
    private List<String> listeners;
    private AbstractSnSearchProviderConfiguration searchProviderConfiguration;
    private List<String> synonymDictionaryIds;


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


    public void setUser(String user)
    {
        this.user = user;
    }


    public String getUser()
    {
        return this.user;
    }


    public void setLanguages(List<SnLanguage> languages)
    {
        this.languages = languages;
    }


    public List<SnLanguage> getLanguages()
    {
        return this.languages;
    }


    public void setCurrencies(List<SnCurrency> currencies)
    {
        this.currencies = currencies;
    }


    public List<SnCurrency> getCurrencies()
    {
        return this.currencies;
    }


    public void setListeners(List<String> listeners)
    {
        this.listeners = listeners;
    }


    public List<String> getListeners()
    {
        return this.listeners;
    }


    public void setSearchProviderConfiguration(AbstractSnSearchProviderConfiguration searchProviderConfiguration)
    {
        this.searchProviderConfiguration = searchProviderConfiguration;
    }


    public AbstractSnSearchProviderConfiguration getSearchProviderConfiguration()
    {
        return this.searchProviderConfiguration;
    }


    public void setSynonymDictionaryIds(List<String> synonymDictionaryIds)
    {
        this.synonymDictionaryIds = synonymDictionaryIds;
    }


    public List<String> getSynonymDictionaryIds()
    {
        return this.synonymDictionaryIds;
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
        SnIndexConfiguration other = (SnIndexConfiguration)o;
        return (Objects.equals(getId(), other.getId()) &&
                        Objects.equals(getName(), other.getName()) &&
                        Objects.equals(getUser(), other.getUser()) &&
                        Objects.equals(getLanguages(), other.getLanguages()) &&
                        Objects.equals(getCurrencies(), other.getCurrencies()) &&
                        Objects.equals(getListeners(), other.getListeners()) &&
                        Objects.equals(getSearchProviderConfiguration(), other.getSearchProviderConfiguration()) &&
                        Objects.equals(getSynonymDictionaryIds(), other.getSynonymDictionaryIds()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<Locale, String> attribute = (Object<Locale, String>)this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<Locale, String>)this.name;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<Locale, String>)this.user;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        List<SnLanguage> list3 = this.languages;
        result = 31 * result + ((list3 == null) ? 0 : list3.hashCode());
        List<SnCurrency> list = this.currencies;
        result = 31 * result + ((list == null) ? 0 : list.hashCode());
        List<String> list2 = this.listeners;
        result = 31 * result + ((list2 == null) ? 0 : list2.hashCode());
        AbstractSnSearchProviderConfiguration abstractSnSearchProviderConfiguration = this.searchProviderConfiguration;
        result = 31 * result + ((abstractSnSearchProviderConfiguration == null) ? 0 : abstractSnSearchProviderConfiguration.hashCode());
        List<String> list1 = this.synonymDictionaryIds;
        result = 31 * result + ((list1 == null) ? 0 : list1.hashCode());
        return result;
    }
}
