package de.hybris.platform.voucher.jalo.util;

import de.hybris.platform.jalo.order.AbstractOrderEntry;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class VoucherEntrySet implements Set
{
    private final Set set = new HashSet();


    public VoucherEntrySet()
    {
    }


    public VoucherEntrySet(Collection c)
    {
        this();
        addAll(c);
    }


    public boolean add(Object o)
    {
        VoucherEntry newEntry = checkForVoucherEntry(o);
        if(newEntry == null)
        {
            throw new IllegalArgumentException("Unable to add object of class " + o.getClass().getName() + "!");
        }
        VoucherEntry existingEntry = getVoucherEntryByOrderEntry(newEntry.getOrderEntry());
        if(existingEntry == null)
        {
            return this.set.add(newEntry);
        }
        existingEntry.setQuantity(existingEntry
                        .getQuantity() + newEntry.getUnit().convert(existingEntry.getUnit(), newEntry.getQuantity()));
        return true;
    }


    public boolean addAll(Collection c)
    {
        for(Iterator iterator = c.iterator(); iterator.hasNext(); )
        {
            Object next = iterator.next();
            if(!(next instanceof VoucherEntry) && !(next instanceof AbstractOrderEntry))
            {
                throw new IllegalArgumentException("Unable to add given collection!");
            }
        }
        boolean changed = false;
        for(Iterator iterator1 = c.iterator(); iterator1.hasNext(); )
        {
            changed |= add(iterator1.next());
        }
        return changed;
    }


    protected static VoucherEntry checkForVoucherEntry(Object o)
    {
        if(o instanceof VoucherEntry)
        {
            return (VoucherEntry)o;
        }
        if(o instanceof AbstractOrderEntry)
        {
            AbstractOrderEntry entry = (AbstractOrderEntry)o;
            return new VoucherEntry(entry, entry.getQuantity().longValue(), entry.getUnit());
        }
        return null;
    }


    public void clear()
    {
        this.set.clear();
    }


    public boolean contains(Object o)
    {
        VoucherEntry voucherEntry = checkForVoucherEntry(o);
        return (voucherEntry != null && containsInternal(voucherEntry));
    }


    private boolean containsInternal(VoucherEntry aVoucherEntry)
    {
        VoucherEntry existingEntry = getVoucherEntryByOrderEntry(aVoucherEntry.getOrderEntry());
        return (existingEntry != null && existingEntry.getQuantity() > aVoucherEntry.getUnit().convert(existingEntry.getUnit(), aVoucherEntry
                        .getQuantity()));
    }


    public boolean containsAll(Collection c)
    {
        boolean contains = true;
        for(Iterator iterator = c.iterator(); iterator.hasNext(); )
        {
            contains &= contains(iterator.next());
        }
        return contains;
    }


    private VoucherEntry getVoucherEntryByOrderEntry(AbstractOrderEntry anAbstractOrderEntry)
    {
        return getVoucherEntryByOrderEntry(this.set, anAbstractOrderEntry);
    }


    private VoucherEntry getVoucherEntryByOrderEntry(Collection c, AbstractOrderEntry anAbstractOrderEntry)
    {
        for(Iterator iterator = c.iterator(); iterator.hasNext(); )
        {
            try
            {
                VoucherEntry nextEntry = checkForVoucherEntry(iterator.next());
                if(nextEntry.getOrderEntry().equals(anAbstractOrderEntry))
                {
                    return nextEntry;
                }
            }
            catch(IllegalArgumentException e)
            {
            }
        }
        return null;
    }


    public boolean isEmpty()
    {
        return this.set.isEmpty();
    }


    public Iterator iterator()
    {
        return this.set.iterator();
    }


    public boolean remove(Object o)
    {
        if(contains(o))
        {
            VoucherEntry entry = checkForVoucherEntry(o);
            VoucherEntry existingEntry = getVoucherEntryByOrderEntry(entry.getOrderEntry());
            if(existingEntry != null)
            {
                if(existingEntry.getQuantity() > entry.getQuantity())
                {
                    existingEntry.setQuantity(existingEntry.getQuantity() - entry.getQuantity());
                }
                else
                {
                    this.set.remove(entry);
                }
                return true;
            }
        }
        return false;
    }


    public boolean removeAll(Collection c)
    {
        boolean changed = false;
        for(Iterator iterator = c.iterator(); iterator.hasNext(); )
        {
            changed |= remove(iterator.next());
        }
        return changed;
    }


    public boolean retainAll(Collection c)
    {
        boolean changed = false;
        for(Iterator<VoucherEntry> iterator = this.set.iterator(); iterator.hasNext(); )
        {
            VoucherEntry nextEntry = iterator.next();
            VoucherEntry retainingEntry = getVoucherEntryByOrderEntry(c, nextEntry.getOrderEntry());
            if(retainingEntry == null)
            {
                iterator.remove();
                changed = true;
                continue;
            }
            if(!nextEntry.equals(retainingEntry))
            {
                nextEntry.setQuantity(Math.min(nextEntry.getQuantity(), retainingEntry
                                .getUnit().convert(nextEntry.getUnit(), retainingEntry.getQuantity())));
                changed = true;
            }
        }
        return changed;
    }


    public int size()
    {
        return this.set.size();
    }


    public Object[] toArray()
    {
        return this.set.toArray();
    }


    public Object[] toArray(Object[] a)
    {
        return this.set.toArray(a);
    }
}
