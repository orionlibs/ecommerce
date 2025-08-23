package de.hybris.platform.embeddedserver.tomcat;

import com.google.common.io.Files;
import de.hybris.platform.embeddedserver.api.EmbeddedServer;
import de.hybris.platform.embeddedserver.base.AbstractServerBuilder;
import java.io.File;
import org.apache.catalina.connector.Connector;

public class EmbeddedTomcatServerBuilderImpl extends AbstractServerBuilder implements EmbeddedTomcatServerBuilder
{
    private static final String KEYSTORE_RESOURCE = "tomcatembeddedserver/keystore";


    protected EmbeddedServer createEmbeddedServerFor(AbstractServerBuilder.EmbeddedServerCreationData serverInformation)
    {
        File tmpBaseDir = Files.createTempDir();
        File tmpAppBase = Files.createTempDir();
        return (EmbeddedServer)new TomcatEmbeddedServer(serverInformation.getPorts(), serverInformation.getApplications(), tmpBaseDir.toPath(), tmpAppBase
                        .toPath());
    }


    private static Connector buildConnector(int portNumber, boolean useHttps)
    {
        String keystorePath = EmbeddedTomcatServerBuilderImpl.class.getClassLoader().getResource("tomcatembeddedserver/keystore").getPath();
        Connector connector = new Connector();
        connector.setPort(portNumber);
        if(useHttps)
        {
            connector.setSecure(true);
            connector.setScheme("https");
            connector.setAttribute("URIEncoding", "UTF-8");
            connector.setAttribute("keystorePass", "123456");
            connector.setAttribute("keystoreFile", keystorePath);
            connector.setAttribute("clientAuth", "false");
            connector.setAttribute("sslProtocol", "TLS");
            connector.setAttribute("maxThreads", "150");
            connector.setAttribute("protocol", "org.apache.coyote.http11.Http11AprProtocol");
            connector.setAttribute("SSLEnabled", Boolean.TRUE);
        }
        return connector;
    }


    public boolean isAvailable()
    {
        return true;
    }
}
