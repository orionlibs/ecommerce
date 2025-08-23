package de.hybris.platform.workflow.daos.impl;

import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.workflow.daos.WorkflowLinksTemplateDao;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultWorkflowLinksTemplateDao implements WorkflowLinksTemplateDao
{
    private FlexibleSearchService flexibleSearchService;


    public List<LinkModel> findWorkflowActionLinkRelationBySource(String source)
    {
        SearchResult<LinkModel> res = this.flexibleSearchService.search("SELECT {pk} from {WorkflowActionTemplateLinkTemplateRelation} where {source}=?desc",
                        Collections.singletonMap("desc", source));
        return res.getResult();
    }


    public Collection<LinkModel> findLinksByDecisionAndAction(AbstractWorkflowDecisionModel decision, AbstractWorkflowActionModel action)
    {
        Collection<LinkModel> results;
        Map<Object, Object> params = new HashMap<>();
        if(decision == null && action == null)
        {
            throw new IllegalArgumentException("Decision and action cannot both be null");
        }
        if(action == null)
        {
            params.put("desc", decision);
            SearchResult<LinkModel> res = this.flexibleSearchService.search("SELECT {pk} from {WorkflowActionTemplateLinkTemplateRelation} where {source}=?desc", params);
            results = res.getResult();
        }
        else if(decision == null)
        {
            params.put("act", action);
            SearchResult<LinkModel> res = this.flexibleSearchService.search("SELECT {pk} from {WorkflowActionTemplateLinkTemplateRelation} where {target}=?act", params);
            results = res.getResult();
        }
        else
        {
            params.put("act", action);
            params.put("desc", decision);
            SearchResult<LinkModel> res = this.flexibleSearchService.search("SELECT {pk} from {WorkflowActionTemplateLinkTemplateRelation} where {source}=?desc AND {target}=?act", params);
            results = res.getResult();
            ServicesUtil.validateIfSingleResult(results, "There is no WorkflowActionTemplateLinkTemplateRelation for source '" + decision
                            .getCode() + "' and target '" + action.getCode() + "'", "There is more than one WorkflowActionTemplateLinkTemplateRelation for source '" + decision
                            .getCode() + "' and target '" + action
                            .getCode() + "'");
        }
        return results;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
