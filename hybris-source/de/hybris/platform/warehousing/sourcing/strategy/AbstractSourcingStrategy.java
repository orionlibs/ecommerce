package de.hybris.platform.warehousing.sourcing.strategy;

import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResult;
import de.hybris.platform.warehousing.sourcing.result.SourcingResultFactory;
import java.util.Set;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractSourcingStrategy implements SourcingStrategy
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSourcingStrategy.class);
    private Boolean terminal;
    private SourcingResultFactory sourcingResultFactory;


    protected Long getAvailabilityForProduct(ProductModel productModel, SourcingLocation sourcingLocation)
    {
        Long stockLevel = Long.valueOf(0L);
        if(sourcingLocation.getAvailability() != null && sourcingLocation.getAvailability().get(productModel) != null)
        {
            stockLevel = (Long)sourcingLocation.getAvailability().get(productModel);
        }
        return stockLevel;
    }


    protected long getQuantitySourced(Set<SourcingResult> sourcingResults, AbstractOrderEntryModel orderEntry)
    {
        return sourcingResults.stream().filter(result -> (result.getAllocation() != null))
                        .mapToLong(obj -> (obj.getAllocation().get(orderEntry) == null) ? 0L : ((Long)obj.getAllocation().get(orderEntry)).longValue())
                        .sum();
    }


    protected boolean checkSourceCompleted(SourcingContext sourcingContext)
    {
        Predicate<? super AbstractOrderEntryModel> predicate = entry -> (getQuantitySourced(sourcingContext.getResult().getResults(), entry) == ((OrderEntryModel)entry).getQuantityUnallocated().longValue());
        boolean allMatch = sourcingContext.getOrderEntries().stream().allMatch(predicate);
        LOGGER.debug("The order has been completely sourced :: {}", allMatch ? "YES" : "NO");
        return allMatch;
    }


    public Boolean isTerminal()
    {
        return this.terminal;
    }


    protected SourcingResultFactory getSourcingResultFactory()
    {
        return this.sourcingResultFactory;
    }


    @Required
    public void setSourcingResultFactory(SourcingResultFactory sourcingResultFactory)
    {
        this.sourcingResultFactory = sourcingResultFactory;
    }


    @Required
    public void setTerminal(Boolean terminal)
    {
        this.terminal = terminal;
    }
}
