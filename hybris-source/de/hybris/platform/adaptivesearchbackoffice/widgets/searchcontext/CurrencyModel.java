package de.hybris.platform.adaptivesearchbackoffice.widgets.searchcontext;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class CurrencyModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String isoCode;
    private String name;


    public String getIsoCode()
    {
        return this.isoCode;
    }


    public void setIsoCode(String isoCode)
    {
        this.isoCode = isoCode;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
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
        CurrencyModel that = (CurrencyModel)obj;
        return (new EqualsBuilder()).append(this.isoCode, that.isoCode).isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.isoCode});
    }
}
