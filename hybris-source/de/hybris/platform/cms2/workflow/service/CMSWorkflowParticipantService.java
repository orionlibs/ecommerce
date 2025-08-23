package de.hybris.platform.cms2.workflow.service;

import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import java.util.List;

public interface CMSWorkflowParticipantService
{
    boolean isWorkflowParticipant(WorkflowModel paramWorkflowModel);


    boolean isParticipantForAttachedItems(List<? extends CMSItemModel> paramList);


    boolean isWorkflowActionParticipant(WorkflowActionModel paramWorkflowActionModel);


    Collection<PrincipalModel> getRelatedPrincipals(PrincipalModel paramPrincipalModel);


    boolean isActiveWorkflowActionParticipant(WorkflowModel paramWorkflowModel);


    boolean isActiveWorkflowActionParticipantForAttachmentItems(List<? extends CMSItemModel> paramList);
}
