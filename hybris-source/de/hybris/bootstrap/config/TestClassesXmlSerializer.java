package de.hybris.bootstrap.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class TestClassesXmlSerializer
{
    public Set<String> readModuleTests(ExtensionInfo extensionInfo, ExtensionModule extensionModule, ClassLoader classLoader)
    {
        String resourceString = getDumpFileNameForRead(extensionInfo, extensionModule.getTestClassFileName());
        return readFromDumpFile(extensionInfo, classLoader, resourceString);
    }


    private String getDumpFileNameForRead(ExtensionInfo extensionInfo, String dumpFileNamePostfix)
    {
        StringBuilder builder = new StringBuilder();
        builder.append(extensionInfo.getName()).append('/').append(extensionInfo.getName());
        builder.append('-').append(dumpFileNamePostfix).append(".xml");
        return builder.toString();
    }


    private Set<String> readFromDumpFile(ExtensionInfo extensionInfo, ClassLoader classLoader, String resourceString)
    {
        Set<String> testclasses = new LinkedHashSet<>();
        InputStream inputStream = null;
        try
        {
            inputStream = getResource(classLoader, resourceString);
            if(inputStream != null)
            {
                XPath xPath;
                InputSource inputSource = new InputSource(inputStream);
                ClassLoader ctxClassLoaderBefore = Thread.currentThread().getContextClassLoader();
                try
                {
                    if(ctxClassLoaderBefore == null)
                    {
                        Thread.currentThread().setContextClassLoader(
                                        (classLoader == null) ? getClass().getClassLoader() : classLoader);
                    }
                    xPath = XPathFactory.newInstance().newXPath();
                }
                finally
                {
                    Thread.currentThread().setContextClassLoader(ctxClassLoaderBefore);
                }
                String pathExpr = "//testclasses/" + extensionInfo.getName() + "/testclass";
                try
                {
                    NodeList nodes = (NodeList)xPath.evaluate(pathExpr, inputSource, XPathConstants.NODESET);
                    for(int i = 0; i < nodes.getLength(); i++)
                    {
                        testclasses.add(nodes.item(i).getTextContent());
                    }
                }
                catch(Exception e)
                {
                    System.err
                                    .println("Error parsing " + resourceString + " : " + e
                                                    .getMessage() + " - please run ant clean all to make sure that no stale *-testclasses.xml files are left from previous builds.");
                }
            }
            else
            {
                System.err.println("No test class file '" + resourceString + "' in class loader " + classLoader);
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }
        finally
        {
            closeStreamIfNeeded(inputStream);
        }
        return testclasses;
    }


    public InputStream getResource(ClassLoader classLoader, String resourceString)
    {
        return classLoader.getResourceAsStream(resourceString);
    }


    private void closeStreamIfNeeded(InputStream inputStream)
    {
        if(inputStream != null)
        {
            try
            {
                inputStream.close();
            }
            catch(IOException e)
            {
                throw new IllegalStateException(e);
            }
        }
    }


    public void saveModuleTests(ExtensionInfo extensionInfo, ExtensionModule extensionModule, Set<String> testclassesFound)
    {
        File testclassesFile = getDumpFileName(extensionInfo, extensionModule.getTestClassFileName());
        saveDumpFile(extensionInfo, testclassesFound, testclassesFile);
    }


    private void saveDumpFile(ExtensionInfo extensionInfo, Set<String> testclassesFound, File testclassesFile)
    {
        XMLOutputFactory xof = XMLOutputFactory.newInstance();
        XMLStreamWriter xtw = null;
        try
        {
            xtw = xof.createXMLStreamWriter(new OutputStreamWriter(new FileOutputStream(testclassesFile), "utf-8"));
            writeXML(xtw, extensionInfo, testclassesFound);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            closeWriterIfNeeded(xtw);
        }
    }


    private void writeXML(XMLStreamWriter xtw, ExtensionInfo extensionInfo, Set<String> testclassesFound) throws XMLStreamException, IOException
    {
        xtw.writeStartDocument("utf-8", "1.0");
        xtw.writeStartElement("testclasses");
        xtw.writeStartElement(extensionInfo.getName());
        for(String classname : testclassesFound)
        {
            xtw.writeStartElement("testclass");
            xtw.writeCharacters(classname);
            xtw.writeEndElement();
        }
        xtw.writeEndElement();
        xtw.writeEndElement();
        xtw.writeEndDocument();
        xtw.flush();
    }


    private File getDumpFileName(ExtensionInfo extensionInfo, String dumpFileNamePostfix)
    {
        StringBuilder builder = new StringBuilder("resources/");
        builder.append(extensionInfo.getName()).append('/').append(extensionInfo.getName());
        builder.append('-').append(dumpFileNamePostfix).append(".xml");
        return new File(extensionInfo.getExtensionDirectory(), builder.toString());
    }


    private void closeWriterIfNeeded(XMLStreamWriter xmlWriter)
    {
        if(xmlWriter != null)
        {
            try
            {
                xmlWriter.close();
            }
            catch(Exception e)
            {
                throw new IllegalStateException(e);
            }
        }
    }
}
