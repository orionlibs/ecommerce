package de.hybris.platform.workflow.daos;

import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import java.util.Collection;
import java.util.List;

public interface WorkflowLinksDao
{
    List<LinkModel> findWorkflowActionLinkRelationBySource(String paramString);


    Collection<LinkModel> findLinksByDecisionAndAction(AbstractWorkflowDecisionModel paramAbstractWorkflowDecisionModel, AbstractWorkflowActionModel paramAbstractWorkflowActionModel);
}
