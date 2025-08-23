package de.hybris.platform.util;

import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.threadregistry.OperationInfo;
import de.hybris.platform.core.threadregistry.RevertibleUpdate;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.util.jdbc.DatabaseNameResolver;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Driver;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.JdbcUtils;

class DBUtilities
{
    private static final Logger LOG = Logger.getLogger(DBUtilities.class.getName());
    private static final String HANA_APPLICATION_ID_KEY = "SESSIONVARIABLE:APPLICATION";
    private static final String HANA_APPLICATION_ID_VALUE = "SAP_COMMERCE_HRA";
    private static final String HANA_APPLICATION_ID = "SESSIONVARIABLE:APPLICATION=SAP_COMMERCE_HRA";
    private static final String HANA_APPLICATION_ID_REGEX = "((?i)&SESSIONVARIABLE:APPLICATION=[^&]*)";
    private static final String MYSQL_5X_DRIVER_SCHEMA_BEHAVIOUR_KEY = "nullCatalogMeansCurrent";
    private static final String MYSQL_5X_DRIVER_SCHEMA_BEHAVIOUR_VALUE = "true";
    private static final String MYSQL_5X_DRIVER_SCHEMA_BEHAVIOUR = "nullCatalogMeansCurrent=true";
    private static final String SQLSERVER_DEFAULT_ENCRYPT_PROPERTY_KEY = "encrypt";
    private static final String SQLSERVER_DEFAULT_ENCRYPT_PROPERTY_VALUE = "false";
    private static final String SQLSERVER_DEFAULT_ENCRYPT_PROPERTY = "encrypt=false";
    private static final PreparedStatementSetter PK_METADATA_SETTER = (PreparedStatementSetter)new Object();
    private static final ResultSetExtractor<Boolean> IS_INITIALIZED_EXTRACTOR = (ResultSetExtractor<Boolean>)new Object();
    private static final ResultSetExtractor<String> GET_SYSTEM_NAME_EXTRACTOR = (ResultSetExtractor<String>)new Object();


    boolean isDBConnectionValid(String dbUrl, String dbUser, String dbPassword, String driver, String fromJNDI)
    {
        if(StringUtils.isBlank(fromJNDI))
        {
            String databaseURLWithSpecialProperties = Utilities.applySpecialPropertiesToDatabaseUrl(dbUrl);
            return isDBConnectionValid(databaseURLWithSpecialProperties, dbUser, dbPassword, driver);
        }
        try
        {
            DataSource jndiSource = (DataSource)(new InitialContext()).lookup(fromJNDI);
            if(jndiSource == null)
            {
                LOG.error("Can not find JNDI data source '" + fromJNDI + "'");
                return false;
            }
            return true;
        }
        catch(Exception e)
        {
            LOG.error(e);
            return false;
        }
    }


    boolean isDBConnectionValid(String dbUrl, String dbUser, String dbPassword, String driver)
    {
        Connection conn = null;
        try
        {
            Class<?> driverClass = Class.forName(driver);
            Driver driverInstance = (Driver)driverClass.newInstance();
            Properties info = new Properties();
            info.put("user", dbUser);
            info.put("password", dbPassword);
            conn = driverInstance.connect(dbUrl, info);
            DatabaseMetaData md = conn.getMetaData();
            md.getDatabaseProductVersion();
            return true;
        }
        catch(Exception e)
        {
            LOG.error("Error while checking db connection", e);
            return false;
        }
        finally
        {
            JdbcUtils.closeConnection(conn);
        }
    }


