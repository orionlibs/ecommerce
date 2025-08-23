package de.hybris.platform.ticket.dao.impl;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.comments.constants.GeneratedCommentsConstants;
import de.hybris.platform.commerceservices.search.flexiblesearch.PagedFlexibleSearchService;
import de.hybris.platform.commerceservices.search.flexiblesearch.data.SortQueryData;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.ticket.dao.TicketDao;
import de.hybris.platform.ticket.enums.CsInterventionType;
import de.hybris.platform.ticket.enums.CsResolutionType;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;
import de.hybris.platform.ticket.model.CsAgentGroupModel;
import de.hybris.platform.ticket.model.CsTicketModel;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Required;

public class DefaultTicketDao extends AbstractItemDao implements TicketDao
{
    private static final String COMMENT_ITEM_RELATION = "CommentItemRelation";
    private static final String SEARCH_TICKET = "SELECT {t:pk} FROM {CsTicket AS t} ";
    private static final String FIND_TICKETS_BY_CUSTOMER_SITE_QUERY_AND_ORDERBY_MODIFIEDTIME = "SELECT {pk} FROM {CsTicket} WHERE {customer} = ?user AND {baseSite} = ?baseSite";
    private static final String FIND_TICKETS_BY_CUSTOMER_SITE_QUERY_AND_ORDERBY_TICKETID = "SELECT {pk} FROM {CsTicket} WHERE {customer} = ?user AND {baseSite} = ?baseSite";
    private static final String SORT_TICKETS_BY_MODIFIED_DATE = " ORDER BY {modifiedtime} DESC";
    private static final String SORT_TICKETS_BY_TICKETID = " ORDER BY {ticketID} DESC";
    private PagedFlexibleSearchService pagedFlexibleSearchService;


