package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.solrfacetsearch.config.IndexOperation;
import de.hybris.platform.solrfacetsearch.config.IndexedTypeFlexibleSearchQuery;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexerQueryModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexerQueryParameterModel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.CollectionUtils;

public class DefaultIndexedTypeFlexibleSearchQueryPopulator implements Populator<SolrIndexerQueryModel, IndexedTypeFlexibleSearchQuery>
{
    private static final String ANONYMOUS = "anonymous";


    public void populate(SolrIndexerQueryModel source, IndexedTypeFlexibleSearchQuery target)
    {
        target.setQuery(source.getQuery());
        UserModel user = source.getUser();
        target.setUserId((user == null) ? "anonymous" : user.getUid());
        target.setInjectCurrentDate(source.isInjectCurrentDate());
        target.setInjectCurrentTime(source.isInjectCurrentTime());
        target.setInjectLastIndexTime(source.isInjectLastIndexTime());
        target.setParameters(initializeFSQParameters(source.getSolrIndexerQueryParameters()));
        target.setParameterProviderId(source.getParameterProvider());
        if(source.getType() != null)
        {
            target.setType(IndexOperation.valueOf(source.getType().toString()));
        }
    }


    protected Map<String, Object> initializeFSQParameters(List<SolrIndexerQueryParameterModel> list)
    {
        HashMap<String, Object> parameters = new HashMap<>();
        if(CollectionUtils.isNotEmpty(list))
        {
            for(SolrIndexerQueryParameterModel parameterModel : list)
            {
                parameters.put(parameterModel.getName(), parameterModel.getValue());
            }
        }
        return parameters;
    }
}
