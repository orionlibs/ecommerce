package de.hybris.platform.cockpit.services.config.jaxb.wizard;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "group", propOrder = {"label", "property"})
public class Group
{
    @XmlElement(required = true)
    protected List<Label> label;
    @XmlElement(required = true)
    protected List<Property> property;
    @XmlAttribute(required = true)
    protected String qualifier;
    @XmlAttribute
    protected Boolean visible;
    @XmlAttribute
    protected Boolean tabbed;
    @XmlAttribute(name = "initially-opened")
    protected Boolean initiallyOpened;
    @XmlAttribute(name = "show-if-empty")
    protected Boolean showIfEmpty;
    @XmlAttribute(name = "show-in-create-mode")
    protected Boolean showInCreateMode;
    @XmlAttribute
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger position;


    public List<Label> getLabel()
    {
        if(this.label == null)
        {
            this.label = new ArrayList<>();
        }
        return this.label;
    }


    public List<Property> getProperties()
    {
        if(this.property == null)
        {
            this.property = new ArrayList<>();
        }
        return this.property;
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


    public boolean isTabbed()
    {
        if(this.tabbed == null)
        {
            return false;
        }
        return this.tabbed.booleanValue();
    }


    public void setTabbed(Boolean value)
    {
        this.tabbed = value;
    }


    public boolean isInitiallyOpened()
    {
        if(this.initiallyOpened == null)
        {
            return true;
        }
        return this.initiallyOpened.booleanValue();
    }


    public void setInitiallyOpened(Boolean value)
    {
        this.initiallyOpened = value;
    }


    public boolean isShowIfEmpty()
    {
        if(this.showIfEmpty == null)
        {
            return true;
        }
        return this.showIfEmpty.booleanValue();
    }


    public void setShowIfEmpty(Boolean value)
    {
        this.showIfEmpty = value;
    }


    public boolean isShowInCreateMode()
    {
        if(this.showInCreateMode == null)
        {
            return true;
        }
        return this.showInCreateMode.booleanValue();
    }


    public void setShowInCreateMode(Boolean value)
    {
        this.showInCreateMode = value;
    }


    public BigInteger getPosition()
    {
        if(this.position == null)
        {
            return new BigInteger("1");
        }
        return this.position;
    }


    public void setPosition(BigInteger value)
    {
        this.position = value;
    }
}
