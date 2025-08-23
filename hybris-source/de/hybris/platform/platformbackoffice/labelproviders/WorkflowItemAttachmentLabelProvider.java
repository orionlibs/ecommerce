package de.hybris.platform.platformbackoffice.labelproviders;

import com.hybris.cockpitng.labels.LabelProvider;
import com.hybris.cockpitng.labels.LabelService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.workflow.model.WorkflowItemAttachmentModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.zkoss.util.resource.Labels;

public class WorkflowItemAttachmentLabelProvider implements LabelProvider<WorkflowItemAttachmentModel>
{
    private static final String LABEL_KEY_NOT_SAVED = "labelprovider.notsaved";
    private ModelService modelService;
    private LabelService labelService;


    public String getLabel(WorkflowItemAttachmentModel workflowItemAttachmentModel)
    {
        StringBuilder label = new StringBuilder();
        if(StringUtils.isNotEmpty(workflowItemAttachmentModel.getName()))
        {
            label.append(workflowItemAttachmentModel.getName());
            if(workflowItemAttachmentModel.getItem() != null)
            {
                label.append(": ");
            }
        }
        if(workflowItemAttachmentModel.getItem() != null)
        {
            label.append(getLabelService().getObjectLabel(workflowItemAttachmentModel.getItem()));
        }
        if(getModelService().isNew(workflowItemAttachmentModel))
        {
            label.append(" [").append(Labels.getLabel("labelprovider.notsaved")).append("]");
        }
        return label.toString();
    }


    public String getShortLabel(WorkflowItemAttachmentModel workflowItemAttachmentModel)
    {
        if(workflowItemAttachmentModel.getItem() != null)
        {
            return getLabelService().getShortObjectLabel(workflowItemAttachmentModel.getItem());
        }
        return workflowItemAttachmentModel.getName();
    }


    public String getDescription(WorkflowItemAttachmentModel workflowItemAttachmentModel)
    {
        return null;
    }


    public String getIconPath(WorkflowItemAttachmentModel workflowItemAttachmentModel)
    {
        return null;
    }


    public void setModelService(ModelService modelService)
    {
        this.modelService = modelService;
    }


    protected ModelService getModelService()
    {
        return this.modelService;
    }


    @Required
    public void setLabelService(LabelService labelService)
    {
        this.labelService = labelService;
    }


    protected LabelService getLabelService()
    {
        return this.labelService;
    }
}
