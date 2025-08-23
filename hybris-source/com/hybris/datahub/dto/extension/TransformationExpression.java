package com.hybris.datahub.dto.extension;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"value", "spel"})
public class TransformationExpression
{
    @XmlValue
    String value = defaultValue();
    @XmlAttribute
    boolean spel;


    public static TransformationExpression expression()
    {
        return new TransformationExpression();
    }


    public String getValue()
    {
        return this.value;
    }


    public void setValue(String v)
    {
        this.value = (v != null) ? v : defaultValue();
    }


    private static String defaultValue()
    {
        return "";
    }


    public TransformationExpression withValue(String val)
    {
        setValue(val);
        return this;
    }


    public boolean isSpel()
    {
        return this.spel;
    }


    public void setSpel(boolean spel)
    {
        this.spel = spel;
    }


    public TransformationExpression spel()
    {
        setSpel(true);
        return this;
    }


    public String toString()
    {
        return "Expression{value='" + this.value + "', spel='" + this.spel + "'}";
    }


    public boolean equals(Object o)
    {
        if(this == o)
        {
            return true;
        }
        if(o instanceof TransformationExpression)
        {
            TransformationExpression that = (TransformationExpression)o;
            return (this.value.equals(that.value) && this.spel == that.spel);
        }
        return false;
    }


    public int hashCode()
    {
        return 31 * this.value.hashCode() + (this.spel ? 1 : 0);
    }
}
