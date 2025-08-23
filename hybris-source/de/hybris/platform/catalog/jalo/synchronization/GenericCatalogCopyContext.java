package de.hybris.platform.catalog.jalo.synchronization;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.SynchronizationPersistenceAdapter;
import de.hybris.platform.catalog.constants.CatalogConstants;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.CatalogItem;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.Keyword;
import de.hybris.platform.catalog.jalo.ProductFeature;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.catalog.synchronization.ServiceLayerSynchronizationPersistenceAdapter;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.LegacyFlagsUtils;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloConnection;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.media.Media;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.security.AccessManager;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.security.UserRight;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.CollectionType;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.MapType;
import de.hybris.platform.jalo.type.RelationDescriptor;
import de.hybris.platform.jalo.type.RelationType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.util.CSVUtils;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.ItemPropertyValue;
import de.hybris.platform.util.JaloPropertyContainer;
import de.hybris.platform.util.collections.YFastFIFOMap;
import de.hybris.platform.util.collections.fast.YLongToByteMap;
import de.hybris.platform.util.collections.fast.YLongToObjectMap;
import de.hybris.platform.util.collections.fast.procedures.YLongProcedure;
import de.hybris.platform.variants.jalo.VariantProduct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.log4j.Level;

public abstract class GenericCatalogCopyContext extends AbstractItemCopyContext
{
    private final Set<String> creationRequiredAttributes = new HashSet<>();
    public static final String LEGACY_MODE_FLAG = "synchronization.legacy.mode";
    public static final String PARTIAL_COLLECTION_TRANSLATION_ALLOWED = "synchronization.allow.partial.collection.translation";
    private static final String NO_CV_ATTRIBUTE = "<...>";
    private YLongToObjectMap<String> _catalogVersionAttributeCache;
    private YLongToObjectMap<Set<String>> _catalogKeyAttributesCache;
    private YLongToObjectMap<Set<String>> _nonCatalogKeyAttributesCache;
    private YLongToObjectMap<Set<ParentResolver>> _dependentItemResolverCache;
    private YLongToObjectMap<String> _rootCatalogItemTypeCache;
    private YLongToByteMap _isCopyOnDemandCache;
    private Map<PK, ItemCopyCacheEntry> _copiedItemCache;
    private static final byte TRUE_BYTE = 1;
    private static final byte FALSE_BYTE = -1;
    private YLongToByteMap _isCatalogTypeCache;
    private static final int MIN_COPY_CACHESIZE = 5000;
    private int copyCacheSize;
    private long copyCacheHits = 0L;
    private long copyCacheMisses = 0L;
    private final boolean forceUpdate;
    private boolean started = false;
    private boolean savePreviousValues = false;
    private YLongToObjectMap<CatalogItemCopyCreator> _currentlyCopying = new YLongToObjectMap();
    private final CatalogVersionSyncJob job;
    private final boolean exclusiveMode;
    private final CatalogVersionSyncCronJob cronjob;
    private final CatalogVersion sourceVersion;
    private final CatalogVersion targetVersion;
    private Set<Language> targetLanguages;
    private boolean copyCategoryProducts = false;
    private boolean copyCategorySubcategories = false;
    private final SynchronizationPersistenceAdapter persistenceAdapter;
    private final ItemCopyCreator.AfterItemCopiedCallback afterItemCopiedCallback;


    public GenericCatalogCopyContext(SessionContext ctx, CatalogVersionSyncJob job, CatalogVersionSyncCronJob cronJob, Level logLevel, boolean forceUpdate)
    {
        super(ctx, logLevel);
        this.afterItemCopiedCallback = (ItemCopyCreator.AfterItemCopiedCallback)new Object(this);
        this.forceUpdate = forceUpdate;
        for(AttributeDescriptor ad : getCreationAttributes())
        {
            addCreationAttribute(ad);
        }
        this.ctx = ctx;
        this.cronjob = cronJob;
        this.job = job;
        this.exclusiveMode = job.isExclusiveModeAsPrimitive();
        this.sourceVersion = this.job.getSourceVersion();
        this.targetVersion = this.job.getTargetVersion();
        setCopyCacheSize(Math.max(this.job.getCopyCacheSizeAsPrimitive(), 5000));
        setTargetLanguages(this.job.getSyncLanguages(ctx));
        this.persistenceAdapter = initPersistenceAdapter();
    }


    protected SynchronizationPersistenceAdapter initPersistenceAdapter()
    {
        if(LegacyFlagsUtils.isLegacyFlagEnabled(LegacyFlagsUtils.LegacyFlag.SYNC))
        {
            return (SynchronizationPersistenceAdapter)new LegacySynchronizationPersistenceAdapter(this);
        }
        return (SynchronizationPersistenceAdapter)new ServiceLayerSynchronizationPersistenceAdapter(this);
    }


    SynchronizationPersistenceAdapter getPersistenceAdapter()
    {
        return this.persistenceAdapter;
    }


    protected void setTargetLanguages(Collection<Language> languages)
    {
        if(languages != null && !languages.isEmpty())
        {
            this.targetLanguages = Collections.unmodifiableSet(new HashSet<>(languages));
        }
        else
        {
            Set<Language> set = new HashSet<>(getSourceVersion().getLanguages());
            set.retainAll(getTargetVersion().getLanguages());
            this.targetLanguages = Collections.unmodifiableSet(set);
        }
    }


    protected YLongToByteMap getIsCatalogTypeCache()
    {
        if(this._isCatalogTypeCache == null)
        {
            this._isCatalogTypeCache = new YLongToByteMap();
        }
        return this._isCatalogTypeCache;
    }


    public Set<ComposedType> getDependentItemTypes()
    {
        YLongToObjectMap<Set<ParentResolver>> cache = getDependentItemResolverCache();
        Set<ComposedType> ret = new HashSet<>(cache.size());
        cache.forEachKey((YLongProcedure)new Object(this, ret));
        return ret;
    }


    protected YLongToObjectMap<Set<ParentResolver>> getDependentItemResolverCache()
    {
        if(this._dependentItemResolverCache == null)
        {
            synchronized(this)
            {
                if(this._dependentItemResolverCache == null)
                {
                    this._dependentItemResolverCache = computeDependentItemResolvers();
                }
            }
        }
        return this._dependentItemResolverCache;
    }


    protected boolean mayBeCatalogItemDependent(Item item)
    {
        return mayBeCatalogItemDependent(item.getComposedType());
    }


    protected boolean mayBeCatalogItemDependent(ComposedType composedType)
    {
        long ctPK = composedType.getPK().getLongValue();
        Set<ParentResolver> resolvers = (Set<ParentResolver>)getDependentItemResolverCache().get(ctPK);
        return (resolvers != null && !resolvers.isEmpty());
    }


    protected boolean isCatalogItem(Item item)
    {
        return isCatalogItemType(item.getComposedType());
    }


    protected boolean isCatalogItemType(ComposedType composedType)
    {
        long ctPK = composedType.getPK().getLongValue();
        YLongToByteMap catalogCache = getIsCatalogTypeCache();
        byte byteCatalogTypePK = catalogCache.get(ctPK);
        if(byteCatalogTypePK == catalogCache.getEmptyValue())
        {
            getIsCatalogTypeCache().put(ctPK, byteCatalogTypePK = getCatalogManager().isCatalogItem(composedType) ? 1 : -1);
        }
        return (byteCatalogTypePK == 1);
    }


    public void setCategoryOptions(boolean copyProducts, boolean copySubcategories)
    {
        if(isStarted())
        {
            throw new IllegalStateException("cannot change category options after copying started");
        }
        this.copyCategoryProducts = copyProducts;
        this.copyCategorySubcategories = copySubcategories;
    }


    public final CatalogVersion getSourceVersion()
    {
        return this.sourceVersion;
    }


    public final CatalogVersion getTargetVersion()
    {
        return this.targetVersion;
    }


    protected CatalogVersionSyncJob getJob()
    {
        return this.job;
    }


    public boolean isExclusiveMode()
    {
        return this.exclusiveMode;
    }


    protected CatalogVersionSyncCronJob getCronjob()
    {
        return this.cronjob;
    }


    protected abstract int getCurrentTurn();


    public boolean isFromSourceVersion(Item item) throws JaloInvalidParameterException
    {
        return getSourceVersion().equals(getCatalogVersion(item));
    }


