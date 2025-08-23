package de.hybris.platform.audit.provider;

import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.persistence.audit.gateway.AuditRecord;
import java.util.stream.Stream;

public interface AuditRecordsProvider
{
    Stream<AuditRecord> getRecords(TypeAuditReportConfig paramTypeAuditReportConfig);
}
