package de.hybris.platform.workflow.services.internal.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.internal.model.impl.ItemModelCloneCreator;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.workflow.daos.WorkflowLinksDao;
import de.hybris.platform.workflow.daos.WorkflowLinksTemplateDao;
import de.hybris.platform.workflow.impl.DefaultWorkflowService;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.services.internal.WorkflowFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DecisionsFromActionTemplateFactory extends AbstractWorkflowFactory implements WorkflowFactory<WorkflowModel, WorkflowActionTemplateModel, List<WorkflowDecisionModel>>
{
    private static final Logger LOG = Logger.getLogger(DefaultWorkflowService.class);
    private TypeService typeService;
    private I18NService i18nService;
    private WorkflowLinksDao workflowLinksDao;
    private WorkflowLinksTemplateDao workflowLinksTemplateDao;
    private WorkflowFactory<WorkflowModel, WorkflowDecisionTemplateModel, WorkflowDecisionModel> decisionFactory;


    @Required
    public void setWorkflowLinksDao(WorkflowLinksDao workflowLinksDao)
    {
        this.workflowLinksDao = workflowLinksDao;
    }


    @Required
    public void setWorkflowLinksTemplateDao(WorkflowLinksTemplateDao workflowLinksTemplateDao)
    {
        this.workflowLinksTemplateDao = workflowLinksTemplateDao;
    }


    @Required
    public void setDecisionFactory(WorkflowFactory<WorkflowModel, WorkflowDecisionTemplateModel, WorkflowDecisionModel> converter)
    {
        this.decisionFactory = converter;
    }


    public List<WorkflowDecisionModel> create(WorkflowModel root, WorkflowActionTemplateModel templateAction)
    {
        WorkflowActionModel action = getWorkAction(templateAction, root.getActions());
        List<WorkflowDecisionModel> decisions = new ArrayList<>(action.getDecisions());
        for(WorkflowDecisionTemplateModel workflowDecisionTemplate : templateAction.getDecisionTemplates())
        {
            WorkflowDecisionModel newDecision = (WorkflowDecisionModel)this.decisionFactory.create(root, workflowDecisionTemplate);
            ItemModelCloneCreator cloneCreator = new ItemModelCloneCreator(this.modelService, this.i18nService, this.typeService);
            cloneCreator.copyAttributes((ItemModel)workflowDecisionTemplate, (ItemModel)newDecision, getAttributesToCopy());
            newDecision.setAction(getWorkAction(workflowDecisionTemplate.getActionTemplate(), root.getActions()));
            this.modelService.save(newDecision);
            decisions.add(newDecision);
            this.modelService.save(action);
            copyAndConnectionAttribute(workflowDecisionTemplate, root.getActions(), newDecision);
        }
        action.setDecisions(decisions);
        this.modelService.save(action);
        return decisions;
    }


    private static final Set<String> EXCLUDED_ATTRIBUTES = new HashSet<>(Arrays.asList(new String[0]));


    private Set<String> getAttributesToCopy()
    {
        Set<String> result = new HashSet<>();
        ComposedTypeModel type = this.typeService.getComposedTypeForCode("AbstractWorkflowDecision");
        for(AttributeDescriptorModel attributeDescriptor : type.getDeclaredattributedescriptors())
        {
            if(!EXCLUDED_ATTRIBUTES.contains(attributeDescriptor.getQualifier()))
            {
                result.add(attributeDescriptor.getQualifier());
            }
        }
        return result;
    }


    private void copyAndConnectionAttribute(WorkflowDecisionTemplateModel workflowDecisionTemplate, List<WorkflowActionModel> workflowActions, WorkflowDecisionModel newDecision)
    {
        Collection<LinkModel> linkTemplates = this.workflowLinksTemplateDao.findLinksByDecisionAndAction((AbstractWorkflowDecisionModel)workflowDecisionTemplate, null);
        if(LOG.isDebugEnabled())
        {
            LOG.debug("linkTemplates.size()" + linkTemplates.size());
        }
        for(LinkModel linkTemplate : linkTemplates)
        {
            WorkflowActionTemplateModel acttionTemplate = (WorkflowActionTemplateModel)linkTemplate.getTarget();
            WorkflowActionModel action = getWorkAction(acttionTemplate, workflowActions);
            Collection<LinkModel> links = this.workflowLinksDao.findLinksByDecisionAndAction((AbstractWorkflowDecisionModel)newDecision, (AbstractWorkflowActionModel)action);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("links.size():" + links.size());
            }
            try
            {
                setAttributeForLink(links.iterator().next(), "andconnection", linkTemplate, "andConnectionTemplate");
            }
            catch(Exception e)
            {
                LOG.error(e.toString());
            }
        }
    }


    private void setAttributeForLink(LinkModel link, String attribute, LinkModel source, String attributeSource)
    {
        Link linkSource = (Link)this.modelService.getSource(link);
        Link linkTarget = (Link)this.modelService.getSource(source);
        try
        {
            linkSource.setAttribute(attribute, linkTarget.getAttribute(attributeSource));
        }
        catch(Exception e)
        {
            LOG.error(e);
        }
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }


    public void setI18nService(I18NService i18nService)
    {
        this.i18nService = i18nService;
    }
}
