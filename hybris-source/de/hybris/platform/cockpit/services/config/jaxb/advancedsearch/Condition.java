package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "condition", propOrder = {"label", "values"})
public class Condition
{
    protected List<Label> label;
    protected ShortcutValueList values;
    @XmlAttribute(required = true)
    protected String operator;
    @XmlAttribute
    protected BigInteger index;


    public List<Label> getLabel()
    {
        if(this.label == null)
        {
            this.label = new ArrayList<>();
        }
        return this.label;
    }


    public ShortcutValueList getValues()
    {
        return this.values;
    }


    public void setValues(ShortcutValueList value)
    {
        this.values = value;
    }


    public String getOperator()
    {
        return this.operator;
    }


    public void setOperator(String value)
    {
        this.operator = value;
    }


    public BigInteger getIndex()
    {
        return this.index;
    }


    public void setIndex(BigInteger value)
    {
        this.index = value;
    }
}
