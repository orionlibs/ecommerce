package de.hybris.platform.persistence.audit.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.persistence.audit.AuditScopeInvalidator;
import de.hybris.platform.persistence.audit.AuditableOperations;

public class DefaultAuditScopeInvalidator implements AuditScopeInvalidator
{
    public void clearCurrentAuditForPK(PK itemPK)
    {
        AuditableOperations.clearCurrentAuditOperationsFor(itemPK);
    }


    public void clearCurrentAuditForType(String typeCode)
    {
        AuditableOperations.clearCurrentAuditOperationsFor(typeCode);
    }
}
