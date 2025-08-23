package de.hybris.platform.catalog.jalo.copy;

import com.google.common.base.Preconditions;
import de.hybris.bootstrap.util.LocaleHelper;
import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloAbstractTypeException;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import de.hybris.platform.tx.Transaction;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = false)
public class ItemCopyCreator
{
    private static final Logger LOG = Logger.getLogger(ItemCopyCreator.class);
    private final TypeCopyDescriptor tcd;
    private final ItemCopyContext context;
    private final long sourcePK;
    private final ItemCopyCreator parent;
    private final Map<String, Boolean> whitelist;
    private final Map<String, Boolean> blacklist;
    private final Map<String, Object> presetedValues;
    private final ComposedType copyType;
    private Set<AttributeCopyCreator> initialAttributeCopyCreators;
    private Set<AttributeCopyCreator> partOfAttributeCopyCreators;
    private Set<AttributeCopyCreator> otherAttributeCopyCreators;
    private boolean sourceValuesRead = false;
    private long targetPK;
    private final boolean update;
    private long timeTotal = 0L;
    private long timeRead = 0L;
    private long timeCopyAttributes = 0L;
    private long timeCreate = 0L;
    private long timeSetAttributes = 0L;
    private Map<String, Object> previousValues;
    private Map<String, Object> storedValues;
    private Map<String, Object> pendingCreateValues;
    private transient Item sourceItemCache;
    private transient Item targetItemCache;


    public int hashCode()
    {
        return getSourceItemPK().hashCode();
    }


    public boolean equals(Object obj)
    {
        return (obj instanceof ItemCopyCreator && getSourceItemPK().equals(((ItemCopyCreator)obj).getSourceItemPK()));
    }


    protected ItemCopyCreator(ItemCopyContext itemCopyContext, ItemCopyCreator parent, Item source, Item target, Collection<String> blackList, Collection<String> whiteList, Map<String, Object> presetedValues) throws IllegalArgumentException
    {
        if(itemCopyContext == null)
        {
            throw new NullPointerException("copy context cannot be null");
        }
        if(source == null)
        {
            throw new NullPointerException("source value cannot be null");
        }
        this.context = itemCopyContext;
        this.parent = parent;
        this.sourceItemCache = source;
        this.sourcePK = source.getPK().getLongValue();
        this.copyType = source.getComposedType();
        this.targetItemCache = target;
        this.targetPK = (target != null) ? target.getPK().getLongValue() : 0L;
        this.update = (target != null);
        this.tcd = itemCopyContext.getTypeCopyDescriptor(getTargetType());
        this.blacklist = (Map<String, Boolean>)new CaseInsensitiveMap();
        if(blackList != null)
        {
            for(String qualifier : blackList)
            {
                this.blacklist.put(qualifier, Boolean.TRUE);
            }
        }
        if(itemCopyContext.isDebugEnabled())
        {
            itemCopyContext.debug("blacklist = " + this.blacklist);
        }
        this.whitelist = (Map<String, Boolean>)new CaseInsensitiveMap();
        if(whiteList != null)
        {
            for(String qualifier : whiteList)
            {
                this.whitelist.put(qualifier, Boolean.TRUE);
            }
        }
        if(itemCopyContext.isDebugEnabled())
        {
            itemCopyContext.debug("whitelist = " + this.whitelist);
        }
        this.presetedValues = (Map<String, Object>)new CaseInsensitiveMap();
        if(presetedValues != null)
        {
            this.presetedValues.putAll(presetedValues);
        }
        if(itemCopyContext.isDebugEnabled())
        {
            itemCopyContext.debug("preseted values = " + this.blacklist);
        }
    }


