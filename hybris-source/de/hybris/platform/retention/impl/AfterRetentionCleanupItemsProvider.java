package de.hybris.platform.retention.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.processing.model.AfterRetentionCleanupRuleModel;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

public class AfterRetentionCleanupItemsProvider extends AbstractCleanupItemsProvider
{
    private PK lastPK;
    private boolean isAllRecordsRetrived = false;
    private final AfterRetentionCleanupRuleModel rule;


    public AfterRetentionCleanupItemsProvider(AfterRetentionCleanupRuleModel retentionModel)
    {
        this.rule = retentionModel;
    }


    public List<ItemToCleanup> nextItemsForCleanup()
    {
        if(this.isAllRecordsRetrived)
        {
            return Collections.emptyList();
        }
        Map<String, Object> parameters = new HashMap<>();
        String query = buildQuery(parameters);
        FlexibleSearchQuery searchQuery = new FlexibleSearchQuery(query.toString());
        for(Map.Entry<String, Object> entry : parameters.entrySet())
        {
            searchQuery.addQueryParameter(entry.getKey(), entry.getValue());
        }
        searchQuery.setResultClassList(Arrays.asList((Class<?>[][])new Class[] {PK.class, ComposedTypeModel.class}));
        searchQuery.setCount(this.batchSize.intValue());
        SearchResult<List<Object>> queryResult = this.flexibleSearchService.search(searchQuery);
        List<ItemToCleanup> retentionItems = (List<ItemToCleanup>)queryResult.getResult().stream().map(row -> extractItemsFromResult(row)).collect(Collectors.toList());
        if(CollectionUtils.isNotEmpty(retentionItems))
        {
            this.lastPK = ((ItemToCleanup)retentionItems.get(retentionItems.size() - 1)).getPk();
        }
        else
        {
            this.isAllRecordsRetrived = true;
        }
        return retentionItems;
    }


    private String buildQuery(Map<String, Object> parameters)
    {
        StringBuilder query = new StringBuilder();
        query.append(
                        String.format("select {%s}, {%s} from {%s} ", new Object[] {"pk", "itemtype", this.rule.getRetirementItemType().getCode()}));
        query.append(" where 1=1 ");
        if(StringUtils.isNotEmpty(this.rule.getItemFilterExpression()))
        {
            query.append(" and ").append(this.rule.getItemFilterExpression()).append(" ");
        }
        if(this.rule.getRetirementDateAttribute() != null && this.rule.getRetentionTimeSeconds() != null)
        {
            long retirementSeconds = this.rule.getRetentionTimeSeconds().longValue();
            Date retirementDate = Date.from(Instant.now().atZone(ZoneId.systemDefault()).minusSeconds(retirementSeconds).toInstant());
            query.append(String.format(" and {%s} < ?retirementDate ", new Object[] {this.rule.getRetirementDateAttribute().getQualifier()}));
            parameters.put("retirementDate", retirementDate);
        }
        else if(StringUtils.isNotEmpty(this.rule.getRetirementDateExpression()))
        {
            query.append(" and ").append(this.rule.getRetirementDateExpression()).append(" ");
        }
        else
        {
            throw new IllegalStateException("Retention must be explicitly set by retirementDateAttribute with retentionTimeSeconds or by retriementDateExpression");
        }
        if(this.lastPK != null)
        {
            query.append(String.format(" and {%s} > ?pkValue", new Object[] {"pk"}));
            parameters.put("pkValue", this.lastPK.getLong());
        }
        query.append(String.format(" and ({%s} = 0 or {%s} is NULL) ", new Object[] {"sealed", "sealed"}));
        query.append(String.format(" order by {%s} asc", new Object[] {"pk"}));
        return query.toString();
    }
}
