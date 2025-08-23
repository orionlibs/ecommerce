package de.hybris.platform.searchservices.admin.data;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class SnExpressionInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    private String expression;
    private Map<Locale, String> name;


    public void setExpression(String expression)
    {
        this.expression = expression;
    }


    public String getExpression()
    {
        return this.expression;
    }


    public void setName(Map<Locale, String> name)
    {
        this.name = name;
    }


    public Map<Locale, String> getName()
    {
        return this.name;
    }


    public boolean equals(Object o)
    {
        if(o == null)
        {
            return false;
        }
        if(o == this)
        {
            return true;
        }
        if(getClass() != o.getClass())
        {
            return false;
        }
        SnExpressionInfo other = (SnExpressionInfo)o;
        return (Objects.equals(getExpression(), other.getExpression()) &&
                        Objects.equals(getName(), other.getName()));
    }


    public int hashCode()
    {
        int result = 1;
        Object<Locale, String> attribute = (Object<Locale, String>)this.expression;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        attribute = (Object<Locale, String>)this.name;
        result = 31 * result + ((attribute == null) ? 0 : attribute.hashCode());
        return result;
    }
}
