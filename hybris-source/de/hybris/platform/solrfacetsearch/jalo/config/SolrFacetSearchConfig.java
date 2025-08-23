package de.hybris.platform.solrfacetsearch.jalo.config;

import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.solrfacetsearch.jalo.redirect.SolrFacetSearchKeywordRedirect;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SolrFacetSearchConfig extends GeneratedSolrFacetSearchConfig
{
    public List<SolrSynonymConfig> getLanguageSynonymMapping(SessionContext ctx)
    {
        List<SolrSynonymConfig> synonyms = getAllLanguageSynonymMapping(ctx).get(ctx.getLanguage());
        return (synonyms == null) ? Collections.<SolrSynonymConfig>emptyList() : synonyms;
    }


    public Map<Language, List<SolrSynonymConfig>> getAllLanguageSynonymMapping(SessionContext ctx)
    {
        Map<Language, List<SolrSynonymConfig>> map = new TreeMap<>();
        List<SolrSynonymConfig> synonyms = getSynonyms();
        for(SolrSynonymConfig syn : synonyms)
        {
            Language lang = syn.getLanguage();
            if(!map.containsKey(lang))
            {
                map.put(lang, new ArrayList<>());
            }
            ((List<SolrSynonymConfig>)map.get(lang)).add(syn);
        }
        return map;
    }


    public void setLanguageSynonymMapping(SessionContext ctx, List<SolrSynonymConfig> value)
    {
        List<SolrSynonymConfig> synonyms = new ArrayList<>(getSynonyms());
        synonyms.removeAll(getLanguageSynonymMapping(ctx));
        synonyms.addAll(value);
        setSynonyms(synonyms);
    }


    public void setAllLanguageSynonymMapping(SessionContext ctx, Map<Language, List<SolrSynonymConfig>> value)
    {
        List<SolrSynonymConfig> synonyms = new ArrayList<>();
        for(Map.Entry<Language, List<SolrSynonymConfig>> langValue : value.entrySet())
        {
            synonyms.addAll(langValue.getValue());
        }
        setSynonyms(synonyms);
    }


    public List<SolrFacetSearchKeywordRedirect> getLanguageKeywordRedirectMapping(SessionContext ctx)
    {
        List<SolrFacetSearchKeywordRedirect> keywordRedirects = getAllLanguageKeywordRedirectMapping(ctx).get(ctx
                        .getLanguage());
        return (keywordRedirects == null) ? Collections.<SolrFacetSearchKeywordRedirect>emptyList() : keywordRedirects;
    }


    public Map<Language, List<SolrFacetSearchKeywordRedirect>> getAllLanguageKeywordRedirectMapping(SessionContext ctx)
    {
        Map<Language, List<SolrFacetSearchKeywordRedirect>> map = new TreeMap<>();
        Collection<SolrFacetSearchKeywordRedirect> keywordRedirects = getKeywordRedirects();
        for(SolrFacetSearchKeywordRedirect syn : keywordRedirects)
        {
            Language lang = syn.getLanguage();
            if(!map.containsKey(lang))
            {
                map.put(lang, new ArrayList<>());
            }
            ((List<SolrFacetSearchKeywordRedirect>)map.get(lang)).add(syn);
        }
        return map;
    }


    public void setLanguageKeywordRedirectMapping(SessionContext ctx, List<SolrFacetSearchKeywordRedirect> value)
    {
        List<SolrFacetSearchKeywordRedirect> keywordRedirects = new ArrayList<>(getKeywordRedirects());
        keywordRedirects.removeAll(getLanguageKeywordRedirectMapping(ctx));
        keywordRedirects.addAll(value);
        setKeywordRedirects(keywordRedirects);
    }


    public void setAllLanguageKeywordRedirectMapping(SessionContext ctx, Map<Language, List<SolrFacetSearchKeywordRedirect>> value)
    {
    }


    public List<SolrStopWord> getLanguageStopWordMapping(SessionContext ctx)
    {
        List<SolrStopWord> stopWords = getAllLanguageStopWordMapping(ctx).get(ctx.getLanguage());
        return (stopWords == null) ? Collections.<SolrStopWord>emptyList() : stopWords;
    }


    public Map<Language, List<SolrStopWord>> getAllLanguageStopWordMapping(SessionContext ctx)
    {
        Map<Language, List<SolrStopWord>> map = new TreeMap<>();
        Collection<SolrStopWord> stopWords = getStopWords();
        for(SolrStopWord syn : stopWords)
        {
            Language lang = syn.getLanguage();
            if(!map.containsKey(lang))
            {
                map.put(lang, new ArrayList<>());
            }
            ((List<SolrStopWord>)map.get(lang)).add(syn);
        }
        return map;
    }


    public void setLanguageStopWordMapping(SessionContext ctx, List<SolrStopWord> value)
    {
        List<SolrStopWord> stopWords = new ArrayList<>(getStopWords());
        stopWords.removeAll(getLanguageStopWordMapping(ctx));
        stopWords.addAll(value);
        setStopWords(stopWords);
    }


    public void setAllLanguageStopWordMapping(SessionContext ctx, Map<Language, List<SolrStopWord>> value)
    {
    }
}
