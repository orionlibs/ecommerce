package de.hybris.platform.workflow.daos.impl;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.search.impl.SearchResultImpl;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.workflow.WorkflowStatus;
import de.hybris.platform.workflow.daos.WorkflowDao;
import de.hybris.platform.workflow.daos.WorkflowTemplateDao;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collections;
import java.util.Date;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

public class DefaultWorkflowDao extends DefaultGenericDao<WorkflowModel> implements WorkflowDao
{
    private WorkflowTemplateDao workflowTemplateDao;
    private UserService userService;


    public DefaultWorkflowDao(String typecode)
    {
        super(typecode);
    }


    protected void includeDateFilters(Map<String, Object> params, Date dateFrom, Date dateTo)
    {
        if(dateFrom == null || dateTo == null || !dateTo.before(dateFrom))
        {
            if(dateFrom != null)
            {
                params.put("dateFrom", dateFrom);
            }
            if(dateTo != null)
            {
                params.put("dateTo", dateTo);
            }
        }
    }


    protected String includeDateFilters(String query, Date dateFrom, Date dateTo)
    {
        return includeDateFilters(query, null, dateFrom, dateTo);
    }


    protected String includeDateFilters(String query, String alias, Date dateFrom, Date dateTo)
    {
        String finalQuery = query;
        if(dateFrom == null || dateTo == null || !dateTo.before(dateFrom))
        {
            String fieldName = (StringUtils.isNoneEmpty(new CharSequence[] {alias}) ? (alias + ".") : "") + "modifiedtime";
            if(dateFrom != null)
            {
                finalQuery = finalQuery + " AND {" + finalQuery + "} >= ?dateFrom";
            }
            if(dateTo != null)
            {
                finalQuery = finalQuery + " AND {" + finalQuery + "} <= ?dateTo";
            }
        }
        return finalQuery;
    }


    public List<WorkflowModel> findAllAdhocWorkflows(Date adhocDateFrom, Date adhocDateTo)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("user", this.userService.getCurrentUser());
        List<WorkflowTemplateModel> adhocWorkflowTemplate = this.workflowTemplateDao.findAdhocWorkflowTemplates();
        if(adhocWorkflowTemplate.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        params.put("adhocWorkflowTemplate", adhocWorkflowTemplate.get(0));
        String query = "SELECT {pk} FROM {Workflow} WHERE {owner} = ?user AND {job} = ?adhocWorkflowTemplate";
        includeDateFilters((Map)params, adhocDateFrom, adhocDateTo);
        query = includeDateFilters(query, adhocDateFrom, adhocDateTo);
        SearchResult<WorkflowModel> res = getFlexibleSearchService().search(query, params);
        return res.getResult();
    }