    protected CatalogVersion getCatalogVersion(Item item) throws JaloInvalidParameterException
    {
        if(item == null)
        {
            return null;
        }
        if(item instanceof CatalogItem)
        {
            return ((CatalogItem)item).getCatalogVersion(getCtx());
        }
        if(item instanceof Product)
        {
            return getCatalogManager().getCatalogVersion((Product)item);
        }
        if(item instanceof Media)
        {
            return getCatalogManager().getCatalogVersion((Media)item);
        }
        if(item instanceof Category)
        {
            return getCatalogManager().getCatalogVersion((Category)item);
        }
        if(isCatalogItemType(item.getComposedType()))
        {
            String versionAttr = getCatalogVersionAttribute(item.getComposedType());
            if(versionAttr != null)
            {
                try
                {
                    return (CatalogVersion)item.getAttribute(getCtx(), versionAttr);
                }
                catch(Exception e)
                {
                    if(e instanceof RuntimeException)
                    {
                        throw (RuntimeException)e;
                    }
                    if(e instanceof JaloInvalidParameterException)
                    {
                        throw (JaloInvalidParameterException)e;
                    }
                    throw new JaloSystemException(e);
                }
            }
            throw new JaloInvalidParameterException("catalog item " + item + " (type " + item.getComposedType().getCode() + ") has no version attribute", 0);
        }
        return null;
    }


    protected Object translate(AttributeCopyCreator acc)
    {
        Object<Language, V> translated;
        Map<Language, Object> map;
        if(isNotReference(acc) || acc.isPreset())
        {
            translated = (Object<Language, V>)acc.getSourceValue();
            if(isDebugEnabled())
            {
                debug("left value for " + acc + " unchanged");
            }
        }
        else
        {
            translated = (Object<Language, V>)translateValue(acc, acc.getSourceValue(), acc.getDescriptor().isPartOf() ? copyPartOfValues(acc) : null, acc.getDescriptor().isLocalized());
            if(isDebugEnabled())
            {
                debug("translated " + acc + " into " + translated);
            }
        }
        ItemCopyCreator itemCopyCreator = acc.getParent();
        if(GeneratedCatalogConstants.Attributes.Product.FEATURES.equalsIgnoreCase(acc.getDescriptor().getQualifier()) && itemCopyCreator.isUpdate() && itemCopyCreator.getSourceItem() instanceof Product)
        {
            List<ProductFeature> toPreserve = getCatalogManager().getFeatures(getCtx(), (Product)itemCopyCreator.getTargetItem());
            if(CollectionUtils.isNotEmpty(toPreserve))
            {
                for(ProductFeature existingFeature : toPreserve)
                {
                    if(isUntranslatableProductFeature(existingFeature))
                    {
                        if(((Collection)translated).isEmpty())
                        {
                            translated = (Object<Language, V>)new ArrayList(toPreserve.size());
                        }
                        ((Collection<ProductFeature>)translated).add(existingFeature);
                    }
                }
            }
        }
        if(!acc.isPreset() && acc.getDescriptor().isLocalized())
        {
            Set<Language> languages = getTargetLanguages();
            if(languages.isEmpty())
            {
                translated = (Object<Language, V>)Collections.EMPTY_MAP;
            }
            else if(translated == null || ((Map)translated).size() != languages.size() || !((Map)translated).keySet().equals(languages))
            {
                if(languages.size() == 1)
                {
                    Language language = languages.iterator().next();
                    translated = Collections.singletonMap(language, (translated != null) ? (V)((Map)translated).get(language) : null);
                }
                else
                {
                    Map src = (Map)translated;
                    Map<Language, Object> map1 = new HashMap<>(languages.size());
                    for(Language l : languages)
                    {
                        map1.put(l, (src != null) ? src.get(l) : null);
                    }
                    map = map1;
                }
            }
        }
        return map;
    }


    private boolean isNotReference(AttributeCopyCreator acc)
    {
        if(acc.getSourceValue() instanceof Item)
        {
            return false;
        }
        return acc.getDescriptor().isAtomic();
    }


    protected void finishedCopying(ItemCopyCreator icc)
    {
        if(icc.getSourceItem() instanceof Media)
        {
            Media mSrc = (Media)icc.getSourceItem();
            if(mSrc.hasData())
            {
                copyMediaData(mSrc, (Media)icc.getTargetItem());
            }
            List<UserRight> rights = Collections.singletonList(AccessManager.getInstance().getUserRightByCode("read"));
            Map permissionMap = icc.getSourceItem().getPermissionMap(rights);
            if(MapUtils.isNotEmpty(permissionMap))
            {
                try
                {
                    icc.getTargetItem().setPermissionsByMap(rights, permissionMap);
                }
                catch(JaloSecurityException e)
                {
                    error("Setting permissions failed..." + e);
                }
            }
        }
        if(isSavePrevousValues())
        {
            logSavedValues(icc);
        }
        if(isDebugEnabled())
        {
            debug(icc.getReport());
        }
        ItemSyncTimestamp itemSyncTimestamp = setTimestamp(getCronjob(), getCurrentTurn(), (CatalogItemCopyCreator)icc);
        registerCopy(new ItemCopyCacheEntry(icc.getSourceItem(), icc.getTargetItem(), itemSyncTimestamp));
    }


    protected CatalogItemCopyCreator createCreator(ItemCopyCreator parent, Item source, Item target, ItemSyncTimestamp itemSyncTimestamp, Collection<String> blacklist, Collection<String> whiteList, Map<String, Object> presetValues)
    {
        return new CatalogItemCopyCreator(this, parent, source, target, itemSyncTimestamp, blacklist, whiteList, presetValues);
    }


    protected ItemSyncTimestamp setTimestamp(CatalogVersionSyncCronJob owningCronJob, int currentTurn, CatalogItemCopyCreator icc)
    {
        ItemSyncTimestamp itemSyncTimestamp = null;
        if(icc.getTargetItem() != null)
        {
            Item src = icc.getSourceItem();
            itemSyncTimestamp = (icc.getSyncTimestamp() != null) ? icc.getSyncTimestamp() : queryExistingSyncTimestamp(src, icc.getTargetItem().getPK());
            if(itemSyncTimestamp != null)
            {
                updateSyncTimestamp(itemSyncTimestamp, owningCronJob, currentTurn, src, (ItemCopyCreator)icc);
            }
            else
            {
                itemSyncTimestamp = createSyncTimestamp(owningCronJob, currentTurn, src, (ItemCopyCreator)icc);
            }
            icc.setSyncTimestamp(itemSyncTimestamp);
        }
        return itemSyncTimestamp;
    }


    protected void updateSyncTimestamp(ItemSyncTimestamp itemSyncTimestamp, CatalogVersionSyncCronJob owningCronJob, int currentTurn, Item source, ItemCopyCreator icc)
    {
        JaloPropertyContainer cont = getSession().createPropertyContainer();
        if(icc.hasPendingAttributes())
        {
            cont.setProperty("lastSyncSourceModifiedTime", new Date(0L));
            cont.setProperty("pendingAttributeQualifiers", CSVUtils.joinAndEscape(new ArrayList(icc.getPendingAttributeQualifiers()), SyncSchedule.ESCAPE, ',', true));
            cont.setProperty("pendingAttributesOwnerJob", owningCronJob);
            cont.setProperty("pendingAttributesScheduledTurn", Integer.valueOf(currentTurn + 1));
        }
        else
        {
            cont.setProperty("pendingAttributeQualifiers", null);
            cont.setProperty("pendingAttributesOwnerJob", null);
            cont.setProperty("pendingAttributesScheduledTurn", null);
            Date modTS = source.getModificationTime();
            if(modTS == null)
            {
                modTS = source.getCreationTime();
            }
            cont.setProperty("lastSyncSourceModifiedTime", modTS);
        }
        cont.setProperty("lastSyncTime", new Date());
        try
        {
            itemSyncTimestamp.setAllProperties(getCtx(), cont);
        }
        catch(ConsistencyCheckException e)
        {
            throw new IllegalStateException("cannot update sync timestamp " + itemSyncTimestamp, e);
        }
    }


