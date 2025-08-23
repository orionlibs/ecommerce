package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.warehousing.util.builder.WorkflowDecisionTemplateModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import org.springframework.beans.factory.annotation.Required;

public class WorkflowDecisionTemplates extends AbstractItems<WorkflowDecisionTemplateModel>
{
    public static final String PICKING_CODE = "NPR001";
    public static final String PICKING_NAME = "Picked";
    public static final String PACKING_CODE = "NPR002";
    public static final String PACKING_NAME = "Packed";
    public static final String AUTO_PACK_SHIP_CODE = "NPR003";
    public static final String AUTO_PACK_SHIP_NAME = "Redirect to shipping";
    public static final String AUTO_PACK_PICKUP_CODE = "NPR004";
    public static final String AUTO_PACK_PICKUP_NAME = "Redirect to pick up";
    public static final String SHIPPING_CODE = "NPR005";
    public static final String SHIPPING_NAME = "Shipped";
    public static final String AUTO_SHIP_CODE = "NPR006";
    public static final String AUTO_SHIP_NAME = "Automated Shipping";
    public static final String PICKUP_CODE = "NPR007";
    public static final String PICKUP_NAME = "Picked up";
    public static final String AUTO_PICKUP_CODE = "NPR008";
    public static final String AUTO_PICKUP_NAME = "Automated Pick up";
    public static final String AUTO_REALLOCATE_CONS_CODE = "ASN001";
    public static final String AUTO_REALLOCATE_CONS_NAME = "Reallocate Consignmets After ASN cancel";
    public static final String AUTO_DELETE_CANCELLATION_EVENT_CODE = "ASN002";
    public static final String AUTO_DELETE_CANCELLATION_EVENT_NAME = "Delete CancellationEvents After Asn Cancel";
    public static final String AUTO_DELETE_STOCKS_CODE = "ASN003";
    public static final String AUTO_DELETE_STOCKS_NAME = "Delete StockLevels After ASN cancel";
    private WarehousingDao<WorkflowDecisionTemplateModel> workflowDecisionTemplateDao;


    public WorkflowDecisionTemplateModel Picking()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("NPR001"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withCode("NPR001").withName("Picked").build());
    }


    public WorkflowDecisionTemplateModel Packing()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("NPR002"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withCode("NPR002").withName("Packed").build());
    }


    public WorkflowDecisionTemplateModel AutoPackingShipping()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("NPR003"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withCode("NPR003").withName("Redirect to shipping").build());
    }


    public WorkflowDecisionTemplateModel AutoPackingPickup()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("NPR004"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withCode("NPR004").withName("Redirect to pick up").build());
    }


    public WorkflowDecisionTemplateModel Shipping()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("NPR005"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withCode("NPR005").withName("Shipped").build());
    }


    public WorkflowDecisionTemplateModel AutoShipping()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("NPR006"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withCode("NPR006").withName("Automated Shipping").build());
    }


    public WorkflowDecisionTemplateModel Pickup()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("NPR007"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withCode("NPR007").withName("Picked up").build());
    }


    public WorkflowDecisionTemplateModel AutoPickup()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("NPR008"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withCode("NPR008").withName("Automated Pick up").build());
    }


    public WorkflowDecisionTemplateModel AutoReallocateConsignments()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("ASN001"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withName("ASN001").withName("Reallocate Consignmets After ASN cancel").build());
    }


    public WorkflowDecisionTemplateModel AutoDeleteCancellationEvents()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("ASN002"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withName("ASN002").withName("Delete CancellationEvents After Asn Cancel").build());
    }


    public WorkflowDecisionTemplateModel AutoDeleteStockLevels()
    {
        return (WorkflowDecisionTemplateModel)getOrSaveAndReturn(() -> (WorkflowDecisionTemplateModel)getWorkflowDecisionTemplateDao().getByCode("ASN003"), () -> WorkflowDecisionTemplateModelBuilder.aModel().withName("ASN003").withName("Delete StockLevels After ASN cancel").build());
    }


    protected WarehousingDao<WorkflowDecisionTemplateModel> getWorkflowDecisionTemplateDao()
    {
        return this.workflowDecisionTemplateDao;
    }


    @Required
    public void setWorkflowDecisionTemplateDao(WarehousingDao<WorkflowDecisionTemplateModel> workflowDecisionTemplateDao)
    {
        this.workflowDecisionTemplateDao = workflowDecisionTemplateDao;
    }
}
