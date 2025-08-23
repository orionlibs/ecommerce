package de.hybris.platform.persistence.polyglot.uow;

import de.hybris.platform.persistence.polyglot.ItemStateRepository;
import de.hybris.platform.persistence.polyglot.PolyglotPersistence;
import de.hybris.platform.persistence.polyglot.model.ChangeSet;
import de.hybris.platform.persistence.polyglot.model.Identity;
import de.hybris.platform.persistence.polyglot.model.ItemState;
import de.hybris.platform.persistence.polyglot.model.Key;
import de.hybris.platform.persistence.polyglot.model.SingleAttributeKey;
import de.hybris.platform.persistence.polyglot.search.FindResult;
import de.hybris.platform.persistence.polyglot.search.StandardFindResult;
import de.hybris.platform.persistence.polyglot.search.criteria.Criteria;
import de.hybris.platform.persistence.polyglot.view.ItemStateView;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitOfWork
{
    private static final Logger LOG = LoggerFactory.getLogger(UnitOfWork.class);
    private static final SingleAttributeKey ID_ATTRIBUTE_KEY = PolyglotPersistence.CoreAttributes.pk();
    private static final SingleAttributeKey VERSION_ATTRIBUTE_KEY = PolyglotPersistence.CoreAttributes.version();
    private final Map<Identity, Long> requiredVersions = new HashMap<>();
    private final Map<Identity, UnitOfWorkEntry> entries = new HashMap<>();


    public void flush()
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Flushing {}.", this);
        }
        try
        {
            List<UnitOfWorkEntry> slRelatedEntries = getSlRelatedEntries();
            slRelatedEntries.forEach(UnitOfWorkEntry::flush);
            List<UnitOfWorkEntry> nonSlRelatedEntries = getNonSlRelatedEntries();
            nonSlRelatedEntries.forEach(e -> e.flush(true));
            LOG.debug("Flushed.");
        }
        catch(RuntimeException e)
        {
            LOG.debug("Failed to flush.", e);
            throw e;
        }
        clear();
    }


    private List<UnitOfWorkEntry> getNonSlRelatedEntries()
    {
        return (List<UnitOfWorkEntry>)this.entries.values()
                        .stream()
                        .filter(e -> !this.requiredVersions.containsKey(e.getId()))
                        .collect(Collectors.toList());
    }


    private List<UnitOfWorkEntry> getSlRelatedEntries()
    {
        return (List<UnitOfWorkEntry>)this.entries.values()
                        .stream()
                        .filter(e -> this.requiredVersions.containsKey(e.getId()))
                        .collect(Collectors.toList());
    }


    public void clear()
    {
        this.requiredVersions.clear();
        this.entries.clear();
        LOG.debug("Cleared");
    }


    public void registerRequiredVersion(Identity id, long requiredVersion)
    {
        UnitOfWorkEntry existingEntry = this.entries.get(id);
        if(existingEntry != null && !existingEntry.isNew() &&
                        !Long.valueOf(requiredVersion).equals(existingEntry.get((Key)VERSION_ATTRIBUTE_KEY)))
        {
            String msg = String.format("Registering required version %s but an ItemState with version %s has been already loaded.", new Object[] {Long.valueOf(requiredVersion), existingEntry
                            .get((Key)VERSION_ATTRIBUTE_KEY)});
            LOG.debug("Failed to register required version. {}", msg);
            throw new PolyglotPersistenceConcurrentModificationException(msg);
        }
        this.requiredVersions.put(id, Long.valueOf(requiredVersion));
    }


    public ItemState get(ItemStateRepository repo, Identity id)
    {
        UnitOfWorkEntry existingEntry = this.entries.get(id);
        if(existingEntry != null)
        {
            return existingEntry.isRemoval() ? null : (ItemState)existingEntry;
        }
        ItemState existingState = repo.get(id);
        if(existingState == null)
        {
            return null;
        }
        long version = ((Long)existingState.get((Key)VERSION_ATTRIBUTE_KEY)).longValue();
        Long requiredVersion = this.requiredVersions.get(id);
        if(requiredVersion != null && requiredVersion.longValue() != version)
        {
            String msg = String.format("Required version %s but received an ItemState with %s version", new Object[] {requiredVersion,
                            Long.valueOf(version)});
            LOG.debug("Failed to obtain an ItemState. {}", msg);
            throw new PolyglotPersistenceConcurrentModificationException(msg);
        }
        UnitOfWorkEntry newEntry = UnitOfWorkEntry.forExistingState(repo, existingState);
        this.entries.put(newEntry.getId(), newEntry);
        return (ItemState)newEntry;
    }


    public ChangeSet beginCreation(ItemStateRepository repo, Identity id)
    {
        UnitOfWorkEntry newEntry = UnitOfWorkEntry.forNonExistingState(repo, id);
        this.entries.put(id, newEntry);
        return newEntry.beginModification();
    }


    public void store(ItemStateRepository targetRepo, ChangeSet changeSet)
    {
        Identity id = UnitOfWorkEntry.extractIdFromChangeSet(targetRepo, changeSet);
        UnitOfWorkEntry existingEntry = this.entries.get(id);
        existingEntry.applyChanges(changeSet);
    }


    public void remove(ItemStateRepository targetRepo, ItemState state)
    {
        Identity id = UnitOfWorkEntry.extractIdFromItemState(targetRepo, state);
        UnitOfWorkEntry existingEntry = this.entries.get(id);
        existingEntry.markAsRemoved();
    }


    public FindResult find(ItemStateRepository targetRepo, Criteria criteria)
    {
        if(checkIfEntriesContainsAnyRemovedEntry())
        {
            Criteria criteriaForSearch = recreateCriteria(criteria);
            FindResult findResult = targetRepo.find(criteriaForSearch);
            Stream<ItemStateView> streamResult = findResult.getResult().filter(this::isNotRemoved).map(this::toModifiedState);
            return (FindResult)StandardFindResult.buildFromStream(streamResult).withCriteria(criteria).build();
        }
        FindResult fResult = targetRepo.find(criteria);
        return (FindResult)StandardFindResult.buildFromStream(fResult
                                        .getResult()
                                        .map(this::toModifiedState))
                        .withCount(fResult.getCount())
                        .withTotalCount(fResult.getTotalCount()).build();
    }


    private boolean checkIfEntriesContainsAnyRemovedEntry()
    {
        return this.entries.entrySet().stream().filter(entry -> ((UnitOfWorkEntry)entry.getValue()).isRemoval()).findAny().isPresent();
    }


    private Criteria recreateCriteria(Criteria criteria)
    {
        if(criteria.isNeedTotal())
        {
            return Criteria.newBuilderFromTemplate(criteria).withNeedTotal(false).withCount(-1).withStart(0).build();
        }
        int critCount = criteria.getCount();
        return Criteria.newBuilderFromTemplate(criteria)
                        .withCount((critCount != -1) ? (critCount + this.entries.size() + criteria.getStart()) : critCount)
                        .withStart(0)
                        .build();
    }


    private boolean isNotRemoved(ItemStateView s)
    {
        Identity id = (Identity)s.get((Key)ID_ATTRIBUTE_KEY);
        UnitOfWorkEntry entry = this.entries.get(id);
        return (entry == null || !entry.isRemoval());
    }


    private ItemStateView toModifiedState(ItemStateView s)
    {
        Identity id = (Identity)s.get((Key)ID_ATTRIBUTE_KEY);
        UnitOfWorkEntry entry = this.entries.get(id);
        return (entry == null) ? s : (ItemStateView)entry;
    }


    public String toString()
    {
        return this.entries.toString();
    }
}
