package de.hybris.platform.cms2.servicelayer.daos.impl;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSWorkflowTemplateDao;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.internal.dao.AbstractItemDao;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.FlexibleSearchUtils;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

public class DefaultCMSWorkflowTemplateDao extends AbstractItemDao implements CMSWorkflowTemplateDao
{
    private static final String PRINCIPALS = "principals";


    public List<WorkflowTemplateModel> getVisibleWorkflowTemplatesForCatalogVersion(CatalogVersionModel catalogVersion, PrincipalModel principal)
    {
        StringBuilder queryBuilder = new StringBuilder();
        Map<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("catalogVersion", catalogVersion);
        queryBuilder.append("SELECT DISTINCT {wt.").append("pk").append("} ")
                        .append("FROM {").append("WorkflowTemplate").append(" as wt ")
                        .append("JOIN ").append("WorkflowTemplateForCatalogVersion").append(" AS cvrel ")
                        .append("  ON {wt.").append("pk").append("} = {cvrel.").append("target").append("}")
                        .append("JOIN ").append("WorkflowTemplate2PrincipalRelation").append(" AS prel ")
                        .append("  ON {prel. ").append("source").append("} = {wt.").append("pk").append("} } ")
                        .append("WHERE {cvrel.").append("source").append("} = ?catalogVersion ");
        List<PrincipalModel> principals = new ArrayList<>();
        principals.addAll(principal.getAllGroups());
        principals.add(principal);
        if(CollectionUtils.isNotEmpty(principals))
        {
            queryParameters.put("principals", principals);
            queryBuilder.append("AND ")
                            .append(FlexibleSearchUtils.buildOracleCompatibleCollectionStatement("{prel.target} IN (?principals)", "principals", "AND", principals, queryParameters));
        }
        SearchResult<WorkflowTemplateModel> result = search(queryBuilder.toString(), queryParameters);
        return result.getResult();
    }
}
