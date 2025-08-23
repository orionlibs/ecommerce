package com.hybris.datahub.dto.metadata;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.hybris.datahub.dto.extension.ExportCode;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "TargetAttribute")
@XmlAccessorType(XmlAccessType.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TargetAttributeData extends AttributeData
{
    private String targetSystemName;
    private String transformationExpression;
    private ExportCode exportCode;
    private Boolean isMandatoryInHeader;
    private Boolean isLocalizable;
    private Boolean isCollection;
    private boolean isOverride;
    private boolean isDisabled;


    public String getTransformationExpression()
    {
        return this.transformationExpression;
    }


    public void setTransformationExpression(String transformationExpression)
    {
        this.transformationExpression = transformationExpression;
    }


    public ExportCode getExportCode()
    {
        return this.exportCode;
    }


    public void setExportCode(ExportCode exportCode)
    {
        this.exportCode = exportCode;
    }


    public Boolean getIsMandatoryInHeader()
    {
        return this.isMandatoryInHeader;
    }


    public void setIsMandatoryInHeader(Boolean mandatoryInHeader)
    {
        this.isMandatoryInHeader = mandatoryInHeader;
    }


    public String getTargetSystemName()
    {
        return this.targetSystemName;
    }


    public void setTargetSystemName(String targetSystemName)
    {
        this.targetSystemName = targetSystemName;
    }


    public Boolean getIsLocalizable()
    {
        return this.isLocalizable;
    }


    public void setIsLocalizable(Boolean localizable)
    {
        this.isLocalizable = localizable;
    }


    public Boolean getIsCollection()
    {
        return this.isCollection;
    }


    public void setIsCollection(Boolean collection)
    {
        this.isCollection = collection;
    }


    public boolean isOverride()
    {
        return this.isOverride;
    }


    public void setOverride(boolean isOverride)
    {
        this.isOverride = isOverride;
    }


    public boolean isDisabled()
    {
        return this.isDisabled;
    }


    public void setDisabled(boolean isDisabled)
    {
        this.isDisabled = isDisabled;
    }


    public String toString()
    {
        return "TargetAttributeData{" + super
                        .toString() + "targetSystemName='" + this.targetSystemName + "', transformationExpression='" + this.transformationExpression + "', exportCode='" + this.exportCode + "', isMandatoryInHeader=" + this.isMandatoryInHeader + ", isLocalizable=" + this.isLocalizable
                        + ", isCollection=" + this.isCollection + ", isOverride=" + this.isOverride + ", isDisabled=" + this.isDisabled + "}";
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
        TargetAttributeData that = (TargetAttributeData)o;
        if(this.isMandatoryInHeader != that.isMandatoryInHeader)
        {
            return false;
        }
        if(!this.exportCode.equals(that.exportCode))
        {
            return false;
        }
        if(!this.isCollection.equals(that.isCollection))
        {
            return false;
        }
        if(!this.isLocalizable.equals(that.isLocalizable))
        {
            return false;
        }
        if(!this.targetSystemName.equals(that.targetSystemName))
        {
            return false;
        }
        if(this.isOverride != that.isOverride)
        {
            return false;
        }
        if(this.isDisabled != that.isDisabled)
        {
            return false;
        }
        return this.transformationExpression.equals(that.transformationExpression);
    }


    public int hashCode()
    {
        int result = super.hashCode();
        result = 31 * result + this.targetSystemName.hashCode();
        result = 31 * result + this.transformationExpression.hashCode();
        result = 31 * result + this.exportCode.hashCode();
        result = 31 * result + (this.isMandatoryInHeader.booleanValue() ? 1 : 0);
        result = 31 * result + this.isLocalizable.hashCode();
        result = 31 * result + this.isCollection.hashCode();
        return result;
    }
}
