/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.persistence;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.workflow.designer.services.WorkflowModelFinder;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionExecutionBody;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.tx.Transaction;
import de.hybris.platform.tx.TransactionBody;
import de.hybris.platform.util.OneToManyHandler;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Required;

/**
 * Default implementation of persisting Workflow Template that is being edited in Workflow Designer.
 */
public class DefaultWorkflowDesignerPersistenceService implements WorkflowDesignerPersistenceService
{
    private static final Map<String, Object> IGNORE_PART_OF_CONSTRAINT_FOR_DECISIONS = Map
                    .of(OneToManyHandler.IGNORE_PART_OF_CONSTRAINT_FOR_TYPE_CODE, WorkflowDecisionTemplateModel._TYPECODE);
    private List<NetworkChartContextPreprocessor> preprocessors;
    private List<NetworkChartContextPostprocessor> postprocessors;
    private ModelService modelService;
    private SessionService sessionService;
    private WorkflowModelFinder workflowModelFinder;


    /**
     * Persists the Workflow Template data given in the context.
     *
     * @param context
     *           contains the data about the Workflow Template to persist
     */
    @Override
    public void persist(final NetworkChartContext context)
    {
        withinLocalView(IGNORE_PART_OF_CONSTRAINT_FOR_DECISIONS, () -> withinTransaction(() -> {
            executePreprocessors(context, getPreprocessors());
            saveWorkflowTemplate(context);
            executePostprocessors(context, getPostprocessors());
            saveWorkflowTemplate(context);
        }));
    }


    /**
     * Executes given statement within local session that is populated with parameters provided in the map.
     *
     * @param parameters
     *           parameters that will be set in local view
     * @param sessionExecutionBody
     *           callback to be invoked
     */
    protected void withinLocalView(final Map<String, Object> parameters, final Runnable sessionExecutionBody)
    {
        getSessionService().executeInLocalViewWithParams(parameters, new SessionExecutionBody()
        {
            @Override
            public void executeWithoutResult()
            {
                sessionExecutionBody.run();
            }
        });
    }


    /**
     * Executes given statement within single transaction
     *
     * @param transactionBody
     *           the statement that should be executed within transaction
     */
    protected void withinTransaction(final Runnable transactionBody)
    {
        try
        {
            Transaction.current().execute(new TransactionBody()
            {
                @Override
                public <T> T execute()
                {
                    transactionBody.run();
                    return null;
                }
            });
        }
        catch(final Exception e)
        {
            throw new WorkflowDesignerSavingException("Exception during saving workflow model. Transaction will be rolled back", e);
        }
    }


    private void executePreprocessors(final NetworkChartContext context, final List<NetworkChartContextPreprocessor> preprocessors)
    {
        preprocessors.forEach(preprocessor -> preprocessor.preprocess(context));
    }


    private void executePostprocessors(final NetworkChartContext context,
                    final List<NetworkChartContextPostprocessor> postprocessors)
    {
        postprocessors.forEach(postprocessor -> postprocessor.postprocess(context));
    }


    private void saveWorkflowTemplate(final NetworkChartContext context)
    {
        final WorkflowTemplateModel workflowTemplate = getWorkflowModelFinder().findWorkflowTemplate(context);
        final List<ItemModel> models = new ArrayList<>();
        models.addAll(getWorkflowModelFinder().findWorkflowDecisionsFromWorkflowTemplateModel(context));
        models.addAll(getWorkflowModelFinder().findWorkflowActionsFromWorkflowTemplateModel(context));
        models.add(workflowTemplate);
        getModelService().saveAll(models);
    }


    public List<NetworkChartContextPreprocessor> getPreprocessors()
    {
        return preprocessors;
    }


    @Required
    public void setPreprocessors(final List<NetworkChartContextPreprocessor> preprocessors)
    {
        this.preprocessors = preprocessors;
    }


    public ModelService getModelService()
    {
        return modelService;
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }


    public List<NetworkChartContextPostprocessor> getPostprocessors()
    {
        return postprocessors;
    }


    @Required
    public void setPostprocessors(final List<NetworkChartContextPostprocessor> postprocessors)
    {
        this.postprocessors = postprocessors;
    }


    public SessionService getSessionService()
    {
        return sessionService;
    }


    @Required
    public void setSessionService(final SessionService sessionService)
    {
        this.sessionService = sessionService;
    }


    public WorkflowModelFinder getWorkflowModelFinder()
    {
        return workflowModelFinder;
    }


    @Required
    public void setWorkflowModelFinder(final WorkflowModelFinder workflowModelFinder)
    {
        this.workflowModelFinder = workflowModelFinder;
    }
}
