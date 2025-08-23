package de.hybris.platform.audit.provider.internal.resolver;

import de.hybris.platform.audit.provider.internal.resolver.impl.AuditTypeContext;
import java.util.List;

public interface VirtualReferenceValuesExtractor
{
    <AUDITRECORD extends de.hybris.platform.persistence.audit.internal.AuditRecordInternal> List<AUDITRECORD> extractValues(AuditRecordInternalProvider<AUDITRECORD> paramAuditRecordInternalProvider, AuditTypeContext<AUDITRECORD> paramAuditTypeContext);
}
