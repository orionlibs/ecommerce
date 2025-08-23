package de.hybris.platform.core.internal;

import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.bootstrap.config.ExtensionInfo;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.util.Token;
import de.hybris.platform.util.Utilities;
import de.hybris.platform.util.config.ConfigIntf;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShutdownTokenFileCreator
{
    private static final Logger LOG = LoggerFactory.getLogger(ShutdownTokenFileCreator.class);
    public static final String TOMCAT_HTTP_CONNECTOR_SECURE = "tomcat.http.connector.secure";
    public static final String TOMCAT_HTTP_PORT = "tomcat.http.port";
    public static final String TOMCAT_SSL_PORT = "tomcat.ssl.port";
    private static final String HYBRIS_DATA_DIR = "HYBRIS_DATA_DIR";
    private static final String SHUTDOWN_TOKEN = "shutdown.token";
    private final Token localShutdownToken;
    private final ConfigIntf config;


    public ShutdownTokenFileCreator(Token localShutdownToken, ConfigIntf config)
    {
        this.localShutdownToken = localShutdownToken;
        this.config = config;
    }


    public void writeShutdownTokenFile()
    {
        try
        {
            String hybrisDataDir = ConfigUtil.getPropertyOrEnv("HYBRIS_DATA_DIR");
            if(hybrisDataDir == null)
            {
                LOG.warn("Failed to determine hybris data dir - skipping shutdown token file creation");
            }
            else
            {
                String tokenContent = createShutdownTokenContent();
                Files.write(Paths.get(hybrisDataDir, new String[] {"shutdown.token"}), tokenContent.getBytes(StandardCharsets.UTF_8), new OpenOption[] {StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING});
            }
        }
        catch(Exception e)
        {
            throw new SystemException("Failed to create system shutdown token", e);
        }
    }


    public String createShutdownTokenContent()
    {
        String protocol = getHybrisProtocol();
        String port = getHybrisPort();
        String webRoot = getHacWebRoot();
        String tokenContent = protocol + "://localhost:" + protocol + port + "/monitoring/suspendresume/halt;" + webRoot;
        return tokenContent;
    }


    private String getHacWebRoot()
    {
        ExtensionInfo hac = Utilities.getPlatformConfig().getExtensionInfo("hac");
        return hac.getWebModule().getWebRoot();
    }


    private String getHybrisProtocol()
    {
        boolean tomcatOnHttpPort = this.config.getBoolean("tomcat.http.connector.secure", false);
        return tomcatOnHttpPort ? "http" : "https";
    }


    private String getHybrisPort()
    {
        boolean tomcatOnHttpPort = this.config.getBoolean("tomcat.http.connector.secure", false);
        return tomcatOnHttpPort ? this.config.getParameter("tomcat.http.port") : this.config.getParameter("tomcat.ssl.port");
    }
}
