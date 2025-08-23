package de.hybris.platform.solrfacetsearch.suggester;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;

public interface SolrAutoSuggestService
{
    SolrSuggestion getAutoSuggestionsForQuery(LanguageModel paramLanguageModel, SolrIndexedTypeModel paramSolrIndexedTypeModel, String paramString) throws SolrAutoSuggestException;
}
