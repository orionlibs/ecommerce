package de.hybris.platform.persistence.hjmp;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public final class QueryResult
{
    Map columnNames = new HashMap<>();
    List entries = new ArrayList();
    int currentResultLine = -1;


    QueryResult(ResultSet rs) throws SQLException
    {
        int columnCount = rs.getMetaData().getColumnCount();
        for(int i = 1; i <= columnCount; i++)
        {
            this.columnNames.put(rs.getMetaData().getColumnName(i).toUpperCase(), Integer.valueOf(i));
        }
        while(rs.next())
        {
            Object[] values = new Object[columnCount + 1];
            for(int j = 1; j <= columnCount; j++)
            {
                values[j] = rs.getObject(j);
            }
            this.entries.add(values);
        }
    }


    public String toString()
    {
        StringBuilder result = new StringBuilder("QueryResult(");
        Iterator<Object[]> entryIter = this.entries.iterator();
        while(entryIter.hasNext())
        {
            Object[] nextEntry = entryIter.next();
            Iterator<Map.Entry> columnIter = this.columnNames.entrySet().iterator();
            while(columnIter.hasNext())
            {
                String value;
                Map.Entry nextColumn = columnIter.next();
                String columnName = (String)nextColumn.getKey();
                int columnIndex = ((Integer)nextColumn.getValue()).intValue();
                try
                {
                    value = String.valueOf(nextEntry[columnIndex]);
                }
                catch(IndexOutOfBoundsException e)
                {
                    value = e.getMessage();
                }
                result.append(columnName + "->" + columnName);
                if(columnIter.hasNext())
                {
                    result.append(", ");
                }
            }
            if(entryIter.hasNext())
            {
                result.append("; ");
            }
        }
        result.append(")");
        return result.toString();
    }


    public int getLineCount()
    {
        return this.entries.size();
    }


    public void setToLine(int resultLine)
    {
        if(resultLine < 0 || resultLine >= getLineCount())
        {
            throw new HJMPException("no result line " + resultLine + " (found " + getLineCount() + " lines)");
        }
        this.currentResultLine = resultLine;
    }


    private Object[] getEntry()
    {
        if(this.currentResultLine == -1)
        {
            throw new HJMPException("result line not set!");
        }
        return this.entries.get(this.currentResultLine);
    }


    private int getColumnIndex(String columnName)
    {
        return ((Integer)this.columnNames.get(columnName.toUpperCase())).intValue();
    }


    public Object getObject(String columnName)
    {
        return getEntry()[getColumnIndex(columnName)];
    }


    public String getString(String columnName)
    {
        return (String)getObject(columnName);
    }


    public long getLong(String columnName)
    {
        return ((Number)getObject(columnName)).longValue();
    }


    public int getInt(String columnName)
    {
        return ((Number)getObject(columnName)).intValue();
    }


    public boolean getBoolean(String columnName)
    {
        Object value = getObject(columnName);
        if(value instanceof Boolean)
        {
            return ((Boolean)value).booleanValue();
        }
        return (((Number)value).intValue() != 0);
    }


    public double getDouble(String columnName)
    {
        Object value = getObject(columnName);
        return ((Number)value).doubleValue();
    }


    public Date getDate(String columnName)
    {
        Object value = getObject(columnName);
        return (Date)value;
    }
}
