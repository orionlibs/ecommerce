package de.hybris.platform.ticket.event.dao.impl;

import de.hybris.platform.commerceservices.search.dao.impl.DefaultPagedGenericDao;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.time.TimeService;
import de.hybris.platform.ticket.enums.EventType;
import de.hybris.platform.ticket.event.dao.CustomerSupportEventDao;
import de.hybris.platform.ticketsystem.events.model.SessionEventModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;

public class DefaultCustomerSupportEventDao extends DefaultPagedGenericDao<SessionEventModel> implements CustomerSupportEventDao
{
    private static final String CURRENTDATE = "currentDate";
    private static final String LOGINDISABLED_PARAMETER = "loginDisabled";
    private static final String SEARCH_ALL = "SELECT {c:pk} FROM {SessionEvent AS c}";
    private static final String SEARCH_STARTED_SESSIONS = "SELECT {c:pk} FROM {SessionStartEvent AS c}";
    private static final String SEARCH_CUSTOMERS_BY_STARTED_SESSIONS = "SELECT {c:customer},MAX({c:creationtime}) AS maxCreationTime FROM {SessionStartEvent AS c}";
    private static final String SEARCH_CUSTOMERS_BY_STARTED_SESSIONS_FILTER_DISABLED_ACC = "SELECT  {c:customer} , MAX({c:creationtime}) AS maxCreationTime FROM {SessionStartEvent AS c JOIN Customer as cu ON {c:customer} = {cu:pk}} ";
    private static final String SORT_SESSIONS_BY_DATE_ASC_CUSTOMER_GROUPING = " GROUP BY {c:customer} ORDER BY MAX({creationtime}) ASC";
    private static final String SORT_SESSIONS_BY_DATE_DESC_CUSTOMER_GROUPING = " GROUP BY {c:customer} ORDER BY MAX({creationtime}) DESC";
    private static final String SORT_SESSIONS_BY_DATE_ASC = " ORDER BY {creationtime} ASC";
    private static final String SORT_SESSIONS_BY_DATE_DESC = " ORDER BY {creationtime} DESC";
    private static final String START_DATE_PARAMETER = "startDate";
    private static final String END_DATE_PARAMETER = "endDate";
    private static final String BEFORE_DATE_PARAMETER = "beforeDate";
    private static final String AGENT_PARAMETER = "agent";
    private static final String SORT_BY_DATE_ASC = "byDateAsc";
    private static final String SORT_BY_DATE_DESC = "byDateDesc";
    private static final String WHERE_CLAUSE_KEY = "whereClause";
    private static final String WHERE_CLAUSE_PARAMETERS_KEY = "queryParam";
    private static final int EVENT_PAGE_SIZE = 500;
    private TimeService timeService;


    public DefaultCustomerSupportEventDao(String typeCode)
    {
        super(typeCode);
    }


    public SearchPageData<SessionEventModel> findAllEventsByAgent(EmployeeModel agent, EventType eventType, Date startDate, Date endDate, PageableData pageableData, int limit)
    {
        String queryToExecute;
        Map<String, Object> preparedResultsMap = validateAndPrepareWhereClause(agent, startDate, endDate, limit, true);
        if(eventType.equals(EventType.START_SESSION_EVENT))
        {
            queryToExecute = "SELECT {c:pk} FROM {SessionStartEvent AS c}";
        }
        else
        {
            queryToExecute = "SELECT {c:pk} FROM {SessionEvent AS c}";
        }
        List<SortQueryData> sortQueries = Arrays.asList(new SortQueryData[] {createSortQueryData("byDateAsc", createQuery(new String[] {queryToExecute, preparedResultsMap
                        .get("whereClause").toString(), " ORDER BY {creationtime} ASC"})), createSortQueryData("byDateDesc", createQuery(new String[] {queryToExecute, preparedResultsMap.get("whereClause").toString(), " ORDER BY {creationtime} DESC"}))});
        return getPagedFlexibleSearchService().search(sortQueries, "byDateDesc", (Map)preparedResultsMap
                        .get("queryParam"), pageableData);
    }


    @Deprecated(since = "6.7", forRemoval = true)
    public <T extends de.hybris.platform.core.model.user.CustomerModel> SearchPageData<T> findAllCustomersByEventsAndAgent(EmployeeModel agent, EventType eventType, Date startDate, Date endDate, PageableData pageableData, int limit)
    {
        return findAllCustomersByEventsAndAgent(agent, eventType, startDate, endDate, pageableData, limit, true);
    }


