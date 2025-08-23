package de.hybris.platform.solrfacetsearch.converters.populator;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.solrfacetsearch.config.SearchConfig;
import de.hybris.platform.solrfacetsearch.model.config.SolrSearchConfigModel;
import java.util.Collections;

public class DefaultSearchConfigPopulator implements Populator<SolrSearchConfigModel, SearchConfig>
{
    public void populate(SolrSearchConfigModel source, SearchConfig target)
    {
        target.setDefaultSortOrder(
                        (source.getDefaultSortOrder() == null) ? Collections.emptyList() : source.getDefaultSortOrder());
        target.setPageSize((source.getPageSize() != null) ? source.getPageSize().intValue() : 0);
        target.setAllFacetValuesInResponse(source.isAllFacetValuesInResponse());
        target.setRestrictFieldsInResponse(source.isRestrictFieldsInResponse());
        target.setEnableHighlighting(source.isEnableHighlighting());
        target.setLegacyMode(source.isLegacyMode());
    }
}
