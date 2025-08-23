package de.hybris.platform.util;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StrandedItemsRegistry
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StrandedItemsRegistry.class);
    private final ConcurrentHashMap<PK, StrandedItemContext> strandedItems = new ConcurrentHashMap<>();
    private final StrandedItemsResolutionHandler<?> resolutionHandler;


    public StrandedItemsRegistry(StrandedItemsResolutionHandler<?> resolutionHandler)
    {
        this.resolutionHandler = Objects.<StrandedItemsResolutionHandler>requireNonNull(resolutionHandler);
    }


    public <T extends Item> void markOnFailure(T item, Consumer<T> logic)
    {
        markOnFailure(item, logic, i -> StrandedItemContext.NO_CONTEXT);
    }


    public <T extends Item> void markOnFailure(T item, Consumer<T> logic, Function<T, StrandedItemContext> itemContextFunction)
    {
        Objects.requireNonNull(item, "item can't be null");
        Objects.requireNonNull(logic, "logic can't be null");
        try
        {
            logic.accept(item);
        }
        catch(Exception e)
        {
            StrandedItemContext currentContext = this.strandedItems.compute(item.getPK(), (pk, oldContext) -> {
                if(oldContext == null || oldContext == StrandedItemContext.NO_CONTEXT)
                {
                    return createStrandedItemContext(item, itemContextFunction);
                }
                StrandedItemContext newContext = createStrandedItemContext(item, itemContextFunction);
                return (newContext != StrandedItemContext.NO_CONTEXT) ? newContext : oldContext;
            });
            LOGGER.info("inserted {} for item {}", new Object[] {currentContext, item.getPK(), e});
            throw e;
        }
    }


    private <T extends Item> StrandedItemContext createStrandedItemContext(T item, Function<T, StrandedItemContext> itemContextProvided)
    {
        try
        {
            return (StrandedItemContext)ObjectUtils.defaultIfNull(itemContextProvided.apply(item), StrandedItemContext.NO_CONTEXT);
        }
        catch(Exception e2)
        {
            LOGGER.warn("error while creating context data for item {} of type {}: {}", new Object[] {item.getPK(), item.getClass(), e2
                            .getMessage(), e2});
            return StrandedItemContext.NO_CONTEXT;
        }
    }


    public void markStrandedItem(PK pk)
    {
        Objects.requireNonNull(pk, "pk can't be null");
        this.strandedItems.put(pk, StrandedItemContext.NO_CONTEXT);
    }


    public Set<PK> getStrandedItems()
    {
        return Set.copyOf(this.strandedItems.keySet());
    }


    public Optional<StrandedItemContext> getStrandedItemContext(PK pk)
    {
        return Optional.ofNullable(this.strandedItems.get(pk));
    }


    private void markItemAsChecked(PK pk)
    {
        this.strandedItems.remove(Objects.requireNonNull(pk));
    }


    public void checkStrandedItems(int maxItemsToCheck)
    {
        Set<PK> cronJobsToBeChecked = getStrandedItems();
        if(CollectionUtils.isEmpty(cronJobsToBeChecked))
        {
            return;
        }
        if(Math.max(maxItemsToCheck, 0) == 0 || cronJobsToBeChecked.size() <= maxItemsToCheck)
        {
            this.resolutionHandler.checkStrandedItems(this, cronJobsToBeChecked);
        }
        else
        {
            this.resolutionHandler.checkStrandedItems(this, (Set)cronJobsToBeChecked
                            .stream().limit(maxItemsToCheck).collect(Collectors.toSet()));
        }
    }
}
