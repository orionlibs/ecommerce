package de.hybris.bootstrap.ddl.model;

import de.hybris.bootstrap.typesystem.YAttributeDescriptor;
import java.util.HashMap;
import java.util.Map;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.model.Table;

public class YTable extends Table
{
    private final int typeCode;
    private final Map<String, String> columnDescriptor = new HashMap<>();


    public YTable(String name)
    {
        this(name, 0);
    }


    YTable(String name, int typeCode)
    {
        setName(name);
        this.typeCode = typeCode;
    }


    public int getTypeCode()
    {
        return this.typeCode;
    }


    public void setName(String name)
    {
        if(name == null)
        {
            throw new NullPointerException();
        }
        super.setName(name);
    }


    public void addTableColumnAttributeDescriptorRelation(String columnName, String javaClassName)
    {
        this.columnDescriptor.put(columnName, javaClassName);
    }


    public Map<String, String> getColumnDescriptor()
    {
        return this.columnDescriptor;
    }


    public YColumn findMappedColumn(YAttributeDescriptor attr)
    {
        int max = getColumnCount();
        for(int i = 0; i < max; i++)
        {
            Column col = getColumn(i);
            if(col instanceof YColumn)
            {
                if(((YColumn)col).isMappedToAttribute(attr))
                {
                    return (YColumn)col;
                }
            }
        }
        return null;
    }


    public void addColumn(Column column)
    {
        super.addColumn(column);
    }


    public void addColumn(int idx, Column column)
    {
        super.addColumn(idx, column);
    }
}