    protected ItemSyncTimestamp createSyncTimestamp(CatalogVersionSyncCronJob owningCronJob, int currentTurn, Item source, ItemCopyCreator icc)
    {
        Item copy = icc.getTargetItem();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("sourceItem", source);
        attributes.put("targetItem", (copy != null) ? new ItemPropertyValue(copy.getPK()) : null);
        if(isExclusiveMode())
        {
            attributes.put("syncJob", getJob());
        }
        else
        {
            attributes.put("sourceVersion", getSourceVersion());
            attributes.put("targetVersion", getTargetVersion());
        }
        attributes.put("lastSyncTime", new Date());
        if(icc.hasPendingAttributes())
        {
            attributes.put("pendingAttributeQualifiers", CSVUtils.joinAndEscape(new ArrayList(icc.getPendingAttributeQualifiers()), SyncSchedule.ESCAPE, ',', true));
            attributes.put("pendingAttributesOwnerJob", owningCronJob);
            attributes.put("pendingAttributesScheduledTurn", Integer.valueOf(currentTurn + 1));
            attributes.put("lastSyncSourceModifiedTime", new Date(0L));
        }
        else
        {
            Date modTS = source.getModificationTime();
            if(modTS == null)
            {
                modTS = source.getCreationTime();
            }
            attributes.put("lastSyncSourceModifiedTime", modTS);
        }
        return getCatalogManager().createItemSyncTimestamp(attributes);
    }


    protected ItemSyncTimestamp queryExistingSyncTimestamp(Item src, PK tgtPK)
    {
        Map<PK, ItemSyncTimestamp> timestampsForSourceItem = new HashMap<>(getCatalogManager().getSyncTimestampMap(src, (SyncItemJob)getJob()));
        ItemSyncTimestamp existingOneToUpdate = timestampsForSourceItem.remove(tgtPK);
        if(Config.getBoolean("synchronization.support.for.stale.sync.ts.enabled", true))
        {
            existingOneToUpdate = handleStale(src, tgtPK, timestampsForSourceItem, existingOneToUpdate);
        }
        if(!timestampsForSourceItem.isEmpty() && isErrorEnabled())
        {
            error("found ambigous sync timestamps for " + src.getPK() + " and job " + getJob().getCode() + " : expected target " + tgtPK + " but found " + timestampsForSourceItem.keySet());
        }
        return existingOneToUpdate;
    }


    protected ItemSyncTimestamp handleStale(Item src, PK tgtPK, Map<PK, ItemSyncTimestamp> all, ItemSyncTimestamp existingOneToUpdate)
    {
        ItemSyncTimestamp stale = all.remove(null);
        if(stale != null)
        {
            PK srcPK = src.getPK();
            if(existingOneToUpdate == null)
            {
                warn(String.format("Found stale sync timestamp %s (%s->NULL) - reusing as (%s->%s)...", new Object[] {stale.getPK(), srcPK, srcPK, tgtPK}));
                stale.setProperty("targetItem", tgtPK);
                existingOneToUpdate = stale;
            }
            else
            {
                warn(String.format("Found stale sync timestamp %s (%s->NULL) - removing ...", new Object[] {srcPK, srcPK}));
                try
                {
                    stale.remove();
                }
                catch(Exception e)
                {
                    warn("Error removing stale sync timestamp", e);
                }
            }
        }
        return existingOneToUpdate;
    }


    protected boolean mustBeTranslated(AttributeCopyCreator acc, Item original)
    {
        return (isCurrentlyCopying(original) || (isCatalogItem(original) && isFromSourceVersion(original)) || (mayBeCatalogItemDependent(original) && isDependentFromSourceVersion(original)));
    }


    protected boolean mustBeTranslated(String attributeQualifier, Item original)
    {
        return (isCurrentlyCopying(original) || (isCatalogItem(original) && isFromSourceVersion(original)) || (mayBeCatalogItemDependent(original) && isDependentFromSourceVersion(original)));
    }


    protected YLongToObjectMap<Set<ParentResolver>> computeDependentItemResolvers()
    {
        YLongToObjectMap<Set<ParentResolver>> ret = new YLongToObjectMap(300);
        Set<ComposedType> ctControlsSet = new HashSet<>(300);
        Set<AttributeDescriptor> adControlSet = new HashSet<>(2000);
        for(ComposedType ct : getCatalogManager().getAllCatalogItemTypes())
        {
            for(ParentResolver r : computeParentResolvers(ct, ctControlsSet, adControlSet))
            {
                if(isDebugEnabled())
                {
                    debug("dependent item resolver " + r);
                }
                ComposedType composedType = r.getDependentItemType();
                Set<ParentResolver> set = (Set<ParentResolver>)ret.get(composedType.getPK().getLongValue());
                if(set == null)
                {
                    ret.put(composedType.getPK().getLongValue(), set = new LinkedHashSet<>(5));
                }
                set.add(r);
                for(ComposedType subDt : composedType.getAllSubTypes())
                {
                    set = (Set<ParentResolver>)ret.get(subDt.getPK().getLongValue());
                    if(set == null)
                    {
                        ret.put(subDt.getPK().getLongValue(), set = new LinkedHashSet<>(5));
                    }
                    set.add(r);
                }
            }
        }
        return ret;
    }


    protected Collection<ParentResolver> computeParentResolvers(ComposedType parentType, Set<ComposedType> ctControlSet, Set<AttributeDescriptor> adControlSet)
    {
        if(ctControlSet.contains(parentType))
        {
            return Collections.EMPTY_SET;
        }
        ctControlSet.add(parentType);
        Collection<ParentResolver> ret = new LinkedHashSet<>();
        for(AttributeDescriptor myAd : parentType.getAttributeDescriptorsIncludingPrivate())
        {
            int modifiers = myAd.getModifiers();
            if((modifiers & 0x802) != 0 && isPartOf(myAd))
            {
                ComposedType nested;
                if(adControlSet.contains(myAd))
                {
                    continue;
                }
                AttributeDescriptor attributeDescriptor = myAd.isInherited() ? myAd.getDeclaringEnclosingType().getAttributeDescriptorIncludingPrivate(myAd.getQualifier()) : myAd;
                if(adControlSet.contains(attributeDescriptor))
                {
                    continue;
                }
                adControlSet.add(myAd);
                adControlSet.add(attributeDescriptor);
                Type attrType = attributeDescriptor.getRealAttributeType();
                if(attrType instanceof ComposedType)
                {
                    nested = (ComposedType)attrType;
                }
                else if(attrType instanceof CollectionType && ((CollectionType)attrType).getElementType() instanceof ComposedType)
                {
                    nested = (ComposedType)((CollectionType)attrType).getElementType();
                }
                else if(attrType instanceof MapType && ((MapType)attrType).getReturnType() instanceof ComposedType)
                {
                    nested = (ComposedType)((MapType)attrType).getReturnType();
                }
                else
                {
                    nested = null;
                }
                if(nested != null)
                {
                    if(isCatalogItemType(nested))
                    {
                        if(isDebugEnabled())
                        {
                            debug("skipped partOf attribute " + safeToString(attributeDescriptor) + " since it already contains catalog items");
                        }
                        continue;
                    }
                    if(attributeDescriptor instanceof RelationDescriptor)
                    {
                        RelationDescriptor relationDescriptor = (RelationDescriptor)attributeDescriptor;
                        RelationType relationType = relationDescriptor.getRelationType();
                        if(relationType.isOneToMany())
                        {
                            ret.add(new ReferenceParentResolver(this, relationDescriptor.isSource() ? relationType.getTargetType() : relationType.getSourceType(), attributeDescriptor, parentType, relationDescriptor.isSource() ? relationType.getTargetTypeRole() : relationType.getSourceTypeRole()));
                            ret.addAll(computeParentResolvers(nested, ctControlSet, adControlSet));
                            continue;
                        }
                        if(isDebugEnabled())
                        {
                            debug("skipped partOf attribute " + safeToString(attributeDescriptor) + " since it is n-m relation attribute");
                        }
                        continue;
                    }
                    if(attributeDescriptor.isProperty() || attributeDescriptor.getDatabaseColumn() != null)
                    {
                        if(attrType instanceof ComposedType)
                        {
                            ret.add(new ReverseReferenceParentResolver(this, (ComposedType)attrType, attributeDescriptor, parentType, attributeDescriptor.getQualifier(), attributeDescriptor.isLocalized()));
                            ret.addAll(computeParentResolvers((ComposedType)attrType, ctControlSet, adControlSet));
                            continue;
                        }
                        if(isDebugEnabled())
                        {
                            debug("skipped partOf property attribute " + safeToString(attributeDescriptor) + " since it does not contain a single item but " + safeToString(attrType));
                        }
                        continue;
                    }
                    if(isDebugEnabled())
                    {
                        debug("skipped unsupported partOf attribute " + safeToString(attributeDescriptor));
                    }
                    continue;
                }
                if(isDebugEnabled())
                {
                    debug("skipped partOf attribute " + safeToString(attributeDescriptor) + " since it does not contain items");
                }
            }
        }
        return ret;
    }


