package de.hybris.platform.persistence.audit;

import de.hybris.platform.core.PK;

public interface AuditableOperationHandler
{
    void aboutToExecute(InternalAuditableOperation paramInternalAuditableOperation);


    void successfullyExecuted(InternalAuditableOperation paramInternalAuditableOperation);


    void failedToExecute(InternalAuditableOperation paramInternalAuditableOperation);


    void clearAudit(PK paramPK);


    void clearAudit(String paramString);
}
