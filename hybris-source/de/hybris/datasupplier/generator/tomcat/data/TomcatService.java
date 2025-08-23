package de.hybris.datasupplier.generator.tomcat.data;

import com.sap.sup.admin.sldsupplier.utils.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TomcatService extends BaseTomcatConfigObject
{
    public static final String ATTR_NAME = "name";
    public static final String ATTR_CONTAINER_NAME = "containerName";
    public static final String ATTR_CONNECTOR_NAMES = "connectorNames";
    private TomcatEngine engine;
    private TomcatServer parentServer;
    private final List connectors = new ArrayList();


    public String toString()
    {
        StringBuilder sb = new StringBuilder("TomcatService[");
        sb.append(getName());
        sb.append(",");
        sb.append(getContainerName());
        sb.append("]");
        return sb.toString();
    }


    public String getName()
    {
        return (String)getAttribute("name");
    }


    public void setName(String name)
    {
        setAttribute("name", name);
    }


    public String getContainerName()
    {
        return (String)getAttribute("containerName");
    }


    public void setContainerName(String str)
    {
        setAttribute("containerName", str);
    }


    public TomcatEngine getEngine()
    {
        return this.engine;
    }


    public void setEngine(TomcatEngine engine)
    {
        engine.setParentService(this);
        this.engine = engine;
    }


    public TomcatServer getParentServer()
    {
        return this.parentServer;
    }


    public void setParentServer(TomcatServer parentServer)
    {
        this.parentServer = parentServer;
    }


    public Collection getConnectors()
    {
        return this.connectors;
    }


    public void addConnector(TomcatConnector connector)
    {
        connector.setParentService(this);
        this.connectors.add(connector);
    }


    protected String buildTechnicalName()
    {
        StringBuilder sb = new StringBuilder();
        String serverInfo = getName();
        if(serverInfo != null)
        {
            serverInfo = StringUtils.cropTo(serverInfo, 223);
            sb.append(serverInfo);
        }
        String hostNameUQ = getHostNameUQ();
        sb.append("@");
        if(this.engine != null)
        {
            String installationPath = this.engine.getBaseDir();
            sb.append(StringUtils.toMD5Hex(serverInfo, hostNameUQ, installationPath, null));
        }
        else
        {
            sb.append(StringUtils.toMD5Hex(serverInfo, hostNameUQ, null));
        }
        return sb.toString();
    }


    protected String buildCaption()
    {
        String name = getName();
        String hostNameUQ = getHostNameUQ();
        return StringUtils.cropAndConcatenate(name, 30, hostNameUQ, 30, " ON ");
    }


    public String getDisplayName()
    {
        return getName();
    }


    public String getHostNameFQ()
    {
        TomcatServer parent = getParentServer();
        if(parent == null)
        {
            return null;
        }
        return parent.getHostNameFQ();
    }


    public String getHostNameUQ()
    {
        TomcatServer parent = getParentServer();
        if(parent == null)
        {
            return null;
        }
        return parent.getHostNameUQ();
    }
}
