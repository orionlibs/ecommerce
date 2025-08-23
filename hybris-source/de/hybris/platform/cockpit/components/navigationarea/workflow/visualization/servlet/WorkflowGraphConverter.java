package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.servlet;

import de.hybris.platform.workflow.enums.WorkflowActionStatus;
import de.hybris.platform.workflow.enums.WorkflowActionType;
import de.hybris.platform.workflow.model.WorkflowActionModel;
import de.hybris.platform.workflow.model.WorkflowDecisionModel;
import de.hybris.platform.workflow.model.WorkflowModel;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;

public class WorkflowGraphConverter implements GraphConverter
{
    public boolean canConvert(Object element)
    {
        return element instanceof WorkflowModel;
    }


    public GraphRepresentation convert(Object element)
    {
        if(!canConvert(element))
        {
            throw new IllegalArgumentException("Only objects of type " + WorkflowModel.class.getCanonicalName() + " may be converted.");
        }
        WorkflowModel workflow = (WorkflowModel)element;
        GraphRepresentation graph = GraphRepresentation.getInstance(true, true);
        for(WorkflowActionModel action : workflow.getActions())
        {
            Vertex vertex = new Vertex(action.getPk().toString(), action.getName(), (action.getActionType() == WorkflowActionType.START), (action.getActionType() == WorkflowActionType.END));
            if(action.getStatus() == WorkflowActionStatus.COMPLETED || action
                            .getStatus() == WorkflowActionStatus.ENDED_THROUGH_END_OF_WORKFLOW)
            {
                vertex.setColor(VertexColor.GREEN);
            }
            else if(action.getStatus() == WorkflowActionStatus.IN_PROGRESS)
            {
                vertex.setColor(VertexColor.ORANGE);
            }
            else if(action.getStatus() == WorkflowActionStatus.DISABLED || action.getStatus() == WorkflowActionStatus.TERMINATED)
            {
                vertex.setColor(VertexColor.RED);
            }
            graph.addVertex(vertex);
        }
        for(WorkflowActionModel from : workflow.getActions())
        {
            Collection<WorkflowDecisionModel> decisions = from.getDecisions();
            for(WorkflowDecisionModel decision : decisions)
            {
                for(WorkflowActionModel to : decision.getToActions())
                {
                    String label = StringUtils.isEmpty(decision.getName()) ? String.format("[%s]", new Object[] {decision.getCode()}) : decision.getName();
                    graph.addEdge(new Edge(graph.getVertex(from.getPk().toString()), graph.getVertex(to.getPk().toString()), label));
                }
            }
        }
        return graph;
    }
}
