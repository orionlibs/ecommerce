package de.hybris.platform.workflow.dao.impl;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.enumeration.EnumerationManager;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.user.Employee;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.FlexibleSearchUtils;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import de.hybris.platform.workflow.dao.WorkflowDAO;
import de.hybris.platform.workflow.jalo.Workflow;
import de.hybris.platform.workflow.jalo.WorkflowTemplate;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class DefaultWorkflowDAO extends AbstractItemDao implements WorkflowDAO
{
    private static final Logger LOG = Logger.getLogger(DefaultWorkflowDAO.class);
    private static String ADHOC_TEMPLATE_NAME = Config.getParameter("workflow.adhoctemplate.name");
    private UserService userService;


    public List<WorkflowModel> getAllWorkflows(Date dateFrom, Date dateTo)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("user", this.userService.getCurrentUser());
        String query = "SELECT {" + Item.PK + "} FROM {" + GeneratedWorkflowConstants.TC.WORKFLOW + "} WHERE {" + Workflow.OWNER + "} = ?user";
        WorkflowTemplateModel adhocWorkflowTemplate = getAdhocWorkflowTemplate();
        if(adhocWorkflowTemplate != null)
        {
            params.put("adhocWorkflowTemplate", adhocWorkflowTemplate);
            query = query + " AND {job} != ?adhocWorkflowTemplate";
        }
        if(dateFrom != null && dateTo != null && dateFrom.compareTo(dateTo) <= 0)
        {
            query = query + " AND {" + query + "} >= ?dateFrom AND {" + Workflow.MODIFIED_TIME + "} <= ?dateTo";
            params.put("dateFrom", dateFrom);
            params.put("dateTo", dateTo);
        }
        SearchResult<WorkflowModel> res = getFlexibleSearchService().search(query, params);
        return res.getResult();
    }


    public List<WorkflowModel> getAllAdhocWorkflows(Date adhocDateFrom, Date adhocDateTo)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("user", this.userService.getCurrentUser());
        WorkflowTemplateModel adhocWorkflowTemplate = getAdhocWorkflowTemplate();
        if(adhocWorkflowTemplate == null)
        {
            return Collections.EMPTY_LIST;
        }
        params.put("adhocWorkflowTemplate", adhocWorkflowTemplate);
        String query = "SELECT {" + Item.PK + "} FROM {" + GeneratedWorkflowConstants.TC.WORKFLOW + "} WHERE {" + Workflow.OWNER + "} = ?user AND {job} = ?adhocWorkflowTemplate";
        if(adhocDateFrom != null && adhocDateTo != null && adhocDateFrom.compareTo(adhocDateTo) <= 0)
        {
            query = query + " AND {" + query + "} >= ?dateFrom AND {" + Workflow.MODIFIED_TIME + "} <= ?dateTo";
            params.put("dateFrom", adhocDateFrom);
            params.put("dateTo", adhocDateTo);
        }
        SearchResult<WorkflowModel> res = getFlexibleSearchService().search(query, params);
        return res.getResult();
    }


    public List<Object> getUsersWorkflowTemplates()
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("user", this.userService.getCurrentUser());
        String query = "SELECT {" + Item.PK + "} FROM {" + GeneratedWorkflowConstants.TC.WORKFLOWTEMPLATE + "} WHERE {" + WorkflowTemplate.OWNER + "} = ?user";
        SearchResult<Object> res = getFlexibleSearchService().search(query, params);
        return res.getResult();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public UserService getUserService()
    {
        return this.userService;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public WorkflowTemplateModel getAdhocWorkflowTemplate()
    {
        List<Object> ret = getFlexibleSearchService().search("SELECT {pk} FROM {" + GeneratedWorkflowConstants.TC.WORKFLOWTEMPLATE + " as wt} WHERE {wt.code} LIKE '" + getAdhocTemplateName() + "'").getResult();
        if(ret.isEmpty())
        {
            LOG.error("No adhoc workflow template with name: '" + getAdhocTemplateName() + "' was found. Check project.properties for 'workflow.adhoctemplate.name' property.");
            return null;
        }
        return (WorkflowTemplateModel)ret.get(0);
    }


    public Employee getAdhocWorkflowTemplateDummyOwner()
    {
        return JaloSession.getCurrentSession().getUserManager().getAdminEmployee();
    }


    private String getAdhocTemplateName()
    {
        return (ADHOC_TEMPLATE_NAME == null) ? "" : ADHOC_TEMPLATE_NAME;
    }


    public List<WorkflowActionModel> getStartWorkflowActions(WorkflowModel wfModel)
    {
        EnumerationManager enumManager = JaloSession.getCurrentSession().getEnumerationManager();
        EnumerationValue startType = enumManager.getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONTYPE, GeneratedWorkflowConstants.Enumerations.WorkflowActionType.START);
        return getWorkflowActionsByType(startType, wfModel);
    }


    public List<WorkflowActionModel> getNormalWorkflowActions(WorkflowModel wfModel)
    {
        EnumerationManager enumManager = JaloSession.getCurrentSession().getEnumerationManager();
        EnumerationValue endType = enumManager.getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONTYPE, GeneratedWorkflowConstants.Enumerations.WorkflowActionType.NORMAL);
        return getWorkflowActionsByType(endType, wfModel);
    }


    public List<WorkflowActionModel> getEndWorkflowActions(WorkflowModel wfModel)
    {
        EnumerationManager enumManager = JaloSession.getCurrentSession().getEnumerationManager();
        EnumerationValue endType = enumManager.getEnumerationValue(GeneratedWorkflowConstants.TC.WORKFLOWACTIONTYPE, GeneratedWorkflowConstants.Enumerations.WorkflowActionType.END);
        return getWorkflowActionsByType(endType, wfModel);
    }


    public List<WorkflowActionModel> getWorkflowActionsByType(EnumerationValue type, WorkflowModel wfModel)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("actionType", type);
        params.put("workflow", wfModel);
        String query = "SELECT {" + Item.PK + "} FROM {WorkflowAction} WHERE {actionType} = ?actionType AND {workflow} = ?workflow";
        SearchResult<WorkflowActionModel> res = this.flexibleSearchService.search(query, params);
        return res.getResult();
    }


    public List<WorkflowTemplateModel> getWorkflowTemplatesVisibleForUser(PrincipalModel principalModel)
    {
        StringBuilder query = new StringBuilder();
        query.append("SELECT {t:").append(Item.PK).append("} ");
        query.append("FROM {").append(GeneratedWorkflowConstants.TC.WORKFLOWTEMPLATE).append(" AS t ");
        query.append("JOIN ").append(GeneratedWorkflowConstants.Relations.WORKFLOWTEMPLATE2PRINCIPALRELATION).append(" AS r ");
        query.append("ON {r:").append("source").append("}={t:").append(Item.PK).append("} } ");
        Collection<PrincipalModel> principals = new ArrayList<>();
        principals.addAll(principalModel.getAllGroups());
        principals.add(principalModel);
        Map<Object, Object> params = new HashMap<>();
        params.put("principals", principals);
        String inPart = FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{r:target} IN (?principals)", "principals", "AND", principals, params);
        query.append("WHERE ").append(inPart);
        SearchResult<WorkflowTemplateModel> res = getFlexibleSearchService().search(query.toString(), params);
        return res.getResult();
    }
}
