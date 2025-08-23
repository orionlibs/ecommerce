package de.hybris.platform.servicelayer.internal.converter.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class PrefetchModeResolver
{
    private static final Logger LOG = Logger.getLogger(PrefetchModeResolver.class);
    private final String config;


    public PrefetchModeResolver(String config)
    {
        this.config = config;
    }


    public AttributePrefetchMode getPrefetchMode()
    {
        if("default".equalsIgnoreCase(this.config) || StringUtils.isEmpty(this.config))
        {
            return DefaultPrefetchAttributeMode.ATTRIBUTE_PREFETCH_MODE_NONE;
        }
        if("literal".equalsIgnoreCase(this.config))
        {
            return DefaultPrefetchAttributeMode.ATTRIBUTE_PREFETCH_MODE_LITERAL;
        }
        if("all".equalsIgnoreCase(this.config))
        {
            return DefaultPrefetchAttributeMode.ATTRIBUTE_PREFETCH_MODE_ALL;
        }
        if("none".equalsIgnoreCase(this.config))
        {
            return DefaultPrefetchAttributeMode.ATTRIBUTE_PREFETCH_MODE_NONE;
        }
        LOG.warn("invalid service layer pre-fetch setting servicelayer.prefetch=" + this.config + " - using default instead");
        return DefaultPrefetchAttributeMode.ATTRIBUTE_PREFETCH_MODE_LITERAL;
    }
}
