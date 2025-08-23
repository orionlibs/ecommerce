package de.hybris.platform.servicelayer.web;

import org.springframework.web.filter.GenericFilterBean;

public abstract class AbstractPlatformPreFilter extends GenericFilterBean
{
    public abstract boolean mustStopProcessingChain();
}
