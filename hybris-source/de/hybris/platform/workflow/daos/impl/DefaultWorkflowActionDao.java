package de.hybris.platform.workflow.daos.impl;

import de.hybris.platform.constants.GeneratedCoreConstants;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import de.hybris.platform.workflow.daos.WorkflowActionDao;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultWorkflowActionDao extends DefaultGenericDao<WorkflowActionModel> implements WorkflowActionDao
{
    public DefaultWorkflowActionDao(String typecode)
    {
        super(typecode);
    }


    public List<WorkflowActionModel> findEndWorkflowActions(WorkflowModel wfModel)
    {
        return findWorkflowActionsByType(WorkflowActionType.END, wfModel);
    }


    public List<WorkflowActionModel> findNormalWorkflowActions(WorkflowModel wfModel)
    {
        return findWorkflowActionsByType(WorkflowActionType.NORMAL, wfModel);
    }


    public List<WorkflowActionModel> findStartWorkflowActions(WorkflowModel wfModel)
    {
        return findWorkflowActionsByType(WorkflowActionType.START, wfModel);
    }


    public List<WorkflowActionModel> findWorkflowActionsByType(WorkflowActionType type, WorkflowModel wfModel)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("actionType", type);
        params.put("workflow", wfModel);
        return find(params);
    }


    public List<WorkflowActionModel> findWorkflowActionsByStatusAndAttachmentType(List<ComposedTypeModel> attachmentTypes, Collection<WorkflowActionStatus> actionStatuses)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("status", actionStatuses);
        StringBuilder typeCondition = new StringBuilder();
        if(!attachmentTypes.isEmpty())
        {
            params.put("types", attachmentTypes);
            typeCondition.append(" AND {attachment:typeOfItem} IN (?types) ");
        }
        SearchResult<WorkflowActionModel> res = getFlexibleSearchService().search(
                        "SELECT DISTINCT {actions:pk}, {actions:creationtime} FROM {WorkflowAction as actions JOIN " + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION + "* AS rel ON {rel:" + GeneratedCoreConstants.Attributes.Link.SOURCE
                                        + "}={actions:pk} JOIN WorkflowItemAttachment AS attachment ON {rel:" + GeneratedCoreConstants.Attributes.Link.TARGET
                                        + "}={attachment:pk} } WHERE {actions:status} IN (?status) AND ({actions:principalAssigned}=?session.user OR {actions:principalAssigned} IN (?session.user.allgroups)) AND {rel:" + GeneratedCoreConstants.Attributes.Link.QUALIFIER + "} = '"
                                        + GeneratedWorkflowConstants.Relations.WORKFLOWACTIONITEMATTACHMENTRELATION + "' AND {rel:" + GeneratedCoreConstants.Attributes.Link.LANGUAGE + "} IS NULL " + typeCondition + "ORDER BY {actions:creationtime} ASC", params);
        return res.getResult();
    }
}
