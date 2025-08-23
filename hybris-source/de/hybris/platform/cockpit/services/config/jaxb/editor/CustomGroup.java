package de.hybris.platform.cockpit.services.config.jaxb.editor;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "custom-group", propOrder = {"label"})
public class CustomGroup
{
    protected List<Label> label;
    @XmlAttribute(required = true)
    protected String qualifier;
    @XmlAttribute
    protected Boolean visible;
    @XmlAttribute(name = "class")
    protected String clazz;
    @XmlAttribute(name = "spring-bean")
    protected String springBean;
    @XmlAttribute(name = "initially-opened")
    protected Boolean initiallyOpened;
    @XmlAttribute(name = "show-if-empty")
    protected Boolean showIfEmpty;
    @XmlAttribute
    protected Boolean printable;
    @XmlAttribute
    protected String xmlDataProvider;


    public List<Label> getLabel()
    {
        if(this.label == null)
        {
            this.label = new ArrayList<>();
        }
        return this.label;
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


    public String getClazz()
    {
        return this.clazz;
    }


    public void setClazz(String value)
    {
        this.clazz = value;
    }


    public String getSpringBean()
    {
        return this.springBean;
    }


    public void setSpringBean(String value)
    {
        this.springBean = value;
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


    public boolean isPrintable()
    {
        if(this.printable == null)
        {
            return true;
        }
        return this.printable.booleanValue();
    }


    public void setPrintable(Boolean value)
    {
        this.printable = value;
    }


    public String getXmlDataProvider()
    {
        if(this.xmlDataProvider == null)
        {
            return "xmlDataProvider";
        }
        return this.xmlDataProvider;
    }


    public void setXmlDataProvider(String value)
    {
        this.xmlDataProvider = value;
    }
}
