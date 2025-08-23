package de.hybris.platform.catalog.jalo.copy;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.util.collections.fast.YLongSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

@Deprecated(since = "ages", forRemoval = false)
public class AttributeCopyCreator
{
    private static final long[] NO_WAITING = new long[0];
    private final ItemCopyCreator parent;
    private final boolean preset;
    final AttributeCopyDescriptor acd;
    private Object sourceValue;
    private Object targetValue;
    private long[] partOfItemsToCopy;
    private boolean pending = false;
    private YLongSet waitingForValues;
    private boolean translated;
    private boolean memoryCleared;
    private transient Set<Item> partOfItemsCache;


    protected AttributeCopyCreator(ItemCopyCreator icc, AttributeCopyDescriptor acd, boolean isPreset, Object sourceValue)
    {
        this.acd = acd;
        this.parent = icc;
        this.preset = isPreset;
        this.sourceValue = mask(sourceValue);
        this.partOfItemsToCopy = (isPreset || !getDescriptor().isPartOf()) ? null : toPKArray(this
                        .partOfItemsCache = icc.getCopyContext().collectPartOfItemsToCopy(this));
    }


    protected long[] toPKArray(Collection<Item> items)
    {
        if(items == null)
        {
            return null;
        }
        if(items.isEmpty())
        {
            return new long[0];
        }
        long[] ret = new long[items.size()];
        int idx = 0;
        for(Item i : items)
        {
            ret[idx++] = i.getPK().getLongValue();
        }
        return ret;
    }


    protected Set<Item> toItemSet(long[] pks)
    {
        if(pks == null)
        {
            return null;
        }
        if(pks.length == 0)
        {
            return Collections.EMPTY_SET;
        }
        Set<Item> ret = new HashSet<>(pks.length);
        PK[] realPKs = new PK[pks.length];
        for(int i = 0; i < pks.length; i++)
        {
            realPKs[i] = PK.fromLong(pks[i]);
        }
        JaloSession jSession = JaloSession.getCurrentSession();
        for(Item item : jSession.getItems(jSession.getSessionContext(), Arrays.asList(realPKs), true, false))
        {
            ret.add(item);
        }
        return ret;
    }


    public final AttributeCopyDescriptor getDescriptor()
    {
        return this.acd;
    }


    protected final void addPendingValue(long pendingValue)
    {
        if(this.memoryCleared)
        {
            throw new IllegalStateException("attribute " + this + " is already cleared");
        }
        if(this.waitingForValues == null)
        {
            this.waitingForValues = new YLongSet();
        }
        this.waitingForValues.add(pendingValue);
        this.pending = true;
    }


    protected final void removePendingValue(long pendingValue)
    {
        if(this.waitingForValues != null)
        {
            this.waitingForValues.remove(pendingValue);
            if(this.waitingForValues.isEmpty())
            {
                this.waitingForValues = null;
            }
        }
    }


    protected final long[] getPendingValuesToTranslate()
    {
        return (this.waitingForValues != null) ? this.waitingForValues.toArray() : NO_WAITING;
    }


    public final boolean isPending()
    {
        return this.pending;
    }


    public final boolean isPreset()
    {
        return this.preset;
    }


    protected final boolean isTranslated()
    {
        return this.translated;
    }


    public final boolean valueHasChanged()
    {
        if(!this.translated)
        {
            throw new IllegalStateException("not yet translated");
        }
        Object src = getSourceValueNoCheck();
        Object tgt = getCopiedValueNoCheck();
        return (src != tgt && (src == null || !src.equals(tgt)));
    }


    public final ItemCopyCreator getParent()
    {
        return this.parent;
    }


    public final int hashCode()
    {
        return getParent().hashCode() ^ this.acd.getQualifier().hashCode();
    }


    public final boolean equals(Object obj)
    {
        return (obj instanceof AttributeCopyCreator && getParent().equals(((AttributeCopyCreator)obj).getParent()) && this.acd
                        .equals(((AttributeCopyCreator)obj).acd));
    }


    public final boolean hasPartOfItems()
    {
        return (this.partOfItemsToCopy != null && this.partOfItemsToCopy.length > 0);
    }


