package de.hybris.platform.cockpit.services.config.impl;

import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.PropertyEditorDescriptor;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.xmlprovider.XmlDataProvider;
import de.hybris.platform.core.Registry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

public class PropertyEditorRowConfiguration implements EditorRowConfiguration
{
    private static final Logger LOG = LoggerFactory.getLogger(PropertyEditorRowConfiguration.class);
    private final PropertyDescriptor propertyDescriptor;
    private boolean visible;
    private boolean editable;
    private PropertyEditorDescriptor editorDescriptor;
    private String editor;
    private final Map<String, String> parameters = new HashMap<>();
    private String printoutAs;
    private XmlDataProvider xmlDataProvider;


    public PropertyEditorRowConfiguration(PropertyDescriptor propertyDescriptor, boolean visible, boolean editable)
    {
        this.propertyDescriptor = propertyDescriptor;
        this.visible = visible;
        this.editable = editable;
    }


    public PropertyDescriptor getPropertyDescriptor()
    {
        return this.propertyDescriptor;
    }


    public boolean isVisible()
    {
        return this.visible;
    }


    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }


    public boolean isEditable()
    {
        return this.editable;
    }


    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }


    public PropertyEditorDescriptor getEditorDescriptor()
    {
        return this.editorDescriptor;
    }


    public void setEditorDescriptor(PropertyEditorDescriptor editorDescriptor)
    {
        if(editorDescriptor == null)
        {
            throw new NullPointerException("editor descriptor was NULL");
        }
        if(!editorDescriptor.getEditorType().equalsIgnoreCase(getPropertyDescriptor().getEditorType()))
        {
            throw new IllegalArgumentException("editor type mismatch " + editorDescriptor.getEditorType() + "<>" +
                            getPropertyDescriptor().getEditorType() + " in " + editorDescriptor + " and " + getPropertyDescriptor());
        }
        this.editorDescriptor = editorDescriptor;
    }


    public String getEditor()
    {
        return this.editor;
    }


    public void setEditor(String editor)
    {
        this.editor = editor;
    }


    public Map<String, String> getParameters()
    {
        return Collections.unmodifiableMap(this.parameters);
    }


    public String getParameter(String name)
    {
        return this.parameters.get(name);
    }


    public void setParameter(String name, String value)
    {
        this.parameters.put(name, value);
    }


    public String getPrintoutAs()
    {
        return this.printoutAs;
    }


    public void setPrintoutAs(String printoutAs)
    {
        this.printoutAs = printoutAs;
    }


    public XmlDataProvider getXmlDataProvider()
    {
        if(this.xmlDataProvider == null)
        {
            setXmlDataProvider("xmlDataProvider");
        }
        return this.xmlDataProvider;
    }


    public void setXmlDataProvider(String xmlDataProvider)
    {
        try
        {
            this.xmlDataProvider = (XmlDataProvider)Registry.getApplicationContext().getBean(xmlDataProvider);
        }
        catch(NoSuchBeanDefinitionException e)
        {
            LOG.error("Couldn't find bean: " + xmlDataProvider + ", row " + this + " will not have xml representation.");
        }
    }
}
