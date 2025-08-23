package de.hybris.platform.audit.view;

import de.hybris.platform.audit.TypeAuditReportConfig;
import de.hybris.platform.audit.view.impl.ReportView;
import java.util.stream.Stream;

public interface AuditViewService
{
    Stream<ReportView> getViewOn(TypeAuditReportConfig paramTypeAuditReportConfig);
}
