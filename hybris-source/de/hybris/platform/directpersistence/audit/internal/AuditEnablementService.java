package de.hybris.platform.directpersistence.audit.internal;

import de.hybris.platform.core.PK;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "ages", forRemoval = true)
public class AuditEnablementService
{
    private de.hybris.platform.persistence.audit.internal.AuditEnablementService delegate;


    @PostConstruct
    public void refreshConfiguredAuditTypes()
    {
        this.delegate.refreshConfiguredAuditTypes();
    }


    public boolean isAuditEnabledGlobally()
    {
        return this.delegate.isAuditEnabledGlobally();
    }


    public boolean isAuditEnabledForType(String typeCode)
    {
        return this.delegate.isAuditEnabledForType(typeCode);
    }


    public boolean isAuditEnabledForType(PK typePk)
    {
        return this.delegate.isAuditEnabledForType(typePk);
    }


    public boolean isAuditEnabledForAllTypes()
    {
        return this.delegate.isAuditEnabledForAllTypes();
    }


    @Required
    public void setDelegate(de.hybris.platform.persistence.audit.internal.AuditEnablementService delegate)
    {
        this.delegate = delegate;
    }


    public de.hybris.platform.persistence.audit.internal.AuditEnablementService getDelegate()
    {
        return this.delegate;
    }
}
