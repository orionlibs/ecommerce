package de.hybris.platform.adaptivesearchbackoffice.widgets.categoryselector;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class CategoryModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;
    private boolean virtual;
    private boolean hasSearchConfiguration;
    private int numberOfConfigurations;


    public String getCode()
    {
        return this.code;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public boolean isVirtual()
    {
        return this.virtual;
    }


    public void setVirtual(boolean virtual)
    {
        this.virtual = virtual;
    }


    public void setHasSearchConfiguration(boolean hasSearchConfiguration)
    {
        this.hasSearchConfiguration = hasSearchConfiguration;
    }


    public boolean isHasSearchConfiguration()
    {
        return this.hasSearchConfiguration;
    }


    public void setNumberOfConfigurations(int numberOfConfigurations)
    {
        this.numberOfConfigurations = numberOfConfigurations;
    }


    public int getNumberOfConfigurations()
    {
        return this.numberOfConfigurations;
    }


    public boolean equals(Object obj)
    {
        if(this == obj)
        {
            return true;
        }
        if(obj == null || getClass() != obj.getClass())
        {
            return false;
        }
        CategoryModel that = (CategoryModel)obj;
        return (new EqualsBuilder()).append(this.code, that.code).isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.code});
    }
}
