package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.catalog.RecoverableSynchronizationPersistenceException;
import de.hybris.platform.catalog.SynchronizationPersistenceException;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.SyncAttributeDescriptorConfig;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.apache.log4j.Logger;

public class CatalogVersionSyncCopyContext extends GenericCatalogCopyContext
{
    private static final Logger LOG = Logger.getLogger(CatalogVersionSyncCopyContext.class.getName());
    private final CatalogVersionSyncWorker worker;
    private final Map<CatalogVersion, CatalogVersion> cv_map;


    public CatalogVersionSyncCopyContext(CatalogVersionSyncJob job, CatalogVersionSyncCronJob cronjob, CatalogVersionSyncWorker worker)
    {
        super(job
                                        .createSyncSessionContext((SyncItemCronJob)cronjob), job, cronjob,
                        AbstractItemCopyContext.getLogLevel(cronjob), (cronjob != null && cronjob
                                        .isForceUpdateAsPrimitive()));
        if(cronjob != null)
        {
            setSavePrevousValues(cronjob.isCreateSavedValuesAsPrimitive());
        }
        this.worker = worker;
        this.cv_map = job.loadDependentCatalogVersionMap(false);
    }


    protected CatalogVersionSyncWorker getWorker()
    {
        return this.worker;
    }


    protected SyncSchedule getCurrentSchedule()
    {
        return getWorker().getCurrent();
    }


    protected boolean hasSchedule(CatalogItemCopyCreator icc)
    {
        return hasSchedule(icc.getSourceItem().getPK());
    }


    protected boolean hasSchedule(PK srcPK)
    {
        return (getCurrentSchedule() != null && getCurrentSchedule().getSrcPK().equals(srcPK));
    }


    protected int getCurrentTurn()
    {
        return getWorker().getMaster().getCurrentTurn();
    }


    protected void afterCopying(Item copy, CatalogItemCopyCreator icc)
    {
        super.afterCopying(copy, icc);
        if(mustDump(icc))
        {
            writeDumpRecord(icc);
        }
    }


    protected boolean mustDump(CatalogItemCopyCreator icc)
    {
        boolean noCopyCreated = (icc.getTargetItem() == null);
        boolean isUnfinishedCopy = icc.hasPendingAttributes();
        boolean isIndependentlyScheduled = !icc.isPartOf();
        boolean isScheduledItem = hasSchedule(icc);
        boolean wasOptimisticLockException = icc.encounteredOptimisticLockException();
        boolean ret = (wasOptimisticLockException || isUnfinishedCopy || (noCopyCreated && (isScheduledItem || isIndependentlyScheduled)));
        return ret;
    }


    protected void writeDumpRecord(CatalogItemCopyCreator icc)
    {
        boolean copyExists = (icc.getTargetItem() != null);
        Set<String> pendingAttributes = null;
        if(copyExists)
        {
            pendingAttributes = new LinkedHashSet<>();
            for(AttributeCopyCreator acc : icc.getPendingAttributes())
            {
                pendingAttributes.add(acc.getDescriptor().getQualifier());
            }
        }
        Map<PK, PK> parentMappings = null;
        for(ItemCopyCreator parentCC = icc.getParent(); parentCC != null; parentCC = parentCC.getParent())
        {
            Item tgt = parentCC.getTargetItem();
            if(tgt != null)
            {
                if(parentMappings == null)
                {
                    parentMappings = new LinkedHashMap<>(5);
                }
                parentMappings.put(parentCC.getSourceItem().getPK(), tgt.getPK());
            }
        }
        writeDumpRecord(icc
                                        .getSourceItem().getPK(),
                        (icc.getTargetItem() != null) ? icc.getTargetItem().getPK() : null,
                        (icc.getSyncTimestamp() != null) ? icc.getSyncTimestamp().getPK() : null, pendingAttributes, parentMappings, icc
                                        .encounteredOptimisticLockException());
    }


