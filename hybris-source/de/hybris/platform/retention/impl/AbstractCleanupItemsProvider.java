package de.hybris.platform.retention.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.retention.RetentionItemsProvider;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import java.util.List;

public abstract class AbstractCleanupItemsProvider implements RetentionItemsProvider
{
    protected FlexibleSearchService flexibleSearchService;
    protected Integer batchSize;


    protected ItemToCleanup extractItemsFromResult(List<Object> row)
    {
        return ItemToCleanup.builder().withPK((PK)row.get(0)).withItemType(((ComposedTypeModel)row.get(1)).getCode()).build();
    }


    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }


    public void setBatchSize(Integer batchSize)
    {
        this.batchSize = batchSize;
    }
}