    public <T extends de.hybris.platform.core.model.user.CustomerModel> SearchPageData<T> findAllCustomersByEventsAndAgent(EmployeeModel agent, EventType eventType, Date startDate, Date endDate, PageableData pageableData, int limit, boolean includeDisabledAccounts)
    {
        Map<String, Object> preparedResultsMap = validateAndPrepareWhereClause(agent, startDate, endDate, limit, includeDisabledAccounts);
        if(eventType.equals(EventType.START_SESSION_EVENT))
        {
            String query;
            if(includeDisabledAccounts)
            {
                query = "SELECT {c:customer},MAX({c:creationtime}) AS maxCreationTime FROM {SessionStartEvent AS c}";
            }
            else
            {
                query = "SELECT  {c:customer} , MAX({c:creationtime}) AS maxCreationTime FROM {SessionStartEvent AS c JOIN Customer as cu ON {c:customer} = {cu:pk}} ";
            }
            List<SortQueryData> sortQueries = Arrays.asList(new SortQueryData[] {createSortQueryData("byDateAsc",
                            createQuery(new String[] {query, preparedResultsMap.get("whereClause").toString(), " GROUP BY {c:customer} ORDER BY MAX({creationtime}) ASC"})), createSortQueryData("byDateDesc",
                            createQuery(new String[] {query, preparedResultsMap.get("whereClause").toString(), " GROUP BY {c:customer} ORDER BY MAX({creationtime}) DESC"}))});
            return getPagedFlexibleSearchService().search(sortQueries, "byDateDesc", (Map)preparedResultsMap
                            .get("queryParam"), pageableData);
        }
        return new SearchPageData();
    }


    protected Map<String, Object> validateAndPrepareWhereClause(EmployeeModel agent, Date startDate, Date endDate, int limit, boolean includeDisabledAcc)
    {
        Map<String, Object> preparedResultsMap = new HashMap<>();
        List<String> whereClause = new ArrayList<>();
        Map<String, Object> queryParameters = new HashMap<>();
        if(limit < 1)
        {
            throw new IllegalArgumentException("Query limit shouldn't be less than 1");
        }
        if(null != agent)
        {
            whereClause.add("{agent}=?agent");
            queryParameters.put("agent", agent);
        }
        if(null != startDate && null != endDate && startDate.before(endDate))
        {
            whereClause.add("{creationtime} between ?startDate AND ?endDate");
            queryParameters.put("startDate", startDate);
            queryParameters.put("endDate", endDate);
        }
        if(!includeDisabledAcc)
        {
            whereClause.add(" {cu:loginDisabled} = ?loginDisabled AND ({cu:deactivationDate} IS NULL OR {cu:deactivationDate} > ?currentDate) ");
            queryParameters.put("currentDate", getTimeService().getCurrentTime());
            queryParameters.put("loginDisabled", Boolean.FALSE);
        }
        String whereClauseStr = "";
        if(!whereClause.isEmpty())
        {
            whereClauseStr = " WHERE " + StringUtils.join(whereClause, " AND ");
        }
        preparedResultsMap.put("whereClause", whereClauseStr);
        preparedResultsMap.put("queryParam", queryParameters);
        return preparedResultsMap;
    }


    public List<SessionEventModel> findAllEventsBeforeDate(EventType eventType, Date beforeDate)
    {
        String whereClauseStr = " WHERE {creationtime} < ?beforeDate";
        FlexibleSearchQuery query = new FlexibleSearchQuery(createQuery(new String[] {(null != eventType && eventType.equals(EventType.START_SESSION_EVENT)) ? "SELECT {c:pk} FROM {SessionStartEvent AS c}" : "SELECT {c:pk} FROM {SessionEvent AS c}", " WHERE {creationtime} < ?beforeDate"}, ),
                        Collections.singletonMap("beforeDate", beforeDate));
        PageableData pageableData = new PageableData();
        pageableData.setPageSize(500);
        SearchPageData<SessionEventModel> searchPageData = getPagedFlexibleSearchService().search(query, pageableData);
        return searchPageData.getResults();
    }


    protected String createQuery(String... queryClauses)
    {
        StringBuilder queryBuilder = new StringBuilder();
        for(String queryClause : queryClauses)
        {
            queryBuilder.append(queryClause);
        }
        return queryBuilder.toString();
    }


    protected TimeService getTimeService()
    {
        return this.timeService;
    }


    @Required
    public void setTimeService(TimeService timeService)
    {
        this.timeService = timeService;
    }
}
