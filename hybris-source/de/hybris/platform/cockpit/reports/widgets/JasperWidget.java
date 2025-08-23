package de.hybris.platform.cockpit.reports.widgets;

import de.hybris.platform.cockpit.enums.RefreshTimeOption;
import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.exceptions.JasperWidgetException;
import de.hybris.platform.cockpit.reports.jasperreports.JasperReportsCache;
import de.hybris.platform.cockpit.reports.jasperreports.JasperReportsRefresh;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import org.zkoss.image.AImage;
import org.zkoss.zk.ui.Component;
import org.zkoss.zul.Image;
import org.zkoss.zul.api.Div;

public class JasperWidget implements ReportWidget<JasperWidgetPreferencesModel>
{
    private JasperReportsCache jasperReportCache;
    private JasperReportsRefresh jasperReportsRefresh;


    public void initialize(JasperWidgetPreferencesModel configuration, Div view)
    {
        try
        {
            AImage image = this.jasperReportCache.getFilled(configuration);
            addImageToView(view, image);
            initializeRefresh(configuration);
            view.setSclass("reportWidgetContent");
        }
        catch(Exception e)
        {
            throw new JasperWidgetException(e);
        }
    }


    private void addImageToView(Div view, AImage aImage)
    {
        Image image = new Image();
        image.setContent((Image)aImage);
        image.setParent((Component)view);
    }


    private void initializeRefresh(JasperWidgetPreferencesModel jasperPrefModel)
    {
        for(WidgetParameterModel parameter : jasperPrefModel.getParameters())
        {
            if(parameter.getName().equals("Refresh"))
            {
                RefreshTimeOption refresh = (RefreshTimeOption)parameter.getValue();
                this.jasperReportsRefresh.startRefreshing((WidgetPreferencesModel)jasperPrefModel, refresh);
            }
        }
    }


    public void setJasperReportCache(JasperReportsCache jasperReportCache)
    {
        this.jasperReportCache = jasperReportCache;
    }


    public void setJasperReportsRefresh(JasperReportsRefresh jasperReportsRefresh)
    {
        this.jasperReportsRefresh = jasperReportsRefresh;
    }
}