    public boolean isDependentFromSourceVersion(Item dependentItem)
    {
        return getSourceVersion().equals(resolveDependentCatalogversion(dependentItem));
    }


    private final CatalogVersion resolveDependentCatalogversion(Item toCheck)
    {
        CatalogVersion catalogVersion = resolveDependentCatalogversionInternal(toCheck, null);
        if(isDebugEnabled())
        {
            debug("found catalog version " + safeToString(catalogVersion) + " of dependent item " + safeToString(toCheck));
        }
        return catalogVersion;
    }


    private final CatalogVersion resolveDependentCatalogversionInternal(Item toCheck, Set<Item> controlSet)
    {
        if(controlSet != null && controlSet.contains(toCheck))
        {
            warn("detected cyclic partOf relation between " + safeToString(controlSet) + " - skipped ");
            return null;
        }
        CatalogVersion ret = null;
        Set<ParentResolver> resolvers = (Set<ParentResolver>)getDependentItemResolverCache().get(toCheck.getComposedType().getPK().getLongValue());
        if(resolvers != null)
        {
            for(ParentResolver pr : resolvers)
            {
                Item parent = pr.getParent(toCheck);
                if(parent != null)
                {
                    if(isCatalogItem(parent))
                    {
                        String cvAttr = getCatalogVersionAttribute(parent.getComposedType());
                        if(cvAttr != null)
                        {
                            try
                            {
                                ret = (CatalogVersion)parent.getAttribute(getCtx(), cvAttr);
                                break;
                            }
                            catch(ClassCastException e)
                            {
                                error("cannot read catalog version attribute '" + cvAttr + "' from " + parent + " due to " + e.getMessage() + " - ignored");
                            }
                            catch(JaloInvalidParameterException e)
                            {
                                error("cannot read catalog version attribute '" + cvAttr + "' from " + parent + " due to " + e.getMessage() + " - ignored");
                            }
                            catch(JaloSecurityException e)
                            {
                                error("cannot read catalog version attribute '" + cvAttr + "' from " + parent + " due to " + e.getMessage() + " - ignored");
                            }
                        }
                    }
                    else
                    {
                        Set<Item> ctrSet = (controlSet != null) ? controlSet : new HashSet<>();
                        ctrSet.add(toCheck);
                        ret = resolveDependentCatalogversionInternal(parent, ctrSet);
                    }
                }
                if(ret != null)
                {
                    break;
                }
            }
        }
        return ret;
    }


    protected Map getCopyCreatorPresetValues(Item original, Item copyToupdate)
    {
        return Collections.EMPTY_MAP;
    }


    protected YLongToObjectMap<String> getRootCatalogTypeCache()
    {
        if(this._rootCatalogItemTypeCache == null)
        {
            this._rootCatalogItemTypeCache = new YLongToObjectMap(300);
        }
        return this._rootCatalogItemTypeCache;
    }


    protected YLongToObjectMap<String> getCatalogVersionAttributeCache()
    {
        if(this._catalogVersionAttributeCache == null)
        {
            this._catalogVersionAttributeCache = new YLongToObjectMap(300);
        }
        return this._catalogVersionAttributeCache;
    }


    protected YLongToObjectMap<Set<String>> getCatalogKeyAttributesCache()
    {
        if(this._catalogKeyAttributesCache == null)
        {
            this._catalogKeyAttributesCache = new YLongToObjectMap(300);
        }
        return this._catalogKeyAttributesCache;
    }


    protected YLongToObjectMap<Set<String>> getNonCatalogKeyAttributesCache()
    {
        if(this._nonCatalogKeyAttributesCache == null)
        {
            this._nonCatalogKeyAttributesCache = new YLongToObjectMap(300);
        }
        return this._nonCatalogKeyAttributesCache;
    }


    protected YLongToByteMap getCopyOnDemandCache()
    {
        if(this._isCopyOnDemandCache == null)
        {
            this._isCopyOnDemandCache = new YLongToByteMap(3000);
        }
        return this._isCopyOnDemandCache;
    }


    protected String getCatalogVersionAttribute(ComposedType composedType)
    {
        String ret = (String)getCatalogVersionAttributeCache().get(composedType.getPK().getLongValue());
        if(ret == null)
        {
            AttributeDescriptor attributeDescriptor = getCatalogManager().getCatalogVersionAttribute(getCtx(), composedType);
            ret = (attributeDescriptor != null) ? attributeDescriptor.getQualifier() : null;
            getCatalogVersionAttributeCache().put(composedType.getPK().getLongValue(), (ret != null) ? ret : "<...>");
        }
        return (ret != "<...>") ? ret : null;
    }


    protected String getRootCatalogType(ComposedType composedType)
    {
        String ret = (String)getRootCatalogTypeCache().get(composedType.getPK().getLongValue());
        if(ret == null)
        {
            ret = composedType.getCode();
            for(ComposedType parent = composedType.getSuperType(); parent != null && isCatalogItemType(parent); parent = parent.getSuperType())
            {
                ret = parent.getCode();
            }
            getRootCatalogTypeCache().put(composedType.getPK().getLongValue(), ret);
        }
        return ret;
    }


    protected String getRootUniqueItemType(ComposedType composedType, Set<String> uniqueAttributes)
    {
        String ret = (String)getRootCatalogTypeCache().get(composedType.getPK().getLongValue());
        if(ret == null)
        {
            ret = composedType.getCode();
            for(ComposedType parent = composedType.getSuperType(); parent != null; parent = parent.getSuperType())
            {
                Set<String> parentUniqueKeys = getNonCatalogItemUniqueAttributes(parent);
                if(!uniqueAttributes.equals(parentUniqueKeys))
                {
                    break;
                }
                ret = parent.getCode();
            }
            getRootCatalogTypeCache().put(composedType.getPK().getLongValue(), ret);
        }
        return ret;
    }


    protected Set<String> getUniqueKeyAttributes(ComposedType composedType)
    {
        YLongToObjectMap<Set<String>> cache = getCatalogKeyAttributesCache();
        Set<String> ret = (Set<String>)cache.get(composedType.getPK().getLongValue());
        if(ret == null)
        {
            ret = new HashSet<>(getCatalogManager().getUniqueKeyAttributeQualifiers(composedType));
            cache.put(composedType.getPK().getLongValue(), ret);
        }
        else if(ret.isEmpty())
        {
            ret = new HashSet<>(getCatalogManager().getUniqueKeyAttributeQualifiers(composedType));
            cache.put(composedType.getPK().getLongValue(), ret);
        }
        return ret;
    }


    protected Set<String> getNonCatalogItemUniqueAttributes(ComposedType composedType)
    {
        YLongToObjectMap<Set<String>> cache = getNonCatalogKeyAttributesCache();
        Set<String> ret = (Set<String>)cache.get(composedType.getPK().getLongValue());
        if(ret == null)
        {
            ret = new HashSet<>(3);
            for(AttributeDescriptor ad : composedType.getAttributeDescriptorsIncludingPrivate())
            {
                if(ad.isUnique())
                {
                    ret.add(ad.getQualifier());
                }
            }
            cache.put(composedType.getPK().getLongValue(), ret);
        }
        return ret;
    }


    protected boolean isUntranslatablePartOfItem(AttributeCopyCreator acc, Item original)
    {
        boolean ret = false;
        String qualifier = acc.getDescriptor().getQualifier();
        if(original instanceof ProductFeature && GeneratedCatalogConstants.Attributes.Product.FEATURES.equalsIgnoreCase(qualifier))
        {
            ret = isUntranslatableProductFeature((ProductFeature)original);
        }
        if(isDebugEnabled())
        {
            debug("isUntranslatable( " + acc + "," + original + ") q = " + qualifier + " -> " + ret);
        }
        return ret;
    }


    protected boolean isUntranslatableProductFeature(ProductFeature productFeature)
    {
        Language language = productFeature.getLanguage();
        return (language != null && !getTargetLanguages().contains(language));
    }


    protected final boolean isCopyOnDemand(AttributeDescriptor attributeDescriptor)
    {
        long key = attributeDescriptor.getPK().getLongValue();
        YLongToByteMap copyOnDemandCache = getCopyOnDemandCache();
        byte bytePK = copyOnDemandCache.get(key);
        if(bytePK == copyOnDemandCache.getEmptyValue())
        {
            bytePK = computeIsCopyOnDemand(attributeDescriptor) ? 1 : -1;
            getCopyOnDemandCache().put(key, bytePK);
        }
        return (bytePK == 1);
    }


