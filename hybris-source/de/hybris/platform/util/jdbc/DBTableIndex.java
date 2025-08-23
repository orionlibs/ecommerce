package de.hybris.platform.util.jdbc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DBTableIndex
{
    private final DBTable table;
    private final String indexName;
    private boolean unique;
    private Collection<IndexedColumn> columns;
    private boolean modified;
    private boolean exists;


    DBTableIndex(DBTable table, String indexName)
    {
        this.table = table;
        this.indexName = indexName;
    }


    public String getIndexName()
    {
        return this.indexName;
    }


    public DBTable getTable()
    {
        return this.table;
    }


    public boolean isUnique()
    {
        return this.unique;
    }


    public void setUnique(boolean unique)
    {
        if(this.unique != unique)
        {
            this.unique = unique;
            this.modified = true;
        }
    }


    public void removeIndexedColumn(DBColumn col)
    {
        IndexedColumn ic = getIndexedColumn(col);
        if(ic != null)
        {
            this.columns.remove(ic);
            this.modified = true;
        }
    }


    public void addIndexedColumn(DBColumn col, int position, boolean lower)
    {
        if(this.columns == null)
        {
            this.columns = new ArrayList<>();
        }
        this.columns.add(new IndexedColumn(col, position, lower));
        this.modified = true;
    }


    public void setPosition(DBColumn col, int position)
    {
        IndexedColumn ic = getIndexedColumn(col);
        if(ic != null && ic.position != position)
        {
            ic.position = position;
            this.modified = true;
        }
        else
        {
            throw new IllegalArgumentException("no column " + col + " within index " + this);
        }
    }


    public void setLower(DBColumn col, boolean lower)
    {
        IndexedColumn ic = getIndexedColumn(col);
        if(ic != null && ic.lower != lower)
        {
            ic.lower = lower;
            this.modified = true;
        }
        else
        {
            throw new IllegalArgumentException("no column " + col + " within index " + this);
        }
    }


    public boolean isLower(DBColumn col)
    {
        IndexedColumn ic = getIndexedColumn(col);
        return (ic != null && ic.lower);
    }


    public DBColumn getColumn(String columnName)
    {
        for(DBColumn col : getColumns())
        {
            if(col.getColumnName().equalsIgnoreCase(columnName))
            {
                return col;
            }
        }
        return null;
    }


    private static final Comparator<IndexedColumn> COL_COMP = (Comparator<IndexedColumn>)new Object();


    protected IndexedColumn getIndexedColumn(DBColumn col)
    {
        if(this.columns != null)
        {
            for(IndexedColumn idxCol : this.columns)
            {
                if(idxCol.col.equals(col))
                {
                    return idxCol;
                }
            }
        }
        return null;
    }


    protected List<IndexedColumn> getIndexedColumns()
    {
        if(this.columns == null || this.columns.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        if(this.columns.size() == 1)
        {
            return Collections.singletonList(this.columns.iterator().next());
        }
        List<IndexedColumn> ret = new ArrayList<>(this.columns);
        Collections.sort(ret, COL_COMP);
        return ret;
    }


    public List<DBColumn> getColumns()
    {
        List<IndexedColumn> idxCols = getIndexedColumns();
        if(idxCols.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<DBColumn> ret = new ArrayList<>(idxCols.size());
        for(IndexedColumn idxCol : idxCols)
        {
            ret.add(idxCol.col);
        }
        return ret;
    }


    public boolean hasChanged()
    {
        return this.modified;
    }


    public boolean indexExists()
    {
        return this.exists;
    }


    public void setExists(boolean exists)
    {
        this.exists = exists;
    }


    void notifyChangesPersisted(boolean tableDroped)
    {
        this.exists = !tableDroped;
        this.modified = false;
    }
}
