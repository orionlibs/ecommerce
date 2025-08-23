package de.hybris.platform.masterserver.impl;

public class StatisticsPayload
{
    private final String password;
    private final String homeURL;
    private final String data;


    public StatisticsPayload(String password, String homeURL, String data)
    {
        this.password = password;
        this.homeURL = homeURL;
        this.data = data;
    }


    public String getPassword()
    {
        return this.password;
    }


    public String getHomeURL()
    {
        return this.homeURL;
    }


    public String getData()
    {
        return this.data;
    }
}
