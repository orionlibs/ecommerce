package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.warehousing.util.builder.AutomatedWorkflowActionTemplateModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.AutomatedWorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Required;

public class AutomatedWorkflowActionTemplates extends AbstractItems<AutomatedWorkflowActionTemplateModel>
{
    public static final String AUTO_PACK_CODE = "NPR_Automated_Packing";
    public static final String AUTO_PACK_NAME = "Automated Packing";
    public static final String AUTO_PACK_HANDLER = "taskAssignmentPackConsignmentAction";
    public static final String AUTO_SHIP_CODE = "NPR_Automated_Shipping";
    public static final String AUTO_SHIP_NAME = "Automated Shipping";
    public static final String AUTO_SHIP_HANDLER = "taskAssignmentShipConsignmentAction";
    public static final String AUTO_PICKUP_CODE = "NPR_Automated_Pickup";
    public static final String AUTO_PICKUP_NAME = "Automated Pick up";
    public static final String AUTO_PICKUP_HANDLER = "taskAssignmentPickupConsignmentAction";
    public static final String AUTO_REALLOCATE_CONS_CODE = "ASN_Automated_ReallocateConsignments";
    public static final String AUTO_REALLOCATE_CONS_NAME = "Automated Reallocate Consignments";
    public static final String AUTO_REALLOCATE_HANDLER = "taskReallocateConsignmentsOnAsnCancelAction";
    public static final String AUTO_DELETE_CANCELEVENT_CODE = "ASN_Automated_DeleteCancellationEvents";
    public static final String AUTO_DELETE_CANCELEVENT_NAME = "Automated Delete CancellationEvents";
    public static final String AUTO_DELETE_CANCELLATION_EVENT_HANDLER = "taskDeleteCancellationEventsOnAsnCancelAction";
    public static final String AUTO_DELETE_STOCKS_CODE = "ASN_Automated_DeleteStockLevels";
    public static final String AUTO_DELETE_STOCKS_NAME = "Automated Delete StockLevels";
    public static final String AUTO_DELETE_STOCKLEVEL_HANDLER = "taskDeleteStockLevelsOnAsnCancelAction";
    private WarehousingDao<AutomatedWorkflowActionTemplateModel> automatedWorkflowActionTemplateDao;
    private WorkflowDecisionTemplates workflowDecisionTemplates;
    private WorkflowTemplates workflowTemplates;
    private UserService userService;


    public AutomatedWorkflowActionTemplateModel AutoPacking()
    {
        return (AutomatedWorkflowActionTemplateModel)getOrSaveAndReturn(() -> (AutomatedWorkflowActionTemplateModel)getAutomatedWorkflowActionTemplateDao().getByCode("NPR_Automated_Packing"),
                        () -> AutomatedWorkflowActionTemplateModelBuilder.aModel().withCode("NPR_Automated_Packing").withName("Automated Packing").withJobHandler("taskAssignmentPackConsignmentAction").withAction(WorkflowActionType.NORMAL).withWorkflow(getWorkflowTemplates().ConsignmentTemplate())
                                        .withPrincipal((PrincipalModel)getUserService().getAdminUser()).withDecision(Arrays.asList(new WorkflowDecisionTemplateModel[] {getWorkflowDecisionTemplates().AutoPackingShipping(), getWorkflowDecisionTemplates().AutoPackingPickup()})).build());
    }


    public AutomatedWorkflowActionTemplateModel AutoShipping()
    {
        return (AutomatedWorkflowActionTemplateModel)getOrSaveAndReturn(() -> (AutomatedWorkflowActionTemplateModel)getAutomatedWorkflowActionTemplateDao().getByCode("NPR_Automated_Shipping"),
                        () -> AutomatedWorkflowActionTemplateModelBuilder.aModel().withCode("NPR_Automated_Shipping").withName("Automated Shipping").withJobHandler("taskAssignmentShipConsignmentAction").withAction(WorkflowActionType.NORMAL).withWorkflow(getWorkflowTemplates().ConsignmentTemplate())
                                        .withPrincipal((PrincipalModel)getUserService().getAdminUser()).withDecision(Arrays.asList(new WorkflowDecisionTemplateModel[] {getWorkflowDecisionTemplates().AutoShipping()})).build());
    }


