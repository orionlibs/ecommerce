package de.hybris.bootstrap.ddl;

import de.hybris.bootstrap.ddl.sql.HanaSqlBuilder;
import de.hybris.bootstrap.util.LocaleHelper;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import org.apache.ddlutils.Platform;
import org.apache.ddlutils.model.Column;
import org.apache.ddlutils.platform.DatabaseMetaDataWrapper;
import org.apache.ddlutils.platform.JdbcModelReader;

public class HanaModelReader extends JdbcModelReader
{
    private static final String TABLE_NAME = "TABLE_NAME";
    private static final String IS_PRIMARY_KEY = "IS_PRIMARY_KEY";
    private Map<String, Collection> cachedColumns;
    private final HanaPksAndIndices hanaPksAndIndices = new HanaPksAndIndices(this);
    private HanaSqlBuilder hanaSqlBuilder;
    private final Platform platform;
    private final Set<String> indexNames = new HashSet<>();


    public HanaModelReader(Platform platform, String tablePrefix)
    {
        super(platform);
        this.platform = platform;
        this.hanaSqlBuilder = (HanaSqlBuilder)platform.getSqlBuilder();
        setDefaultTablePattern((tablePrefix + "%").toUpperCase(LocaleHelper.getPersistenceLocale()));
    }


    protected Collection readColumns(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException
    {
        if(!columnsAreCached())
        {
            cacheColumnsForAllTables(metaData);
        }
        return getColumnsFromCacheFor(tableName);
    }


    protected Collection readIndices(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException
    {
        return this.hanaPksAndIndices.getIndicesFor(metaData.getSchemaPattern(), tableName);
    }


    protected Collection readPrimaryKeyNames(DatabaseMetaDataWrapper metaData, String tableName) throws SQLException
    {
        return this.hanaPksAndIndices.getPkColumnsFor(metaData.getSchemaPattern(), tableName);
    }


    protected Column readColumn(DatabaseMetaDataWrapper metaData, Map values) throws SQLException
    {
        return (Column)new TableNameAwareColumn(super.readColumn(metaData, values), values.get("TABLE_NAME").toString().toLowerCase(LocaleHelper.getPersistenceLocale()));
    }


    private boolean columnsAreCached()
    {
        return (this.cachedColumns != null);
    }


    private void cacheColumnsForAllTables(DatabaseMetaDataWrapper metaData) throws SQLException
    {
        this.cachedColumns = new HashMap<>();
        Collection<TableNameAwareColumn> allColumns = super.readColumns(metaData, null);
        for(TableNameAwareColumn column : allColumns)
        {
            Collection<TableNameAwareColumn> columnsForTable = this.cachedColumns.get(column.getTableName());
            if(columnsForTable == null)
            {
                columnsForTable = new LinkedList<>();
                this.cachedColumns.put(column.getTableName(), columnsForTable);
            }
            columnsForTable.add(column);
        }
    }


    private Collection getColumnsFromCacheFor(String tableName)
    {
        return this.cachedColumns.get(tableName.toLowerCase(LocaleHelper.getPersistenceLocale()));
    }
}