    public String toString()
    {
        ItemCopyContext ctx = getParent().getCopyContext();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getParent().getSourceItemPK()).append(".");
        stringBuilder.append(getDescriptor()).append("(").append(ctx.safeToString(this.sourceValue));
        stringBuilder.append(this.translated ? (" -> " + ctx.safeToString(this.targetValue)) : "");
        stringBuilder.append(this.memoryCleared ? " CLEARED" : "").append(this.pending ? " PENDING" : "");
        stringBuilder.append(")");
        return stringBuilder.toString();
    }


    public final void copy()
    {
        if(this.memoryCleared)
        {
            throw new IllegalStateException("attribute " + this + " is already cleared (stack " + getCopyStack() + " )");
        }
        if(this.translated && !this.pending)
        {
            throw new IllegalStateException("attribute " + this + " is already translated and not pending");
        }
        this.pending = false;
        this.targetValue = getParent().getCopyContext().translate(this);
        this.translated = true;
    }


    protected String getCopyStack()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getDescriptor().getQualifier());
        for(ItemCopyCreator icc = getParent(); icc != null; icc = icc.getParent())
        {
            stringBuilder.append("<-").append(icc.getSourceItemPK());
        }
        return stringBuilder.toString();
    }


    protected void freeMemory()
    {
        if(isTranslated() && !isPending())
        {
            this.sourceValue = null;
            this.targetValue = null;
            this.partOfItemsToCopy = null;
            this.waitingForValues = null;
            this.partOfItemsCache = null;
            this.memoryCleared = true;
        }
        else
        {
            freeMemory(this.sourceValue);
            this.partOfItemsCache = null;
        }
    }


    private void freeMemory(Object sourceValue)
    {
        if(sourceValue instanceof LeanItemHandle)
        {
            ((LeanItemHandle)sourceValue).freeMemory();
        }
        else if(sourceValue instanceof LeanItemCollection)
        {
            ((LeanItemCollection)sourceValue).freeMemory();
        }
        else if(sourceValue instanceof Collection)
        {
            for(Object o : sourceValue)
            {
                freeMemory(o);
            }
        }
        else if(sourceValue instanceof Map)
        {
            for(Map.Entry<Object, Object> e : (Iterable<Map.Entry<Object, Object>>)((Map)sourceValue).entrySet())
            {
                freeMemory(e.getValue());
            }
        }
    }


    public final Object getSourceValue()
    {
        if(this.memoryCleared)
        {
            throw new IllegalStateException("cannot read source value because memory is already cleared");
        }
        return unmask(this.sourceValue);
    }


    protected final Object getSourceValueNoCheck()
    {
        return this.memoryCleared ? "<cleared>" : this.sourceValue;
    }


    public final Set<Item> getPartOfItemsToCopy()
    {
        if(this.partOfItemsCache != null)
        {
            return this.partOfItemsCache;
        }
        if(this.partOfItemsToCopy != null)
        {
            return Collections.unmodifiableSet(toItemSet(this.partOfItemsToCopy));
        }
        return Collections.EMPTY_SET;
    }


    public final Object getCopiedValue()
    {
        if(!this.translated)
        {
            throw new IllegalStateException("attribute " + getDescriptor() + " not translated yet - call copy() before");
        }
        if(this.memoryCleared)
        {
            throw new IllegalStateException("cannot read source value because memory is already cleared");
        }
        return this.targetValue;
    }


    protected final Object getCopiedValueNoCheck()
    {
        return this.memoryCleared ? "<cleared>" : this.targetValue;
    }


    protected Object unmask(Object value)
    {
        Object returnValue;
        if(!getDescriptor().isAtomic())
        {
            if(value instanceof LeanItemHandle)
            {
                returnValue = ((LeanItemHandle)value).getItem();
            }
            else if(value instanceof LeanItemCollection)
            {
                returnValue = ((LeanItemCollection)value).getCollection();
            }
            else if(getDescriptor().isLocalized() && value instanceof LinkedHashMap)
            {
                for(Map.Entry<Object, Object> e : (Iterable<Map.Entry<Object, Object>>)((Map)value).entrySet())
                {
                    e.setValue(unmask(e.getValue()));
                }
                returnValue = value;
            }
            else
            {
                returnValue = value;
            }
        }
        else
        {
            returnValue = value;
        }
        return returnValue;
    }


    protected Object mask(Object value)
    {
        Object returnValue;
        if(!getDescriptor().isAtomic())
        {
            if(value instanceof Item)
            {
                returnValue = new LeanItemHandle((Item)value);
            }
            else if(value instanceof Collection)
            {
                boolean canMask = true;
                for(Object o : value)
                {
                    if(!(o instanceof Item))
                    {
                        canMask = false;
                        break;
                    }
                }
                if(canMask)
                {
                    returnValue = new LeanItemCollection((Collection)value);
                }
                else
                {
                    returnValue = value;
                }
            }
            else if(getDescriptor().isLocalized() && value instanceof Map)
            {
                Map<Object, Object> tmp = new LinkedHashMap<>((Map<?, ?>)value);
                for(Map.Entry<Object, Object> e : tmp.entrySet())
                {
                    e.setValue(mask(e.getValue()));
                }
                returnValue = tmp;
            }
            else
            {
                returnValue = value;
            }
        }
        else
        {
            returnValue = value;
        }
        return returnValue;
    }
}
