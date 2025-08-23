package de.hybris.bootstrap.ddl.tools;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.Objects;

public final class CopyTableOperation
{
    private final String fromTable;
    private final String toTable;
    private final boolean isColumnBased;
    private final boolean withData;
    private final String dataToCopySelector;
    private final Object[] dataToCopySelectorParams;
    private String[] pkColumns;


    public static CopyTableOperation withoutData(String fromTable, String toTable, boolean isColumnBased)
    {
        return new CopyTableOperation(fromTable, toTable, isColumnBased, false, null, new Object[0]);
    }


    public static CopyTableOperation withData(String fromTable, String toTable, boolean isColumnBased)
    {
        return new CopyTableOperation(fromTable, toTable, isColumnBased, true, null, new Object[0]);
    }


    public static CopyTableOperation withData(String fromTable, String toTable, boolean isColumnBased, String dataToCopySelector, Object... dataToCopySelectorParams)
    {
        Objects.requireNonNull(dataToCopySelector, "dataToCopySelector can't be null");
        return new CopyTableOperation(fromTable, toTable, isColumnBased, true, dataToCopySelector, dataToCopySelectorParams);
    }


    private CopyTableOperation(String fromTable, String toTable, boolean isColumnBased, boolean withData, String dataToCopySelector, Object... dataToCopySelectorParams)
    {
        this.fromTable = Objects.<String>requireNonNull(fromTable, "fromTable can't be null");
        this.toTable = Objects.<String>requireNonNull(toTable, "toTable can't be null");
        this.withData = withData;
        this.dataToCopySelector = dataToCopySelector;
        this.dataToCopySelectorParams = dataToCopySelectorParams;
        this.isColumnBased = isColumnBased;
    }


    public CopyTableOperation withPK(String... pkColumns)
    {
        this.pkColumns = pkColumns;
        return this;
    }


    public boolean hasPrimaryKey()
    {
        return (this.pkColumns != null && this.pkColumns.length > 0);
    }


    public Collection<String> getPrimaryKeyColumns()
    {
        return (Collection<String>)ImmutableList.copyOf((Object[])this.pkColumns);
    }


    public String getFromTable()
    {
        return this.fromTable;
    }


    public String getToTable()
    {
        return this.toTable;
    }


    public boolean isColumnBased()
    {
        return this.isColumnBased;
    }


    public boolean isWithData()
    {
        return this.withData;
    }


    public boolean isWithSelectiveData()
    {
        return (this.dataToCopySelector != null);
    }


    public String getDataToCopySelector()
    {
        return this.dataToCopySelector;
    }


    public Object[] getDataToCopySelectorParams()
    {
        return this.dataToCopySelectorParams;
    }
}
