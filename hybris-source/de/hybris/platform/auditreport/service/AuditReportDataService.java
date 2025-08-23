package de.hybris.platform.auditreport.service;

import de.hybris.platform.auditreport.model.AuditReportDataModel;
import de.hybris.platform.core.model.ItemModel;

public interface AuditReportDataService
{
    AuditReportDataModel createReport(CreateAuditReportParams paramCreateAuditReportParams);


    void deleteReportsForItem(ItemModel paramItemModel);
}
