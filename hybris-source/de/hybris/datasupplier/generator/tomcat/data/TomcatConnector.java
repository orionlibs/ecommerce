package de.hybris.datasupplier.generator.tomcat.data;

import com.sap.sup.admin.sldsupplier.utils.StringUtils;

public class TomcatConnector extends BaseTomcatConfigObject
{
    private TomcatService parentService;
    public static final String ATTR_PORT = "port";
    public static final String ATTR_SCHEME = "scheme";
    public static final String ATTR_PROTOCOL = "protocol";


    public String toString()
    {
        StringBuilder sb = new StringBuilder("TomcatConnector[");
        sb.append(getScheme());
        sb.append(",");
        sb.append(getPort());
        sb.append("]");
        return sb.toString();
    }


    public TomcatService getParentService()
    {
        return this.parentService;
    }


    public void setParentService(TomcatService parentService)
    {
        this.parentService = parentService;
    }


    public String getPort()
    {
        return (String)getAttribute("port");
    }


    public void setPort(String str)
    {
        setAttribute("port", str);
    }


    public String getScheme()
    {
        return (String)getAttribute("scheme");
    }


    public void setScheme(String str)
    {
        setAttribute("scheme", str);
    }


    public String getProtocol()
    {
        return (String)getAttribute("protocol");
    }


    public void setProtocol(String str)
    {
        setAttribute("protocol", str);
    }


    public String getDisplayName()
    {
        return getPort();
    }


    protected String buildTechnicalName()
    {
        return StringUtils.notNull(getProtocol() + "/" + getProtocol());
    }


    protected String buildUniqueName()
    {
        StringBuilder sb = new StringBuilder();
        TomcatService service = getParentService();
        String hostNameUQ = null;
        if(service != null)
        {
            hostNameUQ = service.getHostNameUQ();
            sb.append(hostNameUQ);
            sb.append(".ServerPort.");
        }
        String port = getPort();
        if(port != null)
        {
            sb.append(port);
        }
        String protocol = getProtocol();
        if(protocol != null)
        {
            sb.append("_");
            sb.append(protocol);
        }
        String scheme = getProtocol();
        if(scheme != null)
        {
            sb.append("_");
            sb.append(scheme);
        }
        return StringUtils.cropTo(sb.toString(), 256);
    }


    protected String buildCaption()
    {
        StringBuilder sb = new StringBuilder();
        String protocol = getProtocol();
        if(protocol != null)
        {
            sb.append(protocol);
        }
        String scheme = getScheme();
        if(scheme != null)
        {
            sb.append(",");
            sb.append(scheme);
        }
        String port = getPort();
        if(port != null)
        {
            sb.append(" ");
            sb.append(" ON ");
            sb.append(" ");
            sb.append(port);
        }
        return StringUtils.cropTo(sb.toString(), 64);
    }
}
