package de.hybris.platform.configurablebundlecockpits.admincockpit.services.security.data;

public class UIAccessRightDependency
{
    private String typeCode;
    private String attributeName;
    private String dependentOnAttributeName;
    private Boolean isNull;


    public String getTypeCode()
    {
        return this.typeCode;
    }


    public void setTypeCode(String typeCode)
    {
        this.typeCode = typeCode;
    }


    public String getAttributeName()
    {
        return this.attributeName;
    }


    public void setAttributeName(String attributeName)
    {
        this.attributeName = attributeName;
    }


    public String getDependentOnAttributeName()
    {
        return this.dependentOnAttributeName;
    }


    public void setDependentOnAttributeName(String dependentOnAttributeName)
    {
        this.dependentOnAttributeName = dependentOnAttributeName;
    }


    public Boolean getIsNull()
    {
        return this.isNull;
    }


    public void setIsNull(Boolean isNull)
    {
        this.isNull = isNull;
    }
}
