package de.hybris.platform.cockpit.model.listview.impl;

import de.hybris.platform.cockpit.helpers.WorkflowHelper;
import de.hybris.platform.cockpit.model.listview.CellRenderer;
import de.hybris.platform.cockpit.model.listview.TableModel;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.workflow.constants.GeneratedWorkflowConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.spring.SpringUtil;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;

public class WorkflowStatusImageCellRenderer implements CellRenderer
{
    private static final Logger LOG = LoggerFactory.getLogger(WorkflowStatusImageCellRenderer.class);
    private WorkflowHelper workflowHelper;


    public void render(TableModel model, int colIndex, int rowIndex, Component parent)
    {
        if(model == null || parent == null)
        {
            throw new IllegalArgumentException("Model and parent can not be null");
        }
        try
        {
            Object value = model.getValueAt(colIndex, rowIndex);
            Div div = new Div();
            div.setParent(parent);
            div.setStyle("overflow: hidden;height:100%;text-align:center;");
            String imgSrc = null;
            if(value instanceof TypedObject && ((TypedObject)value).getType().getCode().equals(GeneratedWorkflowConstants.TC.WORKFLOW))
            {
                imgSrc = getWorkflowHelper().getStatusUrl((TypedObject)value);
            }
            else if(value instanceof String)
            {
                imgSrc = (String)value;
            }
            else if(value != null)
            {
                LOG.warn("Could not render cell. Reason: Value not a TypedObject.");
            }
            Image img = new Image(imgSrc);
            img.setParent((Component)div);
            img.setSclass("listViewCellImage");
        }
        catch(IllegalArgumentException iae)
        {
            LOG.warn("Could not render cell using " + WorkflowStatusImageCellRenderer.class.getSimpleName() + ".", iae);
            return;
        }
    }


    private WorkflowHelper getWorkflowHelper()
    {
        if(this.workflowHelper == null)
        {
            this.workflowHelper = (WorkflowHelper)SpringUtil.getBean("workflowHelper");
        }
        return this.workflowHelper;
    }
}
