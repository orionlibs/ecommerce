package de.hybris.platform.solrfacetsearch.model.config;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import de.hybris.platform.solrfacetsearch.model.cron.SolrUpdateStopWordsCronJobModel;
import de.hybris.platform.solrfacetsearch.model.cron.SolrUpdateSynonymsCronJobModel;
import de.hybris.platform.solrfacetsearch.model.indexer.cron.SolrIndexerCronJobModel;
import de.hybris.platform.solrfacetsearch.model.redirect.SolrFacetSearchKeywordRedirectModel;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

public class SolrFacetSearchConfigModel extends ItemModel
{
    public static final String _TYPECODE = "SolrFacetSearchConfig";
    public static final String _SOLRSYNONYMCONFIG2SOLRFACETSEARCHCONFIG = "SolrSynonymConfig2SolrFacetSearchConfig";
    public static final String _SOLRSTOPWORD2SOLRFACETSEARCHCONFIG = "SolrStopWord2SolrFacetSearchConfig";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String DOCUMENT = "document";
    public static final String SOLRSEARCHCONFIG = "solrSearchConfig";
    public static final String SOLRINDEXCONFIG = "solrIndexConfig";
    public static final String SOLRSERVERCONFIG = "solrServerConfig";
    public static final String INDEXNAMEPREFIX = "indexNamePrefix";
    public static final String LANGUAGESYNONYMMAPPING = "languageSynonymMapping";
    public static final String LANGUAGESTOPWORDMAPPING = "languageStopWordMapping";
    public static final String LANGUAGEKEYWORDREDIRECTMAPPING = "languageKeywordRedirectMapping";
    public static final String ENABLEDLANGUAGEFALLBACKMECHANISM = "enabledLanguageFallbackMechanism";
    public static final String LISTENERS = "listeners";
    public static final String CATALOGVERSIONS = "catalogVersions";
    public static final String CURRENCIES = "currencies";
    public static final String LANGUAGES = "languages";
    public static final String SOLRVALUERANGESETS = "solrValueRangeSets";
    public static final String SOLRINDEXERCRONJOB = "solrIndexerCronJob";
    public static final String SOLRINDEXEDTYPES = "solrIndexedTypes";
    public static final String KEYWORDREDIRECTS = "keywordRedirects";
    public static final String SOLRUPDATESYNONYMSCRONJOBS = "solrUpdateSynonymsCronJobs";
    public static final String SOLRUPDATESTOPWORDSCRONJOBS = "solrUpdateStopWordsCronJobs";
    public static final String SYNONYMS = "synonyms";
    public static final String STOPWORDS = "StopWords";


    public SolrFacetSearchConfigModel()
    {
    }