    protected void writeDumpRecord(PK srcPK, PK tgtPK, PK timestampPK, Set<String> pendingAttributes, Map<PK, PK> parentMappings, boolean isDeadlockVictim)
    {
        SyncSchedule newRecord = new SyncSchedule(srcPK, tgtPK, timestampPK, pendingAttributes, parentMappings, isDeadlockVictim);
        getWorker().getMaster().dump(newRecord);
    }


    protected Set<String> getCopyCreatorWhitelist(Item original, Item toUpdate)
    {
        ComposedType myType = original.getComposedType();
        Set<String> ret = new HashSet<>();
        if(toUpdate != null && isCatalogItem(original) && isFromSourceVersion(original))
        {
            ret.addAll(getWorker().getMaster().getCatalogRequiredAttributes(myType));
        }
        Set<String> pendingAttributes = hasSchedule(original.getPK()) ? getCurrentSchedule().getPendingAttributes() : Collections.EMPTY_SET;
        boolean hasPending = !pendingAttributes.isEmpty();
        for(SyncAttributeDescriptorData cfg : getWorker().getMaster()
                        .getAllAttributeConfig(myType))
        {
            if(!hasPending || pendingAttributes.contains(cfg.getQualifier()))
            {
                if(cfg != null && cfg.isIncludedInSync())
                {
                    ret.add(cfg.getQualifier());
                }
            }
        }
        ret.addAll(super.getCopyCreatorWhitelist(original, toUpdate));
        return ret;
    }


    protected Set getCopyCreatorBlacklist(Item original, Item toUpdate)
    {
        Set<String> ret = super.getCopyCreatorBlacklist(original, toUpdate);
        if(original != null)
        {
            ComposedType composedType = original.getComposedType();
            ret.addAll(getWorker().getMaster().getDontCopySet(composedType));
            ret.addAll(getNonCopyableOneToManyAttributes(original, composedType, (toUpdate != null)));
        }
        return ret;
    }


