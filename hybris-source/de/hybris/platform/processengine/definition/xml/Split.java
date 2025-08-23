package de.hybris.platform.processengine.definition.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "split", propOrder = {"targetNode"})
public class Split
{
    @XmlElement(required = true)
    protected List<TargetNode> targetNode;
    @XmlAttribute(name = "id", required = true)
    protected String id;


    public List<TargetNode> getTargetNode()
    {
        if(this.targetNode == null)
        {
            this.targetNode = new ArrayList<>();
        }
        return this.targetNode;
    }


    public String getId()
    {
        return this.id;
    }


    public void setId(String value)
    {
        this.id = value;
    }
}
