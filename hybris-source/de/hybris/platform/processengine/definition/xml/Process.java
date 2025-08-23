package de.hybris.platform.processengine.definition.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {"contextParameter", "nodes"})
@XmlRootElement(name = "process")
public class Process
{
    protected List<ContextParameter> contextParameter;
    @XmlElements({@XmlElement(name = "action", type = Action.class), @XmlElement(name = "scriptAction", type = ScriptAction.class), @XmlElement(name = "split", type = Split.class), @XmlElement(name = "wait", type = Wait.class), @XmlElement(name = "end", type = End.class),
                    @XmlElement(name = "join", type = Join.class), @XmlElement(name = "notify", type = Notify.class)})
    protected List<Object> nodes;
    @XmlAttribute(name = "name", required = true)
    protected String name;
    @XmlAttribute(name = "start", required = true)
    protected String start;
    @XmlAttribute(name = "onError")
    protected String onError;
    @XmlAttribute(name = "processClass")
    protected String processClass;
    @XmlAttribute(name = "defaultNodeGroup")
    protected String defaultNodeGroup;


    public List<ContextParameter> getContextParameter()
    {
        if(this.contextParameter == null)
        {
            this.contextParameter = new ArrayList<>();
        }
        return this.contextParameter;
    }


    public List<Object> getNodes()
    {
        if(this.nodes == null)
        {
            this.nodes = new ArrayList();
        }
        return this.nodes;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String value)
    {
        this.name = value;
    }


    public String getStart()
    {
        return this.start;
    }


    public void setStart(String value)
    {
        this.start = value;
    }


    public String getOnError()
    {
        return this.onError;
    }


    public void setOnError(String value)
    {
        this.onError = value;
    }


    public String getProcessClass()
    {
        if(this.processClass == null)
        {
            return "de.hybris.platform.processengine.model.BusinessProcessModel";
        }
        return this.processClass;
    }


    public void setProcessClass(String value)
    {
        this.processClass = value;
    }


    public String getDefaultNodeGroup()
    {
        return this.defaultNodeGroup;
    }


    public void setDefaultNodeGroup(String value)
    {
        this.defaultNodeGroup = value;
    }
}
