package de.hybris.platform.solrfacetsearch.suggester.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MockSolrAutoCompleteService implements SolrAutoSuggestService
{
    public SolrSuggestion getAutoSuggestionsForQuery(LanguageModel language, SolrIndexedTypeModel indexedType, String queryInput)
    {
        Map<String, Collection<String>> suggestionMap = new HashMap<>();
        suggestionMap.put("alfa", Arrays.asList(new String[] {"", "delta"}));
        Collection<String> collations = Arrays.asList(new String[] {"alfa beta", "alfa gamma", "alfa delta"});
        return new SolrSuggestion(suggestionMap, collations);
    }
}
