package de.hybris.platform.solrfacetsearch.config;

import java.io.Serializable;

public class ValueRange implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String name;
    private Comparable from;
    private Comparable to;


    public void setName(String name)
    {
        this.name = name;
    }


    public String getName()
    {
        return this.name;
    }


    public void setFrom(Comparable from)
    {
        this.from = from;
    }


    public Comparable getFrom()
    {
        return this.from;
    }


    public void setTo(Comparable to)
    {
        this.to = to;
    }


    public Comparable getTo()
    {
        return this.to;
    }
}
