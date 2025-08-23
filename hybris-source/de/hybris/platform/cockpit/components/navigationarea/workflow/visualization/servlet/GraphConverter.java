package de.hybris.platform.cockpit.components.navigationarea.workflow.visualization.servlet;

public interface GraphConverter
{
    boolean canConvert(Object paramObject);


    GraphRepresentation convert(Object paramObject);
}