    public SearchResult<WorkflowModel> findAllAdhocWorkflows(Date adhocDateFrom, Date adhocDateTo, EnumSet<WorkflowStatus> workflowsStatuses, int startIndex, int pageSize)
    {
        Assert.state((startIndex >= 0), "Start index should be positive number");
        Assert.state((pageSize > 0), "Page size should be greater than 0");
        Map<Object, Object> params = new HashMap<>();
        params.put("user", this.userService.getCurrentUser());
        List<WorkflowTemplateModel> adhocWorkflowTemplate = this.workflowTemplateDao.findAdhocWorkflowTemplates();
        if(adhocWorkflowTemplate.isEmpty())
        {
            return (SearchResult<WorkflowModel>)new SearchResultImpl(Collections.EMPTY_LIST, 0, pageSize, startIndex);
        }
        params.put("adhocWorkflowTemplate", adhocWorkflowTemplate.get(0));
        includeDateFilters((Map)params, adhocDateFrom, adhocDateTo);
        String query = createFlexibleSearchQueryStringForWorkflows(true, workflowsStatuses, adhocWorkflowTemplate, adhocDateFrom, adhocDateTo);
        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query, params);
        flexibleSearchQuery.setCount(pageSize);
        flexibleSearchQuery.setNeedTotal(true);
        flexibleSearchQuery.setStart(startIndex);
        SearchResult<WorkflowModel> res = getFlexibleSearchService().search(flexibleSearchQuery);
        return res;
    }


    public List<WorkflowModel> findAllWorkflows(Date dateFrom, Date dateTo)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("user", this.userService.getCurrentUser());
        String query = "SELECT {pk} FROM {Workflow} WHERE {owner} = ?user";
        List<WorkflowTemplateModel> adhocWorkflowTemplate = this.workflowTemplateDao.findAdhocWorkflowTemplates();
        if(!adhocWorkflowTemplate.isEmpty())
        {
            params.put("adhocWorkflowTemplate", adhocWorkflowTemplate.get(0));
            query = query + " AND {job} != ?adhocWorkflowTemplate";
        }
        includeDateFilters((Map)params, dateFrom, dateTo);
        query = includeDateFilters(query, dateFrom, dateTo);
        SearchResult<WorkflowModel> res = getFlexibleSearchService().search(query, params);
        return res.getResult();
    }


    public SearchResult<WorkflowModel> findAllWorkflows(Date dateFrom, Date dateTo, EnumSet<WorkflowStatus> workflowsStatuses, int startIndex, int pageSize)
    {
        Assert.state((startIndex >= 0), "Start index should be positive number");
        Assert.state((pageSize > 0), "Page size should be greater than 0");
        Map<Object, Object> params = new HashMap<>();
        params.put("user", this.userService.getCurrentUser());
        List<WorkflowTemplateModel> adhocWorkflowTemplate = this.workflowTemplateDao.findAdhocWorkflowTemplates();
        if(!adhocWorkflowTemplate.isEmpty())
        {
            params.put("adhocWorkflowTemplate", adhocWorkflowTemplate.get(0));
        }
        includeDateFilters((Map)params, dateFrom, dateTo);
        boolean isAdhoc = false;
        String query = createFlexibleSearchQueryStringForWorkflows(false, workflowsStatuses, adhocWorkflowTemplate, dateFrom, dateTo);
        FlexibleSearchQuery flexibleSearchQuery = new FlexibleSearchQuery(query, params);
        flexibleSearchQuery.setCount(pageSize);
        flexibleSearchQuery.setNeedTotal(true);
        flexibleSearchQuery.setStart(startIndex);
        SearchResult<WorkflowModel> res = getFlexibleSearchService().search(flexibleSearchQuery);
        return res;
    }


    private String createFlexibleSearchQueryStringForWorkflows(boolean isAdhoc, EnumSet<WorkflowStatus> workflowsStatuses, List<WorkflowTemplateModel> adhocWorkflowTemplate, Date dateFrom, Date dateTo)
    {
        String query = "";
        if(workflowsStatuses == null || workflowsStatuses.isEmpty())
        {
            query = getQueryWhenNoStatusSelected(isAdhoc, !adhocWorkflowTemplate.isEmpty(), dateFrom, dateTo);
        }
        else
        {
            query = "SELECT x.PK FROM (";
            boolean requireUnionPhrase = false;
            for(WorkflowStatus status : workflowsStatuses)
            {
                if(requireUnionPhrase)
                {
                    query = query + " UNION ";
                }
                switch(null.$SwitchMap$de$hybris$platform$workflow$WorkflowStatus[status.ordinal()])
                {
                    case 1:
                        query = query + query;
                        break;
                    case 2:
                        query = query + query;
                        break;
                    case 3:
                        query = query + query;
                        break;
                    case 4:
                        query = query + query;
                        break;
                }
                requireUnionPhrase = true;
            }
            query = query + " ) x order by pk";
        }
        return query;
    }


    private String getQueryWhenNoStatusSelected(boolean isAdhoc, boolean adhocWorkflowTemplateExists, Date dateFrom, Date dateTo)
    {
        String query = "SELECT {pk} FROM {Workflow} WHERE {owner} = ?user";
        if(!isAdhoc && adhocWorkflowTemplateExists)
        {
            query = query + " AND {job} != ?adhocWorkflowTemplate";
        }
        else if(isAdhoc)
        {
            query = query + " AND {job} = ?adhocWorkflowTemplate";
        }
        query = includeDateFilters(query, dateFrom, dateTo);
        return query;
    }


    private String getQueryWhenPlannedStatusSelected(boolean isAdhoc, boolean adhocWorkflowTemplateExists, Date dateFrom, Date dateTo)
    {
        String query = "{{SELECT DISTINCT {w.pk} FROM {Workflow AS w JOIN WorkflowAction AS wa ON {w.pk} = {wa.workflow} JOIN WorkflowActionStatus AS was ON {wa.status} = {was.pk}} WHERE {w.owner}=?user AND {was.code} = 'pending'";
        if(!isAdhoc && adhocWorkflowTemplateExists)
        {
            query = query + " AND {w.job} != ?adhocWorkflowTemplate";
        }
        else if(isAdhoc)
        {
            query = query + " AND {w.job} = ?adhocWorkflowTemplate";
        }
        query = includeDateFilters(query, "w", dateFrom, dateTo);
        query = query + " GROUP BY {w.pk} HAVING COUNT(*) = ";
        query = query + "({{SELECT COUNT(*) FROM {Workflow AS w2 JOIN WorkflowAction AS wa2 ON {w2.pk} = {wa2.workflow}} WHERE {w.pk} = {w2.pk} }}) }}";
        query = query + "UNION {{ " + query + " }}";
        return query;
    }


    private String getQueryWhenRunningStatusSelected(boolean isAdhoc, boolean adhocWorkflowTemplateExists, Date dateFrom, Date dateTo)
    {
        String query = "{{ SELECT DISTINCT {w.pk}FROM {Workflow AS w JOIN WorkflowAction AS wa ON {w.pk} = {wa.workflow} JOIN WorkflowActionStatus AS was ON {wa.status} = {was.pk} } WHERE {w.owner}=?user and {was.code} = 'in_progress'";
        if(!isAdhoc && adhocWorkflowTemplateExists)
        {
            query = query + " AND {w.job} != ?adhocWorkflowTemplate";
        }
        else if(isAdhoc)
        {
            query = query + " AND {w.job} = ?adhocWorkflowTemplate";
        }
        query = includeDateFilters(query, "w", dateFrom, dateTo);
        query = query + " GROUP BY {w.pk}  HAVING COUNT(*) > 0 }}";
        return query;
    }


    private String getQueryWhenFinishedStatusSelected(boolean isAdhoc, boolean adhocWorkflowTemplateExists, Date dateFrom, Date dateTo)
    {
        String query = "{{SELECT DISTINCT {w.pk} FROM {Workflow AS w JOIN WorkflowAction AS wa ON {w.pk} = {wa.workflow} JOIN WorkflowActionStatus AS was ON {wa.status} = {was.pk} } WHERE  {w.owner}=?user AND ({was.code} = 'disabled' OR {was.code} = 'completed')";
        if(!isAdhoc && adhocWorkflowTemplateExists)
        {
            query = query + " AND {w.job} != ?adhocWorkflowTemplate";
        }
        else if(isAdhoc)
        {
            query = query + " AND {w.job} = ?adhocWorkflowTemplate";
        }
        query = includeDateFilters(query, "w", dateFrom, dateTo);
        query = query
                        + " GROUP BY {w.pk} HAVING COUNT(*) =  ({{SELECT COUNT(*)  FROM {Workflow AS w2 JOIN WorkflowAction AS wa2 ON {w2.pk} = {wa2.workflow} } WHERE {w.pk} = {w2.pk}  }}) }} UNION  {{ SELECT DISTINCT {w2.pk} FROM { Workflow AS w2 JOIN WorkflowAction AS wa2 ON {w2.pk} = {wa2.workflow} JOIN  WorkflowActionStatus AS was2 ON {wa2.status} = {was2.pk} }  WHERE  {w2.owner}=?user AND {was2.code} = 'ended_through_end_of_workflow'";
        if(!isAdhoc && adhocWorkflowTemplateExists)
        {
            query = query + " AND {w2.job} != ?adhocWorkflowTemplate";
        }
        else if(isAdhoc)
        {
            query = query + " AND {w2.job} = ?adhocWorkflowTemplate";
        }
        query = includeDateFilters(query, "w2", dateFrom, dateTo);
        query = query + " GROUP BY {w2.pk} HAVING COUNT(*) > 0 }}";
        query = query + " UNION {{ " + query + "}}";
        return query;
    }


    private String getQueryWhenTerminatedStatusSelected(boolean isAdhoc, boolean adhocWorkflowTemplateExists, Date dateFrom, Date dateTo)
    {
        String query = "{{ SELECT DISTINCT {w.pk} FROM {Workflow AS w JOIN WorkflowAction AS wa ON {w.pk} = {wa.workflow} JOIN WorkflowActionStatus AS was ON {wa.status} = {was.pk}}  WHERE {w.owner}=?user AND {was.code} = 'terminated'";
        if(!isAdhoc && adhocWorkflowTemplateExists)
        {
            query = query + " AND {w.job} != ?adhocWorkflowTemplate";
        }
        else if(isAdhoc)
        {
            query = query + " AND {w.job} = ?adhocWorkflowTemplate";
        }
        query = includeDateFilters(query, "w", dateFrom, dateTo);
        query = query + " GROUP BY {w.pk} HAVING COUNT(*) > 0 }}";
        return query;
    }


    private String getQueryWorkflowDoesNotHaveAnyAction(boolean isAdhoc, boolean adhocWorkflowTemplateExists, Date dateFrom, Date dateTo)
    {
        String query = "SELECT DISTINCT {w3.pk} FROM {Workflow AS w3} WHERE {w3.owner}=?user ";
        if(!isAdhoc && adhocWorkflowTemplateExists)
        {
            query = query + " AND {w3.job} != ?adhocWorkflowTemplate";
        }
        else if(isAdhoc)
        {
            query = query + " AND {w3.job} = ?adhocWorkflowTemplate";
        }
        query = includeDateFilters(query, "w3", dateFrom, dateTo);
        query = query + " AND NOT EXISTS ( {{ SELECT * FROM {WorkflowAction as wa3} WHERE {wa3.workflow} = {w3.pk} }}) ";
        return query;
    }


    public List<WorkflowModel> findWorkflowsByUserAndTemplate(UserModel user, WorkflowTemplateModel template)
    {
        Map<String, Object> params = new HashMap<>();
        params.put("owner", user);
        params.put("job", template);
        return find(params);
    }


    public List<WorkflowModel> findWorkflowsByCode(String code)
    {
        return find(Collections.singletonMap("code", code));
    }


    public void setWorkflowTemplateDao(WorkflowTemplateDao workflowTemplateDao)
    {
        this.workflowTemplateDao = workflowTemplateDao;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
