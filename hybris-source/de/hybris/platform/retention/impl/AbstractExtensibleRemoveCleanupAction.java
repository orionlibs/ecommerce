package de.hybris.platform.retention.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.persistence.audit.AuditScopeInvalidator;
import de.hybris.platform.persistence.audit.gateway.WriteAuditGateway;
import de.hybris.platform.retention.ItemToCleanup;
import de.hybris.platform.retention.RetentionCleanupAction;
import de.hybris.platform.retention.hook.ItemCleanupHook;
import de.hybris.platform.servicelayer.model.ModelService;
import java.util.List;
import org.springframework.beans.factory.annotation.Required;

public abstract class AbstractExtensibleRemoveCleanupAction<MODEL extends ItemModel> implements RetentionCleanupAction
{
    private List<ItemCleanupHook> itemCleanupHooks;
    private ModelService modelService;
    private WriteAuditGateway writeAuditGateway;
    private AuditScopeInvalidator auditScopeInvalidator;


    protected void cleanupRelatedObjects(MODEL itemModel)
    {
        for(ItemCleanupHook itemCleanupHook : getCleanupHooks())
        {
            itemCleanupHook.cleanupRelatedObjects((ItemModel)itemModel);
        }
        getModelService().refresh(itemModel);
    }


    protected List<ItemCleanupHook> getCleanupHooks()
    {
        return this.itemCleanupHooks;
    }


    protected void removeAuditRecords(ItemToCleanup item)
    {
        this.writeAuditGateway.removeAuditRecordsForType(item.getItemType(), item.getPk());
        this.auditScopeInvalidator.clearCurrentAuditForPK(item.getPk());
    }


    @Required
    public void setItemCleanupHooks(List<ItemCleanupHook> itemCleanupHooks)
    {
        this.itemCleanupHooks = itemCleanupHooks;
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


    @Required
    public void setWriteAuditGateway(WriteAuditGateway writeAuditGateway)
    {
        this.writeAuditGateway = writeAuditGateway;
    }


    @Required
    public void setAuditScopeInvalidator(AuditScopeInvalidator auditScopeInvalidator)
    {
        this.auditScopeInvalidator = auditScopeInvalidator;
    }
}
