package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.servlet;

import de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.EdgeStyle;

public interface VisualizationService
{
    String prepareVisualization(Object paramObject, EdgeStyle paramEdgeStyle, double paramDouble, String paramString) throws Exception;
}
