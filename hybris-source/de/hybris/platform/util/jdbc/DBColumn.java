package de.hybris.platform.util.jdbc;

public class DBColumn
{
    private final int UNDEFINED = -1;
    private final DBTable table;
    private final String columnName;
    private String typeDef;
    private final int dataType;
    private boolean nullable;
    private boolean primaryKey;
    private final int columnSize;
    private final int decimalDigits;
    private final String defaultValue;
    private final String dbName;
    private boolean exists;
    private boolean typedef_modified = false;
    private boolean nullable_modified = false;
    private boolean primkey_modified = false;


    DBColumn(DBTable table, CachingSchemaProvider.ColumnsData columnData)
    {
        this.table = table;
        this.columnName = columnData.getColumnName();
        this.dataType = columnData.getDataType().intValue();
        this.columnSize = columnData.getColumnSize().intValue();
        this.decimalDigits = columnData.getDecimalDigits().intValue();
        this.defaultValue = columnData.getDefaultValue();
        int nullableInt = columnData.getNullableInt().intValue();
        String nullableStr = columnData.getNullableStr();
        this.nullable = (nullableInt != 0 && !"NO".equalsIgnoreCase(nullableStr));
        this.dbName = table.getSchema().getDbName();
        String typeName = columnData.getTypeName();
        StringBuilder sb = new StringBuilder();
        if(typeName != null && typeName.length() > 0)
        {
            sb.append(typeName);
        }
        else
        {
            switch(this.dataType)
            {
                case 12:
                    sb.append("varchar");
                    break;
                case 1:
                    sb.append("char");
                    break;
                case -1:
                    sb.append("longvarchar");
                    break;
                case -5:
                    sb.append("bigint");
                    break;
                case 4:
                    sb.append("integer");
                    break;
                case 5:
                    sb.append("smallint");
                    break;
                case -6:
                    sb.append("tinyint");
                    break;
                case 3:
                    sb.append("decimal");
                    break;
                case 8:
                    sb.append("double");
                    break;
                case 6:
                    sb.append("float");
                    break;
                case 2:
                    sb.append("numeric");
                    break;
                case -7:
                    sb.append("bit");
                    break;
                case 16:
                    sb.append("boolean");
                    break;
                case 2004:
                    sb.append("blob");
                    break;
                case 2005:
                    sb.append("clob");
                    break;
                case -4:
                    sb.append("longvarbinary");
                    break;
            }
        }
        if(sb.indexOf("(") < 0)
        {
            switch(this.dataType)
            {
                case -6:
                case -5:
                case -1:
                case 1:
                case 4:
                case 5:
                case 12:
                    sb.append("(").append(this.columnSize).append(")");
                    break;
                case 2:
                case 3:
                case 6:
                case 8:
                    sb.append("(").append(this.columnSize).append(",").append(this.decimalDigits).append(")");
                    break;
            }
        }
        if(this.defaultValue != null && this.defaultValue.length() > 0)
        {
            switch(this.dataType)
            {
                case -1:
                case 1:
                case 12:
                case 2005:
                    sb.append(" default '").append(this.defaultValue).append("'");
                    break;
                default:
                    sb.append(" default ").append(this.defaultValue);
                    break;
            }
        }
        if(!isNullable())
        {
            sb.append(" not null");
        }
        this.typeDef = sb.toString();
        this.exists = true;
    }


    DBColumn(DBTable table, String columnName, String typeDef)
    {
        if(table == null)
        {
            throw new NullPointerException("table was null");
        }
        if(columnName == null)
        {
            throw new NullPointerException("column name was null");
        }
        if(typeDef == null)
        {
            throw new NullPointerException("sql type definition was null");
        }
        this.table = table;
        this.columnName = columnName;
        this.typeDef = typeDef;
        this.exists = false;
        this.columnSize = -1;
        this.dataType = -1;
        this.decimalDigits = -1;
        this.defaultValue = null;
        this.dbName = table.getSchema().getDbName();
    }


    public DBTable getTable()
    {
        return this.table;
    }


    public String getDbName()
    {
        return this.dbName;
    }


    public boolean hasChanged()
    {
        return (!columnExists() || this.nullable_modified || this.typedef_modified || this.primkey_modified);
    }


    void notifyChangesPersisted(boolean tableDroped)
    {
        this.exists = !tableDroped;
        this.nullable_modified = false;
        this.typedef_modified = false;
        this.primkey_modified = false;
    }


    public boolean nullableHasChanged()
    {
        return this.nullable_modified;
    }


    public boolean columnExists()
    {
        return this.exists;
    }


    public String getSQLTypeDefinition()
    {
        return this.typeDef;
    }


    public void setSQLTypeDefinition(String newDef)
    {
        if(this.typeDef != newDef && (this.typeDef == null || !this.typeDef.equalsIgnoreCase(newDef)))
        {
            this.typeDef = newDef;
            this.typedef_modified = true;
        }
    }


    public boolean isPrimaryKey()
    {
        return this.primaryKey;
    }


    public void setIsPrimaryKey(boolean pk)
    {
        if(this.primaryKey != pk)
        {
            this.primaryKey = pk;
            this.primkey_modified = true;
        }
    }


    public final String getColumnName()
    {
        return this.columnName;
    }


    public int getDataType()
    {
        return this.dataType;
    }


    public final boolean isNullable()
    {
        return this.nullable;
    }


    public final void setNullable(boolean nullable)
    {
        if(this.nullable != nullable)
        {
            this.nullable = nullable;
            this.nullable_modified = true;
        }
    }


    public final String toString()
    {
        return getTable().getName() + "." + getTable().getName() + "(dataType=" + getColumnName() + ",nullable=" + getDataType() + "):" + isNullable();
    }


    public int getColumnSize()
    {
        return this.columnSize;
    }


    public int getDecimalDigits()
    {
        return this.decimalDigits;
    }
}
