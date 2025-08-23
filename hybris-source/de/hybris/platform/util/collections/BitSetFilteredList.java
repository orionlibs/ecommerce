package de.hybris.platform.util.collections;

import com.google.common.base.Preconditions;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;

public class BitSetFilteredList<E> extends AbstractList<E>
{
    private final List<E> original;
    private final BitSet permitted;


    public BitSetFilteredList(BitSet permitted, E[] original)
    {
        this(permitted, Arrays.asList(original));
    }


    public BitSetFilteredList(BitSet permitted, List<E> original)
    {
        if(permitted == null)
        {
            throw new NullPointerException("bitset cannot be null");
        }
        if(original.size() < permitted.length())
        {
            throw new IllegalArgumentException("original list is too small for bit set (" + original.size() + "<" + permitted
                            .length() + ")");
        }
        this.original = original;
        this.permitted = permitted;
    }


    public E get(int index)
    {
        if(index >= size())
        {
            throw new IndexOutOfBoundsException("" + index + ">" + index);
        }
        int idx = 0;
        int pos = this.permitted.nextSetBit(0);
        for(; pos != -1 && idx < index; idx++)
        {
            pos = this.permitted.nextSetBit(pos + 1);
        }
        Preconditions.checkArgument((idx == index));
        Preconditions.checkArgument((pos != -1));
        return this.original.get(pos);
    }


    public boolean contains(Object o)
    {
        return (indexOf(o) != -1);
    }


    public int indexOf(Object o)
    {
        int idx = 0;
        for(int pos = this.permitted.nextSetBit(0); pos != -1; pos = this.permitted.nextSetBit(pos + 1), idx++)
        {
            Object got = this.original.get(pos);
            if(got == o || (got != null && o != null && got.equals(o)))
            {
                return idx;
            }
        }
        return -1;
    }


    public int size()
    {
        return this.permitted.cardinality();
    }
}
