package de.hybris.platform.personalizationservices.retention.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.personalizationservices.model.CxResultsModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.retention.hook.ItemCleanupHook;
import de.hybris.platform.servicelayer.model.ModelService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class PersonalizationCustomerCleanupHook implements ItemCleanupHook<CustomerModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(PersonalizationCustomerCleanupHook.class);
    private ModelService modelService;
    private WriteAuditGateway writeAuditGateway;


    public void cleanupRelatedObjects(CustomerModel customer)
    {
        LOG.debug("Cleaning up personalization related objects for: {}", customer);
        for(CxUserToSegmentModel userToSegment : customer.getUserToSegments())
        {
            getModelService().remove(userToSegment);
            getWriteAuditGateway().removeAuditRecordsForType("CxUserToSegment", userToSegment.getPk());
        }
        for(CxResultsModel results : customer.getCxResults())
        {
            getModelService().remove(results);
            getWriteAuditGateway().removeAuditRecordsForType("CxResults", results.getPk());
        }
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected WriteAuditGateway getWriteAuditGateway()
    {
        return this.writeAuditGateway;
    }


    @Required
    public void setWriteAuditGateway(WriteAuditGateway writeAuditGateway)
    {
        this.writeAuditGateway = writeAuditGateway;
    }
}