    public AutomatedWorkflowActionTemplateModel AutoPickup()
    {
        return (AutomatedWorkflowActionTemplateModel)getOrSaveAndReturn(() -> (AutomatedWorkflowActionTemplateModel)getAutomatedWorkflowActionTemplateDao().getByCode("NPR_Automated_Pickup"),
                        () -> AutomatedWorkflowActionTemplateModelBuilder.aModel().withCode("NPR_Automated_Pickup").withName("Automated Pick up").withJobHandler("taskAssignmentPickupConsignmentAction").withAction(WorkflowActionType.NORMAL).withWorkflow(getWorkflowTemplates().ConsignmentTemplate())
                                        .withPrincipal((PrincipalModel)getUserService().getAdminUser()).withDecision(Arrays.asList(new WorkflowDecisionTemplateModel[] {getWorkflowDecisionTemplates().AutoPickup()})).build());
    }


    public AutomatedWorkflowActionTemplateModel AutoReallocateConsignment()
    {
        return (AutomatedWorkflowActionTemplateModel)getOrSaveAndReturn(() -> (AutomatedWorkflowActionTemplateModel)getAutomatedWorkflowActionTemplateDao().getByCode("ASN_Automated_ReallocateConsignments"),
                        () -> AutomatedWorkflowActionTemplateModelBuilder.aModel().withCode("ASN_Automated_ReallocateConsignments").withName("Automated Reallocate Consignments").withJobHandler("taskReallocateConsignmentsOnAsnCancelAction").withAction(WorkflowActionType.START)
                                        .withWorkflow(getWorkflowTemplates().AsnTemplate()).withPrincipal((PrincipalModel)getUserService().getAdminUser()).withDecision(Arrays.asList(new WorkflowDecisionTemplateModel[] {getWorkflowDecisionTemplates().AutoReallocateConsignments()})).build());
    }


    public AutomatedWorkflowActionTemplateModel AutoDeleteCancellationEvent()
    {
        return (AutomatedWorkflowActionTemplateModel)getOrSaveAndReturn(() -> (AutomatedWorkflowActionTemplateModel)getAutomatedWorkflowActionTemplateDao().getByCode("ASN_Automated_DeleteCancellationEvents"),
                        () -> AutomatedWorkflowActionTemplateModelBuilder.aModel().withCode("ASN_Automated_DeleteCancellationEvents").withName("Automated Delete CancellationEvents").withJobHandler("taskDeleteCancellationEventsOnAsnCancelAction").withAction(WorkflowActionType.NORMAL)
                                        .withWorkflow(getWorkflowTemplates().AsnTemplate()).withPrincipal((PrincipalModel)getUserService().getAdminUser()).withDecision(Arrays.asList(new WorkflowDecisionTemplateModel[] {getWorkflowDecisionTemplates().AutoDeleteCancellationEvents()})).build());
    }


    public AutomatedWorkflowActionTemplateModel AutoDeleteStockLevel()
    {
        return (AutomatedWorkflowActionTemplateModel)getOrSaveAndReturn(() -> (AutomatedWorkflowActionTemplateModel)getAutomatedWorkflowActionTemplateDao().getByCode("ASN_Automated_DeleteStockLevels"),
                        () -> AutomatedWorkflowActionTemplateModelBuilder.aModel().withCode("ASN_Automated_DeleteStockLevels").withName("Automated Delete StockLevels").withJobHandler("taskDeleteStockLevelsOnAsnCancelAction").withAction(WorkflowActionType.NORMAL)
                                        .withWorkflow(getWorkflowTemplates().AsnTemplate()).withPrincipal((PrincipalModel)getUserService().getAdminUser()).withDecision(Arrays.asList(new WorkflowDecisionTemplateModel[] {getWorkflowDecisionTemplates().AutoDeleteStockLevels()})).build());
    }


    protected WarehousingDao<AutomatedWorkflowActionTemplateModel> getAutomatedWorkflowActionTemplateDao()
    {
        return this.automatedWorkflowActionTemplateDao;
    }


    @Required
    public void setAutomatedWorkflowActionTemplateDao(WarehousingDao<AutomatedWorkflowActionTemplateModel> automatedWorkflowActionTemplateDao)
    {
        this.automatedWorkflowActionTemplateDao = automatedWorkflowActionTemplateDao;
    }


    protected WorkflowDecisionTemplates getWorkflowDecisionTemplates()
    {
        return this.workflowDecisionTemplates;
    }


    @Required
    public void setWorkflowDecisionTemplates(WorkflowDecisionTemplates workflowDecisionTemplates)
    {
        this.workflowDecisionTemplates = workflowDecisionTemplates;
    }


    protected WorkflowTemplates getWorkflowTemplates()
    {
        return this.workflowTemplates;
    }


    @Required
    public void setWorkflowTemplates(WorkflowTemplates workflowTemplates)
    {
        this.workflowTemplates = workflowTemplates;
    }


    protected UserService getUserService()
    {
        return this.userService;
    }


    @Required
    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }
}