    protected boolean computeIsCopyOnDemand(AttributeDescriptor attributeDescriptor)
    {
        String qualifier = attributeDescriptor.getQualifier();
        if(this.copyCategorySubcategories && "categories".equals(qualifier))
        {
            Class<?> clazz = attributeDescriptor.getEnclosingType().getJaloClass();
            if(Category.class.isAssignableFrom(clazz))
            {
                return true;
            }
        }
        else if(this.copyCategoryProducts && "products".equals(qualifier))
        {
            Class<?> clazz = attributeDescriptor.getEnclosingType().getJaloClass();
            if(Category.class.isAssignableFrom(clazz))
            {
                return true;
            }
        }
        Boolean copyAllOnDemand = null;
        Set<ComposedType> catalogItemTypes = getContainedCatalogItemTypes(attributeDescriptor);
        for(ComposedType ct : catalogItemTypes)
        {
            boolean copyTypeOnDemand = isCopyOnDemandCatalogItemType(ct, ct.getJaloClass());
            if(copyAllOnDemand == null)
            {
                copyAllOnDemand = Boolean.valueOf(copyTypeOnDemand);
                continue;
            }
            if(copyAllOnDemand.booleanValue() != copyTypeOnDemand)
            {
                if(isWarnEnabled())
                {
                    warn("found attribute " + toString(attributeDescriptor) + " containing both copy-on-demand and referenced catalog item types " + safeToString(catalogItemTypes) + " - cannot copy");
                }
                return false;
            }
        }
        return (copyAllOnDemand != null && copyAllOnDemand.booleanValue());
    }


    protected boolean isCopyOnDemandCatalogItemType(ComposedType catalogItemType, Class<?> jaloClass)
    {
        return (Media.class.isAssignableFrom(jaloClass) || Keyword.class.isAssignableFrom(jaloClass));
    }


    protected final Set<ComposedType> getContainedCatalogItemTypes(AttributeDescriptor attributeDescriptor)
    {
        Type realType = attributeDescriptor.getRealAttributeType();
        if(realType instanceof de.hybris.platform.jalo.type.AtomicType || realType instanceof de.hybris.platform.jalo.enumeration.EnumerationType)
        {
            return Collections.EMPTY_SET;
        }
        Set<ComposedType> ret = new HashSet<>();
        collectContainedCatalogItems(realType, attributeDescriptor.isLocalized(), ret);
        return ret;
    }


    private final void collectContainedCatalogItems(Type type, boolean localized, Set<ComposedType> addTo)
    {
        if(type instanceof ComposedType)
        {
            addTo.add((ComposedType)type);
        }
        else if(type instanceof CollectionType)
        {
            collectContainedCatalogItems(((CollectionType)type).getElementType(), false, addTo);
        }
        else if(type instanceof MapType)
        {
            if(!localized)
            {
                collectContainedCatalogItems(((MapType)type).getArgumentType(), false, addTo);
            }
            collectContainedCatalogItems(((MapType)type).getReturnType(), false, addTo);
        }
    }


    protected final Map<PK, ItemCopyCacheEntry> getCopiedItemCache(boolean create)
    {
        return (this._copiedItemCache != null) ? this._copiedItemCache : (create ? (this._copiedItemCache = (Map<PK, ItemCopyCacheEntry>)new YFastFIFOMap(this.copyCacheSize)) : Collections.EMPTY_MAP);
    }


    protected ItemCopyCacheEntry getCopy(Item source)
    {
        PK srcPK = source.getPK();
        boolean doRegister = false;
        ItemCopyCacheEntry ret = getCopiedItemCache(false).get(srcPK);
        if(ret != null)
        {
            this.copyCacheHits++;
        }
        else
        {
            this.copyCacheMisses++;
        }
        if(ret == null && isCurrentlyCopying(source))
        {
            CatalogItemCopyCreator icc = getCurrentlyCopying(source);
            ret = new ItemCopyCacheEntry(source, icc.getTargetItem(), icc.getSyncTimestamp());
        }
        if(ret == null)
        {
            ret = queryCopy(source);
            doRegister = (ret != null);
        }
        if(doRegister)
        {
            registerCopy(ret);
            if(isDebugEnabled())
            {
                debug("found catalog item copy " + ret + " and registered mapping");
            }
        }
        return ret;
    }


    protected ItemCopyCacheEntry queryCopy(Item source)
    {
        if(isCatalogItem(source) && isFromSourceVersion(source))
        {
            return queryCatalogItemCopy(source);
        }
        return queryNonCatalogItemCopy(source);
    }


    protected ItemCopyCacheEntry queryCatalogItemCopy(Item sourceItem)
    {
        ComposedType composedType = sourceItem.getComposedType();
        String versionAD = getCatalogVersionAttribute(composedType);
        Set<String> keyAttributes = getUniqueKeyAttributes(composedType);
        if(keyAttributes == null || keyAttributes.isEmpty())
        {
            throw new JaloInvalidParameterException("no key attribute(s) defined for catalog item type " + composedType.getCode(), 0);
        }
        Map<String, Object> params = new HashMap<>();
        params.put("tgtVer", getTargetVersionForQueryCatalogItemCopy(sourceItem));
        if(isExclusiveMode())
        {
            params.put("job", getJob());
        }
        params.put("srcItem", sourceItem);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("SELECT x.PK FROM (");
        stringBuilder.append("{{ ");
        stringBuilder.append("SELECT {counterpart.").append(Item.PK).append("} AS PK ");
        stringBuilder.append("FROM {").append(getRootCatalogType(composedType)).append(" as counterpart");
        stringBuilder.append(" LEFT JOIN ").append(GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP).append(" as ts ");
        stringBuilder.append("ON {ts.targetItem} = {counterpart.PK} AND");
        if(!isExclusiveMode())
        {
            stringBuilder.append("{ts.").append("targetVersion").append("}=?tgtVer AND ");
        }
        stringBuilder.append("{ts.").append("syncJob").append("}").append(isExclusiveMode() ? " = ?job " : "=0 ").append("}");
        stringBuilder.append("WHERE ({ts.PK} IS NULL OR {ts.sourceItem}=?srcItem) AND ");
        stringBuilder.append("  {counterpart.").append(versionAD).append("}=?tgtVer ");
        List<Object> keys = new ArrayList();
        for(String keyAD : keyAttributes)
        {
            try
            {
                Object value = sourceItem.getAttribute(null, keyAD);
                stringBuilder.append(" AND {counterpart.").append(keyAD).append("}");
                if(value == null)
                {
                    stringBuilder.append(" IS NULL ");
                    keys.add(null);
                    continue;
                }
                String token = "counterpart." + keyAD + "KeyValue";
                stringBuilder.append("=?").append(token).append(" ");
                params.put(token, value);
                keys.add(value);
            }
            catch(Exception e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
        }
        stringBuilder.append("}} ");
        stringBuilder.append("UNION ALL ");
        stringBuilder.append("{{ ");
        stringBuilder.append("SELECT {").append(ItemSyncTimestamp.PK).append("} AS PK ");
        stringBuilder.append("FROM {").append(GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP).append("*} ");
        stringBuilder.append("WHERE ");
        stringBuilder.append("{").append("sourceItem").append("}=?srcItem AND ");
        if(!isExclusiveMode())
        {
            stringBuilder.append("{").append("targetVersion").append("}=?tgtVer AND ");
        }
        stringBuilder.append("{").append("syncJob").append("}").append(isExclusiveMode() ? " = ?job " : "=0 ");
        stringBuilder.append(" }} ) x");
        List<Item> rows = getFlexibleSearch().search(getCtx(), stringBuilder.toString(), params, Item.class).getResult();
        return processQueryResult(sourceItem, rows, keys);
    }


    protected ItemCopyCacheEntry queryNonCatalogItemCopy(Item source)
    {
        List<Item> timestampItems = queryForTimestampRows(source);
        if(timestampItems.size() == 1)
        {
            return processQueryResult(source, timestampItems, Collections.emptyList());
        }
        ArrayList<Object> keyValues = new ArrayList();
        List<Item> targetItems = queryForTargetItems(source, keyValues);
        if(targetItems.isEmpty())
        {
            return processQueryResult(source, timestampItems, Collections.emptyList());
        }
        ArrayList<Item> allItems = new ArrayList<>(timestampItems.size() + targetItems.size());
        allItems.addAll(targetItems);
        allItems.addAll(timestampItems);
        return processQueryResult(source, allItems, keyValues);
    }


    private List<Item> queryForTimestampRows(Item source)
    {
        String timestampQuery = "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "*}WHERE {syncJob}" + (isExclusiveMode() ? "=?job" : "=0") + " AND " + (isExclusiveMode() ? "" : "{targetVersion}=?tgtVer AND ") + "{sourceItem}=?srcItem";
        Map<String, Object> params = new HashMap<>();
        if(isExclusiveMode())
        {
            params.put("job", getJob());
        }
        params.put("srcItem", source);
        params.put("tgtVer", getTargetVersion());
        SearchResult<Item> searchResult = getFlexibleSearch().search(getCtx(), timestampQuery, params, ItemSyncTimestamp.class);
        return searchResult.getResult();
    }


