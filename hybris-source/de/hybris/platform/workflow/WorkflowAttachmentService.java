package de.hybris.platform.workflow;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.List;

public interface WorkflowAttachmentService
{
    List<WorkflowItemAttachmentModel> addItems(WorkflowModel paramWorkflowModel, List<? extends ItemModel> paramList);


    default void removeItems(WorkflowModel workflow, List<WorkflowItemAttachmentModel> itemsToRemove)
    {
        throw new UnsupportedOperationException("This method was not implemented...");
    }


    ItemModel containsItem(WorkflowModel paramWorkflowModel, List<? extends ItemModel> paramList);


    List<ItemModel> getAttachmentsForAction(WorkflowActionModel paramWorkflowActionModel);


    List<ItemModel> getAttachmentsForAction(WorkflowActionModel paramWorkflowActionModel, String paramString);


    List<ItemModel> getAttachmentsForAction(WorkflowActionModel paramWorkflowActionModel, List<String> paramList);
}
