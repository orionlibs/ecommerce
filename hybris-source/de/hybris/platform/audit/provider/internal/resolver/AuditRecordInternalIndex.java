package de.hybris.platform.audit.provider.internal.resolver;

import de.hybris.platform.core.PK;
import java.util.Set;

public interface AuditRecordInternalIndex<T extends de.hybris.platform.persistence.audit.internal.AuditRecordInternal>
{
    Set<T> getRecords(PK paramPK);


    Set<T> getRecords(String paramString, PK paramPK);


    Set<T> getRecords(String paramString);
}
