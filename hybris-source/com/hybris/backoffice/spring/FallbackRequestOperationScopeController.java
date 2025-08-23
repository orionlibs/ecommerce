/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.spring;

import com.hybris.backoffice.jalo.BackofficeManager;
import com.hybris.backoffice.jalo.PersistenceLayerSessionListener;
import com.hybris.cockpitng.core.model.Identifiable;
import com.hybris.cockpitng.core.spring.RequestOperationContextHolder;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.Tenant;
import de.hybris.platform.core.TenantListener;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.JaloSessionListener;

/**
 * A {@link JaloSessionListener} that manages {@link com.hybris.cockpitng.core.spring.RequestOperationScope} for those
 * threads that are not bound to any {@link java.net.http.HttpRequest} nor
 * {@link com.hybris.cockpitng.engine.operations.LongOperation} triggered by {@link java.net.http.HttpRequest}.
 */
public class FallbackRequestOperationScopeController implements PersistenceLayerSessionListener, Identifiable
{
    @Override
    public Object getId()
    {
        return getClass().getName();
    }


    public void init()
    {
        if(Registry.hasCurrentTenant() && JaloSession.hasCurrentSession())
        {
            BackofficeManager.getInstance().addSessionListener(this);
        }
        Registry.registerTenantListener(new TenantListener()
        {
            @Override
            public void afterTenantStartUp(final Tenant tenant)
            {
                if(Registry.hasCurrentTenant() && JaloSession.hasCurrentSession())
                {
                    BackofficeManager.getInstance().addSessionListener(FallbackRequestOperationScopeController.this);
                }
            }


            @Override
            public void beforeTenantShutDown(final Tenant tenant)
            {
                if(Registry.hasCurrentTenant() && JaloSession.hasCurrentSession())
                {
                    BackofficeManager.getInstance().removeListener(FallbackRequestOperationScopeController.this);
                }
            }


            @Override
            public void afterSetActivateSession(final Tenant tenant)
            {
                // NOP
            }


            @Override
            public void beforeUnsetActivateSession(final Tenant tenant)
            {
                // NOP
            }
        });
    }


    @Override
    public void sessionCreated(final Object session)
    {
        initializeContext();
    }


    protected void initializeContext()
    {
        RequestOperationContextHolder.instance().initializeContext();
    }


    @Override
    public void sessionClosed(final Object session)
    {
        finalizeContext();
    }


    protected void finalizeContext()
    {
        RequestOperationContextHolder.instance().detachFromContext();
    }
}
