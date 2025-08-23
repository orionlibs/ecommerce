package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;

public class HomePageData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private AbstractPageData current;
    private AbstractPageData old;
    private AbstractPageData fallback;


    public void setCurrent(AbstractPageData current)
    {
        this.current = current;
    }


    public AbstractPageData getCurrent()
    {
        return this.current;
    }


    public void setOld(AbstractPageData old)
    {
        this.old = old;
    }


    public AbstractPageData getOld()
    {
        return this.old;
    }


    public void setFallback(AbstractPageData fallback)
    {
        this.fallback = fallback;
    }


    public AbstractPageData getFallback()
    {
        return this.fallback;
    }
}