    boolean isSystemInitialized(HybrisDataSource ds)
    {
        Tenant tenant = ds.getTenant();
        boolean initialized = false;
        try
        {
            RevertibleUpdate theUpdate = OperationInfo.updateThread(OperationInfo.builder().withCategory(OperationInfo.Category.SYSTEM).build());
            try
            {
                JdbcTemplate checkIsInitialized = new JdbcTemplate((DataSource)ds);
                String tablename = tenant.getConfig().getString("db.tableprefix", "") + "metainformations";
                initialized = ((Boolean)checkIsInitialized.query("SELECT isInitialized FROM " + tablename + " WHERE PK = ?", PK_METADATA_SETTER, IS_INITIALIZED_EXTRACTOR)).booleanValue();
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
        catch(DataAccessException e)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug(e.getMessage());
            }
            initialized = false;
        }
        return initialized;
    }


    String getTenantID(HybrisDataSource ds)
    {
        Tenant tenant = ds.getTenant();
        try
        {
            String tablename = tenant.getConfig().getString(Config.SystemSpecificParams.DB_TABLEPREFIX, "") + "metainformations";
            JdbcTemplate getTenantId = new JdbcTemplate((DataSource)ds);
            return (String)getTenantId.query("SELECT SystemName FROM " + tablename + " WHERE PK = ?", PK_METADATA_SETTER, GET_SYSTEM_NAME_EXTRACTOR);
        }
        catch(DataAccessException e)
        {
            if(LOG.isInfoEnabled())
            {
                LOG.info(e.getMessage());
            }
            return null;
        }
    }


    Map<String, String> getMySQLSlaveStatus(HybrisDataSource dsi)
    {
        if("mysql".equalsIgnoreCase(dsi.getDatabaseName()))
        {
            try
            {
                JdbcTemplate showSlaveStatus = new JdbcTemplate((DataSource)dsi);
                return (Map<String, String>)showSlaveStatus.query("show slave status", (ResultSetExtractor)new Object(this));
            }
            catch(DataAccessException e2)
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug(e2);
                }
                return Collections.EMPTY_MAP;
            }
        }
        return Collections.EMPTY_MAP;
    }


    String applySpecialPropertiesToDatabaseUrl(String databaseUrl)
    {
        if(databaseUrl != null)
        {
            String databaseName = DatabaseNameResolver.guessDatabaseNameFromURL(databaseUrl);
            if("sap".equals(databaseName))
            {
                String cleared = removeAllExistingApplicationIdEntries(databaseUrl);
                return addHanaApplicationId(cleared);
            }
            if("mysql".equals(databaseName))
            {
                return addMySqlLegacySchemaBehaviour(databaseUrl);
            }
            if("sqlserver".equals(databaseName))
            {
                return addSqlserverEncryptProperty(databaseUrl);
            }
        }
        return databaseUrl;
    }


    private String removeAllExistingApplicationIdEntries(String databaseUrl)
    {
        Pattern pattern = Pattern.compile("((?i)&SESSIONVARIABLE:APPLICATION=[^&]*)");
        String updated = databaseUrl.replace("?", "&");
        Matcher matcher = pattern.matcher(updated);
        updated = matcher.replaceAll("");
        updated = updated.replaceFirst("&", "?");
        return updated;
    }


    private String addHanaApplicationId(String databaseUrl)
    {
        if(databaseUrl.contains("?"))
        {
            return databaseUrl + "&SESSIONVARIABLE:APPLICATION=SAP_COMMERCE_HRA";
        }
        return databaseUrl + "?SESSIONVARIABLE:APPLICATION=SAP_COMMERCE_HRA";
    }


    private String addMySqlLegacySchemaBehaviour(String databaseUrl)
    {
        if(databaseUrl.contains("nullCatalogMeansCurrent"))
        {
            return databaseUrl;
        }
        if(databaseUrl.contains("?"))
        {
            return databaseUrl + "&nullCatalogMeansCurrent=true";
        }
        return databaseUrl + "?nullCatalogMeansCurrent=true";
    }


    private String addSqlserverEncryptProperty(String databaseUrl)
    {
        if(databaseUrl.contains("encrypt"))
        {
            return databaseUrl;
        }
        return databaseUrl + ";encrypt=false";
    }
}
