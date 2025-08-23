package de.hybris.platform.cockpit.services.xmlprovider.impl;

import de.hybris.platform.cockpit.model.listview.impl.DefaultValueHandler;
import de.hybris.platform.cockpit.model.meta.PropertyDescriptor;
import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.values.ValueHandlerException;
import de.hybris.platform.cockpit.services.values.ValueService;
import de.hybris.platform.cockpit.services.xmlprovider.XmlDataProvider;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.servicelayer.i18n.I18NService;
import java.util.HashSet;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class LocalizedXmlDataProvider implements XmlDataProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(LocalizedXmlDataProvider.class);
    private I18NService i18service;


    public Object generateAsXml(EditorSectionConfiguration editorSection, TypedObject curObj)
    {
        return null;
    }


    public Object generateAsXml(EditorRowConfiguration editorRow, TypedObject curObj)
    {
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", null);
        DocumentBuilder docBuilder = null;
        try
        {
            dbfac.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
            dbfac.setAttribute("http://javax.xml.XMLConstants/property/accessExternalSchema", "");
            dbfac.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            docBuilder = dbfac.newDocumentBuilder();
        }
        catch(ParserConfigurationException e)
        {
            LOG.error("", e);
        }
        Document doc = docBuilder.newDocument();
        Element row = doc.createElement("root");
        row.setAttribute("type", "localized");
        Element attribute = doc.createElement("attribute");
        PropertyDescriptor descriptor = editorRow.getPropertyDescriptor();
        attribute.setAttribute("name", getName(descriptor));
        row.appendChild(attribute);
        createLanguagesRows(descriptor, curObj, attribute, doc);
        return row;
    }


    private void createLanguagesRows(PropertyDescriptor descriptor, TypedObject curObj, Element attribute, Document doc)
    {
        Set<PropertyDescriptor> porpDesc = new HashSet<>();
        porpDesc.add(descriptor);
        Set<LanguageModel> langs = getI18service().getAllActiveLanguages();
        Set<String> langsISOs = getIso(langs);
        for(String iso : langsISOs)
        {
            Element lang = doc.createElement("language");
            lang.setAttribute("name", iso);
            lang.setAttribute("group", "1");
            lang.setTextContent(getLocalizedValue(curObj, descriptor, iso));
            attribute.appendChild(lang);
        }
    }


    private Set<String> getIso(Set<LanguageModel> langs)
    {
        Set<String> isos = new HashSet<>();
        for(LanguageModel languageModel : langs)
        {
            isos.add(languageModel.getIsocode());
        }
        return isos;
    }


    private String getName(PropertyDescriptor descriptor)
    {
        String name = descriptor.getName();
        if(name == null)
        {
            name = descriptor.getQualifier();
        }
        return name;
    }


    protected String getLocalizedValue(TypedObject curObj, PropertyDescriptor descriptor, String iso)
    {
        DefaultValueHandler defaultValueHandler = new DefaultValueHandler(descriptor);
        Object value = null;
        try
        {
            value = defaultValueHandler.getValue(curObj, iso);
        }
        catch(ValueHandlerException e)
        {
            LOG.error("", (Throwable)e);
        }
        return (value == null) ? "-" : value.toString();
    }


    @Deprecated
    public void setValueService(ValueService valueService)
    {
    }


    public I18NService getI18service()
    {
        return this.i18service;
    }


    public void setI18service(I18NService i18service)
    {
        this.i18service = i18service;
    }
}
