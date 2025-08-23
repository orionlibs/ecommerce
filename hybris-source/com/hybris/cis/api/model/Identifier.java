package com.hybris.cis.api.model;

public class Identifier implements Identifiable
{
    private String id;


    public Identifier()
    {
    }


    public Identifier(String id)
    {
        this.id = id;
    }


    public String getId()
    {
        return this.id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String toString()
    {
        return "Identifier [id=" + this.id + "]";
    }
}
