package de.hybris.platform.searchservices.admin.data;

import de.hybris.platform.searchservices.enums.SnFieldType;
import de.hybris.platform.searchservices.enums.SnSearchTolerance;
import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SnField implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String id;
    private Map<Locale, String> name;
    private SnFieldType fieldType;
    private String valueProvider;
    private Map<String, String> valueProviderParameters;
    private Boolean retrievable;
    private Boolean searchable;
    private SnSearchTolerance searchTolerance;
    private Boolean localized;
    private String qualifierTypeId;
    private Boolean multiValued;
    private Boolean useForSuggesting;
    private Boolean useForSpellchecking;
    private Float weight;


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


    public void setFieldType(SnFieldType fieldType)
    {
        this.fieldType = fieldType;
    }


    public SnFieldType getFieldType()
    {
        return this.fieldType;
    }


    public void setValueProvider(String valueProvider)
    {
        this.valueProvider = valueProvider;
    }


    public String getValueProvider()
    {
        return this.valueProvider;
    }


    public void setValueProviderParameters(Map<String, String> valueProviderParameters)
    {
        this.valueProviderParameters = valueProviderParameters;
    }


    public Map<String, String> getValueProviderParameters()
    {
        return this.valueProviderParameters;
    }


    public void setRetrievable(Boolean retrievable)
    {
        this.retrievable = retrievable;
    }


    public Boolean getRetrievable()
    {
        return this.retrievable;
    }


    public void setSearchable(Boolean searchable)
    {
        this.searchable = searchable;
    }


    public Boolean getSearchable()
    {
        return this.searchable;
    }


    public void setSearchTolerance(SnSearchTolerance searchTolerance)
    {
        this.searchTolerance = searchTolerance;
    }


    public SnSearchTolerance getSearchTolerance()
    {
        return this.searchTolerance;
    }


    public void setLocalized(Boolean localized)
    {
        this.localized = localized;
    }


    public Boolean getLocalized()
    {
        return this.localized;
    }


    public void setQualifierTypeId(String qualifierTypeId)
    {
        this.qualifierTypeId = qualifierTypeId;
    }


    public String getQualifierTypeId()
    {
        return this.qualifierTypeId;
    }


    public void setMultiValued(Boolean multiValued)
    {
        this.multiValued = multiValued;
    }


    public Boolean getMultiValued()
    {
        return this.multiValued;
    }


    public void setUseForSuggesting(Boolean useForSuggesting)
    {
        this.useForSuggesting = useForSuggesting;
    }


    public Boolean getUseForSuggesting()
    {
        return this.useForSuggesting;
    }


    public void setUseForSpellchecking(Boolean useForSpellchecking)
    {
        this.useForSpellchecking = useForSpellchecking;
    }


    public Boolean getUseForSpellchecking()
    {
        return this.useForSpellchecking;
    }


    public void setWeight(Float weight)
    {
        this.weight = weight;
    }


    public Float getWeight()
    {
        return this.weight;
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
        SnField other = (SnField)o;
        return (Objects.equals(getId(), other.getId()) &&
                        Objects.equals(getName(), other.getName()) &&
                        Objects.equals(getFieldType(), other.getFieldType()) &&
                        Objects.equals(getValueProvider(), other.getValueProvider()) &&
                        Objects.equals(getValueProviderParameters(), other.getValueProviderParameters()) &&
                        Objects.equals(getRetrievable(), other.getRetrievable()) &&
                        Objects.equals(getSearchable(), other.getSearchable()) &&
                        Objects.equals(getSearchTolerance(), other.getSearchTolerance()) &&
                        Objects.equals(getMultiValued(), other.getMultiValued()) &&
                        Objects.equals(getUseForSuggesting(), other.getUseForSuggesting()) &&
                        Objects.equals(getUseForSpellchecking(), other.getUseForSpellchecking()) &&
                        Objects.equals(getWeight(), other.getWeight()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<Locale, String> attribute = (Object<Locale, String>)this.id;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<Locale, String>)this.name;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        SnFieldType snFieldType = this.fieldType;
        result = 31 * result + ((snFieldType == null) ? 0 : snFieldType.hashCode());
        String str = this.valueProvider;
        result = 31 * result + ((str == null) ? 0 : str.hashCode());
        Map<String, String> map = this.valueProviderParameters;
        result = 31 * result + ((map == null) ? 0 : map.hashCode());
        Boolean bool2 = this.retrievable;
        result = 31 * result + ((bool2 == null) ? 0 : bool2.hashCode());
        bool2 = this.searchable;
        result = 31 * result + ((bool2 == null) ? 0 : bool2.hashCode());
        SnSearchTolerance snSearchTolerance = this.searchTolerance;
        result = 31 * result + ((snSearchTolerance == null) ? 0 : snSearchTolerance.hashCode());
        Boolean bool1 = this.multiValued;
        result = 31 * result + ((bool1 == null) ? 0 : bool1.hashCode());
        bool1 = this.useForSuggesting;
        result = 31 * result + ((bool1 == null) ? 0 : bool1.hashCode());
        bool1 = this.useForSpellchecking;
        result = 31 * result + ((bool1 == null) ? 0 : bool1.hashCode());
        Float float_ = this.weight;
        result = 31 * result + ((float_ == null) ? 0 : float_.hashCode());
        return result;
    }
}
