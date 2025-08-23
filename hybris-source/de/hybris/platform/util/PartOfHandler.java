package de.hybris.platform.util;

import de.hybris.platform.jalo.ConsistencyCheckException;
import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class PartOfHandler<T>
{
    public void setValue(SessionContext ctx, T value)
    {
        Set<Item> toRemove = extractItems(doGetValue(ctx));
        if(!toRemove.isEmpty())
        {
            toRemove.removeAll(extractItems(value));
        }
        doSetValue(ctx, value);
        if(!toRemove.isEmpty())
        {
            for(Item i : Utilities.sortItemsByPK(toRemove))
            {
                if(i.isAlive())
                {
                    try
                    {
                        i.remove(ctx);
                    }
                    catch(ConsistencyCheckException e)
                    {
                        throw new JaloSystemException(e);
                    }
                }
            }
        }
    }


    protected Set<Item> extractItems(Object value)
    {
        if(value == null)
        {
            return Collections.EMPTY_SET;
        }
        Set<Item> ret = new HashSet<>();
        extractItems(ret, value);
        return ret;
    }


    protected void extractItems(Set<Item> ret, Object value)
    {
        if(value instanceof Item)
        {
            ret.add((Item)value);
        }
        else if(value instanceof java.util.Collection)
        {
            for(Object element : value)
            {
                extractItems(ret, element);
            }
        }
        else if(value instanceof Map)
        {
            for(Map.Entry e : ((Map)value).entrySet())
            {
                extractItems(ret, e.getValue());
            }
        }
    }


    protected abstract T doGetValue(SessionContext paramSessionContext);


    protected abstract void doSetValue(SessionContext paramSessionContext, T paramT);
}
