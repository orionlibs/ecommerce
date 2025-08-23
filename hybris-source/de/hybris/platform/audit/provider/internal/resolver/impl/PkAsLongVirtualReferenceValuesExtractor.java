package de.hybris.platform.audit.provider.internal.resolver.impl;

import de.hybris.platform.audit.provider.internal.resolver.AuditRecordInternalProvider;
import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceValuesExtractor;
import de.hybris.platform.core.PK;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PkAsLongVirtualReferenceValuesExtractor implements VirtualReferenceValuesExtractor
{
    public <AUDITRECORD extends de.hybris.platform.persistence.audit.internal.AuditRecordInternal> List<AUDITRECORD> extractValues(AuditRecordInternalProvider<AUDITRECORD> provider, AuditTypeContext<AUDITRECORD> ctx)
    {
        Set<Object> pkToLongs = (Set<Object>)ctx.getBasePKs().stream().map(PK::getLong).collect(Collectors.toSet());
        return provider.queryRecords(pkToLongs);
    }
}
