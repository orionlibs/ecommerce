package de.hybris.platform.warehousingbackoffice.renderers;

import com.hybris.cockpitng.core.config.impl.jaxb.listview.ListColumn;
import com.hybris.cockpitng.dataaccess.facades.type.DataType;
import com.hybris.cockpitng.engine.WidgetInstanceManager;
import com.hybris.cockpitng.widgets.common.WidgetComponentRenderer;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.workflow.WorkflowService;
import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Optional;
import javax.annotation.Resource;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Label;
import org.zkoss.zul.Listcell;

public class ConsignmentAssigneeListRenderer implements WidgetComponentRenderer<Listcell, ListColumn, Object>
{
    protected static final String SCLASS_YW_LISTVIEW_CELL_LABEL = "yw-listview-cell-label";
    @Resource
    protected WorkflowService newestWorkflowService;


    public void render(Listcell listcell, ListColumn configuration, Object object, DataType dataType, WidgetInstanceManager widgetInstanceManager)
    {
        if(object instanceof ConsignmentModel && ((ConsignmentModel)object).getTaskAssignmentWorkflow() != null)
        {
            WorkflowModel taskAssignmentWorkflow = getNewestWorkflowService().getWorkflowForCode(((ConsignmentModel)object).getTaskAssignmentWorkflow());
            Optional<WorkflowActionModel> taskInProgress = taskAssignmentWorkflow.getActions().stream().filter(action -> action.getStatus().equals(WorkflowActionStatus.IN_PROGRESS)).findAny();
            if(taskInProgress.isPresent())
            {
                Label assigneeLabel = new Label(((WorkflowActionModel)taskInProgress.get()).getPrincipalAssigned().getDisplayName());
                assigneeLabel.setSclass("yw-listview-cell-label");
                listcell.appendChild((Component)assigneeLabel);
            }
        }
    }


    protected WorkflowService getNewestWorkflowService()
    {
        return this.newestWorkflowService;
    }
}
