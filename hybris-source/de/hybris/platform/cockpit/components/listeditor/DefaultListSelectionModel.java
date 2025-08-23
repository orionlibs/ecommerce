package de.hybris.platform.cockpit.components.listeditor;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultListSelectionModel implements ListSelectionModel
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultListSelectionModel.class);
    private final List<ListSelectionListener> listenerList = new ArrayList<>();
    private BitSet value = new BitSet(32);


    public void insertIndexRange(int index, int length)
    {
        int maxBit = getMaxSelectionIndex();
        if(index <= maxBit)
        {
            BitSet newOne = new BitSet(this.value.size() + length);
            newOne.or(this.value.get(0, index));
            int i;
            for(i = this.value.nextSetBit(index); i != -1; i = this.value.nextSetBit(i + 1))
            {
                newOne.set(i + length);
            }
            this.value = newOne;
        }
    }


    public void removeIndexRange(int index, int length)
    {
        int maxBit = getMaxSelectionIndex();
        if(index <= maxBit)
        {
            int newSize = maxBit + 1 + Math.max(0, maxBit - index + length);
            BitSet newOne = new BitSet(newSize);
            newOne.or(this.value.get(0, index));
            if(maxBit >= index + length)
            {
                int i;
                for(i = this.value.nextSetBit(index + length); i != -1; i = this.value.nextSetBit(i + 1))
                {
                    newOne.set(i - length);
                }
            }
            this.value = newOne;
        }
    }


    public void setSelectionInterval(int index0, int index1)
    {
        addSelectionInterval(index0, index1);
        clearOutsideRange(Math.min(index0, index1), Math.max(index0, index1));
    }


    public void addSelectionInterval(int index0, int index1)
    {
        if(index0 == -1 || index1 == -1)
        {
            return;
        }
        int min = Math.min(index0, index1);
        int max = Math.max(index0, index1);
        for(int i = min; i <= max; i++)
        {
            set(i);
        }
    }


    public void toggleSelectionInterval(int index0, int index1)
    {
        int min = Math.min(index0, index1);
        int max = Math.max(index0, index1);
        for(int i = min; i <= max; i++)
        {
            if(isSelectedIndex(i))
            {
                clear(i);
            }
            else
            {
                set(i);
            }
        }
    }


    protected void clearOutsideRange(int min, int max)
    {
        int i;
        for(i = getMinSelectionIndex(); i < min; i++)
        {
            clear(i);
        }
        for(i = getMaxSelectionIndex(); i > max; i--)
        {
            clear(i);
        }
    }


    public List<Integer> getAllSelectedIndexes()
    {
        List<Integer> ret = new ArrayList<>(this.value.size());
        for(int pos = this.value.nextSetBit(0); pos > -1; pos = this.value.nextSetBit(pos + 1))
        {
            ret.add(Integer.valueOf(pos));
        }
        return ret;
    }


    public int getMinSelectionIndex()
    {
        return this.value.nextSetBit(0);
    }


    public int getMaxSelectionIndex()
    {
        return this.value.length() - 1;
    }


    private void fireValueSelectionCleared(int pos)
    {
        for(ListSelectionListener l : new ArrayList(this.listenerList))
        {
            try
            {
                l.valueSelectionCleared(pos);
            }
            catch(RuntimeException e)
            {
                LOG.error("error notifying selection cleared (" + pos + ") in " + l + " : " + e.getMessage(), e);
            }
        }
    }


    private void fireValueSelected(int pos)
    {
        for(ListSelectionListener l : new ArrayList(this.listenerList))
        {
            try
            {
                l.valueSelected(pos);
            }
            catch(RuntimeException e)
            {
                LOG.error("error notifying selected (" + pos + ") in " + l + " : " + e.getMessage(), e);
            }
        }
    }


    private void clear(int index)
    {
        if(this.value.get(index))
        {
            this.value.clear(index);
            fireValueSelectionCleared(index);
        }
    }


    private void set(int index)
    {
        if(!this.value.get(index))
        {
            this.value.set(index);
            fireValueSelected(index);
        }
    }


    public void clearSelection()
    {
        if(!this.value.isEmpty())
        {
            if(this.listenerList.isEmpty())
            {
                this.value.clear();
            }
            else
            {
                BitSet oldOne = this.value;
                this.value = new BitSet();
                for(int i = 0; i < oldOne.length(); i++)
                {
                    if(oldOne.get(i))
                    {
                        fireValueSelectionCleared(i);
                    }
                }
            }
        }
    }


    public boolean isSelectedIndex(int index)
    {
        return this.value.get(index);
    }


    public List<ListSelectionListener> getListeners()
    {
        return this.listenerList;
    }


    public void addListSelectionListener(ListSelectionListener listSelectionListener)
    {
        this.listenerList.add(listSelectionListener);
    }


    public void removeListSelectionListener(ListSelectionListener listSelectionListener)
    {
        this.listenerList.remove(listSelectionListener);
    }
}
