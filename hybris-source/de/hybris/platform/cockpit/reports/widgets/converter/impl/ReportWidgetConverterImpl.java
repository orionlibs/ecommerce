package de.hybris.platform.cockpit.reports.widgets.converter.impl;

import de.hybris.platform.cockpit.model.WidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.jasperreports.JasperReportExporter;
import de.hybris.platform.cockpit.reports.jasperreports.JasperReportsCache;
import de.hybris.platform.cockpit.reports.jasperreports.JasperReportsRefresh;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.cockpit.reports.widgets.converter.ReportWidgetConverter;
import de.hybris.platform.cockpit.reports.widgets.factory.ReportWidgetFactory;
import de.hybris.platform.cockpit.widgets.Widget;
import de.hybris.platform.cockpit.widgets.WidgetConfig;
import de.hybris.platform.cockpit.widgets.controllers.WidgetController;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidget;
import de.hybris.platform.cockpit.widgets.impl.DefaultWidgetConfig;
import de.hybris.platform.cockpit.widgets.models.WidgetModel;
import de.hybris.platform.cockpit.widgets.renderers.WidgetRenderer;
import org.zkoss.util.resource.Labels;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zul.Separator;
import org.zkoss.zul.Toolbarbutton;

public class ReportWidgetConverterImpl implements ReportWidgetConverter
{
    private JasperReportExporter jasperReportPdfExporter;
    private JasperReportExporter jasperReportXlsExporter;
    private JasperReportsCache jasperReportsCache;
    private JasperReportsRefresh jasperReportsRefresh;
    private ReportWidgetFactory reportWidgetFactory;
    private String widgetSClass;
    private boolean widgetLazyLoadingEnabled = false;
    private boolean widgetFocusable = false;


    public WidgetConfig<Widget<WidgetModel, WidgetController>> createWidgetConfig(WidgetPreferencesModel widgetPreferences)
    {
        DefaultWidgetConfig defaultWidgetConfig = new DefaultWidgetConfig();
        defaultWidgetConfig.setWidgetRenderer(createWidgetRenderer(widgetPreferences));
        Class<DefaultWidget> widgetClass = DefaultWidget.class;
        defaultWidgetConfig.setWidgetClass(widgetClass);
        defaultWidgetConfig.setWidgetTitle(widgetPreferences.getTitle());
        defaultWidgetConfig.setWidgetSclass(this.widgetSClass);
        defaultWidgetConfig.setLazyLoadingEnabled(this.widgetLazyLoadingEnabled);
        defaultWidgetConfig.setFocusable(this.widgetFocusable);
        return (WidgetConfig<Widget<WidgetModel, WidgetController>>)defaultWidgetConfig;
    }


    protected WidgetRenderer<Widget> createWidgetRenderer(WidgetPreferencesModel widgetPreferences)
    {
        return (WidgetRenderer<Widget>)new Object(this, widgetPreferences);
    }


    private void addVerticalSeparator(Component cmp)
    {
        Separator sep = new Separator("vertical");
        sep.setBar(true);
        sep.setWidth("1px");
        cmp.appendChild((Component)sep);
    }


    private void addHeaderButtons(Component parent, WidgetPreferencesModel widgetPreferences)
    {
        if(widgetPreferences instanceof JasperWidgetPreferencesModel)
        {
            Toolbarbutton exportButtonPdf = new Toolbarbutton("", "cockpit/images/pdfexport_action.png");
            exportButtonPdf.setTooltiptext(Labels.getLabel("cmscockpit.action.exportAsPdf"));
            exportButtonPdf.setParent(parent);
            exportButtonPdf.addEventListener("onClick", (EventListener)new Object(this, widgetPreferences));
            addVerticalSeparator(parent);
            Toolbarbutton exportButtonXls = new Toolbarbutton("", "cockpit/images/xlsexport_action.png");
            exportButtonXls.setTooltiptext(Labels.getLabel("cmscockpit.action.exportAsXls"));
            exportButtonXls.setParent(parent);
            exportButtonXls.addEventListener("onClick", (EventListener)new Object(this, widgetPreferences));
            addVerticalSeparator(parent);
            Toolbarbutton reloadButton = new Toolbarbutton("", "cockpit/images/icon_func_refresh_available.png");
            reloadButton.setTooltiptext(Labels.getLabel("cmscockpit.action.refresh"));
            reloadButton.setParent(parent);
            reloadButton.addEventListener("onClick", (EventListener)new Object(this, widgetPreferences));
            addVerticalSeparator(parent);
        }
        Toolbarbutton editButton = new Toolbarbutton("", "cockpit/images/cnt_elem_edit_action.png");
        editButton.setTooltiptext(Labels.getLabel("cmscockpit.action.edit"));
        editButton.setParent(parent);
        editButton.addEventListener("onClick", (EventListener)new Object(this, widgetPreferences));
        if(!widgetPreferences.getClass().equals(JasperWidgetPreferencesModel.class))
        {
            return;
        }
        addVerticalSeparator(parent);
        Toolbarbutton removeButton = new Toolbarbutton("", "cockpit/images/cnt_elem_remove_action.png");
        removeButton.setParent(parent);
        removeButton.addEventListener("onClick", (EventListener)new Object(this, widgetPreferences));
    }


    public void setJasperReportPdfExporter(JasperReportExporter jasperReportPdfExporter)
    {
        this.jasperReportPdfExporter = jasperReportPdfExporter;
    }


    public void setJasperReportsCache(JasperReportsCache jasperReportsCache)
    {
        this.jasperReportsCache = jasperReportsCache;
    }


    public void setJasperReportsRefresh(JasperReportsRefresh jasperReportsRefresh)
    {
        this.jasperReportsRefresh = jasperReportsRefresh;
    }


    public void setJasperReportXlsExporter(JasperReportExporter jasperReportXlsExporter)
    {
        this.jasperReportXlsExporter = jasperReportXlsExporter;
    }


    public void setReportWidgetFactory(ReportWidgetFactory reportWidgetFactory)
    {
        this.reportWidgetFactory = reportWidgetFactory;
    }


    @Deprecated
    public ReportWidgetFactory getReportWidgetFactory()
    {
        return this.reportWidgetFactory;
    }


    public void setWidgetLazyLoadingEnabled(boolean widgetLazyLoadingEnabled)
    {
        this.widgetLazyLoadingEnabled = widgetLazyLoadingEnabled;
    }


    public boolean isWidgetLazyLoadingEnabled()
    {
        return this.widgetLazyLoadingEnabled;
    }


    public void setWidgetFocusable(boolean widgetFocusable)
    {
        this.widgetFocusable = widgetFocusable;
    }


    public boolean isWidgetFocusable()
    {
        return this.widgetFocusable;
    }
}
