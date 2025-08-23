package de.hybris.datasupplier.generator.tomcat.data;

import com.sap.sup.admin.sldsupplier.utils.StringUtils;

public class TomcatEngine extends BaseTomcatConfigObject
{
    public static final String ATTR_NAME = "name";
    public static final String ATTR_BASE_DIR = "baseDir";
    public static final String ATTR_DEFAULT_HOST = "defaultHost";
    public static final String ATTR_MANAGED_RESOURCE = "managedResource";
    private TomcatHost defaultTomcatHost;
    private TomcatService parentService;


    public String toString()
    {
        StringBuilder sb = new StringBuilder("TomcatEngine[");
        sb.append(getName());
        sb.append(",");
        sb.append(getDefaultHost());
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


    public String getBaseDir()
    {
        return (String)getAttribute("baseDir");
    }


    public void setBaseDir(String str)
    {
        setAttribute("baseDir", str);
    }


    public String getDefaultHost()
    {
        return (String)getAttribute("defaultHost");
    }


    public void setDefaultHost(String name)
    {
        setAttribute("defaultHost", name);
    }


    public TomcatHost getDefaultTomcatHost()
    {
        return this.defaultTomcatHost;
    }


    public void setDefaultTomcatHost(TomcatHost defaultTomcatHost)
    {
        this.defaultTomcatHost = defaultTomcatHost;
    }


    public TomcatService getParentService()
    {
        return this.parentService;
    }


    public void setParentService(TomcatService parentService)
    {
        this.parentService = parentService;
    }


    public String getDisplayName()
    {
        return getName();
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
        sb.append("@");
        String installationPath = getBaseDir();
        sb.append(StringUtils.toMD5Hex(serverInfo, installationPath, null));
        return sb.toString();
    }


    protected String buildCaption()
    {
        String name = getName();
        TomcatService service = getParentService();
        String hostNameUQ = service.getHostNameUQ();
        return StringUtils.cropAndConcatenate(name, 30, hostNameUQ, 30, " ON ");
    }
}
