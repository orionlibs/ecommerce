package de.hybris.platform.regioncache.key.legacy;

import java.io.Serializable;

public class LegacyCacheKeyType implements Serializable
{
    private final Object typeCode;
    private final String typeCodeString;
    private final String entityCode;
    private final Object additionalCode;
    private static final long serialVersionUID = 32123221L;


    public LegacyCacheKeyType(Object typeCode, String entityCode)
    {
        this(typeCode, entityCode, null);
    }


    public LegacyCacheKeyType(Object typeCode, String entityCode, Object additionalCode)
    {
        this.typeCode = typeCode;
        this.entityCode = entityCode;
        this.additionalCode = additionalCode;
        this.typeCodeString = String.valueOf(typeCode);
    }


    public String toString()
    {
        return getTypeCodeString();
    }


    public String getTypeCodeString()
    {
        return this.typeCodeString;
    }


    public int hashCode()
    {
        int prime = 31;
        int result = 1;
        result = 31 * result + ((this.additionalCode == null) ? 0 : this.additionalCode.hashCode());
        result = 31 * result + ((this.entityCode == null) ? 0 : this.entityCode.hashCode());
        result = 31 * result + ((this.typeCode == null) ? 0 : this.typeCode.hashCode());
        return result;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null)
        {
            return false;
        }
        if(getClass() != obj.getClass())
        {
            return false;
        }
        LegacyCacheKeyType other = (LegacyCacheKeyType)obj;
        if(this.additionalCode == null)
        {
            if(other.additionalCode != null)
            {
                return false;
            }
        }
        else if(!this.additionalCode.equals(other.additionalCode))
        {
            return false;
        }
        if(this.entityCode == null)
        {
            if(other.entityCode != null)
            {
                return false;
            }
        }
        else if(!this.entityCode.equals(other.entityCode))
        {
            return false;
        }
        if(this.typeCode == null)
        {
            if(other.typeCode != null)
            {
                return false;
            }
        }
        else if(!this.typeCode.equals(other.typeCode))
        {
            return false;
        }
        return true;
    }


    public static String getFullTypeName(Object typeCode)
    {
        if(typeCode == null)
        {
            return "NULL";
        }
        if(typeCode instanceof String)
        {
            return (String)typeCode;
        }
        if(typeCode instanceof LegacyCacheKeyType)
        {
            LegacyCacheKeyType type = (LegacyCacheKeyType)typeCode;
            return "" + type + "{" + type + "[" + type.getClass().getSimpleName() + ", " + type.entityCode + ", " + type.additionalCode + "(" + type.typeCode + ")]}";
        }
        return "" + typeCode + "{" + typeCode + "}";
    }
}
