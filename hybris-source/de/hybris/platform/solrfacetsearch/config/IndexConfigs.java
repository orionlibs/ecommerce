package de.hybris.platform.solrfacetsearch.config;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.solrfacetsearch.enums.IndexMode;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

public final class IndexConfigs
{
    public static IndexConfig createIndexConfig(Collection<IndexedType> indexedTypes, Collection<CatalogVersionModel> catalogVersions, Collection<LanguageModel> languages, Collection<CurrencyModel> currencies, String exportPath, int batchSize, int numberOfThreads, IndexMode indexMode,
                    boolean enabledLanguageFallbackMechanism)
    {
        IndexConfig indexConfig = new IndexConfig();
        indexConfig.setIndexedTypes(new HashMap<>());
        for(IndexedType indexedType : indexedTypes)
        {
            indexConfig.getIndexedTypes().put(indexedType.getUniqueIndexedTypeCode(), indexedType);
        }
        indexConfig.setCatalogVersions(Collections.unmodifiableCollection(catalogVersions));
        indexConfig.setLanguages(Collections.unmodifiableCollection(languages));
        indexConfig.setCurrencies(Collections.unmodifiableCollection(currencies));
        indexConfig.setExportPath(exportPath);
        indexConfig.setBatchSize(batchSize);
        indexConfig.setNumberOfThreads(numberOfThreads);
        indexConfig.setIndexMode(indexMode);
        indexConfig.setEnabledLanguageFallbackMechanism(enabledLanguageFallbackMechanism);
        return indexConfig;
    }


    public static IndexConfig createIndexConfig(Collection<IndexedType> indexedTypes, Collection<CatalogVersionModel> catalogVersions, Collection<LanguageModel> languages, Collection<CurrencyModel> currencies, String exportPath, int batchSize, int numberOfThreads,
                    boolean enabledLanguageFallbackMechanism)
    {
        return createIndexConfig(indexedTypes, catalogVersions, languages, currencies, exportPath, batchSize, 1, IndexMode.DIRECT, enabledLanguageFallbackMechanism);
    }


    public static IndexConfig createIndexConfig(Collection<IndexedType> indexedTypes, Collection<CatalogVersionModel> catalogVersions, Collection<LanguageModel> languages, Collection<CurrencyModel> currencies, String exportPath, int batchSize, boolean enabledLanguageFallbackMechanism)
    {
        return createIndexConfig(indexedTypes, catalogVersions, languages, currencies, exportPath, batchSize, 1, enabledLanguageFallbackMechanism);
    }
}
