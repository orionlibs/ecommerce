package de.hybris.bootstrap.ddl.model;

public class AttributeDescriptor
{
    private String name;
    private String inheritancePathString;
    private String columnName;
    private String attributeType;
    private String qualifierinternal;
    private long pk;


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getInheritancePathString()
    {
        return this.inheritancePathString;
    }


    public void setInheritancePathString(String inheritancePathString)
    {
        this.inheritancePathString = inheritancePathString;
    }


    public String getColumnName()
    {
        return this.columnName;
    }


    public void setColumnName(String columnName)
    {
        this.columnName = columnName;
    }


    public String getAttributeType()
    {
        return this.attributeType;
    }


    public void setAttributeType(String attributeType)
    {
        this.attributeType = attributeType;
    }


    public String getQualifierInternal()
    {
        return this.qualifierinternal;
    }


    public void setQualifierInternal(String qualifierInternal)
    {
        this.qualifierinternal = qualifierInternal;
    }


    public long getPk()
    {
        return this.pk;
    }


    public void setPk(long pk)
    {
        this.pk = pk;
    }
}
