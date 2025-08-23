package de.hybris.platform.assistedservicefacades.customer360;

import java.io.Serializable;
import java.util.List;

public class Section implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String title;
    private String id;
    private List<Fragment> fragments;
    private Integer priority;


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


    public void setFragments(List<Fragment> fragments)
    {
        this.fragments = fragments;
    }


    public List<Fragment> getFragments()
    {
        return this.fragments;
    }


    public void setPriority(Integer priority)
    {
        this.priority = priority;
    }


    public Integer getPriority()
    {
        return this.priority;
    }
}
