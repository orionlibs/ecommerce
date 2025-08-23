package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.servicelayer.daos.CMSWorkflowActionDao;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.FlexibleSearchUtils;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

public class DefaultCMSWorkflowActionDao extends AbstractCMSItemDao implements CMSWorkflowActionDao
{
    private static final String STATUS_CODES_KEY = "statusCodes";
    private static final String RELATED_PRINCIPALS_KEY = "principals";


    public SearchResult<WorkflowActionModel> findAllActiveWorkflowActionsByStatusAndPrincipals(Set<CronJobStatus> workflowStatuses, Collection<PrincipalModel> currentPrincipals, PageableData pageableData)
    {
        Assert.state((pageableData.getCurrentPage() >= 0), "Start index should be positive number");
        Assert.state((pageableData.getPageSize() > 0), "Page size should be greater than 0");
        Map<String, Object> queryParameters = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        List<String> statusCodes = (List<String>)workflowStatuses.stream().map(CronJobStatus::getCode).collect(Collectors.toList());
        queryBuilder.append("SELECT {wa.pk} ")
                        .append("FROM {WorkflowAction as wa ")
                        .append("JOIN WorkflowActionStatus as was ")
                        .append("  ON {was.pk} = {wa.status} ")
                        .append("JOIN Workflow as wf ")
                        .append("  ON {wf.pk} = {wa.workflow} ")
                        .append("JOIN WorkflowItemAttachment as wia ")
                        .append("  ON {wf.pk} = {wia.workflow} ")
                        .append("JOIN CronJobStatus as st ")
                        .append("  ON {wf.status} = {st.pk} ")
                        .append("JOIN AbstractPage as ap ")
                        .append("  ON {ap.pk} = {wia.item} ").append("} WHERE ");
        if(CollectionUtils.isNotEmpty(statusCodes))
        {
            queryBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{st.code} IN (?statusCodes) ", "statusCodes", "OR", statusCodes, queryParameters))
                            .append(" AND ");
        }
        if(CollectionUtils.isNotEmpty(currentPrincipals))
        {
            queryBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{wa.principalAssigned} IN (?principals) ", "principals", "OR", currentPrincipals, queryParameters))
                            .append(" AND ");
        }
        queryBuilder.append("{was.code} = ?workflowActionStatusCode ");
        queryParameters.put("workflowActionStatusCode", WorkflowActionStatus.IN_PROGRESS.getCode());
        queryBuilder.append("ORDER BY {wa.activated} DESC");
        return search(
                        buildQuery(queryBuilder.toString(), queryParameters, pageableData.getCurrentPage(), pageableData.getPageSize()));
    }
}