    public SolrFacetSearchConfigModel(ItemModelContext ctx)
    {
        super(ctx);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrFacetSearchConfigModel(boolean _enabledLanguageFallbackMechanism, String _name, SolrSearchConfigModel _solrSearchConfig)
    {
        setEnabledLanguageFallbackMechanism(_enabledLanguageFallbackMechanism);
        setName(_name);
        setSolrSearchConfig(_solrSearchConfig);
    }


    @Deprecated(since = "4.1.1", forRemoval = true)
    public SolrFacetSearchConfigModel(boolean _enabledLanguageFallbackMechanism, String _name, ItemModel _owner, SolrSearchConfigModel _solrSearchConfig)
    {
        setEnabledLanguageFallbackMechanism(_enabledLanguageFallbackMechanism);
        setName(_name);
        setOwner(_owner);
        setSolrSearchConfig(_solrSearchConfig);
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.GETTER)
    public List<CatalogVersionModel> getCatalogVersions()
    {
        return (List<CatalogVersionModel>)getPersistenceContext().getPropertyValue("catalogVersions");
    }


    @Accessor(qualifier = "currencies", type = Accessor.Type.GETTER)
    public List<CurrencyModel> getCurrencies()
    {
        return (List<CurrencyModel>)getPersistenceContext().getPropertyValue("currencies");
    }


    @Accessor(qualifier = "description", type = Accessor.Type.GETTER)
    public String getDescription()
    {
        return (String)getPersistenceContext().getPropertyValue("description");
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "document", type = Accessor.Type.GETTER)
    public MediaModel getDocument()
    {
        return (MediaModel)getPersistenceContext().getPropertyValue("document");
    }


    @Accessor(qualifier = "indexNamePrefix", type = Accessor.Type.GETTER)
    public String getIndexNamePrefix()
    {
        return (String)getPersistenceContext().getPropertyValue("indexNamePrefix");
    }


    @Accessor(qualifier = "keywordRedirects", type = Accessor.Type.GETTER)
    public Collection<SolrFacetSearchKeywordRedirectModel> getKeywordRedirects()
    {
        return (Collection<SolrFacetSearchKeywordRedirectModel>)getPersistenceContext().getPropertyValue("keywordRedirects");
    }


    @Accessor(qualifier = "languageKeywordRedirectMapping", type = Accessor.Type.GETTER)
    public List<SolrFacetSearchKeywordRedirectModel> getLanguageKeywordRedirectMapping()
    {
        return getLanguageKeywordRedirectMapping(null);
    }


    @Accessor(qualifier = "languageKeywordRedirectMapping", type = Accessor.Type.GETTER)
    public List<SolrFacetSearchKeywordRedirectModel> getLanguageKeywordRedirectMapping(Locale loc)
    {
        return (List<SolrFacetSearchKeywordRedirectModel>)getPersistenceContext().getLocalizedValue("languageKeywordRedirectMapping", loc);
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.GETTER)
    public List<LanguageModel> getLanguages()
    {
        return (List<LanguageModel>)getPersistenceContext().getPropertyValue("languages");
    }


    @Accessor(qualifier = "languageStopWordMapping", type = Accessor.Type.GETTER)
    public List<SolrStopWordModel> getLanguageStopWordMapping()
    {
        return getLanguageStopWordMapping(null);
    }


    @Accessor(qualifier = "languageStopWordMapping", type = Accessor.Type.GETTER)
    public List<SolrStopWordModel> getLanguageStopWordMapping(Locale loc)
    {
        return (List<SolrStopWordModel>)getPersistenceContext().getLocalizedValue("languageStopWordMapping", loc);
    }


    @Accessor(qualifier = "languageSynonymMapping", type = Accessor.Type.GETTER)
    public List<SolrSynonymConfigModel> getLanguageSynonymMapping()
    {
        return getLanguageSynonymMapping(null);
    }


    @Accessor(qualifier = "languageSynonymMapping", type = Accessor.Type.GETTER)
    public List<SolrSynonymConfigModel> getLanguageSynonymMapping(Locale loc)
    {
        return (List<SolrSynonymConfigModel>)getPersistenceContext().getLocalizedValue("languageSynonymMapping", loc);
    }


    @Accessor(qualifier = "listeners", type = Accessor.Type.GETTER)
    public Collection<String> getListeners()
    {
        return (Collection<String>)getPersistenceContext().getPropertyValue("listeners");
    }


    @Accessor(qualifier = "name", type = Accessor.Type.GETTER)
    public String getName()
    {
        return (String)getPersistenceContext().getPropertyValue("name");
    }


    @Accessor(qualifier = "solrIndexConfig", type = Accessor.Type.GETTER)
    public SolrIndexConfigModel getSolrIndexConfig()
    {
        return (SolrIndexConfigModel)getPersistenceContext().getPropertyValue("solrIndexConfig");
    }


    @Accessor(qualifier = "solrIndexedTypes", type = Accessor.Type.GETTER)
    public List<SolrIndexedTypeModel> getSolrIndexedTypes()
    {
        return (List<SolrIndexedTypeModel>)getPersistenceContext().getPropertyValue("solrIndexedTypes");
    }


    @Accessor(qualifier = "solrIndexerCronJob", type = Accessor.Type.GETTER)
    public List<SolrIndexerCronJobModel> getSolrIndexerCronJob()
    {
        return (List<SolrIndexerCronJobModel>)getPersistenceContext().getPropertyValue("solrIndexerCronJob");
    }


    @Accessor(qualifier = "solrSearchConfig", type = Accessor.Type.GETTER)
    public SolrSearchConfigModel getSolrSearchConfig()
    {
        return (SolrSearchConfigModel)getPersistenceContext().getPropertyValue("solrSearchConfig");
    }


    @Accessor(qualifier = "solrServerConfig", type = Accessor.Type.GETTER)
    public SolrServerConfigModel getSolrServerConfig()
    {
        return (SolrServerConfigModel)getPersistenceContext().getPropertyValue("solrServerConfig");
    }


    @Accessor(qualifier = "solrUpdateStopWordsCronJobs", type = Accessor.Type.GETTER)
    public List<SolrUpdateStopWordsCronJobModel> getSolrUpdateStopWordsCronJobs()
    {
        return (List<SolrUpdateStopWordsCronJobModel>)getPersistenceContext().getPropertyValue("solrUpdateStopWordsCronJobs");
    }


    @Accessor(qualifier = "solrUpdateSynonymsCronJobs", type = Accessor.Type.GETTER)
    public List<SolrUpdateSynonymsCronJobModel> getSolrUpdateSynonymsCronJobs()
    {
        return (List<SolrUpdateSynonymsCronJobModel>)getPersistenceContext().getPropertyValue("solrUpdateSynonymsCronJobs");
    }


    @Accessor(qualifier = "solrValueRangeSets", type = Accessor.Type.GETTER)
    public List<SolrValueRangeSetModel> getSolrValueRangeSets()
    {
        return (List<SolrValueRangeSetModel>)getPersistenceContext().getPropertyValue("solrValueRangeSets");
    }


    @Accessor(qualifier = "StopWords", type = Accessor.Type.GETTER)
    public List<SolrStopWordModel> getStopWords()
    {
        return (List<SolrStopWordModel>)getPersistenceContext().getPropertyValue("StopWords");
    }


    @Accessor(qualifier = "synonyms", type = Accessor.Type.GETTER)
    public List<SolrSynonymConfigModel> getSynonyms()
    {
        return (List<SolrSynonymConfigModel>)getPersistenceContext().getPropertyValue("synonyms");
    }


    @Accessor(qualifier = "enabledLanguageFallbackMechanism", type = Accessor.Type.GETTER)
    public boolean isEnabledLanguageFallbackMechanism()
    {
        return toPrimitive((Boolean)getPersistenceContext().getPropertyValue("enabledLanguageFallbackMechanism"));
    }


    @Accessor(qualifier = "catalogVersions", type = Accessor.Type.SETTER)
    public void setCatalogVersions(List<CatalogVersionModel> value)
    {
        getPersistenceContext().setPropertyValue("catalogVersions", value);
    }


    @Accessor(qualifier = "currencies", type = Accessor.Type.SETTER)
    public void setCurrencies(List<CurrencyModel> value)
    {
        getPersistenceContext().setPropertyValue("currencies", value);
    }


    @Accessor(qualifier = "description", type = Accessor.Type.SETTER)
    public void setDescription(String value)
    {
        getPersistenceContext().setPropertyValue("description", value);
    }


    @Deprecated(since = "ages", forRemoval = true)
    @Accessor(qualifier = "document", type = Accessor.Type.SETTER)
    public void setDocument(MediaModel value)
    {
        getPersistenceContext().setPropertyValue("document", value);
    }


    @Accessor(qualifier = "enabledLanguageFallbackMechanism", type = Accessor.Type.SETTER)
    public void setEnabledLanguageFallbackMechanism(boolean value)
    {
        getPersistenceContext().setPropertyValue("enabledLanguageFallbackMechanism", toObject(value));
    }


    @Accessor(qualifier = "indexNamePrefix", type = Accessor.Type.SETTER)
    public void setIndexNamePrefix(String value)
    {
        getPersistenceContext().setPropertyValue("indexNamePrefix", value);
    }


    @Accessor(qualifier = "keywordRedirects", type = Accessor.Type.SETTER)
    public void setKeywordRedirects(Collection<SolrFacetSearchKeywordRedirectModel> value)
    {
        getPersistenceContext().setPropertyValue("keywordRedirects", value);
    }


    @Accessor(qualifier = "languageKeywordRedirectMapping", type = Accessor.Type.SETTER)
    public void setLanguageKeywordRedirectMapping(List<SolrFacetSearchKeywordRedirectModel> value)
    {
        setLanguageKeywordRedirectMapping(value, null);
    }


    @Accessor(qualifier = "languageKeywordRedirectMapping", type = Accessor.Type.SETTER)
    public void setLanguageKeywordRedirectMapping(List<SolrFacetSearchKeywordRedirectModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("languageKeywordRedirectMapping", loc, value);
    }


    @Accessor(qualifier = "languages", type = Accessor.Type.SETTER)
    public void setLanguages(List<LanguageModel> value)
    {
        getPersistenceContext().setPropertyValue("languages", value);
    }


    @Accessor(qualifier = "languageStopWordMapping", type = Accessor.Type.SETTER)
    public void setLanguageStopWordMapping(List<SolrStopWordModel> value)
    {
        setLanguageStopWordMapping(value, null);
    }


    @Accessor(qualifier = "languageStopWordMapping", type = Accessor.Type.SETTER)
    public void setLanguageStopWordMapping(List<SolrStopWordModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("languageStopWordMapping", loc, value);
    }


    @Accessor(qualifier = "languageSynonymMapping", type = Accessor.Type.SETTER)
    public void setLanguageSynonymMapping(List<SolrSynonymConfigModel> value)
    {
        setLanguageSynonymMapping(value, null);
    }


    @Accessor(qualifier = "languageSynonymMapping", type = Accessor.Type.SETTER)
    public void setLanguageSynonymMapping(List<SolrSynonymConfigModel> value, Locale loc)
    {
        getPersistenceContext().setLocalizedValue("languageSynonymMapping", loc, value);
    }


    @Accessor(qualifier = "listeners", type = Accessor.Type.SETTER)
    public void setListeners(Collection<String> value)
    {
        getPersistenceContext().setPropertyValue("listeners", value);
    }


    @Accessor(qualifier = "name", type = Accessor.Type.SETTER)
    public void setName(String value)
    {
        getPersistenceContext().setPropertyValue("name", value);
    }


    @Accessor(qualifier = "solrIndexConfig", type = Accessor.Type.SETTER)
    public void setSolrIndexConfig(SolrIndexConfigModel value)
    {
        getPersistenceContext().setPropertyValue("solrIndexConfig", value);
    }


    @Accessor(qualifier = "solrIndexedTypes", type = Accessor.Type.SETTER)
    public void setSolrIndexedTypes(List<SolrIndexedTypeModel> value)
    {
        getPersistenceContext().setPropertyValue("solrIndexedTypes", value);
    }


    @Accessor(qualifier = "solrIndexerCronJob", type = Accessor.Type.SETTER)
    public void setSolrIndexerCronJob(List<SolrIndexerCronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("solrIndexerCronJob", value);
    }


    @Accessor(qualifier = "solrSearchConfig", type = Accessor.Type.SETTER)
    public void setSolrSearchConfig(SolrSearchConfigModel value)
    {
        getPersistenceContext().setPropertyValue("solrSearchConfig", value);
    }


    @Accessor(qualifier = "solrServerConfig", type = Accessor.Type.SETTER)
    public void setSolrServerConfig(SolrServerConfigModel value)
    {
        getPersistenceContext().setPropertyValue("solrServerConfig", value);
    }


    @Accessor(qualifier = "solrUpdateStopWordsCronJobs", type = Accessor.Type.SETTER)
    public void setSolrUpdateStopWordsCronJobs(List<SolrUpdateStopWordsCronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("solrUpdateStopWordsCronJobs", value);
    }


    @Accessor(qualifier = "solrUpdateSynonymsCronJobs", type = Accessor.Type.SETTER)
    public void setSolrUpdateSynonymsCronJobs(List<SolrUpdateSynonymsCronJobModel> value)
    {
        getPersistenceContext().setPropertyValue("solrUpdateSynonymsCronJobs", value);
    }


    @Accessor(qualifier = "solrValueRangeSets", type = Accessor.Type.SETTER)
    public void setSolrValueRangeSets(List<SolrValueRangeSetModel> value)
    {
        getPersistenceContext().setPropertyValue("solrValueRangeSets", value);
    }


    @Accessor(qualifier = "StopWords", type = Accessor.Type.SETTER)
    public void setStopWords(List<SolrStopWordModel> value)
    {
        getPersistenceContext().setPropertyValue("StopWords", value);
    }


    @Accessor(qualifier = "synonyms", type = Accessor.Type.SETTER)
    public void setSynonyms(List<SolrSynonymConfigModel> value)
    {
        getPersistenceContext().setPropertyValue("synonyms", value);
    }
}
