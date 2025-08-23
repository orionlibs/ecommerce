package de.hybris.bootstrap.beangenerator.definitions.model;

import java.io.File;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class Extension
{
    private final String name;
    private final File home;


    public Extension(String name, File home)
    {
        this.name = name;
        this.home = home;
    }


    public String getName()
    {
        return this.name;
    }


    public File getHome()
    {
        return this.home;
    }


    public String toString()
    {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SIMPLE_STYLE);
    }
}
