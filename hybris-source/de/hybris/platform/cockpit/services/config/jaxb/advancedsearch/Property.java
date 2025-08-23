package de.hybris.platform.cockpit.services.config.jaxb.advancedsearch;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property", propOrder = {"conditions", "parameter"})
public class Property
{
    protected ConditionList conditions;
    protected List<Parameter> parameter;
    @XmlAttribute(required = true)
    protected String qualifier;
    @XmlAttribute
    protected Boolean visible;
    @XmlAttribute(name = "sort-enabled")
    protected Boolean sortEnabled;
    @XmlAttribute
    protected String editor;


    public ConditionList getConditions()
    {
        return this.conditions;
    }


    public void setConditions(ConditionList value)
    {
        this.conditions = value;
    }


    public List<Parameter> getParameter()
    {
        if(this.parameter == null)
        {
            this.parameter = new ArrayList<>();
        }
        return this.parameter;
    }


    public String getQualifier()
    {
        return this.qualifier;
    }


    public void setQualifier(String value)
    {
        this.qualifier = value;
    }


    public boolean isVisible()
    {
        if(this.visible == null)
        {
            return true;
        }
        return this.visible.booleanValue();
    }


    public void setVisible(Boolean value)
    {
        this.visible = value;
    }


    public boolean isSortEnabled()
    {
        if(this.sortEnabled == null)
        {
            return true;
        }
        return this.sortEnabled.booleanValue();
    }


    public void setSortEnabled(Boolean value)
    {
        this.sortEnabled = value;
    }


    public String getEditor()
    {
        return this.editor;
    }


    public void setEditor(String value)
    {
        this.editor = value;
    }
}
