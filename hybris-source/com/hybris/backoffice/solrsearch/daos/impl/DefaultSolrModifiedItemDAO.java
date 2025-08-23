package com.hybris.backoffice.solrsearch.daos.impl;

import com.hybris.backoffice.solrsearch.daos.SolrModifiedItemDAO;
import com.hybris.backoffice.solrsearch.enums.SolrItemModificationType;
import com.hybris.backoffice.solrsearch.model.SolrModifiedItemModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "1808", forRemoval = true)
public class DefaultSolrModifiedItemDAO implements SolrModifiedItemDAO
{
    protected static final String FIND_REMOVED_ITEMS_BY_MODIFICATION_TYPE = String.format("SELECT {pk} from {%s} WHERE  {%s}=?modificationType", new Object[] {"SolrModifiedItem", "modificationType"});
    private FlexibleSearchService flexibleSearchService;


    public Collection<SolrModifiedItemModel> findByModificationType(SolrItemModificationType modificationType)
    {
        FlexibleSearchQuery query = new FlexibleSearchQuery(FIND_REMOVED_ITEMS_BY_MODIFICATION_TYPE);
        query.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {SolrModifiedItemModel.class}));
        query.addQueryParameter("modificationType", modificationType);
        return executeQuery(query);
    }


    protected List<SolrModifiedItemModel> executeQuery(FlexibleSearchQuery query)
    {
        List<SolrModifiedItemModel> items = this.flexibleSearchService.search(query).getResult();
        return CollectionUtils.isNotEmpty(items) ? items : Collections.<SolrModifiedItemModel>emptyList();
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
