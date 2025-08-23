package de.hybris.platform.persistence.polyglot.config.supplier;

import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

public class PropertyTypeCodeDefinition
{
    final String typeCode;
    final String qualifier;
    final String qualifierType;


    public PropertyTypeCodeDefinition(String typeCode, String qualifier, String qualifierType)
    {
        this.typeCode = typeCode;
        this.qualifier = qualifier;
        this.qualifierType = qualifierType;
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.typeCode, this.qualifier, this.qualifierType});
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || o.getClass() != getClass())
        {
            return false;
        }
        PropertyTypeCodeDefinition that = (PropertyTypeCodeDefinition)o;
        return (Objects.equals(this.typeCode, that.typeCode) &&
                        Objects.equals(this.qualifier, that.qualifier) &&
                        Objects.equals(this.qualifierType, that.qualifierType));
    }


    public String toString()
    {
        String str = this.typeCode;
        if(StringUtils.isNotEmpty(this.qualifier))
        {
            if(StringUtils.isNotEmpty(this.qualifierType))
            {
                str = str + "[" + str + "=" + this.qualifier + "]";
            }
            else
            {
                str = str + "[" + str + "]";
            }
        }
        return str;
    }
}
