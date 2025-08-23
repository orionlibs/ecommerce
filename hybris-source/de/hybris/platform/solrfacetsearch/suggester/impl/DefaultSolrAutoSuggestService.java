package de.hybris.platform.solrfacetsearch.suggester.impl;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.common.AbstractYSolrService;
import de.hybris.platform.solrfacetsearch.config.FacetSearchConfig;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.indexer.SolrIndexedTypeCodeResolver;
import de.hybris.platform.solrfacetsearch.model.SolrIndexModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedTypeModel;
import de.hybris.platform.solrfacetsearch.solr.Index;
import de.hybris.platform.solrfacetsearch.solr.SolrIndexService;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProvider;
import de.hybris.platform.solrfacetsearch.solr.SolrSearchProviderFactory;
import de.hybris.platform.solrfacetsearch.solr.exceptions.SolrServiceException;
import de.hybris.platform.solrfacetsearch.suggester.SolrAutoSuggestService;
import de.hybris.platform.solrfacetsearch.suggester.SolrSuggestion;
import de.hybris.platform.solrfacetsearch.suggester.exceptions.SolrAutoSuggestException;
import java.io.Closeable;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.client.solrj.response.SuggesterResponse;
import org.apache.solr.common.params.SolrParams;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSolrAutoSuggestService extends AbstractYSolrService implements SolrAutoSuggestService
{
    private static final Logger LOG = Logger.getLogger(DefaultSolrAutoSuggestService.class);
    private static final String ENCODING = "UTF-8";
    public static final String SUGGESTER_QUERY_TYPE = "/suggest";
    public static final String SUGGEST_QUERY = "suggest.q";
    public static final String SUGGEST_DICTIONARY = "suggest.dictionary";
    public static final String SPELLCHECK_QUERY = "spellcheck.q";
    public static final String SPELLCHECK_DICTIONARY = "spellcheck.dictionary";
    private SolrIndexService solrIndexService;
    private SolrSearchProviderFactory solrSearchProviderFactory;
    private SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver;


    public SolrSuggestion getAutoSuggestionsForQuery(LanguageModel language, SolrIndexedTypeModel solrIndexedType, String queryInput) throws SolrAutoSuggestException
    {
        SolrClient solrClient = null;
        if(StringUtils.isNotBlank(queryInput))
        {
            try
            {
                String dictionary = language.getIsocode();
                String configName = solrIndexedType.getSolrFacetSearchConfig().getName();
                FacetSearchConfig facetSearchConfig = this.facetSearchConfigService.getConfiguration(configName);
                IndexedType indexedType = (IndexedType)facetSearchConfig.getIndexConfig().getIndexedTypes().get(this.solrIndexedTypeCodeResolver.resolveIndexedTypeCode(solrIndexedType));
                SolrSearchProvider solrSearchProvider = this.solrSearchProviderFactory.getSearchProvider(facetSearchConfig, indexedType);
                SolrIndexModel solrIndex = this.solrIndexService.getActiveIndex(facetSearchConfig.getName(), indexedType
                                .getIdentifier());
                Index index = solrSearchProvider.resolveIndex(facetSearchConfig, indexedType, solrIndex.getQualifier());
                solrClient = solrSearchProvider.getClient(index);
                SolrQuery query = new SolrQuery();
                query.setQuery(queryInput);
                query.setRequestHandler("/suggest");
                query.set("suggest.q", new String[] {queryInput});
                query.set("suggest.dictionary", new String[] {dictionary});
                query.set("spellcheck.q", new String[] {queryInput});
                query.set("spellcheck.dictionary", new String[] {dictionary});
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Solr Suggest Query: \n" + URLDecoder.decode(query.toString(), "UTF-8"));
                }
                QueryResponse response = solrClient.query(index.getName(), (SolrParams)query);
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("Solr Suggest Response: \n" + response);
                }
                SuggesterResponse suggesterResponse = response.getSuggesterResponse();
                if(suggesterResponse != null)
                {
                    return createResultFromSuggesterResponse(suggesterResponse, dictionary);
                }
                SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();
                if(spellCheckResponse != null)
                {
                    return createResultFromSpellCheckResponse(spellCheckResponse);
                }
            }
            catch(SolrServiceException | de.hybris.platform.solrfacetsearch.config.exceptions.FacetConfigServiceException | org.apache.solr.client.solrj.SolrServerException | java.io.IOException e)
            {
                throw new SolrAutoSuggestException("Error issuing suggestion query", e);
            }
            finally
            {
                IOUtils.closeQuietly((Closeable)solrClient);
            }
        }
        return new SolrSuggestion(new HashMap<>(), new ArrayList());
    }


    protected boolean checkIfIndexPropertyQualifies(SolrIndexedPropertyModel indexedProperty)
    {
        return (Boolean.TRUE.equals(indexedProperty.getUseForAutocomplete()) && !indexedProperty.isCurrency());
    }


    protected SolrSuggestion createResultFromSuggesterResponse(SuggesterResponse suggesterResponse, String dictionary)
    {
        Map<String, Collection<String>> resultSuggestions = new HashMap<>();
        Collection<String> resultCollations = new LinkedHashSet<>();
        Map<String, List<String>> suggestedTerms = suggesterResponse.getSuggestedTerms();
        if(MapUtils.isNotEmpty(suggestedTerms))
        {
            Collection<String> collations = suggestedTerms.get(dictionary);
            if(CollectionUtils.isNotEmpty(collations))
            {
                resultCollations.addAll(collations);
            }
        }
        return new SolrSuggestion(resultSuggestions, resultCollations);
    }


    protected SolrSuggestion createResultFromSpellCheckResponse(SpellCheckResponse spellCheckResponse)
    {
        Map<String, Collection<String>> resultSuggestions = new HashMap<>();
        Collection<String> resultCollations = new LinkedHashSet<>();
        List<SpellCheckResponse.Suggestion> suggestions = spellCheckResponse.getSuggestions();
        for(SpellCheckResponse.Suggestion suggestion : suggestions)
        {
            List<String> alternatives = suggestion.getAlternatives();
            resultSuggestions.put(suggestion.getToken(), alternatives);
        }
        List<SpellCheckResponse.Collation> collatedResults = spellCheckResponse.getCollatedResults();
        if(collatedResults != null)
        {
            for(SpellCheckResponse.Collation collation : collatedResults)
            {
                resultCollations.add(collation.getCollationQueryString());
            }
        }
        return new SolrSuggestion(resultSuggestions, resultCollations);
    }


    protected void populateSuggestionsFromResponse(Map<String, Collection<String>> resultSuggestionMap, Collection<String> resultCollations, SpellCheckResponse spellCheckResponse)
    {
        List<SpellCheckResponse.Suggestion> suggestions = spellCheckResponse.getSuggestions();
        for(SpellCheckResponse.Suggestion suggestion : suggestions)
        {
            List<String> alternatives = suggestion.getAlternatives();
            resultSuggestionMap.put(suggestion.getToken(), alternatives);
        }
        List<SpellCheckResponse.Collation> collatedResults = spellCheckResponse.getCollatedResults();
        if(collatedResults != null)
        {
            for(SpellCheckResponse.Collation collation : collatedResults)
            {
                resultCollations.add(collation.getCollationQueryString());
            }
        }
    }


    public SolrIndexService getSolrIndexService()
    {
        return this.solrIndexService;
    }


    @Required
    public void setSolrIndexService(SolrIndexService solrIndexService)
    {
        this.solrIndexService = solrIndexService;
    }


    public SolrSearchProviderFactory getSolrSearchProviderFactory()
    {
        return this.solrSearchProviderFactory;
    }


    @Required
    public void setSolrSearchProviderFactory(SolrSearchProviderFactory solrSearchProviderFactory)
    {
        this.solrSearchProviderFactory = solrSearchProviderFactory;
    }


    public SolrIndexedTypeCodeResolver getSolrIndexedTypeCodeResolver()
    {
        return this.solrIndexedTypeCodeResolver;
    }


    @Required
    public void setSolrIndexedTypeCodeResolver(SolrIndexedTypeCodeResolver solrIndexedTypeCodeResolver)
    {
        this.solrIndexedTypeCodeResolver = solrIndexedTypeCodeResolver;
    }
}
