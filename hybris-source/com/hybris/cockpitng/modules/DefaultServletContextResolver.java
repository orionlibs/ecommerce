/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.modules;

import javax.servlet.ServletContext;
import org.zkoss.zk.ui.Execution;
import org.zkoss.zk.ui.Executions;

/**
 * Implementation of {@link ServletContextResolver} using ZK's {@link Execution} to retrieve the current
 * {@link ServletContext}.
 */
public class DefaultServletContextResolver implements ServletContextResolver
{
    @Override
    public ServletContext getServletContext()
    {
        final Execution exec = Executions.getCurrent();
        if(exec != null && exec.getDesktop() != null && exec.getDesktop().getWebApp() != null)
        {
            return exec.getDesktop().getWebApp().getServletContext();
        }
        return null;
    }
}
