package de.hybris.platform.util.jdbc;

import de.hybris.bootstrap.typesystem.YAttributeDeployment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DBTable
{
    private final DBSchema schema;
    private final String name;
    private Map<String, DBColumn> columns;
    private Set<DBTableIndex> indexes;
    private boolean exists;
    private boolean dropFlag;
    private ColumnOptimizer optimizer;


    DBTable(DBSchema schema, String name, boolean exists)
    {
        this.name = name;
        this.schema = schema;
        this.exists = exists;
    }


    DBColumn addExistingColumn(DBColumn newOne)
    {
        if(this.columns == null)
        {
            this.columns = new LinkedHashMap<>();
        }
        if(hasColumn(newOne.getColumnName()))
        {
            throw new IllegalStateException("column '" + newOne.getColumnName() + "' already exists within table " + this);
        }
        this.columns.put(newOne.getColumnName().toLowerCase(), newOne);
        return newOne;
    }


    protected ColumnOptimizer getOptimizer()
    {
        if(this.optimizer == null)
        {
            this.optimizer = new ColumnOptimizer(this);
        }
        return this.optimizer;
    }


    public DBColumn addColumn(YAttributeDeployment aDepl, String qualifier, String columnName, String sqlDef, boolean isPK, boolean adjustName)
    {
        return getOptimizer().addColumn(aDepl, qualifier, columnName, sqlDef, isPK, adjustName);
    }


    DBColumn createColumn(String columnName, String sqlDefinition, boolean isPrimaryKey)
    {
        if(this.columns == null)
        {
            this.columns = new LinkedHashMap<>();
        }
        if(getColumn(columnName) != null)
        {
            throw new IllegalStateException("column '" + columnName + "' already exists within table " + this);
        }
        DBColumn newOne = new DBColumn(this, columnName, sqlDefinition);
        newOne.setIsPrimaryKey(isPrimaryKey);
        this.columns.put(columnName.toLowerCase(), newOne);
        return newOne;
    }


    public boolean hasChanged()
    {
        if(!tableExists() || tableRemoved())
        {
            return true;
        }
        for(DBColumn col : getColumns())
        {
            if(col.hasChanged())
            {
                return true;
            }
        }
        return false;
    }


    void notifyChangesPersisted()
    {
        for(DBColumn col : getColumns())
        {
            col.notifyChangesPersisted(this.dropFlag);
        }
        for(DBTableIndex idx : getIndexes())
        {
            idx.notifyChangesPersisted(this.dropFlag);
        }
        this.exists = !this.dropFlag;
        this.dropFlag = false;
    }


    public void drop()
    {
        this.dropFlag = true;
    }


    public boolean tableRemoved()
    {
        return this.dropFlag;
    }


    public boolean tableExists()
    {
        return this.exists;
    }


    void setTableExists(boolean exists)
    {
        this.exists = exists;
    }


    public boolean columnExists(String name)
    {
        DBColumn col = getColumn(name);
        return (col != null && col.columnExists());
    }


    public DBColumn getColumn(String name)
    {
        for(DBColumn col : getColumns())
        {
            if(col.getColumnName().equalsIgnoreCase(name))
            {
                return col;
            }
        }
        return null;
    }


    private boolean hasColumn(String name)
    {
        return this.columns.keySet().contains(name.toLowerCase());
    }


    public Set<DBColumn> getChangedColumns()
    {
        if(this.columns == null || this.columns.isEmpty())
        {
            return Collections.EMPTY_SET;
        }
        Set<DBColumn> ret = new LinkedHashSet<>();
        for(DBColumn col : this.columns.values())
        {
            if(col.hasChanged())
            {
                ret.add(col);
            }
        }
        return ret;
    }


    public Set<DBColumn> getColumns()
    {
        return (this.columns != null) ? new HashSet<>(this.columns.values()) : Collections.EMPTY_SET;
    }


    public String getName()
    {
        return this.name;
    }


    public DBSchema getSchema()
    {
        return this.schema;
    }


    public DBTableIndex addExistingIndex(String indexName)
    {
        DBTableIndex ret = new DBTableIndex(this, indexName);
        if(this.indexes == null)
        {
            this.indexes = new LinkedHashSet<>();
        }
        ret.setExists(true);
        this.indexes.add(ret);
        return ret;
    }


    public DBTableIndex getIndex(String indexName)
    {
        if(this.indexes != null)
        {
            for(DBTableIndex idx : this.indexes)
            {
                if(idx.getIndexName().equalsIgnoreCase(indexName))
                {
                    return idx;
                }
            }
        }
        return null;
    }


    public boolean indexExists(String indexName)
    {
        DBTableIndex index = getIndex(indexName);
        return (index != null && index.indexExists());
    }


    public Set<DBTableIndex> getIndexes()
    {
        return (this.indexes != null) ? Collections.<DBTableIndex>unmodifiableSet(this.indexes) : Collections.EMPTY_SET;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(getSchema().getSchemaName()).append(".").append(getName());
        sb.append("[");
        List<DBColumn> l = new ArrayList<>(getColumns());
        Collections.sort(l, (Comparator<? super DBColumn>)new Object(this));
        boolean first = true;
        for(DBColumn col : l)
        {
            if(first)
            {
                first = false;
            }
            else
            {
                sb.append(";");
            }
            sb.append(col.getColumnName()).append("(dataType=").append(col.getDataType()).append(",nullable=")
                            .append(col.isNullable()).append(")");
        }
        sb.append("]");
        return sb.toString();
    }
}
