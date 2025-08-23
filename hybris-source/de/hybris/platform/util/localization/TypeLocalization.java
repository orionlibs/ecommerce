package de.hybris.platform.util.localization;

import com.google.common.base.Stopwatch;
import com.google.common.util.concurrent.MoreExecutors;
import de.hybris.platform.core.Registry;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ThreadUtilities;
import de.hybris.platform.util.localization.jalo.JaloBasedTypeLocalization;
import de.hybris.platform.util.localization.jdbc.DbInfo;
import de.hybris.platform.util.localization.jdbc.JdbcBasedTypeSystemLocalization;
import de.hybris.platform.util.localization.jdbc.LocalizationInfo;
import de.hybris.platform.util.localization.jdbc.StatementsExecutor;
import de.hybris.platform.util.localization.jdbc.executor.BatchedStatementsExecutor;
import de.hybris.platform.util.localization.jdbc.info.JaloBasedDbInfo;
import de.hybris.platform.util.localization.jdbc.info.PropertiesBasedLocalizationInfo;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

public final class TypeLocalization
{
    private static final String PK_ALIAS = "PK";
    private static final String CODE_ALIAS = "Code";
    private static final TypeLocalization INSTANCE = new TypeLocalization();
    private static final Logger LOG = Logger.getLogger(TypeLocalization.class);


    public static TypeLocalization getInstance()
    {
        return INSTANCE;
    }


    public Map<Language, Properties> getLocalizations()
    {
        return JaloBasedTypeLocalization.getInstance().getLocalizations();
    }


    public void localizeTypes()
    {
        Stopwatch stopwatch = Stopwatch.createStarted();
        if(isJdbcLocalizationEnabled())
        {
            localizeTypesJdbcWay();
        }
        else
        {
            localizeTypesJaloWay();
        }
        LOG.info("Localization took " + stopwatch);
    }


    public void clearLocalizationCache()
    {
        JaloBasedTypeLocalization.getInstance().clearCache();
    }


    private void localizeTypesJdbcWay()
    {
        DataSource dataSource = (DataSource)Registry.getCoreApplicationContext().getBean("dataSource", DataSource.class);
        ExecutorService executorService = getLocalizationExecutorService();
        try
        {
            JaloBasedDbInfo jaloBasedDbInfo = new JaloBasedDbInfo();
            PropertiesBasedLocalizationInfo propertiesBasedLocalizationInfo = new PropertiesBasedLocalizationInfo(getLocalizations(), buildPkToTypeCodeMap((DbInfo)jaloBasedDbInfo, dataSource));
            BatchedStatementsExecutor batchedStatementsExecutor = new BatchedStatementsExecutor(executorService, dataSource);
            JdbcBasedTypeSystemLocalization localization = new JdbcBasedTypeSystemLocalization(executorService, dataSource, (DbInfo)jaloBasedDbInfo, (LocalizationInfo)propertiesBasedLocalizationInfo, (StatementsExecutor)batchedStatementsExecutor);
            localization.localizeTypeSystem();
        }
        finally
        {
            executorService.shutdown();
        }
    }


    private Map<Long, String> buildPkToTypeCodeMap(DbInfo dbInfo, DataSource dataSource)
    {
        Map<Long, String> pkToTypeCode = new HashMap<>();
        JdbcTemplate jdbc = new JdbcTemplate(dataSource);
        jdbc.query(renderPkToTypeCodeQry(dbInfo), rs -> {
            long pk = rs.getLong("PK");
            String code = rs.getString("Code");
            pkToTypeCode.put(Long.valueOf(pk), code);
        });
        return pkToTypeCode;
    }


    private String renderPkToTypeCodeQry(DbInfo dbInfo)
    {
        String composedTypeTable = dbInfo.getTableNameFor("ComposedType");
        String pkCol = dbInfo.getColumnNameFor("ComposedType", "pk");
        String codeCol = dbInfo.getColumnNameFor("ComposedType", "code");
        return String.format("select %s AS %s, %s AS %s from %s", new Object[] {pkCol, "PK", codeCol, "Code", composedTypeTable});
    }


    private void localizeTypesJaloWay()
    {
        JaloBasedTypeLocalization.getInstance().localizeTypes();
    }


    private boolean isJdbcLocalizationEnabled()
    {
        return Config.getBoolean("types.localization.jdbc.mode.enabled", true);
    }


    private ExecutorService getLocalizationExecutorService()
    {
        int numberOfThreads = getNumberOfLocalizationThreads();
        LOG.info("" + numberOfThreads + numberOfThreads + " will be used to localize type system.");
        if(numberOfThreads == 1)
        {
            return (ExecutorService)MoreExecutors.newDirectExecutorService();
        }
        return (ExecutorService)new TypeLocalizationThreadPoolExecutor(numberOfThreads, numberOfThreads);
    }


    private int getNumberOfLocalizationThreads()
    {
        String expression = Config.getParameter("types.localization.jdbc.number.of.threads");
        return ThreadUtilities.getNumberOfThreadsFromExpression(expression, 2);
    }
}
