package de.hybris.platform.warehousing.sourcing.context.impl;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import de.hybris.platform.ordersplitting.model.WarehouseModel;
import de.hybris.platform.warehousing.data.sourcing.SourcingContext;
import de.hybris.platform.warehousing.data.sourcing.SourcingLocation;
import de.hybris.platform.warehousing.data.sourcing.SourcingResults;
import de.hybris.platform.warehousing.sourcing.context.SourcingContextFactory;
import de.hybris.platform.warehousing.sourcing.context.grouping.OrderEntryGroup;
import de.hybris.platform.warehousing.sourcing.context.populator.SourcingLocationPopulator;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class DefaultSourcingContextFactory implements SourcingContextFactory, InitializingBean
{
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultSourcingContextFactory.class);
    private Set<SourcingLocationPopulator> sourcingLocationPopulators;


    public Collection<SourcingContext> create(Collection<OrderEntryGroup> groups, Collection<WarehouseModel> locations) throws IllegalArgumentException
    {
        Collection<SourcingContext> contexts = Lists.newArrayList();
        if(CollectionUtils.isEmpty(groups))
        {
            LOGGER.info("No order groups to source.");
            return contexts;
        }
        if(CollectionUtils.isEmpty(locations))
        {
            LOGGER.info("No sourcing locations found for sourcing order groups.");
            return contexts;
        }
        for(OrderEntryGroup group : groups)
        {
            SourcingContext context = new SourcingContext();
            SourcingResults results = new SourcingResults();
            results.setResults(Sets.newHashSet());
            results.setComplete(Boolean.FALSE.booleanValue());
            context.setResult(results);
            context.setOrderEntries(Lists.newArrayList(group.getEntries()));
            Set<SourcingLocation> sourcingLocations = Sets.newHashSet();
            locations.forEach(location -> sourcingLocations.add(createSourcingLocation(context, location)));
            context.setSourcingLocations(sourcingLocations);
            contexts.add(context);
        }
        return contexts;
    }


    protected SourcingLocation createSourcingLocation(SourcingContext context, WarehouseModel location)
    {
        SourcingLocation sourcingLocation = new SourcingLocation();
        sourcingLocation.setWarehouse(location);
        sourcingLocation.setContext(context);
        getSourcingLocationPopulators().forEach(populator -> populator.populate(location, sourcingLocation));
        return sourcingLocation;
    }


    public void afterPropertiesSet() throws Exception
    {
        if(CollectionUtils.isEmpty(getSourcingLocationPopulators()))
        {
            throw new IllegalArgumentException("Sourcing location populators cannot be empty.");
        }
    }


    protected Set<SourcingLocationPopulator> getSourcingLocationPopulators()
    {
        return this.sourcingLocationPopulators;
    }


    @Required
    public void setSourcingLocationPopulators(Set<SourcingLocationPopulator> populators)
    {
        this.sourcingLocationPopulators = populators;
    }
}
