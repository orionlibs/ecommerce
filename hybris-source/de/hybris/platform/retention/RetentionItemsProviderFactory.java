package de.hybris.platform.retention;

import de.hybris.platform.processing.model.AbstractRetentionRuleModel;
import de.hybris.platform.processing.model.AfterRetentionCleanupRuleModel;
import de.hybris.platform.processing.model.FlexibleSearchRetentionRuleModel;
import de.hybris.platform.retention.impl.AfterRetentionCleanupItemsProvider;
import de.hybris.platform.retention.impl.FlexibleSearchRetentionItemsProvider;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import org.springframework.beans.factory.annotation.Required;

public class RetentionItemsProviderFactory
{
    private FlexibleSearchService flexibleSearchService;


    public RetentionItemsProvider create(RetentionRequestParams request)
    {
        AbstractRetentionRuleModel rule = request.getRetentionRule();
        if(rule instanceof AfterRetentionCleanupRuleModel)
        {
            AfterRetentionCleanupItemsProvider provider = new AfterRetentionCleanupItemsProvider((AfterRetentionCleanupRuleModel)rule);
            provider.setFlexibleSearchService(this.flexibleSearchService);
            provider.setBatchSize(request.getBatchSize());
            return (RetentionItemsProvider)provider;
        }
        if(rule instanceof FlexibleSearchRetentionRuleModel)
        {
            FlexibleSearchRetentionItemsProvider provider = new FlexibleSearchRetentionItemsProvider((FlexibleSearchRetentionRuleModel)rule);
            provider.setFlexibleSearchService(this.flexibleSearchService);
            provider.setBatchSize(request.getBatchSize());
            return (RetentionItemsProvider)provider;
        }
        throw new IllegalStateException("Wrong type of retentionRule");
    }


    @Required
    public void setFlexibleSearchService(FlexibleSearchService flexibleSearchService)
    {
        this.flexibleSearchService = flexibleSearchService;
    }
}
