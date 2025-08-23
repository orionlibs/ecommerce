package de.hybris.platform.hac.facade;

import de.hybris.platform.core.Registry;
import de.hybris.platform.hac.data.dto.DatabaseInfoData;
import de.hybris.platform.jdbcwrapper.DataSourceImpl;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.jdbcwrapper.logger.JDBCSLF4JAwareLogger;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import groovy.lang.Binding;
import groovy.util.GroovyScriptEngine;
import groovy.util.ResourceException;
import groovy.util.ScriptException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.FileUtils;
import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

public class HacDatabaseFacade
{
    private static final Logger LOG = LoggerFactory.getLogger(HacDatabaseFacade.class);
    private JdbcTemplate jdbcTemplate;
    private static final String COUNT_QUERY = "SELECT COUNT(*) FROM {0}";


    public DatabaseInfoData getDatabaseInfoForInitUpdatePage()
    {
        DatabaseInfoData dbInfoData = new DatabaseInfoData();
        DatabaseData dbData = getDatabaseData();
        dbInfoData.setUrl(dbData.getUrl().replace(";", "; "));
        dbInfoData.setPool((getJNDIName() == null) ? "hybris" : ("JNDI " + getJNDIName()));
        dbInfoData.setTablePrefixName(getDbTablePrefix());
        dbInfoData.setDbName(dbData.getName());
        dbInfoData.setDbUser(dbData.getUserAccount());
        return dbInfoData;
    }


    @Transactional(value = "txManager", readOnly = true)
    public Map<String, Long> getDatabaseTableRowsCount()
    {
        Map<String, Long> result = new HashMap<>();
        for(String tableName : getTables())
        {
            try
            {
                int countResult = getRowsCountFromTable(tableName);
                LOG.debug("Count for table {} is {}", tableName, Integer.valueOf(countResult));
                result.put(tableName, Long.valueOf(countResult));
            }
            catch(RuntimeException e)
            {
                LOG.debug("Can't count rows for table: {} (reason: {})", tableName, e.getMessage());
            }
        }
        return result;
    }


    private int getRowsCountFromTable(String tableName)
    {
        String query = MessageFormat.format("SELECT COUNT(*) FROM {0}", new Object[] {tableName});
        Integer result = (Integer)this.jdbcTemplate.queryForObject(query, Integer.class);
        return result.intValue();
    }


