package de.hybris.platform.sap.sapcpiadapter.data;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "sapCpiTargetSystem")
public class SapCpiTargetSystem implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String url;
    private String username;
    private String senderName;
    private String senderPort;
    private String receiverName;
    private String receiverPort;
    private String client;


    public void setUrl(String url)
    {
        this.url = url;
    }


    public String getUrl()
    {
        return this.url;
    }


    public void setUsername(String username)
    {
        this.username = username;
    }


    public String getUsername()
    {
        return this.username;
    }


    public void setSenderName(String senderName)
    {
        this.senderName = senderName;
    }


    public String getSenderName()
    {
        return this.senderName;
    }


    public void setSenderPort(String senderPort)
    {
        this.senderPort = senderPort;
    }


    public String getSenderPort()
    {
        return this.senderPort;
    }


    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }


    public String getReceiverName()
    {
        return this.receiverName;
    }


    public void setReceiverPort(String receiverPort)
    {
        this.receiverPort = receiverPort;
    }


    public String getReceiverPort()
    {
        return this.receiverPort;
    }


    public void setClient(String client)
    {
        this.client = client;
    }


    public String getClient()
    {
        return this.client;
    }
}
