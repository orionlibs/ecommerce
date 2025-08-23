package de.hybris.platform.cms2.workflow.actions;

import de.hybris.platform.cms2.relateditems.RelatedItemsOnPageService;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.jobs.AutomatedWorkflowTemplateJob;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import java.util.function.Predicate;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

public class ApprovePageAutomatedWorkflowAction implements AutomatedWorkflowTemplateJob
{
    private ModelService modelService;
    private Predicate<ItemModel> pageTypePredicate;
    private PlatformTransactionManager transactionManager;
    private RelatedItemsOnPageService relatedItemsOnPageService;


    public WorkflowDecisionModel perform(WorkflowActionModel action)
    {
        (new TransactionTemplate(getTransactionManager())).execute((TransactionCallback)new Object(this, action));
        return action.getDecisions().stream().findFirst().orElse(null);
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


    protected Predicate<ItemModel> getPageTypePredicate()
    {
        return this.pageTypePredicate;
    }


    @Required
    public void setPageTypePredicate(Predicate<ItemModel> pageTypePredicate)
    {
        this.pageTypePredicate = pageTypePredicate;
    }


    protected PlatformTransactionManager getTransactionManager()
    {
        return this.transactionManager;
    }


    @Required
    public void setTransactionManager(PlatformTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }


    protected RelatedItemsOnPageService getRelatedItemsOnPageService()
    {
        return this.relatedItemsOnPageService;
    }


    @Required
    public void setRelatedItemsOnPageService(RelatedItemsOnPageService relatedItemsOnPageService)
    {
        this.relatedItemsOnPageService = relatedItemsOnPageService;
    }
}
