package de.hybris.platform.productcockpit.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

public abstract class OneToManySetter
{
    protected abstract Collection doGetMany(Object paramObject);


    protected abstract void doSetMany(Object paramObject, Collection paramCollection);


    protected abstract Object doGetOne(Object paramObject);


    protected abstract void doSetOne(Object paramObject1, Object paramObject2);


    public Collection getMany(Object one)
    {
        return doGetManySafe(one);
    }


    public Object getOne(Object many)
    {
        return doGetOne(many);
    }


    public void setMany(Object one, Collection<?> elements)
    {
        Collection toDetach = new LinkedHashSet(doGetManySafe(one));
        if(elements != null)
        {
            toDetach.removeAll(elements);
        }
        doSetMany(one, elements);
        for(Object p : toDetach)
        {
            doSetOne(p, null);
        }
        if(elements != null)
        {
            for(Object newMany : elements)
            {
                Object prevOne = doGetOne(newMany);
                if(!one.equals(prevOne))
                {
                    if(prevOne != null)
                    {
                        Collection<?> all = doGetManySafe(prevOne);
                        if(all.contains(newMany))
                        {
                            Collection newColl = (all instanceof java.util.Set) ? new LinkedHashSet(all) : new ArrayList(all);
                            newColl.remove(newMany);
                            doSetMany(prevOne, newColl);
                        }
                    }
                    doSetOne(newMany, one);
                }
            }
        }
    }


    public void setOne(Object many, Object one, int position)
    {
        Object prev = doGetOne(many);
        if(prev != one && (prev == null || !prev.equals(one)))
        {
            doSetOne(many, one);
            if(prev != null && doGetManySafe(prev).contains(many))
            {
                Collection all = doGetManySafe(prev);
                Collection newColl = (all instanceof java.util.Set) ? new LinkedHashSet() : new ArrayList();
                newColl.addAll(all);
                newColl.remove(many);
                doSetMany(prev, newColl);
            }
            if(one != null)
            {
                Collection<?> all = doGetManySafe(one);
                if(!all.contains(many))
                {
                    Collection<Object> newColl = (all instanceof java.util.Set) ? new LinkedHashSet(all) : new ArrayList(all);
                    if(newColl instanceof List && position > -1)
                    {
                        ((List<Object>)newColl).add(position, many);
                    }
                    else
                    {
                        newColl.add(many);
                    }
                    doSetMany(one, newColl);
                }
            }
        }
        else if(position > -1 && one != null)
        {
            Collection<?> all = doGetManySafe(one);
            int currentPos = -1;
            if(all instanceof List && (currentPos = ((List)all).indexOf(many)) != position)
            {
                List<Object> lst = new ArrayList(all);
                lst.add(position, many);
                if(currentPos >= 0)
                {
                    lst.remove((currentPos > position) ? (currentPos + 1) : currentPos);
                }
                doSetMany(one, lst);
            }
        }
    }


    protected Collection doGetManySafe(Object one)
    {
        Collection coll = doGetMany(one);
        return (coll != null) ? coll : Collections.EMPTY_LIST;
    }
}
