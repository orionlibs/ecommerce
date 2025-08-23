package de.hybris.platform.cockpit.util;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class DefaultCockpitTemplateParser implements CockpitTemplateParser
{
    private static final Logger LOG = LoggerFactory.getLogger(DefaultCockpitTemplateParser.class);


    public String parseTemplate(String template) throws IllegalArgumentException
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<zk xmlns=\"http://www.w3.org/1999/xhtml\"\n");
        stringBuilder.append("    xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
        stringBuilder.append("    xmlns:z=\"http://www.zkoss.org/2005/zul\"\n");
        stringBuilder.append("    xmlns:zk=\"http://www.zkoss.org/2005/zk\"\n");
        stringBuilder.append("    xsi:schemaLocation=\"http://www.zkoss.org/2005/zul http://www.zkoss.org/2005/zul/zul.xsd\">\n");
        String injectionString = null;
        injectionString = getInjectionString(template);
        if(StringUtils.isBlank(injectionString))
        {
            throw new IllegalArgumentException("Injection string is empty.");
        }
        stringBuilder.append(injectionString);
        stringBuilder.append("</zk>\n");
        return stringBuilder.toString();
    }


    protected String getInjectionString(String template) throws IllegalArgumentException
    {
        String ret = null;
        if(StringUtils.isBlank(template))
        {
            throw new IllegalArgumentException("Parsing of template aborted. Reason: Template string is empty.");
        }
        try
        {
            long start = System.currentTimeMillis();
            Document document = stringToDocument(template);
            ret = documentToString(document);
            NodeList cockpitNodeSet = findNodes(document, "//cockpit");
            int index = 0;
            for(; index < cockpitNodeSet.getLength(); index++)
            {
                String code = "";
                String value = "";
                Node cockpitNode = cockpitNodeSet.item(index);
                NamedNodeMap attributes = cockpitNode.getAttributes();
                if(attributes == null)
                {
                    LOG.warn("Cockpit node has no attributes");
                }
                else
                {
                    Node codeNode = attributes.getNamedItem("code");
                    if(codeNode == null)
                    {
                        throw new IllegalArgumentException("Template not valid. cockpit has no code attribute");
                    }
                    code = codeNode.getNodeValue();
                    Node valueNode = attributes.getNamedItem("value");
                    if(valueNode != null)
                    {
                        value = valueNode.getNodeValue();
                        value = StringUtils.isBlank(value) ? "" : value;
                    }
                    Map<String, Object> creationParams = new HashMap<>();
                    creationParams.put("code", code);
                    creationParams.put("value", value);
                    Node attributesNode = attributes.getNamedItem("attrs");
                    if(attributesNode != null)
                    {
                        String attributesNodeValue = attributesNode.getNodeValue();
                        if(StringUtils.isNotBlank(attributesNodeValue))
                        {
                            String[] attrNames = StringUtils.split(attributesNodeValue, ',');
                            for(String attrName : attrNames)
                            {
                                Node attrNode = attributes.getNamedItem(attrName);
                                if(attrNode != null)
                                {
                                    String attrValue = attrNode.getNodeValue();
                                    if(StringUtils.isNotBlank(attrValue))
                                    {
                                        creationParams.put(attrName, attrValue);
                                    }
                                }
                            }
                        }
                    }
                    cockpitNode.getParentNode().replaceChild(getReplacementNode(document, creationParams), cockpitNode);
                    ret = documentToString(document);
                }
            }
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Parsing of template done. Parsed " + index + " cockpit tag" + ((index == 1) ? "" : "s") + " (" +
                                System.currentTimeMillis() - start + " ms).");
            }
        }
        catch(ParserConfigurationException pce)
        {
            throw new IllegalArgumentException("Could not create new document builder.", pce);
        }
        catch(SAXException saxe)
        {
            throw new IllegalArgumentException("Could not parse template.", saxe);
        }
        catch(IOException ioe)
        {
            throw new IllegalArgumentException("Error occurred while parsing template.", ioe);
        }
        catch(XPathExpressionException xpee)
        {
            throw new IllegalArgumentException("Error occurred while parsing nodes.", xpee);
        }
        return ret;
    }


    protected Document stringToDocument(String text) throws SAXException, IOException, ParserConfigurationException
    {
        Document document = null;
        StringReader stringReader = null;
        try
        {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", null);
            docBuilderFactory.setValidating(false);
            docBuilderFactory.setCoalescing(false);
            docBuilderFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
            docBuilderFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalSchema", "");
            docBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
            DocumentBuilder documentBuilder = docBuilderFactory.newDocumentBuilder();
            stringReader = new StringReader(text);
            document = documentBuilder.parse(new InputSource(stringReader));
        }
        finally
        {
            if(stringReader != null)
            {
                stringReader.close();
            }
        }
        return document;
    }


    protected String documentToString(Document document)
    {
        String docString = null;
        try
        {
            Source source = new DOMSource(document.getFirstChild());
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl", null);
            factory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
            factory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalStylesheet", "");
            Transformer transformer = factory.newTransformer();
            transformer.setOutputProperty("standalone", "no");
            transformer.setOutputProperty("omit-xml-declaration", "yes");
            transformer.transform(source, result);
            docString = stringWriter.getBuffer().toString();
        }
        catch(TransformerConfigurationException tce)
        {
            LOG.error(tce.getMessage(), tce);
        }
        catch(TransformerException te)
        {
            LOG.error(te.getMessage(), te);
        }
        return docString;
    }


    protected Node getReplacementNode(Document document, Map<String, Object> params)
    {
        Node divNode = document.createElement("z:div");
        Element attrElement = document.createElement("z:attribute");
        divNode.appendChild(attrElement);
        attrElement.setAttribute("name", "onCreate");
        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("      java.util.Map params = new java.util.HashMap();\n");
        for(String key : params.keySet())
        {
            strBuilder.append("      params.put(\"" + key + "\", \"" + params.get(key) + "\");\n");
        }
        strBuilder.append("      event.getArg().get(\"injector\").injectComponent(self, params);\n");
        attrElement.setTextContent(strBuilder.toString());
        return divNode;
    }


    private NodeList findNodes(Object obj, String xPathString) throws XPathExpressionException
    {
        XPath xPath = XPathFactory.newInstance().newXPath();
        XPathExpression expression = xPath.compile(xPathString);
        return (NodeList)expression.evaluate(obj, XPathConstants.NODESET);
    }
}
