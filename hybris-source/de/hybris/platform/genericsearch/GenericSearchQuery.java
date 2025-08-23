package de.hybris.platform.genericsearch;

import de.hybris.platform.core.GenericQuery;
import de.hybris.platform.servicelayer.search.AbstractQuery;

public class GenericSearchQuery extends AbstractQuery
{
    private final GenericQuery query;


    public GenericSearchQuery(GenericQuery query)
    {
        this.query = query;
    }


    public GenericQuery getQuery()
    {
        return this.query;
    }


    @Deprecated(since = "ages", forRemoval = true)
    public int getRangeCount()
    {
        return getCount();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public int getRangeStart()
    {
        return getStart();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public boolean isDontNeedTotal()
    {
        return !isNeedTotal();
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setDontNeedTotal(boolean dontNeedTotal)
    {
        setNeedTotal(!dontNeedTotal);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setRangeCount(int rangeCount)
    {
        setCount(rangeCount);
    }


    @Deprecated(since = "ages", forRemoval = true)
    public void setRangeStart(int rangeStart)
    {
        setStart(rangeStart);
    }
}
