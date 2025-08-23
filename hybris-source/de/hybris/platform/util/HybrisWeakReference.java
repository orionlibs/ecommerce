package de.hybris.platform.util;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

public class HybrisWeakReference extends WeakReference
{
    private final int hashCodeFlag;


    public HybrisWeakReference(Object o)
    {
        super((T)o);
        this.hashCodeFlag = o.hashCode();
    }


    public HybrisWeakReference(Object o, ReferenceQueue<? super T> queue)
    {
        super((T)o, queue);
        this.hashCodeFlag = o.hashCode();
    }


    public boolean equals(Object o)
    {
        if(o instanceof HybrisWeakReference)
        {
            HybrisWeakReference ref = (HybrisWeakReference)o;
            if(get() != null && ref.get() != null)
            {
                return get().equals(((HybrisWeakReference)o).get());
            }
            return (this.hashCodeFlag == ref.hashCode());
        }
        return false;
    }


    public int hashCode()
    {
        return this.hashCodeFlag;
    }
}
