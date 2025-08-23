package de.hybris.platform.directpersistence.record;

public class ColumnPayload
{
    private final Class declaredTypeClass;
    private final String columnName;
    private final Object value;
    private final TargetTableType targetTableType;


    private ColumnPayload(Builder builder)
    {
        this.declaredTypeClass = builder.declaredTypeClass;
        this.columnName = builder.columnName;
        this.value = builder.value;
        this.targetTableType = builder.targetTableType;
    }


    public Class getDeclaredTypeClass()
    {
        return this.declaredTypeClass;
    }


    public String getColumnName()
    {
        return this.columnName;
    }


    public Object getValue()
    {
        return this.value;
    }


    public TargetTableType getTargetTableType()
    {
        return this.targetTableType;
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
        ColumnPayload that = (ColumnPayload)o;
        if((this.columnName != null) ? !this.columnName.equals(that.columnName) : (that.columnName != null))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        return (this.columnName != null) ? this.columnName.hashCode() : 0;
    }


    public String toString()
    {
        return this.columnName + "=" + this.columnName;
    }


    public static Builder builder()
    {
        return new Builder();
    }
}
