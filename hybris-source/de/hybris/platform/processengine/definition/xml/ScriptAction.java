package de.hybris.platform.processengine.definition.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "scriptAction", propOrder = {"script", "parameter", "transition"})
public class ScriptAction
{
    @XmlElement(required = true)
    protected Script script;
    protected List<Parameter> parameter;
    @XmlElement(required = true)
    protected List<Transition> transition;
    @XmlAttribute(name = "node")
    protected Integer node;
    @XmlAttribute(name = "nodeGroup")
    protected String nodeGroup;
    @XmlAttribute(name = "canJoinPreviousNode")
    protected Boolean canJoinPreviousNode;
    @XmlAttribute(name = "id", required = true)
    protected String id;


    public Script getScript()
    {
        return this.script;
    }


    public void setScript(Script value)
    {
        this.script = value;
    }


    public List<Parameter> getParameter()
    {
        if(this.parameter == null)
        {
            this.parameter = new ArrayList<>();
        }
        return this.parameter;
    }


    public List<Transition> getTransition()
    {
        if(this.transition == null)
        {
            this.transition = new ArrayList<>();
        }
        return this.transition;
    }


    public Integer getNode()
    {
        return this.node;
    }


    public void setNode(Integer value)
    {
        this.node = value;
    }


    public String getNodeGroup()
    {
        return this.nodeGroup;
    }


    public void setNodeGroup(String value)
    {
        this.nodeGroup = value;
    }


    public Boolean isCanJoinPreviousNode()
    {
        return this.canJoinPreviousNode;
    }


    public void setCanJoinPreviousNode(Boolean value)
    {
        this.canJoinPreviousNode = value;
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
