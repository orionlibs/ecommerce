package de.hybris.platform.retention.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.processing.model.FlexibleSearchRetentionRuleModel;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlexibleSearchRetentionItemsProvider extends AbstractCleanupItemsProvider
{
    static final String PARAM_JAVA_CURRENT_TIME = "JAVA_CURRENT_TIME";
    static final String PARAM_CALC_RETIREMENT_DATE = "CALC_RETIREMENT_TIME";
    static final String PARAM_RETENTION_TIME_SECONDS = "RETENTION_TIME_SECONDS";
    private static final Logger LOG = LoggerFactory.getLogger(FlexibleSearchRetentionItemsProvider.class);
    private final FlexibleSearchRetentionRuleModel flexibleRetentionRule;
    private List<ItemToCleanup> itemsToRetention = null;
    private int currentIndex = 0;


    public FlexibleSearchRetentionItemsProvider(FlexibleSearchRetentionRuleModel rule)
    {
        this.flexibleRetentionRule = rule;
    }


    public List<ItemToCleanup> nextItemsForCleanup()
    {
        if(this.itemsToRetention == null)
        {
            validateProviderParams();
            this.itemsToRetention = getItemsToRetention();
        }
        int endIndex = this.currentIndex + this.batchSize.intValue();
        if(endIndex > this.itemsToRetention.size())
        {
            endIndex = this.itemsToRetention.size();
        }
        if(this.currentIndex > this.itemsToRetention.size())
        {
            this.currentIndex = this.itemsToRetention.size();
        }
        ImmutableList immutableList = ImmutableList.copyOf(this.itemsToRetention.subList(this.currentIndex, endIndex));
        this.currentIndex += this.batchSize.intValue();
        return (List<ItemToCleanup>)immutableList;
    }


    private void validateProviderParams()
    {
        Preconditions.checkArgument((this.batchSize != null), "Missing required argument batchSize");
        Preconditions.checkArgument((this.flexibleRetentionRule.getSearchQuery() != null), "Missing required argument searchQuery");
        Preconditions.checkArgument((this.batchSize.intValue() > 0), "Argument batchSize must be greater than 0");
    }


    private List<ItemToCleanup> getItemsToRetention()
    {
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(this.flexibleRetentionRule.getSearchQuery());
        searchQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PK.class, ComposedTypeModel.class}));
        searchQuery.addQueryParameters(getFinalQueryParams());
        try
        {
            SearchResult<List<Object>> queryResult = this.flexibleSearchService.search(searchQuery);
            List<List<Object>> resultList = queryResult.getResult();
            return (List<ItemToCleanup>)resultList.stream().map(row -> extractItemsFromResult(row)).collect(Collectors.toList());
        }
        catch(Exception e)
        {
            LOG.error("Ensure that set searchQuery only returns pk and item type in proper order, e.g. \"SELECT {pk},{itemType} FROM...\"");
            throw e;
        }
    }


    protected Map<String, Object> getFinalQueryParams()
    {
        Map<String, Object> mapToReturn = new HashMap<>();
        Map<String, Object> additionalParams = provideCalculatedAdditionalParams();
        Map<String, String> userProvidedParams = this.flexibleRetentionRule.getQueryParameters();
        if(userProvidedParams != null)
        {
            for(Map.Entry<String, String> entry : userProvidedParams.entrySet())
            {
                String key = entry.getKey();
                if(additionalParams.containsKey(key))
                {
                    LOG.warn("Param key:{} value: {} exist in queryParameters and won't be replaced by system calculated value: {}", new Object[] {key, entry
                                    .getValue(), additionalParams.get(key)});
                }
                mapToReturn.put(key, entry.getValue());
            }
        }
        mapToReturn.putAll(additionalParams);
        return mapToReturn;
    }


    protected Map<String, Object> provideCalculatedAdditionalParams()
    {
        Map<String, Object> calculatedParams = new HashMap<>();
        Instant instantNow = Instant.now().atZone(ZoneId.systemDefault()).toInstant();
        Date currentDate = Date.from(instantNow);
        calculatedParams.put("JAVA_CURRENT_TIME", currentDate);
        if(this.flexibleRetentionRule.getRetentionTimeSeconds() != null)
        {
            long retentionTimeSeconds = this.flexibleRetentionRule.getRetentionTimeSeconds().longValue();
            Date retirementDate = Date.from(instantNow.minusSeconds(retentionTimeSeconds));
            calculatedParams.put("CALC_RETIREMENT_TIME", retirementDate);
            calculatedParams.put("RETENTION_TIME_SECONDS", this.flexibleRetentionRule.getRetentionTimeSeconds());
        }
        return calculatedParams;
    }
}
