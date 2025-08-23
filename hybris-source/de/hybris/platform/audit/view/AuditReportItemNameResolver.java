package de.hybris.platform.audit.view;

import de.hybris.platform.audit.internal.config.AuditReportItemNameResolvable;
import java.util.Set;

public interface AuditReportItemNameResolver
{
    String getName(Set<String> paramSet, AuditReportItemNameResolvable paramAuditReportItemNameResolvable);
}
