package de.hybris.platform.cms2.servicelayer.services.impl;

import de.hybris.platform.cms2.model.preview.CMSPreviewTicketModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.ModelRemovalException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.task.RetryLaterException;
import de.hybris.platform.task.TaskModel;
import de.hybris.platform.task.TaskRunner;
import de.hybris.platform.task.TaskService;
import org.apache.log4j.Logger;

public class PreviewTicketRemoveTaskRunner implements TaskRunner<TaskModel>
{
    private static final Logger LOG = Logger.getLogger(PreviewTicketRemoveTaskRunner.class);
    private ModelService modelService;


    public void run(TaskService taskService, TaskModel task) throws RetryLaterException
    {
        ItemModel contextItem = task.getContextItem();
        if(contextItem instanceof CMSPreviewTicketModel)
        {
            CMSPreviewTicketModel ticket = (CMSPreviewTicketModel)contextItem;
            if(!getModelService().isRemoved(ticket) && !getModelService().isNew(ticket))
            {
                try
                {
                    getModelService().remove(ticket);
                }
                catch(ModelRemovalException e)
                {
                    LOG.error("Unable to remove preview ticket [" + ticket + "]", (Throwable)e);
                }
            }
        }
    }


    public void handleError(TaskService taskService, TaskModel task, Throwable error)
    {
    }


    public ModelService getModelService()
    {
        return this.modelService;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }
}
