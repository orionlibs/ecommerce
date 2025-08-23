package de.hybris.platform.servicelayer.web;

import java.util.List;
import javax.servlet.Filter;

public class PlatformFilterChain extends AbstractPlatformFilterChain
{
    public PlatformFilterChain(List<Filter> internalFilters)
    {
        super(internalFilters);
    }


    protected boolean isBackOfficeFilterChain()
    {
        return false;
    }
}
