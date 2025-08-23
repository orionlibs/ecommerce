package de.hybris.platform.util.jdbc;

import de.hybris.bootstrap.typesystem.YDBTypeMapping;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jdbcwrapper.DataSourceImpl;
import de.hybris.platform.util.Config;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.JdbcUtils;

public final class SchemaAnalyzer
{
    public static final String TOPIC_BASE = "core.init";
    private static final CachingSchemaProvider CACHING_SCHEMA_PROVIDER = new CachingSchemaProvider();
    private static final Logger LOG = Logger.getLogger(SchemaAnalyzer.class);


    public static void invalidateCache()
    {
        CACHING_SCHEMA_PROVIDER.invalidate();
    }


    public static DBSchema readTableMetaData(String catalogName, DatabaseMetaData meta, Collection<String> itemTableNames)
    {
        Set<String> possibleTableNames = new HashSet<>();
        for(String tblName : itemTableNames)
        {
            possibleTableNames.add(tblName.toLowerCase(LocaleHelper.getPersistenceLocale()));
            possibleTableNames.add((tblName + tblName).toLowerCase(LocaleHelper.getPersistenceLocale()));
        }
        return readSchemaMetaData(catalogName, meta, null, possibleTableNames);
    }


    private static DBSchema readSchemaMetaData(String catalogName, DatabaseMetaData meta, YDBTypeMapping dbTypeMappings, Set<String> possibleTableNames)
    {
        DBSchema ret = null;
        try
        {
            String schemaName = Registry.getCurrentTenant().getDataSource().getSchemaName();
            String userName = DataSourceImpl.getUserNameFromDatabaseMetaData(meta, Config.getParameter("db.username")) + "@" + DataSourceImpl.getUserNameFromDatabaseMetaData(meta, Config.getParameter("db.username"));
            ret = new DBSchema(userName, schemaName, DatabaseNameResolver.guessDatabaseNameFromURL(meta.getURL()), dbTypeMappings);
            fetchSchemaCompletely(possibleTableNames, ret, meta, schemaName);
        }
        catch(SQLException e)
        {
            LOG.error("Error loading existing table : " + e.getMessage(), e);
            throw new JaloSystemException(e);
        }
        return ret;
    }


    private static void fetchSchemaCompletely(Set<String> possibleTableNames, DBSchema schema, DatabaseMetaData md, String schemaName) throws SQLException
    {
        Collection<DBTable> loadedTables = fetchTables(schema, schemaName, possibleTableNames, md);
        fetchColumnsWithoutTables(schema, schemaName, md);
        fetchIndexes(loadedTables, md);
    }


    private static Collection<DBTable> fetchTables(DBSchema schema, String schemaName, Set<String> possibleTableNames, DatabaseMetaData md)
    {
        Collection<DBTable> ret = new LinkedList<>();
        long t1 = System.currentTimeMillis();
        int found = 0;
        int all = 0;
        for(CachingSchemaProvider.TableSchemaData schema1 : CACHING_SCHEMA_PROVIDER.getTables(md, schemaName))
        {
            all++;
            if(isTableAllowed(possibleTableNames, schema1.getTable(), schema1.getSchema()))
            {
                found++;
                ret.add(schema.addExistingTable(schema1.getTable()));
            }
        }
        long t2 = System.currentTimeMillis();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("gathering tables took " + t2 - t1 + "ms (found:" + found + ",read:" + all + ",candidates:" + possibleTableNames
                            .size() + ")");
        }
        return ret;
    }


    private static void fetchColumnsWithoutTables(DBSchema schema, String schemaName, DatabaseMetaData md)
    {
        long t1 = System.currentTimeMillis();
        int found = 0;
        int all = 0;
        for(CachingSchemaProvider.ColumnsData cd : CACHING_SCHEMA_PROVIDER.getColumns(md, schemaName))
        {
            all++;
            String tableName = cd.getTableName();
            DBTable tbl = schema.getTable(tableName);
            if(tbl != null)
            {
                found++;
                tbl.addExistingColumn(new DBColumn(tbl, cd));
            }
        }
        long t2 = System.currentTimeMillis();
        if(LOG.isDebugEnabled())
        {
            LOG.debug("gathering columns took " + t2 - t1 + "ms (found:" + found + ",read:" + all + ",tables:" + schema
                            .getAllTablesCount() + ")");
        }
    }


    private static void fetchIndexes(Collection<DBTable> tables, DatabaseMetaData md)
    {
        ResultSet rs = null;
        try
        {
            long t1 = System.currentTimeMillis();
            int found = 0;
            int all = 0;
            boolean oracleMode = Config.isOracleUsed();
            for(DBTable tbl : tables)
            {
                String catalog = null;
                String schema = oracleMode ? ("\"" + tbl.getSchema().getSchemaName() + "\"") : tbl.getSchema().getSchemaName();
                String table = oracleMode ? ("\"" + tbl.getName() + "\"") : tbl.getName();
                rs = md.getIndexInfo(catalog, schema, table, false, true);
                while(rs.next())
                {
                    all++;
                    String indexName = rs.getString("INDEX_NAME");
                    if(indexName != null)
                    {
                        DBTableIndex idx = tbl.getIndex(indexName);
                        if(idx == null)
                        {
                            boolean unique = (!rs.getBoolean("NON_UNIQUE") && !rs.wasNull());
                            idx = tbl.addExistingIndex(indexName);
                            idx.setUnique(unique);
                            idx.notifyChangesPersisted(false);
                        }
                        String columnName = rs.getString("COLUMN_NAME");
                        int columnPos = rs.getInt("ORDINAL_POSITION");
                        idx.addIndexedColumn(tbl.getColumn(columnName), columnPos, false);
                        found++;
                    }
                }
                rs.close();
                rs = null;
            }
            long t2 = System.currentTimeMillis();
            if(LOG.isDebugEnabled())
            {
                LOG.debug("gathering indexes (standard) took " + t2 - t1 + "ms (found:" + found + ",read:" + all + ",tables:" + tables
                                .size() + ")");
            }
        }
        catch(SQLException e)
        {
            LOG.error("Error loading existing table : " + e.getMessage(), e);
            throw new JaloSystemException(e);
        }
        finally
        {
            JdbcUtils.closeResultSet(rs);
        }
    }


    private static boolean isTableAllowed(Set<String> possibleTableNames, String tblName, String tblSchema)
    {
        return ((CollectionUtils.isEmpty(possibleTableNames) || possibleTableNames.contains(tblName.toLowerCase(LocaleHelper.getPersistenceLocale()))) &&
                        !"INFORMATION_SCHEMA".equalsIgnoreCase(tblSchema));
    }
}
