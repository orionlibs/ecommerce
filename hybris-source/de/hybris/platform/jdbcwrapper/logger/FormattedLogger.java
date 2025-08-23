package de.hybris.platform.jdbcwrapper.logger;

import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;
import java.util.List;
import org.apache.commons.lang.StringUtils;

public abstract class FormattedLogger implements JDBCLogger
{
    private static final String DELIM = "|";
    private String lastEntry;


    public void logSQL(long threadId, String dataSourceID, int connectionId, String now, long elapsed, String category, String prepared, String sql)
    {
        logText("" + threadId + "|" + threadId + "|" + dataSourceID + "|" + now + " ms|" + elapsed + "|" + category + prepared);
    }


    String sqlWithParametersIfEnabled(String sql)
    {
        String result;
        if(StringUtils.isBlank(sql))
        {
            return "";
        }
        if(containsStacktrace(sql))
        {
            result = createResultWithStrackTrace(sql);
        }
        else
        {
            result = logWithParameters() ? sql : "";
        }
        return StringUtils.isEmpty(result) ? result : ("|" + result);
    }


    private String createResultWithStrackTrace(String sql)
    {
        List<String> sqlAndStack = split(sql);
        if(logWithParameters() && logWithStacktrace())
        {
            return String.join("|", (Iterable)sqlAndStack);
        }
        if(logWithParameters() && !logWithStacktrace())
        {
            return sqlAndStack.get(0);
        }
        if(!logWithParameters() && logWithStacktrace())
        {
            return sqlAndStack.get(1);
        }
        return "";
    }


    private List<String> split(String sqlAndStacktrace)
    {
        int startOfStacktrace = sqlAndStacktrace.lastIndexOf("/*");
        if(startOfStacktrace == -1)
        {
            return (List<String>)ImmutableList.of(sqlAndStacktrace, "");
        }
        String query = sqlAndStacktrace.substring(0, startOfStacktrace);
        String stacktrace = sqlAndStacktrace.substring(startOfStacktrace);
        return (List<String>)ImmutableList.of(query.trim(),
                        StringUtils.removeEnd(StringUtils.removeStart(stacktrace, "/*"), "*/").trim());
    }


    private boolean containsStacktrace(String sql)
    {
        if(StringUtils.isBlank(sql))
        {
            return false;
        }
        return sql.endsWith("*/");
    }


    private boolean logWithParameters()
    {
        if(Registry.isCurrentTenantStarted())
        {
            return Config.getBoolean("db.log.sql.parameters", false);
        }
        return false;
    }


    private boolean logWithStacktrace()
    {
        if(Registry.isCurrentTenantStarted())
        {
            return Config.getBoolean("db.log.appendStackTrace", false);
        }
        return false;
    }


    public void setLastEntry(String inVar)
    {
        this.lastEntry = inVar;
    }


    public String getLastEntry()
    {
        return this.lastEntry;
    }
}
