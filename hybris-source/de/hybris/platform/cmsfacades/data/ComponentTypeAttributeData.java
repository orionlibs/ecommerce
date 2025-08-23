package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ComponentTypeAttributeData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String qualifier;
    private boolean required;
    private Boolean localized;
    private boolean editable;
    private String cmsStructureType;
    private String cmsStructureEnumType;
    private String i18nKey;
    private boolean paged;
    private boolean collection;
    private String dependsOn;
    private List<OptionData> options;
    private String idAttribute;
    private List<String> labelAttributes;
    private Map<String, String> params;
    private String uri;
    private Map<String, String> subTypes;
    private Set<String> containedTypes;
    private String placeholder;


    public void setQualifier(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setRequired(boolean required)
    {
        this.required = required;
    }


    public boolean isRequired()
    {
        return this.required;
    }


    public void setLocalized(Boolean localized)
    {
        this.localized = localized;
    }


    public Boolean getLocalized()
    {
        return this.localized;
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public void setCmsStructureType(String cmsStructureType)
    {
        this.cmsStructureType = cmsStructureType;
    }


    public String getCmsStructureType()
    {
        return this.cmsStructureType;
    }


    public void setCmsStructureEnumType(String cmsStructureEnumType)
    {
        this.cmsStructureEnumType = cmsStructureEnumType;
    }


    public String getCmsStructureEnumType()
    {
        return this.cmsStructureEnumType;
    }


    public void setI18nKey(String i18nKey)
    {
        this.i18nKey = i18nKey;
    }


    public String getI18nKey()
    {
        return this.i18nKey;
    }


    public void setPaged(boolean paged)
    {
        this.paged = paged;
    }


    public boolean isPaged()
    {
        return this.paged;
    }


    public void setCollection(boolean collection)
    {
        this.collection = collection;
    }


    public boolean isCollection()
    {
        return this.collection;
    }


    public void setDependsOn(String dependsOn)
    {
        this.dependsOn = dependsOn;
    }


    public String getDependsOn()
    {
        return this.dependsOn;
    }


    public void setOptions(List<OptionData> options)
    {
        this.options = options;
    }


    public List<OptionData> getOptions()
    {
        return this.options;
    }


    public void setIdAttribute(String idAttribute)
    {
        this.idAttribute = idAttribute;
    }


    public String getIdAttribute()
    {
        return this.idAttribute;
    }


    public void setLabelAttributes(List<String> labelAttributes)
    {
        this.labelAttributes = labelAttributes;
    }


    public List<String> getLabelAttributes()
    {
        return this.labelAttributes;
    }


    public void setParams(Map<String, String> params)
    {
        this.params = params;
    }


    public Map<String, String> getParams()
    {
        return this.params;
    }


    public void setUri(String uri)
    {
        this.uri = uri;
    }


    public String getUri()
    {
        return this.uri;
    }


    public void setSubTypes(Map<String, String> subTypes)
    {
        this.subTypes = subTypes;
    }


    public Map<String, String> getSubTypes()
    {
        return this.subTypes;
    }


    public void setContainedTypes(Set<String> containedTypes)
    {
        this.containedTypes = containedTypes;
    }


    public Set<String> getContainedTypes()
    {
        return this.containedTypes;
    }


    public void setPlaceholder(String placeholder)
    {
        this.placeholder = placeholder;
    }


    public String getPlaceholder()
    {
        return this.placeholder;
    }
}
