package de.hybris.platform.workflow.daos;

import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Collection;
import java.util.List;

public interface WorkflowTemplateDao
{
    List<WorkflowTemplateModel> findWorkflowTemplatesByUser(UserModel paramUserModel);


    List<WorkflowTemplateModel> findWorkflowTemplatesVisibleForPrincipal(PrincipalModel paramPrincipalModel);


    List<WorkflowTemplateModel> findAdhocWorkflowTemplates();


    EmployeeModel findAdhocWorkflowTemplateDummyOwner();


    List<WorkflowTemplateModel> findAllWorkflowTemplates();


    List<WorkflowActionTemplateModel> findWorkflowActionTemplatesByCode(String paramString);


    List<WorkflowTemplateModel> findWorkflowTemplatesByCode(String paramString);


    Collection<LinkModel> findWorkflowLinkTemplatesByAction(AbstractWorkflowActionModel paramAbstractWorkflowActionModel);


    Collection<LinkModel> findWorkflowLinkTemplatesByDecision(AbstractWorkflowDecisionModel paramAbstractWorkflowDecisionModel);


    Collection<LinkModel> findWorkflowLinkTemplates(AbstractWorkflowDecisionModel paramAbstractWorkflowDecisionModel, AbstractWorkflowActionModel paramAbstractWorkflowActionModel);
}
