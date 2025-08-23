package de.hybris.platform.cockpit.services.sync.impl;

import de.hybris.platform.catalog.daos.ItemSyncTimestampDao;
import de.hybris.platform.catalog.jalo.Catalog;
import de.hybris.platform.catalog.jalo.CatalogManager;
import de.hybris.platform.catalog.jalo.CatalogVersion;
import de.hybris.platform.catalog.jalo.ItemSyncTimestamp;
import de.hybris.platform.catalog.jalo.SyncItemCronJob;
import de.hybris.platform.catalog.jalo.SyncItemJob;
import de.hybris.platform.catalog.jalo.synchronization.CatalogVersionSyncCronJob;
import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.catalog.model.ItemSyncTimestampModel;
import de.hybris.platform.catalog.model.SyncItemJobModel;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.cockpit.daos.SynchronizationServiceDao;
import de.hybris.platform.cockpit.model.meta.BaseType;
import de.hybris.platform.cockpit.model.meta.ObjectType;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.SystemService;
import de.hybris.platform.cockpit.services.impl.AbstractServiceImpl;
import de.hybris.platform.cockpit.services.sync.SynchronizationService;
import de.hybris.platform.cockpit.services.values.ObjectValueContainer;
import de.hybris.platform.cockpit.services.values.ObjectValueHandlerRegistry;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.cockpit.util.TypeTools;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.Job;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SearchResult;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.util.Config;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class SynchronizationServiceImpl extends AbstractServiceImpl implements SynchronizationService
{
    private static final Logger LOG = LoggerFactory.getLogger(SynchronizationServiceImpl.class);
    public static final String DISABLE_RESTRICTION = "disableRestrictions";
    private static final char RIGHT_ARROW = '→';
    private static final String LEFT_ROUND_BRACKET = " (";
    private static final String RIGHT_ROUND_BRACKET = ") ";
    private static final String LEFT_ANGLE_BRACKET = "< ";
    private static final String RIGHT_ANGLE_BRACKET = "> ";
    private static final String INIT_SYNCHRONIZATION_CHECK_METHOD = "catalog.synchronization.initialinit.check.timestamps";
    private SystemService systemService;
    private ObjectValueHandlerRegistry valueHandlerRegistry;
    private final CatalogManager catalogManager = CatalogManager.getInstance();
    private final TypeManager typeManager = TypeManager.getInstance();
    private SynchronizationServiceDao synchronizationServiceDao;
    private Map<String, List<String>> relatedReferencesTypesMap = new HashMap<>();
    private int relatedReferencesMaxDepth = -1;
    private volatile Map<ObjectType, Set<PropertyDescriptor>> relatedProperties = new HashMap<>();
    private ItemSyncTimestampDao itemSyncTimestampDao;
    private Boolean disabledSearchRestrictions = null;


    protected Map<String, List<String>> getRelatedReferencesTypesMap()
    {
        return this.relatedReferencesTypesMap;
    }


    public void setRelatedReferencesTypesMap(Map<String, List<String>> relatedReferencesTypesMap)
    {
        this.relatedReferencesTypesMap = relatedReferencesTypesMap;
    }


    protected int getRelatedReferencesMaxDepth()
    {
        return this.relatedReferencesMaxDepth;
    }


    public void setRelatedReferencesMaxDepth(int relatedReferencesMaxDepth)
    {
        this.relatedReferencesMaxDepth = relatedReferencesMaxDepth;
    }


    public TypeManager getTypeManager()
    {
        return this.typeManager;
    }


    public CatalogManager getCatalogManager()
    {
        return this.catalogManager;
    }


    public Collection<TypedObject> getSyncSources(TypedObject object)
    {
        return getTypeService().wrapItems(this.synchronizationServiceDao.getSyncSources((ItemModel)object.getObject()).getResult());
    }


    public Collection<TypedObject> getSyncTargets(TypedObject object)
    {
        return getTypeService().wrapItems(this.synchronizationServiceDao.getSyncTargets((ItemModel)object.getObject()).getResult());
    }


    public Collection<TypedObject> getSyncSourcesAndTargets(TypedObject object)
    {
        return getTypeService().wrapItems(this.synchronizationServiceDao
                        .getSyncSourcesAndTargets((ItemModel)object.getObject()).getResult());
    }


    public void performPullSynchronization(List<TypedObject> targetItems)
    {
        Map<CatalogVersionModel, Set<TypedObject>> syncMap = new HashMap<>();
        for(TypedObject typedObject : targetItems)
        {
            SyncContextImpl syncContextImpl = createPullSyncStatusContext(typedObject);
            if(syncContextImpl.getSourceItemModels().size() != 1)
            {
                continue;
            }
            CatalogVersionModel targetCatalogVersion = getCatalogVersionForItem(typedObject);
            Set<TypedObject> tcProductSet = null;
            if(syncMap.containsKey(targetCatalogVersion))
            {
                tcProductSet = syncMap.get(targetCatalogVersion);
            }
            else
            {
                tcProductSet = new HashSet<>();
                syncMap.put(targetCatalogVersion, tcProductSet);
            }
            try
            {
                ItemModel sourceModel = syncContextImpl.getSourceItemModels().iterator().next();
                TypedObject sourceProduct = getTypeService().wrapItem(sourceModel);
                tcProductSet.add(sourceProduct);
            }
            catch(NoSuchElementException noSuchElementException)
            {
            }
        }
        for(Map.Entry<CatalogVersionModel, Set<TypedObject>> objectSetEntry : syncMap.entrySet())
        {
            CatalogVersionModel version = objectSetEntry.getKey();
            Set<TypedObject> items = objectSetEntry.getValue();
            List<TypedObject> itemsList = new ArrayList<>(items);
            processSingleRuleSync(version, itemsList, null);
        }
    }


    public Collection<TypedObject> performSynchronization(Collection<? extends Object> items, List<String> syncJobPkList, CatalogVersionModel targetCatalogVersion, String qualifier)
    {
        if(items == null || items.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<TypedObject> ret = new ArrayList<>();
        for(Object object : items)
        {
            TypedObject wrappedItem = null;
            if(object instanceof Item)
            {
                wrappedItem = getTypeService().wrapItem(((Item)object).getPK());
            }
            else if(object instanceof ItemModel)
            {
                wrappedItem = getTypeService().wrapItem(((ItemModel)object).getPk());
            }
            else if(object instanceof TypedObject)
            {
                wrappedItem = (TypedObject)object;
            }
            if(wrappedItem != null)
            {
                ret.add(wrappedItem);
                ret.addAll(getRelatedReferences(wrappedItem));
                continue;
            }
            LOG.error("Couldn't wrap item '" + object + "' into a typed object.");
        }
        if(syncJobPkList == null)
        {
            processSingleRuleSync(targetCatalogVersion, ret, qualifier);
        }
        else
        {
            processMultiRuleSync(syncJobPkList, ret);
        }
        return ret;
    }


    private void processSingleRuleSync(CatalogVersionModel target, List<TypedObject> products, String qualifier)
    {
        Map<Object, List<TypedObject>> syncPool = new HashMap<>();
        if(products == null || products.isEmpty())
        {
            return;
        }
        boolean isCategory = ((TypedObject)products.get(0)).getObject() instanceof de.hybris.platform.category.model.CategoryModel;
        SyncItemJob existingSyncJob = null;
        for(TypedObject product : products)
        {
            CatalogVersion sourceCatalogVersion = retrieveCatalogVersion(product);
            if(!isVersionSynchronizedAtLeastOnce(sourceCatalogVersion.getSynchronizations()))
            {
                continue;
            }
            if(target == null)
            {
                existingSyncJob = this.catalogManager.getSyncJobFromSource(sourceCatalogVersion);
            }
            else if(qualifier != null)
            {
                existingSyncJob = this.catalogManager.getSyncJob(sourceCatalogVersion, (CatalogVersion)this.modelService.getSource(target), qualifier);
            }
            else
            {
                existingSyncJob = this.catalogManager.getSyncJob(sourceCatalogVersion, (CatalogVersion)this.modelService.getSource(target));
            }
            if(existingSyncJob != null)
            {
                if(checkUserRightToTargetVersion((Job)existingSyncJob))
                {
                    addToPool(syncPool, existingSyncJob, product);
                }
            }
        }
        for(Map.Entry<Object, List<TypedObject>> entry : syncPool.entrySet())
        {
            existingSyncJob = (SyncItemJob)entry.getKey();
            SyncItemCronJob synchronizeJob = existingSyncJob.newExecution();
            if(!isCategory)
            {
                List<PK[]> pkArrayList = (List)new ArrayList<>();
                for(TypedObject product : entry.getValue())
                {
                    PK[] pkArray = new PK[2];
                    pkArray[0] = ((ItemModel)product.getObject()).getPk();
                    pkArray[1] = null;
                    pkArrayList.add(pkArray);
                }
                if(synchronizeJob instanceof CatalogVersionSyncCronJob)
                {
                    ((CatalogVersionSyncCronJob)synchronizeJob).addPendingItems(pkArrayList);
                }
                else
                {
                    synchronizeJob.addPendingItems(pkArrayList, false);
                }
            }
            else
            {
                SessionContext ctx = null;
                try
                {
                    ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                    ctx.setAttribute("disableRestrictions", isSearchRestrictionDisabled());
                    List<Category> rawCategory = new ArrayList<>();
                    for(TypedObject typedObject : syncPool.get(existingSyncJob))
                    {
                        rawCategory.add((Category)extractJaloItem(typedObject.getObject()));
                    }
                    existingSyncJob.addCategoriesToSync(synchronizeJob, rawCategory, true, true);
                }
                finally
                {
                    if(ctx != null)
                    {
                        JaloSession.getCurrentSession().removeLocalSessionContext();
                    }
                }
            }
            synchronizeJob.setConfigurator((SyncItemCronJob.Configurator)new CockpitDummySyncConfigurator(this, synchronizeJob, existingSyncJob));
            existingSyncJob.perform((CronJob)synchronizeJob, true);
        }
    }


    private <E extends TypedObject> void processMultiRuleSync(List<String> syncJobPkList, List<E> items)
    {
        if(items == null || items.isEmpty())
        {
            return;
        }
        boolean isCategory = ((TypedObject)items.get(0)).getObject() instanceof de.hybris.platform.category.model.CategoryModel;
        for(String syncJobPk : syncJobPkList)
        {
            SyncItemJob syncJob = (SyncItemJob)JaloSession.getCurrentSession().getItem(PK.parse(syncJobPk));
            SyncItemCronJob synchronizeJob = syncJob.newExecution();
            if(!isCategory)
            {
                List<PK[]> pkArrayList = (List)new ArrayList<>();
                for(TypedObject typedObject : items)
                {
                    CatalogVersion sourceCatalogVersion = retrieveCatalogVersion(typedObject);
                    if(!isVersionSynchronizedAtLeastOnce(sourceCatalogVersion.getSynchronizations()))
                    {
                        continue;
                    }
                    if(sourceCatalogVersion.getSynchronizations().contains(syncJob))
                    {
                        PK[] pkArray = new PK[2];
                        pkArray[0] = ((ItemModel)typedObject.getObject()).getPk();
                        pkArray[1] = null;
                        pkArrayList.add(pkArray);
                    }
                }
                if(synchronizeJob instanceof CatalogVersionSyncCronJob)
                {
                    ((CatalogVersionSyncCronJob)synchronizeJob).addPendingItems(pkArrayList);
                }
                else
                {
                    synchronizeJob.addPendingItems(pkArrayList, false);
                }
            }
            else
            {
                SessionContext ctx = null;
                try
                {
                    ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                    ctx.setAttribute("disableRestrictions", isSearchRestrictionDisabled());
                    syncJob.addCategoriesToSync(synchronizeJob, getRawObjects(items), true, true);
                }
                finally
                {
                    if(ctx != null)
                    {
                        JaloSession.getCurrentSession().removeLocalSessionContext();
                    }
                }
            }
            synchronizeJob.setConfigurator((SyncItemCronJob.Configurator)new CockpitDummySyncConfigurator(this, synchronizeJob, syncJob));
            syncJob.perform((CronJob)synchronizeJob, true);
        }
    }


    private <E extends TypedObject> List getRawObjects(List<E> source)
    {
        List<Item> ret = new ArrayList();
        for(TypedObject element : source)
        {
            ret.add(extractJaloItem(element.getObject()));
        }
        return ret;
    }


    private <E extends TypedObject> void addToPool(Map<Object, List<E>> pool, Object key, E item)
    {
        List<E> value = pool.get(key);
        if(value == null)
        {
            value = new ArrayList<>();
            pool.put(key, value);
        }
        value.add(item);
    }


    public <E extends TypedObject> CatalogVersion retrieveCatalogVersion(E item)
    {
        return this.catalogManager.getCatalogVersion(extractJaloItem(item.getObject()));
    }


    public int getPullSyncStatus(TypedObject product)
    {
        return createPullSyncStatusContext(product).getPullSyncStatus();
    }


    private SyncContextImpl createPullSyncStatusContext(TypedObject product)
    {
        SyncContextImpl ret = new SyncContextImpl(this);
        int pullSync = -1;
        CatalogVersion targetCatalogVersion = retrieveCatalogVersion(product);
        Item productItem = extractJaloItem(product.getObject());
        Product sourceProduct = null;
        List<ItemSyncTimestamp> synchronizationSources = CatalogManager.getInstance().getSynchronizationSources(productItem);
        if(synchronizationSources.size() == 1)
        {
            for(ItemSyncTimestamp itemSyncTimestamp : synchronizationSources)
            {
                Item sourceItem = itemSyncTimestamp.getSourceItem();
                if(!(sourceItem instanceof Product))
                {
                    continue;
                }
                CatalogVersion sourceCatalogVersion = CatalogManager.getInstance().getCatalogVersion(sourceItem);
                if(sourceCatalogVersion == null || targetCatalogVersion == null || sourceCatalogVersion.equals(targetCatalogVersion))
                {
                    continue;
                }
                SyncItemJob syncJob = CatalogManager.getInstance().getSyncJob(sourceCatalogVersion, targetCatalogVersion);
                if(!checkUserRight((Job)syncJob))
                {
                    pullSync = -1;
                    continue;
                }
                sourceProduct = (Product)sourceItem;
                pullSync = isSynchronized((Item)sourceProduct, targetCatalogVersion, syncJob, productItem, null);
                try
                {
                    ret.addSourceItemModel((ItemModel)getModelService().get(sourceProduct));
                }
                catch(Exception e)
                {
                    LOG.error(e.getMessage());
                }
            }
        }
        ret.setPullSyncStatus(pullSync);
        return ret;
    }


    public final int isObjectSynchronized(TypedObject object)
    {
        return getSyncInfo(object).getSyncStatus();
    }


    protected SyncInfo getSyncInfo(TypedObject object)
    {
        SyncInfo syncInfoSimple = getSyncInfoSimple(object);
        Set<TypedObject> affectedItems = new HashSet<>(syncInfoSimple.getAffectedItems());
        int syncStatus = syncInfoSimple.getSyncStatus();
        for(TypedObject typedObject : getRelatedReferences(object))
        {
            affectedItems.add(typedObject);
            if(getSyncInfoSimple(typedObject).getSyncStatus() == 1)
            {
                syncStatus = 1;
                break;
            }
        }
        syncInfoSimple.setSyncStatus(syncStatus);
        syncInfoSimple.setAffectedItems(affectedItems);
        return syncInfoSimple;
    }


    protected SyncInfo getSyncInfoSimple(TypedObject object)
    {
        int status = 0;
        Set<TypedObject> affectedItems = new HashSet<>();
        SyncInfo ret = new SyncInfo(this);
        CatalogVersion sourceCatalogVersion = retrieveCatalogVersion(object);
        if(sourceCatalogVersion == null)
        {
            status = -1;
        }
        else
        {
            List<Job> synchronizationRules = sourceCatalogVersion.getSynchronizations();
            if(synchronizationRules == null || synchronizationRules.isEmpty() ||
                            !isTypeInRootTypes(object.getType().getCode(), synchronizationRules))
            {
                status = -1;
            }
            else if(!isVersionSynchronizedAtLeastOnce(synchronizationRules))
            {
                status = 2;
            }
            else
            {
                for(Iterator<Job> iter = synchronizationRules.iterator(); iter.hasNext(); )
                {
                    Object rawObject = iter.next();
                    if(rawObject instanceof SyncItemJob)
                    {
                        SyncItemJob syncTask = (SyncItemJob)rawObject;
                        if(!checkUserRight((Job)syncTask))
                        {
                            status = -1;
                            continue;
                        }
                        CatalogVersion targetCatalogVersion = syncTask.getTargetVersion();
                        status = isSynchronized(extractJaloItem(object.getObject()), targetCatalogVersion, syncTask, null, affectedItems);
                        if(status == 1)
                        {
                            break;
                        }
                    }
                }
            }
        }
        ret.setSyncStatus(status);
        ret.setAffectedItems(affectedItems);
        return ret;
    }


    private boolean isTypeInRootTypes(String typeCode, List<Job> syncJobs)
    {
        for(Job job : syncJobs)
        {
            SyncItemJob catalogVersionSynJob = (SyncItemJob)job;
            ComposedType objectType = TypeManager.getInstance().getComposedType(typeCode);
            List<ComposedType> rootTypes = catalogVersionSynJob.getRootTypes();
            for(ComposedType composedType : rootTypes)
            {
                if(composedType.isAssignableFrom((Type)objectType))
                {
                    return true;
                }
            }
        }
        return false;
    }


    private int isSynchronized(Item sourceItem, CatalogVersion targetVersion, SyncItemJob syncTask, Item targetItem, Set<TypedObject> affectedItemsReturn)
    {
        int ret = 0;
        Item targetCounterPart = null;
        if(targetItem == null)
        {
            SessionContext ctx = null;
            try
            {
                ctx = JaloSession.getCurrentSession().createLocalSessionContext();
                ctx.setAttribute("disableRestrictions", isSearchRestrictionDisabled());
                targetCounterPart = CatalogManager.getInstance().getCounterpartItem(sourceItem, targetVersion);
            }
            finally
            {
                if(ctx != null)
                {
                    JaloSession.getCurrentSession().removeLocalSessionContext();
                }
            }
        }
        else
        {
            targetCounterPart = targetItem;
        }
        if(targetCounterPart == null)
        {
            ret = 1;
        }
        else
        {
            if(this.catalogManager.getLastSyncModifiedTime(syncTask, sourceItem, targetCounterPart) == null)
            {
                ret = 1;
            }
            else
            {
                long secondSyncTime = this.catalogManager.getLastSyncModifiedTime(syncTask, sourceItem, targetCounterPart).getTime();
                if(secondSyncTime < sourceItem.getModificationTime().getTime())
                {
                    ret = 1;
                }
            }
            if(affectedItemsReturn != null)
            {
                affectedItemsReturn.add(getTypeService().wrapItem(targetCounterPart));
            }
        }
        return ret;
    }


    public boolean isVersionSynchronizedAtLeastOnce(List<SyncItemJob> list)
    {
        List<SyncItemJob> synchronizationRules = new ArrayList<>();
        if(list == null || list.isEmpty())
        {
            return false;
        }
        for(SyncItemJob object : list)
        {
            if(object instanceof CatalogVersionModel)
            {
                CatalogVersion sourceCatalogVersion = (CatalogVersion)this.modelService.getSource(object);
                synchronizationRules = sourceCatalogVersion.getSynchronizations();
            }
            else if(object instanceof de.hybris.platform.category.model.CategoryModel)
            {
                Category category = (Category)this.modelService.getSource(object);
                CatalogVersion sourceCatalogVersion = this.catalogManager.getCatalogVersion(category);
                synchronizationRules = sourceCatalogVersion.getSynchronizations();
            }
            else
            {
                synchronizationRules = list;
            }
            for(SyncItemJob job : synchronizationRules)
            {
                if(checkIfSynchronizationWasExecuted((SyncItemJobModel)getModelService().get(job)))
                {
                    return true;
                }
            }
        }
        return false;
    }


    private boolean checkIfSynchronizationWasExecuted(SyncItemJobModel syncItemJobModel)
    {
        if(useSyncTimestamps())
        {
            Collection<ItemSyncTimestampModel> syncRecords = this.itemSyncTimestampDao.findSyncTimestampsByCatalogVersion(syncItemJobModel.getSourceVersion(), 1);
            return CollectionUtils.isNotEmpty(syncRecords);
        }
        return CollectionUtils.isNotEmpty(syncItemJobModel.getCronJobs());
    }


    private boolean useSyncTimestamps()
    {
        return Config.getBoolean("catalog.synchronization.initialinit.check.timestamps", true);
    }


    public Long getLastTargetProductSyncTimestamp(Item product)
    {
        JaloSession jaloSession = JaloSession.getCurrentSession();
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", isSearchRestrictionDisabled());
            if(product instanceof Product)
            {
                StringBuilder query = new StringBuilder();
                query.append("SELECT {TS:PK} FROM {Product as p JOIN ItemSyncTimestamp as TS  \tON {p:PK}={TS:targetItem} and {p:PK} = " + product
                                .getPK() + "}");
                SearchResult<ItemSyncTimestamp> results = jaloSession.getFlexibleSearch().search(query.toString(), ItemSyncTimestamp.class);
                if(!results.getResult().isEmpty())
                {
                    return Long.valueOf(((ItemSyncTimestamp)results.getResult().iterator().next()).getLastSyncTime().getTime());
                }
            }
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        return Long.valueOf(0L);
    }


    protected Boolean isSearchRestrictionDisabled()
    {
        if(Boolean.FALSE.equals(this.disabledSearchRestrictions))
        {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }


    protected boolean checkRootOrSubType(TypedObject item, SyncItemJob syncJob)
    {
        return isTypeInRootTypes(item.getType().getCode(), (List)Collections.singletonList(syncJob));
    }


    public List<SyncItemJobModel>[] getTargetCatalogVersions(TypedObject item)
    {
        List[] ret = new List[2];
        List<SyncItemJobModel> accessibleRules = new ArrayList<>();
        List<SyncItemJobModel> forbiddenRules = new ArrayList<>();
        CatalogVersion sourceCatalogVersion = retrieveCatalogVersion(item);
        List syncCollection = sourceCatalogVersion.getSynchronizations();
        for(Object synchronization : syncCollection)
        {
            if(synchronization instanceof SyncItemJob)
            {
                SyncItemJob syncJob = (SyncItemJob)synchronization;
                if(!checkUserRight((Job)syncJob))
                {
                    continue;
                }
                boolean init = isVersionSynchronizedAtLeastOnce(Collections.singletonList(syncJob));
                if(!init)
                {
                    forbiddenRules.add((SyncItemJobModel)this.modelService.get(syncJob));
                    continue;
                }
                if(syncJob.getTargetVersion() != null && checkRootOrSubType(item, syncJob))
                {
                    accessibleRules.add((SyncItemJobModel)this.modelService.get(syncJob));
                }
            }
        }
        ret[0] = accessibleRules;
        ret[1] = forbiddenRules;
        return (List<SyncItemJobModel>[])ret;
    }


    public List<SyncItemJobModel>[] getSyncJobs(ItemModel source, ObjectType objectType)
    {
        ArrayList[] arrayOfArrayList = new ArrayList[2];
        CatalogVersion sourceCatalogVersion = null;
        if(source instanceof CatalogVersionModel)
        {
            sourceCatalogVersion = (CatalogVersion)this.modelService.getSource(source);
        }
        else if(source instanceof de.hybris.platform.category.model.CategoryModel)
        {
            Category category = (Category)this.modelService.getSource(source);
            sourceCatalogVersion = this.catalogManager.getCatalogVersion(category);
        }
        List<SyncItemJobModel> accesibleRules = new ArrayList<>();
        List<SyncItemJobModel> forbidenRules = new ArrayList<>();
        if(sourceCatalogVersion != null)
        {
            List syncCollection = sourceCatalogVersion.getSynchronizations();
            for(Object synchronization : syncCollection)
            {
                if(synchronization instanceof SyncItemJob)
                {
                    SyncItemJob syncJob = (SyncItemJob)synchronization;
                    boolean init = isVersionSynchronizedAtLeastOnce(Collections.singletonList(syncJob));
                    if(!checkUserRight((Job)syncJob))
                    {
                        continue;
                    }
                    if(init || source instanceof CatalogVersionModel)
                    {
                        if(syncJob.getTargetVersion() != null && objectType != null && syncJob
                                        .getRootTypes().contains(this.typeManager.getComposedType(objectType.getCode())))
                        {
                            accesibleRules.add((SyncItemJobModel)this.modelService.get(syncJob));
                            continue;
                        }
                        if(objectType == null)
                        {
                            accesibleRules.add((SyncItemJobModel)this.modelService.get(syncJob));
                        }
                        continue;
                    }
                    forbidenRules.add((SyncItemJobModel)this.modelService.get(syncJob));
                }
            }
        }
        arrayOfArrayList[0] = (ArrayList)accesibleRules;
        arrayOfArrayList[1] = (ArrayList)forbidenRules;
        return (List<SyncItemJobModel>[])arrayOfArrayList;
    }


    public CatalogVersionModel getCatalogVersionForItem(TypedObject item)
    {
        return (CatalogVersionModel)this.modelService.get(retrieveCatalogVersion(item));
    }


    public Map<String, String>[] getAllSynchronizationRules(Collection items)
    {
        HashMap[] arrayOfHashMap = new HashMap[2];
        Map<String, String> accesibleRules = new HashMap<>();
        Map<String, String> forbidenRules = new HashMap<>();
        for(Object item : items)
        {
            CatalogVersion sourceCatalogVersion = null;
            Item jaloItem = null;
            if(item instanceof TypedObject)
            {
                jaloItem = extractJaloItem(((TypedObject)item).getObject());
                sourceCatalogVersion = retrieveCatalogVersion((TypedObject)item);
            }
            else if(item instanceof CatalogVersionModel)
            {
                sourceCatalogVersion = (CatalogVersion)this.modelService.getSource(item);
            }
            else if(item instanceof de.hybris.platform.category.model.CategoryModel)
            {
                jaloItem = (Item)this.modelService.getSource(item);
                sourceCatalogVersion = this.catalogManager.getCatalogVersion(jaloItem);
            }
            if(sourceCatalogVersion != null)
            {
                for(SyncItemJob syncJob : sourceCatalogVersion.getSynchronizations())
                {
                    if(!checkUserRight((Job)syncJob))
                    {
                        continue;
                    }
                    boolean init = isVersionSynchronizedAtLeastOnce(Collections.singletonList(syncJob));
                    if(!init && !(item instanceof CatalogVersionModel))
                    {
                        forbidenRules.put(syncJob.getPK().toString(), prepareReadableSyncRuleName(syncJob));
                        continue;
                    }
                    ComposedType superType = null;
                    if(jaloItem != null)
                    {
                        ComposedType currentComposedType = this.typeManager.getComposedType(jaloItem.getClass());
                        if(currentComposedType != null)
                        {
                            superType = currentComposedType.getSuperType();
                        }
                        List<ComposedType> rootTypes = syncJob.getRootTypes();
                        if(rootTypes.contains(currentComposedType) || rootTypes.contains(superType))
                        {
                            accesibleRules.put(syncJob.getPK().toString(), prepareReadableSyncRuleName(syncJob));
                        }
                        continue;
                    }
                    accesibleRules.put(syncJob.getPK().toString(), prepareReadableSyncRuleName(syncJob));
                }
            }
        }
        arrayOfHashMap[0] = (HashMap)accesibleRules;
        arrayOfHashMap[1] = (HashMap)forbidenRules;
        return (Map<String, String>[])arrayOfHashMap;
    }


    private String prepareReadableSyncRuleName(SyncItemJob syncJob)
    {
        String finalName = "";
        CatalogVersion sourceCatalgVersion = syncJob.getSourceVersion();
        CatalogVersion targetCatalgVersion = syncJob.getTargetVersion();
        String catalogname = "";
        String label = "";
        Catalog catalog = syncJob.getSourceVersion().getCatalog();
        if(catalog.getName() != null)
        {
            catalogname = catalog.getName();
        }
        label = ((catalogname != null) ? catalogname : ("< " + catalog.getId() + "> ")) + " " + ((catalogname != null) ? catalogname : ("< " + catalog.getId() + "> "));
        finalName = finalName + finalName + " (" + label + ") ";
        catalog = syncJob.getTargetVersion().getCatalog();
        if(catalog.getName() != null)
        {
            catalogname = catalog.getName();
        }
        label = ((catalogname != null) ? catalogname : ("< " + catalog.getId() + "> ")) + " " + ((catalogname != null) ? catalogname : ("< " + catalog.getId() + "> "));
        finalName = finalName + " → " + finalName + " (" + label + ") ";
        finalName = finalName + " (" + finalName + ")";
        return finalName;
    }


    private boolean checkUserRight(Job job)
    {
        return (job instanceof SyncItemJob && checkUserRightToTargetVersion(job));
    }


    private boolean checkUserRightToTargetVersion(Job job)
    {
        SyncItemJob syncItemJob = (SyncItemJob)job;
        JaloSession jaloSession = JaloSession.getCurrentSession();
        User currentUser = jaloSession.getUser();
        if(currentUser.isAdmin())
        {
            return true;
        }
        SessionContext ctx = jaloSession.getSessionContext();
        return this.catalogManager.canSync(ctx, currentUser, syncItemJob);
    }


    public List<String> getSynchronizationStatuses(List<SyncItemJobModel> rules, TypedObject sourceObject)
    {
        Collection<TypedObject> relatedReferences = new ArrayList<>();
        List<String> retStatuses = getSynchronizationStatusesSimple(rules, sourceObject);
        relatedReferences.addAll(getRelatedReferences(sourceObject));
        for(TypedObject typedObject : relatedReferences)
        {
            List<String> childrenStatuses = getSynchronizationStatusesSimple(rules, typedObject);
            retStatuses.retainAll(childrenStatuses);
        }
        return retStatuses;
    }


    public List<String> getSynchronizationStatusesSimple(List<SyncItemJobModel> rules, TypedObject sourceObject)
    {
        List<String> result = new ArrayList<>();
        SessionContext ctx = null;
        try
        {
            ctx = JaloSession.getCurrentSession().createLocalSessionContext();
            ctx.setAttribute("disableRestrictions", isSearchRestrictionDisabled());
            for(SyncItemJobModel rule : rules)
            {
                CatalogVersion target = (CatalogVersion)this.modelService.getSource(rule.getTargetVersion());
                Item targetCounterPart = this.catalogManager.getCounterpartItem(extractJaloItem(sourceObject.getObject()), target);
                if(targetCounterPart == null)
                {
                    continue;
                }
                SyncItemJob syncJob = (SyncItemJob)this.modelService.getSource(rule);
                long secondSyncTime = 0L;
                Date secondSyncDate = this.catalogManager.getLastSyncModifiedTime(syncJob,
                                extractJaloItem(sourceObject.getObject()), targetCounterPart);
                if(secondSyncDate != null)
                {
                    secondSyncTime = secondSyncDate.getTime();
                }
                if(secondSyncTime >= extractJaloItem(sourceObject.getObject()).getModificationTime().getTime())
                {
                    result.add(syncJob.getCode());
                }
            }
        }
        finally
        {
            if(ctx != null)
            {
                JaloSession.getCurrentSession().removeLocalSessionContext();
            }
        }
        return result;
    }


    public void performCatalogVersionSynchronization(Collection<CatalogVersionModel> data, List<String> syncRulePkList, CatalogVersionModel targetCatalogVersion, String qualifier)
    {
        if(syncRulePkList != null)
        {
            JaloSession jaloSession = JaloSession.getCurrentSession();
            for(String jobPk : syncRulePkList)
            {
                SyncItemJob syncJob = (SyncItemJob)jaloSession.getItem(PK.parse(jobPk));
                SyncItemCronJob synchronizeJob = syncJob.newExecution();
                syncJob.perform((CronJob)synchronizeJob, true);
            }
        }
        else
        {
            for(CatalogVersionModel object : data)
            {
                CatalogVersion catalogVersion = null;
                if(object instanceof CatalogVersionModel)
                {
                    catalogVersion = (CatalogVersion)this.modelService.getSource(object);
                }
                else if(object instanceof de.hybris.platform.category.model.CategoryModel)
                {
                    catalogVersion = this.catalogManager.getCatalogVersion((Item)this.modelService.getSource(object));
                }
                SyncItemJob catalogVersionSyncJob = null;
                if(targetCatalogVersion == null)
                {
                    catalogVersionSyncJob = this.catalogManager.getSyncJobFromSource(catalogVersion);
                }
                else
                {
                    CatalogVersion target = (CatalogVersion)this.modelService.getSource(targetCatalogVersion);
                    if(qualifier != null)
                    {
                        catalogVersionSyncJob = this.catalogManager.getSyncJob(catalogVersion, target, qualifier);
                    }
                    else
                    {
                        catalogVersionSyncJob = this.catalogManager.getSyncJob(catalogVersion, target);
                    }
                }
                if(catalogVersionSyncJob != null)
                {
                    SyncItemCronJob synchronizeJob = catalogVersionSyncJob.newExecution();
                    catalogVersionSyncJob.perform((CronJob)synchronizeJob, true);
                }
            }
        }
    }


    public boolean hasMultipleRules(Collection items)
    {
        for(Iterator iter = items.iterator(); iter.hasNext(); )
        {
            Object item = iter.next();
            CatalogVersion version = null;
            if(item instanceof TypedObject && ((TypedObject)item).getObject() instanceof de.hybris.platform.core.model.product.ProductModel)
            {
                version = retrieveCatalogVersion((TypedObject)item);
            }
            else if(item instanceof CatalogVersionModel)
            {
                version = (CatalogVersion)this.modelService.getSource(item);
            }
            else if(item instanceof de.hybris.platform.category.model.CategoryModel)
            {
                version = this.catalogManager.getCatalogVersion((Item)this.modelService.getSource(item));
            }
            if(version != null && version.getSynchronizations().size() > 1)
            {
                return true;
            }
        }
        return false;
    }


    private Item extractJaloItem(Object sourceObject)
    {
        if(sourceObject instanceof Item)
        {
            return (Item)sourceObject;
        }
        if(sourceObject instanceof ItemModel)
        {
            return (Item)getModelService().getSource(sourceObject);
        }
        return null;
    }


    public SynchronizationService.SyncContext getSyncContext(TypedObject product)
    {
        return getSyncContext(product, false);
    }


    public SynchronizationService.SyncContext getSyncContext(TypedObject product, boolean pullSync)
    {
        SyncContextImpl ret = null;
        if(pullSync)
        {
            ret = createPullSyncStatusContext(product);
        }
        else
        {
            List[] arrayOfList = (List[])getTargetCatalogVersions(product);
            CatalogVersionModel sourceCatalogVersion = getCatalogVersionForItem(product);
            Set<CatalogVersionModel> sourceVersions = new HashSet<>();
            sourceVersions.add(sourceCatalogVersion);
            SyncInfo syncInfo = getSyncInfo(product);
            int productSynchronized = syncInfo.getSyncStatus();
            ret = new SyncContextImpl(this, -1, sourceVersions, arrayOfList, productSynchronized, null);
            if(syncInfo.getAffectedItems() != null)
            {
                ret.setAffectedItems(syncInfo.getAffectedItems());
            }
        }
        return (SynchronizationService.SyncContext)ret;
    }


    public void setSynchronizationServiceDao(SynchronizationServiceDao synchronizationServiceDao)
    {
        this.synchronizationServiceDao = synchronizationServiceDao;
    }


    protected List<TypedObject> getRelatedReferences(TypedObject typedObject)
    {
        List<String> relatedReferencesTypes = getConfiguredReferenceTypes(typedObject);
        List<TypedObject> ret = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(relatedReferencesTypes))
        {
            ret.addAll(getTypeService().wrapItems(
                            lookupRelatedReferences(typedObject, relatedReferencesTypes,
                                            computeRelatedReferencesMaxDepth(relatedReferencesTypes), new HashSet<>())));
            if(ret.contains(typedObject))
            {
                ret.remove(typedObject);
            }
        }
        return ret;
    }


    private int computeRelatedReferencesMaxDepth(List<String> relatedReferencesTypes)
    {
        if(this.relatedReferencesMaxDepth == -1)
        {
            return relatedReferencesTypes.size();
        }
        return Math.max(0, this.relatedReferencesMaxDepth);
    }


    private boolean isValueTypeAssignableFrom(PropertyDescriptor propertyDescriptor, Collection<String> typeCodes)
    {
        for(String typeCode : typeCodes)
        {
            if(getTypeService().getObjectType(typeCode).isAssignableFrom(
                            getTypeService().getObjectType(getTypeService().getValueTypeCode(propertyDescriptor))))
            {
                return true;
            }
        }
        return false;
    }


    private synchronized Set<PropertyDescriptor> getRelevantDescriptors(BaseType type, List<String> relatedReferenceTypesAndProperties)
    {
        Set<PropertyDescriptor> ret = this.relatedProperties.get(type);
        if(ret == null)
        {
            Set<PropertyDescriptor> configuredDescriptors = new HashSet<>();
            Set<String> configuredTypes = new HashSet<>();
            for(String typeOrProperty : relatedReferenceTypesAndProperties)
            {
                if(StringUtils.contains(typeOrProperty, "."))
                {
                    configuredDescriptors.add(getTypeService().getPropertyDescriptor(typeOrProperty));
                    continue;
                }
                configuredTypes.add(typeOrProperty);
            }
            ret = new HashSet<>();
            Set<PropertyDescriptor> allPropertyDescriptors = type.getPropertyDescriptors();
            for(PropertyDescriptor propertyDescriptor : allPropertyDescriptors)
            {
                if("REFERENCE".equals(propertyDescriptor.getEditorType()) && (configuredDescriptors
                                .contains(propertyDescriptor) || isValueTypeAssignableFrom(propertyDescriptor, configuredTypes)))
                {
                    ret.add(propertyDescriptor);
                }
            }
            this.relatedProperties.put(type, ret);
        }
        return ret;
    }


    private Set<Object> lookupRelatedReferences(TypedObject typedObject, List<String> relatedReferenceTypesAndProperties, int depth, Set<Long> added)
    {
        Set<Object> ret = new HashSet();
        if(depth == 0 || typedObject == null)
        {
            return ret;
        }
        Set<String> availableLanguageIsos = getSystemService().getAvailableLanguageIsos();
        Set<PropertyDescriptor> relevantDescriptors = getRelevantDescriptors(typedObject.getType(), relatedReferenceTypesAndProperties);
        ObjectValueContainer valueContainer = TypeTools.createValueContainer(this.valueHandlerRegistry, typedObject, relevantDescriptors, availableLanguageIsos, false);
        for(PropertyDescriptor propertyDescriptor : relevantDescriptors)
        {
            if(propertyDescriptor.isLocalized())
            {
                for(String langIso : availableLanguageIsos)
                {
                    ObjectValueContainer.ObjectValueHolder ovh = valueContainer.getValue(propertyDescriptor, langIso);
                    List<Object> list = extractNonLocalizedAttrValues(propertyDescriptor, ovh);
                    for(Object object : list)
                    {
                        object = getTypeService().wrapItem(object);
                        if(object instanceof TypedObject)
                        {
                            Long pk = getObjectPk((TypedObject)object);
                            if(!added.contains(pk))
                            {
                                ret.add(object);
                                added.add(pk);
                            }
                        }
                    }
                }
                continue;
            }
            List<Object> objects = extractNonLocalizedAttrValues(propertyDescriptor, valueContainer
                            .getValue(propertyDescriptor, null));
            for(Object object : objects)
            {
                object = getTypeService().wrapItem(object);
                if(object instanceof TypedObject)
                {
                    Long pk = getObjectPk((TypedObject)object);
                    if(!added.contains(pk))
                    {
                        ret.add(object);
                        added.add(pk);
                    }
                }
            }
        }
        List<Object> localCopy = new ArrayList(ret);
        for(Object alreadyFound : localCopy)
        {
            ret.addAll(lookupRelatedReferences(getTypeService().wrapItem(alreadyFound), relatedReferenceTypesAndProperties, depth - 1, added));
        }
        return ret;
    }


    private Long getObjectPk(TypedObject object)
    {
        PK pk = ((ItemModel)object.getObject()).getPk();
        return Long.valueOf((pk == null) ? 0L : pk.getLongValue());
    }


    protected List<String> getConfiguredReferenceTypes(TypedObject typedObject)
    {
        List<ObjectType> pathTypes = TypeTools.getAllSupertypes((ObjectType)typedObject.getType());
        Collections.reverse(pathTypes);
        List<String> relatedReferencesTypes = getRelatedReferencesTypesMap().get(typedObject.getType().getCode());
        Iterator<ObjectType> fallbackIter = pathTypes.iterator();
        while(fallbackIter.hasNext() && relatedReferencesTypes == null)
        {
            ObjectType currentObjetType = fallbackIter.next();
            relatedReferencesTypes = getRelatedReferencesTypesMap().get(currentObjetType.getCode());
        }
        return relatedReferencesTypes;
    }


    private List<Object> extractNonLocalizedAttrValues(PropertyDescriptor propertyDescriptor, ObjectValueContainer.ObjectValueHolder objectValueHolder)
    {
        List<Object> ret = new ArrayList();
        if(PropertyDescriptor.Multiplicity.SINGLE.equals(propertyDescriptor.getMultiplicity()))
        {
            Object currentValue = objectValueHolder.getCurrentValue();
            if(currentValue != null)
            {
                ret.add(currentValue);
            }
        }
        else if(PropertyDescriptor.Multiplicity.LIST.equals(propertyDescriptor.getMultiplicity()) || PropertyDescriptor.Multiplicity.SET
                        .equals(propertyDescriptor.getMultiplicity()))
        {
            Object rawColletion = objectValueHolder.getCurrentValue();
            if(rawColletion instanceof Collection)
            {
                ret.addAll((Collection)rawColletion);
            }
        }
        return ret;
    }


    public void setSearchRestrictionsDisabled(boolean disabled)
    {
        this.disabledSearchRestrictions = Boolean.valueOf(disabled);
    }


    public void setItemSyncTimestampDao(ItemSyncTimestampDao itemSyncTimestampDao)
    {
        this.itemSyncTimestampDao = itemSyncTimestampDao;
    }


    public SystemService getSystemService()
    {
        if(this.systemService == null)
        {
            this
                            .systemService = UISessionUtils.getCurrentSession().getSystemService();
        }
        return this.systemService;
    }


    public void setSystemService(SystemService systemService)
    {
        this.systemService = systemService;
    }


    @Required
    public void setValueHandlerRegistry(ObjectValueHandlerRegistry valueHandlerRegistry)
    {
        this.valueHandlerRegistry = valueHandlerRegistry;
    }
}
