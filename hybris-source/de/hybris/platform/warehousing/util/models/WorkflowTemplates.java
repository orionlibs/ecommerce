package de.hybris.platform.warehousing.util.models;

import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.warehousing.util.builder.WorkflowTemplateModelBuilder;
import de.hybris.platform.warehousing.util.dao.WarehousingDao;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Required;

public class WorkflowTemplates extends AbstractItems<WorkflowTemplateModel>
{
    public static final String WORKFLOW_CONSIGNMENT_TEMPLATE_CODE = "ConsignmentTemplate";
    public static final String WORKFLOW_CONSIGNMENT_TEMPLATE_NAME = "Consignment Template";
    public static final String WORKFLOW_ASN_TEMPLATE_CODE = "AsnTemplate";
    public static final String WORKFLOW_ASN_TEMPLATE_NAME = "Asn Template";
    private WarehousingDao<WorkflowTemplateModel> workflowTemplateDao;
    private UserService userService;
    private UserGroups userGroups;
    private WorkflowActionTemplates workflowActionTemplates;
    private AutomatedWorkflowActionTemplates automatedWorkflowActionTemplates;


    public WorkflowTemplateModel ConsignmentTemplate()
    {
        return (WorkflowTemplateModel)getOrSaveAndReturn(() -> (WorkflowTemplateModel)getWorkflowTemplateDao().getByCode("ConsignmentTemplate"), () -> WorkflowTemplateModelBuilder.aModel().withCode("ConsignmentTemplate").withName("Consignment Template").withPrincipals(Arrays.asList(
                        new PrincipalModel[] {(PrincipalModel)getUserGroups().AdminGroup(), (PrincipalModel)getUserGroups().WarehouseAdministratorGroup(), (PrincipalModel)getUserGroups().WarehouseManagerGroup(), (PrincipalModel)getUserGroups().WarehouseAgentGroup(),
                                        (PrincipalModel)getUserGroups().CustomerGroup()})).build());
    }


    public WorkflowTemplateModel AsnTemplate()
    {
        return (WorkflowTemplateModel)getOrSaveAndReturn(() -> (WorkflowTemplateModel)getWorkflowTemplateDao().getByCode("AsnTemplate"),
                        () -> WorkflowTemplateModelBuilder.aModel().withCode("AsnTemplate").withName("Asn Template").withPrincipals(Arrays.asList(new PrincipalModel[] {(PrincipalModel)getUserGroups().AdminGroup(), (PrincipalModel)getUserGroups().WarehouseAdministratorGroup()})).build());
    }


    protected WarehousingDao<WorkflowTemplateModel> getWorkflowTemplateDao()
    {
        return this.workflowTemplateDao;
    }


    @Required
    public void setWorkflowTemplateDao(WarehousingDao<WorkflowTemplateModel> workflowTemplateDao)
    {
        this.workflowTemplateDao = workflowTemplateDao;
    }


    protected UserGroups getUserGroups()
    {
        return this.userGroups;
    }


    @Required
    public void setUserGroups(UserGroups userGroups)
    {
        this.userGroups = userGroups;
    }


    protected WorkflowActionTemplates getWorkflowActionTemplates()
    {
        return this.workflowActionTemplates;
    }


    @Required
    public void setWorkflowActionTemplates(WorkflowActionTemplates workflowActionTemplates)
    {
        this.workflowActionTemplates = workflowActionTemplates;
    }


    protected AutomatedWorkflowActionTemplates getAutomatedWorkflowActionTemplates()
    {
        return this.automatedWorkflowActionTemplates;
    }


    @Required
    public void setAutomatedWorkflowActionTemplates(AutomatedWorkflowActionTemplates automatedWorkflowActionTemplates)
    {
        this.automatedWorkflowActionTemplates = automatedWorkflowActionTemplates;
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