    private List<Item> queryForTargetItems(Item source, List<Object> keyValues)
    {
        Preconditions.checkArgument(keyValues.isEmpty(), "keyValues must be empty");
        ComposedType composedType = source.getComposedType();
        Set<String> uniqueAttributes = getNonCatalogItemUniqueAttributes(composedType);
        if(uniqueAttributes.isEmpty())
        {
            return Collections.emptyList();
        }
        List<Object> keys = null;
        Map<String, Object> params = new HashMap<>();
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT {counterpart.").append(Item.PK).append("} AS PK ");
        queryBuilder.append("FROM {").append(getRootUniqueItemType(composedType, uniqueAttributes)).append(" AS counterpart");
        queryBuilder.append(" LEFT JOIN ").append(GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP).append(" as ts ");
        queryBuilder.append("ON {ts.targetItem} = {counterpart.PK}  AND");
        if(!isExclusiveMode())
        {
            queryBuilder.append("{ts.").append("targetVersion").append("}=?tgtVer AND ");
        }
        queryBuilder.append("{ts.").append("syncJob").append("}").append(isExclusiveMode() ? " = ?job " : "=0 ").append("}");
        queryBuilder.append("WHERE ({ts.PK} IS NULL OR {ts.sourceItem}=?srcItem)");
        keys = new ArrayList();
        params.put("srcItem", source);
        params.put("tgtVer", getTargetVersionForQueryCatalogItemCopy(source));
        if(isExclusiveMode())
        {
            params.put("job", getJob());
        }
        for(String keyAD : uniqueAttributes)
        {
            try
            {
                queryBuilder.append(" AND ");
                Object value = source.getAttribute(null, keyAD);
                if(value instanceof Item && mustBeTranslated(keyAD, (Item)value))
                {
                    ItemCopyCacheEntry copyEntry = getCopy((Item)value);
                    if(copyEntry == null)
                    {
                        return Collections.emptyList();
                    }
                    value = copyEntry.getCopy();
                }
                queryBuilder.append(" {counterpart.").append(keyAD).append("}");
                if(value == null)
                {
                    queryBuilder.append(" IS NULL ");
                    keys.add(null);
                    continue;
                }
                String token = keyAD + "KeyValue";
                queryBuilder.append("=?").append(token).append(" ");
                params.put(token, value);
                keys.add(value);
            }
            catch(Exception e)
            {
                throw new JaloInvalidParameterException(e, 0);
            }
        }
        List<Item> searchResult = getFlexibleSearch().search(getCtx(), queryBuilder.toString(), params, Item.class).getResult();
        keyValues.addAll(keys);
        return searchResult;
    }


    protected ItemCopyCacheEntry processQueryResult(Item sourceItem, List<Item> rows, List<Object> keys)
    {
        Item counterpartItem = null;
        Map<Item, ItemSyncTimestamp> timestampMap = null;
        for(Item i : rows)
        {
            if(i instanceof ItemSyncTimestamp)
            {
                ItemSyncTimestamp itemSyncTimestamp = (ItemSyncTimestamp)i;
                if(timestampMap == null)
                {
                    timestampMap = new LinkedHashMap<>();
                }
                timestampMap.put(itemSyncTimestamp.getTargetItem(), itemSyncTimestamp);
                continue;
            }
            if(counterpartItem != null)
            {
                error("found duplicate counterpart item for " + sourceItem.getPK() + ": got " + safeToString(counterpartItem) + " and found " + safeToString(i) + " - ignoring second match");
                continue;
            }
            counterpartItem = i;
        }
        Item copy = null;
        ItemSyncTimestamp copyTS = null;
        if(timestampMap != null && !timestampMap.isEmpty())
        {
            if(timestampMap.size() == 1)
            {
                Map.Entry<Item, ItemSyncTimestamp> map = timestampMap.entrySet().iterator().next();
                copyTS = map.getValue();
                copy = map.getKey();
                if(counterpartItem != null && !counterpartItem.equals(copy) && isWarnEnabled())
                {
                    warn("found ambigous catalog item " + sourceItem.getPK() + ": keys " + keys + " match " + safeToString(counterpartItem) + " but sync timestamp got " + map.getKey() + " - using timestamp!");
                }
            }
            else if(counterpartItem != null && timestampMap.containsKey(counterpartItem))
            {
                copyTS = timestampMap.get(counterpartItem);
                copy = counterpartItem;
            }
            else
            {
                for(Map.Entry<Item, ItemSyncTimestamp> map : timestampMap.entrySet())
                {
                    ItemSyncTimestamp itemSyncTimestamp = map.getValue();
                    if(copyTS == null || copyTS.getCreationTime().after(itemSyncTimestamp.getCreationTime()))
                    {
                        copyTS = itemSyncTimestamp;
                    }
                }
                copy = copyTS.getTargetItem();
                if(isErrorEnabled())
                {
                    error("found ambigous sync timestamps for " + safeToString(sourceItem) + " and job " + getJob().getCode() + " : chose " + safeToString(copyTS) + "->" + safeToString(copy) + " from " + safeToString(timestampMap));
                }
            }
        }
        else if(counterpartItem != null)
        {
            copy = counterpartItem;
        }
        return (copy != null) ? new ItemCopyCacheEntry(sourceItem, copy, copyTS) : null;
    }


    protected Item getCounterpartItem(Item original)
    {
        return getCatalogManager().getCounterpartItem(getCtx(), original, getTargetVersion());
    }


    protected void setSavePrevousValues(boolean setSaveToOn)
    {
        this.savePreviousValues = setSaveToOn;
    }


    public boolean isSavePrevousValues()
    {
        return this.savePreviousValues;
    }


    protected Set<AttributeDescriptor> getCreationAttributes()
    {
        Set<AttributeDescriptor> ret = new HashSet<>();
        TypeManager typeManager = TypeManager.getInstance();
        ComposedType composedType = typeManager.getComposedType(Product.class);
        ret.add(composedType.getAttributeDescriptorIncludingPrivate(GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION));
        ret.add(composedType.getAttributeDescriptorIncludingPrivate(GeneratedCatalogConstants.Attributes.Product.VARIANTTYPE));
        composedType = typeManager.getComposedType(VariantProduct.class);
        ret.add(composedType.getAttributeDescriptorIncludingPrivate("baseProduct"));
        composedType = typeManager.getComposedType(Media.class);
        ret.add(composedType.getAttributeDescriptorIncludingPrivate(GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION));
        composedType = typeManager.getComposedType(Category.class);
        ret.add(composedType.getAttributeDescriptorIncludingPrivate(CatalogConstants.Attributes.Category.CATALOGVERSION));
        composedType = typeManager.getComposedType(Keyword.class);
        ret.add(composedType.getAttributeDescriptorIncludingPrivate("catalogVersion"));
        return ret;
    }


    protected final String translateAD(AttributeDescriptor attributeDescriptor)
    {
        return attributeDescriptor.getDeclaringEnclosingType().getCode() + "." + attributeDescriptor.getDeclaringEnclosingType().getCode();
    }


    protected final void addCreationAttribute(AttributeDescriptor attributeDescriptor)
    {
        this.creationRequiredAttributes.add(translateAD(attributeDescriptor));
    }


    public final boolean isStarted()
    {
        return this.started;
    }


    protected AttributeCopyCreator createAttributeCopyCreator(ItemCopyCreator icc, AttributeCopyDescriptor acd, boolean ispreset, Object sourceValue)
    {
        if(isDebugEnabled())
        {
            debug("createAttributeCopyCreator( ad=" + acd + ", preset=" + ispreset + ", source=" + sourceValue);
        }
        return new AttributeCopyCreator(icc, acd, ispreset, sourceValue);
    }


