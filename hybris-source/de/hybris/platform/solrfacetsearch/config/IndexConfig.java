package de.hybris.platform.solrfacetsearch.config;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.enums.IndexMode;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

public class IndexConfig implements Serializable
{
    private static final long serialVersionUID = 1L;
    private Map<String, IndexedType> indexedTypes;
    private Collection<CatalogVersionModel> catalogVersions;
    private Collection<LanguageModel> languages;
    private Collection<CurrencyModel> currencies;
    private boolean enabledLanguageFallbackMechanism;
    private Collection<String> listeners;
    private String exportPath;
    private int batchSize;
    private int numberOfThreads;
    private IndexMode indexMode;
    private CommitMode commitMode;
    private OptimizeMode optimizeMode;
    private boolean ignoreErrors;
    private boolean legacyMode;
    private int maxRetries;
    private int maxBatchRetries;
    private boolean distributedIndexing;
    private String nodeGroup;
    private BaseSiteModel baseSite;


    public void setIndexedTypes(Map<String, IndexedType> indexedTypes)
    {
        this.indexedTypes = indexedTypes;
    }


    public Map<String, IndexedType> getIndexedTypes()
    {
        return this.indexedTypes;
    }


    public void setCatalogVersions(Collection<CatalogVersionModel> catalogVersions)
    {
        this.catalogVersions = catalogVersions;
    }


    public Collection<CatalogVersionModel> getCatalogVersions()
    {
        return this.catalogVersions;
    }


    public void setLanguages(Collection<LanguageModel> languages)
    {
        this.languages = languages;
    }


    public Collection<LanguageModel> getLanguages()
    {
        return this.languages;
    }


    public void setCurrencies(Collection<CurrencyModel> currencies)
    {
        this.currencies = currencies;
    }


    public Collection<CurrencyModel> getCurrencies()
    {
        return this.currencies;
    }


    public void setEnabledLanguageFallbackMechanism(boolean enabledLanguageFallbackMechanism)
    {
        this.enabledLanguageFallbackMechanism = enabledLanguageFallbackMechanism;
    }


    public boolean isEnabledLanguageFallbackMechanism()
    {
        return this.enabledLanguageFallbackMechanism;
    }


    public void setListeners(Collection<String> listeners)
    {
        this.listeners = listeners;
    }


    public Collection<String> getListeners()
    {
        return this.listeners;
    }


    public void setExportPath(String exportPath)
    {
        this.exportPath = exportPath;
    }


    public String getExportPath()
    {
        return this.exportPath;
    }


    public void setBatchSize(int batchSize)
    {
        this.batchSize = batchSize;
    }


    public int getBatchSize()
    {
        return this.batchSize;
    }


    public void setNumberOfThreads(int numberOfThreads)
    {
        this.numberOfThreads = numberOfThreads;
    }


    public int getNumberOfThreads()
    {
        return this.numberOfThreads;
    }


    public void setIndexMode(IndexMode indexMode)
    {
        this.indexMode = indexMode;
    }


    public IndexMode getIndexMode()
    {
        return this.indexMode;
    }


    public void setCommitMode(CommitMode commitMode)
    {
        this.commitMode = commitMode;
    }


    public CommitMode getCommitMode()
    {
        return this.commitMode;
    }


    public void setOptimizeMode(OptimizeMode optimizeMode)
    {
        this.optimizeMode = optimizeMode;
    }


    public OptimizeMode getOptimizeMode()
    {
        return this.optimizeMode;
    }


    public void setIgnoreErrors(boolean ignoreErrors)
    {
        this.ignoreErrors = ignoreErrors;
    }


    public boolean isIgnoreErrors()
    {
        return this.ignoreErrors;
    }


    @Deprecated
    public void setLegacyMode(boolean legacyMode)
    {
        this.legacyMode = legacyMode;
    }


    @Deprecated
    public boolean isLegacyMode()
    {
        return this.legacyMode;
    }


    public void setMaxRetries(int maxRetries)
    {
        this.maxRetries = maxRetries;
    }


    public int getMaxRetries()
    {
        return this.maxRetries;
    }


    public void setMaxBatchRetries(int maxBatchRetries)
    {
        this.maxBatchRetries = maxBatchRetries;
    }


    public int getMaxBatchRetries()
    {
        return this.maxBatchRetries;
    }


    public void setDistributedIndexing(boolean distributedIndexing)
    {
        this.distributedIndexing = distributedIndexing;
    }


    public boolean isDistributedIndexing()
    {
        return this.distributedIndexing;
    }


    public void setNodeGroup(String nodeGroup)
    {
        this.nodeGroup = nodeGroup;
    }


    public String getNodeGroup()
    {
        return this.nodeGroup;
    }


    public void setBaseSite(BaseSiteModel baseSite)
    {
        this.baseSite = baseSite;
    }


    public BaseSiteModel getBaseSite()
    {
        return this.baseSite;
    }
}
