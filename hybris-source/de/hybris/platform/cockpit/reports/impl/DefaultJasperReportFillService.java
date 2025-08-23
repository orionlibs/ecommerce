package de.hybris.platform.cockpit.reports.impl;

import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.reports.JasperReportFillService;
import de.hybris.platform.cockpit.reports.exceptions.JasperReportFillException;
import de.hybris.platform.cockpit.reports.factories.JasperReportConnectionFactory;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.support.JdbcUtils;

public class DefaultJasperReportFillService implements JasperReportFillService
{
    private static final String REPORT_TITLE_PARAMETER = "REPORT_TITLE";
    private I18NService i18NService;
    private JasperReportConnectionFactory jasperReportConnectionFactory;


    public JasperPrint fillReport(JasperReport report, JasperWidgetPreferencesModel configuration)
    {
        Map parameters = createReportParametersMap(configuration);
        Connection conn = null;
        try
        {
            conn = this.jasperReportConnectionFactory.createConnection();
            return JasperFillManager.fillReport(report, parameters, conn);
        }
        catch(Exception e)
        {
            throw new JasperReportFillException(e);
        }
        finally
        {
            JdbcUtils.closeConnection(conn);
        }
    }


    private Map createReportParametersMap(JasperWidgetPreferencesModel preferences)
    {
        Map<Object, Object> parameters = new HashMap<>();
        parameters.put("REPORT_TITLE", preferences.getTitle());
        for(WidgetParameterModel parameter : preferences.getParameters())
        {
            parameters.put(parameter.getName(), getParameterValue(parameter));
        }
        parameters.put("REPORT_LOCALE", this.i18NService.getCurrentLocale());
        return parameters;
    }


    private Object getParameterValue(WidgetParameterModel parameter)
    {
        Object value = null;
        if(StringUtils.equals(parameter.getType().getCode(), parameter.getTargetType()))
        {
            value = parameter.getValue();
        }
        else
        {
            Object storedValue = parameter.getValue();
            if(StringUtils.equals("java.lang.Long", parameter.getTargetType()))
            {
                if(storedValue instanceof ItemModel)
                {
                    ItemModel item = (ItemModel)parameter.getValue();
                    value = Long.valueOf(item.getPk().getLongValue());
                }
            }
            else if(StringUtils.equals("java.util.Collection", parameter.getTargetType()))
            {
                value = new ArrayList();
                if(storedValue != null)
                {
                    for(ItemModel item : storedValue)
                    {
                        ((Collection<Long>)value).add(Long.valueOf(item.getPk().getLongValue()));
                    }
                }
            }
            else if(StringUtils.equals("java.lang.String", parameter.getTargetType()))
            {
                if(storedValue != null)
                {
                    value = storedValue.toString();
                }
            }
        }
        return value;
    }


    public void setI18NService(I18NService i18nService)
    {
        this.i18NService = i18nService;
    }


    public void setJasperReportConnectionFactory(JasperReportConnectionFactory jasperReportConnectionFactory)
    {
        this.jasperReportConnectionFactory = jasperReportConnectionFactory;
    }
}
