package de.hybris.platform.servicelayer.web;

import java.util.List;
import javax.servlet.Filter;

public class BackOfficeFilterChain extends AbstractPlatformFilterChain
{
    public BackOfficeFilterChain(List<Filter> internalFilters)
    {
        super(internalFilters);
    }


    protected boolean isBackOfficeFilterChain()
    {
        return true;
    }
}
