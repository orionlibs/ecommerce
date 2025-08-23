package de.hybris.platform.cockpit.reports.widgets.factory;

import de.hybris.platform.cockpit.model.WidgetParameterModel;
import net.sf.jasperreports.engine.JRParameter;

public interface JasperReportParameterFactory
{
    WidgetParameterModel createParameter(JRParameter paramJRParameter);


    boolean isSameMetaData(JRParameter paramJRParameter, WidgetParameterModel paramWidgetParameterModel);
}