    protected Item copy(ItemCopyContext itemCopyContext)
    {
        if(itemCopyContext.isDebugEnabled())
        {
            itemCopyContext.debug((isUpdate() ? "updating" : "copying") + " item " + (isUpdate() ? "updating" : "copying"));
        }
        boolean done = false;
        boolean isContinuedCreate = (this.pendingCreateValues != null);
        boolean ownTA = !Transaction.current().isRunning();
        try
        {
            long time1 = System.currentTimeMillis();
            Item tgt = (this.targetItemCache != null) ? this.targetItemCache : itemCopyContext.toItem(getTargetItemPK());
            tgt = changeType(tgt, itemCopyContext);
            if(ownTA)
            {
                Transaction.current().begin();
            }
            if(!isContinuedCreate)
            {
                readSourceValues((this.sourceItemCache != null) ? this.sourceItemCache : itemCopyContext.toItem(getSourceItemPK()), tgt, itemCopyContext);
            }
            boolean copyPartOf = hasPartOfItems();
            tgt = create(tgt, itemCopyContext);
            if(tgt != null)
            {
                if(ownTA && copyPartOf)
                {
                    Transaction.current().commit();
                }
                setReferences(tgt, itemCopyContext);
                if(ownTA && !copyPartOf)
                {
                    Transaction.current().commit();
                }
                done = true;
                this.timeTotal = System.currentTimeMillis() - time1;
                if(itemCopyContext.isDebugEnabled())
                {
                    itemCopyContext.debug((isUpdate() ? "updated" : "copied") + " item " + (isUpdate() ? "updated" : "copied") + " to " + getSourceItemPK() + "( read=" +
                                    getTargetItemPK() + "ms, copy attributes=" + this.timeRead + ", create=" + this.timeCopyAttributes + "ms, set attributes=" + this.timeCreate + "ms)" + this.timeSetAttributes);
                }
            }
            else
            {
                if(ownTA)
                {
                    Transaction.current().commit();
                }
                Preconditions.checkArgument(!isContinuedCreate);
            }
            return tgt;
        }
        catch(Exception e)
        {
            if(ownTA && !done && Transaction.current().isRunning())
            {
                try
                {
                    Transaction.current().rollback();
                }
                catch(Exception ex)
                {
                    LOG.error(ex.getMessage(), ex);
                }
            }
            if(itemCopyContext.isErrorEnabled())
            {
                itemCopyContext.error("error copying " + getSourceItemPK() + " due to " + e.getLocalizedMessage());
                itemCopyContext.error("(Failed) " + getReport());
            }
            if(e instanceof RuntimeException)
            {
                throw (RuntimeException)e;
            }
            throw new JaloSystemException(e);
        }
        finally
        {
            this.sourceItemCache = null;
            this.targetItemCache = null;
            for(AttributeCopyCreator acd : getInitialAttributeCreators())
            {
                acd.freeMemory();
            }
            for(AttributeCopyCreator acd : getPartOfAttributeCreators())
            {
                acd.freeMemory();
            }
            for(AttributeCopyCreator acd : getOtherAttributeCreators())
            {
                acd.freeMemory();
            }
            if(ownTA && Transaction.current().isRunning())
            {
                try
                {
                    Transaction.current().rollback();
                }
                catch(Exception ex)
                {
                    LOG.error(ex.getMessage(), ex);
                }
                throw new IllegalStateException("transaction still running - should be committed or rollbacked by now");
            }
        }
    }


    public Map<String, Object> getStoredValues()
    {
        return this.storedValues;
    }


    public Map<String, Object>[] getModifiedValues()
    {
        if(!isUpdate() || this.storedValues == null)
        {
            return null;
        }
        Map[] ret = {(Map)new CaseInsensitiveMap(), (Map)new CaseInsensitiveMap()};
        for(Iterator<Map.Entry> iter = this.storedValues.entrySet().iterator(); iter.hasNext(); )
        {
            Map.Entry entry = iter.next();
            String quali = (String)entry.getKey();
            Object newOne = entry.getValue();
            Object oldOne = this.previousValues.get(quali);
            if(oldOne != newOne && (oldOne == null || !oldOne.equals(newOne)))
            {
                ret[0].put(quali, oldOne);
                ret[1].put(quali, newOne);
            }
        }
        return (Map<String, Object>[])ret;
    }


