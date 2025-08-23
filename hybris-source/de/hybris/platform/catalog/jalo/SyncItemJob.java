package de.hybris.platform.catalog.jalo;

import com.google.common.base.Preconditions;
import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.catalog.jalo.copy.ItemCopyContext;
import de.hybris.platform.category.jalo.Category;
import de.hybris.platform.core.PK;
import de.hybris.platform.cronjob.constants.GeneratedCronJobConstants;
import de.hybris.platform.cronjob.jalo.AbortCronJobException;
import de.hybris.platform.cronjob.jalo.CronJob;
import de.hybris.platform.cronjob.jalo.TriggerableJob;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.product.Product;
import de.hybris.platform.jalo.type.AttributeDescriptor;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.Type;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.type.TypeManagerManaged;
import de.hybris.platform.jalo.type.ViewType;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.variants.jalo.VariantProduct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class SyncItemJob extends GeneratedSyncItemJob implements TriggerableJob
{
    private static final Logger LOG = Logger.getLogger(SyncItemJob.class.getName());
    protected static final String START_TIME = "startTime";
    public static final Set<String> HIDDEN_ATTRIBUTES = new HashSet<>(Arrays.asList(new String[] {
                    "Item." + Item.TYPE, "Item." + Item.CREATION_TIME, "Item." + Item.MODIFIED_TIME, "Product." + GeneratedCatalogConstants.Attributes.Product.CATALOG, "Product." + GeneratedCatalogConstants.Attributes.Product.CATALOGVERSION,
                    "Media." + GeneratedCatalogConstants.Attributes.Media.CATALOG, "Media." + GeneratedCatalogConstants.Attributes.Media.CATALOGVERSION, GeneratedCatalogConstants.TC.CATEGORY + "." + GeneratedCatalogConstants.TC.CATEGORY,
                    GeneratedCatalogConstants.TC.CATEGORY + "." + GeneratedCatalogConstants.TC.CATEGORY, GeneratedCatalogConstants.TC.KEYWORD + ".catalog",
                    GeneratedCatalogConstants.TC.KEYWORD + ".catalogVersion"}));


    @ForceJALO(reason = "something else")
    protected Item createItem(SessionContext ctx, ComposedType type, Item.ItemAttributeMap allAttributes) throws JaloBusinessException
    {
        Set missing = new HashSet();
        if(((!checkMandatoryAttribute("sourceVersion", allAttributes, missing) ? 1 : 0) | (
                        !checkMandatoryAttribute("targetVersion", allAttributes, missing) ? 1 : 0)) != 0)
        {
            throw new JaloInvalidParameterException("missing attributes " + missing + " for creating a new " + type.getCode(), 0);
        }
        if(allAttributes.get("sourceVersion").equals(allAttributes.get("targetVersion")))
        {
            throw new JaloInvalidParameterException("source and target version shouldn't be the same catalogversion!", 0);
        }
        String code = (String)allAttributes.get("code");
        if(StringUtils.isBlank(code))
        {
            CatalogVersion fromCV = (CatalogVersion)allAttributes.get("sourceVersion");
            CatalogVersion toCV = (CatalogVersion)allAttributes.get("targetVersion");
            allAttributes.put("code", "Sync " + fromCV.getCatalog().getId() + ":" + fromCV.getVersion(ctx) + " -> " + toCV
                            .getCatalog().getId() + ":" + toCV.getVersion(ctx));
        }
        Collection<ComposedType> rootTypes = (Collection<ComposedType>)allAttributes.get("rootTypes");
        if(rootTypes == null || rootTypes.isEmpty())
        {
            allAttributes.put("rootTypes", getDefaultRootTypes());
        }
        allAttributes.setAttributeMode("sourceVersion", Item.AttributeMode.INITIAL);
        allAttributes.setAttributeMode("targetVersion", Item.AttributeMode.INITIAL);
        return super.createItem(ctx, type, allAttributes);
    }


    @ForceJALO(reason = "something else")
    public void remove(SessionContext ctx) throws ConsistencyCheckException
    {
        String code = getCode();
        LOG.info("removing sync job " + code + " - this may take some time due to sync timestamp removal!");
        removeSyncTimestamps(ctx);
        super.remove(ctx);
        LOG.info("done sync job " + code + " - this may take some time due to sync timestamp removal!");
    }


    protected void removeSyncTimestamps(SessionContext ctx)
    {
        if(LOG.isInfoEnabled())
        {
            LOG.info("removing timestamps for " + getCode() + " ...");
        }
        int RANGE = 1000;
        List<ItemSyncTimestamp> toRemove = null;
        int total = 0;
        do
        {
            toRemove = FlexibleSearch.getInstance().search("SELECT {PK} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "*} WHERE {syncJob}=?me", Collections.singletonMap("me", this), Collections.singletonList(ItemSyncTimestamp.class), true, true, 0, 1000).getResult();
            for(ItemSyncTimestamp ts : toRemove)
            {
                try
                {
                    ts.remove(ctx);
                }
                catch(ConsistencyCheckException consistencyCheckException)
                {
                }
            }
            total += toRemove.size();
            if(!LOG.isInfoEnabled())
            {
                continue;
            }
            LOG.info("removed " + total + " timestamps for " + getCode() + " by now...");
        }
        while(toRemove.size() == 1000);
        if(LOG.isInfoEnabled())
        {
            LOG.info("done removing timestamps for " + getCode() + ".");
        }
    }


    public static List<ComposedType> getDefaultRootTypes()
    {
        return CatalogManager.getInstance().getDefaultRootTypes();
    }


    public SyncItemCronJob newExecution()
    {
        return CatalogManager.getInstance().createSyncItemCronJob(Collections.singletonMap("job", this));
    }


    @Deprecated(since = "ages", forRemoval = false)
    public SyncItemCronJob newExcecution()
    {
        return newExecution();
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection getExecutions(SessionContext ctx)
    {
        return getCronJobs(ctx);
    }


    @ForceJALO(reason = "something else")
    public boolean isAbortable(CronJob conJob)
    {
        return true;
    }


    protected SessionContext createSyncSessionContext(SyncItemCronJob cronJob)
    {
        SessionContext ctx = getSession().createSessionContext();
        ctx.setLanguage(null);
        ctx.removeAttribute("disableRestrictions");
        ctx.removeAttribute("disableRestrictionGroupInheritance");
        ctx.setAttribute("catalog.sync.active", Boolean.TRUE);
        ctx.setAttribute("disable.attribute.check", Boolean.TRUE);
        ctx.setAttribute("disable_subcategory_removalcheck", Boolean.TRUE);
        ctx.setAttribute("disable_setallowedprincipal_recursively", Boolean.TRUE);
        ctx.removeAttribute("enable.language.fallback");
        if(cronJob != null && cronJob.isCreateSavedValuesAsPrimitive())
        {
            ctx.setAttribute("is.hmc.session", Boolean.TRUE);
        }
        else
        {
            ctx.removeAttribute("is.hmc.session");
        }
        ctx.setAttribute("all.attributes.use.ta", Boolean.FALSE);
        return ctx;
    }


    protected void initializeTimeCounter(SyncItemCronJob cronJob)
    {
        cronJob.setTransientObject("startTime", Long.valueOf(System.currentTimeMillis()));
    }


    public long getElapsedMillis(SyncItemCronJob cronJob)
    {
        Long startTime = (Long)cronJob.getTransientObject("startTime");
        if(startTime != null)
        {
            return System.currentTimeMillis() - startTime.longValue();
        }
        return -1L;
    }


    protected boolean canUndo(CronJob cronJob)
    {
        return false;
    }


    protected int getDuplicatedCatalogItemsCount(SessionContext ctx, CatalogVersion version, SyncItemCronJob cronJob, ComposedType type)
    {
        CatalogManager catman = CatalogManager.getInstance();
        String cvAD = catman.getCatalogVersionAttribute(type).getQualifier();
        Set<String> adquals = new LinkedHashSet<>();
        for(AttributeDescriptor ad : catman.getUniqueKeyAttributes(type))
        {
            adquals.add("{" + ad.getQualifier() + "}");
        }
        String uids = StringUtils.join(adquals, ",");
        String additional = getAdditionalQueryRestrictions(ctx, type, cronJob);
        Map<String, CatalogVersion> values = Collections.singletonMap("version", version);
        String query = "SELECT count(*) FROM ({{SELECT {" + cvAD + "} FROM {" + type.getCode() + "} " + ((additional != null) ? ("WHERE " + additional + " ") : "") + ((additional != null) ? " AND " : " WHERE ") + "{" + cvAD + "}=?version GROUP BY {" + cvAD + "}," + uids
                        + " HAVING count(*) > 1}}) x";
        return ((Integer)FlexibleSearch.getInstance().search(ctx, query, values, Integer.class).getResult().get(0)).intValue();
    }


    protected boolean checkCatalogVersionValidity(SyncItemCronJob cronJob, SyncItemCopyContext syncItemCopyContext)
    {
        boolean gotNoError = true;
        long time1 = System.currentTimeMillis();
        for(ComposedType root_ct : getRootTypes())
        {
            int duplicatedSrcCVItemCount = getDuplicatedCatalogItemsCount(syncItemCopyContext.getCtx(), syncItemCopyContext
                            .getSourceVersion(), cronJob, root_ct);
            if(duplicatedSrcCVItemCount > 0)
            {
                syncItemCopyContext.error("cannot use source catalog version " + syncItemCopyContext.getSourceVersion() + " for synchronization since it owns " + duplicatedSrcCVItemCount + " duplicate " + root_ct
                                .getCode() + " IDs ");
                gotNoError = false;
            }
            int duplicatedTrgCVItemCount = getDuplicatedCatalogItemsCount(syncItemCopyContext.getCtx(), syncItemCopyContext
                            .getTargetVersion(), cronJob, root_ct);
            if(duplicatedTrgCVItemCount > 0)
            {
                syncItemCopyContext.error("cannot use source catalog version " + syncItemCopyContext.getTargetVersion() + " for synchronization since it owns " + duplicatedTrgCVItemCount + " duplicate " + root_ct
                                .getCode() + " IDs ");
                gotNoError = false;
            }
        }
        long time2 = System.currentTimeMillis();
        if(syncItemCopyContext.isDebugEnabled())
        {
            syncItemCopyContext.debug("checking version validity for " + syncItemCopyContext.getSourceVersion() + " -> " + syncItemCopyContext
                            .getTargetVersion() + " took " + time2 - time1 + "ms");
        }
        return gotNoError;
    }


    protected ItemCopyContext createCopyContext(SyncItemCronJob cronJob, CatalogVersion srcV, CatalogVersion tgtV, Map<PK, PK> alreadyFinished)
    {
        return (ItemCopyContext)new SyncItemCopyContext(this, cronJob, this, srcV, tgtV, alreadyFinished);
    }


    protected void registerSynchronizedItem(SyncItemCronJob cronjob, Item source, Item copy, String message)
    {
        cronjob.finishedItem(source, copy, message);
    }


    protected List<PK> getSingleVersionCatalogItems(SessionContext ctx, ComposedType type, String additionalQueryRestrictions, boolean fromSource, int start, int range)
    {
        String versionAttr = CatalogManager.getInstance().getCatalogVersionAttribute(ctx, type).getQualifier();
        String typeCode = type.getCode();
        boolean exclusive = isExclusiveModeAsPrimitive();
        Map<String, Object> params = new HashMap<>();
        if(exclusive)
        {
            params.put("me", this);
        }
        params.put("srcVer", getSourceVersion());
        params.put("tgtVer", getTargetVersion());
        String query = "SELECT {p:" + Item.PK + "} FROM {" + typeCode + " AS p} WHERE {p:" + versionAttr + "}=?" + (fromSource ? "srcVer" : "tgtVer") + " AND " + ((additionalQueryRestrictions != null) ? ("( " + additionalQueryRestrictions + ") AND ") : "") + "NOT EXISTS ( {{SELECT {ts:" + Item.PK
                        + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + " AS ts} WHERE {ts:syncJob}" + (exclusive ? "=?me" : "=0") + " AND " + (exclusive ? "" : "{ts:targetVersion}=?tgtVer AND {ts:sourceVersion}=?srcVer AND ") + "{ts:" + (fromSource ? "sourceItem" : "targetItem")
                        + "}={p:" + Item.PK + "} }} ) AND NOT EXISTS( {{SELECT {pp:" + Item.PK + "} FROM {" + typeCode + " as pp LEFT JOIN " + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + " AS ts2 ON {ts2:" + (fromSource ? "targetItem" : "sourceItem") + "}={pp:" + Item.PK + "} AND {ts2:syncJob}"
                        + (exclusive ? "=?me" : "=0") + (exclusive ? "}" : " AND {ts2:targetVersion}=?tgtVer AND {ts2:sourceVersion}=?srcVer } ") + "\tWHERE ";
        for(AttributeDescriptor keyAd : CatalogManager.getInstance().getUniqueKeyAttributes(ctx, type))
        {
            query = query + "{pp:" + query + "}={p:" + keyAd.getQualifier() + "} AND ";
        }
        query = query + query + "{pp:" + ((additionalQueryRestrictions != null) ? ("( " + additionalQueryRestrictions + ") AND ") : "") + "}=?" + versionAttr + " AND {ts2:" + (fromSource ? "tgtVer" : "srcVer") + "} IS NULL }} ) ";
        query = query + "ORDER BY ";
        if(Product.class.isAssignableFrom(type.getJaloClass()))
        {
            ComposedType variantType = TypeManager.getInstance().getComposedType(VariantProduct.class);
            Collection<ComposedType> vtypes = new ArrayList(variantType.getAllSubTypes());
            vtypes.add(variantType);
            params.put("variantTypes", vtypes);
            query = query + "CASE WHEN {p:" + query + "} IN (?variantTypes) THEN 1 ELSE 0 END ASC,";
        }
        query = query + "{p:" + query + "} ASC, {p:" + Item.CREATION_TIME + "} ASC";
        return FlexibleSearch.getInstance()
                        .search(ctx, query, params, Collections.singletonList(PK.class), true, true, start, range)
                        .getResult();
    }


    protected void copyItem(ItemCopyContext itemCopyContext, SyncItemCronJob cronJob, Item src, Item tgt)
    {
        itemCopyContext.copy(src, tgt);
    }


    protected List<ModifiedItemsPair> getModifiedCatalogItemPairs(SessionContext ctx, ComposedType type, int start, int range, boolean forceUpdate)
    {
        String typeCode = type.getCode();
        boolean exclusive = isExclusiveModeAsPrimitive();
        Map<Object, Object> params = new HashMap<>();
        if(exclusive)
        {
            params.put("me", this);
        }
        else
        {
            params.put("srcVer", getSourceVersion());
            params.put("tgtVer", getTargetVersion());
        }
        String query = "SELECT {p1:" + Item.PK + "},{p2:" + Item.PK + "},{ts:lastSyncSourceModifiedTime} FROM {" + typeCode + " AS p1 JOIN " + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + " AS ts ON {ts:syncJob}" + (exclusive ? "=?me" : "=0") + " AND " + (exclusive
                        ? ""
                        : "{ts.targetVersion}=?tgtVer AND {ts.sourceVersion}=?srcVer AND ") + "{p1:" + Item.PK + "}={ts:sourceItem} JOIN " + typeCode + " AS p2 ON {p2:" + Item.PK + "}={ts:targetItem} }" + (!forceUpdate
                        ? ("WHERE {ts:lastSyncSourceModifiedTime} < {p1:" + Item.MODIFIED_TIME + "} ")
                        : "") + "ORDER BY ";
        if(Product.class.isAssignableFrom(type.getJaloClass()))
        {
            ComposedType variantType = TypeManager.getInstance().getComposedType(VariantProduct.class);
            Collection<ComposedType> vtypes = new ArrayList(variantType.getAllSubTypes());
            vtypes.add(variantType);
            params.put("variantTypes", vtypes);
            query = query + "CASE WHEN {p1:" + query + "} IN (?variantTypes) THEN 1 ELSE 0 END ASC,";
        }
        query = query + "{p1:" + query + "} ASC,{p1:" + Item.CREATION_TIME + "} ASC";
        List<List<Object>> rows = FlexibleSearch.getInstance().search(ctx, query, params, Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class, Date.class}, ), true, true, start, range).getResult();
        List<ModifiedItemsPair> ret = new ArrayList<>(rows.size());
        for(List<Object> row : rows)
        {
            ret.add(new ModifiedItemsPair((PK)row.get(0), (PK)row.get(1), (Date)row.get(2)));
        }
        return ret;
    }


    protected List<ModifiedItemsPair> getPossibleCatalogItemPairs(SessionContext ctx, ComposedType type, String additionalQueryRestrictions, int start, int range)
    {
        Collection<AttributeDescriptor> uniqueKeyAttributes = CatalogManager.getInstance().getUniqueKeyAttributes(ctx, type);
        String versionAttr = CatalogManager.getInstance().getCatalogVersionAttribute(ctx, type).getQualifier();
        String typeCode = type.getCode();
        boolean exclusive = isExclusiveModeAsPrimitive();
        Map<Object, Object> params = new HashMap<>();
        if(exclusive)
        {
            params.put("me", this);
        }
        params.put("srcVer", getSourceVersion());
        params.put("tgtVer", getTargetVersion());
        String query = "SELECT {p1:" + Item.PK + "},{p2:" + Item.PK + "} FROM {" + typeCode + " AS p1 JOIN " + typeCode + " AS p2 ON {p1:" + versionAttr + "}=?srcVer AND {p2:" + versionAttr + "}=?tgtVer ";
        for(AttributeDescriptor keyAd : uniqueKeyAttributes)
        {
            query = query + " AND {p1:" + query + "}={p2:" + keyAd.getQualifier() + "}";
        }
        query = query + " } ";
        query = query + "WHERE " + query + "NOT EXISTS ({{SELECT {" + ((additionalQueryRestrictions != null) ? ("( " + additionalQueryRestrictions + ") AND ") : "") + "} FROM {" + Item.PK + " AS ts} WHERE {ts:syncJob}" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP + "  AND " + (exclusive
                        ? "=?me"
                        : "=0") + "({ts:sourceItem}={p1:" + (exclusive ? "" : "{ts.targetVersion}=?tgtVer AND {ts.sourceVersion}=?srcVer AND ") + "} )}}) AND NOT EXISTS ({{ SELECT {" + Item.PK + "} FROM {" + Item.PK + " AS ts} WHERE {ts:syncJob}" + GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP
                        + "  AND " + (exclusive ? "=?me" : "=0") + "({ts:targetItem}={p2:" + (exclusive ? "" : "{ts.targetVersion}=?tgtVer AND {ts.sourceVersion}=?srcVer AND ") + "} )}})ORDER BY ";
        if(Product.class.isAssignableFrom(type.getJaloClass()))
        {
            ComposedType variantType = TypeManager.getInstance().getComposedType(VariantProduct.class);
            Collection<ComposedType> vtypes = new ArrayList(variantType.getAllSubTypes());
            vtypes.add(variantType);
            params.put("variantTypes", vtypes);
            query = query + "CASE WHEN {p1:" + query + "} IN (?variantTypes) THEN 1 ELSE 0 END ASC,";
        }
        query = query + "{p1:" + query + "} ASC,{p1:" + Item.CREATION_TIME + "} ASC";
        List<List<Object>> rows = FlexibleSearch.getInstance().search(ctx, query, params, Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class}, ), true, true, start, range).getResult();
        List<ModifiedItemsPair> ret = new ArrayList<>(rows.size());
        for(List<Object> row : rows)
        {
            ret.add(new ModifiedItemsPair((PK)row.get(0), (PK)row.get(1), null));
        }
        return ret;
    }


    protected void removeItem(ItemCopyContext itemCopyContext, SyncItemCronJob cronJob, Item removeItem)
    {
        if(removeItem.isAlive())
        {
            try
            {
                removeItem.remove(itemCopyContext.getCtx());
                if(itemCopyContext.isInfoEnabled())
                {
                    itemCopyContext.info("removed " + removeItem.getClass().getName() + " " + removeItem.getPK());
                }
            }
            catch(ConsistencyCheckException e)
            {
                if(itemCopyContext.isErrorEnabled())
                {
                    itemCopyContext.error("could not remove item " + removeItem + " due to " + e.getLocalizedMessage());
                }
            }
        }
    }


    protected Set<Item> getMissingTargetItems(Collection<? extends Item> sourceItems, Collection<? extends Item> targetItems, CatalogVersion sourceVersion)
    {
        Set<String> srcIDs = new HashSet<>();
        CatalogManager catalogManager = CatalogManager.getInstance();
        for(Iterator<? extends Item> iter = sourceItems.iterator(); iter.hasNext(); )
        {
            srcIDs.add(catalogManager.getCatalogItemID(iter.next()));
        }
        Set<Item> ret = null;
        for(Item i : targetItems)
        {
            if(!srcIDs.contains(catalogManager.getCatalogItemID(i)) &&
                            CatalogManager.getInstance().getCounterpartItem(i, sourceVersion) == null)
            {
                if(ret == null)
                {
                    ret = new LinkedHashSet<>();
                }
                ret.add(i);
            }
        }
        return (ret != null) ? ret : Collections.EMPTY_SET;
    }


    public void addCatalogItemsToSync(SyncItemCronJob cronJob, Collection<? extends Item> items)
    {
        CatalogVersion tgt = getTargetVersion();
        CatalogManager catalogManager = CatalogManager.getInstance();
        List<PK[]> itemPKs = (List)new ArrayList<>(items.size());
        for(Item item1 : items)
        {
            if(item1 instanceof Category)
            {
                throw new IllegalArgumentException("cannot add categories to sync via addCatalogItemsToSync() - use addCategoriesToSync() instead");
            }
            Item copy = catalogManager.getSynchronizedCopy(item1, this);
            if(copy == null)
            {
                copy = catalogManager.getCounterpartItem(item1, tgt);
            }
            itemPKs.add(new PK[] {item1
                            .getPK(), (copy != null) ? copy.getPK() : null});
        }
        for(int i = 0; i < itemPKs.size(); i += 500)
        {
            int end = Math.min(itemPKs.size(), i + 500);
            cronJob.addPendingItems(itemPKs.subList(i, end), false);
            if(LOG.isInfoEnabled())
            {
                LOG.info("added pending sync actions for " + end - i + " items");
            }
        }
    }


    public void addCategoriesToSync(SyncItemCronJob cronJob, Collection<? extends Category> categories, boolean includeSubcategories, boolean includeProducts)
    {
        boolean createNew = isCreateNewItemsAsPrimitive();
        CatalogVersion src = getSourceVersion();
        CatalogVersion tgt = getTargetVersion();
        SessionContext ctx = createSyncSessionContext(cronJob);
        CatalogManager catalogManager = CatalogManager.getInstance();
        Collection<? extends Category> current = categories;
        Set<Category> controlSet = new HashSet<>();
        List<Item> updates = new ArrayList<>();
        List<Item> removes = new ArrayList<>();
        while(current != null && !current.isEmpty())
        {
            List<Category> newCurrents = new ArrayList<>();
            for(Category cat : current)
            {
                Category tgtCat = (Category)catalogManager.getCounterpartItem((Item)cat, tgt);
                if(tgtCat == null && !createNew)
                {
                    continue;
                }
                updates.add(cat);
                updates.add(tgtCat);
                controlSet.add(cat);
                if(includeSubcategories)
                {
                    Set<? extends Item> subCats = new HashSet(cat.getSubcategories(ctx));
                    Set<? extends Item> tgtSubCats = (tgtCat != null) ? new HashSet(tgtCat.getSubcategories(ctx)) : null;
                    if(tgtSubCats != null)
                    {
                        Set<Item> missingTargetItems = getMissingTargetItems(subCats, tgtSubCats, src);
                        for(Item missingOne : missingTargetItems)
                        {
                            removes.add(missingOne);
                        }
                    }
                    newCurrents.addAll(subCats);
                }
                if(includeProducts)
                {
                    Set<Product> products = new HashSet<>(cat.getProducts(ctx));
                    Set<Product> tgtProducts = (tgtCat != null) ? new HashSet<>(tgtCat.getProducts(ctx)) : null;
                    if(tgtProducts != null)
                    {
                        for(Item missingOne : getMissingTargetItems((Collection)products, (Collection)tgtProducts, src))
                        {
                            removes.add(missingOne);
                        }
                    }
                    for(Product p : products)
                    {
                        updates.add(p);
                        updates.add(CatalogManager.getInstance().getCounterpartItem((Item)p, tgt));
                    }
                }
            }
            newCurrents.removeAll(controlSet);
            current = newCurrents;
        }
        List<PK[]> itemPKs = (List)new ArrayList<>(removes.size());
        for(Item toRemove : removes)
        {
            itemPKs.add(new PK[] {null, toRemove
                            .getPK()});
        }
        for(int j = 0; j < itemPKs.size(); j += 500)
        {
            int end = Math.min(itemPKs.size(), j + 500);
            cronJob.addPendingItems(itemPKs.subList(j, end), true);
            if(LOG.isInfoEnabled())
            {
                LOG.info("added pending remove actions for " + end - j + " items");
            }
        }
        itemPKs = (List)new ArrayList<>(updates.size() / 2);
        for(Iterator<Item> it = updates.iterator(); it.hasNext(); )
        {
            Item toUpdate = it.next();
            Item existingCopy = it.next();
            itemPKs.add(new PK[] {toUpdate
                            .getPK(), (existingCopy != null) ? existingCopy.getPK() : null});
        }
        for(int i = 0; i < itemPKs.size(); i += 500)
        {
            int end = Math.min(itemPKs.size(), i + 500);
            cronJob.addPendingItems(itemPKs.subList(i, end), false);
            if(LOG.isInfoEnabled())
            {
                LOG.info("added pending update actions for " + end - i + " items");
            }
        }
    }


    public void configureFullVersionSync(SyncItemCronJob cronJob)
    {
        boolean createNew = isCreateNewItemsAsPrimitive();
        boolean removeMissing = isRemoveMissingItemsAsPrimitive();
        SessionContext ctx = createSyncSessionContext(cronJob);
        boolean force = cronJob.isForceUpdateAsPrimitive();
        int range = -1;
        for(ComposedType rootType : getRootTypes(ctx))
        {
            configureFullSyncForItemType(ctx, cronJob, rootType, getAdditionalQueryRestrictions(ctx, rootType, cronJob), removeMissing, createNew, force, -1);
        }
    }


    protected String getAdditionalQueryRestrictions(SessionContext ctx, ComposedType rootType, SyncItemCronJob cronJob)
    {
        String restriction = (String)rootType.getProperty("catalog.sync.root.type.restriction");
        return StringUtils.isNotBlank(restriction) ? restriction : null;
    }


    protected List<PK[]> toPendingItemsList(List<PK> itemPKs, boolean remove)
    {
        List<PK[]> ret = (List)new ArrayList<>(itemPKs.size());
        for(PK pk : itemPKs)
        {
            ret.add(new PK[] {remove ? null : pk, remove ? pk : null});
        }
        return ret;
    }


    protected List<PK[]> toPendingItemsList(List<ModifiedItemsPair> itemPairs)
    {
        List<PK[]> ret = (List)new ArrayList<>(itemPairs.size());
        for(ModifiedItemsPair pair : itemPairs)
        {
            ret.add(new PK[] {pair
                            .getSource(), pair.getTarget()});
        }
        return ret;
    }


    protected void configureFullSyncForItemType(SessionContext ctx, SyncItemCronJob cronJob, ComposedType type, String additionalQueryRestrictions, boolean removeMissing, boolean createNew, boolean forceUpdate, int range)
    {
        int pendingItemsRange = 500;
        if(removeMissing)
        {
            int i = 0;
            int j = 0;
            int k = 0;
            do
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("searching range [" + i + "," + i + range + "] for removable " + type
                                    .getCode() + " items ...");
                }
                List<PK[]> itemPKs = toPendingItemsList(
                                getSingleVersionCatalogItems(ctx, type, additionalQueryRestrictions, false, i, range), true);
                for(int m = 0; m < itemPKs.size(); m += 500)
                {
                    int end = Math.min(itemPKs.size(), m + 500);
                    cronJob.addPendingItems(itemPKs.subList(m, end), true);
                    j += end - m;
                }
                k += j;
                j = 0;
                if(range <= 0)
                {
                    continue;
                }
                i += range;
            }
            while(range > 0 && j == range);
            if(LOG.isInfoEnabled() && k > 0)
            {
                LOG.info("Scheduled " + k + " " + type.getCode() + " items for removal");
            }
        }
        if(createNew)
        {
            int i = 0;
            int j = 0;
            int k = 0;
            do
            {
                if(LOG.isDebugEnabled())
                {
                    LOG.debug("searching range [" + i + "," + i + range + "] for new " + type.getCode() + " items ...");
                }
                List<PK[]> itemPKs = toPendingItemsList(
                                getSingleVersionCatalogItems(ctx, type, additionalQueryRestrictions, true, i, range), false);
                for(int m = 0; m < itemPKs.size(); m += 500)
                {
                    int end = Math.min(itemPKs.size(), m + 500);
                    cronJob.addPendingItems(itemPKs.subList(m, end), false);
                    j += end - m;
                }
                k += j;
                j = 0;
                if(range <= 0)
                {
                    continue;
                }
                i += range;
            }
            while(range > 0 && j == range);
            if(LOG.isInfoEnabled() && k > 0)
            {
                LOG.info("scheduled " + k + " new " + type.getCode() + " items for copy");
            }
            j = 0;
        }
        int start = 0;
        int count = 0;
        int totalCount = 0;
        do
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("searching range [" + start + "," + start + range + "] for modifed " + type
                                .getCode() + " items (force=" + forceUpdate + ")...");
            }
            List<PK[]> itemPKs = toPendingItemsList(getModifiedCatalogItemPairs(ctx, type, start, range, forceUpdate));
            for(int i = 0; i < itemPKs.size(); i += 500)
            {
                int end = Math.min(itemPKs.size(), i + 500);
                cronJob.addPendingItems(itemPKs.subList(i, end), false);
                count += end - i;
            }
            totalCount += count;
            count = 0;
            if(range <= 0)
            {
                continue;
            }
            start += range;
        }
        while(range > 0 && count == range);
        if(LOG.isInfoEnabled() && totalCount > 0)
        {
            LOG.info("scheduled " + totalCount + " modified " + type.getCode() + " items for update");
        }
        start = 0;
        count = 0;
        totalCount = 0;
        do
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("searching range [" + start + "," + start + range + "] for non-synchronized " + type.getCode() + " item pairs ...");
            }
            List<PK[]> itemPKs = toPendingItemsList(
                            getPossibleCatalogItemPairs(ctx, type, additionalQueryRestrictions, start, range));
            for(int i = 0; i < itemPKs.size(); i += 500)
            {
                int end = Math.min(itemPKs.size(), i + 500);
                cronJob.addPendingItems(itemPKs.subList(i, end), false);
                count += end - i;
            }
            totalCount += count;
            count = 0;
            if(range <= 0)
            {
                continue;
            }
            start += range;
        }
        while(range > 0 && count == range);
        if(LOG.isInfoEnabled() && totalCount > 0)
        {
            LOG.info("scheduled " + totalCount + " non-synchronized " + type.getCode() + " items for update");
        }
    }


    public CompletionInfo getCompletionInfo(SyncItemCronJob cronJob)
    {
        if(cronJob.getConfigurator() != null)
        {
            CompletionInfo configInfo = cronJob.getConfigurator().getCompletionInfo();
            return (configInfo != null) ? new CompletionInfo(configInfo.getCurrentTask(), configInfo.getPercentage(), configInfo
                            .getTotalCount(), configInfo.getCompletedCount(), 5 * configInfo
                            .getPercentage() / 100) :
                            new CompletionInfo("configuring", 0, 0, 0, 0);
        }
        List counts = FlexibleSearch.getInstance().search("SELECT {done},{copiedImplicitely}, count(*) FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR + "} WHERE {cronJob}=?cj GROUP BY {done},{copiedImplicitely}", Collections.singletonMap("cj", cronJob),
                        Arrays.asList((Class<?>[][])new Class[] {Boolean.class, Boolean.class, Integer.class}, ), true, true, 0, -1).getResult();
        int completed = 0;
        int pending = 0;
        int completedImplicitely = 0;
        for(Iterator<List> it = counts.iterator(); it.hasNext(); )
        {
            List<Integer> row = it.next();
            boolean done = Boolean.TRUE.equals(row.get(0));
            boolean implicit = Boolean.TRUE.equals(row.get(1));
            int count = ((Integer)row.get(2)).intValue();
            if(!done)
            {
                pending = count;
                continue;
            }
            if(!implicit)
            {
                completed = count;
                continue;
            }
            completedImplicitely = count;
        }
        int percentage = (pending + completed > 0) ? (completed * 100 / (pending + completed)) : 100;
        if(LOG.isDebugEnabled())
        {
            LOG.debug("got completion status of " + getCode() + " = " + percentage + " (complete=" + completed + ",pending=" + pending + ",implicite=" + completedImplicitely + ")");
        }
        return new CompletionInfo("synchronizing", percentage, pending + completed, completed, percentage);
    }


    protected CronJob.CronJobResult performCronJob(CronJob cronJob)
    {
        SyncItemCronJob cronJob1 = (SyncItemCronJob)cronJob;
        SyncItemCopyContext syncItemCopyContext = null;
        try
        {
            if(!getSession().getUser().equals(UserManager.getInstance().getAdminEmployee()))
            {
                LOG.info("session user is not admin but " + getSession().getUser() + " - due to possible restrictions synchronization may not cover all items");
            }
            syncItemCopyContext = (SyncItemCopyContext)createCopyContext(cronJob1, getSourceVersion(), getTargetVersion(), cronJob1
                            .getFinishedItemPKMap());
            if(!checkCatalogVersionValidity(cronJob1, syncItemCopyContext))
            {
                LOG.error("aborted due to version validation error");
                return cronJob1.getAbortResult();
            }
            initializeTimeCounter(cronJob1);
            if(cronJob1.getConfigurator() != null)
            {
                long l = System.currentTimeMillis();
                if(LOG.isInfoEnabled())
                {
                    LOG.info("starting configuration ..");
                }
                cronJob1.getConfigurator().configureCronjob(cronJob1, syncItemCopyContext);
                if(LOG.isInfoEnabled())
                {
                    LOG.info("finished configuration in " + System.currentTimeMillis() - l + "ms");
                }
                cronJob1.setConfigurator(null);
            }
            else if(cronJob1.getPendingDescriptorsCount() > 0)
            {
                long l = System.currentTimeMillis();
                if(LOG.isInfoEnabled())
                {
                    LOG.info("starting configureFullVersionSync ..");
                }
                configureFullVersionSync(cronJob1);
                if(LOG.isInfoEnabled())
                {
                    LOG.info("finished configureFullVersionSync in " + System.currentTimeMillis() - l + "ms");
                }
            }
            long time1 = System.currentTimeMillis();
            if(LOG.isInfoEnabled())
            {
                LOG.info("starting synchronization...");
            }
            boolean errorsOccured = doSynchronization(syncItemCopyContext, cronJob1);
            long time2 = System.currentTimeMillis();
            if(LOG.isInfoEnabled())
            {
                if(errorsOccured)
                {
                    LOG.warn("finished synchronization in " + time2 - time1 + "ms. There were errors during the synchronization!");
                }
                else
                {
                    LOG.info("finished synchronization in " + time2 - time1 + "ms. No errors.");
                }
                LOG.info("starting pending references processing...");
            }
            syncItemCopyContext.processPendingReferences();
            if(LOG.isInfoEnabled())
            {
                LOG.info("finished pending references processing in " + System.currentTimeMillis() - time2 + "ms");
            }
            return cronJob.getFinishedResult(!errorsOccured);
        }
        catch(AbortCronJobException e)
        {
            return doAbort(cronJob1);
        }
        finally
        {
            cronJob1.setConfigurator(null);
            if(syncItemCopyContext != null)
            {
                syncItemCopyContext.cleanup();
            }
        }
    }


    protected boolean doSynchronization(SyncItemCopyContext syncItemCopyContext, SyncItemCronJob cronJob) throws AbortCronJobException
    {
        int range = 100;
        List descriptors = null;
        EnumerationValue errorMode = cronJob.getErrorMode(syncItemCopyContext.getCtx());
        boolean ignoreErrors = (errorMode != null && GeneratedCronJobConstants.Enumerations.ErrorMode.IGNORE.equals(errorMode.getCode()));
        boolean errorsOccured = false;
        while(true)
        {
            descriptors = cronJob.getPendingDescriptors(0, 100);
            for(Iterator<ItemSyncDescriptor> it = descriptors.iterator(); it.hasNext(); )
            {
                ItemSyncDescriptor isd = it.next();
                if(cronJob.isRequestAbortAsPrimitive())
                {
                    throw new AbortCronJobException("abort requested");
                }
                Item src = isd.getChangedItem(syncItemCopyContext.getCtx());
                Item tgt = isd.getTargetItem(syncItemCopyContext.getCtx());
                try
                {
                    if(src == null && tgt != null)
                    {
                        removeItem((ItemCopyContext)syncItemCopyContext, cronJob, tgt);
                        isd.setDone(syncItemCopyContext.getCtx(), true);
                        continue;
                    }
                    if(src != null && CatalogManager.getInstance().isCatalogItem(src) && syncItemCopyContext
                                    .isFromSourceVersion(src))
                    {
                        copyItem((ItemCopyContext)syncItemCopyContext, cronJob, src, tgt);
                        if(!isd.isDoneAsPrimitive(syncItemCopyContext.getCtx()))
                        {
                            if(LOG.isDebugEnabled())
                            {
                                LOG.debug("found descriptor " + isd + " still pending after copying item - correcting manually ");
                            }
                            isd.setDone(syncItemCopyContext.getCtx(), true);
                        }
                        continue;
                    }
                    LOG.error("found invalid sync descriptor " + isd + " - ignored");
                    isd.setDone(syncItemCopyContext.getCtx(), true);
                    isd.setDescription(syncItemCopyContext.getCtx(), " invalid descriptor - did not sync");
                }
                catch(RuntimeException e)
                {
                    if(ignoreErrors)
                    {
                        errorsOccured = true;
                        String errorTxt = "caught unexpected error " + e.getLocalizedMessage() + " synchronizing " + ((src != null) ? (String)src.getPK() : "n/a") + "->" + ((tgt != null) ? (String)tgt.getPK() : "n/a") + "\n" + Utilities.getStackTraceAsString(e);
                        LOG.error(errorTxt);
                        isd.setDone(syncItemCopyContext.getCtx(), true);
                        isd.setDescription(syncItemCopyContext.getCtx(), errorTxt);
                        continue;
                    }
                    throw e;
                }
            }
            if(descriptors.size() != 100)
            {
                return errorsOccured;
            }
        }
    }


    protected CronJob.CronJobResult doAbort(SyncItemCronJob cronJob)
    {
        LOG.warn("cronjob " + cronJob.getCode() + " was aborted by client");
        cronJob.setRequestAbort(null);
        return cronJob.getAbortResult();
    }


    protected boolean canSelectAttribute(AttributeDescriptor attributeDescriptor)
    {
        if((attributeDescriptor.getModifiers() & 0x802) == 0)
        {
            return false;
        }
        ComposedType declaringType = attributeDescriptor.getDeclaringEnclosingType();
        String quali = attributeDescriptor.getQualifier();
        String full = declaringType.getCode() + "." + declaringType.getCode();
        if(HIDDEN_ATTRIBUTES.contains(full))
        {
            return false;
        }
        CatalogManager catalogManager = CatalogManager.getInstance();
        if(catalogManager.isCatalogItem(declaringType))
        {
            return !quali.equalsIgnoreCase(catalogManager.getCatalogVersionAttributeQualifier(declaringType));
        }
        return true;
    }


    @ForceJALO(reason = "abstract method implementation")
    public Map<AttributeDescriptor, Boolean> getAllExportAttributeDescriptors(SessionContext ctx)
    {
        Map<AttributeDescriptor, Boolean> map = new HashMap<>();
        for(List<List> pair : getAttributeAndConfigPairs())
        {
            AttributeDescriptor attributeDescriptor = (AttributeDescriptor)pair.get(0);
            if(canSelectAttribute(attributeDescriptor))
            {
                SyncAttributeDescriptorConfig cfg = (SyncAttributeDescriptorConfig)pair.get(1);
                map.put(attributeDescriptor, (cfg == null || cfg.isIncludedInSyncAsPrimitive(ctx)) ? Boolean.TRUE : Boolean.FALSE);
            }
        }
        return map;
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setAllExportAttributeDescriptors(SessionContext ctx, Map syncMap)
    {
        for(List<List> pair : getAttributeAndConfigPairs())
        {
            AttributeDescriptor attributeDescriptor = (AttributeDescriptor)pair.get(0);
            if(syncMap.containsKey(attributeDescriptor))
            {
                SyncAttributeDescriptorConfig cfg = (SyncAttributeDescriptorConfig)pair.get(1);
                boolean include = Boolean.TRUE.equals(syncMap.get(attributeDescriptor));
                if(include)
                {
                    if(cfg != null)
                    {
                        cfg.setIncludedInSync(true);
                    }
                    continue;
                }
                if(cfg == null)
                {
                    cfg = createDefaultConfigFor(ctx, attributeDescriptor);
                }
                cfg.setIncludedInSync(false);
            }
        }
    }


    protected boolean configAlreadyExists(AttributeDescriptor attributeDescriptor)
    {
        return (getConfigFor(attributeDescriptor, false) != null);
    }


    public SyncAttributeDescriptorConfig getConfigFor(AttributeDescriptor attributeDescriptor, boolean createOnDemand)
    {
        return getConfigFor(getSession().getSessionContext(), attributeDescriptor, createOnDemand);
    }


    public SyncAttributeDescriptorConfig getConfigFor(SessionContext ctx, AttributeDescriptor attributeDescriptor, boolean createOnDemand)
    {
        Preconditions.checkArgument((attributeDescriptor != null));
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("ad", attributeDescriptor.isInherited() ?
                        attributeDescriptor.getDeclaringEnclosingType().getAttributeDescriptorIncludingPrivate(attributeDescriptor
                                        .getQualifier()) : attributeDescriptor);
        List<SyncAttributeDescriptorConfig> rows = FlexibleSearch.getInstance().search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.SYNCATTRIBUTEDESCRIPTORCONFIG + "} WHERE {attributeDescriptor}=?ad and {syncJob}=?me ", params, SyncAttributeDescriptorConfig.class)
                        .getResult();
        if(rows.isEmpty())
        {
            return createOnDemand ? createDefaultConfigFor(ctx, attributeDescriptor) : null;
        }
        return rows.get(0);
    }


    protected List<List> getAttributeAndConfigPairs()
    {
        Set<ComposedType> excludedTypes = new HashSet<>();
        excludedTypes.addAll(TypeManager.getInstance().getComposedType(ViewType.class).getAllInstances());
        excludedTypes.addAll(TypeManager.getInstance().getComposedType("RelationMetaType").getAllInstances());
        excludedTypes.addAll(TypeManager.getInstance().getComposedType("ConfigProxyMetaType").getAllInstances());
        try
        {
            ComposedType composedType1 = TypeManager.getInstance().getComposedType("WizardBusinessItem");
            excludedTypes.add(composedType1);
            excludedTypes.addAll(composedType1.getAllSubTypes());
        }
        catch(JaloItemNotFoundException jaloItemNotFoundException)
        {
        }
        ComposedType composedType = TypeManager.getInstance().getComposedType(TypeManagerManaged.class);
        excludedTypes.add(composedType);
        excludedTypes.addAll(composedType.getAllSubTypes());
        composedType = TypeManager.getInstance().getComposedType(GeneratedCatalogConstants.TC.CATALOG);
        excludedTypes.add(composedType);
        excludedTypes.addAll(composedType.getAllSubTypes());
        composedType = TypeManager.getInstance().getComposedType(GeneratedCatalogConstants.TC.CATALOGVERSION);
        excludedTypes.add(composedType);
        excludedTypes.addAll(composedType.getAllSubTypes());
        composedType = TypeManager.getInstance().getComposedType(GeneratedCatalogConstants.TC.ITEMSYNCTIMESTAMP);
        excludedTypes.add(composedType);
        excludedTypes.addAll(composedType.getAllSubTypes());
        composedType = TypeManager.getInstance().getComposedType(GeneratedCatalogConstants.TC.SYNCITEMJOB);
        excludedTypes.add(composedType);
        excludedTypes.addAll(composedType.getAllSubTypes());
        composedType = TypeManager.getInstance().getComposedType(GeneratedCatalogConstants.TC.SYNCITEMCRONJOB);
        excludedTypes.add(composedType);
        excludedTypes.addAll(composedType.getAllSubTypes());
        String query = "SELECT {ad." + AttributeDescriptor.MODIFIERS + "},{ad:" + Item.PK + "},{cfg:" + Item.PK + "} FROM {" + TypeManager.getInstance().getComposedType(AttributeDescriptor.class).getCode() + " AS ad LEFT JOIN " + GeneratedCatalogConstants.TC.SYNCATTRIBUTEDESCRIPTORCONFIG
                        + " AS cfg ON {ad:" + Item.PK + "}={cfg:attributeDescriptor} AND {cfg:syncJob}=?me } ";
        if(!excludedTypes.isEmpty())
        {
            query = query + " WHERE {ad." + query + "} NOT IN (";
            int index = 0;
            for(ComposedType ct : excludedTypes)
            {
                query = query + query + ((index > 1) ? "," : "");
                index++;
            }
            query = query + " ) ";
        }
        List<List> rows = FlexibleSearch.getInstance().search(query, Collections.singletonMap("me", this), Arrays.asList((Class<?>[][])new Class[] {Integer.class, PK.class, PK.class}, ), true, true, 0, -1).getResult();
        List<List> ret = new ArrayList<>(rows.size());
        JaloSession jSession = getSession();
        for(List<Integer> row : rows)
        {
            int modifiers = ((Integer)row.get(0)).intValue();
            if((modifiers & 0x400) == 1024 || (modifiers & 0x802) == 0)
            {
                continue;
            }
            PK adPK = (PK)row.get(1);
            PK syncCfgPK = (PK)row.get(2);
            ret.add(Arrays.asList(new Object[] {jSession.getItem(adPK), (syncCfgPK != null) ? jSession.getItem(syncCfgPK) : null}));
        }
        return ret;
    }


    protected SyncAttributeDescriptorConfig createDefaultConfigFor(SessionContext ctx, AttributeDescriptor attributeDescriptor)
    {
        Map<Object, Object> values = new HashMap<>();
        values.put("syncJob", this);
        values.put("attributeDescriptor", attributeDescriptor);
        values.put("includedInSync", Boolean.TRUE);
        return CatalogManager.getInstance().createSyncAttributeDescriptorConfig(ctx, values);
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection<SyncAttributeDescriptorConfig> getSyncAttributeConfigurations(SessionContext ctx)
    {
        Collection<SyncAttributeDescriptorConfig> ret = new ArrayList<>();
        for(List<AttributeDescriptor> pair : getAttributeAndConfigPairs())
        {
            AttributeDescriptor attributeDescriptor = pair.get(0);
            Preconditions.checkArgument(!attributeDescriptor.isInherited());
            if(canSelectAttribute(attributeDescriptor))
            {
                SyncAttributeDescriptorConfig cfg = (SyncAttributeDescriptorConfig)pair.get(1);
                ret.add((cfg != null) ? cfg : createDefaultConfigFor(ctx, attributeDescriptor));
            }
        }
        return ret;
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setSyncAttributeConfigurations(SessionContext ctx, Collection param)
    {
    }


    @ForceJALO(reason = "something else")
    public List<ComposedType> getRootTypes(SessionContext ctx)
    {
        List<ComposedType> ret = super.getRootTypes(ctx);
        return assureCatalogItemTypes(ret.isEmpty() ? getDefaultRootTypes() : ret);
    }


    protected List<ComposedType> assureCatalogItemTypes(List<ComposedType> types)
    {
        List<ComposedType> ret = null;
        Collection<String> illegal = new LinkedHashSet<>();
        if(types != null)
        {
            ret = new ArrayList<>(types.size());
            CatalogManager catalogManager = CatalogManager.getInstance();
            for(ComposedType ct : types)
            {
                if(ret.contains(ct))
                {
                    continue;
                }
                if(catalogManager.isCatalogItem(ct))
                {
                    ret.add(ct);
                    continue;
                }
                illegal.add(ct.getCode());
            }
        }
        if(!illegal.isEmpty())
        {
            LOG.error("found invalid catalog item types " + illegal + " - ignored");
        }
        return ret;
    }


    @ForceJALO(reason = "something else")
    public void setRootTypes(SessionContext ctx, List<ComposedType> value)
    {
        if(value != null)
        {
            Set<String> illegal = new LinkedHashSet<>();
            Set<ComposedType> illegal2 = new LinkedHashSet<>();
            CatalogManager catalogManager = CatalogManager.getInstance();
            for(ComposedType ct : value)
            {
                if(!catalogManager.isCatalogItem(ct))
                {
                    illegal.add(ct.getCode());
                    continue;
                }
                for(ComposedType other : value)
                {
                    if(!ct.equals(other) && (ct.isAssignableFrom((Type)other) || other.isAssignableFrom((Type)ct)))
                    {
                        illegal2.add(ct);
                        illegal2.add(other);
                    }
                }
            }
            if(!illegal.isEmpty())
            {
                throw new JaloInvalidParameterException("found illegal root types " + illegal + " - all root types must be catalog item types !", 0);
            }
            if(!illegal2.isEmpty())
            {
                throw new JaloInvalidParameterException("found overlapping root types " + illegal + " - root types must not be assignable from each other !", 0);
            }
        }
        super.setRootTypes(ctx, (value != null) ? new ArrayList(new LinkedHashSet(value)) : null);
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection<Language> getEffectiveSyncLanguages(SessionContext ctx)
    {
        Set<Language> preset = getSyncLanguages(ctx);
        if(preset == null || preset.isEmpty())
        {
            Set<Language> ret = new LinkedHashSet<>(getSourceVersion().getLanguages());
            ret.retainAll(getTargetVersion().getLanguages());
            return ret;
        }
        return preset;
    }
}
