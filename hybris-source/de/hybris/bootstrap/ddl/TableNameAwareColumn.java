package de.hybris.bootstrap.ddl;

import org.apache.ddlutils.model.Column;

public class TableNameAwareColumn extends Column
{
    private final Column target;
    private final String tableName;


    public String getTableName()
    {
        return this.tableName;
    }


    public String getName()
    {
        return this.target.getName();
    }


    public void setName(String name)
    {
        this.target.setName(name);
    }


    public String getJavaName()
    {
        return this.target.getJavaName();
    }


    public void setJavaName(String javaName)
    {
        this.target.setJavaName(javaName);
    }


    public String getDescription()
    {
        return this.target.getDescription();
    }


    public void setDescription(String description)
    {
        this.target.setDescription(description);
    }


    public boolean isPrimaryKey()
    {
        return this.target.isPrimaryKey();
    }


    public void setPrimaryKey(boolean primaryKey)
    {
        this.target.setPrimaryKey(primaryKey);
    }


    public boolean isRequired()
    {
        return this.target.isRequired();
    }


    public void setRequired(boolean required)
    {
        this.target.setRequired(required);
    }


    public boolean isAutoIncrement()
    {
        return this.target.isAutoIncrement();
    }


    public void setAutoIncrement(boolean autoIncrement)
    {
        this.target.setAutoIncrement(autoIncrement);
    }


    public int getTypeCode()
    {
        return this.target.getTypeCode();
    }


    public void setTypeCode(int typeCode)
    {
        this.target.setTypeCode(typeCode);
    }


    public String getType()
    {
        return this.target.getType();
    }


    public void setType(String type)
    {
        this.target.setType(type);
    }


    public boolean isOfNumericType()
    {
        return this.target.isOfNumericType();
    }


    public boolean isOfTextType()
    {
        return this.target.isOfTextType();
    }


    public boolean isOfBinaryType()
    {
        return this.target.isOfBinaryType();
    }


    public boolean isOfSpecialType()
    {
        return this.target.isOfSpecialType();
    }


    public String getSize()
    {
        return this.target.getSize();
    }


    public int getSizeAsInt()
    {
        return this.target.getSizeAsInt();
    }


    public void setSize(String size)
    {
        this.target.setSize(size);
    }


    public int getScale()
    {
        return this.target.getScale();
    }


    public void setScale(int scale)
    {
        this.target.setScale(scale);
    }


    public void setSizeAndScale(int size, int scale)
    {
        this.target.setSizeAndScale(size, scale);
    }


    public int getPrecisionRadix()
    {
        return this.target.getPrecisionRadix();
    }


    public void setPrecisionRadix(int precisionRadix)
    {
        this.target.setPrecisionRadix(precisionRadix);
    }


    public String getDefaultValue()
    {
        return this.target.getDefaultValue();
    }


    public Object getParsedDefaultValue()
    {
        return this.target.getParsedDefaultValue();
    }


    public void setDefaultValue(String defaultValue)
    {
        this.target.setDefaultValue(defaultValue);
    }


    public Object clone() throws CloneNotSupportedException
    {
        return this.target.clone();
    }


    public boolean equals(Object obj)
    {
        return this.target.equals(obj);
    }


    public int hashCode()
    {
        return this.target.hashCode();
    }


    public String toString()
    {
        return this.target.toString();
    }


    public String toVerboseString()
    {
        return this.target.toVerboseString();
    }


    public TableNameAwareColumn(Column target, String tableName)
    {
        this.target = target;
        this.tableName = tableName;
    }
}
