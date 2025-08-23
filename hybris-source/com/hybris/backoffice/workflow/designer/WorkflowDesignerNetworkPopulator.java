/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer;

import com.hybris.backoffice.widgets.networkchart.context.NetworkChartContext;
import com.hybris.backoffice.widgets.networkchart.handler.NetworkPopulator;
import com.hybris.backoffice.workflow.designer.dto.ElementDto;
import com.hybris.backoffice.workflow.designer.dto.ElementLocation;
import com.hybris.backoffice.workflow.designer.dto.Operation;
import com.hybris.backoffice.workflow.designer.handler.create.InitialElementLocationProvider;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowEntity;
import com.hybris.backoffice.workflow.designer.pojo.WorkflowPojoMapper;
import com.hybris.cockpitng.components.visjs.network.data.Network;
import com.hybris.cockpitng.components.visjs.network.data.Node;
import com.hybris.cockpitng.components.visjs.network.response.Action;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdate;
import com.hybris.cockpitng.components.visjs.network.response.NetworkUpdates;
import com.hybris.cockpitng.core.model.WidgetModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.List;
import java.util.Optional;
import org.assertj.core.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;

public class WorkflowDesignerNetworkPopulator implements NetworkPopulator
{
    public static final String MODEL_NEW_WORKFLOW_ITEMS_KEY = "workflowNewModels";
    private static final Logger LOGGER = LoggerFactory.getLogger(WorkflowDesignerNetworkPopulator.class);
    private ModelService modelService;
    private WorkflowNetworkEntitiesFactory workflowNetworkEntitiesFactory;
    private InitialElementLocationProvider initialElementLocationProvider;


    @Override
    public Network populate(final NetworkChartContext networkChartContext)
    {
        final Optional initData = networkChartContext.getInitData();
        if(initData.isPresent() && isWorkflowModel(initData.get()))
        {
            return WorkflowPojoMapper.mapItemToWorkflow((ItemModel)initData.get()).map(workflow -> {
                modelService.refresh(workflow.getModel());
                return workflowNetworkEntitiesFactory.generateNetwork(workflow);
            }).orElse(Network.EMPTY);
        }
        return Network.EMPTY;
    }


    private boolean isWorkflowModel(final Object object)
    {
        return object instanceof WorkflowTemplateModel || object instanceof WorkflowModel;
    }


    @Override
    public NetworkUpdates update(final Object object, final NetworkChartContext networkChartContext)
    {
        if(object instanceof ElementDto<?>)
        {
            final ElementDto<ItemModel> dto = (ElementDto<ItemModel>)object;
            final Optional<WorkflowEntity> entity = WorkflowPojoMapper.mapDtoToWorkflowEntity(dto);
            if(entity.isPresent())
            {
                return tryToUpdateNode(dto, entity.get()).or(() -> createNode(entity.get(), networkChartContext)).stream()
                                .peek(node -> storeInModel(dto.getModel(), networkChartContext)).findFirst()
                                .map(node -> createNetworkUpdates(dto.getOperation(), node)).orElseGet(() -> {
                                    LOGGER.warn(String.format("No renderer found to handle update for entity: [%s]", object));
                                    return NetworkUpdates.EMPTY;
                                });
            }
        }
        return NetworkUpdates.EMPTY;
    }


    private Optional<Node> createNode(final WorkflowEntity workflowEntity, final NetworkChartContext networkChartContext)
    {
        return workflowNetworkEntitiesFactory.generateNode(workflowEntity).map(node -> {
            final ElementLocation location = initialElementLocationProvider.provideLocation(networkChartContext.getWim());
            return new Node.Builder(node).withX(location.getX()).withY(location.getY()).build();
        });
    }


    private NetworkUpdates createNetworkUpdates(final Operation operation, final Node node)
    {
        return operation == Operation.EDIT ? new NetworkUpdates(new NetworkUpdate(Action.UPDATE, node))
                        : new NetworkUpdates(new NetworkUpdate(Action.ADD, node));
    }


    private Optional<Node> tryToUpdateNode(final ElementDto elementDto, final WorkflowEntity workflowEntity)
    {
        return Optional.ofNullable(elementDto.getNode())
                        .flatMap(node -> workflowNetworkEntitiesFactory.generateNode(workflowEntity, node));
    }


    private void storeInModel(final ItemModel model, final NetworkChartContext context)
    {
        if(modelService.isNew(model))
        {
            final WidgetModel widgetModel = context.getWim().getModel();
            Optional.ofNullable(widgetModel.getValue(MODEL_NEW_WORKFLOW_ITEMS_KEY, List.class)).ifPresentOrElse(
                            list -> list.add(model), () -> widgetModel.put(MODEL_NEW_WORKFLOW_ITEMS_KEY, Lists.newArrayList(model)));
        }
    }


    @Required
    public void setModelService(final ModelService modelService)
    {
        this.modelService = modelService;
    }


    @Required
    public void setWorkflowNetworkEntitiesFactory(final WorkflowNetworkEntitiesFactory workflowNetworkEntitiesFactory)
    {
        this.workflowNetworkEntitiesFactory = workflowNetworkEntitiesFactory;
    }


    @Required
    public void setInitialElementLocationProvider(final InitialElementLocationProvider initialElementLocationProvider)
    {
        this.initialElementLocationProvider = initialElementLocationProvider;
    }
}
