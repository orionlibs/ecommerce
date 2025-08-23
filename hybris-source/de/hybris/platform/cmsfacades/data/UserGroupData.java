package de.hybris.platform.cmsfacades.data;

import java.io.Serializable;
import java.util.Map;

public class UserGroupData implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String uid;
    private Map<String, String> name;


    public void setUid(String uid)
    {
        this.uid = uid;
    }


    public String getUid()
    {
        return this.uid;
    }


    public void setName(Map<String, String> name)
    {
        this.name = name;
    }


    public Map<String, String> getName()
    {
        return this.name;
    }
}
