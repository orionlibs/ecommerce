package de.hybris.platform.impex.distributed.batch.impl;

import com.google.common.collect.ImmutableMap;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.imp.QueryParameters;
import de.hybris.platform.impex.jalo.imp.ValueLine;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BatchingExistingItemResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(BatchingExistingItemResolver.class);
    private static final Map<UniqeColumnsKey, List<ItemModel>> QUERY_LINE_BY_LINE = (Map<UniqeColumnsKey, List<ItemModel>>)ImmutableMap.of();
    private final FlexibleSearchService flexibleSearchService;
    private final I18NService i18nService;
    private final CommonI18NService commonI18NService;
    private final BatchData batchData;
    private Map<UniqeColumnsKey, List<ItemModel>> modelsCache;


    public BatchingExistingItemResolver(FlexibleSearchService flexibleSearchService, BatchData batchData, I18NService i18nService, CommonI18NService commonI18NService)
    {
        Objects.requireNonNull(flexibleSearchService, "flexibleSearchService mustn't be null");
        Objects.requireNonNull(batchData, "batchData mustn't be null");
        Objects.requireNonNull(i18nService, "i18nService mustn't be null");
        Objects.requireNonNull(commonI18NService, "commonI18NService mustn't be null");
        this.flexibleSearchService = flexibleSearchService;
        this.batchData = batchData;
        this.i18nService = i18nService;
        this.commonI18NService = commonI18NService;
    }


    public List<ItemModel> findExisting(BatchData.ImportData importData)
    {
        ValueLine line = importData.getValueLine();
        SortedMap<StandardColumnDescriptor, Object> values = importData.getUniqueValues();
        if(values.isEmpty() || line.isUnresolved(this.batchData.getUniqueColumns()))
        {
            return Collections.emptyList();
        }
        assureItemsCache();
        if(isLineByLineForced())
        {
            return queryForLine(line, values);
        }
        return getItemsFromCache(values);
    }


    private void forceLineByLineQuerying()
    {
        this.modelsCache = QUERY_LINE_BY_LINE;
    }


    private boolean isLineByLineForced()
    {
        return (QUERY_LINE_BY_LINE == this.modelsCache);
    }


    private List<ItemModel> queryForLine(ValueLine line, SortedMap<StandardColumnDescriptor, Object> values)
    {
        QueryParameters queryParameters = createQueryParameters(line, values);
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(queryParameters.getQuery());
        fQuery.setDisableCaching(true);
        fQuery.addQueryParameters(queryParameters.getParameters());
        SearchResult<ItemModel> searchResult = this.flexibleSearchService.search(fQuery);
        return searchResult.getResult();
    }


    private List<ItemModel> getItemsFromCache(SortedMap<StandardColumnDescriptor, Object> values)
    {
        UniqeColumnsKey key = UniqeColumnsKey.from(values);
        List<ItemModel> models = this.modelsCache.get(key);
        if(models == null)
        {
            return Collections.emptyList();
        }
        return models;
    }


    private void assureItemsCache()
    {
        if(this.modelsCache != null)
        {
            return;
        }
        if(Config.getBoolean("impex.distributed.query.for.each.line", false))
        {
            LOG.info("Will not query in batch because it's turned off in the configuration.");
            forceLineByLineQuerying();
            return;
        }
        List<BatchData.ImportData> importData = this.batchData.getImportData();
        if(importData.isEmpty())
        {
            LOG.info("Will not query in batch because there is no data.");
            forceLineByLineQuerying();
            return;
        }
        HeaderDescriptor header = ((BatchData.ImportData)importData.get(0)).getCurrentHeader();
        ComposedType composedType = header.getConfiguredComposedType();
        String composedTypeCode = composedType.getCode();
        boolean isSingleton = composedType.isSingleton();
        Set<StandardColumnDescriptor> uniqueColumns = this.batchData.getUniqueColumns();
        QueryParameters params = new QueryParameters(composedTypeCode, isSingleton, uniqueColumns, ((BatchData.ImportData)importData.get(0)).getUniqueValues());
        List<Map<StandardColumnDescriptor, Object>> allValues = new ArrayList<>(importData.size());
        for(BatchData.ImportData data : importData)
        {
            SortedMap<StandardColumnDescriptor, Object> uniqueValues = data.getUniqueValues();
            if(data.getValueLine().isUnresolved(uniqueColumns))
            {
                LOG.info("Will not query in batch because there is at least one line with an unresolved reference. {}", data
                                .getValueLine());
                forceLineByLineQuerying();
                return;
            }
            if(uniqueValues.containsValue(null))
            {
                LOG.info("Will not query in batch because there is at least one line with a null reference. {}", data
                                .getValueLine());
                forceLineByLineQuerying();
                return;
            }
            allValues.add(uniqueValues);
        }
        String query = QueryParameters.buildQueryForValues(params, allValues);
        FlexibleSearchQuery fQuery = new FlexibleSearchQuery(query);
        fQuery.setDisableCaching(true);
        fQuery.addQueryParameters(params.getParameters());
        SearchResult<ItemModel> searchResult = this.flexibleSearchService.search(fQuery);
        this.modelsCache = new HashMap<>();
        for(ItemModel model : searchResult.getResult())
        {
            UniqeColumnsKey key = getKeyFromModel(uniqueColumns, model);
            List<ItemModel> models = this.modelsCache.get(key);
            if(models == null)
            {
                models = new LinkedList<>();
                this.modelsCache.put(key, models);
            }
            models.add(model);
        }
    }


    private UniqeColumnsKey getKeyFromModel(Set<StandardColumnDescriptor> uniqueColumns, ItemModel model)
    {
        SortedMap<StandardColumnDescriptor, Object> values = new TreeMap<>();
        for(StandardColumnDescriptor column : uniqueColumns)
        {
            Object value;
            if(column.isLocalized())
            {
                String langIsoCode = column.getLanguageIsoCode();
                Locale columnLocale = (langIsoCode == null) ? this.i18nService.getCurrentLocale() : this.commonI18NService.getLocaleForIsoCode(langIsoCode);
                Locale localeToUse = this.i18nService.getBestMatchingLocale(columnLocale);
                value = model.getProperty(column.getQualifier(), localeToUse);
            }
            else
            {
                value = model.getProperty(column.getQualifier());
            }
            values.put(column, value);
        }
        return UniqeColumnsKey.from(values);
    }


    private QueryParameters createQueryParameters(ValueLine line, SortedMap<StandardColumnDescriptor, Object> values)
    {
        HeaderDescriptor header = line.getHeader();
        ComposedType composedType = header.getConfiguredComposedType();
        String composedTypeCode = composedType.getCode();
        boolean isSingleton = composedType.isSingleton();
        Set<StandardColumnDescriptor> uniqueColumns = this.batchData.getUniqueColumns();
        return new QueryParameters(composedTypeCode, isSingleton, uniqueColumns, values);
    }
}
