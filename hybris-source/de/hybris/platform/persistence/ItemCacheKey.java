package de.hybris.platform.persistence;

import de.hybris.platform.jalo.JaloSystemException;

public abstract class ItemCacheKey implements Cloneable
{
    private Object theValue;
    private Object theUnmodifiableView;


    public ItemCacheKey()
    {
    }


    protected ItemCacheKey(Object value)
    {
        this.theValue = value;
    }


    public final ItemCacheKey getCopy()
    {
        try
        {
            ItemCacheKey result = (ItemCacheKey)clone();
            result.theUnmodifiableView = null;
            if(this.theValue != null)
            {
                result.theValue = cloneValue(this.theValue);
            }
            return result;
        }
        catch(CloneNotSupportedException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
    }


    protected final Object getValueInternal()
    {
        return this.theValue;
    }


    protected abstract Object getQualifier();


    protected abstract boolean isValid(ItemEJB paramItemEJB);


    protected void dispose(boolean inRemove)
    {
        this.theValue = null;
        this.theUnmodifiableView = null;
    }


    protected abstract Object computeValue(ItemEJB paramItemEJB);


    private final Object getOrCreateValue(ItemEJB item)
    {
        Object ret = this.theValue;
        if(this.theValue == null)
        {
            this.theValue = ret = computeValue(item);
        }
        return ret;
    }


    protected final Object getValueForReading(ItemEJB item)
    {
        if(this.theUnmodifiableView == null)
        {
            this.theUnmodifiableView = createUnmodifiableViewOfValue(getOrCreateValue(item));
        }
        return this.theUnmodifiableView;
    }


    protected final Object getValueForReadingIfAvailable(ItemEJB item)
    {
        if(this.theValue == null)
        {
            return null;
        }
        Object result = getValueForReading(item);
        return result;
    }


    protected final Object getValueForModification(ItemEJB item)
    {
        return getOrCreateValue(item);
    }


    protected abstract Object cloneValue(Object paramObject);


    protected abstract Object createUnmodifiableViewOfValue(Object paramObject);
}
