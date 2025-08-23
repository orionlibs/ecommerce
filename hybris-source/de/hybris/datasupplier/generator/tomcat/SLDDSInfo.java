package de.hybris.datasupplier.generator.tomcat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SLDDSInfo implements Serializable
{
    private static final long serialVersionUID = -8893335045765889757L;
    private static final String VERSION_TAG = "Version";
    private static final String CREATED_TAG = "Created";
    private static final String CONFIGURATION_TAG = "Server";
    private static final String PRODUCT_SOFTWARE_TAG = "Software SCs";
    private static final String SLD_TAG = "SLD-DS for Tomcat Application Server";
    private final String configInfo;
    private final String productSoftwareInfo;
    private final String timestamp;
    private final String description;


    public SLDDSInfo(String configInfo, String productSoftwareInfo)
    {
        this.configInfo = configInfo;
        this.productSoftwareInfo = productSoftwareInfo;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd:HH:mmss");
        Date currentDate = new Date();
        this.timestamp = formatter.format(currentDate);
        this.description = buildDescription();
    }


    public String getTimestamp()
    {
        return this.timestamp;
    }


    public String getVersion()
    {
        return Version.VERSION.getVersion();
    }


    public String getConfigInfo()
    {
        return this.configInfo;
    }


    public String getSldTag()
    {
        return "SLD-DS for Tomcat Application Server";
    }


    public String getDescription()
    {
        return this.description;
    }


    private String buildDescription()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("SLD-DS for Tomcat Application Server");
        sb.append(";\n");
        sb.append("Version");
        sb.append("=");
        sb.append(Version.VERSION.getVersion());
        sb.append(";\n");
        sb.append("Created");
        sb.append(getTimestamp());
        if(this.configInfo != null)
        {
            sb.append(";\n ");
            sb.append("Server");
            sb.append("=");
            sb.append(this.configInfo);
        }
        if(this.productSoftwareInfo != null)
        {
            sb.append(";\n");
            sb.append("Software SCs");
            sb.append("=");
            sb.append(this.productSoftwareInfo);
        }
        return sb.toString();
    }
}
