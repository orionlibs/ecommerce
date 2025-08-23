package de.hybris.platform.warehousing.asn.service.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.exceptions.ModelSavingException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.localization.Localization;
import de.hybris.platform.warehousing.asn.service.AsnWorkflowService;
import de.hybris.platform.warehousing.model.AdvancedShippingNoticeModel;
import de.hybris.platform.workflow.WorkflowProcessingService;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class DefaultAsnWorkflowService implements AsnWorkflowService
{
    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultAsnWorkflowService.class);
    protected static final String CONSIGNMENT_TEMPLATE_NAME = "warehousing.asn.workflow.template";
    protected static final String WORKFLOW_OF_ASN = "asn_workflow_";
    private ModelService modelService;
    private WorkflowService workflowService;
    private WorkflowTemplateService workflowTemplateService;
    private WorkflowProcessingService workflowProcessingService;
    private UserService userService;
    private ConfigurationService configurationService;


    public void startAsnCancellationWorkflow(AdvancedShippingNoticeModel advancedShippingNotice)
    {
        String asnWorkflowName = getConfigurationService().getConfiguration().getString("warehousing.asn.workflow.template");
        try
        {
            WorkflowTemplateModel workflowTemplate = getWorkflowTemplateService().getWorkflowTemplateForCode(asnWorkflowName);
            if(workflowTemplate != null)
            {
                WorkflowModel workflow = getWorkflowService().createWorkflow("asn_workflow_" + advancedShippingNotice.getInternalId(), workflowTemplate,
                                Collections.singletonList(advancedShippingNotice), (UserModel)getUserService().getAdminUser());
                getModelService().save(workflow);
                workflow.getActions().forEach(action -> getModelService().save(action));
                getWorkflowProcessingService().startWorkflow(workflow);
                workflow.setOwner((ItemModel)getUserService().getAdminUser());
                workflow.getActions().forEach(action -> {
                    action.setPrincipalAssigned((PrincipalModel)getUserService().getAdminUser());
                    getModelService().save(action);
                });
                getModelService().save(workflow);
            }
            else
            {
                LOGGER.debug(Localization.getLocalizedString("warehousing.asncancellation.workflow.no.template.found"));
            }
        }
        catch(UnknownIdentifierException | IllegalArgumentException e)
        {
            LOGGER.debug(
                            String.format(Localization.getLocalizedString("warehousing.asncancellation.workflow.no.template.found.for.code"), new Object[] {asnWorkflowName}));
        }
        catch(ModelSavingException e)
        {
            LOGGER.debug(
                            String.format(Localization.getLocalizedString("warehousing.asncancellation.workflow.error"), new Object[] {advancedShippingNotice.getInternalId()}));
        }
    }


    protected WorkflowService getWorkflowService()
    {
        return this.workflowService;
    }


    @Required
    public void setWorkflowService(WorkflowService workflowService)
    {
        this.workflowService = workflowService;
    }


    protected WorkflowTemplateService getWorkflowTemplateService()
    {
        return this.workflowTemplateService;
    }


    @Required
    public void setWorkflowTemplateService(WorkflowTemplateService workflowTemplateService)
    {
        this.workflowTemplateService = workflowTemplateService;
    }


    protected WorkflowProcessingService getWorkflowProcessingService()
    {
        return this.workflowProcessingService;
    }


    @Required
    public void setWorkflowProcessingService(WorkflowProcessingService workflowProcessingService)
    {
        this.workflowProcessingService = workflowProcessingService;
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


    protected ConfigurationService getConfigurationService()
    {
        return this.configurationService;
    }


    @Required
    public void setConfigurationService(ConfigurationService configurationService)
    {
        this.configurationService = configurationService;
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
}
