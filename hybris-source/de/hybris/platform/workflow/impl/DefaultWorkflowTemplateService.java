package de.hybris.platform.workflow.impl;

import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloInvalidParameterException;
import de.hybris.platform.jalo.link.Link;
import de.hybris.platform.jalo.security.JaloSecurityException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.util.ServicesUtil;
import de.hybris.platform.workflow.WorkflowTemplateService;
import de.hybris.platform.workflow.daos.WorkflowTemplateDao;
import de.hybris.platform.workflow.model.AbstractWorkflowActionModel;
import de.hybris.platform.workflow.model.AbstractWorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

public class DefaultWorkflowTemplateService implements WorkflowTemplateService
{
    private static final Logger LOG = Logger.getLogger(DefaultWorkflowTemplateService.class);
    private WorkflowTemplateDao workflowTemplateDao;
    private ModelService modelService;


    public List<WorkflowTemplateModel> getAllVisibleWorkflowTemplatesForUser(UserModel user)
    {
        List<WorkflowTemplateModel> tmpls = this.workflowTemplateDao.findWorkflowTemplatesByUser(user);
        Set<WorkflowTemplateModel> templates = new HashSet<>(tmpls);
        List<WorkflowTemplateModel> visibleTmpls = this.workflowTemplateDao.findWorkflowTemplatesVisibleForPrincipal((PrincipalModel)user);
        if(!visibleTmpls.isEmpty())
        {
            templates.addAll(visibleTmpls);
        }
        WorkflowTemplateModel adhocTemplate = getAdhocWorkflowTemplate();
        if(adhocTemplate != null)
        {
            templates.remove(adhocTemplate);
        }
        return new ArrayList<>(templates);
    }


    public List<WorkflowTemplateModel> getAllWorkflowTemplates()
    {
        return this.workflowTemplateDao.findAllWorkflowTemplates();
    }


    public WorkflowTemplateModel getAdhocWorkflowTemplate()
    {
        List<WorkflowTemplateModel> adhocWorkflowTemplates = this.workflowTemplateDao.findAdhocWorkflowTemplates();
        ServicesUtil.validateIfSingleResult(adhocWorkflowTemplates, "Adhoc workflow template not found", "More than one adhoc workflow template has been found");
        return adhocWorkflowTemplates.get(0);
    }


    public EmployeeModel getAdhocWorkflowTemplateDummyOwner()
    {
        return this.workflowTemplateDao.findAdhocWorkflowTemplateDummyOwner();
    }


    public WorkflowActionTemplateModel getWorkflowActionTemplateForCode(String code)
    {
        List<WorkflowActionTemplateModel> templates = this.workflowTemplateDao.findWorkflowActionTemplatesByCode(code);
        ServicesUtil.validateIfSingleResult(templates, "No WorkflowActionTemplate found for code " + code, "Too much WorkflowActionTemplates found for code " + code);
        return templates.get(0);
    }


    public WorkflowTemplateModel getWorkflowTemplateForCode(String code)
    {
        List<WorkflowTemplateModel> templates = this.workflowTemplateDao.findWorkflowTemplatesByCode(code);
        ServicesUtil.validateIfSingleResult(templates, "No WorkflowTemplate found for code " + code, "Too much WorkflowTemplates found for code " + code);
        return templates.get(0);
    }


    @Required
    public void setWorkflowTemplateDao(WorkflowTemplateDao workflowTemplateDao)
    {
        this.workflowTemplateDao = workflowTemplateDao;
    }


    public void setAndConnectionBetweenActionAndDecision(WorkflowDecisionTemplateModel decision, WorkflowActionTemplateModel workflowAction)
    {
        setConnectionBetweenActionAndDecision(decision, workflowAction, true);
    }


    public void setOrConnectionBetweenActionAndDecision(WorkflowDecisionTemplateModel decision, WorkflowActionTemplateModel workflowAction)
    {
        setConnectionBetweenActionAndDecision(decision, workflowAction, false);
    }


    private void setConnectionBetweenActionAndDecision(WorkflowDecisionTemplateModel decision, WorkflowActionTemplateModel workflowAction, boolean isAndConnection)
    {
        Collection<LinkModel> incomingLinkList = getLinkTemplates((AbstractWorkflowDecisionModel)decision, (AbstractWorkflowActionModel)workflowAction);
        for(LinkModel link : incomingLinkList)
        {
            setAttributeForLink(link, "andconnectionTemplate", Boolean.valueOf(isAndConnection));
        }
    }


    private Collection<LinkModel> getLinkTemplates(AbstractWorkflowDecisionModel decision, AbstractWorkflowActionModel action)
    {
        Collection<LinkModel> results;
        if(decision == null && action == null)
        {
            throw new IllegalArgumentException("Decision and action cannot both be null");
        }
        if(action == null)
        {
            results = this.workflowTemplateDao.findWorkflowLinkTemplatesByDecision(decision);
        }
        else if(decision == null)
        {
            results = this.workflowTemplateDao.findWorkflowLinkTemplatesByAction(action);
        }
        else
        {
            results = this.workflowTemplateDao.findWorkflowLinkTemplates(decision, action);
            ServicesUtil.validateIfSingleResult(results, "There is no WorkflowActionTemplateLinkTemplateRelation for source '" + decision
                            .getCode() + "' and target '" + action.getCode() + "'", "There is more than one WorkflowActionTemplateLinkTemplateRelation for source '" + decision
                            .getCode() + "' and target '" + action
                            .getCode() + "'");
        }
        return results;
    }


    private void setAttributeForLink(LinkModel link, String attribute, Boolean value)
    {
        Link linkSource = (Link)this.modelService.getSource(link);
        try
        {
            linkSource.setAttribute(attribute, value);
        }
        catch(JaloInvalidParameterException e)
        {
            LOG.error("Jalo invalid parameter exception", (Throwable)e);
        }
        catch(JaloSecurityException e)
        {
            LOG.error("Jalo security attribute exception", (Throwable)e);
        }
        catch(JaloBusinessException e)
        {
            LOG.error("Jalo business attribute exception", (Throwable)e);
        }
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
