package de.hybris.datasupplier.generator.tomcat.data;

import com.sap.sup.admin.sldsupplier.data.java.JavaRuntime;
import com.sap.sup.admin.sldsupplier.data.model.BaseConfigObject;
import com.sap.sup.admin.sldsupplier.utils.StringUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class TomcatServer extends BaseTomcatConfigObject
{
    public static final String ATTR_SERVICE_NAMES = "serviceNames";
    public static final String ATTR_SERVER_INFO = "serverInfo";
    public static final String ATTR_PORT = "port";
    public static final String TOMCAT_VERSION_8_5 = "8.5";
    public static final String TOMCAT_VERSION_7 = "7.0";
    public static final String TOMCAT_VERSION_6 = "6.0";
    public static final String TOMCAT_VERSION_5_5 = "5.5";
    private final List services = new ArrayList();
    private String hostName;
    private JavaRuntime javaRuntime;
    private String systemId;
    private String version;
    private String hybrisClusterID;
    private String hybrisClusterMode;
    private String hybrisExtensionSet;
    private String hybrisPlatformHome;
    private String hybrisBinPath;
    private String hybrisConfigPath;
    private String hybrisDataPath;
    private String hybrisTempPath;
    private String hybrisLogPath;
    private String hybrisInstallPath;
    private String hybrisServerLogPath;
    private String ipAddress;


    public String toString()
    {
        StringBuilder sb = new StringBuilder("TomcatServer[");
        sb.append(this.hostName);
        sb.append(",");
        sb.append(this.services.size());
        sb.append(",");
        sb.append(getServerInfo());
        sb.append(",");
        sb.append(getPort());
        sb.append("]");
        return sb.toString();
    }


    public String getHostName()
    {
        return this.hostName;
    }


    public void setHostName(String hostName)
    {
        this.hostName = hostName;
    }


    public String getServerInfo()
    {
        return (String)getAttribute("serverInfo");
    }


    public void setServerInfo(String serverInfo)
    {
        setAttribute("serverInfo", serverInfo);
    }


    public String getVersion()
    {
        return this.version;
    }


    public void setVersion(String version)
    {
        this.version = version;
    }


    public String getPort()
    {
        return (String)getAttribute("port");
    }


    public void setPort(String port)
    {
        setAttribute("port", port);
    }


    public Collection getServices()
    {
        return this.services;
    }


    public void addService(TomcatService service)
    {
        service.setParentServer(this);
        this.services.add(service);
    }


    public void sortServices()
    {
        BaseConfigObject.sortConfigObjects(this.services);
    }


    public void setJavaRuntime(JavaRuntime javaRuntime)
    {
        this.javaRuntime = javaRuntime;
    }


    public JavaRuntime getJavaRuntime()
    {
        return this.javaRuntime;
    }


    protected String buildTechnicalName()
    {
        StringBuilder sb = new StringBuilder();
        String hostNameUQ = getHostNameUQ();
        String port = getPort();
        sb.append(hostNameUQ);
        sb.append(".ServerPort.");
        sb.append(port);
        return StringUtils.cropTo(sb.toString(), 256);
    }


    protected String buildUniqueName()
    {
        String hostNameUQ = getHostNameUQ();
        String port = getPort();
        return StringUtils.cropAndConcatenate(hostNameUQ, 240, port, 16, "_");
    }


    protected String buildCaption()
    {
        String port = getPort();
        String hostNameUQ = getHostNameUQ();
        StringBuilder sb = new StringBuilder("ATC Server on Port ");
        sb.append(port);
        String serverInfo = sb.toString();
        return StringUtils.cropAndConcatenate(serverInfo, 30, hostNameUQ, 30, " ON ");
    }


    public String getDisplayName()
    {
        return getServerInfo();
    }


    public String getHostNameFQ()
    {
        if(this.hostName == null)
        {
            return "";
        }
        return this.hostName.toLowerCase();
    }


    public String getHostNameUQ()
    {
        if(this.hostName == null)
        {
            return "";
        }
        return StringUtils.unqualify(this.hostName.toLowerCase());
    }


    public String getInstallationPath()
    {
        TomcatService service = findDefaultService();
        if(service != null)
        {
            return StringUtils.notNull(service.getEngine().getBaseDir());
        }
        return "";
    }


    protected TomcatService findDefaultService()
    {
        if(this.services.isEmpty())
        {
            return null;
        }
        Iterator<TomcatService> iter = this.services.iterator();
        if(iter.hasNext())
        {
            return iter.next();
        }
        return null;
    }


    public String getSystemId()
    {
        return this.systemId;
    }


    public void setSystemId(String systemId)
    {
        this.systemId = systemId;
    }


    public String getHybrisClusterID()
    {
        return this.hybrisClusterID;
    }


    public void setHybrisClusterID(String hybrisClusterID)
    {
        this.hybrisClusterID = hybrisClusterID;
    }


    public String getHybrisClusterMode()
    {
        return this.hybrisClusterMode;
    }


    public void setHybrisClusterMode(String hybrisClusterMode)
    {
        this.hybrisClusterMode = hybrisClusterMode;
    }


    public String getHybrisExtensionSet()
    {
        return this.hybrisExtensionSet;
    }


    public void setHybrisExtensionSet(String hybrisExtensionSet)
    {
        this.hybrisExtensionSet = hybrisExtensionSet;
    }


    public String getHybrisPlatformHome()
    {
        return this.hybrisPlatformHome;
    }


    public void setHybrisPlatformHome(String hybrisPlatformHome)
    {
        this.hybrisPlatformHome = hybrisPlatformHome;
    }


    public String getHybrisBinPath()
    {
        return this.hybrisBinPath;
    }


    public void setHybrisBinPath(String hybrisBinPath)
    {
        this.hybrisBinPath = hybrisBinPath;
    }


    public String getHybrisConfigPath()
    {
        return this.hybrisConfigPath;
    }


    public void setHybrisConfigPath(String hybrisConfigPath)
    {
        this.hybrisConfigPath = hybrisConfigPath;
    }


    public String getHybrisDataPath()
    {
        return this.hybrisDataPath;
    }


    public void setHybrisDataPath(String hybrisDataPath)
    {
        this.hybrisDataPath = hybrisDataPath;
    }


    public String getHybrisTempPath()
    {
        return this.hybrisTempPath;
    }


    public void setHybrisTempPath(String hybrisTempPath)
    {
        this.hybrisTempPath = hybrisTempPath;
    }


    public String getHybrisLogPath()
    {
        return this.hybrisLogPath;
    }


    public void setHybrisLogPath(String hybrisLogPath)
    {
        this.hybrisLogPath = hybrisLogPath;
    }


    public String getHybrisInstallPath()
    {
        return this.hybrisInstallPath;
    }


    public void setHybrisInstallPath(String hybrisInstallPath)
    {
        this.hybrisInstallPath = hybrisInstallPath;
    }


    public String getHybrisServerLogPath()
    {
        return this.hybrisServerLogPath;
    }


    public void setHybrisServerLogPath(String hybrisServerLogPath)
    {
        this.hybrisServerLogPath = hybrisServerLogPath;
    }


    public String getIpAddress()
    {
        return this.ipAddress;
    }


    public void setIpAddress(String ipAddress)
    {
        this.ipAddress = ipAddress;
    }
}
