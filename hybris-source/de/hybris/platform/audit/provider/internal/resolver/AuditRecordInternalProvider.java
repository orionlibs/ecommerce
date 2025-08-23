package de.hybris.platform.audit.provider.internal.resolver;

import java.util.List;
import java.util.Set;

public interface AuditRecordInternalProvider<AUDIT_RECORD extends de.hybris.platform.persistence.audit.internal.AuditRecordInternal>
{
    List<AUDIT_RECORD> queryRecords(Set<Object> paramSet);
}
