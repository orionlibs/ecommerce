package de.hybris.platform.warehousing.sourcing.impl;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.sourcing.SourcingService;
import de.hybris.platform.warehousing.sourcing.context.SourcingContextFactory;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryGroup;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryGroupingService;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryMatcher;
import de.hybris.platform.warehousing.sourcing.filter.SourcingFilterProcessor;
import de.hybris.platform.warehousing.sourcing.result.SourcingResultFactory;
import de.hybris.platform.warehousing.sourcing.strategy.SourcingStrategy;
import de.hybris.platform.warehousing.sourcing.strategy.SourcingStrategyMapper;
import de.hybris.platform.warehousing.sourcing.strategy.SourcingStrategyService;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSourcingService implements SourcingService
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSourcingService.class);
    private SourcingContextFactory sourcingContextFactory;
    private SourcingFilterProcessor sourcingFilterProcessor;
    private OrderEntryGroupingService orderEntryGroupingService;
    private Collection<OrderEntryMatcher> orderEntryMatchers;
    private SourcingStrategyService sourcingStrategyService;
    private Collection<SourcingStrategyMapper> sourcingStrategyMappers;
    private SourcingResultFactory sourcingResultFactory;


    public SourcingResults sourceOrder(AbstractOrderModel order)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("order", order);
        Preconditions.checkArgument(Objects.nonNull(order), "Parameter order cannot be null.");
        LOGGER.debug("Starting sourcing Order [{}]", order.getCode());
        Set<WarehouseModel> locations = Sets.newHashSet();
        this.sourcingFilterProcessor.filterLocations(order, locations);
        LOGGER.debug("> Total filtered sourcing locations found: {}", Integer.valueOf(locations.size()));
        Set<OrderEntryGroup> groups = this.orderEntryGroupingService.splitOrderByMatchers(order, this.orderEntryMatchers);
        LOGGER.debug("> Total order entry groups found: {}", Integer.valueOf(groups.size()));
        Collection<SourcingContext> contexts = this.sourcingContextFactory.create(groups, locations);
        Collection<SourcingResults> results = Lists.newArrayList();
        for(SourcingContext context : contexts)
        {
            List<String> productNames = Lists.newArrayList();
            context.getOrderEntries().forEach(e -> productNames.add(e.getProduct().getCode()));
            if(LOGGER.isDebugEnabled())
            {
                LOGGER.debug("Start sourcing products [{}]", StringUtils.join(productNames, ", "));
            }
            List<SourcingStrategy> strategies = this.sourcingStrategyService.getStrategies(context, this.sourcingStrategyMappers);
            if(strategies.isEmpty())
            {
                strategies = this.sourcingStrategyService.getDefaultStrategies();
            }
            List<String> strategyNames = Lists.newArrayList();
            strategies.forEach(s -> strategyNames.add(s.getClass().getSimpleName()));
            int strategiesSize = strategies.size();
            String strategiesList = StringUtils.join(strategyNames, ", ");
            LOGGER.debug("> Total sourcing strategies found for context: {} :: {}", Integer.valueOf(strategiesSize), strategiesList);
            for(SourcingStrategy strategy : strategies)
            {
                LOGGER.debug("------ Apply sourcing strategy: {}", strategy.getClass().getSimpleName());
                strategy.source(context);
                context.getResult().getResults()
                                .forEach(result -> LOGGER.debug("Warehouse found by sourcing strategy: {}", result.getWarehouse().getCode()));
                results.add(context.getResult());
                if(LOGGER.isDebugEnabled())
                {
                    LOGGER.debug("Sourcing strategy '{}' applied{} successfully", strategy.getClass().getSimpleName(), context.getResult().isComplete() ? "" : " not");
                }
                if(strategy.isTerminal().booleanValue() || context.getResult().isComplete())
                {
                    break;
                }
            }
        }
        return this.sourcingResultFactory.create(results);
    }


    protected SourcingContextFactory getSourcingContextFactory()
    {
        return this.sourcingContextFactory;
    }


    @Required
    public void setSourcingContextFactory(SourcingContextFactory sourcingContextFactory)
    {
        this.sourcingContextFactory = sourcingContextFactory;
    }


    protected SourcingFilterProcessor getSourcingFilterProcessor()
    {
        return this.sourcingFilterProcessor;
    }


    @Required
    public void setSourcingFilterProcessor(SourcingFilterProcessor sourcingFilterProcessor)
    {
        this.sourcingFilterProcessor = sourcingFilterProcessor;
    }


    protected OrderEntryGroupingService getOrderEntryGroupingService()
    {
        return this.orderEntryGroupingService;
    }


    @Required
    public void setOrderEntryGroupingService(OrderEntryGroupingService orderEntryGroupingService)
    {
        this.orderEntryGroupingService = orderEntryGroupingService;
    }


    protected Collection<OrderEntryMatcher> getOrderEntryMatchers()
    {
        return this.orderEntryMatchers;
    }


    @Required
    public void setOrderEntryMatchers(Collection<OrderEntryMatcher> orderEntryMatchers)
    {
        this.orderEntryMatchers = orderEntryMatchers;
    }


    protected SourcingStrategyService getSourcingStrategyService()
    {
        return this.sourcingStrategyService;
    }


    @Required
    public void setSourcingStrategyService(SourcingStrategyService sourcingStrategyService)
    {
        this.sourcingStrategyService = sourcingStrategyService;
    }


    protected Collection<SourcingStrategyMapper> getSourcingStrategyMappers()
    {
        return this.sourcingStrategyMappers;
    }


    @Required
    public void setSourcingStrategyMappers(Collection<SourcingStrategyMapper> sourcingStrategyMappers)
    {
        this.sourcingStrategyMappers = sourcingStrategyMappers;
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
}
