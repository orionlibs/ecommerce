/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules.spring.filter;

import com.hybris.cockpitng.modules.core.impl.CockpitModuleComponentDefinitionService;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.GenericFilterBean;

/**
 * Filter that sets the module application context class loader as context class loader for the current thread.
 */
public class ExternalModuleContextClassLoaderFilter extends GenericFilterBean
{
    private CockpitModuleComponentDefinitionService componentDefinitionService;


    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
                    throws IOException, ServletException
    {
        final ApplicationContext externalApplicationContext = componentDefinitionService.getExternalApplicationContext();
        if(externalApplicationContext != null)
        {
            Thread.currentThread().setContextClassLoader(externalApplicationContext.getClassLoader());
        }
        chain.doFilter(request, response);
    }


    @Required
    public void setComponentDefinitionService(final CockpitModuleComponentDefinitionService componentDefinitionService)
    {
        this.componentDefinitionService = componentDefinitionService;
    }
}
