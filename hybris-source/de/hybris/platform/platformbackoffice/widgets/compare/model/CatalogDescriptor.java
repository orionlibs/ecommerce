package de.hybris.platform.platformbackoffice.widgets.compare.model;

import java.util.Objects;

public class CatalogDescriptor
{
    private final String id;
    private final String name;
    private final String version;
    private final boolean canRead;
    private final boolean canWrite;


    public CatalogDescriptor(String id, String name, String version, boolean canRead, boolean canWrite)
    {
        this.id = id;
        this.name = name;
        this.version = version;
        this.canRead = canRead;
        this.canWrite = canWrite;
    }


    public String getId()
    {
        return this.id;
    }


    public String getName()
    {
        return this.name;
    }


    public String getVersion()
    {
        return this.version;
    }


    public boolean canRead()
    {
        return this.canRead;
    }


    public boolean canWrite()
    {
        return this.canWrite;
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o == null || getClass() != o.getClass())
        {
            return false;
        }
        CatalogDescriptor that = (CatalogDescriptor)o;
        return (Objects.equals(this.id, that.id) && Objects.equals(this.name, that.name) && Objects.equals(this.version, that.version));
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.id, this.name, this.version});
    }
}
