package de.hybris.platform.hac.facade;

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.catalog.CatalogVersionService;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.hac.data.dto.SqlSearchResultData;
import de.hybris.platform.hac.data.samplequery.SampleQuery;
import de.hybris.platform.hac.data.samplequery.impl.AllProductsSampleQuery;
import de.hybris.platform.hac.data.samplequery.impl.CountFromLanguageSampleQuery;
import de.hybris.platform.hac.data.samplequery.impl.CountTypeInstancesSampleQuery;
import de.hybris.platform.hac.data.samplequery.impl.OracleCheckEncodingSampleQuery;
import de.hybris.platform.hac.data.samplequery.impl.ShowMySqlTablesSampleQuery;
import de.hybris.platform.hac.data.samplequery.impl.TotalItemCountSampleQuery;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.internal.ReadOnlyConditionsHelper;
import de.hybris.platform.jdbcwrapper.ConnectionImpl;
import de.hybris.platform.jdbcwrapper.HybrisDataSource;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.TranslationResult;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.GenericValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class HacFlexibleSearchFacade
{
    private static final Logger LOG = Logger.getLogger(HacFlexibleSearchFacade.class);
    public static final Integer DEFAULT_FS_MAX_COUNT = Integer.valueOf(200);
    private FlexibleSearchService flexibleSearchService;
    private CatalogVersionService catalogVersionService;
    private SessionService sessionService;
    private UserService userService;
    private I18NService i18nService;
    private HacDatabaseFacade databaseFacade;
    private final List<SampleQuery> sampleQueries = (List<SampleQuery>)ImmutableList.builder()
                    .add((Object[])new SampleQuery[] {(SampleQuery)new AllProductsSampleQuery(), (SampleQuery)new CountFromLanguageSampleQuery(), (SampleQuery)new CountTypeInstancesSampleQuery(), (SampleQuery)new OracleCheckEncodingSampleQuery(), (SampleQuery)new ShowMySqlTablesSampleQuery(),
                                    (SampleQuery)new TotalItemCountSampleQuery()}).build();
    private final ReadOnlyConditionsHelper readOnlyConditionsHelper = new ReadOnlyConditionsHelper();


    public SqlSearchResultData executeFlexibleSearchQuery(String query, UserModel user, Locale locale, Integer maxCount, boolean commit, HybrisDataSource dataSource)
    {
        long startExecution = System.currentTimeMillis();
        SqlSearchResultData resultData = new SqlSearchResultData();
        boolean isDataSourceReadOnly = isDataSourceReadOnly(dataSource);
        if(StringUtils.isBlank(query))
        {
            return resultData;
        }
        if(commit && isDataSourceReadOnly(dataSource))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        SessionContext ctx = JaloSession.getCurrentSession().createLocalSessionContext();
        try
        {
            Connection con = dataSource.getConnection();
            try
            {
                con.setAutoCommit(commit);
                Integer _maxCount = getPossibleMaxCount(maxCount);
                ctx.setAttribute("ctx.enable.fs.on.read-replica", Boolean.valueOf(isDataSourceReadOnly));
                TranslationResult translationResult = translateQuery(query, user, getPossibleLocale(locale), _maxCount);
                resultData.setExecutionTime(System.currentTimeMillis() - startExecution);
                resultData.setQuery(translationResult.getSQLQuery());
                resultData.setCatalogVersions(getCatalogVersions(user));
                resultData.setParameters(translationResult.getSQLQueryParameters());
                resultData.setDataSourceId(dataSource.getID());
                ResultSet resultSet = getPreparedStatement(con, translationResult.getSQLQuery(), translationResult.getSQLQueryParameters()).executeQuery();
                try
                {
                    resultData.setHeaders(buildHeaders(resultSet.getMetaData()));
                    resultData.setResultList(buildResultList(resultSet, _maxCount));
                    if(resultSet != null)
                    {
                        resultSet.close();
                    }
                }
                catch(Throwable throwable)
                {
                    if(resultSet != null)
                    {
                        try
                        {
                            resultSet.close();
                        }
                        catch(Throwable throwable1)
                        {
                            throwable.addSuppressed(throwable1);
                        }
                    }
                    throw throwable;
                }
                if(!commit)
                {
                    con.rollback();
                }
                if(con != null)
                {
                    con.close();
                }
            }
            catch(Throwable throwable)
            {
                if(con != null)
                {
                    try
                    {
                        con.close();
                    }
                    catch(Throwable throwable1)
                    {
                        throwable.addSuppressed(throwable1);
                    }
                }
                throw throwable;
            }
        }
        catch(Exception e)
        {
            resultData.setException(e);
        }
        finally
        {
            JaloSession.getCurrentSession().removeLocalSessionContext();
        }
        return resultData;
    }


    public SqlSearchResultData executeFlexibleSearchQuery(String query, UserModel user, Locale locale, Integer maxCount, boolean commit)
    {
        return executeFlexibleSearchQuery(query, user, locale, maxCount, commit,
                        Registry.getCurrentTenant().getDataSource());
    }


    public SqlSearchResultData executeFlexibleSearchQuery(String query, UserModel user, Locale locale, Integer maxCount, boolean commit, String dataSourceId)
    {
        return executeFlexibleSearchQuery(query, user, locale, maxCount, commit,
                        getDataSource(dataSourceId));
    }


    public SqlSearchResultData executeRawSql(String query, Integer maxCount, boolean commit)
    {
        return executeRawSql(query, maxCount, commit, Registry.getCurrentTenant().getDataSource().getID());
    }


    public SqlSearchResultData executeRawSql(String query, Integer maxCount, boolean commit, String dataSourceId)
    {
        SqlSearchResultData resultData = new SqlSearchResultData();
        HybrisDataSource dataSource = getDataSource(dataSourceId);
        if(commit && isDataSourceReadOnly(dataSource))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        if(!GenericValidator.isBlankOrNull(query))
        {
            long startExecution = System.currentTimeMillis();
            try
            {
                Connection con = dataSource.getConnection();
                try
                {
                    con.setAutoCommit(commit);
                    long endExecution = executeRawSql(query, maxCount, resultData, con);
                    resultData.setExecutionTime(endExecution - startExecution);
                    resultData.setQuery(query);
                    resultData.setRawExecution(true);
                    if(con instanceof ConnectionImpl)
                    {
                        resultData.setDataSourceId(((ConnectionImpl)con).getDataSource().getID());
                    }
                    if(!commit)
                    {
                        con.rollback();
                    }
                    if(con != null)
                    {
                        con.close();
                    }
                }
                catch(Throwable throwable)
                {
                    if(con != null)
                    {
                        try
                        {
                            con.close();
                        }
                        catch(Throwable throwable1)
                        {
                            throwable.addSuppressed(throwable1);
                        }
                    }
                    throw throwable;
                }
            }
            catch(Exception e)
            {
                LOG.error(e.getMessage());
                resultData.setException(e);
            }
        }
        return resultData;
    }


    private boolean isDataSourceReadOnly(HybrisDataSource dataSource)
    {
        return ((Boolean)this.readOnlyConditionsHelper.getReadOnlyDataSource(Registry.getCurrentTenant())
                        .map(ds -> Boolean.valueOf(ds.getID().equals(dataSource.getID())))
                        .orElse(Boolean.valueOf(false))).booleanValue();
    }


    private long executeRawSql(String query, Integer maxCount, SqlSearchResultData resultData, Connection con) throws Exception
    {
        long endExecution;
        if(isDQL(query))
        {
            endExecution = executeDQL(query, maxCount, resultData, con);
        }
        else if(isDML(query))
        {
            endExecution = executeDML(query, resultData, con);
        }
        else
        {
            throw new IllegalArgumentException("Provided query: " + query + " is not valid DQL or DML language");
        }
        return endExecution;
    }


    private boolean isDQL(String query)
    {
        return query.trim().toUpperCase(LocaleHelper.getPersistenceLocale()).startsWith("SELECT");
    }


    private boolean isDML(String query)
    {
        String upCaseQuery = query.trim().toUpperCase(LocaleHelper.getPersistenceLocale());
        return (upCaseQuery.startsWith("INSERT") || upCaseQuery.startsWith("UPDATE") || upCaseQuery.startsWith("DELETE"));
    }


    private long executeDQL(String query, Integer maxCount, SqlSearchResultData resultData, Connection con) throws SQLException
    {
        long endExecution;
        ResultSet resultSet = getPreparedStatement(con, query, null).executeQuery();
        try
        {
            endExecution = System.currentTimeMillis();
            resultData.setHeaders(buildHeaders(resultSet.getMetaData()));
            resultData.setResultList(buildResultList(resultSet, getPossibleMaxCount(maxCount)));
            if(resultSet != null)
            {
                resultSet.close();
            }
        }
        catch(Throwable throwable)
        {
            if(resultSet != null)
            {
                try
                {
                    resultSet.close();
                }
                catch(Throwable throwable1)
                {
                    throwable.addSuppressed(throwable1);
                }
            }
            throw throwable;
        }
        return endExecution;
    }


    private long executeDML(String query, SqlSearchResultData resultData, Connection con) throws SQLException
    {
        int rowsAffected = getPreparedStatement(con, query, null).executeUpdate();
        long endExecution = System.currentTimeMillis();
        List<String> headers = new ArrayList<>();
        headers.add("Result");
        resultData.setHeaders(headers);
        List<String[]> resultList = (List)new ArrayList<>();
        String[] result = {"" + rowsAffected + " row(s) affected."};
        resultList.add(result);
        resultData.setResultList(resultList);
        return endExecution;
    }


    private PreparedStatement getPreparedStatement(Connection con, String sqlQuery, List<Object> queryParams) throws SQLException
    {
        int resultSetType = Config.isHanaUsed() ? 1003 : 1004;
        PreparedStatement statement = con.prepareStatement(sqlQuery, resultSetType, 1007);
        if(queryParams != null)
        {
            fillStatement(statement, queryParams);
        }
        return statement;
    }


    public List<SampleQuery> getCompatibleSampleQueries()
    {
        return (List<SampleQuery>)this.sampleQueries.stream().filter(SampleQuery::isCompatibleWitCurrentDb).collect(Collectors.toList());
    }


    public String getDefaultDataSource()
    {
        String readOnlyId = Config.getString("flexiblesearch.readOnly.datasource", null);
        return getAllDataSources()
                        .stream()
                        .filter(s -> s.equalsIgnoreCase(readOnlyId))
                        .findFirst()
                        .orElseGet(() -> this.databaseFacade.getDataSource().getID());
    }


    public List<String> getAllDataSources()
    {
        return (List<String>)Stream.<Collection>of(new Collection[] {List.of(Registry.getCurrentTenant().getMasterDataSource().getID()),
                                        Registry.getCurrentTenant().getAllSlaveDataSourceIDs(),
                                        Registry.getCurrentTenant().getAllAlternativeMasterDataSourceIDs()}).flatMap(Collection::stream)
                        .collect(Collectors.toList());
    }


    private List<String> buildHeaders(ResultSetMetaData metaData) throws SQLException
    {
        int colSize = metaData.getColumnCount();
        List<String> headers = new ArrayList<>(colSize);
        for(int i = 0; i < colSize; i++)
        {
            headers.add(metaData.getColumnLabel(i + 1));
        }
        return headers;
    }


    private List<String[]> buildResultList(ResultSet resultSet, Integer maxCount) throws SQLException
    {
        List<String[]> resultList = (List)new ArrayList<>();
        ResultSetMetaData metaData = resultSet.getMetaData();
        int colSize = metaData.getColumnCount();
        while(resultSet.next() && (resultSet.getRow() <= maxCount.intValue() || maxCount.intValue() == -1))
        {
            String[] object = new String[colSize];
            for(int i = 0; i < colSize; i++)
            {
                Object _object = resultSet.getObject(i + 1);
                if(_object != null)
                {
                    object[i] = StringEscapeUtils.escapeHtml(_object.toString());
                }
            }
            resultList.add(object);
        }
        return resultList;
    }


    private void fillStatement(PreparedStatement statement, List<Object> values) throws IllegalArgumentException, SQLException
    {
        JDBCValueMappings.getInstance().fillStatement(statement, values);
    }


    private Collection<CatalogVersionModel> getCatalogVersions(UserModel user)
    {
        Collection<CatalogVersionModel> allReadableCatalogVersions = this.catalogVersionService.getAllReadableCatalogVersions((PrincipalModel)getPossibleUser(user));
        if(allReadableCatalogVersions.isEmpty())
        {
            return this.catalogVersionService.getAllCatalogVersions();
        }
        return allReadableCatalogVersions;
    }


    private HybrisDataSource getDataSource(String dataSourceID)
    {
        return
                        Stream.<HybrisDataSource>concat(Registry.getCurrentTenant().getAllSlaveDataSources().stream(),
                                                        Registry.getCurrentTenant().getAllAlternativeMasterDataSources().stream())
                                        .filter(ds -> ds.getID().equalsIgnoreCase(dataSourceID))
                                        .findFirst()
                                        .orElseGet(() -> Registry.getCurrentTenant().getDataSource());
    }


    private TranslationResult translateQuery(String query, UserModel user, Locale locale, Integer maxCount)
    {
        return (TranslationResult)this.sessionService.executeInLocalView((SessionExecutionBody)new Object(this, user, query, maxCount, locale));
    }


    private Locale getPossibleLocale(Locale locale)
    {
        if(locale == null || GenericValidator.isBlankOrNull(locale.getLanguage()))
        {
            LOG.warn("locale parameter was null or blank, current system locale will be used instead");
            return this.i18nService.getCurrentLocale();
        }
        return locale;
    }


    private UserModel getPossibleUser(UserModel user)
    {
        if(user == null)
        {
            LOG.warn("user parameter was null, current logged user will be used instead");
            return this.userService.getCurrentUser();
        }
        return user;
    }


    private Integer getPossibleMaxCount(Integer maxCount)
    {
        if(maxCount == null)
        {
            LOG.warn("maxCount parameter was null, default: " + DEFAULT_FS_MAX_COUNT + " will be used instead");
            return DEFAULT_FS_MAX_COUNT;
        }
        return maxCount;
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    @Required
    public void setCatalogVersionService(CatalogVersionService catalogVersionService)
    {
        this.catalogVersionService = catalogVersionService;
    }


    @Required
    public void setSessionService(SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    @Required
    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }


    @Required
    public void setDatabaseFacade(HacDatabaseFacade databaseFacade)
    {
        this.databaseFacade = databaseFacade;
    }
}
