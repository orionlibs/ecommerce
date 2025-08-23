package de.hybris.platform.wishlist2.retention.hook.impl;

import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.directpersistence.audit.dao.WriteAuditRecordsDAO;
import de.hybris.platform.retention.hook.ItemCleanupHook;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.wishlist2.model.Wishlist2Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class WishlistCustomerCleanupHook implements ItemCleanupHook<CustomerModel>
{
    private static final Logger LOG = LoggerFactory.getLogger(WishlistCustomerCleanupHook.class);
    private ModelService modelService;
    private WriteAuditRecordsDAO writeAuditRecordsDAO;


    public void cleanupRelatedObjects(CustomerModel customerModel)
    {
        ServicesUtil.validateParameterNotNullStandardMessage("customerModel", customerModel);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("Cleaning up customer wishlist related objects for: {}", customerModel);
        }
        for(Wishlist2Model wishlist : customerModel.getWishlist())
        {
            getModelService().remove(wishlist);
            getWriteAuditRecordsDAO().removeAuditRecordsForType("Wishlist2", wishlist.getPk());
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


    protected WriteAuditRecordsDAO getWriteAuditRecordsDAO()
    {
        return this.writeAuditRecordsDAO;
    }


    @Required
    public void setWriteAuditRecordsDAO(WriteAuditRecordsDAO writeAuditRecordsDAO)
    {
        this.writeAuditRecordsDAO = writeAuditRecordsDAO;
    }
}
