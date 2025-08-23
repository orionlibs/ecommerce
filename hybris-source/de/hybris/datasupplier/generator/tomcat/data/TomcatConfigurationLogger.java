package de.hybris.datasupplier.generator.tomcat.data;

import com.sap.sup.admin.sldsupplier.utils.StringUtils;
import java.util.Collection;
import java.util.Iterator;

public class TomcatConfigurationLogger
{
    public String toLog(TomcatConfiguration loggee)
    {
        StringBuilder sb = new StringBuilder(loggee.toString());
        return sb.toString();
    }


    public String toXMLLog(TomcatConfiguration loggee)
    {
        return toSLDLog(loggee);
    }


    public String toSLDLog(TomcatConfiguration loggee)
    {
        StringBuffer sb = new StringBuffer(loggee.toString());
        sb.append("\n");
        StringUtils.indent(sb, 6);
        sb.append("<TomcatConfiguration>");
        Tomcat tomcat = loggee.getTomcat();
        if(tomcat != null)
        {
            appendToSLDLog(tomcat, sb, 10);
            Collection servers = tomcat.getServers();
            Iterator<TomcatServer> serverIter = servers.iterator();
            while(serverIter.hasNext())
            {
                TomcatServer server = serverIter.next();
                sb.append("\n");
                StringUtils.indent(sb, 14);
                sb.append("<TomcatServer>");
                appendToSLDLog(server, sb, 17);
                Collection services = server.getServices();
                Iterator<TomcatService> serviceIter = services.iterator();
                while(serviceIter.hasNext())
                {
                    TomcatService service = serviceIter.next();
                    sb.append("\n");
                    StringUtils.indent(sb, 19);
                    sb.append("<TomcatService>");
                    appendToSLDLog(service, sb, 23);
                    TomcatEngine engine = service.getEngine();
                    sb.append("\n");
                    StringUtils.indent(sb, 25);
                    sb.append("<TomcatEngine>");
                    appendToSLDLog(engine, sb, 29);
                    sb.append("\n");
                    StringUtils.indent(sb, 25);
                    sb.append("</TomcatEngine>");
                    sb.append("\n");
                    StringUtils.indent(sb, 19);
                    sb.append("</TomcatService>");
                }
                sb.append("\n");
                StringUtils.indent(sb, 14);
                sb.append("</TomcatServer>");
            }
        }
        sb.append("\n");
        sb.append("</TomcatConfiguration>");
        return sb.toString();
    }


    public void appendToSLDLog(Tomcat tomcat, StringBuffer sb, int indent)
    {
    }


    public void appendToSLDLog(TomcatServer server, StringBuffer sb, int indent)
    {
        sb.append("\n");
        StringUtils.indent(sb, indent);
        sb.append("hostName=");
        sb.append(server.getHostName());
        sb.append("\n");
        StringUtils.indent(sb, indent);
        sb.append("serverInfo=");
        sb.append(server.getServerInfo());
        sb.append("\n");
        StringUtils.indent(sb, indent);
        sb.append("port=");
        sb.append(server.getPort());
    }


    public void appendToSLDLog(TomcatService service, StringBuffer sb, int indent)
    {
        sb.append("\n");
        StringUtils.indent(sb, indent);
        sb.append("name=");
        sb.append(service.getName());
        sb.append("\n");
        StringUtils.indent(sb, indent);
        sb.append("containerName=");
        sb.append(service.getContainerName());
    }


    public void appendToSLDLog(TomcatEngine engine, StringBuffer sb, int indent)
    {
        sb.append("\n");
        StringUtils.indent(sb, indent);
        sb.append("name=");
        sb.append(engine.getName());
        sb.append("\n");
        StringUtils.indent(sb, indent);
        sb.append("defaultHost=");
        sb.append(engine.getDefaultHost());
    }
}
