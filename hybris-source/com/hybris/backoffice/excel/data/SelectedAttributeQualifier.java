package com.hybris.backoffice.excel.data;

public class SelectedAttributeQualifier
{
    private final String name;
    private final String qualifier;


    public SelectedAttributeQualifier(String name, String qualifier)
    {
        this.name = name;
        this.qualifier = qualifier;
    }


    public String getName()
    {
        return this.name;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null)
        {
            return false;
        }
        if(o.getClass() != getClass())
        {
            return false;
        }
        SelectedAttributeQualifier that = (SelectedAttributeQualifier)o;
        if((getName() != null) ? !getName().equals(that.getName()) : (that.getName() != null))
        {
            return false;
        }
        return (getQualifier() != null) ? getQualifier().equals(that.getQualifier()) : ((that.getQualifier() == null));
    }


    public int hashCode()
    {
        int result = (getName() != null) ? getName().hashCode() : 0;
        result = 31 * result + ((getQualifier() != null) ? getQualifier().hashCode() : 0);
        return result;
    }
}
