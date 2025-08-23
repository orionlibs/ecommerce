package de.hybris.platform.assistedservicefacades.customer360;

import java.io.Serializable;
import java.util.Map;

public class Fragment implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String title;
    private String id;
    private String jspPath;
    private Object data;
    private Integer priority;
    private Map<String, String> properties;


    public void setTitle(String title)
    {
        this.title = title;
    }


    public String getTitle()
    {
        return this.title;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setJspPath(String jspPath)
    {
        this.jspPath = jspPath;
    }


    public String getJspPath()
    {
        return this.jspPath;
    }


    public void setData(Object data)
    {
        this.data = data;
    }


    public Object getData()
    {
        return this.data;
    }


    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }


    public Integer getPriority()
    {
        return this.priority;
    }


    public void setProperties(Map<String, String> properties)
    {
        this.properties = properties;
    }


    public Map<String, String> getProperties()
    {
        return this.properties;
    }
}
