package de.hybris.platform.persistence.numberseries;

import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.DeploymentImpl;
import de.hybris.platform.core.ItemDeployment;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.numberseries.NumberSeries;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.typesystem.TypeSystemUtils;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class DefaultSerialNumberDAO implements SerialNumberDAO
{
    private static final Logger LOG = Logger.getLogger(DefaultSerialNumberDAO.class);
    public static final String CONFIG_PARAM_SYNCHRONIZE_NUMBERGENERATION = "numberseries.synchronize.generation";
    public static final String CONFIG_PARAM_MAX_RETRIES = "numberseries.maxretries";
    private static final int MAX_RETRIES_DEFAULT = 20;
    private static final String KEY = "serieskey";
    private static final String OLD_NUMBER = "currentNumber";
    private static final String CURRENT_NUMBER = "currentValue";
    private static final String TYPE = "seriestype";
    private static final String TEMPLATE = "template";
    private static final String[] REQUIRED_COLUMNS = new String[] {"serieskey", "currentValue", "seriestype", "template"};
    private final Tenant t;
    private final String selectQuery;
    private final String insertQuery;
    private final String removeQuery;
    private final String updateQuery;
    private final String resetQuery;
    private final String selectAllQuery;
    private final int maxRetries;


    public DefaultSerialNumberDAO(Tenant t, HybrisDataSource initDataSource)
    {
        this.t = t;
        String tbl = calculateTableName(initDataSource);
        this.selectQuery = "SELECT currentValue,seriestype,template FROM " + tbl + " WHERE serieskey = ? ";
        this.selectAllQuery = "SELECT serieskey,currentValue,seriestype,template FROM " + tbl;
        this.insertQuery = "INSERT INTO " + tbl + " (serieskey,seriestype,currentValue,template) VALUES (?,?,?,?)";
        this.removeQuery = "DELETE FROM " + tbl + " WHERE serieskey = ? ";
        this.updateQuery = "UPDATE " + tbl + " SET currentValue = ? WHERE serieskey = ? AND currentValue = ?";
        this.resetQuery = "UPDATE " + tbl + " SET currentValue = ?, seriestype = ? WHERE serieskey = ?";
        this.maxRetries = 1 + this.t.getConfig().getInt("numberseries.maxretries", 20);
    }


    protected String calculateTableName(HybrisDataSource ds)
    {
        String tablePrefix = ds.getTablePrefix();
        return (tablePrefix != null && tablePrefix.length() > 0) ? (tablePrefix + "numberseries") : "numberseries";
    }


    protected HybrisDataSource getDataSource()
    {
        return this.t.getMasterDataSource();
    }


    protected Connection getConnection(HybrisDataSource ds) throws SQLException
    {
        try
        {
            return ds.getConnection(false);
        }
        catch(SQLException e)
        {
            throw e;
        }
        catch(RuntimeException e)
        {
            throw e;
        }
        catch(Exception e)
        {
            throw new JaloSystemException(e);
        }
    }


    public void createSeries(String key, int type, long startValue, String template)
    {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            try
            {
                con = getConnection(getDataSource());
                stmt = con.prepareStatement(this.selectQuery);
                stmt.setString(1, key);
                rs = stmt.executeQuery();
                if(rs.next())
                {
                    throw new IllegalArgumentException("number series key '" + key + "' is already used");
                }
                stmt = con.prepareStatement(this.insertQuery);
                stmt.setString(1, key);
                stmt.setInt(2, type);
                stmt.setLong(3, startValue);
                stmt.setString(4, template);
                if(stmt.executeUpdate() != 1)
                {
                    throw new JaloSystemException(null, "failed to create number series [" + key + "," + type + "," + startValue + "]", 0);
                }
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e, "SQL error creating number series [" + key + "," + type + "," + startValue + "]: " + e, 0);
        }
        finally
        {
            tryToClose(con, stmt, rs);
        }
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void createSeries(String key, int type, long startValue)
    {
        createSeries(key, type, startValue, null);
    }


    public NumberSeries fetchUniqueRange(String key, int count)
    {
        Connection con = null;
        PreparedStatement readStmt = null, updateStmt = null;
        ResultSet rs = null;
        boolean success = false;
        long current = -1L;
        int type = -1;
        String template = null;
        try
        {
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e, "SQL error fetching " + count + " numbers from [" + key + "," + type + "] : " + e, 0);
        }
        finally
        {
            tryToClose(updateStmt, null);
            tryToClose(con, readStmt, rs);
        }
    }


    public void removeSeries(String key)
    {
        Connection con = null;
        PreparedStatement stmt = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            try
            {
                con = getConnection(getDataSource());
                stmt = con.prepareStatement(this.removeQuery);
                stmt.setString(1, key);
                if(stmt.executeUpdate() != 1)
                {
                    throw new IllegalArgumentException("did not find number sequence for key '" + key + "'");
                }
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e, "SQL error removing number series " + key + ": " + e, 0);
        }
        finally
        {
            tryToClose(con, stmt, null);
        }
    }


    public void resetSeries(String key, int type, long startValue)
    {
        Connection con = null;
        PreparedStatement stmt = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            try
            {
                con = getConnection(getDataSource());
                stmt = con.prepareStatement(this.resetQuery);
                stmt.setLong(1, startValue);
                stmt.setInt(2, type);
                stmt.setString(3, key);
                if(stmt.executeUpdate() != 1)
                {
                    throw new IllegalArgumentException("did not find number series " + key + " - cannot reset to [" + key + "," + type + "," + startValue + "]");
                }
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e, "SQL error resetting number series " + key + " : " + e, 0);
        }
        finally
        {
            tryToClose(con, stmt, null);
        }
    }


    public NumberSeries getCurrent(String key)
    {
        Connection con = null;
        PreparedStatement readStmt = null;
        ResultSet rs = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
        }
        catch(SQLSyntaxErrorException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e);
            }
            throw new IllegalArgumentException("key '" + key + "' was no valid number series key , details :" + e.getMessage());
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e, "SQL error fetching current series value for " + key + " : " + e, 0);
        }
        finally
        {
            tryToClose(con, readStmt, rs);
        }
    }


    public Collection<NumberSeries> getAllCurrent()
    {
        Connection con = null;
        Statement readStmt = null;
        ResultSet rs = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
        }
        catch(SQLException e)
        {
            throw new JaloSystemException(e, "SQL error fetching all current series : " + e, 0);
        }
        finally
        {
            tryToClose(con, readStmt, rs);
        }
    }


    public void initPersistence()
    {
        Connection con = null;
        Statement readStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            try
            {
                HybrisDataSource ds = getDataSource();
                String tbl = calculateTableName(ds);
                con = getConnection(ds);
                TableInfo info = getTableInfo(con, tbl);
                if(isSchemaInitOrUpdateRequired(info))
                {
                    DeploymentImpl deployments = TypeSystemUtils.loadDeployments("core", true);
                    ItemDeployment depl = deployments.getItemDeployment("de.hybris.jakarta.ext.numberseries.session.NumberSeries.NumberSeries");
                    if(depl != null)
                    {
                        initTable(con, deployments, depl, info);
                    }
                    else
                    {
                        System.err.println("cannot find number series deployment - aborting persistence init");
                        if(theUpdate != null)
                        {
                            theUpdate.close();
                        }
                        return;
                    }
                }
                if(isOldNumerMigrationRequired(info))
                {
                    readStmt = con.createStatement();
                    rs = readStmt.executeQuery("SELECT serieskey,currentNumber,seriestype FROM " + tbl + " WHERE currentNumber IS NOT NULL");
                    updateStmt = con.prepareStatement("UPDATE " + tbl + " SET currentNumber = NULL, currentValue=? WHERE serieskey=?");
                    int count = 0;
                    while(rs.next())
                    {
                        long value;
                        String key = rs.getString(1);
                        String oldValue = rs.getString(2);
                        int type = rs.getInt(3);
                        if(0 == type)
                        {
                            value = Long.parseLong(oldValue, 36);
                        }
                        else
                        {
                            value = Long.parseLong(oldValue);
                        }
                        updateStmt.setLong(1, value);
                        updateStmt.setString(2, key);
                        if(updateStmt.executeUpdate() < 1)
                        {
                            System.err.println("failed to patch old number series [" + key + "," + type + "," + oldValue + "->" + value + "]");
                        }
                        count++;
                    }
                    if(count > 0)
                    {
                        LOG.info("patched " + count + " old format number series");
                    }
                    updateStmt.close();
                    updateStmt = null;
                    updateStmt = con.prepareStatement("ALTER TABLE " + tbl + " DROP COLUMN currentNumber");
                    try
                    {
                        updateStmt.executeUpdate();
                    }
                    catch(SQLException sQLException)
                    {
                    }
                }
                if(theUpdate != null)
                {
                    theUpdate.close();
                }
            }
            catch(Throwable throwable)
            {
                if(theUpdate != null)
                {
                    try
                    {
                        theUpdate.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(SQLException e)
        {
            System.err.println("SQL error patching old number series : " + e.getMessage());
            e.printStackTrace(System.err);
        }
        finally
        {
            tryToClose(updateStmt, null);
            tryToClose(con, readStmt, rs);
        }
    }


    protected boolean isOldNumerMigrationRequired(TableInfo info)
    {
        return (info.exists && info.columnExists("currentNumber"));
    }


    protected boolean isSchemaInitOrUpdateRequired(TableInfo info)
    {
        if(!info.exists)
        {
            return true;
        }
        for(String col : REQUIRED_COLUMNS)
        {
            if(!info.columnExists(col))
            {
                return true;
            }
        }
        return false;
    }


    protected void initTable(Connection conn, DeploymentImpl deployments, ItemDeployment depl, TableInfo info)
    {
        if(!info.exists)
        {
            createSeriesTable(conn, deployments, depl);
        }
        else
        {
            alterSeriesTable(conn, deployments, depl, info);
        }
    }


    protected void alterSeriesTable(Connection conn, DeploymentImpl deployments, ItemDeployment depl, TableInfo info)
    {
        String tableName = depl.getDatabaseTableName();
        String database = getDataSource().getDatabaseName();
        boolean isOracle = Config.isOracleUsed();
        boolean isSqlServer = Config.isSQLServerUsed();
        boolean isHsql = Config.isHSQLDBUsed();
        Collection<ItemDeployment.Attribute> missingAttributes = new LinkedHashSet<>();
        for(ItemDeployment.Attribute attr : depl.getAttributes())
        {
            if(!info.columnExists(attr.getColumnName(database)))
            {
                missingAttributes.add(attr);
            }
        }
        if(!missingAttributes.isEmpty())
        {
            StringBuilder sb = new StringBuilder();
            if(isHsql)
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
                        sb.append("; ");
                    }
                    sb.append("ALTER TABLE ").append(tableName);
                    sb.append(" ADD COLUMN ");
                    sb.append(attr.getColumnName(database)).append(" ").append(attr.getColumnDeclaration(database, deployments))
                                    .append(" ");
                    sb.append(!attr.isNullAllowed(database) ? " NOT NULL " : "  ");
                }
            }
            else if(isSqlServer)
            {
                sb.append("ALTER TABLE ").append(tableName).append(" ADD ");
                boolean first = true;
                for(ItemDeployment.Attribute attr : missingAttributes)
                {
                    if(first)
                    {
                        first = false;
                    }
                    else
                    {
                        sb.append(" , ");
                    }
                    sb.append(attr.getColumnName(database)).append(" ").append(attr.getColumnDeclaration(database, deployments))
                                    .append(" ");
                    sb.append(!attr.isNullAllowed(database) ? " NOT NULL " : "  ");
                }
            }
            else if(isOracle)
            {
                sb.append("ALTER TABLE ").append(tableName).append(" ");
                for(ItemDeployment.Attribute attr : missingAttributes)
                {
                    sb.append(" ADD ");
                    sb.append(attr.getColumnName(database)).append(" ").append(attr.getColumnDeclaration(database, deployments));
                    sb.append(!attr.isNullAllowed(database) ? " NOT NULL " : "  ");
                }
            }
            else
            {
                boolean first = true;
                sb.append("ALTER TABLE ").append(tableName).append(" ");
                for(ItemDeployment.Attribute attr : missingAttributes)
                {
                    if(first)
                    {
                        first = false;
                    }
                    else
                    {
                        sb.append(" , ");
                    }
                    sb.append(" ADD ");
                    sb.append(attr.getColumnName(database)).append(" ").append(attr.getColumnDeclaration(database, deployments));
                    sb.append(!attr.isNullAllowed(database) ? " NOT NULL " : "  ");
                }
            }
            Statement stmt = null;
            try
            {
                stmt = conn.createStatement();
                stmt.executeUpdate(sb.toString());
            }
            catch(SQLWarning e)
            {
                LOG.warn("sql warning changing table via '" + sb.toString() + "' : " + e, e);
            }
            catch(SQLException e)
            {
                System.err.println("sql error changing table via '" + sb.toString() + "' : " + e);
                e.printStackTrace(System.err);
                throw new JaloSystemException(e);
            }
            finally
            {
                tryToClose(stmt, null);
            }
        }
    }


    protected void createSeriesTable(Connection conn, DeploymentImpl deployments, ItemDeployment depl)
    {
        String tableName = depl.getDatabaseTableName();
        String database = getDataSource().getDatabaseName();
        boolean isOracle = Config.isOracleUsed();
        boolean isSqlServer = Config.isSQLServerUsed();
        boolean isMySql = Config.isMySQLUsed();
        boolean isHana = Config.isHanaUsed();
        StringBuilder sb = (new StringBuilder("CREATE TABLE ")).append(tableName).append(" ( ");
        List<ItemDeployment.Attribute> primkeyAttrs = new ArrayList<>();
        boolean first = true;
        for(ItemDeployment.Attribute attr : depl.getAttributes())
        {
            if(attr.isPrimaryKey())
            {
                primkeyAttrs.add(attr);
            }
            if(first)
            {
                first = false;
            }
            else
            {
                sb.append(",");
            }
            sb.append(attr.getColumnName(database))
                            .append(" ")
                            .append(attr.getColumnDeclaration(database, deployments))
                            .append(" ");
            sb.append((!attr.isNullAllowed(database) || attr.isPrimaryKey()) ? " NOT NULL " : " ");
        }
        if(!primkeyAttrs.isEmpty())
        {
            String clustered = "";
            if(isSqlServer)
            {
                clustered = this.t.getConfig().getString("sqlserver.pkindextype", "NONCLUSTERED");
            }
            sb.append(" , ");
            if(!isHana)
            {
                sb.append("CONSTRAINT PK_").append(PK.createUUIDPK(0).getHex());
            }
            sb.append(" PRIMARY KEY ").append(clustered).append(" ( ");
            first = true;
            for(ItemDeployment.Attribute attr : primkeyAttrs)
            {
                if(first)
                {
                    first = false;
                }
                else
                {
                    sb.append(",");
                }
                sb.append(attr.getColumnName(database));
            }
            sb.append(" ) ");
        }
        if(isOracle)
        {
            String oracleIndexTS = this.t.getConfig().getParameter("oracle.indexTS");
            String oracleDataTS = this.t.getConfig().getParameter("oracle.dataTS");
            if(!primkeyAttrs.isEmpty())
            {
                first = true;
                StringBuilder cols = new StringBuilder();
                for(ItemDeployment.Attribute attr : primkeyAttrs)
                {
                    if(first)
                    {
                        first = false;
                    }
                    else
                    {
                        cols.append(",");
                    }
                    cols.append(attr.getColumnName(database));
                }
                sb.append("USING INDEX (");
                sb.append("CREATE INDEX " + tableName + "_PKIDX ON " + tableName + " (");
                sb.append("" + cols + " ) ");
                if(oracleIndexTS != null && !"".equals(oracleIndexTS))
                {
                    sb.append(" TABLESPACE ").append(oracleIndexTS).append(" ");
                }
                String idxOptions = this.t.getConfig().getString("oracle.globalIndexOptions", "");
                idxOptions = idxOptions.replace("@INDEXCOLUMNS@", cols);
                sb.append(" " + idxOptions + " ) ");
            }
            sb.append(" ) ");
            if(oracleDataTS != null && !"".equals(oracleDataTS))
            {
                sb.append("TABLESPACE ").append(oracleDataTS).append(" ");
            }
            sb.append(" " + this.t.getConfig().getString("oracle.globalTableOptions", "") + " ");
        }
        else
        {
            sb.append(")");
        }
        if(isMySql)
        {
            String tableType = this.t.getConfig().getParameter("mysql.tabletype");
            if(StringUtils.isNotBlank(tableType))
            {
                sb.append(" ENGINE ").append(tableType);
            }
            String optionalDefs = this.t.getConfig().getParameter("mysql.optional.tabledefs");
            if(StringUtils.isNotBlank(optionalDefs))
            {
                sb.append(" ").append(optionalDefs);
            }
        }
        Statement stmt = null;
        try
        {
            stmt = conn.createStatement();
            stmt.executeUpdate(sb.toString());
        }
        catch(SQLException e)
        {
            System.err.println("sql error creating table via '" + sb.toString() + "' : " + e);
            e.printStackTrace(System.err);
            throw new JaloSystemException(e);
        }
        finally
        {
            tryToClose(stmt, null);
        }
    }


    protected void tryToClose(Statement statement, ResultSet resultSet)
    {
        try
        {
            if(resultSet != null)
            {
                resultSet.close();
            }
        }
        catch(SQLException sQLException)
        {
        }
        try
        {
            if(statement != null)
            {
                statement.close();
            }
        }
        catch(SQLException sQLException)
        {
        }
    }


    protected void tryToClose(Connection connection, Statement statement, ResultSet resultSet)
    {
        try
        {
            if(resultSet != null)
            {
                resultSet.close();
            }
        }
        catch(SQLException sQLException)
        {
        }
        try
        {
            if(statement != null)
            {
                statement.close();
            }
        }
        catch(SQLException sQLException)
        {
        }
        try
        {
            if(connection != null)
            {
                connection.close();
            }
        }
        catch(SQLException sQLException)
        {
        }
    }


    protected TableInfo getTableInfo(Connection conn, String expectedName)
    {
        ResultSet tblRs = null, colRs = null;
        boolean exists = false;
        Set<String> columns = null;
        try
        {
            DatabaseMetaData md = conn.getMetaData();
            String schemaName = this.t.getDataSource().getSchemaName();
            String[] TABLE_TYPES = {"TABLE", "VIEW"};
            for(tblRs = md.getTables(null, schemaName, null, TABLE_TYPES); tblRs.next(); )
            {
                String tbl = tblRs.getString("TABLE_NAME");
                if(!expectedName.equalsIgnoreCase(tbl.toLowerCase(LocaleHelper.getPersistenceLocale())) || "INFORMATION_SCHEMA"
                                .equals(tblRs.getString("TABLE_SCHEM")))
                {
                    continue;
                }
                exists = true;
                columns = new LinkedHashSet<>();
                for(colRs = md.getColumns(null, schemaName, tbl, null); colRs.next(); )
                {
                    columns.add(colRs.getString("COLUMN_NAME"));
                }
            }
        }
        catch(SQLException e)
        {
            System.err.println("Error getting meta data : " + e.getMessage());
        }
        finally
        {
            tryToClose(null, colRs);
            tryToClose(null, tblRs);
        }
        return new TableInfo(exists, (columns != null) ? columns : Collections.EMPTY_SET);
    }
}
