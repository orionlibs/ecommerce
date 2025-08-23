/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.flow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hybris.backoffice.widgets.networkchart.NetworkChartController;
import com.hybris.backoffice.widgets.networkchart.OptionsProvider;
import com.hybris.backoffice.workflow.designer.WorkflowDesignerModelKey;
import com.hybris.backoffice.workflow.designer.pojo.Workflow;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowAction;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowPojoMapper;
import com.hybris.cockpitng.components.visjs.network.data.Options;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import de.hybris.platform.core.model.ItemModel;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of {@link OptionsProvider} which allows to load options from different files regarding of the model.
 */
public class WorkflowFlowOptionsProvider implements OptionsProvider
{
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowFlowOptionsProvider.class);
    private static final String JSON_PATH_SHOW_FLOW = "/workflow-designer/show-flow-options.json";
    private static final String JSON_PATH_WORKFLOW_DESIGNER = "/workflow-designer/workflow-designer-options.json";
    private final ObjectMapper mapper = new ObjectMapper();


    @Override
    public Options provide(final NetworkChartController controller)
    {
        return getInitModel(controller.getWidgetInstanceManager())
                        .map(template -> resolveOptions(controller.getWidgetInstanceManager(), template)).orElse(emptyOptions());
    }


    protected Optional<Workflow> getInitModel(final WidgetInstanceManager wim)
    {
        return Optional.ofNullable(wim.getModel().getValue(NetworkChartController.MODEL_INIT_DATA, ItemModel.class))
                        .flatMap(WorkflowPojoMapper::mapItemToWorkflow);
    }


    private Options emptyOptions()
    {
        return new Options.Builder().build();
    }


    protected Options resolveOptions(final WidgetInstanceManager wim, final Workflow workflow)
    {
        final boolean isVisualisationSet = isVisualisationSet(workflow);
        storeInModel(wim, WorkflowDesignerModelKey.KEY_IS_VISUALISATION_SET, isVisualisationSet);
        return Optional.of(new Object()).filter(ignore -> isVisualisationSet).flatMap(ignore -> loadWorkflowDesignerOptions())
                        .or(this::loadShowFlowOptions).orElse(emptyOptions());
    }


    private boolean isVisualisationSet(final Workflow workflow)
    {
        final Predicate<Integer> checkIfVisualisationSet = val -> val != null && val != 0;
        final boolean isVisualisationOfActionsSet = workflow.getActions().stream()
                        .anyMatch(action -> checkIfVisualisationSet.test(action.getModel().getVisualisationX())
                                        || checkIfVisualisationSet.test(action.getModel().getVisualisationY()));
        final boolean isVisualisationOfDecisionsSet = workflow.getActions().stream().map(WorkflowAction::getDecisions)
                        .flatMap(Collection::stream)
                        .anyMatch(decision -> checkIfVisualisationSet.test(decision.getModel().getVisualisationX())
                                        || checkIfVisualisationSet.test(decision.getModel().getVisualisationY()));
        return isVisualisationOfActionsSet || isVisualisationOfDecisionsSet;
    }


    protected void storeInModel(final WidgetInstanceManager wim, final String key, final Object value)
    {
        wim.getModel().put(key, value);
    }


    protected Optional<Options> loadWorkflowDesignerOptions()
    {
        return loadOptions(JSON_PATH_WORKFLOW_DESIGNER);
    }


    protected Optional<Options> loadShowFlowOptions()
    {
        return loadOptions(JSON_PATH_SHOW_FLOW);
    }


    protected Optional<Options> loadOptions(final String path)
    {
        try(final InputStream stream = loadResource(path))
        {
            return Optional.ofNullable(mapper.readValue(stream, Options.class));
        }
        catch(final IOException e)
        {
            LOGGER.error(String.format("Cannot load file: `%s`", path), e);
            return Optional.empty();
        }
    }


    private InputStream loadResource(final String path)
    {
        return getClass().getResourceAsStream(path);
    }
}
