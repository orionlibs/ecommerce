package de.hybris.platform.cockpit.reports.jasperreports;

import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import java.io.IOException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import org.zkoss.image.AImage;

@Deprecated
public interface JasperReportsCache
{
    @Deprecated
    void remove(WidgetPreferencesModel paramWidgetPreferencesModel);


    @Deprecated
    AImage getFilled(JasperWidgetPreferencesModel paramJasperWidgetPreferencesModel) throws JRException, IOException;


    @Deprecated
    void update(WidgetPreferencesModel paramWidgetPreferencesModel);


    @Deprecated
    void invalidateAll();


    @Deprecated
    JasperReport getCompiledReport(JasperWidgetPreferencesModel paramJasperWidgetPreferencesModel);
}
