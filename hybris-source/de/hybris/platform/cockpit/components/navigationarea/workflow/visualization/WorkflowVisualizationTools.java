package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization;

import de.hybris.platform.workflow.model.WorkflowModel;

public class WorkflowVisualizationTools
{
    public static String createWorkflowUrl(WorkflowModel workflow, EdgeStyle style, String language)
    {
        String pk = workflow.getPk().toString();
        return "/productcockpit/controllers/workflowDiagramRenderer?pk=" + pk + "&style=" + style.name() + "&scale=1.0&lang=" + language;
    }


    public static String createWorkflowUrlForRedirect(WorkflowModel workflow, EdgeStyle style, String language)
    {
        String pk = workflow.getPk().toString();
        return "/controllers/workflowVisualization?pk=" + pk + "&style=" + style.name() + "&scale=1.0&lang=" + language;
    }
}
