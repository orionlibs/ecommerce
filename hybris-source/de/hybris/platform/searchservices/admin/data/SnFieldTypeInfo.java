package de.hybris.platform.searchservices.admin.data;

import de.hybris.platform.searchservices.enums.SnFieldType;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class SnFieldTypeInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    private SnFieldType fieldType;
    private Class<?> valueType;
    private boolean facetable;
    private boolean sortable;
    private boolean searchable;
    private boolean groupable;
    private List<String> supportedQueryTypes;


    public void setFieldType(SnFieldType fieldType)
    {
        this.fieldType = fieldType;
    }


    public SnFieldType getFieldType()
    {
        return this.fieldType;
    }


    public void setValueType(Class<?> valueType)
    {
        this.valueType = valueType;
    }


    public Class<?> getValueType()
    {
        return this.valueType;
    }


    public void setFacetable(boolean facetable)
    {
        this.facetable = facetable;
    }


    public boolean isFacetable()
    {
        return this.facetable;
    }


    public void setSortable(boolean sortable)
    {
        this.sortable = sortable;
    }


    public boolean isSortable()
    {
        return this.sortable;
    }


    public void setSearchable(boolean searchable)
    {
        this.searchable = searchable;
    }


    public boolean isSearchable()
    {
        return this.searchable;
    }


    public void setGroupable(boolean groupable)
    {
        this.groupable = groupable;
    }


    public boolean isGroupable()
    {
        return this.groupable;
    }


    public void setSupportedQueryTypes(List<String> supportedQueryTypes)
    {
        this.supportedQueryTypes = supportedQueryTypes;
    }


    public List<String> getSupportedQueryTypes()
    {
        return this.supportedQueryTypes;
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
        SnFieldTypeInfo other = (SnFieldTypeInfo)o;
        return (Objects.equals(getFieldType(), other.getFieldType()) &&
                        Objects.equals(getValueType(), other.getValueType()) &&
                        Objects.equals(Boolean.valueOf(isFacetable()), Boolean.valueOf(other.isFacetable())) &&
                        Objects.equals(Boolean.valueOf(isSortable()), Boolean.valueOf(other.isSortable())) &&
                        Objects.equals(Boolean.valueOf(isSearchable()), Boolean.valueOf(other.isSearchable())) &&
                        Objects.equals(Boolean.valueOf(isGroupable()), Boolean.valueOf(other.isGroupable())) &&
                        Objects.equals(getSupportedQueryTypes(), other.getSupportedQueryTypes()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<?> attribute = (Object<?>)this.fieldType;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<?>)this.valueType;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<?>)Boolean.valueOf(this.facetable);
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<?>)Boolean.valueOf(this.sortable);
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<?>)Boolean.valueOf(this.searchable);
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<?>)Boolean.valueOf(this.groupable);
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        List<String> list = this.supportedQueryTypes;
        result = 31 * result + ((list == null) ? 0 : list.hashCode());
        return result;
    }
}
