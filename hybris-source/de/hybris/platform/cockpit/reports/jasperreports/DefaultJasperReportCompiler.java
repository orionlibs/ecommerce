package de.hybris.platform.cockpit.reports.jasperreports;

import de.hybris.platform.cockpit.model.WidgetParameterModel;
import de.hybris.platform.cockpit.reports.model.JasperWidgetPreferencesModel;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.media.MediaModel;
import de.hybris.platform.jalo.security.CannotDecodePasswordException;
import de.hybris.platform.servicelayer.i18n.I18NService;
import de.hybris.platform.servicelayer.media.MediaService;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessResourceFailureException;

@Deprecated
public class DefaultJasperReportCompiler implements JasperReportCompiler
{
    private static final String REPORT_TITLE_PARAMETER = "REPORT_TITLE";
    private UserService userService;
    private MediaService mediaService;
    private I18NService i18NService;
    private TenantService tenantService;


    public synchronized JasperReport compileReport(JasperWidgetPreferencesModel configuration)
    {
        try
        {
            return compile(configuration);
        }
        catch(JRException ex)
        {
            throw new RuntimeException(ex);
        }
    }


    private JasperReport compile(JasperWidgetPreferencesModel configuration) throws JRException
    {
        InputStream reportInputStream = this.mediaService.getDataStreamFromMedia((MediaModel)configuration.getReport());
        JasperDesign jDesign = JRXmlLoader.load(reportInputStream);
        JasperReport compiledReport = JasperCompileManager.compileReport(jDesign);
        return compiledReport;
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


    protected Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException
    {
        String portProperty = "tomcat.ssl.port";
        if(Config.getParameter("ibm.websphere.internalClassAccessMode") != null)
        {
            portProperty = "cockpit.reports.vjdbc.port";
        }
        String vjdbcId = Config.getParameter("virtualjdbc.id");
        int serverPort = Config.getInt(portProperty, 9002);
        String vjdbcHost = Config.getString("cockpit.reports.vjdbc.host", "localhost");
        Class.forName("de.hybris.vjdbc.VirtualDriver").newInstance();
        String url = "https://" + vjdbcHost + ":" + serverPort + "/virtualjdbc/service?tenant=" + this.tenantService.getCurrentTenantId();
        String connectionUrl = null;
        String username = null;
        try
        {
            username = getUsername();
            String password = getPassword(username);
            connectionUrl = "jdbc:hybris:flexiblesearch:" + url + "," + vjdbcId;
            return DriverManager.getConnection(connectionUrl, username, password);
        }
        catch(SQLException ex)
        {
            throw new DataAccessResourceFailureException(
                            String.format("Unable to connect to: %s. Is host and port correctly set ? Please check if user %s has permissions to connect to VJDBC.", new Object[] {connectionUrl, username}), ex);
        }
        catch(CannotDecodePasswordException ex)
        {
            throw new DataAccessResourceFailureException(
                            String.format("Unable to decode password for user: %s. Please specify properties: 'cockpit.reports.vjdbc.username' and 'cockpit.reports.vjdbc.password'.", new Object[] {username}), ex);
        }
    }


    private String getPassword(String username)
    {
        String usernameFromProperties = Config.getParameter("cockpit.reports.vjdbc.password");
        return (usernameFromProperties != null) ? usernameFromProperties : this.userService.getPassword(username);
    }


    protected String getUsername()
    {
        String passwordFromProperties = Config.getParameter("cockpit.reports.vjdbc.username");
        return (passwordFromProperties != null) ? passwordFromProperties : UISessionUtils.getCurrentSession().getUser().getUid();
    }


    private Object getParameterValue(WidgetParameterModel parameter)
    {
        Object value = null;
        if(StringUtils.equals(parameter.getType().getCode(), parameter.getTargetType()))
        {
            value = parameter.getValue();
        }
        else if(StringUtils.equals("java.lang.Long", parameter.getTargetType()))
        {
            Object storedValue = parameter.getValue();
            if(storedValue instanceof ItemModel)
            {
                ItemModel item = (ItemModel)parameter.getValue();
                value = Long.valueOf(item.getPk().getLongValue());
            }
        }
        else if(StringUtils.equals("java.util.Collection", parameter.getTargetType()))
        {
            Object storedValue = parameter.getValue();
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
            Object storedValue = parameter.getValue();
            if(storedValue != null)
            {
                value = storedValue.toString();
            }
        }
        return value;
    }


    public JasperPrint fillReport(JasperReport report, JasperWidgetPreferencesModel configuration)
    {
        Map parameters = createReportParametersMap(configuration);
        Connection conn = null;
        try
        {
            conn = getConnection();
            return JasperFillManager.fillReport(report, parameters, conn);
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            if(conn != null)
            {
                try
                {
                    conn.close();
                }
                catch(SQLException e)
                {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public void setMediaService(MediaService mediaService)
    {
        this.mediaService = mediaService;
    }


    public void setI18NService(I18NService i18nService)
    {
        this.i18NService = i18nService;
    }


    public void setTenantService(TenantService tenantService)
    {
        this.tenantService = tenantService;
    }
}