    public List<CsTicketModel> findTicketsByAgentGroupState(EmployeeModel agent, CsAgentGroupModel group, CsTicketState state)
    {
        if(agent == null && group == null && state == null)
        {
            return Collections.emptyList();
        }
        Map<String, Object> params = new HashMap<>();
        StringBuilder query = new StringBuilder("SELECT {t:pk} FROM {CsTicket AS t} ");
        query.append(" WHERE ");
        if(agent != null)
        {
            params.put("agent", agent);
            query.append("{assignedAgent} = ?agent ");
            if(group != null || state != null)
            {
                query.append(" AND ");
            }
        }
        if(group != null)
        {
            params.put("group", group);
            query.append("{assignedGroup} = ?group ");
            if(state != null)
            {
                query.append(" AND ");
            }
        }
        if(state != null)
        {
            params.put("state", state);
            query.append("{state} = ?state ");
        }
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search(query.toString(), params);
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsByStringInTicketOrEvent(String searchString)
    {
        if(searchString == null)
        {
            throw new IllegalArgumentException("searchString must not be null");
        }
        String query = "SELECT DISTINCT {t:pk}, {t:creationtime} FROM {CsTicket AS t LEFT JOIN CommentItemRelation AS e2t ON {t:pk}={e2t:target} LEFT JOIN CsTicketEvent AS e ON {e2t:source}={e:pk} } WHERE {t:ticketID} LIKE ?searchText OR {t:headline} LIKE ?searchText OR {e:text} LIKE ?searchText ORDER BY {t:creationtime} DESC";
        SearchResult<CsTicketModel> resultTickets = getFlexibleSearchService().search(
                        "SELECT DISTINCT {t:pk}, {t:creationtime} FROM {CsTicket AS t LEFT JOIN CommentItemRelation AS e2t ON {t:pk}={e2t:target} LEFT JOIN CsTicketEvent AS e ON {e2t:source}={e:pk} } WHERE {t:ticketID} LIKE ?searchText OR {t:headline} LIKE ?searchText OR {e:text} LIKE ?searchText ORDER BY {t:creationtime} DESC",
                        Collections.singletonMap("searchText", "%" + searchString + "%"));
        return resultTickets.getResult();
    }


    public List<CsTicketModel> findTicketsByStringInTicketOrEventAndStates(String searchString, Set<CsTicketState> states)
    {
        if(searchString == null)
        {
            throw new IllegalArgumentException("searchString must not be null");
        }
        if(states == null || states.isEmpty())
        {
            throw new IllegalArgumentException("states must not be null and not be empty");
        }
        String query = "SELECT DISTINCT {t:pk}, {t:creationtime} FROM {CsTicket AS t LEFT JOIN CommentItemRelation AS e2t ON {t:pk}={e2t:target} LEFT JOIN CsTicketEvent AS e ON {e2t:source}={e:pk} } WHERE ({t:ticketID} LIKE ?searchText OR {t:headline} LIKE ?searchText OR {e:text} LIKE ?searchText) AND {t:state} IN (?states) ORDER BY {t:creationtime} DESC";
        Map<String, Object> params = new HashMap<>(2);
        params.put("searchText", "%" + searchString + "%");
        params.put("states", states);
        SearchResult<CsTicketModel> resultTickets = getFlexibleSearchService().search(
                        "SELECT DISTINCT {t:pk}, {t:creationtime} FROM {CsTicket AS t LEFT JOIN CommentItemRelation AS e2t ON {t:pk}={e2t:target} LEFT JOIN CsTicketEvent AS e ON {e2t:source}={e:pk} } WHERE ({t:ticketID} LIKE ?searchText OR {t:headline} LIKE ?searchText OR {e:text} LIKE ?searchText) AND {t:state} IN (?states) ORDER BY {t:creationtime} DESC",
                        params);
        return resultTickets.getResult();
    }


    public List<CsTicketEventModel> findTicketEventsByTicket(CsTicketModel ticket)
    {
        String query = "SELECT {e:pk}, {c2i:reverseSequenceNumber} FROM {CsTicketEvent AS e JOIN " + GeneratedCommentsConstants.Relations.COMMENTITEMRELATION + " AS c2i ON {c2i:source}={e:pk} }WHERE {c2i:target}=?ticket ORDER BY {c2i:reverseSequenceNumber} ASC";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameter("ticket", ticket);
        SearchResult<CsTicketEventModel> result = getFlexibleSearchService().search(fQuery);
        return result.getResult();
    }


    public List<CsTicketEventModel> findTicketEventsForCustomerByTicket(CsTicketModel ticket)
    {
        String query = "SELECT {ce:pk}, {c2i:reverseSequenceNumber} FROM {CsCustomerEvent AS ce JOIN " + GeneratedCommentsConstants.Relations.COMMENTITEMRELATION
                        + " AS c2i ON {c2i:source}={ce:pk} AND {ce:interventionType} != ?interventionType }WHERE {c2i:target}=?ticket ORDER BY {c2i:reverseSequenceNumber} ASC";
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.addQueryParameter("interventionType", CsInterventionType.PRIVATE);
        fQuery.addQueryParameter("ticket", ticket);
        SearchResult<CsTicketEventModel> result = getFlexibleSearchService().search(fQuery);
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsByAgent(EmployeeModel agent)
    {
        if(agent == null)
        {
            throw new IllegalArgumentException("agent must not be null");
        }
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {assignedAgent} = ?agent ORDER BY {creationtime} DESC",
                        Collections.singletonMap("agent", agent));
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsByAgentGroup(CsAgentGroupModel agentGroup)
    {
        if(agentGroup == null)
        {
            throw new IllegalArgumentException("agentGroup must not be null");
        }
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {assignedGroup} = ?group ORDER BY {creationtime} DESC",
                        Collections.singletonMap("group", agentGroup));
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsByCategory(CsTicketCategory... category)
    {
        if(category == null || category.length == 0)
        {
            throw new IllegalArgumentException("category must not be null or empty");
        }
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {category} IN (?category) ORDER BY {creationtime} DESC",
                        Collections.singletonMap("category", Arrays.asList(category)));
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsByCustomer(UserModel customer)
    {
        if(customer == null)
        {
            throw new IllegalArgumentException("customer must not be null");
        }
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {customer} = ?customer ORDER BY {creationtime} DESC",
                        Collections.singletonMap("customer", customer));
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsById(String ticketId)
    {
        if(ticketId == null)
        {
            throw new IllegalArgumentException("ticketId must not be null");
        }
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {ticketID} = ?id",
                        Collections.singletonMap("id", ticketId));
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsByOrder(OrderModel order)
    {
        if(order == null)
        {
            throw new IllegalArgumentException("order must not be null");
        }
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {order} = ?order ORDER BY {creationtime} DESC",
                        Collections.singletonMap("order", order));
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsByPriority(CsTicketPriority... priority)
    {
        if(priority == null || priority.length == 0)
        {
            throw new IllegalArgumentException("priority must not be null or empty");
        }
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {priority} IN (?priority) ORDER BY {creationtime} DESC",
                        Collections.singletonMap("priority", Arrays.asList(priority)));
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsByResolutionType(CsResolutionType... resolutionType)
    {
        if(resolutionType == null || resolutionType.length == 0)
        {
            throw new IllegalArgumentException("resolutionType must not be null or empty");
        }
        String query = "SELECT {t:pk} FROM {CsTicket AS t JOIN CsTicketResolutionEvent AS r ON {t:resolution}={r:pk}} WHERE {r:resolutionType} IN (?resolutionType) ORDER BY {creationtime} DESC";
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {t:pk} FROM {CsTicket AS t JOIN CsTicketResolutionEvent AS r ON {t:resolution}={r:pk}} WHERE {r:resolutionType} IN (?resolutionType) ORDER BY {creationtime} DESC",
                        Collections.singletonMap("resolutionType", Arrays.asList(resolutionType)));
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsByState(CsTicketState... state)
    {
        if(state == null || state.length == 0)
        {
            throw new IllegalArgumentException("state must not be null or empty");
        }
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {state} IN (?state) ORDER BY {creationtime} DESC",
                        Collections.singletonMap("state", Arrays.asList(state)));
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsWithNullAgent()
    {
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {assignedAgent} IS NULL ORDER BY {creationtime} DESC");
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsWithNullAgentGroup()
    {
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {assignedGroup} IS NULL ORDER BY {creationtime} DESC");
        return result.getResult();
    }


    public List<CsTicketModel> findTicketsByCustomerOrderByModifiedTime(UserModel customer)
    {
        if(customer == null)
        {
            throw new IllegalArgumentException("customer must not be null");
        }
        SearchResult<CsTicketModel> result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket} WHERE {customer} = ?customer ORDER BY {modifiedtime} DESC",
                        Collections.singletonMap("customer", customer));
        return result.getResult();
    }


    public SearchPageData<CsTicketModel> findTicketsByCustomerOrderByModifiedTime(UserModel user, BaseSiteModel baseSite, PageableData pageableData)
    {
        ServicesUtil.validateParameterNotNull(user, "Customer must not be null");
        ServicesUtil.validateParameterNotNull(baseSite, "Store must not be null");
        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("user", user);
        queryParams.put("baseSite", baseSite);
        List<SortQueryData> sortQueries = Arrays.asList(new SortQueryData[] {createSortQueryData("byDate", "SELECT {pk} FROM {CsTicket} WHERE {customer} = ?user AND {baseSite} = ?baseSite ORDER BY {modifiedtime} DESC"),
                        createSortQueryData("byTicketId", "SELECT {pk} FROM {CsTicket} WHERE {customer} = ?user AND {baseSite} = ?baseSite ORDER BY {ticketID} DESC")});
        return getPagedFlexibleSearchService().search(sortQueries, "byDate", queryParams, pageableData);
    }


    protected SortQueryData createSortQueryData(String sortCode, String query)
    {
        SortQueryData sortQueryData = new SortQueryData();
        sortQueryData.setSortCode(sortCode);
        sortQueryData.setQuery(query);
        return sortQueryData;
    }


    public PagedFlexibleSearchService getPagedFlexibleSearchService()
    {
        return this.pagedFlexibleSearchService;
    }


    @Required
    public void setPagedFlexibleSearchService(PagedFlexibleSearchService pagedFlexibleSearchService)
    {
        this.pagedFlexibleSearchService = pagedFlexibleSearchService;
    }
}
