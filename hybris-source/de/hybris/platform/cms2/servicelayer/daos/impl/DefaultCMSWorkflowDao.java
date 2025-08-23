package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSWorkflowDao;
import de.hybris.platform.cronjob.enums.CronJobStatus;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.FlexibleSearchUtils;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.util.Assert;

public class DefaultCMSWorkflowDao extends AbstractCMSItemDao implements CMSWorkflowDao
{
    private static final String ITEMS_KEY = "items";
    private static final String STATUS_CODES_KEY = "statusCodes";


    public List<WorkflowModel> findAllWorkflowsByAttachedItems(List<? extends CMSItemModel> items, Set<CronJobStatus> statuses)
    {
        Map<String, Object> queryParameters = new HashMap<>();
        String query = buildFindWorkflowsByAttachedItemsQuery(items, statuses, queryParameters);
        SearchResult<WorkflowModel> result = search(query, queryParameters);
        return result.getResult();
    }


    public Optional<WorkflowModel> findWorkflowForCode(String code)
    {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {pk} ")
                        .append("FROM {Workflow as workflow }")
                        .append("WHERE {workflow.code} = ?code ");
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("code", code);
        SearchResult<WorkflowModel> result = search(queryBuilder.toString(), queryParameters);
        if(result.getResult().isEmpty())
        {
            return Optional.empty();
        }
        return Optional.of(result.getResult().get(0));
    }


    public SearchResult<WorkflowModel> findWorkflowsByAttachedItems(List<? extends CMSItemModel> items, Set<CronJobStatus> statuses, PageableData pageableData)
    {
        Assert.state((pageableData.getCurrentPage() >= 0), "Start index should be positive number");
        Assert.state((pageableData.getPageSize() > 0), "Page size should be greater than 0");
        Map<String, Object> queryParameters = new HashMap<>();
        String query = buildFindWorkflowsByAttachedItemsQuery(items, statuses, queryParameters);
        return search(buildQuery(query, queryParameters, pageableData.getCurrentPage(), pageableData.getPageSize()));
    }


    protected String buildFindWorkflowsByAttachedItemsQuery(List<? extends CMSItemModel> items, Set<CronJobStatus> statuses, Map<String, Object> queryParameters)
    {
        StringBuilder queryBuilder = new StringBuilder();
        List<String> statusCodes = (List<String>)statuses.stream().map(CronJobStatus::getCode).collect(Collectors.toList());
        queryBuilder.append("SELECT {pk} ")
                        .append(" FROM {Workflow as wf ")
                        .append(" JOIN CronJobStatus AS st ")
                        .append(" ON {wf.status} = {st.pk} ")
                        .append(" JOIN WorkflowItemAttachment AS wa ")
                        .append(" ON {wf.pk} = {wa.workflow} ")
                        .append(" JOIN CMSItem AS item ")
                        .append(" ON {wa.item} = {item.pk} ")
                        .append("} WHERE ");
        if(CollectionUtils.isNotEmpty(items))
        {
            queryBuilder
                            .append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{item.pk} IN (?items) ", "items", "OR", items, queryParameters))
                            .append(" AND ");
        }
        if(CollectionUtils.isNotEmpty(statusCodes))
        {
            queryBuilder.append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{st.code} IN (?statusCodes) ", "statusCodes", "OR", statusCodes, queryParameters));
        }
        return queryBuilder.toString();
    }
}
