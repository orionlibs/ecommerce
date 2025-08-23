package de.hybris.platform.cockpit.services.xmlprovider.impl;

import de.hybris.platform.cockpit.model.meta.TypedObject;
import de.hybris.platform.cockpit.services.config.EditorRowConfiguration;
import de.hybris.platform.cockpit.services.config.EditorSectionConfiguration;
import de.hybris.platform.cockpit.services.xmlprovider.XmlDataProvider;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustomXmlDataProvider implements XmlDataProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(CustomXmlDataProvider.class);


    public Object generateAsXml(EditorSectionConfiguration editorSection, TypedObject curObj)
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
        Element section = doc.createElement("root");
        section.setAttribute("type", XmlDataProvider.SECTION_TYPE.custom.toString());
        section.setAttribute("name", "My happy custom section");
        Element child = doc.createElement("child");
        child.setAttribute("name", "happy name 1");
        section.appendChild(child);
        Element child2 = doc.createElement("child");
        child2.setAttribute("name", "happy name 2");
        section.appendChild(child2);
        return section;
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
        row.setAttribute("type", XmlDataProvider.ROW_TYPE.custom.toString());
        row.setAttribute("name", "My happy custom row");
        Element child = doc.createElement("child");
        child.setAttribute("name", "First custom row child name");
        child.setAttribute("value", "First custom value");
        row.appendChild(child);
        Element child2 = doc.createElement("child");
        child2.setAttribute("name", "Second custom row child name");
        child2.setAttribute("value", "Second custom value");
        row.appendChild(child2);
        return row;
    }
}
