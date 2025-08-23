package de.hybris.platform.persistence.audit;

import de.hybris.platform.core.PK;

public interface AuditScopeInvalidator
{
    void clearCurrentAuditForPK(PK paramPK);


    void clearCurrentAuditForType(String paramString);
}
