package de.hybris.platform.core;

public interface TenantListener
{
    void afterTenantStartUp(Tenant paramTenant);


    void beforeTenantShutDown(Tenant paramTenant);


    void afterSetActivateSession(Tenant paramTenant);


    void beforeUnsetActivateSession(Tenant paramTenant);
}
