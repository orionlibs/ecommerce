package de.hybris.platform.persistence;

import de.hybris.platform.core.DeploymentImpl;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.persistence.property.OldPropertyJDBC;
import de.hybris.platform.persistence.property.PropertyTableDefinition;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.MurmurHash;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.jdbc.DBTable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class TableJDBC
{
    private static final Logger LOG = Logger.getLogger(TableJDBC.class.getName());
    private static final String NOT_NULL_DEF = " NOT NULL ";
    private static final String NULL_DEF = "  ";


    public static void dropExistingTable(Connection conn, String tableName)
    {
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate("DROP TABLE " + tableName);
        }
        catch(SQLException e)
        {
            if(LOG.isEnabledFor((Priority)Level.ERROR))
            {
                LOG.error("sql error droping item table via 'DROP TABLE " + tableName + "' : " + e);
                e.printStackTrace(System.err);
            }
            throw new JaloSystemException(e);
        }
        finally
        {
            if(stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch(SQLException e)
                {
                    if(LOG.isDebugEnabled())
                    {
                        LOG.debug(e.getMessage());
                    }
                }
            }
        }
        stmt = null;
    }


    public static void createTable(Connection conn, ItemDeployment depl, DeploymentImpl deployments)
    {
        String createTableQuery = assembleCreateTableQuery(depl, deployments);
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(createTableQuery);
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e, "error creating table '" + depl
                            .getDatabaseTableName() + "' using '" + createTableQuery + "'", -1);
        }
        finally
        {
            Utilities.tryToCloseJDBC(null, stmt, null, true);
        }
    }


    public static void updateTable(Connection conn, ItemDeployment depl, DBTable table, Collection<ItemDeployment.Attribute> missingAttributes, DeploymentImpl deployments)
    {
        String tableName = depl.getDatabaseTableName();
        String alterTableQuery = assembeAlterTableQuery(depl, table, missingAttributes, deployments);
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(alterTableQuery);
            if(isPropsTableDeployment(depl))
            {
                OldPropertyJDBC.patchPropertyNamesAfterUpdate(tableName);
            }
        }
        catch(SQLWarning e)
        {
            if(LOG.isEnabledFor((Priority)Level.ERROR))
            {
                LOG.error("sql warning changing table via '" + alterTableQuery + "' : " + e);
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Exception while alter table def", e);
            }
        }
        catch(SQLException e)
        {
            if(LOG.isEnabledFor((Priority)Level.ERROR))
            {
                LOG.error("sql error changing table via '" + alterTableQuery + "' : " + e);
                e.printStackTrace(System.err);
            }
            throw new JaloSystemException(e);
        }
        finally
        {
            Utilities.tryToCloseJDBC(null, stmt, null, true);
        }
    }


    public static void createPropertyTable(Connection conn, PropertyTableDefinition tableDef)
    {
        String createQuery = buildCreatePropertyTableQuery(tableDef, true);
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(createQuery);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("created localized property table " + tableDef.getTableName(true) + " [ " + createQuery + " ] ");
            }
        }
        catch(SQLException ex)
        {
            throw new JaloSystemException("could not create localized property table " + tableDef.getTableName(true) + " because of SQL error " + ex + " query was " + createQuery);
        }
        finally
        {
            if(stmt != null)
            {
                try
                {
                    stmt.close();
                }
                catch(SQLException sQLException)
                {
                }
            }
            stmt = null;
        }
    }


    public static Set<String> addUnlocalizedPropertiesToItemTable(Connection conn, PropertyTableDefinition tableDef, DBTable table)
    {
        Set<String> addedColumns = new HashSet<>();
        String alterQuery = buildAlterPropertyTableQuery(tableDef, false, table, addedColumns);
        if(alterQuery != null)
        {
            StringTokenizer tok = new StringTokenizer(alterQuery, ";\n");
            while(tok.hasMoreTokens())
            {
                String sql = null;
                Statement stmt = null;
                try
                {
                    sql = tok.nextToken();
                    if(StringUtils.isNotEmpty(sql))
                    {
                        stmt = conn.createStatement();
                        stmt.executeUpdate(sql);
                        if(LOG.isDebugEnabled())
                        {
                            LOG.debug("modified item table " + tableDef.getTableName(false) + " [ " + sql + " ] ");
                        }
                    }
                }
                catch(SQLException ex)
                {
                    throw new JaloSystemException("could not add unlocalized property columns to item table " + tableDef
                                    .getTableName(false) + " because of SQL error " + ex + " query was " + sql);
                }
                finally
                {
                    Utilities.tryToCloseJDBC(null, stmt, null, true);
                }
            }
        }
        return addedColumns;
    }


    public static Set<String> updatePropertyTable(Connection conn, PropertyTableDefinition tableDef, DBTable table)
    {
        Set<String> addedColumns = new HashSet<>();
        String alterQuery = buildAlterPropertyTableQuery(tableDef, true, table, addedColumns);
        if(alterQuery != null)
        {
            StringTokenizer tok = new StringTokenizer(alterQuery, ";\n");
            while(tok.hasMoreTokens())
            {
                String sql = null;
                Statement stmt = null;
                try
                {
                    sql = tok.nextToken();
                    if(StringUtils.isNotEmpty(sql))
                    {
                        stmt = conn.createStatement();
                        stmt.executeUpdate(sql);
                    }
                }
                catch(SQLException ex)
                {
                    throw new JaloSystemException("could not modify localized property table " + tableDef.getTableName(true) + " because of SQL error " + ex + " query was " + sql);
                }
                finally
                {
                    Utilities.tryToCloseJDBC(null, stmt, null);
                }
            }
        }
        return addedColumns;
    }


    public static boolean createIndex(Connection conn, String indexKey, String tableName, List<String> columnNames, boolean unique, boolean isSQLServerClustered)
    {
        String createIndexQuery = buildCreateIndexQuery(indexKey, tableName, columnNames, unique, isSQLServerClustered);
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(createIndexQuery);
            return true;
        }
        catch(SQLException e)
        {
            String str = e.toString();
            if(str.contains("ORA-01408") || str.contains("ORA-00955") || str.contains("ORA-01031") || str
                            .contains("Index already exists") || str.contains("object name already exists"))
            {
                LOG.debug("failed to create index '" + indexKey + "' (sql='" + createIndexQuery + "') due to " + e);
            }
            else
            {
                LOG.error("failed to create index '" + indexKey + "' (sql='" + createIndexQuery + "') due to " + e);
            }
            return false;
        }
        finally
        {
            Utilities.tryToCloseJDBC(null, stmt, null, true);
        }
    }


    private static final String buildCreateIndexQuery(String indexKey, String tableName, List<String> columnNames, boolean unique, boolean isSQLServerClustered)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String oracleIndexTS = Config.getParameter("oracle.indexTS");
        stringBuilder.append("CREATE ");
        if(unique)
        {
            stringBuilder.append("UNIQUE ");
        }
        if(Config.isSQLServerUsed() && isSQLServerClustered)
        {
            stringBuilder.append(" CLUSTERED ");
        }
        StringBuilder columnList = new StringBuilder();
        for(String col : columnNames)
        {
            if(columnList.length() > 0)
            {
                columnList.append(",");
            }
            columnList.append(col);
        }
        stringBuilder.append(" INDEX ").append(indexKey).append(" ON ").append(tableName).append(" ( ");
        stringBuilder.append(columnList);
        stringBuilder.append(")");
        if(Config.isOracleUsed() && oracleIndexTS != null && !"".equals(oracleIndexTS))
        {
            stringBuilder.append(" TABLESPACE ").append(oracleIndexTS);
        }
        if(Config.isOracleUsed())
        {
            String globalTableOptions = Config.getString("oracle.globalIndexOptions", "");
            globalTableOptions = globalTableOptions.replace("@INDEXCOLUMNS@", columnList.toString());
            stringBuilder.append(" " + globalTableOptions + " ");
        }
        return stringBuilder.toString();
    }


    private static String buildAlterPropertyTableQuery(PropertyTableDefinition tableDef, boolean localized, DBTable table, Set<String> addedColumns)
    {
        if(Config.isHSQLDBUsed())
        {
            return buildAlterPropertyTableQueryHSQLDB(tableDef, localized, table, addedColumns);
        }
        if(Config.isSQLServerUsed())
        {
            return buildAlterPropertyTableQuerySQLServer(tableDef, localized, table, addedColumns);
        }
        return buildAlterPropertyTableQueryDefault(tableDef, localized, table, addedColumns);
    }


    private static String buildAlterPropertyTableQueryDefault(PropertyTableDefinition tableDef, boolean localized, DBTable table, Set<String> addedColumns)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String tableName = tableDef.getTableName(localized);
        int columnsCountBefore = addedColumns.size();
        stringBuilder.append("ALTER TABLE ").append(tableDef.getTableName(localized)).append(" ADD ( ");
        for(Iterator<String> it = tableDef.getColumnNames(localized).iterator(); it.hasNext(); )
        {
            String columnName = it.next();
            if(skipColumn(table, columnName))
            {
                if(LOG.isDebugEnabled())
                {
                    String columnDescr = "no column map! ";
                    if(table != null)
                    {
                        columnDescr = table.getColumn(columnName).toString();
                    }
                    LOG.debug("column " + tableName + "." + columnName + " already exists with sql type " + columnDescr + " - skipped");
                }
                continue;
            }
            if(addedColumns.size() > columnsCountBefore)
            {
                stringBuilder.append(",");
            }
            addedColumns.add(columnName);
            String typeDef = Registry.getPersistenceManager().getSQLTypeDef(tableDef
                            .getColumnDefinition(columnName, localized), tableDef
                            .getSqlColumnDescription(columnName, localized));
            stringBuilder.append(columnName).append(" ").append(typeDef);
            if(typeDef.toLowerCase().indexOf(" null") < 0)
            {
                stringBuilder.append("  ");
            }
        }
        stringBuilder.append(")");
        return (addedColumns.size() > columnsCountBefore) ? stringBuilder.toString() : null;
    }


    private static String buildAlterPropertyTableQuerySQLServer(PropertyTableDefinition tableDef, boolean localized, DBTable table, Set<String> addedColumns)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String tableName = tableDef.getTableName(localized);
        int columnsCountBefore = addedColumns.size();
        stringBuilder.append("ALTER TABLE ").append(tableName).append(" ADD ");
        for(Iterator<String> it = tableDef.getColumnNames(localized).iterator(); it.hasNext(); )
        {
            String columnName = it.next();
            if(skipColumn(table, columnName))
            {
                if(LOG.isDebugEnabled())
                {
                    String columnDescr = "no column map! ";
                    if(table != null)
                    {
                        columnDescr = table.getColumn(columnName).toString();
                    }
                    LOG.debug("column " + tableName + "." + columnName + " already exists with sql type " + columnDescr + " - skipped");
                }
                continue;
            }
            if(addedColumns.size() > columnsCountBefore)
            {
                stringBuilder.append(",");
            }
            addedColumns.add(columnName);
            String typeDef = Registry.getPersistenceManager().getSQLTypeDef(tableDef
                            .getColumnDefinition(columnName, localized), tableDef
                            .getSqlColumnDescription(columnName, localized));
            stringBuilder.append(columnName).append(" ").append(typeDef);
            if(typeDef.toLowerCase().indexOf(" null") < 0)
            {
                stringBuilder.append(" NULL ");
            }
        }
        return (addedColumns.size() > columnsCountBefore) ? stringBuilder.toString() : null;
    }


    private static String buildAlterPropertyTableQueryHSQLDB(PropertyTableDefinition tableDef, boolean localized, DBTable table, Set<String> addedColumns)
    {
        StringBuilder stringBuilder = new StringBuilder();
        String tableName = tableDef.getTableName(localized);
        int columnsCountBefore = addedColumns.size();
        for(String columnName : tableDef.getColumnNames(localized))
        {
            if(skipColumn(table, columnName))
            {
                if(LOG.isDebugEnabled())
                {
                    String columnDescr = "no column map! ";
                    if(table != null)
                    {
                        columnDescr = table.getColumn(columnName).toString();
                    }
                    LOG.debug("column " + tableName + "." + columnName + " already exists with sql type " + columnDescr + " - skipped");
                }
                continue;
            }
            addedColumns.add(columnName);
            stringBuilder.append("ALTER TABLE ").append(tableName).append(" ADD COLUMN ");
            String typeDef = Registry.getPersistenceManager().getSQLTypeDef(tableDef
                            .getColumnDefinition(columnName, localized), tableDef
                            .getSqlColumnDescription(columnName, localized));
            stringBuilder.append(columnName).append(" ").append(typeDef);
            if(typeDef.toLowerCase().indexOf(" null") < 0)
            {
                stringBuilder.append("  ");
            }
            stringBuilder.append(" ;");
        }
        return (addedColumns.size() > columnsCountBefore) ? stringBuilder.toString() : null;
    }


    private static boolean skipColumn(DBTable table, String columnName)
    {
        boolean duplicateColumn = (table != null && table.columnExists(columnName));
        boolean duplicateCreationTime = columnName.equalsIgnoreCase("createdTS");
        return (duplicateColumn || duplicateCreationTime);
    }


    private static String buildCreatePropertyTableQuery(PropertyTableDefinition tableDef, boolean localized)
    {
        String dts = Config.getParameter("oracle.dataTS");
        String indexTS = "";
        String dataTS = "";
        if(StringUtils.isNotBlank(dts) && !dts.trim().equalsIgnoreCase("#DEFAULT#"))
        {
            dataTS = "TABLESPACE " + dts;
        }
        String its = Config.getParameter("oracle.indexTS");
        if(StringUtils.isNotBlank(its) && !its.trim().equalsIgnoreCase("#DEFAULT#"))
        {
            indexTS = "TABLESPACE " + its;
        }
        StringBuilder stringBuilder = new StringBuilder();
        if(Config.isHSQLDBUsed() && Config.getBoolean("hsqldb.usecachedtables", false))
        {
            stringBuilder.append("CREATE CACHED TABLE ").append(tableDef.getTableName(localized)).append(" ( ");
        }
        else
        {
            stringBuilder.append("CREATE TABLE ").append(tableDef.getTableName(localized)).append(" ( ");
        }
        stringBuilder.append("ITEMPK")
                        .append(" ")
                        .append(Registry.getPersistenceManager().getSQLTypeDef(PK.class, null))
                        .append(" NOT NULL  ,");
        stringBuilder.append("ITEMTYPEPK").append(" ")
                        .append(Registry.getPersistenceManager().getSQLTypeDef(PK.class, null)).append("  ,");
        if(localized)
        {
            stringBuilder.append("LANGPK")
                            .append(" ")
                            .append(Registry.getPersistenceManager().getSQLTypeDef(PK.class, null))
                            .append(" NOT NULL ,");
        }
        int count = 0;
        for(Iterator<String> it = tableDef.getColumnNames(localized).iterator(); it.hasNext(); )
        {
            String columnName = it.next();
            if(count > 0)
            {
                stringBuilder.append(", ");
            }
            String typeDef = Registry.getPersistenceManager().getSQLTypeDef(tableDef
                            .getColumnDefinition(columnName, localized), tableDef.getSqlColumnDescription(columnName, localized));
            stringBuilder.append(columnName).append(" ").append(typeDef);
            if(typeDef.toLowerCase().indexOf(" null") < 0)
            {
                stringBuilder.append("  ");
            }
            count++;
        }
        String clustered = Config.getString("sqlserver.pkindextype", "NONCLUSTERED");
        if(Config.isOracleUsed())
        {
            stringBuilder.append(", CONSTRAINT PK_").append(createMurmurHash(tableDef.getTableName(false)))
                            .append("_PK PRIMARY KEY ( ")
                            .append(localized ? "ITEMPK,LANGPK" : "ITEMPK")
                            .append(" ) USING INDEX ");
            stringBuilder.append("(CREATE UNIQUE INDEX PK_" + createMurmurHash(tableDef.getTableName(false)) + "_IDX ON " + tableDef
                            .getTableName(localized) + " (");
            stringBuilder.append(localized ? "ITEMPK,LANGPK" : "ITEMPK");
            stringBuilder.append(" ) REVERSE ");
            stringBuilder.append(indexTS + " ");
            String globalTableOptions = Config.getString("oracle.globalIndexOptions", "");
            globalTableOptions = globalTableOptions.replace("@INDEXCOLUMNS@", localized ?
                            "ITEMPK,LANGPK" : "ITEMPK");
            stringBuilder.append(" " + globalTableOptions + " ) ) " + dataTS + " " +
                            Config.getString("oracle.globalTableOptions", ""));
        }
        else if(Config.isSQLServerUsed())
        {
            stringBuilder.append(", CONSTRAINT PK_").append(createMurmurHash(tableDef.getTableName(false)))
                            .append("_PK PRIMARY KEY ").append(clustered).append(" ( ")
                            .append(localized ? "ITEMPK,LANGPK" : "ITEMPK").append(")) ");
        }
        else
        {
            stringBuilder.append(", CONSTRAINT PK_").append(createMurmurHash(tableDef.getTableName(false)))
                            .append("_PK PRIMARY KEY ( ")
                            .append(localized ? "ITEMPK,LANGPK" : "ITEMPK").append(")) ");
        }
        if(Config.isMySQLUsed())
        {
            String tableType = Config.getParameter("mysql.tabletype");
            if(StringUtils.isNotBlank(tableType))
            {
                stringBuilder.append(" ENGINE ").append(tableType);
            }
            String optionalDefs = Config.getParameter("mysql.optional.tabledefs");
            if(StringUtils.isNotBlank(optionalDefs))
            {
                stringBuilder.append(" ").append(optionalDefs);
            }
        }
        return stringBuilder.toString();
    }


    private static final String assembeAlterTableQuery(ItemDeployment depl, DBTable table, Collection<ItemDeployment.Attribute> missingAttributes, DeploymentImpl deployments)
    {
        String tableName = depl.getDatabaseTableName();
        String database = Config.getDatabase();
        StringBuilder stringBuilder = new StringBuilder();
        if(database.equalsIgnoreCase("hsqldb"))
        {
            boolean first = true;
            for(ItemDeployment.Attribute attr : missingAttributes)
            {
                if(first)
                {
                    first = false;
                }
                else
                {
                    stringBuilder.append("; ");
                }
                stringBuilder.append("ALTER TABLE ").append(tableName);
                stringBuilder.append(" ADD COLUMN ");
                stringBuilder.append(attr.getColumnName(database)).append(" ")
                                .append(attr.getColumnDeclaration(database, deployments)).append(" ");
                stringBuilder.append(!attr.isNullAllowed(database) ? " NOT NULL " : "  ");
            }
        }
        else if(database.equalsIgnoreCase("sqlserver"))
        {
            stringBuilder.append("ALTER TABLE ").append(tableName).append(" ADD ");
            boolean first = true;
            for(ItemDeployment.Attribute attr : missingAttributes)
            {
                if(first)
                {
                    first = false;
                }
                else
                {
                    stringBuilder.append(" , ");
                }
                stringBuilder.append(attr.getColumnName(database)).append(" ")
                                .append(attr.getColumnDeclaration(database, deployments)).append(" ");
                stringBuilder.append(!attr.isNullAllowed(database) ? " NOT NULL " : "  ");
            }
        }
        else if(database.equalsIgnoreCase("oracle"))
        {
            stringBuilder.append("ALTER TABLE ").append(tableName).append(" ");
            for(ItemDeployment.Attribute attr : missingAttributes)
            {
                boolean setNull = (missingAttributes.contains(attr) || table.getColumn(attr.getColumnName(database)).isNullable() != attr.isNullAllowed(database));
                stringBuilder.append(" ADD ");
                stringBuilder.append(attr.getColumnName(database)).append(" ")
                                .append(attr.getColumnDeclaration(database, deployments));
                if(setNull)
                {
                    stringBuilder.append(!attr.isNullAllowed(database) ? " NOT NULL " : "  ");
                }
            }
        }
        else
        {
            boolean first = true;
            stringBuilder.append("ALTER TABLE ").append(tableName).append(" ");
            for(ItemDeployment.Attribute attr : missingAttributes)
            {
                boolean setNull = (missingAttributes.contains(attr) || table.getColumn(attr.getColumnName(database)).isNullable() != attr.isNullAllowed(database));
                if(first)
                {
                    first = false;
                }
                else
                {
                    stringBuilder.append(" , ");
                }
                stringBuilder.append(" ADD ");
                stringBuilder.append(attr.getColumnName(database)).append(" ")
                                .append(attr.getColumnDeclaration(database, deployments));
                if(setNull)
                {
                    stringBuilder.append(!attr.isNullAllowed(database) ? " NOT NULL " : "  ");
                }
            }
        }
        return stringBuilder.toString();
    }


    private static String assembleCreateTableQuery(ItemDeployment depl, DeploymentImpl deployments)
    {
        String tableName = depl.getDatabaseTableName();
        String database = Config.getDatabase();
        boolean hsqlCachedTables = Config.getBoolean("hsqldb.usecachedtables", false);
        String oracleIndexTS = Config.getParameter("oracle.indexTS");
        String oracleDataTS = Config.getParameter("oracle.dataTS");
        StringBuilder stringBuilder = new StringBuilder("CREATE ");
        if("hsqldb".equalsIgnoreCase(database) && hsqlCachedTables)
        {
            stringBuilder.append("CACHED ");
        }
        stringBuilder.append("TABLE ").append(tableName).append(" ( ");
        stringBuilder.append("hjmpTS ").append(deployments.getColumnDefinition(database, Long.class.getName()))
                        .append("db2".equalsIgnoreCase(database) ? " " : "  ");
        List<ItemDeployment.Attribute> primkeyAttrs = new ArrayList<>();
        for(ItemDeployment.Attribute attr : depl.getAttributes())
        {
            if(attr.isPrimaryKey())
            {
                primkeyAttrs.add(attr);
            }
            stringBuilder.append(",").append(attr.getColumnName(database)).append(" ")
                            .append(attr.getColumnDeclaration(database, deployments)).append(" ");
            stringBuilder.append((!attr.isNullAllowed(database) || attr.isPrimaryKey()) ? " NOT NULL " : (
                            "db2".equalsIgnoreCase(database) ? " " : "  "));
        }
        if(!primkeyAttrs.isEmpty())
        {
            String clustered = "";
            if(Config.isSQLServerUsed())
            {
                clustered = Config.getString("sqlserver.pkindextype", "NONCLUSTERED");
            }
            stringBuilder.append(" , CONSTRAINT PK_")
                            .append(createMurmurHash(depl.getDatabaseTableName()))
                            .append(" PRIMARY KEY ")
                            .append(clustered)
                            .append(" ( ");
            boolean first = true;
            for(ItemDeployment.Attribute attr : primkeyAttrs)
            {
                if(first)
                {
                    first = false;
                }
                else
                {
                    stringBuilder.append(",");
                }
                stringBuilder.append(attr.getColumnName(database));
            }
            stringBuilder.append(" ) ");
        }
        if("oracle".equalsIgnoreCase(database))
        {
            if(!primkeyAttrs.isEmpty())
            {
                StringBuilder idxcolumns = new StringBuilder();
                boolean first = true;
                for(ItemDeployment.Attribute attr : primkeyAttrs)
                {
                    if(first)
                    {
                        first = false;
                    }
                    else
                    {
                        idxcolumns.append(",");
                    }
                    idxcolumns.append(attr.getColumnName(database));
                }
                stringBuilder.append(" USING INDEX (CREATE UNIQUE INDEX PK_" +
                                createMurmurHash(depl.getDatabaseTableName()) + " ON " + tableName + " (");
                stringBuilder.append(idxcolumns);
                stringBuilder.append(" ) REVERSE ");
                if(oracleIndexTS != null && !"".equals(oracleIndexTS))
                {
                    stringBuilder.append("TABLESPACE ").append(oracleIndexTS).append(" ");
                }
                String globalTableOptions = Config.getString("oracle.globalIndexOptions", "");
                globalTableOptions = globalTableOptions.replace("@INDEXCOLUMNS@", idxcolumns.toString());
                stringBuilder.append(" " + globalTableOptions + " ");
                stringBuilder.append(" ) ");
            }
            stringBuilder.append(" ) ");
            if(oracleDataTS != null && !"".equals(oracleDataTS))
            {
                stringBuilder.append("TABLESPACE ").append(oracleDataTS).append(" ");
            }
            stringBuilder.append(" " + Config.getString("oracle.globalTableOptions", "") + " ");
        }
        else
        {
            stringBuilder.append(")");
        }
        if("mysql".equalsIgnoreCase(database))
        {
            String tableType = Config.getParameter("mysql.tabletype");
            if(StringUtils.isNotBlank(tableType))
            {
                stringBuilder.append(" ENGINE ").append(tableType);
            }
            String optionalDefs = Config.getParameter("mysql.optional.tabledefs");
            if(StringUtils.isNotBlank(optionalDefs))
            {
                stringBuilder.append(" ").append(optionalDefs);
            }
        }
        return stringBuilder.toString();
    }


    private static final Set<String> PROPS_FIELDS = new HashSet<>(Arrays.asList(new String[] {"ITEMPK", "ITEMTYPEPK", "NAME", "REALNAME", "LANGPK", "TYPE1", "VALUESTRING1", "VALUE1"}));


    protected static boolean isPropsTableDeployment(ItemDeployment depl)
    {
        if(!depl.isNonItemDeployment())
        {
            return false;
        }
        for(String field : PROPS_FIELDS)
        {
            if(depl.getAttribute(field) == null)
            {
                return false;
            }
        }
        return true;
    }


    private static String createMurmurHash(String name)
    {
        return String.valueOf(MurmurHash.hash64(name)).replace('-', '_');
    }
}