    public void resetStoredValues()
    {
        this.storedValues = null;
    }


    protected void addToStoredValues(Map<String, Object> attributes)
    {
        if(attributes != null && !attributes.isEmpty() && getCopyContext().isSavePrevousValues())
        {
            if(this.storedValues == null)
            {
                this.storedValues = (Map<String, Object>)new CaseInsensitiveMap();
            }
            this.storedValues.putAll(attributes);
        }
    }


    protected ItemCopyContext getCopyContext()
    {
        return this.context;
    }


    private final Item changeType(Item tgt, ItemCopyContext itemCopyContext)
    {
        Item ret = tgt;
        if(isUpdate())
        {
            if(!getTargetType().equals(tgt.getComposedType()))
            {
                if(itemCopyContext.isDebugEnabled())
                {
                    itemCopyContext.debug("updating target item type from " + tgt.getComposedType().getCode() + " to " +
                                    getTargetType().getCode() + " )");
                }
                ret = tgt.setComposedType(getTargetType());
            }
        }
        return ret;
    }


    private final Item create(Item targetItem, ItemCopyContext itemCopyContext)
    {
        Item ret = targetItem;
        try
        {
            boolean cannotCreate = false;
            Item.ItemAttributeMap<String, Object> itemAttributeMap = new Item.ItemAttributeMap();
            if(this.pendingCreateValues == null)
            {
                for(AttributeCopyCreator acc : getInitialAttributeCreators())
                {
                    acc.copy();
                    if(!acc.isPending())
                    {
                        Object value = acc.getCopiedValue();
                        if(isUpdate() || !isEmpty(value, acc.getDescriptor().isLocalized()))
                        {
                            itemAttributeMap.put(acc.getDescriptor().getQualifier(), value);
                        }
                        continue;
                    }
                    if(!isUpdate() && getCopyContext().isRequiredForCreation(acc
                                    .getDescriptor().getRealAttributeDescriptor()))
                    {
                        cannotCreate = true;
                    }
                }
            }
            else
            {
                itemAttributeMap.putAll(this.pendingCreateValues);
            }
            addToStoredValues((Map<String, Object>)itemAttributeMap);
            long time1 = System.currentTimeMillis();
            if(cannotCreate)
            {
                if(itemCopyContext.isDebugEnabled())
                {
                    itemCopyContext.debug("cannot create item due to pending attributes (values " +
                                    getCopyContext().valuesToString((Map)itemAttributeMap) + ")");
                }
                this.pendingCreateValues = (Map<String, Object>)itemAttributeMap;
            }
            else
            {
                if(isUpdate())
                {
                    if(itemCopyContext.isDebugEnabled())
                    {
                        itemCopyContext.debug("updating target item " + getTargetItemPK() + " (values " +
                                        getCopyContext().valuesToString((Map)itemAttributeMap) + ")");
                    }
                    ret.setAllAttributes(getCopyContext().getCtx(), (Map)itemAttributeMap);
                }
                else
                {
                    if(itemCopyContext.isDebugEnabled())
                    {
                        itemCopyContext.debug("creating new item (values " +
                                        getCopyContext().valuesToString((Map)itemAttributeMap) + ")");
                    }
                    setTargetItem(ret = getTargetType().newInstance(getCopyContext().getCtx(), (Map)itemAttributeMap));
                }
                getCopyContext().registerCopy(getSourceItemPK(), getTargetItemPK());
            }
            this.timeCreate = System.currentTimeMillis() - time1;
        }
        catch(JaloAbstractTypeException e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1919);
        }
        catch(JaloGenericCreationException e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1920);
        }
        catch(JaloInvalidParameterException e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1921);
        }
        catch(JaloSecurityException e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1922);
        }
        catch(JaloBusinessException e)
        {
            throw new JaloSystemException(e, e.getMessage(), 1923);
        }
        return ret;
    }


    protected void setReferences(Item targetItem, ItemCopyContext itemCopyContext)
    {
        if(itemCopyContext.isDebugEnabled())
        {
            itemCopyContext.debug("setReferences for " + getTargetItemPK() + " ( parent item = " + (
                            (getParent() != null) ? (String)getParent().getSourceItemPK() : "n/a") + ")");
        }
        long time1 = System.currentTimeMillis();
        Item.ItemAttributeMap<String, Object> itemAttributeMap = new Item.ItemAttributeMap();
        for(AttributeCopyCreator acc : getPartOfAttributeCreators())
        {
            acc.copy();
            if(!acc.isPending())
            {
                Object value = acc.getCopiedValue();
                if(isUpdate() || !isEmpty(value, acc.getDescriptor().isLocalized()))
                {
                    itemAttributeMap.put(acc.getDescriptor().getQualifier(), value);
                }
            }
        }
        for(AttributeCopyCreator acc : getOtherAttributeCreators())
        {
            acc.copy();
            if(!acc.isPending())
            {
                Object value = acc.getCopiedValue();
                if(isUpdate() || !isEmpty(value, acc.getDescriptor().isLocalized()))
                {
                    itemAttributeMap.put(acc.getDescriptor().getQualifier(), value);
                }
            }
        }
        addToStoredValues((Map<String, Object>)itemAttributeMap);
        long time2 = System.currentTimeMillis();
        if(!itemAttributeMap.isEmpty())
        {
            try
            {
                if(itemCopyContext.isDebugEnabled())
                {
                    itemCopyContext.debug("setting references of " + getTargetItemPK() + " to " + itemAttributeMap);
                }
                targetItem.setAllAttributes(getCopyContext().getCtx(), (Map)itemAttributeMap);
            }
            catch(JaloBusinessException e)
            {
                throw new JaloSystemException(e, e.getMessage(), 1721);
            }
        }
        long time3 = System.currentTimeMillis();
        this.timeCopyAttributes += time2 - time1;
        this.timeSetAttributes += time3 - time2;
    }


    protected void setPendingReferences(ItemCopyContext itemCopyContext, Set<AttributeCopyCreator> accs)
    {
        if(itemCopyContext.isDebugEnabled())
        {
            itemCopyContext.debug("set pending references " + accs + " for " + getTargetItemPK() + " ( parent item = " + (
                            (getParent() != null) ? (String)getParent().getSourceItemPK() : "n/a") + ")");
        }
        long time1 = System.currentTimeMillis();
        long time2 = time1;
        if(this.pendingCreateValues != null)
        {
            for(AttributeCopyCreator acc : accs)
            {
                Preconditions.checkArgument(acc.isPending());
                acc.copy();
                Object value = acc.getCopiedValue();
                if(acc.isPending())
                {
                    if(itemCopyContext.isWarnEnabled())
                    {
                        itemCopyContext.warn("attribute " + acc + " is still pending even after retry - setting value " + value + " without pending items");
                    }
                }
                if(value != null)
                {
                    this.pendingCreateValues.put(acc.getDescriptor().getQualifier(), value);
                }
            }
            boolean canCreate = true;
            for(AttributeCopyCreator acc : getInitialAttributeCreators())
            {
                if(acc.isPending() && getCopyContext().isRequiredForCreation(acc.getDescriptor().getRealAttributeDescriptor()) && this.pendingCreateValues
                                .get(acc.getDescriptor().getQualifier()) == null)
                {
                    canCreate = false;
                    break;
                }
            }
            if(canCreate)
            {
                copy(itemCopyContext);
                itemCopyContext.finishedCopying(this);
            }
        }
        else
        {
            Item.ItemAttributeMap<String, Object> itemAttributeMap = new Item.ItemAttributeMap();
            boolean overwrite = isUpdate();
            for(AttributeCopyCreator acc : accs)
            {
                acc.copy();
                Object value = acc.getCopiedValue();
                if(acc.isPending())
                {
                    if(itemCopyContext.isWarnEnabled())
                    {
                        itemCopyContext.warn("attribute " + acc + " is still pending even after retry - setting value " + value + " without pending items");
                    }
                }
                if(overwrite || !isEmpty(value, acc.getDescriptor().isLocalized()))
                {
                    itemAttributeMap.put(acc.getDescriptor().getQualifier(), value);
                }
            }
            addToStoredValues((Map<String, Object>)itemAttributeMap);
            time2 = System.currentTimeMillis();
            if(!itemAttributeMap.isEmpty())
            {
                try
                {
                    if(itemCopyContext.isDebugEnabled())
                    {
                        itemCopyContext.debug("setting pending references of " + getTargetItemPK() + " to " + itemAttributeMap);
                    }
                    itemCopyContext.toItem(getTargetItemPK()).setAllAttributes(getCopyContext().getCtx(), (Map)itemAttributeMap);
                }
                catch(JaloBusinessException e)
                {
                    throw new JaloSystemException(e, e.getMessage(), 1721);
                }
            }
        }
        long time3 = System.currentTimeMillis();
        this.timeCopyAttributes += time2 - time1;
        this.timeSetAttributes += time3 - time2;
    }


    public final PK getSourceItemPK()
    {
        return (this.sourcePK != 0L) ? PK.fromLong(this.sourcePK) : null;
    }


    private final void setTargetItem(Item tgt)
    {
        this.targetPK = (tgt != null) ? tgt.getPK().getLongValue() : 0L;
    }


    public final boolean isUpdate()
    {
        return this.update;
    }


    public final boolean isPartOf()
    {
        return (getParent() != null);
    }


    public final void addToPresetValues(String qualifier, Object value)
    {
        this.presetedValues.put(qualifier, value);
    }


    public final ComposedType getTargetType()
    {
        return this.copyType;
    }


    protected ItemCopyCreator getParent()
    {
        return this.parent;
    }


    protected final ItemCopyCreator getEnclosingCreatorFor(Item value)
    {
        if(!isPartOf())
        {
            return null;
        }
        PK pk = value.getPK();
        for(ItemCopyCreator p = getParent(); p != null; p = p.getParent())
        {
            if(p.getSourceItemPK().equals(pk))
            {
                return p;
            }
        }
        return null;
    }


    protected final void readSourceValues(Item sourceItem, Item targetItem, ItemCopyContext itemCopyContext)
    {
        long time1 = System.currentTimeMillis();
        try
        {
            Set<AttributeCopyDescriptor> initial = this.tcd.getInitial(isUpdate(), this.blacklist, this.whitelist);
            Set<AttributeCopyDescriptor> partOf = this.tcd.getPartOf(this.blacklist, this.whitelist);
            Set<AttributeCopyDescriptor> others = this.tcd.getOther(this.blacklist, this.whitelist);
            Set<String> permitted = new HashSet<>(initial.size() + partOf.size() + others.size());
            for(AttributeCopyDescriptor acd : initial)
            {
                permitted.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
            for(AttributeCopyDescriptor acd : partOf)
            {
                permitted.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
            for(AttributeCopyDescriptor acd : others)
            {
                permitted.add(acd.getQualifier().toLowerCase(LocaleHelper.getPersistenceLocale()));
            }
            Set<String> qualifiers = new LinkedHashSet<>();
            qualifiers.addAll(permitted);
            if(this.presetedValues != null && !this.presetedValues.isEmpty())
            {
                qualifiers.removeAll(this.presetedValues.keySet());
            }
            SessionContext ctx = getCopyContext().getCtx();
            Map<String, Object> sourceValues = sourceItem.getAllAttributes(ctx, qualifiers);
            if(itemCopyContext.isSavePrevousValues() && isUpdate())
            {
                this.previousValues = (Map<String, Object>)new CaseInsensitiveMap(targetItem.getAllAttributes(ctx, (Item.AttributeFilter)new Object(this, permitted)));
            }
            this.initialAttributeCopyCreators = new HashSet<>();
            for(AttributeCopyDescriptor acd : initial)
            {
                Object srcVal;
                String qualifier = acd.getQualifier();
                boolean prest = (this.presetedValues != null && this.presetedValues.containsKey(qualifier));
                if(prest)
                {
                    srcVal = this.presetedValues.get(qualifier);
                }
                else
                {
                    if(!sourceValues.containsKey(qualifier))
                    {
                        LOG.error("no source value found for " + acd + " from " + getCopyContext().safeToString(sourceItem));
                    }
                    srcVal = sourceValues.get(qualifier);
                }
                this.initialAttributeCopyCreators.add(getCopyContext().createAttributeCopyCreator(this, acd, prest, srcVal));
            }
            this.partOfAttributeCopyCreators = new HashSet<>();
            for(AttributeCopyDescriptor acd : partOf)
            {
                boolean prest = (this.presetedValues != null && this.presetedValues.containsKey(acd.getQualifier()));
                this.partOfAttributeCopyCreators.add(getCopyContext().createAttributeCopyCreator(this, acd, prest,
                                prest ? this.presetedValues.get(acd
                                                .getQualifier()) :
                                                sourceValues.get(acd.getQualifier())));
            }
            this.otherAttributeCopyCreators = new HashSet<>();
            for(AttributeCopyDescriptor acd : others)
            {
                boolean prest = (this.presetedValues != null && this.presetedValues.containsKey(acd.getQualifier()));
                this.otherAttributeCopyCreators.add(getCopyContext().createAttributeCopyCreator(this, acd, prest,
                                prest ? this.presetedValues.get(acd
                                                .getQualifier()) :
                                                sourceValues.get(acd.getQualifier())));
            }
        }
        catch(JaloInvalidParameterException e)
        {
            if(itemCopyContext.isWarnEnabled())
            {
                itemCopyContext.error("error reading values from " + getSourceItemPK() + " from attributes : " + e.getMessage());
            }
            throw new JaloSystemException(e);
        }
        catch(JaloSecurityException e)
        {
            if(itemCopyContext.isWarnEnabled())
            {
                itemCopyContext.error("error reading values from " + getSourceItemPK() + " from attributes : " + e.getMessage());
            }
            throw new JaloSystemException(e);
        }
        this.sourceValuesRead = true;
        this.timeRead = System.currentTimeMillis() - time1;
    }


    protected boolean isEmpty(Object translatedValue, boolean localized)
    {
        if(translatedValue == null)
        {
            return true;
        }
        if(translatedValue instanceof Collection)
        {
            return ((Collection)translatedValue).isEmpty();
        }
        if(translatedValue instanceof Map)
        {
            if(((Map)translatedValue).isEmpty())
            {
                return true;
            }
            if(localized)
            {
                for(Iterator<Map.Entry> it = ((Map)translatedValue).entrySet().iterator(); it.hasNext(); )
                {
                    Map.Entry entry = it.next();
                    if(!isEmpty(entry.getValue(), false))
                    {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }


    protected final boolean hasPartOfItems()
    {
        if(!this.sourceValuesRead)
        {
            throw new IllegalStateException("source values not read - cannot call hasPartOfItems() yet");
        }
        for(Iterator<AttributeCopyCreator> it = getPartOfAttributeCreators().iterator(); it.hasNext(); )
        {
            if(((AttributeCopyCreator)it.next()).hasPartOfItems())
            {
                return true;
            }
        }
        return false;
    }


    protected final boolean hasPendingAttributes()
    {
        for(Iterator<AttributeCopyCreator> iterator2 = getPartOfAttributeCreators().iterator(); iterator2.hasNext(); )
        {
            if(((AttributeCopyCreator)iterator2.next()).isPending())
            {
                return true;
            }
        }
        for(Iterator<AttributeCopyCreator> iterator1 = getOtherAttributeCreators().iterator(); iterator1.hasNext(); )
        {
            if(((AttributeCopyCreator)iterator1.next()).isPending())
            {
                return true;
            }
        }
        for(Iterator<AttributeCopyCreator> it = getInitialAttributeCreators().iterator(); it.hasNext(); )
        {
            if(((AttributeCopyCreator)it.next()).isPending())
            {
                return true;
            }
        }
        return false;
    }


    protected final Set<AttributeCopyCreator> getPartOfAttributeCreators()
    {
        return (this.partOfAttributeCopyCreators != null) ? this.partOfAttributeCopyCreators : Collections.EMPTY_SET;
    }


    protected final Set<AttributeCopyCreator> getInitialAttributeCreators()
    {
        return (this.initialAttributeCopyCreators != null) ? this.initialAttributeCopyCreators : Collections.EMPTY_SET;
    }


    protected final Set<AttributeCopyCreator> getOtherAttributeCreators()
    {
        return (this.otherAttributeCopyCreators != null) ? this.otherAttributeCopyCreators : Collections.EMPTY_SET;
    }


    protected final Set<AttributeCopyCreator> getPendingAttributes()
    {
        Set<AttributeCopyCreator> ret = new HashSet<>();
        for(AttributeCopyCreator acc : getPartOfAttributeCreators())
        {
            if(acc.isPending())
            {
                ret.add(acc);
            }
        }
        for(AttributeCopyCreator acc : getOtherAttributeCreators())
        {
            if(acc.isPending())
            {
                ret.add(acc);
            }
        }
        for(AttributeCopyCreator acc : getInitialAttributeCreators())
        {
            if(acc.isPending())
            {
                ret.add(acc);
            }
        }
        return ret;
    }


    protected final Set<String> getPendingAttributeQualifiers()
    {
        Set<String> ret = new HashSet();
        for(Iterator<AttributeCopyCreator> it = getPendingAttributes().iterator(); it.hasNext(); )
        {
            AttributeCopyCreator acc = it.next();
            ret.add(acc.getDescriptor().toString());
        }
        return ret;
    }


    public String toString()
    {
        return "ICC(" + getSourceItemPK() + (isUpdate() ? ("->" + getTargetItemPK()) : "") + ")";
    }


    public String getReport()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CopyReport src:").append(getSourceItemPK()).append(", target:")
                        .append((getTargetItemPK() != null) ? String.valueOf(getTargetItemPK()) : "n/a").append("\n");
        stringBuilder.append("\tmode:").append(isUpdate() ? "update" : "create").append("\n");
        stringBuilder.append("\ttime: total:").append(this.timeTotal).append("ms, read:").append(this.timeRead).append("ms, copyAttr:")
                        .append(this.timeCopyAttributes).append("ms, ");
        stringBuilder.append(isUpdate() ? "initial:" : "create:").append(this.timeCreate).append("ms, refs:").append(this.timeSetAttributes)
                        .append("ms \n");
        Set<AttributeCopyCreator> excluded = new HashSet();
        if(this.initialAttributeCopyCreators != null && !this.initialAttributeCopyCreators.isEmpty())
        {
            boolean first = true;
            for(AttributeCopyCreator acc : this.initialAttributeCopyCreators)
            {
                if(acc.isPending())
                {
                    excluded.add(acc);
                    continue;
                }
                if(first)
                {
                    stringBuilder.append("\twrote initial attributes \n");
                }
                first = false;
                stringBuilder.append("\t\t").append(acc.getDescriptor()).append(" value:")
                                .append(getCopyContext().safeToString(acc.getSourceValueNoCheck()));
                if(acc.isPreset())
                {
                    stringBuilder.append(", preset\n");
                    continue;
                }
                if(!acc.isTranslated())
                {
                    stringBuilder.append(", NOT YET TRANSLATED!\n");
                    continue;
                }
                if(acc.valueHasChanged())
                {
                    stringBuilder.append(" changed into ").append(getCopyContext().safeToString(acc.getCopiedValueNoCheck()))
                                    .append("\n");
                    continue;
                }
                stringBuilder.append(" unchanged \n");
            }
        }
        if(this.partOfAttributeCopyCreators != null && !this.partOfAttributeCopyCreators.isEmpty())
        {
            boolean first = true;
            for(AttributeCopyCreator acc : this.partOfAttributeCopyCreators)
            {
                if(acc.isPending())
                {
                    excluded.add(acc);
                    continue;
                }
                if(first)
                {
                    stringBuilder.append("\twrote partOf attributes \n");
                }
                first = false;
                stringBuilder.append("\t\t").append(acc.getDescriptor()).append(" value:")
                                .append(getCopyContext().safeToString(acc.getSourceValueNoCheck()));
                if(acc.isPreset())
                {
                    stringBuilder.append(", preset\n");
                    continue;
                }
                if(!acc.isTranslated())
                {
                    stringBuilder.append(", NOT YET TRANSLATED!\n");
                    continue;
                }
                if(acc.valueHasChanged())
                {
                    stringBuilder.append(" changed into ").append(getCopyContext().safeToString(acc.getCopiedValueNoCheck()))
                                    .append("\n");
                    continue;
                }
                stringBuilder.append(" unchanged \n");
            }
        }
        if(this.otherAttributeCopyCreators != null && !this.otherAttributeCopyCreators.isEmpty())
        {
            boolean first = true;
            for(AttributeCopyCreator acc : this.otherAttributeCopyCreators)
            {
                if(acc.isPending())
                {
                    excluded.add(acc);
                    continue;
                }
                if(first)
                {
                    stringBuilder.append("\twrote other attributes \n");
                }
                first = false;
                stringBuilder.append("\t\t").append(acc.getDescriptor()).append(" value:")
                                .append(getCopyContext().safeToString(acc.getSourceValueNoCheck()));
                if(acc.isPreset())
                {
                    stringBuilder.append(", preset\n");
                    continue;
                }
                if(!acc.isTranslated())
                {
                    stringBuilder.append(", NOT YET TRANSLATED!\n");
                    continue;
                }
                if(acc.valueHasChanged())
                {
                    stringBuilder.append(" changed into ").append(getCopyContext().safeToString(acc.getCopiedValueNoCheck()))
                                    .append("\n");
                    continue;
                }
                stringBuilder.append(" unchanged \n");
            }
        }
        stringBuilder.append("\tblacklist: ").append(this.blacklist).append("\n");
        stringBuilder.append("\twhitelist: ").append(this.whitelist).append("\n");
        excluded.addAll(this.tcd.getExcludedAttributeCopyDescriptors(isUpdate(), this.blacklist, this.whitelist));
        if(!excluded.isEmpty())
        {
            stringBuilder.append("\texcluded attributes \n");
            for(AttributeCopyCreator o : excluded)
            {
                if(o instanceof AttributeCopyCreator)
                {
                    AttributeCopyCreator acc = o;
                    stringBuilder.append("\t\t").append(acc.getDescriptor()).append(" still pending (waiting for ")
                                    .append(acc.getPendingValuesToTranslate()).append(")\n");
                    continue;
                }
                if(o instanceof AttributeCopyDescriptor)
                {
                    AttributeCopyDescriptor acd = (AttributeCopyDescriptor)o;
                    stringBuilder.append("\t\t").append(acd)
                                    .append(this.blacklist.containsKey(acd
                                                    .getQualifier()) ? " due to blacklist" : " due to whitelist")
                                    .append("\n");
                }
            }
        }
        return stringBuilder.toString();
    }


    public PK getTargetItemPK()
    {
        return (this.targetPK != 0L) ? PK.fromLong(this.targetPK) : null;
    }
}
