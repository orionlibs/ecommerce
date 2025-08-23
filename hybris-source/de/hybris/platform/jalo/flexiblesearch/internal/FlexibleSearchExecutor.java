package de.hybris.platform.jalo.flexiblesearch.internal;

import com.google.common.base.Preconditions;
import com.google.common.base.Stopwatch;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.WrapperFactory;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearchException;
import de.hybris.platform.jalo.flexiblesearch.hints.Hint;
import de.hybris.platform.jalo.flexiblesearch.hints.PreparedStatementHint;
import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilder;
import de.hybris.platform.jalo.flexiblesearch.limit.LimitStatementBuilderFactory;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.ItemRemote;
import de.hybris.platform.persistence.flexiblesearch.CaseInsensitiveParameterMap;
import de.hybris.platform.persistence.flexiblesearch.TranslatedQuery;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.ItemPropertyValueCollection;
import de.hybris.platform.util.SQLSearchResultFactory;
import de.hybris.platform.util.Utilities;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import javax.sql.DataSource;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlexibleSearchExecutor
{
    private static final Logger LOG = LoggerFactory.getLogger(FlexibleSearchExecutor.class);
    public static final String FLEXIBLE_SEARCH_EXCEPTION_SHOW_QUERY_DETAILS = "flexible.search.exception.show.query.details";
    public static final int RANGE_START_AT_BEGINNING = 0;
    public static final int RANGE_COUNT_INFINITIVE = -1;
    private final Tenant tenant;
    private final LimitStatementBuilderFactory limitStatementFactory;
    private final ReadOnlyConditionsHelper readOnlyConditionsHelper;


    public FlexibleSearchExecutor(Tenant tenant)
    {
        this(tenant, new ReadOnlyConditionsHelper());
    }


    public FlexibleSearchExecutor(Tenant tenant, ReadOnlyConditionsHelper readOnlyConditionsHelper)
    {
        Preconditions.checkArgument((tenant != null), "Missing required argument tenant");
        Preconditions.checkArgument((readOnlyConditionsHelper != null), "Missing required argument readOnlyConditionsHelper");
        this.tenant = tenant;
        this.limitStatementFactory = new LimitStatementBuilderFactory(tenant);
        this.readOnlyConditionsHelper = readOnlyConditionsHelper;
    }


    public HybrisDataSource getDataSourceForQuery(List<Hint> hints)
    {
        if(!this.readOnlyConditionsHelper.couldUseReadOnlyDataSource(this.tenant, hints))
        {
            return this.tenant.getDataSource();
        }
        Objects.requireNonNull(this.tenant);
        return this.readOnlyConditionsHelper.getReadOnlyDataSource(this.tenant).orElseGet(this.tenant::getDataSource);
    }


    public ReadOnlyConditionsHelper getReadOnlyConditionsHelper()
    {
        return this.readOnlyConditionsHelper;
    }


    public void processSearchRows(int start, int count, TranslatedQuery translatedQuery, List<Class<?>> jaloClasses, Map values, PK languagePK, List<Hint> hints, Consumer<Object> rowConsumer)
    {
        processSearchRows(start, count, translatedQuery, jaloClasses, values, languagePK, hints, rowConsumer, (DataSource)
                        getDataSourceForQuery(hints));
    }


    public void processSearchRows(int start, int count, TranslatedQuery translatedQuery, List<Class<?>> jaloClasses, Map values, PK languagePK, List<Hint> hints, Consumer<Object> rowConsumer, DataSource dataSource)
    {
        CacheableResultHolder.ConversionMetaInformation conversionResult = convertResultClasses(jaloClasses);
        TranslatedQuery.ExecutableQuery executableQuery = getExecutableQuery(translatedQuery, values, languagePK);
        JDBCValueMappings jdbcVM = JDBCValueMappings.getInstance();
        LimitStatementBuilder limitBuilder = this.limitStatementFactory.getLimitStatementBuilder(executableQuery, start, count);
        String query = HintsApplier.filterAndApplyQueryHints(limitBuilder.getModifiedStatement(), hints);
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try
        {
            connection = dataSource.getConnection();
            statement = jdbcVM.fillStatement(
                            HintsApplier.filterAndApplyPreparedStatementHints(connection
                                            .prepareStatement(query, 1003, 1007), hints), limitBuilder
                                            .getModifiedStatementValues());
            handleAutoCommit(statement, hints);
            PreparedStatement localStmt = statement;
            Stopwatch queryTimer = Stopwatch.createUnstarted();
            Objects.requireNonNull(localStmt);
            ResultSet rs = withStopwatchMeasurement(queryTimer, localStmt::executeQuery);
            resultSet = rs;
            int limitStart = limitBuilder.hasDbEngineLimitSupport() ? 0 : limitBuilder.getOriginalStart();
            int limitCount = limitBuilder.hasDbEngineLimitSupport() ? -1 : limitBuilder.getOriginalCount();
            Stopwatch processTime = Stopwatch.createUnstarted();
            withStopwatchMeasurement(processTime, () -> jdbcVM.processRows(rs, new ArrayList(conversionResult.getConvertedClasses()), limitStart, limitCount, rowConsumer));
            if(LOG.isDebugEnabled())
            {
                debugQuery(conversionResult, executableQuery, limitBuilder, query, queryTimer, processTime, -1);
            }
        }
        catch(SQLException | FlexibleSearchException ex)
        {
            String errorMessageSecondPart = getErrorMessage(executableQuery, ex.getMessage(), query);
            throw new FlexibleSearchException(ex, "SQL search error - " + errorMessageSecondPart, 0);
        }
        finally
        {
            enableAutoCommit(statement, hints);
            Utilities.tryToCloseJDBC(connection, statement, resultSet);
        }
    }


    private void debugQuery(CacheableResultHolder.ConversionMetaInformation conversionResult, TranslatedQuery.ExecutableQuery executableQuery, LimitStatementBuilder limitBuilder, String query, Stopwatch queryTimer, Stopwatch processTimer, int totalCount)
    {
        LOG.debug("original query='{}', modified query='{}' sig={}, values=[{}], original range=[{}, {}], rowcount={}, time stats=[query: {}, fetch: {}]", new Object[] {executableQuery
                        .getSQL(), query, conversionResult.getConvertedClasses(), executableQuery
                        .getValueList(), Integer.valueOf(limitBuilder.getOriginalStart()), Integer.valueOf(limitBuilder.getOriginalCount()), Integer.valueOf(totalCount), queryTimer, processTimer});
    }


    @Deprecated(since = "2105", forRemoval = true)
    public SearchResult execute(int start, int count, boolean dontNeedTotal, TranslatedQuery translatedQuery, List<Class<?>> resultClasses, Map values, PK languagePK, int prefetchSize, Set<PK> prefetchLanguages, List<Hint> hints)
    {
        return execute(start, count, dontNeedTotal, translatedQuery, resultClasses, values, languagePK, prefetchSize, prefetchLanguages, hints, (DataSource)
                        getDataSourceForQuery(hints));
    }


    public SearchResult execute(int start, int count, boolean dontNeedTotal, TranslatedQuery translatedQuery, List<Class<?>> resultClasses, Map values, PK languagePK, int prefetchSize, Set<PK> prefetchLanguages, List<Hint> hints, DataSource dataSource)
    {
        SearchResult result;
        CacheableResultHolder.ConversionMetaInformation conversionResult = convertResultClasses(resultClasses);
        TranslatedQuery.ExecutableQuery executableQuery = getExecutableQuery(translatedQuery, values, languagePK);
        JDBCValueMappings jdbcVM = JDBCValueMappings.getInstance();
        LimitStatementBuilder limitBuilder = this.limitStatementFactory.getLimitStatementBuilder(executableQuery, start, count);
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = null;
        try
        {
            connection = dataSource.getConnection();
            if(isNeedTotalAndCountIsZero(count, dontNeedTotal))
            {
                int totalCount = jdbcVM.getTotalCountFromCountQuery(connection, executableQuery.getCountSQL(), executableQuery
                                .getCountValueList(), hints);
                result = SQLSearchResultFactory.createCountOnlyResult(totalCount, start, count, executableQuery.getSQL(), executableQuery
                                .getValueList(), ((HybrisDataSource)dataSource).getID());
            }
            else
            {
                query = limitBuilder.getModifiedStatement();
                query = HintsApplier.filterAndApplyQueryHints(query, hints);
                statement = connection.prepareStatement(query);
                statement = HintsApplier.filterAndApplyPreparedStatementHints(statement, hints);
                handleAutoCommit(statement, hints);
                jdbcVM.fillStatement(statement, limitBuilder.getModifiedStatementValues());
                Stopwatch queryTimer = Stopwatch.createUnstarted();
                if(LOG.isDebugEnabled())
                {
                    queryTimer.start();
                }
                resultSet = statement.executeQuery();
                if(LOG.isDebugEnabled())
                {
                    queryTimer.stop();
                }
                int _start = limitBuilder.getOriginalStart();
                int _count = limitBuilder.getOriginalCount();
                if(limitBuilder.hasDbEngineLimitSupport())
                {
                    _start = 0;
                    _count = -1;
                }
                Stopwatch fetchTimer = Stopwatch.createUnstarted();
                if(LOG.isDebugEnabled())
                {
                    fetchTimer.start();
                }
                JDBCValueMappings.RowFetchResult rowFetchResult = jdbcVM.getQueryResults(resultSet, conversionResult.getConvertedClasses(), _start, _count);
                if(LOG.isDebugEnabled())
                {
                    fetchTimer.stop();
                }
                int totalCount = dontNeedTotal ? rowFetchResult.rows.size() : jdbcVM.getTotalCount(resultSet, rowFetchResult, start, count, connection, executableQuery, limitBuilder
                                .hasDbEngineLimitSupport(), hints);
                if(LOG.isDebugEnabled())
                {
                    debugQuery(conversionResult, executableQuery, limitBuilder, query, queryTimer, fetchTimer, totalCount);
                }
                result = SQLSearchResultFactory.createCacheable(new CacheableResultHolder(rowFetchResult, prefetchSize, prefetchLanguages, conversionResult), totalCount, start, count, limitBuilder
                                .getModifiedStatement(), executableQuery
                                .getValueList(), ((HybrisDataSource)dataSource)
                                .getID());
            }
        }
        catch(IllegalArgumentException e)
        {
            String errorMessageSecondPart = getErrorMessage(executableQuery, e.getMessage(), executableQuery.getSQL());
            throw new FlexibleSearchException(e, "wrong flexible search parameter - " + errorMessageSecondPart, 0);
        }
        catch(SQLException e)
        {
            String errorMessageSecondPart = getErrorMessage(executableQuery, e.getMessage(), query);
            throw new FlexibleSearchException(e, "SQL search error - " + errorMessageSecondPart, 0);
        }
        finally
        {
            enableAutoCommit(statement, hints);
            Utilities.tryToCloseJDBC(connection, statement, resultSet);
        }
        return result;
    }


    private void handleAutoCommit(PreparedStatement statement, List<Hint> hints) throws SQLException
    {
        if(isAutoCommitSwitchNeeded(hints))
        {
            statement.getConnection().setAutoCommit(false);
        }
    }


    private void enableAutoCommit(Statement statement, List<Hint> hints)
    {
        if(statement == null)
        {
            LOG.error("Unable to set auto commit to true. Statement is null.");
            return;
        }
        if(isAutoCommitSwitchNeeded(hints))
        {
            try
            {
                statement.getConnection().setAutoCommit(true);
            }
            catch(SQLException sqlException)
            {
                LOG.error("Unable to set auto commit to true." + sqlException.getMessage());
            }
        }
    }


    private boolean isAutoCommitSwitchNeeded(List<Hint> hints)
    {
        if(Transaction.current().isRunning())
        {
            return false;
        }
        Objects.requireNonNull(PreparedStatementHint.class);
        Objects.requireNonNull(PreparedStatementHint.class);
        return hints.stream().filter(PreparedStatementHint.class::isInstance).map(PreparedStatementHint.class::cast)
                        .anyMatch(PreparedStatementHint::needsAutoCommitDisabled);
    }


    private CacheableResultHolder.ConversionMetaInformation convertResultClasses(List<Class<?>> resultClasses)
    {
        List<Class<?>> conversionResult = new ArrayList<>(resultClasses);
        boolean mustWrapItems = false;
        boolean mustWrapObjects = false;
        for(ListIterator<Class<?>> it = conversionResult.listIterator(); it.hasNext(); )
        {
            Class<?> cl = it.next();
            if(Item.class.isAssignableFrom(cl) || ItemPropertyValue.class.isAssignableFrom(cl) || ItemRemote.class
                            .isAssignableFrom(cl))
            {
                it.set(ItemPropertyValue.class);
                mustWrapItems = true;
            }
            if(ItemPropertyValueCollection.class.isAssignableFrom(cl))
            {
                mustWrapObjects = true;
                continue;
            }
            if(Object.class.equals(cl) || Serializable.class.equals(cl) || Collection.class.isAssignableFrom(cl) || Map.class
                            .isAssignableFrom(cl))
            {
                it.set(Serializable.class);
                mustWrapObjects = true;
            }
        }
        return new CacheableResultHolder.ConversionMetaInformation(conversionResult, mustWrapObjects, mustWrapItems);
    }


    private boolean isNeedTotalAndCountIsZero(int count, boolean dontNeedTotal)
    {
        return (!dontNeedTotal && count == 0 && !Config.isHSQLDBUsed() && !Config.isSQLServerUsed());
    }


    public SearchResult simulate(int start, int count, TranslatedQuery translatedQuery, Map values, PK languagePK, List<Hint> hints)
    {
        SearchResult result;
        TranslatedQuery.ExecutableQuery executableQuery = getExecutableQuery(translatedQuery, values, languagePK);
        LimitStatementBuilder limitBuilder = this.limitStatementFactory.getLimitStatementBuilder(executableQuery, start, count);
        try
        {
            String query = limitBuilder.getModifiedStatement();
            query = HintsApplier.filterAndApplyQueryHints(query, hints);
            LOG.debug("ExecuteQuery flag was set to false, query was not executed: {}", query);
            result = SQLSearchResultFactory.createNonExecutable(start, count, query, limitBuilder.getModifiedStatementValues(), "undefined");
        }
        catch(IllegalArgumentException e)
        {
            String errorMessageSecondPart = getErrorMessage(executableQuery, e.getMessage(), executableQuery.getSQL());
            throw new FlexibleSearchException(e, "wrong flexible search parameter - " + errorMessageSecondPart, 0);
        }
        return result;
    }


    private TranslatedQuery.ExecutableQuery getExecutableQuery(TranslatedQuery translatedQuery, Map values, PK languagePK)
    {
        Map unwrappedValues = (Map)WrapperFactory.unwrap(this.tenant.getCache(), values, true);
        return translatedQuery.expandValues(
                        MapUtils.isEmpty(unwrappedValues) ? unwrappedValues : (Map)new CaseInsensitiveParameterMap(unwrappedValues), languagePK);
    }


    private <T> T withStopwatchMeasurement(Stopwatch stopwatch, SQLAwareSupplier<T> func) throws SQLException
    {
        if(LOG.isDebugEnabled())
        {
            stopwatch.start();
        }
        T result = (T)func.get();
        if(LOG.isDebugEnabled())
        {
            stopwatch.stop();
        }
        return result;
    }


    private String getErrorMessage(TranslatedQuery.ExecutableQuery executableQuery, String message, String sql)
    {
        return Config.getBoolean("flexible.search.exception.show.query.details", false) ? (
                        message + " query = '" + message + "', values = " + sql) :
                        "enable the property 'flexible.search.exception.show.query.details' for more details";
    }
}
