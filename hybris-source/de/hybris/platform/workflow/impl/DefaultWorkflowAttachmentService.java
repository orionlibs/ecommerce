package de.hybris.platform.workflow.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.workflow.WorkflowAttachmentService;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.collections4.ListUtils;
import org.apache.log4j.Logger;

public class DefaultWorkflowAttachmentService implements WorkflowAttachmentService
{
    private static final Logger LOG = Logger.getLogger(DefaultWorkflowAttachmentService.class);
    private ModelService modelService;
    private TypeService typeService;


    public List<WorkflowItemAttachmentModel> addItems(WorkflowModel workflowModel, List<? extends ItemModel> itemsToAdd)
    {
        List<WorkflowItemAttachmentModel> workflowItemAttachments = new ArrayList<>();
        List<WorkflowItemAttachmentModel> existingWorkflowAttachments = workflowModel.getAttachments();
        workflowItemAttachments.addAll(existingWorkflowAttachments);
        for(int i = 0; i < itemsToAdd.size(); i++)
        {
            WorkflowItemAttachmentModel att = (WorkflowItemAttachmentModel)this.modelService.create(WorkflowItemAttachmentModel.class);
            att.setItem(itemsToAdd.get(i));
            att.setWorkflow(workflowModel);
            att.setCode("toCheck");
            workflowItemAttachments.add(att);
        }
        workflowModel.setAttachments(workflowItemAttachments);
        Set<ItemModel> modelsToSave = new LinkedHashSet<>();
        for(WorkflowActionModel action : workflowModel.getActions())
        {
            List<WorkflowItemAttachmentModel> actionAttachments = new ArrayList<>();
            List<WorkflowItemAttachmentModel> existingActionAttachments = action.getAttachments();
            actionAttachments.addAll(existingActionAttachments);
            for(WorkflowItemAttachmentModel newAtt : workflowItemAttachments)
            {
                boolean itemExist = false;
                for(WorkflowItemAttachmentModel existingAtt : actionAttachments)
                {
                    if(newAtt.getPk() != null && newAtt.getPk().compareTo(existingAtt.getPk()) == 0)
                    {
                        itemExist = true;
                        break;
                    }
                }
                if(!itemExist)
                {
                    actionAttachments.add(newAtt);
                }
            }
            action.setAttachments(actionAttachments);
            modelsToSave.add(action);
        }
        modelsToSave.add(workflowModel);
        this.modelService.saveAll(modelsToSave);
        return workflowItemAttachments;
    }


    public void removeItems(WorkflowModel workflowModel, List<WorkflowItemAttachmentModel> itemsToRemove)
    {
        List<WorkflowItemAttachmentModel> attachmentsToUpdate = ListUtils.subtract(workflowModel.getAttachments(), itemsToRemove);
        if(attachmentsToUpdate.size() == workflowModel.getAttachments().size())
        {
            return;
        }
        workflowModel.setAttachments(attachmentsToUpdate);
        this.modelService.save(workflowModel);
        this.modelService.removeAll(itemsToRemove);
    }


    public ItemModel containsItem(WorkflowModel workflowModel, List<? extends ItemModel> itemsToAdd)
    {
        List<WorkflowItemAttachmentModel> attachments = workflowModel.getAttachments();
        for(WorkflowItemAttachmentModel workflowItemAttachmentModel : attachments)
        {
            ItemModel itemModel = workflowItemAttachmentModel.getItem();
            if(itemsToAdd.contains(itemModel))
            {
                return itemModel;
            }
        }
        return null;
    }


    public List<ItemModel> getAttachmentsForAction(WorkflowActionModel action)
    {
        List<ItemModel> ret = new ArrayList<>();
        for(ItemModel attachment : action.getAttachmentItems())
        {
            if(this.typeService.isAssignableFrom(attachment.getItemtype(), "Product"))
            {
                ret.add(attachment);
            }
        }
        return ret;
    }


    public List<ItemModel> getAttachmentsForAction(WorkflowActionModel action, String attachmentClassName)
    {
        return getAttachmentsForAction(action, Collections.singletonList(attachmentClassName));
    }


    public List<ItemModel> getAttachmentsForAction(WorkflowActionModel action, List<String> attachmentClassNames)
    {
        List<ItemModel> ret = new ArrayList<>();
        for(String attachmentClassName : attachmentClassNames)
        {
            try
            {
                Class<?> typeClass = Class.forName(attachmentClassName);
                ComposedTypeModel type = this.typeService.getComposedTypeForClass(typeClass);
                List<ItemModel> attachments = action.getAttachmentItems();
                if(attachments != null)
                {
                    for(ItemModel attachment : attachments)
                    {
                        if(attachment != null && this.typeService.isAssignableFrom(type.getCode(), attachment.getItemtype()))
                        {
                            ret.add(attachment);
                        }
                    }
                }
            }
            catch(ClassNotFoundException e)
            {
                LOG.error("The class " + attachmentClassName + " could not be found. Please check if the class name is the fully qualified name for the class.");
            }
            catch(UnknownIdentifierException e)
            {
                LOG.error("The class " + attachmentClassName + " could not be found. Please check if the class name is the fully qualified name for the class.");
            }
        }
        return ret;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    public void setTypeService(TypeService typeService)
    {
        this.typeService = typeService;
    }
}
