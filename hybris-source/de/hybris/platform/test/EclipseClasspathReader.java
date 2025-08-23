package de.hybris.platform.test;

import de.hybris.bootstrap.config.ExtensionInfo;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

class EclipseClasspathReader
{
    public static EclipseClasspath readClasspath(ExtensionInfo extensionInfo) throws Exception
    {
        File extensionInfoFile = new File(extensionInfo.getExtensionDirectory(), ".classpath");
        try
        {
            EclipseClasspath eclipseClasspath = new EclipseClasspath(extensionInfo);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(extensionInfoFile);
            Element classpath = doc.getDocumentElement();
            NodeList childs = classpath.getChildNodes();
            for(int i = 0; i < childs.getLength(); i++)
            {
                Node classpathentryNode = childs.item(i);
                if(classpathentryNode.getNodeName().equals("classpathentry"))
                {
                    ClasspathEntry entry = new ClasspathEntry();
                    for(int j = 0; j < classpathentryNode.getAttributes().getLength(); j++)
                    {
                        Node attribute = classpathentryNode.getAttributes().item(j);
                        if(attribute.getNodeName().equals("kind"))
                        {
                            entry.kind = attribute.getNodeValue();
                        }
                        if(attribute.getNodeName().equals("path"))
                        {
                            entry.path = attribute.getNodeValue();
                        }
                        if(attribute.getNodeName().equals("exported"))
                        {
                            entry.exported = Boolean.parseBoolean(attribute.getNodeValue());
                        }
                        if(attribute.getNodeName().equals("combineaccessrules"))
                        {
                            entry.combineaccessrules = Boolean.parseBoolean(attribute.getNodeValue());
                        }
                    }
                    eclipseClasspath.addClasspathEntry(entry);
                }
            }
            return eclipseClasspath;
        }
        catch(Exception e)
        {
            throw new Exception("Error while reading classpath file : " + extensionInfoFile.getAbsolutePath(), e);
        }
    }
}
