package de.hybris.bootstrap.loader;

import java.net.URL;

public class ClassLocationInfo implements Comparable<ClassLocationInfo>
{
    private final String jarName;
    private final URL location;


    public ClassLocationInfo(String jarName, URL location)
    {
        this.jarName = jarName;
        this.location = location;
    }


    public URL getJarLocation()
    {
        return this.location;
    }


    public String getClassName()
    {
        return this.jarName;
    }


    public String toString()
    {
        return "jar:" + getClassName() + " located at :" + getJarLocation();
    }


    public int hashCode()
    {
        return (getClassName() + getClassName()).hashCode();
    }


    public int compareTo(ClassLocationInfo comparable)
    {
        return hashCode() - comparable.hashCode();
    }


    public boolean equals(Object obj)
    {
        if(obj instanceof ClassLocationInfo)
        {
            return (hashCode() == obj.hashCode());
        }
        return super.equals(obj);
    }
}
