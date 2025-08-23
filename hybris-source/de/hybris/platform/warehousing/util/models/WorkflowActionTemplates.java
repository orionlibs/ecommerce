package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.warehousing.util.builder.WorkflowActionTemplateModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Required;

public class WorkflowActionTemplates extends AbstractItems<WorkflowActionTemplateModel>
{
    public static final String PICKING_CODE = "NPR_Picking";
    public static final String PICKING_NAME = "Picking";
    public static final String PACKING_CODE = "NPR_Packing";
    public static final String PACKING_NAME = "Packing";
    public static final String SHIPPING_CODE = "NPR_Shipping";
    public static final String SHIPPING_NAME = "Shipping";
    public static final String PICKUP_CODE = "NPR_Pickup";
    public static final String PICKUP_NAME = "Pick up";
    public static final String END_CODE = "NPR_End";
    public static final String END_NAME = "End of the Workflow";
    public static final String ASN_END_CODE = "ASN_End";
    public static final String ASN_END_NAME = "End of the Asn Workflow";
    private WarehousingDao<WorkflowActionTemplateModel> workflowActionTemplateDao;
    private WorkflowDecisionTemplates workflowDecisionTemplates;
    private WorkflowTemplates workflowTemplates;
    private UserService userService;


    public WorkflowActionTemplateModel Picking()
    {
        return (WorkflowActionTemplateModel)getOrSaveAndReturn(() -> (WorkflowActionTemplateModel)getWorkflowActionTemplateDao().getByCode("NPR_Picking"),
                        () -> WorkflowActionTemplateModelBuilder.aModel().withCode("NPR_Picking").withName("Picking").withWorkflow(getWorkflowTemplates().ConsignmentTemplate()).withAction(WorkflowActionType.START).withPrincipal((PrincipalModel)getUserService().getAdminUser())
                                        .withDecision(Arrays.asList(new WorkflowDecisionTemplateModel[] {getWorkflowDecisionTemplates().Picking()})).build());
    }


    public WorkflowActionTemplateModel Packing()
    {
        return (WorkflowActionTemplateModel)getOrSaveAndReturn(() -> (WorkflowActionTemplateModel)getWorkflowActionTemplateDao().getByCode("NPR_Packing"),
                        () -> WorkflowActionTemplateModelBuilder.aModel().withCode("NPR_Packing").withName("Packing").withWorkflow(getWorkflowTemplates().ConsignmentTemplate()).withAction(WorkflowActionType.NORMAL).withPrincipal((PrincipalModel)getUserService().getAdminUser())
                                        .withDecision(Arrays.asList(new WorkflowDecisionTemplateModel[] {getWorkflowDecisionTemplates().Packing()})).build());
    }


    public WorkflowActionTemplateModel Shipping()
    {
        return (WorkflowActionTemplateModel)getOrSaveAndReturn(() -> (WorkflowActionTemplateModel)getWorkflowActionTemplateDao().getByCode("NPR_Shipping"),
                        () -> WorkflowActionTemplateModelBuilder.aModel().withCode("NPR_Shipping").withName("Shipping").withWorkflow(getWorkflowTemplates().ConsignmentTemplate()).withAction(WorkflowActionType.NORMAL).withPrincipal((PrincipalModel)getUserService().getAdminUser())
                                        .withDecision(Arrays.asList(new WorkflowDecisionTemplateModel[] {getWorkflowDecisionTemplates().Shipping()})).build());
    }


    public WorkflowActionTemplateModel Pickup()
    {
        return (WorkflowActionTemplateModel)getOrSaveAndReturn(() -> (WorkflowActionTemplateModel)getWorkflowActionTemplateDao().getByCode("NPR_Pickup"),
                        () -> WorkflowActionTemplateModelBuilder.aModel().withCode("NPR_Pickup").withName("Pick up").withWorkflow(getWorkflowTemplates().ConsignmentTemplate()).withAction(WorkflowActionType.NORMAL).withPrincipal((PrincipalModel)getUserService().getAdminUser())
                                        .withDecision(Arrays.asList(new WorkflowDecisionTemplateModel[] {getWorkflowDecisionTemplates().Pickup()})).build());
    }


    public WorkflowActionTemplateModel End()
    {
        return (WorkflowActionTemplateModel)getOrSaveAndReturn(() -> (WorkflowActionTemplateModel)getWorkflowActionTemplateDao().getByCode("NPR_End"),
                        () -> WorkflowActionTemplateModelBuilder.aModel().withCode("NPR_End").withName("End of the Workflow").withPrincipal((PrincipalModel)getUserService().getAdminUser()).withWorkflow(getWorkflowTemplates().ConsignmentTemplate()).withAction(WorkflowActionType.END).build());
    }


    public WorkflowActionTemplateModel EndAsn()
    {
        return (WorkflowActionTemplateModel)getOrSaveAndReturn(() -> (WorkflowActionTemplateModel)getWorkflowActionTemplateDao().getByCode("ASN_End"),
                        () -> WorkflowActionTemplateModelBuilder.aModel().withCode("ASN_End").withName("End of the Asn Workflow").withPrincipal((PrincipalModel)getUserService().getAdminUser()).withWorkflow(getWorkflowTemplates().AsnTemplate()).withAction(WorkflowActionType.END).build());
    }


    protected WarehousingDao<WorkflowActionTemplateModel> getWorkflowActionTemplateDao()
    {
        return this.workflowActionTemplateDao;
    }


    @Required
    public void setWorkflowActionTemplateDao(WarehousingDao<WorkflowActionTemplateModel> workflowActionTemplateDao)
    {
        this.workflowActionTemplateDao = workflowActionTemplateDao;
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