    private DatabaseData getDatabaseData()
    {
        try
        {
            Connection connection = getConnection();
            try
            {
                DatabaseMetaData metaData = connection.getMetaData();
                String name = metaData.getDatabaseProductName();
                String url = metaData.getURL();
                String userAccount = DataSourceImpl.getUserNameFromDatabaseMetaData(metaData,
                                Config.getParameter("db.username"));
                DatabaseData databaseData = new DatabaseData(this, url, name, userAccount);
                if(connection != null)
                {
                    connection.close();
                }
                return databaseData;
            }
            catch(Throwable throwable)
            {
                if(connection != null)
                {
                    try
                    {
                        connection.close();
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
            throw new SystemException(e);
        }
    }


    public Map analyzeLog() throws IOException, ResourceException, ScriptException
    {
        Map<String, Object> returnMap = new HashMap<>();
        URL groovyScriptPath = HacDatabaseFacade.class.getResource("/groovyscripts/");
        URL[] roots = {groovyScriptPath};
        GroovyScriptEngine gse = new GroovyScriptEngine(roots);
        Binding binding = new Binding();
        String jdbcFileName = getDbLogFileName();
        binding.setVariable("jdbcFile", jdbcFileName);
        File jdbcFile = new File(jdbcFileName);
        if(jdbcFile.exists())
        {
            try
            {
                gse.run("analyzeJdbcLog.groovy", binding);
                returnMap.putAll((Map<? extends String, ?>)binding.getVariable("result"));
                returnMap.put("success", Boolean.TRUE);
            }
            catch(Exception e)
            {
                LOG.error("Error", e);
                returnMap.put("success", Boolean.FALSE);
                returnMap.put("error", "Error while parsing the jdbclog file: " + jdbcFile
                                .getAbsolutePath() + ". This functionality may not work in case of customized appender layout pattern.");
            }
            return returnMap;
        }
        returnMap.put("success", Boolean.FALSE);
        returnMap.put("error", "Configured jdbclog file " + jdbcFile
                        .getAbsolutePath() + " not exists");
        return returnMap;
    }


    public HybrisDataSource getDataSource()
    {
        return Registry.getCurrentTenant().getDataSource();
    }


    public DatabaseInfoData resetStatsForDataSource(String datasourceID)
    {
        HybrisDataSource dataSource = getMasterDataSource();
        if(datasourceID.equals(dataSource.getID()))
        {
            dataSource.resetStats();
            return buildDbInfoDataForSource(dataSource);
        }
        for(HybrisDataSource hds : getAllSlaveDataSources())
        {
            if(datasourceID.equals(hds.getID()))
            {
                hds.resetStats();
                return buildDbInfoDataForSource(hds);
            }
        }
        for(HybrisDataSource hds : getAlternativeDataSources())
        {
            if(datasourceID.equals(hds.getID()))
            {
                hds.resetStats();
                return buildDbInfoDataForSource(hds);
            }
        }
        throw new IllegalArgumentException("DataSource ID does not exist");
    }


    public Map<String, DatabaseInfoData> databaseInfo()
    {
        Map<String, DatabaseInfoData> map = new LinkedHashMap<>();
        map.put("master", getMasterDataSourceInfo());
        for(DatabaseInfoData slave : getSlaveDataSourcesInfos())
        {
            map.put(slave.getDsId(), slave);
        }
        for(DatabaseInfoData alternative : getAlternativeDataSourcesInfos())
        {
            map.put(alternative.getDsId(), alternative);
        }
        return map;
    }


    private DatabaseInfoData getMasterDataSourceInfo()
    {
        return buildDbInfoDataForSource(getMasterDataSource());
    }


    private List<DatabaseInfoData> getSlaveDataSourcesInfos()
    {
        List<DatabaseInfoData> result = new ArrayList<>();
        for(HybrisDataSource dataSource : getAllSlaveDataSources())
        {
            result.add(buildDbInfoDataForSource(dataSource, true));
        }
        return result;
    }


    private List<DatabaseInfoData> getAlternativeDataSourcesInfos()
    {
        List<DatabaseInfoData> result = new ArrayList<>();
        for(HybrisDataSource dataSource : getAlternativeDataSources())
        {
            result.add(buildDbInfoDataForSource(dataSource));
        }
        return result;
    }


    private Collection<HybrisDataSource> getAlternativeDataSources()
    {
        return Registry.getCurrentTenant().getAllAlternativeMasterDataSources();
    }


    public boolean isMasterDataSource()
    {
        return getMasterDataSource().equals(getDataSource());
    }


    public void toggleDatabaseLogging(boolean enabled)
    {
        String enabledStr = Boolean.toString(enabled);
        Config.setParameter("db.log.active", enabledStr);
        LOG.debug("Logging is now {}", enabledStr);
    }


    public void toggleQueriesWithParams(boolean enabled)
    {
        String enabledStr = Boolean.toString(enabled);
        Config.setParameter("db.log.sql.parameters", enabledStr);
        LOG.debug("Params in queries are now {}", enabledStr);
    }


    public void toggleTracesInLogOutput(boolean enabled)
    {
        String enabledStr = Boolean.toString(enabled);
        Config.setParameter("db.log.appendStackTrace", enabledStr);
        LOG.debug("Stacktraces now: {}", enabledStr);
    }


    private DatabaseInfoData buildDbInfoDataForSource(HybrisDataSource dataSource)
    {
        return buildDbInfoDataForSource(dataSource, false);
    }


    private DatabaseInfoData buildDbInfoDataForSource(HybrisDataSource dataSource, boolean isSlaveDataSource)
    {
        DatabaseInfoData dbInfoData = new DatabaseInfoData();
        dbInfoData.setActive(isDataSourceActive(dataSource.getID()));
        dbInfoData.setDsId(dataSource.getID());
        dbInfoData.setNumInUse(dataSource.getNumInUse());
        dbInfoData.setNumPhysicalOpen(dataSource.getNumPhysicalOpen());
        dbInfoData.setMaxPhysicalOpen(dataSource.getMaxPhysicalOpen());
        dbInfoData.setMaxAllowedOpen(dataSource.getMaxAllowedPhysicalOpen());
        dbInfoData.setTotalGets(dataSource.totalGets());
        dbInfoData.setMillisWaitedForConn(dataSource.getMillisWaitedForConnection());
        dbInfoData.setNumReadOnlyOpen(dataSource.getNumReadOnlyOpen());
        if(!GenericValidator.isBlankOrNull(dataSource.getTablePrefix()))
        {
            dbInfoData.setTablePrefix(true);
            dbInfoData.setTablePrefixName(dataSource.getTablePrefix());
        }
        if(dataSource.getJNDIName() == null)
        {
            dbInfoData.setJndi(false);
            dbInfoData.setPool("hybris");
            dbInfoData.setUrl(dataSource.getDatabaseURL());
            dbInfoData.setDbName(dataSource.getDatabaseName());
            dbInfoData.setDbUser(dataSource.getDatabaseUser());
            dbInfoData.setDbDriverVersion(dataSource.getDriverVersion());
            dbInfoData.setDbVersion(dataSource.getDatabaseVersion());
        }
        else
        {
            dbInfoData.setJndi(true);
            dbInfoData.setPool("JNDI " + getJNDIName());
        }
        if(isSlaveDataSource)
        {
            Map<String, String> mysqlInfos = getMysqlSlaveStatus(dataSource);
            if(!mysqlInfos.isEmpty())
            {
                dbInfoData.setMysqlInfos(mysqlInfos);
            }
        }
        return dbInfoData;
    }


    private boolean isDataSourceActive(String dsid)
    {
        return dsid.equals(getDataSource().getID());
    }


    private Collection<HybrisDataSource> getAllSlaveDataSources()
    {
        return Registry.getCurrentTenant().getAllSlaveDataSources();
    }


    private Connection getConnection()
    {
        try
        {
            return getDataSource().getConnection();
        }
        catch(SQLException e)
        {
            throw new SystemException(e);
        }
    }


    private String getDbTablePrefix()
    {
        return Config.getString("db.tableprefix", "none");
    }


    private String getJNDIName()
    {
        return getDataSource().getJNDIName();
    }


    private HybrisDataSource getMasterDataSource()
    {
        return Registry.getCurrentTenant().getMasterDataSource();
    }


    private Map<String, String> getMysqlSlaveStatus(HybrisDataSource dataSource)
    {
        return Utilities.getMySQLSlaveStatus(dataSource);
    }


    private List<String> getTables()
    {
        try
        {
            Connection connection = getConnection();
            try
            {
                List<String> result = new ArrayList<>();
                DatabaseMetaData metaData = connection.getMetaData();
                ResultSet tablesResult = metaData.getTables(null, getDataSource().getSchemaName(), "%", new String[] {"TABLE"});
                String tenantPrefix = Config.getString("db.tableprefix", "").toLowerCase();
                while(tablesResult.next())
                {
                    String table = tablesResult.getString("TABLE_NAME");
                    if(table != null && table.toLowerCase().startsWith(tenantPrefix))
                    {
                        result.add(table);
                    }
                }
                tablesResult.close();
                List<String> list1 = result;
                if(connection != null)
                {
                    connection.close();
                }
                return list1;
            }
            catch(Throwable throwable)
            {
                if(connection != null)
                {
                    try
                    {
                        connection.close();
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
            throw new SystemException(e);
        }
    }


    public Map<String, Object> databaseLogsInfo() throws IOException
    {
        Map<String, Object> map = new HashMap<>();
        String fileName = getDbLogFileName();
        File file = new File(fileName);
        if(file.exists())
        {
            map.put("logEnabled", isDbLoggingEnabled());
            map.put("tracesEnabled", isTraceInLogEnabled());
            map.put("logFileSize", getFileSizeInKb(file));
            map.put("logFilePath", file.getAbsolutePath());
        }
        return map;
    }


    private Boolean isDbLoggingEnabled()
    {
        return Boolean.valueOf(Config.getBoolean("db.log.active", false));
    }


    private Boolean isTraceInLogEnabled()
    {
        return Boolean.valueOf(Config.getBoolean("db.log.appendStackTrace", false));
    }


    private Integer getFileSizeInKb(File file)
    {
        return Integer.valueOf((int)(file.length() / 1024L));
    }


    public Map<String, Object> databaseClearLog()
    {
        String fileName = getDbLogFileName();
        File file = new File(fileName);
        LOG.debug("Trying to clear or delete log file: {}", fileName);
        Map<String, Object> deleteResult = new HashMap<>();
        if(file.exists() && file.length() > 0L)
        {
            try
            {
                Config.setParameter("db.log.file.path", "");
                clearOrDeleteFile(file);
                deleteResult.put("success", Boolean.TRUE);
            }
            catch(IOException e)
            {
                deleteResult.put("success", Boolean.FALSE);
                deleteResult.put("error", "Configured jdbclog file " + file
                                .getAbsolutePath() + " can not be deleted: " + e.getMessage());
            }
            finally
            {
                Config.setParameter("db.log.file.path", fileName);
            }
        }
        return deleteResult;
    }


    private void clearOrDeleteFile(File file) throws IOException
    {
        try
        {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        }
        catch(IOException ex)
        {
            try
            {
                FileUtils.forceDelete(file);
            }
            catch(IOException e)
            {
                (new FileOutputStream(file)).close();
            }
        }
    }


    public void databaseLogsDownload(HttpServletResponse response, int downloadSize)
    {
        prepareResponseHeaderForFile(response, "jdbc.log", downloadSize);
        RandomAccessFile file = null;
        try
        {
            PrintWriter out = response.getWriter();
            try
            {
                file = new RandomAccessFile(getDbLogFileName(), "r");
                if(downloadSize > 0)
                {
                    long pos = file.length() - (downloadSize * 1024);
                    if(pos > 0L && pos < file.length())
                    {
                        file.seek(pos);
                    }
                }
                String rline;
                while((rline = file.readLine()) != null)
                {
                    response.getWriter().println(rline);
                    out.flush();
                    response.flushBuffer();
                }
                if(out != null)
                {
                    out.close();
                }
            }
            catch(Throwable throwable)
            {
                if(out != null)
                {
                    try
                    {
                        out.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(FileNotFoundException e)
        {
            errorHandling(e, "File not found");
        }
        catch(IOException e)
        {
            errorHandling(e, "IO Exception");
        }
        finally
        {
            if(file != null)
            {
                try
                {
                    file.close();
                }
                catch(IOException e)
                {
                    errorHandling(e, "IO Exception");
                }
            }
        }
    }


    private String getDbLogFileName()
    {
        if(Config.getString("db.log.loggerclass", "").equals(JDBCSLF4JAwareLogger.class.getName()))
        {
            return Config.getString("log4j2.appender.jdbcFileLogger.fileName", "");
        }
        return Config.getString("db.log.file.path", "");
    }


    private void errorHandling(Exception e, String message)
    {
        LOG.error(message);
        LOG.debug(message, e);
    }


    private void prepareResponseHeaderForFile(HttpServletResponse response, String fileName, int downloadSize)
    {
        LOG.debug("Preparing log download... downloadSize: {}", Integer.valueOf(downloadSize));
        response.setContentType("text/plain");
        response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
    }


    @Required
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
        this.jdbcTemplate = jdbcTemplate;
    }
}
