package de.hybris.datasupplier.generator.tomcat.data;

import com.sap.sup.admin.sldsupplier.data.model.BaseConfigObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Tomcat extends BaseConfigObject implements Serializable
{
    private static final long serialVersionUID = 1L;
    private final List<TomcatServer> tomcatServers = new ArrayList<>();


    public Collection getServers()
    {
        return this.tomcatServers;
    }


    public void addServer(TomcatServer server)
    {
        this.tomcatServers.add(server);
    }


    public void sortServers()
    {
        BaseConfigObject.sortConfigObjects(this.tomcatServers);
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder("Tomcat{");
        sb.append(" servers = ");
        sb.append(this.tomcatServers.size());
        sb.append(" }");
        return sb.toString();
    }


    protected String buildCaption()
    {
        return "Tomcat";
    }


    protected String buildTechnicalName()
    {
        return "Tomcat";
    }
}
