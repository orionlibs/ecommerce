package de.hybris.platform.util.jdbc;

import de.hybris.bootstrap.typesystem.YDBTypeMapping;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.CaseInsensitiveMap;

public class DBSchema
{
    private final YDBTypeMapping dbTypeMappings;
    private final String schemaName;
    private final String dbName;
    private final String label;
    private Map<String, DBTable> tables;


    public DBSchema(String label, String schemaName, String dbName, YDBTypeMapping dbTypeMappings)
    {
        this.label = label;
        this.schemaName = schemaName;
        this.dbTypeMappings = dbTypeMappings;
        this.dbName = dbName;
    }


    public String toString()
    {
        return this.label + "(" + this.label + ")";
    }


    public String getLabel()
    {
        return this.label;
    }


    public String getSchemaName()
    {
        return this.schemaName;
    }


    public String getDbName()
    {
        return this.dbName;
    }


    public YDBTypeMapping getDBTypeMappings()
    {
        return this.dbTypeMappings;
    }


    protected Map<String, DBTable> getTableMap(boolean create)
    {
        return (this.tables != null) ? this.tables : (create ? (this.tables = (Map<String, DBTable>)new CaseInsensitiveMap()) : Collections.EMPTY_MAP);
    }


    public boolean hasChanged()
    {
        for(Map.Entry<String, DBTable> entry : getTableMap(false).entrySet())
        {
            if(((DBTable)entry.getValue()).hasChanged())
            {
                return true;
            }
        }
        return false;
    }


    public Set<DBTable> getTablesToDrop()
    {
        Set<DBTable> ret = new HashSet<>();
        for(Map.Entry<String, DBTable> entry : getTableMap(false).entrySet())
        {
            if(((DBTable)entry.getValue()).tableRemoved())
            {
                ret.add(entry.getValue());
            }
        }
        return ret;
    }


    public Set<DBTable> getTablesToCreate()
    {
        Set<DBTable> ret = new HashSet<>();
        for(Map.Entry<String, DBTable> entry : getTableMap(false).entrySet())
        {
            if(!((DBTable)entry.getValue()).tableExists())
            {
                ret.add(entry.getValue());
            }
        }
        return ret;
    }


    public Set<DBTable> getModifiedTables()
    {
        Set<DBTable> ret = new HashSet<>();
        for(Map.Entry<String, DBTable> entry : getTableMap(false).entrySet())
        {
            if(((DBTable)entry.getValue()).tableExists() && !((DBTable)entry.getValue()).tableRemoved() && ((DBTable)entry.getValue()).hasChanged())
            {
                ret.add(entry.getValue());
            }
        }
        return ret;
    }


    public Collection<DBTable> getAllChangedTables()
    {
        Set<DBTable> ret = new HashSet<>();
        for(Map.Entry<String, DBTable> entry : getTableMap(false).entrySet())
        {
            if(((DBTable)entry.getValue()).hasChanged())
            {
                ret.add(entry.getValue());
            }
        }
        return ret;
    }


    public int getAllTablesCount()
    {
        return getTableMap(false).size();
    }


    public Collection<DBTable> getAllTables()
    {
        return Collections.unmodifiableCollection(getTableMap(false).values());
    }


    public boolean tableExists(String tableName)
    {
        DBTable tbl = getTable(tableName);
        return (tbl != null && tbl.tableExists());
    }


    public DBTable getTable(String tableName)
    {
        return getTableMap(false).get(tableName);
    }


    public void drop(String tableName)
    {
        DBTable tbl = getTable(tableName);
        if(tbl == null)
        {
            throw new IllegalStateException("table '" + tableName + "' doesnt exist - cannot remove");
        }
        if(tbl.tableExists())
        {
            tbl.drop();
        }
        else
        {
            getTableMap(false).remove(tableName);
        }
    }


    public void remove(String tableName)
    {
        getTableMap(false).remove(tableName);
    }


    public DBTable createTable(String name)
    {
        if(getTable(name) != null)
        {
            throw new IllegalStateException("table '" + name + "' already esists within schema " + this);
        }
        DBTable ret = new DBTable(this, name, false);
        getTableMap(true).put(name, ret);
        return ret;
    }


    public DBTable addExistingTable(String name)
    {
        if(getTable(name) != null)
        {
            throw new IllegalStateException("table '" + name + "' already esists within schema " + this);
        }
        DBTable ret = new DBTable(this, name, true);
        getTableMap(true).put(name, ret);
        return ret;
    }
}
