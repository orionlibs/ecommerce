package de.hybris.datasupplier.generator.tomcat.data;

import com.sap.sup.admin.sldsupplier.collector.AppServerConfiguration;
import com.sap.sup.admin.sldsupplier.data.model.Loggable;
import java.io.Serializable;

public class TomcatConfiguration implements Serializable, Loggable, AppServerConfiguration
{
    private static final long serialVersionUID = -1897124456563888584L;
    private Tomcat tomcat = new Tomcat();


    public Tomcat getTomcat()
    {
        return this.tomcat;
    }


    public void setTomcat(Tomcat tomcat)
    {
        this.tomcat = tomcat;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder("TomcatConfiguration{");
        sb.append(" tomcat = ");
        if(this.tomcat != null)
        {
            sb.append(this.tomcat);
        }
        else
        {
            sb.append("0");
        }
        sb.append(" }");
        return sb.toString();
    }


    public String toXMLLog()
    {
        TomcatConfigurationLogger logger = new TomcatConfigurationLogger();
        return logger.toXMLLog(this);
    }


    public String toLog()
    {
        TomcatConfigurationLogger logger = new TomcatConfigurationLogger();
        return logger.toLog(this);
    }
}
