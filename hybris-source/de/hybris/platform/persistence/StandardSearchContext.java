package de.hybris.platform.persistence;

import de.hybris.platform.jalo.JaloInternalException;
import de.hybris.platform.jalo.SearchContext;
import de.hybris.platform.jalo.SessionContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StandardSearchContext extends SessionContext implements SearchContext
{
    private int from;
    private int count;
    private HashMap props;


    public StandardSearchContext(SessionContext ctx)
    {
        super(ctx);
        this.count = -1;
        this.from = 0;
        this.props = new HashMap<>();
    }


    public StandardSearchContext(StandardSearchContext ctx)
    {
        super(ctx);
        this.count = ctx.count;
        this.from = ctx.from;
        this.props = new HashMap<>(ctx.props);
    }


    protected Object clone() throws CloneNotSupportedException
    {
        return new StandardSearchContext(this);
    }


    public SearchContext getCopy()
    {
        try
        {
            return (SearchContext)clone();
        }
        catch(CloneNotSupportedException e)
        {
            throw new JaloInternalException(e, "could not copy " + this, 0);
        }
    }


    public void setRange(int from, int count)
    {
        this.from = from;
        this.count = count;
    }


    public int getRangeStart()
    {
        return this.from;
    }


    public int getRangeCount()
    {
        return this.count;
    }


    public void setProperty(String name, Object value)
    {
        this.props.put(name, value);
    }


    public Object getProperty(String name)
    {
        return this.props.get(name);
    }


    public Map getProperties()
    {
        return Collections.unmodifiableMap(this.props);
    }


    public void setProperties(Map<?, ?> map)
    {
        this.props = new HashMap<>(map);
    }
}
