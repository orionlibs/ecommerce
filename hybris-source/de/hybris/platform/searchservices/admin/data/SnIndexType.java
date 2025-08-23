package de.hybris.platform.searchservices.admin.data;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SnIndexType implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private Map<Locale, String> name;
    private String itemComposedType;
    private String identityProvider;
    private Map<String, String> identityProviderParameters;
    private String defaultValueProvider;
    private Map<String, String> defaultValueProviderParameters;
    private List<String> listeners;
    private String indexConfigurationId;
    private Map<String, SnField> fields;
    private List<String> catalogsIds;
    private List<SnCatalogVersion> catalogVersions;


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


    public void setItemComposedType(String itemComposedType)
    {
        this.itemComposedType = itemComposedType;
    }


    public String getItemComposedType()
    {
        return this.itemComposedType;
    }


    public void setIdentityProvider(String identityProvider)
    {
        this.identityProvider = identityProvider;
    }


    public String getIdentityProvider()
    {
        return this.identityProvider;
    }


    public void setIdentityProviderParameters(Map<String, String> identityProviderParameters)
    {
        this.identityProviderParameters = identityProviderParameters;
    }


    public Map<String, String> getIdentityProviderParameters()
    {
        return this.identityProviderParameters;
    }


    public void setDefaultValueProvider(String defaultValueProvider)
    {
        this.defaultValueProvider = defaultValueProvider;
    }


    public String getDefaultValueProvider()
    {
        return this.defaultValueProvider;
    }


    public void setDefaultValueProviderParameters(Map<String, String> defaultValueProviderParameters)
    {
        this.defaultValueProviderParameters = defaultValueProviderParameters;
    }


    public Map<String, String> getDefaultValueProviderParameters()
    {
        return this.defaultValueProviderParameters;
    }


    public void setListeners(List<String> listeners)
    {
        this.listeners = listeners;
    }


    public List<String> getListeners()
    {
        return this.listeners;
    }


    public void setIndexConfigurationId(String indexConfigurationId)
    {
        this.indexConfigurationId = indexConfigurationId;
    }


    public String getIndexConfigurationId()
    {
        return this.indexConfigurationId;
    }


    public void setFields(Map<String, SnField> fields)
    {
        this.fields = fields;
    }


    public Map<String, SnField> getFields()
    {
        return this.fields;
    }


    public void setCatalogsIds(List<String> catalogsIds)
    {
        this.catalogsIds = catalogsIds;
    }


    public List<String> getCatalogsIds()
    {
        return this.catalogsIds;
    }


    public void setCatalogVersions(List<SnCatalogVersion> catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    public List<SnCatalogVersion> getCatalogVersions()
    {
        return this.catalogVersions;
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
        SnIndexType other = (SnIndexType)o;
        return (Objects.equals(getId(), other.getId()) &&
                        Objects.equals(getName(), other.getName()) &&
                        Objects.equals(getItemComposedType(), other.getItemComposedType()) &&
                        Objects.equals(getIdentityProvider(), other.getIdentityProvider()) &&
                        Objects.equals(getIdentityProviderParameters(), other.getIdentityProviderParameters()) &&
                        Objects.equals(getDefaultValueProvider(), other.getDefaultValueProvider()) &&
                        Objects.equals(getDefaultValueProviderParameters(), other.getDefaultValueProviderParameters()) &&
                        Objects.equals(getListeners(), other.getListeners()) &&
                        Objects.equals(getIndexConfigurationId(), other.getIndexConfigurationId()) &&
                        Objects.equals(getFields(), other.getFields()) &&
                        Objects.equals(getCatalogsIds(), other.getCatalogsIds()) &&
                        Objects.equals(getCatalogVersions(), other.getCatalogVersions()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<Locale, String> attribute = (Object<Locale, String>)this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<Locale, String>)this.name;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<Locale, String>)this.itemComposedType;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<Locale, String>)this.identityProvider;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        Map<String, String> map2 = this.identityProviderParameters;
        result = 31 * result + ((map2 == null) ? 0 : map2.hashCode());
        String str2 = this.defaultValueProvider;
        result = 31 * result + ((str2 == null) ? 0 : str2.hashCode());
        Map<String, String> map1 = this.defaultValueProviderParameters;
        result = 31 * result + ((map1 == null) ? 0 : map1.hashCode());
        List<String> list2 = this.listeners;
        result = 31 * result + ((list2 == null) ? 0 : list2.hashCode());
        String str1 = this.indexConfigurationId;
        result = 31 * result + ((str1 == null) ? 0 : str1.hashCode());
        Map<String, SnField> map = this.fields;
        result = 31 * result + ((map == null) ? 0 : map.hashCode());
        List<String> list1 = this.catalogsIds;
        result = 31 * result + ((list1 == null) ? 0 : list1.hashCode());
        List<SnCatalogVersion> list = this.catalogVersions;
        result = 31 * result + ((list == null) ? 0 : list.hashCode());
        return result;
    }
}
