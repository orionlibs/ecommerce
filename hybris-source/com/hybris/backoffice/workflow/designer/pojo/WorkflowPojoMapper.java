/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.backoffice.workflow.designer.pojo;

import com.hybris.backoffice.workflow.designer.dto.ActionDto;
import com.hybris.backoffice.workflow.designer.dto.AndDto;
import com.hybris.backoffice.workflow.designer.dto.DecisionDto;
import com.hybris.backoffice.workflow.designer.dto.ElementDto;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.link.LinkModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowActionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionTemplateModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import de.hybris.platform.workflow.model.WorkflowTemplateModel;
import java.util.Optional;

/**
 * Utility class which allows to map workflow models to workflow POJOs
 */
public class WorkflowPojoMapper
{
    private WorkflowPojoMapper()
    {
        throw new AssertionError("Utilities class should not be instantiated");
    }


    /**
     * Maps {@link ItemModel} to {@link Workflow}
     *
     * @param itemModel
     *           model to map
     * @return {@link Optional} with instance of {@link Workflow}. If {@link ItemModel} is neither instance of
     *         {@link WorkflowTemplateModel} nor {@link WorkflowModel} then empty {@link Optional} is returned
     */
    public static Optional<Workflow> mapItemToWorkflow(final ItemModel itemModel)
    {
        return Optional.ofNullable(internalMapItemToWorkflow(itemModel));
    }


    private static Workflow internalMapItemToWorkflow(final ItemModel itemModel)
    {
        if(itemModel instanceof WorkflowTemplateModel)
        {
            return new WorkflowTemplate((WorkflowTemplateModel)itemModel);
        }
        if(itemModel instanceof WorkflowModel)
        {
            return new WorkflowInstance((WorkflowModel)itemModel);
        }
        return null;
    }


    /**
     * Maps {@link ItemModel} to {@link WorkflowEntity}. {@link ItemModel} has to be persisted in the database.
     *
     * @param itemModel
     *           model to map
     * @return {@link Optional} with instance of {@link Workflow}. If {@link ItemModel} has no corresponding
     *         {@link WorkflowEntity} then empty {@link Optional} is returned
     */
    public static Optional<WorkflowEntity> mapItemToWorkflowEntity(final ItemModel itemModel)
    {
        return Optional.ofNullable(internalMapItemToWorkflowEntity(itemModel));
    }


    private static WorkflowEntity internalMapItemToWorkflowEntity(final ItemModel itemModel)
    {
        if(itemModel instanceof WorkflowDecisionTemplateModel)
        {
            return new WorkflowDecisionTemplate((WorkflowDecisionTemplateModel)itemModel);
        }
        if(itemModel instanceof WorkflowDecisionModel)
        {
            return new WorkflowDecisionInstance((WorkflowDecisionModel)itemModel);
        }
        if(itemModel instanceof WorkflowActionTemplateModel)
        {
            return new WorkflowActionTemplate((WorkflowActionTemplateModel)itemModel);
        }
        if(itemModel instanceof WorkflowActionModel)
        {
            return new WorkflowActionInstance((WorkflowActionModel)itemModel);
        }
        if(itemModel instanceof LinkModel)
        {
            return WorkflowLink.ofSavedModel((LinkModel)itemModel);
        }
        return null;
    }


    /**
     * Maps {@link ElementDto<ItemModel>} to {@link WorkflowEntity}
     *
     * @param elementDto
     *           model to map
     * @return {@link Optional} with instance of {@link Workflow}. If {@link ElementDto} has no corresponding
     *         {@link WorkflowEntity} then empty {@link Optional} is returned
     */
    public static Optional<WorkflowEntity> mapDtoToWorkflowEntity(final ElementDto<ItemModel> elementDto)
    {
        return Optional.ofNullable(internalMapDtoToWorkflowEntity(elementDto));
    }


    private static WorkflowEntity internalMapDtoToWorkflowEntity(final ElementDto<? extends ItemModel> elementDto)
    {
        if(elementDto instanceof DecisionDto)
        {
            return new WorkflowDecisionTemplate((WorkflowDecisionTemplateModel)elementDto.getModel());
        }
        if(elementDto instanceof ActionDto)
        {
            return new WorkflowActionTemplate((WorkflowActionTemplateModel)elementDto.getModel());
        }
        if(elementDto instanceof AndDto)
        {
            return WorkflowLink.ofUnsavedModel((LinkModel)elementDto.getModel(), true);
        }
        return null;
    }
}
