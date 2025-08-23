package de.hybris.bootstrap.loader.metrics;

public class ClasspathLocationUsage
{
    private final String location;
    private final int usage;


    public ClasspathLocationUsage(String location, int usage)
    {
        this.location = location;
        this.usage = usage;
    }


    public String getLocation()
    {
        return this.location;
    }


    public int getUsage()
    {
        return this.usage;
    }


    public String toString()
    {
        return "ClasspathLocationUsage{location='" + this.location + "', usage=" + this.usage + "}";
    }
}
