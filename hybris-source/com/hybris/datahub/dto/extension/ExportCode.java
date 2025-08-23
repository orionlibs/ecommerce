package com.hybris.datahub.dto.extension;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"value", "expression"})
public class ExportCode implements Serializable
{
    @XmlValue
    String value;
    @XmlAttribute
    boolean expression;


    public ExportCode()
    {
    }


    public ExportCode(String value, boolean expression)
    {
        this.value = value;
        this.expression = expression;
    }


    public String getValue()
    {
        return this.value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public boolean getExpression()
    {
        return this.expression;
    }


    public void setExpression(boolean expression)
    {
        this.expression = expression;
    }


    public String toString()
    {
        return "ExportCode{value='" + this.value + "', expression='" + this.expression + "}";
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
        ExportCode that = (ExportCode)o;
        if(this.expression != that.expression)
        {
            return false;
        }
        if(!this.value.equals(that.value))
        {
            return false;
        }
        return true;
    }


    public int hashCode()
    {
        int result = this.value.hashCode();
        result = 31 * result + (this.expression ? 1 : 0);
        return result;
    }
}
