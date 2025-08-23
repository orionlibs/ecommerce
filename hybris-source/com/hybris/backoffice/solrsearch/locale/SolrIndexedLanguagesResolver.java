package com.hybris.backoffice.solrsearch.locale;

import com.hybris.backoffice.search.services.BackofficeFacetSearchConfigService;
import com.hybris.backoffice.widgets.fulltextsearch.FullTextSearchStrategy;
import com.hybris.backoffice.widgets.quicktogglelocale.controller.IndexedLanguagesResolver;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.util.Config;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Required;

public class SolrIndexedLanguagesResolver implements IndexedLanguagesResolver
{
    private transient BackofficeFacetSearchConfigService backofficeFacetSearchConfigService;
    private transient FullTextSearchStrategy fullTextSearchStrategy;


    public boolean isIndexed(String isoCode)
    {
        String currentSearchStrategy = Config.getString("backoffice.fulltext.search.strategy", "");
        if(!currentSearchStrategy.equals("solr"))
        {
            return true;
        }
        Collection<ComposedTypeModel> mappedTypes = getBackofficeFacetSearchConfigService().getAllMappedTypes();
        if(CollectionUtils.isEmpty(mappedTypes))
        {
            return true;
        }
        return mappedTypes.stream().allMatch(mappedType -> isLanguageAvailableFor(isoCode, mappedType.getCode()));
    }


    protected boolean isLanguageAvailableFor(String isoCode, String typeCode)
    {
        return getFullTextSearchStrategy().getAvailableLanguages(typeCode).contains(isoCode);
    }


    protected BackofficeFacetSearchConfigService getBackofficeFacetSearchConfigService()
    {
        return this.backofficeFacetSearchConfigService;
    }


    @Required
    public void setBackofficeFacetSearchConfigService(BackofficeFacetSearchConfigService backofficeFacetSearchConfigService)
    {
        this.backofficeFacetSearchConfigService = backofficeFacetSearchConfigService;
    }


    protected FullTextSearchStrategy getFullTextSearchStrategy()
    {
        return this.fullTextSearchStrategy;
    }


    @Required
    public void setFullTextSearchStrategy(FullTextSearchStrategy fullTextSearchStrategy)
    {
        this.fullTextSearchStrategy = fullTextSearchStrategy;
    }
}
