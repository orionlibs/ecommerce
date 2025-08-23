package de.hybris.platform.audit.provider.internal.resolver;

import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.internal.config.AbstractTypedAttribute;
import de.hybris.platform.audit.provider.internal.resolver.impl.AuditTypeContext;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import java.util.Collection;
import java.util.Map;

public interface ReferencesResolver
{
    Collection<ResolveResult> resolve(TypeAuditReportConfig paramTypeAuditReportConfig, Map<AbstractTypedAttribute, AuditTypeContext<AuditRecord>> paramMap, Collection<AuditRecord> paramCollection);
}
