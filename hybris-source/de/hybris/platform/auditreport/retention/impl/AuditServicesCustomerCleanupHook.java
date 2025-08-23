package de.hybris.platform.auditreport.retention.impl;

import de.hybris.platform.auditreport.service.AuditReportDataService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.retention.hook.ItemCleanupHook;
import org.springframework.beans.factory.annotation.Required;

public class AuditServicesCustomerCleanupHook implements ItemCleanupHook<CustomerModel>
{
    private AuditReportDataService auditReportDataService;


    public void cleanupRelatedObjects(CustomerModel customer)
    {
        if(customer != null)
        {
            getAuditReportDataService().deleteReportsForItem((ItemModel)customer);
        }
    }


    protected AuditReportDataService getAuditReportDataService()
    {
        return this.auditReportDataService;
    }


    @Required
    public void setAuditReportDataService(AuditReportDataService auditReportDataService)
    {
        this.auditReportDataService = auditReportDataService;
    }
}
