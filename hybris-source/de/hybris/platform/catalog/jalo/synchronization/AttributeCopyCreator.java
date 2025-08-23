package de.hybris.platform.catalog.jalo.synchronization;

import de.hybris.platform.jalo.Item;
import java.util.Collections;
import java.util.Set;

public class AttributeCopyCreator
{
    private final ItemCopyCreator parent;
    private final boolean preset;
    private final AttributeCopyDescriptor acd;
    private final Object sourceValue;
    private Object targetValue;
    private Set<Item> _partOfItemsToCopy = null;
    private boolean pending = false;
    private boolean translated;
    private final int hashCodeValue;


    protected AttributeCopyCreator(ItemCopyCreator icc, AttributeCopyDescriptor acd, boolean isPreset, Object sourceValue)
    {
        this.acd = acd;
        this.parent = icc;
        this.preset = isPreset;
        this.sourceValue = sourceValue;
        this.hashCodeValue = icc.hashCode() ^ acd.getQualifier().hashCode();
    }


    public final AttributeCopyDescriptor getDescriptor()
    {
        return this.acd;
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


    public final void reset()
    {
        this.pending = false;
        this.translated = false;
    }


    public final boolean valueHasChanged()
    {
        if(!this.translated)
        {
            throw new IllegalStateException("not yet translated");
        }
        Object src = getSourceValue();
        Object tgt = getCopiedValue();
        return (src != tgt && (src == null || !src.equals(tgt)));
    }


    public final ItemCopyCreator getParent()
    {
        return this.parent;
    }


    public final int hashCode()
    {
        return this.hashCodeValue;
    }


    public final boolean equals(Object obj)
    {
        return (obj instanceof AttributeCopyCreator && getParent().equals(((AttributeCopyCreator)obj).getParent()) && this.acd
                        .equals(((AttributeCopyCreator)obj).acd));
    }


    public final boolean hasPartOfItems()
    {
        return !getPartOfItemsToCopy().isEmpty();
    }


    public String toString()
    {
        GenericCatalogCopyContext genericCatalogCopyContext = getParent().getCopyContext();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getParent().getSourceItem().getPK()).append(".");
        stringBuilder.append(getDescriptor()).append("(").append(genericCatalogCopyContext.safeToString(this.sourceValue));
        stringBuilder.append(this.translated ? (" -> " + genericCatalogCopyContext.safeToString(this.targetValue)) : "");
        stringBuilder.append(this.pending ? " PENDING" : "");
        stringBuilder.append(")");
        return stringBuilder.toString();
    }


    public final void copy()
    {
        if(this.translated && !this.pending)
        {
            throw new IllegalStateException("attribute " + this + " is already translated and not pending");
        }
        this.pending = false;
        this.targetValue = getParent().getCopyContext().translate(this);
        this.translated = true;
    }


    public final Object getSourceValue()
    {
        return this.sourceValue;
    }


    public final Set<Item> getPartOfItemsToCopy()
    {
        if(this._partOfItemsToCopy == null)
        {
            this
                            ._partOfItemsToCopy = (isPreset() || !getDescriptor().isPartOf()) ? Collections.EMPTY_SET : getParent().getCopyContext().collectPartOfItemsToCopy(this);
        }
        return this._partOfItemsToCopy;
    }


    public final Object getCopiedValue()
    {
        if(!this.translated)
        {
            throw new IllegalStateException("attribute " + getDescriptor() + " not translated yet - call copy() before");
        }
        return this.targetValue;
    }


    public void setPending()
    {
        this.pending = true;
    }


    public boolean canBeTranslatedPartially()
    {
        if(isPartOfAttribute())
        {
            return false;
        }
        return getCopyContext().canBeTranslatedPartially(this);
    }


    private boolean isPartOfAttribute()
    {
        return this.acd.isPartOf();
    }


    private GenericCatalogCopyContext getCopyContext()
    {
        return this.parent.getCopyContext();
    }
}
