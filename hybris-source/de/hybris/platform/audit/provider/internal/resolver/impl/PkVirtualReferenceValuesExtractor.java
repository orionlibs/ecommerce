package de.hybris.platform.audit.provider.internal.resolver.impl;

import de.hybris.platform.audit.provider.internal.resolver.AuditRecordInternalProvider;
import de.hybris.platform.audit.provider.internal.resolver.VirtualReferenceValuesExtractor;
import java.util.List;
import org.assertj.core.util.Sets;

public class PkVirtualReferenceValuesExtractor implements VirtualReferenceValuesExtractor
{
    public <AUDITRECORD extends de.hybris.platform.persistence.audit.internal.AuditRecordInternal> List<AUDITRECORD> extractValues(AuditRecordInternalProvider<AUDITRECORD> provider, AuditTypeContext<AUDITRECORD> ctx)
    {
        return provider.queryRecords(Sets.newHashSet(ctx.getBasePKs()));
    }
}
