package com.hybris.datahub.dto.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hybris.datahub.dto.extension.ExportCode;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TargetItemType")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetItemTypeData extends ItemTypeData
{
    private String targetSystemName;
    private String canonicalItemSource;
    private boolean isUpdatable;
    private String description;
    private ExportCode exportCode;
    private String filterExpression;
    private String filteredItemPublicationStatus;
    private Set<String> dependencies;


    public String getTargetSystemName()
    {
        return this.targetSystemName;
    }


    public void setTargetSystemName(String targetSystemName)
    {
        this.targetSystemName = targetSystemName;
    }


    public TargetItemTypeData in(String targetSystemName)
    {
        setTargetSystemName(targetSystemName);
        return this;
    }


    public String getCanonicalItemSource()
    {
        return this.canonicalItemSource;
    }


    public void setCanonicalItemSource(String canonicalItemSource)
    {
        this.canonicalItemSource = canonicalItemSource;
    }


    public TargetItemTypeData from(String canonicalItemType)
    {
        setCanonicalItemSource(canonicalItemType);
        return this;
    }


    public boolean getIsUpdatable()
    {
        return this.isUpdatable;
    }


    public void setIsUpdatable(boolean isUpdatable)
    {
        this.isUpdatable = isUpdatable;
    }


    public TargetItemTypeData updatable()
    {
        setIsUpdatable(true);
        return this;
    }


    public TargetItemTypeData withName(String name)
    {
        setName(name);
        return this;
    }


    public TargetItemTypeData withDescription(String description)
    {
        setDescription(description);
        return this;
    }


    public ExportCode getExportCode()
    {
        return this.exportCode;
    }


    public void setExportCode(ExportCode exportCode)
    {
        this.exportCode = exportCode;
    }


    public TargetItemTypeData withExportCode(ExportCode exportCode)
    {
        setExportCode(exportCode);
        return this;
    }


    public Set<String> getDependencies()
    {
        return this.dependencies;
    }


    public TargetItemTypeData setDependencies(Set<String> d)
    {
        this.dependencies = d;
        return this;
    }


    public TargetItemTypeData withDependencies(String... targetTypes)
    {
        if(this.dependencies == null)
        {
            this.dependencies = new HashSet<>();
        }
        this.dependencies.addAll(Arrays.asList(targetTypes));
        return this;
    }


    public String getFilterExpression()
    {
        return this.filterExpression;
    }


    public void setFilterExpression(String filterExpression)
    {
        this.filterExpression = filterExpression;
    }


    public String getFilteredItemPublicationStatus()
    {
        return this.filteredItemPublicationStatus;
    }


    public void setFilteredItemPublicationStatus(String filteredItemPublicationStatus)
    {
        this.filteredItemPublicationStatus = filteredItemPublicationStatus;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        if(!super.equals(o))
        {
            return false;
        }
        TargetItemTypeData that = (TargetItemTypeData)o;
        if((this.canonicalItemSource != null) ? !this.canonicalItemSource.equals(that.canonicalItemSource) : (that.canonicalItemSource != null))
        {
            return false;
        }
        if((this.description != null) ? !this.description.equals(that.description) : (that.description != null))
        {
            return false;
        }
        if(this.isUpdatable != that.isUpdatable)
        {
            return false;
        }
        if((this.exportCode != null) ? !this.exportCode.equals(that.exportCode) : (that.exportCode != null))
        {
            return false;
        }
        if((this.targetSystemName != null) ? !this.targetSystemName.equals(that.targetSystemName) : (that.targetSystemName != null))
        {
            return false;
        }
    }


    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + ((this.targetSystemName != null) ? this.targetSystemName.hashCode() : 0);
        result = 31 * result + ((this.canonicalItemSource != null) ? this.canonicalItemSource.hashCode() : 0);
        result = 31 * result + (this.isUpdatable ? 1 : 0);
        result = 31 * result + ((this.description != null) ? this.description.hashCode() : 0);
        result = 31 * result + ((this.exportCode != null) ? this.exportCode.hashCode() : 0);
        return result;
    }


    public String toString()
    {
        return "TargetItemTypeData{type=" + getName() + " @ " + this.targetSystemName + "}";
    }
}
