package de.hybris.platform.cockpit.reports.factories.impl;

import de.hybris.platform.cockpit.reports.factories.JasperReportConnectionFactory;
import de.hybris.platform.cockpit.session.UISessionUtils;
import de.hybris.platform.jalo.security.CannotDecodePasswordException;
import de.hybris.platform.servicelayer.tenant.TenantService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.springframework.dao.DataAccessResourceFailureException;

public class DefaultJasperReportConnectionFactory implements JasperReportConnectionFactory
{
    private UserService userService;
    private TenantService tenantService;


    public Connection createConnection()
    {
        String username = null;
        try
        {
            username = getUsername();
            String password = getPassword(username);
            String connectionUrl = getConnectionUrl();
            return createConnection(connectionUrl, username, password);
        }
        catch(CannotDecodePasswordException ex)
        {
            throw new DataAccessResourceFailureException(
                            String.format("Unable to decode password for user: %s. Please specify properties: 'cockpit.reports.vjdbc.username' and 'cockpit.reports.vjdbc.password'.", new Object[] {username}), ex);
        }
    }


    public Connection createConnection(String user, String password)
    {
        String connectionUrl = getConnectionUrl();
        return createConnection(connectionUrl, user, password);
    }


    public Connection createConnection(String url, String user, String password)
    {
        try
        {
            Class.forName("de.hybris.vjdbc.VirtualDriver").newInstance();
            return DriverManager.getConnection(url, user, password);
        }
        catch(SQLException ex)
        {
            throw new DataAccessResourceFailureException(
                            String.format("Unable to connect to: %s. Is host and port correctly set ? Please check if user %s has permissions to connect to VJDBC.", new Object[] {url, user}), ex);
        }
        catch(Exception ex)
        {
            throw new DataAccessResourceFailureException(String.format("Cannot load driver: %s", new Object[] {"de.hybris.vjdbc.VirtualDriver"}), ex);
        }
    }


    protected String getPassword(String username)
    {
        String usernameFromProperties = Config.getParameter("cockpit.reports.vjdbc.password");
        return (usernameFromProperties != null) ? usernameFromProperties : this.userService.getPassword(username);
    }


    protected String getUsername()
    {
        String passwordFromProperties = Config.getParameter("cockpit.reports.vjdbc.username");
        return (passwordFromProperties != null) ? passwordFromProperties : UISessionUtils.getCurrentSession().getUser().getUid();
    }


    protected String getConnectionUrl()
    {
        String portProperty = "tomcat.ssl.port";
        if(Config.getParameter("ibm.websphere.internalClassAccessMode") != null)
        {
            portProperty = "cockpit.reports.vjdbc.port";
        }
        String vjdbcId = Config.getParameter("virtualjdbc.id");
        int serverPort = Config.getInt(portProperty, 9002);
        String vjdbcHost = Config.getString("cockpit.reports.vjdbc.host", "localhost");
        String url = "https://" + vjdbcHost + ":" + serverPort + "/virtualjdbc/service?tenant=" + this.tenantService.getCurrentTenantId();
        return "jdbc:hybris:flexiblesearch:" + url + "," + vjdbcId;
    }


    public void setUserService(UserService userService)
    {
        this.userService = userService;
    }


    public void setTenantService(TenantService tenantService)
    {
        this.tenantService = tenantService;
    }
}
