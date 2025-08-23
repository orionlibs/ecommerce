package de.hybris.platform.omsbackoffice.config.impl.jaxb.hybris.customersupportrefineby;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroupMemberValue")
public class GroupMemberValue
{
    @XmlAttribute(name = "value", required = true)
    protected String value;
    @XmlAttribute(name = "merge-mode")
    protected MergeMode mergeMode;


    public String getValue()
    {
        return this.value;
    }


    public void setValue(String value)
    {
        this.value = value;
    }


    public MergeMode getMergeMode()
    {
        return this.mergeMode;
    }


    public void setMergeMode(MergeMode value)
    {
        this.mergeMode = value;
    }
}
