package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.model.listview.ValueHandler;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.meta.TypeService;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.Registry;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import de.hybris.platform.workflow.model.WorkflowModel;

public class WorkflowImgValueHandler implements ValueHandler
{
    private final TypeService typeService = UISessionUtils.getCurrentSession().getTypeService();
    private WorkflowService workflowService;
    public static final String COLUMN_STATUS = "Status";
    public static final String COLUMN_THUMBNAIL = "Thumbnail";
    private final String qualifier;


    public WorkflowImgValueHandler(String qualifier)
    {
        this.qualifier = qualifier;
    }


    public Object getValue(TypedObject item) throws ValueHandlerException
    {
        Object obj = item.getObject();
        if(obj instanceof WorkflowItemAttachmentModel)
        {
            if("Thumbnail".equalsIgnoreCase(this.qualifier))
            {
                ItemModel itemModel = ((WorkflowItemAttachmentModel)obj).getItem();
                if(itemModel instanceof ProductModel)
                {
                    return this.typeService.wrapItem(((ProductModel)itemModel).getThumbnail());
                }
            }
            else if("Status".equalsIgnoreCase(this.qualifier))
            {
                String status = null;
                WorkflowModel workflowModel = ((WorkflowItemAttachmentModel)obj).getWorkflow();
                if(getWorkflowService().isPlanned(workflowModel))
                {
                    status = "/cockpit/images/not_started_workflow.png";
                }
                else if(getWorkflowService().isRunning(workflowModel))
                {
                    status = "/cockpit/images/running_workflow.png";
                }
                else if(getWorkflowService().isFinished(workflowModel))
                {
                    status = "/cockpit/images/finished_workflow.png";
                }
                else if(getWorkflowService().isTerminated(workflowModel))
                {
                    status = "/cockpit/images/terminated_workflow.png";
                }
                return (status == null) ? "(none)" : status;
            }
        }
        return "";
    }


    public Object getValue(TypedObject item, String languageIso) throws ValueHandlerException
    {
        return getValue(item);
    }


    public void setValue(TypedObject item, Object value) throws ValueHandlerException
    {
    }


    public void setValue(TypedObject item, Object value, String languageIso) throws ValueHandlerException
    {
    }


    private WorkflowService getWorkflowService()
    {
        if(this.workflowService == null)
        {
            this.workflowService = (WorkflowService)Registry.getApplicationContext().getBean("newestWorkflowService");
        }
        return this.workflowService;
    }
}