    protected Set<Item> collectPartOfItemsToCopy(AttributeCopyCreator acc)
    {
        Set<Item> ret = null;
        Collection<Item> all = allItems(acc, acc.getSourceValue());
        for(Item poi : all)
        {
            if(isUntranslatablePartOfItem(acc, poi))
            {
                if(isDebugEnabled())
                {
                    debug("found untranslatable partOf item " + poi + " for attribute " + acc.getDescriptor().getQualifier() + " - will not copy");
                }
                continue;
            }
            if(isDebugEnabled())
            {
                debug("added translatable partOf item " + poi + " for attribute " + acc.getDescriptor().getQualifier());
            }
            if(ret == null)
            {
                ret = new LinkedHashSet<>(all.size());
            }
            ret.add(poi);
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    protected Map<Item, Item> copyPartOfValues(AttributeCopyCreator acc)
    {
        Map<Item, Item> ret = null;
        ItemCopyCreator parent = acc.getParent();
        for(Item poi : acc.getPartOfItemsToCopy())
        {
            Item copy;
            ItemCopyCreator itemCopyCreator = parent.getEnclosingCreatorFor(poi);
            if(itemCopyCreator != null)
            {
                copy = itemCopyCreator.getTargetItem();
            }
            else
            {
                copy = copy(parent, poi);
            }
            if(ret == null)
            {
                ret = new LinkedHashMap<>();
            }
            ret.put(poi, copy);
        }
        return (ret != null) ? ret : Collections.EMPTY_MAP;
    }


    protected final Object translateValue(AttributeCopyCreator acc, Object original, Map<Item, Item> partOfItemsToBeCopied, boolean localized)
    {
        if(original instanceof Collection)
        {
            Collection coll = (Collection)original;
            if(coll.isEmpty())
            {
                return coll;
            }
            Collection<Object> newOne = (Collection)createNewContainerInstance(coll.getClass());
            Collection<Object> nonTranslateable = null;
            for(Object element : coll)
            {
                Object translated = translateValue(acc, element, partOfItemsToBeCopied, false);
                if(translated != null || element == null)
                {
                    newOne.add(translated);
                    continue;
                }
                if(isDebugEnabled())
                {
                    if(nonTranslateable == null)
                    {
                        nonTranslateable = new ArrayList();
                    }
                    nonTranslateable.add(element);
                }
            }
            if(nonTranslateable != null && isDebugEnabled())
            {
                debug("incomplete translation of " + acc.getDescriptor() + " (original=" + safeToString(coll) + ", translated=" + safeToString(newOne) + ", untranslated=" + nonTranslateable + ")");
            }
            return newOne;
        }
        if(original instanceof Map)
        {
            Map map = (Map)original;
            if(map.isEmpty())
            {
                return map;
            }
            Map<Object, Object> newOne = (Map)createNewContainerInstance(map.getClass());
            Map<Object, Object> nonTranslateable = null;
            for(Iterator<Map.Entry> it = map.entrySet().iterator(); it.hasNext(); )
            {
                Map.Entry entry = it.next();
                Object key = entry.getKey();
                Object translatedKey = localized ? key : translateValue(acc, key, partOfItemsToBeCopied, false);
                if(translatedKey != null)
                {
                    Object value = entry.getValue();
                    Object translatedValue = translateValue(acc, value, partOfItemsToBeCopied, false);
                    newOne.put(translatedKey, translatedValue);
                    if(translatedValue == null && value != null)
                    {
                        if(isWarnEnabled())
                        {
                            if(nonTranslateable == null)
                            {
                                nonTranslateable = new HashMap<>();
                            }
                            nonTranslateable.put(translatedKey, value);
                        }
                    }
                    continue;
                }
                if(isWarnEnabled())
                {
                    if(nonTranslateable == null)
                    {
                        nonTranslateable = new HashMap<>();
                    }
                    nonTranslateable.put(key, entry.getValue());
                }
            }
            if(nonTranslateable != null && isDebugEnabled())
            {
                debug("incomplete translation of " + acc.getDescriptor() + " (original=" + safeToString(map) + ", translated=" + safeToString(newOne) + ", untranslated=" + nonTranslateable + ")");
            }
            return newOne;
        }
        if(original instanceof Item)
        {
            return translateItemValue(acc, (Item)original, partOfItemsToBeCopied);
        }
        return original;
    }


    protected Item translateItemValue(AttributeCopyCreator acc, Item original, Map<Item, Item> partOfItemsToBeCopied)
    {
        Item ret;
        ItemCopyCreator itemCopyCreator = acc.getParent().getEnclosingCreatorFor(original);
        if(itemCopyCreator != null)
        {
            ret = itemCopyCreator.getTargetItem();
        }
        else if(acc.getDescriptor().isPartOf())
        {
            if(partOfItemsToBeCopied.containsKey(original))
            {
                ret = partOfItemsToBeCopied.get(original);
                if(ret == null)
                {
                    acc.setPending();
                }
            }
            else
            {
                ret = null;
                if(isDebugEnabled())
                {
                    debug("skipped untranslatable partOf item " + safeToString(original) + " from " + acc);
                }
            }
        }
        else if(mustBeTranslated(acc, original))
        {
            ItemCopyCacheEntry copyInfo = getCopy(original);
            if(copyInfo != null && copyInfo.getCopy() != null)
            {
                if(isDebugEnabled())
                {
                    debug("found existing copy for non-partOf reference " + acc.getDescriptor() + ":" + copyInfo);
                }
                ret = copyInfo.getCopy();
            }
            else
            {
                if(isDebugEnabled())
                {
                    debug("cannot translate " + acc.getDescriptor() + " (value=" + original + ") - registering as pending attribute");
                }
                Item copyOnDemand = null;
                if(acc.getDescriptor().isCopyOnDemand())
                {
                    copyOnDemand = copy(acc.getParent(), original);
                }
                if(copyOnDemand == null)
                {
                    acc.setPending();
                    ret = null;
                }
                else
                {
                    ret = copyOnDemand;
                }
            }
        }
        else
        {
            if(isDebugEnabled())
            {
                debug(acc.getDescriptor().getQualifier() + ": found plain item reference " + acc.getDescriptor().getQualifier() + " which stays unchanged");
            }
            ret = original;
        }
        return ret;
    }


    private final Object createNewContainerInstance(Class<?> type)
    {
        if(List.class.isAssignableFrom(type))
        {
            return new ArrayList();
        }
        if(Set.class.isAssignableFrom(type))
        {
            return SortedSet.class.isAssignableFrom(type) ? new TreeSet() : new LinkedHashSet();
        }
        if(Map.class.isAssignableFrom(type))
        {
            return SortedMap.class.isAssignableFrom(type) ? new TreeMap<>() : new LinkedHashMap<>();
        }
        return new ArrayList();
    }


    protected final CatalogItemCopyCreator getCurrentlyCopying(Item original)
    {
        return (CatalogItemCopyCreator)this._currentlyCopying.get(original.getPK().getLongValue());
    }


    protected final boolean isCurrentlyCopying(Item original)
    {
        return this._currentlyCopying.containsKey(original.getPK().getLongValue());
    }


    protected final void pushCurrentlyCopying(CatalogItemCopyCreator icc)
    {
        this._currentlyCopying.put(icc.getSourceItem().getPK().getLongValue(), icc);
    }


    protected final void popCurrentlyCopying(CatalogItemCopyCreator icc)
    {
        this._currentlyCopying.remove(icc.getSourceItem().getPK().getLongValue());
    }


    protected final void registerCopy(ItemCopyCacheEntry copyInfo)
    {
        Transaction tx = Transaction.current();
        if(tx.isRunning())
        {
            tx.executeOnRollback((Transaction.TransactionAwareExecution)new Object(this, copyInfo));
        }
        getCopiedItemCache(true).put(copyInfo.getOriginal().getPK(), copyInfo);
    }


    protected void beforeCopying(CatalogItemCopyCreator icc)
    {
        pushCurrentlyCopying(icc);
        if(isDebugEnabled())
        {
            debug("starting to " + (icc.isUpdate() ? "update" : "copy") + " " + icc.getSourceItem().getPK());
        }
    }


    protected void afterCopying(Item copy, CatalogItemCopyCreator icc)
    {
        popCurrentlyCopying(icc);
        if(isDebugEnabled())
        {
            debug("ended " + (icc.isUpdate() ? "update" : "copy") + " " + icc.getSourceItem().getPK());
        }
    }


    protected void logSavedValues(ItemCopyCreator icc)
    {
        Item tgt = icc.getTargetItem();
        boolean wasUpdate = icc.isUpdate();
        if(wasUpdate)
        {
            Map[] values = icc.getModifiedValues();
            if(values != null)
            {
                JaloConnection.getInstance().logItemModification(tgt, values[1], values[0], false);
                icc.resetStoredValues();
            }
        }
        else
        {
            JaloConnection.getInstance().logItemCreation(tgt, icc.getStoredValues());
            icc.resetStoredValues();
        }
    }


    protected void copyMediaData(Media src, Media tgt)
    {
        try
        {
            long time1 = System.currentTimeMillis();
            tgt.setData(src);
            if(isDebugEnabled())
            {
                long time2 = System.currentTimeMillis();
                debug("copied media data from " + src.getPK() + "(" + src.getCode() + ") to " + tgt.getPK() + "(" + tgt.getCode() + ") in " + time2 - time1 + "ms");
            }
        }
        catch(Exception e)
        {
            if(isErrorEnabled())
            {
                error("could not copy data from " + src.getPK() + " to " + tgt.getPK() + " due to " + e.getLocalizedMessage());
            }
            try
            {
                tgt.setRealFileName(src.getRealFileName());
                tgt.setMime(src.getMime());
            }
            catch(Exception exception)
            {
            }
        }
    }


    public Item copy(Item original)
    {
        return copy(null, original, null, null);
    }


    public Item copy(Item original, Item existingCopy, ItemSyncTimestamp itemSyncTimestamp)
    {
        return copy(null, original, existingCopy, itemSyncTimestamp);
    }


    protected Item copy(ItemCopyCreator parent, Item original)
    {
        return copy(parent, original, null, null);
    }


    protected Item copy(ItemCopyCreator parent, Item original, Item copyToUpdate, ItemSyncTimestamp itemSyncTimestamp)
    {
        ItemCopyCacheEntry copyInfo;
        this.started = true;
        boolean partOf = (parent != null);
        if(isCurrentlyCopying(original))
        {
            throw new IllegalStateException("found existing copy creator for " + (partOf ? "partOf" : "") + " item " + original + (partOf ? (" within " + parent) : ""));
        }
        if(copyToUpdate == null)
        {
            copyInfo = getCopy(original);
        }
        else
        {
            copyInfo = new ItemCopyCacheEntry(original, copyToUpdate, itemSyncTimestamp);
        }
        if(copyInfo != null && !this.forceUpdate && copyInfo.isUpToDate())
        {
            if(isDebugEnabled())
            {
                debug("found finished copy " + copyInfo + " - no need to copy");
            }
            return copyInfo.getCopy();
        }
        if(copyInfo != null && copyInfo.isProcessedInTurn(getCronjob(), getCurrentTurn()))
        {
            if(isDebugEnabled())
            {
                debug("found processed copy " + copyInfo + " - won't copy now");
            }
            return copyInfo.getCopy();
        }
        Item actualCopyToUpdate = (copyInfo != null) ? copyInfo.getCopy() : null;
        CatalogItemCopyCreator icc = createCreator(parent, original, actualCopyToUpdate, (copyInfo != null) ? copyInfo.getTimestamp() : null, getCopyCreatorBlacklist(original, actualCopyToUpdate), getCopyCreatorWhitelist(original, actualCopyToUpdate), calculatePresets(original, actualCopyToUpdate));
        Item ret = null;
        beforeCopying(icc);
        try
        {
            icc.setAfterItemCopiedCallback(this.afterItemCopiedCallback);
            ret = (Item)icc.copy();
        }
        finally
        {
            afterCopying(ret, icc);
        }
        return ret;
    }


    private final Map<String, Object> calculatePresets(Item original, Item actualCopyToUpdate)
    {
        String versionAttribute;
        Map<String, Object> ret;
        if(actualCopyToUpdate == null)
        {
            ComposedType composedType = original.getComposedType();
            if(isCatalogItemType(composedType))
            {
                versionAttribute = getCatalogVersionAttribute(composedType);
                if(versionAttribute == null)
                {
                    error("found catalog item type '" + safeToString(composedType) + "' without version attribute - skipping item " +
                                    safeToString(original));
                    return null;
                }
            }
            else
            {
                versionAttribute = null;
            }
        }
        else
        {
            versionAttribute = null;
        }
        Map<String, Object> customPreset = getCopyCreatorPresetValues(original, actualCopyToUpdate);
        if(versionAttribute != null)
        {
            if(customPreset != null && !customPreset.isEmpty())
            {
                ret = new HashMap<>();
                ret.putAll(customPreset);
                ret.put(versionAttribute, getTargetVersion());
            }
            else
            {
                ret = Collections.singletonMap(versionAttribute, getTargetVersion());
            }
        }
        else if(customPreset != null && !customPreset.isEmpty())
        {
            ret = customPreset;
        }
        else
        {
            ret = Collections.EMPTY_MAP;
        }
        return ret;
    }


    protected Set getCopyCreatorBlacklist(Item original, Item toUpdate)
    {
        Set<String> blacklist = new HashSet<>();
        blacklist.add(Item.MODIFIED_TIME);
        if(original instanceof Media && ((Media)original).hasData())
        {
            if(isDebugEnabled())
            {
                debug("found media " + ((Media)original).getCode() + " with data");
            }
            blacklist.add("dataPK");
            blacklist.add("size");
            blacklist.add("url");
            blacklist.add("internalURL");
            blacklist.add("url2");
            blacklist.add("location");
            blacklist.add("locationHash");
            blacklist.add("realFileName");
            blacklist.add("mime");
        }
        ComposedType composedType = null;
        if(toUpdate != null && isCatalogItemType(composedType = original.getComposedType()))
        {
            String catalogVersionAttribute = getCatalogVersionAttribute(composedType);
            if(catalogVersionAttribute == null)
            {
                error("found catalog item type '" + safeToString(composedType) + "' without version attribute for item " +
                                safeToString(original) + " - ignored");
            }
            else
            {
                blacklist.add(catalogVersionAttribute);
            }
        }
        return blacklist;
    }


    protected Set getCopyCreatorWhitelist(Item original, Item toUpdate)
    {
        return Collections.EMPTY_SET;
    }


    public boolean forceCopy(AttributeDescriptor attributeDescriptor)
    {
        return false;
    }


    protected boolean isRequiredForCreation(AttributeDescriptor attributeDescriptor)
    {
        return (super.isRequiredForCreation(attributeDescriptor) || this.creationRequiredAttributes
                        .contains(translateAD(attributeDescriptor)));
    }


    public void cleanup()
    {
        this.creationRequiredAttributes.clear();
        this._currentlyCopying = null;
        super.cleanup();
    }


    protected boolean isForceUpdate()
    {
        return this.forceUpdate;
    }


    public int getCopyCacheSize()
    {
        return this.copyCacheSize;
    }


    public void setCopyCacheSize(int copyCacheSize)
    {
        if(this._copiedItemCache != null)
        {
            throw new IllegalStateException("Cache already created - cannot change cache size");
        }
        this.copyCacheSize = copyCacheSize;
    }


    protected Set<Language> getTargetLanguages()
    {
        return this.targetLanguages;
    }


    protected void afterCreate(ItemCopyCreator icc, Item ret)
    {
        registerCopy(new ItemCopyCacheEntry(icc
                        .getSourceItem(), ret,
                        (icc instanceof CatalogItemCopyCreator) ? ((CatalogItemCopyCreator)icc).getSyncTimestamp() : null));
    }


    public long getCopyCacheHits()
    {
        return this.copyCacheHits;
    }


    public void setCopyCacheHits(long copyCacheHits)
    {
        this.copyCacheHits = copyCacheHits;
    }


    public long getCopyCacheMisses()
    {
        return this.copyCacheMisses;
    }


    public void setCopyCacheMisses(long copyCacheMisses)
    {
        this.copyCacheMisses = copyCacheMisses;
    }


    protected CatalogVersion getTargetVersionForQueryCatalogItemCopy(Item sourceItem)
    {
        return getTargetVersion();
    }


    protected boolean canBeTranslatedPartially(AttributeCopyCreator attributeCopyCreator)
    {
        return isPartialCollectionTranslationEnabledGlobally();
    }


    protected boolean isPartialCollectionTranslationEnabledGlobally()
    {
        return Config.getBoolean("synchronization.allow.partial.collection.translation", true);
    }
}
