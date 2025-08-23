package de.hybris.platform.processengine.impl;

import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.workflow.jalo.WorkflowTemplate;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.HashMap;
import java.util.Map;

public class DefaultWorkflowIntegrationDao implements WorkflowIntegrationDao
{
    private FlexibleSearchService flexibleSearchService;


    public WorkflowTemplateModel getWorkflowTemplateModelById(String id)
    {
        String code = TypeManager.getInstance().getComposedType(WorkflowTemplate.class).getCode();
        Map<String, Object> params = new HashMap<>();
        params.put("code", id);
        String SQL = "SELECT {" + WorkflowTemplate.PK + "} FROM {" + code + "} WHERE {code}=?code";
        FlexibleSearchQuery query = new FlexibleSearchQuery(SQL, params);
        SearchResult<WorkflowTemplateModel> res = this.flexibleSearchService.search(query);
        if(res.getResult().size() == 1)
        {
            return res.getResult().get(0);
        }
        return null;
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public FlexibleSearchService getFlexibleSearchService()
    {
        return this.flexibleSearchService;
    }
}
