package de.hybris.platform.catalog.jalo;

import de.hybris.platform.catalog.constants.GeneratedCatalogConstants;
import de.hybris.platform.core.PK;
import de.hybris.platform.directpersistence.annotation.ForceJALO;
import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.flexiblesearch.FlexibleSearch;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.TypeManager;
import de.hybris.platform.jalo.user.User;
import de.hybris.platform.jalo.user.UserManager;
import de.hybris.platform.persistence.property.JDBCValueMappings;
import de.hybris.platform.util.ItemPropertyValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class SyncItemCronJob extends GeneratedSyncItemCronJob
{
    private static Logger LOG = Logger.getLogger(SyncItemCronJob.class.getName());
    private transient Configurator conf = null;
    private transient Integer changeDescriptorSequenceNumber;


    @ForceJALO(reason = "something else")
    public Boolean isChangeRecordingEnabled(SessionContext ctx)
    {
        return Boolean.TRUE;
    }


    @ForceJALO(reason = "something else")
    public void setChangeRecordingEnabled(SessionContext ctx, boolean enabled)
    {
    }


    protected JaloSession createSessionForCronJob(JaloSession jaloSession)
    {
        JaloSession jSession = super.createSessionForCronJob(jaloSession);
        if(getJob().getSessionUser() == null || getJob().getSessionUser().isAnonymousCustomer())
        {
            jSession.setUser((User)UserManager.getInstance().getAdminEmployee());
        }
        else
        {
            jSession.setUser(getJob().getSessionUser());
        }
        return jSession;
    }


    protected ItemSyncDescriptor finishedItem(Item src, Item copy, String message)
    {
        ItemSyncDescriptor isd = getPendingDescriptorFor(src, true);
        if(isd != null)
        {
            isd.setDone(true);
            isd.setTargetItem(copy);
            isd.setDescription(message);
        }
        else
        {
            Item.ItemAttributeMap<String, Boolean> itemAttributeMap = new Item.ItemAttributeMap();
            itemAttributeMap.put("done", Boolean.TRUE);
            itemAttributeMap.put("copiedImplicitely", Boolean.TRUE);
            itemAttributeMap.put("targetItem", copy);
            isd = (ItemSyncDescriptor)addChangeDescriptor(
                            TypeManager.getInstance().getComposedType(GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR), null, "syncronize", src, message, (Map)itemAttributeMap);
        }
        return isd;
    }


    protected Item getFinishedItemCopy(Item source)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("source", source);
        params.put("done", Boolean.TRUE);
        List<Item> coll = FlexibleSearch.getInstance().search("SELECT {targetItem} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR + "} WHERE {cronJob}=?me AND {changedItem}=?source AND {done}=?done", params, Item.class).getResult();
        if(coll.isEmpty())
        {
            return null;
        }
        if(coll.size() == 1)
        {
            Item copy = coll.get(0);
            return copy;
        }
        throw new IllegalStateException("multiple sync items found for cronjob " + this + " and source item " + source);
    }


    protected Map<PK, PK> getFinishedItemPKMap()
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("done", Boolean.TRUE);
        List<List<PK>> rows = FlexibleSearch.getInstance()
                        .search("SELECT DISTINCT {changedItem},{targetItem} FROM  {" + GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR + "} WHERE {cronJob}=?me AND {done}=?done AND {changedItem} IS NOT NULL", params, Arrays.asList((Class<?>[][])new Class[] {PK.class, PK.class}, ), true, true, 0, -1)
                        .getResult();
        Map<Object, Object> mappings = new HashMap<>();
        for(List<PK> row : rows)
        {
            mappings.put(row.get(0), row.get(1));
        }
        return (Map)mappings;
    }


    protected ItemSyncDescriptor getPendingDescriptorFor(Item item, boolean asSource)
    {
        return getPendingDescriptorFor(item.getPK(), asSource);
    }


    protected Map<PK, ItemSyncDescriptor> getPendingDescriptorsFor(List<PK[]> items, boolean remove)
    {
        if(items.isEmpty())
        {
            return Collections.EMPTY_MAP;
        }
        List<PK> pkList = new ArrayList<>(items.size());
        for(PK[] pks : items)
        {
            pkList.add(remove ? pks[1] : pks[0]);
        }
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("items", pkList);
        params.put("done", Boolean.FALSE);
        List<List<Object>> rows = FlexibleSearch.getInstance()
                        .search("SELECT {" + Item.PK + "},{" + (remove ? "targetItem" : "changedItem") + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR + "} WHERE {cronJob}=?me AND {done}=?done AND {" + (remove ? "targetItem" : "changedItem") + "} IN (?items ) ", params,
                                        Arrays.asList((Class<?>[][])new Class[] {ItemSyncDescriptor.class, PK.class}, ), true, true, 0, -1).getResult();
        Map<PK, ItemSyncDescriptor> ret = new HashMap<>(rows.size());
        for(List<Object> row : rows)
        {
            ret.put((PK)row.get(1), (ItemSyncDescriptor)row.get(0));
        }
        return ret;
    }


    protected ItemSyncDescriptor getPendingDescriptorFor(PK item, boolean asSource)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("item", item);
        params.put("done", Boolean.FALSE);
        List<ItemSyncDescriptor> coll = FlexibleSearch.getInstance().search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR + "} WHERE {cronJob}=?me AND {done}=?done AND {" + (asSource ? "changedItem" : "targetItem") + "}=?item", params, ItemSyncDescriptor.class)
                        .getResult();
        if(coll.isEmpty())
        {
            return null;
        }
        if(coll.size() > 1)
        {
            throw new IllegalStateException("multiple descriptors found item " + item + " and cronjob " + this);
        }
        return coll.get(0);
    }


    public int getPendingDescriptorsCount()
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("done", Boolean.FALSE);
        return (
                        (Integer)FlexibleSearch.getInstance()
                                        .search("SELECT count(*) FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR + "} WHERE {cronJob}=?me AND {done}=?done ", params, Integer.class)
                                        .getResult()
                                        .iterator()
                                        .next()).intValue();
    }


    protected List getPendingDescriptors(int start, int range)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("done", Boolean.FALSE);
        return
                        FlexibleSearch.getInstance()
                                        .search("SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR + "} WHERE {cronJob}=?me AND {done}=?done ORDER BY {sequenceNumber} ASC ", params,
                                                        Collections.singletonList(ItemSyncDescriptor.class), true, true, start, range).getResult();
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection getPendingItems(SessionContext ctx)
    {
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT CASE WHEN {changedItem} IS NULL THEN {targetItem} ELSE {changedItem} END FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR + "} WHERE {cronJob}=?me AND {done}=" + JDBCValueMappings.F + " ORDER BY {sequenceNumber} ASC ",
                                                        Collections.singletonMap("me", this),
                                                        Collections.singletonList(Item.class), true, true, 0, -1).getResult();
    }


    @ForceJALO(reason = "something else")
    protected Integer getNextChangeNumber(ComposedType changeDescriptorType)
    {
        synchronized(this)
        {
            if(this.changeDescriptorSequenceNumber == null)
            {
                this.changeDescriptorSequenceNumber = super.getNextChangeNumber(changeDescriptorType);
            }
            else
            {
                this.changeDescriptorSequenceNumber = Integer.valueOf(this.changeDescriptorSequenceNumber.intValue() + 1);
            }
            return this.changeDescriptorSequenceNumber;
        }
    }


    public ItemSyncDescriptor addPendingItem(Item src, Item copy)
    {
        return addPendingItem((src != null) ? src.getPK() : null, (copy != null) ? copy.getPK() : null);
    }


    public List<ItemSyncDescriptor> addPendingItems(List<PK[]> items, boolean isRemoval)
    {
        if(items == null || items.isEmpty())
        {
            return Collections.EMPTY_LIST;
        }
        List<ItemSyncDescriptor> ret = new ArrayList<>(items.size());
        Map<PK, ItemSyncDescriptor> mappings = getPendingDescriptorsFor(items, isRemoval);
        ComposedType sdType = null;
        for(PK[] pks : items)
        {
            PK src = pks[0];
            PK copy = pks[1];
            ItemSyncDescriptor present = mappings.get(isRemoval ? copy : src);
            if(present != null)
            {
                present.setTargetDirect((copy != null) ? new ItemPropertyValue(copy) : null);
                ret.add(present);
                continue;
            }
            Item.ItemAttributeMap attributes = new Item.ItemAttributeMap();
            attributes.put("done", Boolean.FALSE);
            if(copy != null)
            {
                attributes.put("targetItem", new ItemPropertyValue(copy));
            }
            if(sdType == null)
            {
                sdType = TypeManager.getInstance().getComposedType(GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR);
            }
            ret.add((ItemSyncDescriptor)addChangeDescriptor(sdType, null, "syncronize", src, null, (Map)attributes));
        }
        return ret;
    }


    public ItemSyncDescriptor addPendingItem(PK src, PK copy)
    {
        if(src == null && copy == null)
        {
            throw new JaloInvalidParameterException("source and target item cannot be both null", 0);
        }
        boolean isRemoval = (src == null && copy != null);
        ItemSyncDescriptor present = getPendingDescriptorFor(isRemoval ? copy : src, !isRemoval);
        if(present != null)
        {
            present.setTargetDirect((copy != null) ? new ItemPropertyValue(copy) : null);
            return present;
        }
        Item.ItemAttributeMap attributes = new Item.ItemAttributeMap();
        attributes.put("done", Boolean.FALSE);
        if(copy != null)
        {
            attributes.put("targetItem", new ItemPropertyValue(copy));
        }
        return (ItemSyncDescriptor)addChangeDescriptor(
                        TypeManager.getInstance().getComposedType(GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR), null, "syncronize", src, null, (Map)attributes);
    }


    @ForceJALO(reason = "abstract method implementation")
    public void setPendingItems(SessionContext ctx, Collection items)
    {
        boolean removeAll = (items == null || items.isEmpty());
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("done", Boolean.FALSE);
        if(!removeAll)
        {
            params.put("items", items);
        }
        List<ItemSyncDescriptor> allDescriptors = FlexibleSearch.getInstance().search(ctx, "SELECT {" + Item.PK + "} FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR + "} WHERE {cronJob}=?me AND {done}=?done", params, ItemSyncDescriptor.class).getResult();
        Map<Object, Object> descriptorMap = new HashMap<>();
        for(ItemSyncDescriptor isd : allDescriptors)
        {
            if(!isd.isRemoval())
            {
                descriptorMap.put(isd.getChangedItem(), isd);
            }
        }
        if(!removeAll)
        {
            ComposedType itemSyncDescriptorType = TypeManager.getInstance().getComposedType(GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR);
            SyncItemJob job = (SyncItemJob)getJob(ctx);
            CatalogVersion srcVer = job.getSourceVersion(ctx);
            CatalogVersion tgtVer = job.getTargetVersion(ctx);
            CatalogManager catalogManager = CatalogManager.getInstance();
            int count = 0;
            Set<Item> done = new HashSet();
            for(Iterator<Item> it = items.iterator(); it.hasNext(); count++)
            {
                Item item = it.next();
                if(!done.contains(item))
                {
                    boolean fromTarget = false;
                    if(!srcVer.equals(catalogManager.getCatalogVersion(ctx, item)))
                    {
                        if(!(fromTarget = tgtVer.equals(catalogManager
                                        .getCatalogVersion(ctx, item))))
                        {
                            throw new JaloInvalidParameterException("item " + item + " cannot be pending item of " + getCode() + " since it does not belong to job source version " + srcVer + " or target version " + tgtVer, 0);
                        }
                    }
                    if(fromTarget && catalogManager.getCounterpartItem(item, srcVer) != null)
                    {
                        throw new JaloInvalidParameterException("item " + item + " from target version " + tgtVer + " cannot removed by synchronization since it still exists in source version " + srcVer, 0);
                    }
                    ItemSyncDescriptor presentOne = (ItemSyncDescriptor)descriptorMap.get(item);
                    if(presentOne == null)
                    {
                        Item.ItemAttributeMap<String, Boolean> itemAttributeMap = new Item.ItemAttributeMap();
                        itemAttributeMap.put("done", Boolean.FALSE);
                        itemAttributeMap.put("sequenceNumber", Integer.valueOf(count));
                        if(fromTarget)
                        {
                            itemAttributeMap.put("targetItem", item);
                        }
                        addChangeDescriptor(itemSyncDescriptorType, null, "syncronize",
                                        fromTarget ? null : item, null, (Map)itemAttributeMap);
                    }
                    else
                    {
                        presentOne.setSequenceNumber(ctx, count);
                        descriptorMap.remove(item);
                    }
                    done.add(item);
                }
            }
            try
            {
                for(Iterator<Map.Entry> iter = descriptorMap.entrySet().iterator(); iter.hasNext(); )
                {
                    Map.Entry entry = iter.next();
                    ((ItemSyncDescriptor)entry.getValue()).remove(ctx);
                }
            }
            catch(ConsistencyCheckException e)
            {
                throw new JaloSystemException(e);
            }
        }
    }


    @ForceJALO(reason = "abstract method implementation")
    public Collection getFinishedItems(SessionContext ctx)
    {
        Map<Object, Object> params = new HashMap<>();
        params.put("me", this);
        params.put("done", Boolean.TRUE);
        params.put("implicitely", Boolean.FALSE);
        return
                        FlexibleSearch.getInstance()
                                        .search(ctx, "SELECT CASE WHEN {changedItem} IS NULL THEN {targetItem} ELSE {changedItem} END FROM {" + GeneratedCatalogConstants.TC.ITEMSYNCDESCRIPTOR
                                                        + "} WHERE {cronJob}=?me AND {done}=?done AND {copiedImplicitely}=?implicitely ORDER BY {sequenceNumber} ASC ", params, Item.class)
                                        .getResult();
    }


    public void setConfigurator(Configurator config)
    {
        if(config != null && this.conf != null)
        {
            throw new IllegalStateException("configurator of " + this + " already set to " + this.conf + " - cannot set to " + config);
        }
        this.conf = config;
    }


    public Configurator getConfigurator()
    {
        return this.conf;
    }
}
