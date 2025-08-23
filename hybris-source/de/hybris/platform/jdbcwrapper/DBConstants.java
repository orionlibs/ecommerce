package de.hybris.platform.jdbcwrapper;

public final class DBConstants
{
    public static final String LOG_ACTIVATED = "db.log.active";
    public static final boolean DEFAULT_LOG_ACTIVATED = false;
    public static final String LOG_STACKAPPENDING_ACTIVATED = "db.log.appendStackTrace";
    public static final boolean DEFAULT_LOG_STACKAPPENDING_ACTIVATED = false;
    public static final String LOG_SQL_WITH_PARAMETERS = "db.log.sql.parameters";
    public static final boolean DEFAULT_LOG_SQL_WITH_PARAMETERS = false;
    public static final String LOG_LOGGERCLASS = "db.log.loggerclass";
    public static final String LOG_FILE_PATH = "db.log.file.path";
    public static final String DEFAULT_LOG_FILE_PATH = "jdbc.log";
    public static final String LOG_DATEFORMAT = "db.log.dateformat";
    public static final String DEFAULT_LOG_DATEFORMAT = "yyMMdd HH:mm:ss:SS";
    public static final String LOG_TIMETHRESHOLD = "db.log.filter.timethreshold";
    public static final int DEFAULT_LOG_TIMETHRESHOLD = -1;
    public static final String LOG_INCLUDETABLES = "db.log.filter.includetables";
    public static final String DEFAULT_LOG_INCLUDETABLES = "";
    public static final String LOG_EXCLUDETABLES = "db.log.filter.excludetables";
    public static final String DEFAULT_LOG_EXCLUDETABLES = "";
    public static final String LOG_INCLUDECATEGORIES = "db.log.includecategories";
    public static final String DEFAULT_LOG_INCLUDECATEGORIES = "";
    public static final String LOG_EXCLUDECATEGORIES = "db.log.excludecategories";
    public static final String DEFAULT_LOG_EXCLUDECATEGORIES = "";
    public static final String POOL_CLOSE_ON_ROLLBACK_AFTER_ERROR = "db.pool.closeOnRollbackAfterError";
    public static final String POOL_DUMP_STACK_ON_CONNECTION_ERROR = "db.pool.dumpThreadsOnBorrowError";
    public static final boolean DEFAULT_POOL_DUMP_STACK_ON_CONNECTION_ERROR = true;
    public static final String CONNECT_RETRIES = "db.pool.connectRetries";
    public static final int DEFAULT_CONNECT_RETRIES = 3;
    public static final String ORACLE_STATEMENT_CACHE_SIZE = "oracle.statementcachesize";
    public static final int DEFAULT_ORACLE_STATEMENT_CACHE_SIZE = 0;
}
