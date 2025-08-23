package de.hybris.platform.adaptivesearchbackoffice.widgets.searchresultbrowser;

import java.io.Serializable;
import java.util.Objects;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class SortModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String code;
    private String name;


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
        SortModel that = (SortModel)obj;
        return (new EqualsBuilder())
                        .append(this.code, that.code)
                        .isEquals();
    }


    public int hashCode()
    {
        return Objects.hash(new Object[] {this.code});
    }
}
