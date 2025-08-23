package de.hybris.platform.persistence.audit;

import java.util.Collection;

public interface AuditableSaver
{
    void storeAudit(Collection<AuditableChange> paramCollection);
}
