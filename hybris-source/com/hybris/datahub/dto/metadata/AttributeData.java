package com.hybris.datahub.dto.metadata;

public abstract class AttributeData
{
    private String name;
    private String itemType;
    private boolean isSecured;


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getItemType()
    {
        return this.itemType;
    }


    public void setItemType(String itemType)
    {
        this.itemType = itemType;
    }


    public boolean getIsSecured()
    {
        return this.isSecured;
    }


    public void setIsSecured(boolean secured)
    {
        this.isSecured = secured;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(!(o instanceof AttributeData))
        {
            return false;
        }
        AttributeData that = (AttributeData)o;
        if((this.itemType != null) ? !this.itemType.equals(that.itemType) : (that.itemType != null))
        {
            return false;
        }
        if((this.name != null) ? !this.name.equals(that.name) : (that.name != null))
        {
            return false;
        }
    }


    public int hashCode()
    {
        int result = (this.name != null) ? this.name.hashCode() : 0;
        result = 31 * result + ((this.itemType != null) ? this.itemType.hashCode() : 0);
        return result;
    }
}