    protected Set<String> getNonCopyableOneToManyAttributes(Item original, ComposedType composedType, boolean forUpdate)
    {
        Set<String> ret = null;
        boolean isNonCatalogItem = !isCatalogItem(original);
        for(AttributeDescriptor attributeDescriptor : composedType.getAttributeDescriptorsIncludingPrivate())
        {
            if(isPotentiallyNonCopyableOneToManyAttribute(attributeDescriptor, forUpdate))
            {
                if(isNonCatalogItem || hasExternalCatalogReferences(attributeDescriptor, original))
                {
                    if(ret == null)
                    {
                        ret = new LinkedHashSet<>();
                    }
                    ret.add(attributeDescriptor.getQualifier());
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    boolean hasExternalCatalogReferences(AttributeDescriptor attributeDescriptor, Item original)
    {
        try
        {
            if(attributeDescriptor.isLocalized())
            {
                Map<Language, Collection<Item>> locRefs = (Map<Language, Collection<Item>>)original.getAttribute(getCtx(), attributeDescriptor
                                .getQualifier());
                for(Map.Entry<Language, Collection<Item>> e : locRefs.entrySet())
                {
                    for(Item i : e.getValue())
                    {
                        if(!isFromSourceVersion(i))
                        {
                            return true;
                        }
                    }
                }
                return false;
            }
            Collection<Item> references = (Collection<Item>)original.getAttribute(getCtx(), attributeDescriptor
                            .getQualifier());
            for(Item i : references)
            {
                if(!isFromSourceVersion(i))
                {
                    return true;
                }
            }
            return false;
        }
        catch(Exception e)
        {
            LOG.warn("error reading " + attributeDescriptor.getQualifier() + " of " + original + " due to " + e.getMessage());
            return true;
        }
    }


    protected boolean isPotentiallyNonCopyableOneToManyAttribute(AttributeDescriptor attributeDescriptor, boolean forUpdate)
    {
        if(attributeDescriptor instanceof RelationDescriptor && !attributeDescriptor.isPartOf() && (attributeDescriptor
                        .isWritable() || (forUpdate && attributeDescriptor.isInitial())))
        {
            RelationDescriptor relationDescriptor = (RelationDescriptor)attributeDescriptor;
            return (relationDescriptor.getRelationType().isOneToMany() && !attributeDescriptor.isPartOf() &&
                            !isOneEndAttribute(relationDescriptor));
        }
        return false;
    }


    private boolean isOneEndAttribute(RelationDescriptor relationDescriptor)
    {
        return (relationDescriptor.isProperty() || relationDescriptor.getPersistenceQualifier() != null);
    }


    protected Map getCopyCreatorPresetValues(Item original, Item copyToupdate)
    {
        ComposedType myType = original.getComposedType();
        Set<String> pendingAttributes = hasSchedule(original.getPK()) ? getCurrentSchedule().getPendingAttributes() : Collections.EMPTY_SET;
        boolean hasPending = !pendingAttributes.isEmpty();
        Map<String, Object> ret = null;
        for(SyncAttributeDescriptorData cfg : getWorker().getMaster().getAllAttributeConfig(myType))
        {
            if(!hasPending || pendingAttributes.contains(cfg.getQualifier()))
            {
                if(cfg != null && cfg.isIncludedInSync())
                {
                    Object preset = cfg.getPresetValue();
                    if(preset != null)
                    {
                        if(ret == null)
                        {
                            ret = new HashMap<>();
                        }
                        ret.put(cfg.getQualifier(), preset);
                    }
                }
            }
        }
        Map<String, Object> superMap = super.getCopyCreatorPresetValues(original, copyToupdate);
        if(superMap != null && !superMap.isEmpty())
        {
            if(ret == null)
            {
                return superMap;
            }
            for(Map.Entry<String, Object> e : superMap.entrySet())
            {
                if(!ret.containsKey(e.getKey()))
                {
                    ret.put(e.getKey(), e.getValue());
                }
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_MAP;
    }


    protected boolean isUntranslatablePartOfItem(AttributeCopyCreator acc, Item original)
    {
        SyncAttributeDescriptorData cfg = getWorker().getMaster().getAttributeConfig(acc
                        .getDescriptor().getRealAttributeDescriptor());
        if(cfg != null && cfg.isUntranslatable())
        {
            return true;
        }
        return super.isUntranslatablePartOfItem(acc, original);
    }


    protected boolean mustBeTranslated(AttributeCopyCreator acc, Item original)
    {
        SyncAttributeDescriptorData cfg = getWorker().getMaster().getAttributeConfig(acc
                        .getDescriptor().getRealAttributeDescriptor());
        if(cfg != null && cfg.isUntranslatable())
        {
            return false;
        }
        return super.mustBeTranslated(acc, original);
    }


    protected boolean canBeTranslatedPartially(AttributeCopyCreator attributeCopyCreator)
    {
        SyncAttributeDescriptorData syncattributeData = getSyncAttributeDescriptorData(attributeCopyCreator);
        if(syncattributeData == null || syncattributeData.isPartialTranslationEnabled() == null)
        {
            return super.canBeTranslatedPartially(attributeCopyCreator);
        }
        return syncattributeData.isPartialTranslationEnabled().booleanValue();
    }


    protected boolean isPartOf(AttributeDescriptor attributeDescriptor)
    {
        if(super.isPartOf(attributeDescriptor))
        {
            return true;
        }
        if(getWorker() != null)
        {
            SyncAttributeDescriptorData syncAttributeDescriptorData = getWorker().getMaster().getAttributeConfig(attributeDescriptor);
            return (syncAttributeDescriptorData != null && syncAttributeDescriptorData.isCopyByValue());
        }
        SyncAttributeDescriptorConfig cfg = findConfig(attributeDescriptor);
        return (cfg != null && cfg.isCopyByValueAsPrimitive());
    }


    private final SyncAttributeDescriptorConfig findConfig(AttributeDescriptor attributeDescriptor)
    {
        Map<String, Object> params = new HashMap<>(3);
        params.put("job", getJob());
        params.put("ad", attributeDescriptor.isInherited() ?
                        attributeDescriptor.getDeclaringEnclosingType().getAttributeDescriptorIncludingPrivate(attributeDescriptor
                                        .getQualifier()) : attributeDescriptor);
        List<SyncAttributeDescriptorConfig> matches = FlexibleSearch.getInstance().search(null, "SELECT {PK} FROM {" + GeneratedCatalogConstants.TC.SYNCATTRIBUTEDESCRIPTORCONFIG + "*} WHERE {syncJob}=?job AND {attributeDescriptor}=?ad", params, SyncAttributeDescriptorConfig.class).getResult();
        return matches.isEmpty() ? null : matches.get(0);
    }


    protected SyncAttributeDescriptorData getSyncAttributeDescriptorData(AttributeCopyCreator attributeCopyCreator)
    {
        AttributeDescriptor attrDesc = ((AttributeCopyCreator)Objects.<AttributeCopyCreator>requireNonNull(attributeCopyCreator)).getDescriptor().getRealAttributeDescriptor();
        return getWorker().getMaster().getAttributeConfig(attrDesc);
    }


    protected GenericCatalogCopyContext.ItemCopyCacheEntry getCopy(Item source)
    {
        GenericCatalogCopyContext.ItemCopyCacheEntry ret = null;
        PK srcPK = source.getPK();
        SyncSchedule schedule = getCurrentSchedule();
        if(srcPK.equals(schedule.getSrcPK()))
        {
            if(schedule.getTgtPK() != null)
            {
                Item copy = toItem(schedule.getTgtPK());
                if(copy != null)
                {
                    ret = new GenericCatalogCopyContext.ItemCopyCacheEntry(source, copy, (ItemSyncTimestamp)toItem(schedule.getTimestampPK()));
                }
            }
        }
        else
        {
            PK cachedCopyPK = (PK)schedule.getParentMappings().get(srcPK);
            if(cachedCopyPK != null)
            {
                Item copy = toItem(cachedCopyPK);
                if(copy != null)
                {
                    ret = new GenericCatalogCopyContext.ItemCopyCacheEntry(source, copy, null);
                }
            }
        }
        return (ret != null) ? ret : super.getCopy(source);
    }


    protected ItemSyncTimestamp createSyncTimestamp(CatalogVersionSyncCronJob owningCronJob, int currentTurn, Item source, ItemCopyCreator icc)
    {
        return super.createSyncTimestamp(owningCronJob, currentTurn, source, icc);
    }


    public void delete(Item original) throws SynchronizationPersistenceException, RecoverableSynchronizationPersistenceException
    {
        getPersistenceAdapter().remove(original);
    }


    protected Item copy(ItemCopyCreator parent, Item original, Item copyToUpdate, ItemSyncTimestamp itemSyncTimestamp)
    {
        return super.copy(parent, original, copyToUpdate, itemSyncTimestamp);
    }


    public boolean isFromSourceVersion(Item item) throws JaloInvalidParameterException
    {
        return (super.isFromSourceVersion(item) || this.cv_map.containsKey(getCatalogVersion(item)));
    }


    protected CatalogVersion getTargetVersionForQueryCatalogItemCopy(Item sourceItem)
    {
        CatalogVersion dependentSrcVersion = getCatalogVersion(sourceItem);
        if(this.cv_map.containsKey(dependentSrcVersion))
        {
            return this.cv_map.get(dependentSrcVersion);
        }
        return super.getTargetVersionForQueryCatalogItemCopy(sourceItem);
    }
}
