package de.hybris.platform.cockpit.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.SortedSet;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ItemsToPropertiesConverter
{
    private static final Logger LOG = LoggerFactory.getLogger(ItemsToPropertiesConverter.class);
    private static final String OUTPUT_FILE_NAME = "test.properties";
    private final List<String> usedPrefixes = new ArrayList<>();
    private static final List<String> additionalQualifiers = new LinkedList<>();
    private static final String ITEMS_XML_FILE_NAME = "ext/cockpit/resources/cockpit-items.xml";
    private static final String PROPERTY_FILE_NAME = "ext/cockpit/resources/localization/cockpit-locales_de.properties";


    public static void main(String[] args)
    {
        try
        {
            (new ItemsToPropertiesConverter()).convert();
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
        }
    }


    private void convert() throws Exception
    {
        Properties properties = new Properties();
        properties.load(new FileInputStream("ext/cockpit/resources/localization/cockpit-locales_de.properties"));
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance("com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl", null);
        docBuilderFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
        docBuilderFactory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalSchema", "");
        docBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", true);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(new File("ext/cockpit/resources/cockpit-items.xml"));
        PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("test.properties")));
        try
        {
            handleEnumTypes(properties, doc, out);
            handleRelations(properties, doc, out);
            handleItemsTypes(properties, doc, out);
        }
        finally
        {
            out.flush();
            IOUtils.closeQuietly(out);
        }
        handleNotUsedProperties(properties);
    }


    private void handleRelations(Properties properties, Document doc, PrintWriter out)
    {
        Hashtable<String, String> keyMap = getPropertiesKeyMap(properties);
        NodeList relationList = doc.getElementsByTagName("relation");
        LOG.info("Total no of relations : " + relationList.getLength());
        for(int ii = 0; ii < relationList.getLength(); ii++)
        {
            Node relation = relationList.item(ii);
            String relationCode = getAttributeFromNode(relation, "code");
            out.println();
            out.println("### Localization for " + relationCode);
            out.println();
            NodeList valueList = relation.getChildNodes();
            String sourceCode = null, sourceQualifier = null, targetCode = null, targetQualifier = null;
            for(int jj = 0; jj < valueList.getLength(); jj++)
            {
                Node node = valueList.item(jj);
                if(node.getNodeType() == 1 && "sourceElement".equals(node.getNodeName()))
                {
                    sourceCode = getAttributeFromNode(node, "type");
                    sourceQualifier = getAttributeFromNode(node, "qualifier");
                }
                if(node.getNodeType() == 1 && "targetElement".equals(node.getNodeName()))
                {
                    targetCode = getAttributeFromNode(node, "type");
                    targetQualifier = getAttributeFromNode(node, "qualifier");
                }
            }
            StringBuffer prefix = new StringBuffer();
            prefix.append("type.").append(sourceCode).append('.').append(targetQualifier);
            String value = getValue(properties, keyMap, prefix.toString(), "name");
            println("" + prefix + ".name", value, false, out);
            prefix = new StringBuffer();
            prefix.append("type.").append(targetCode).append('.').append(sourceQualifier);
            value = getValue(properties, keyMap, prefix.toString(), "name");
            println("" + prefix + ".name", value, false, out);
        }
    }


    private void handleEnumTypes(Properties properties, Document doc, PrintWriter out)
    {
        Hashtable<String, String> keyMap = getPropertiesKeyMap(properties);
        NodeList enumTypeList = doc.getElementsByTagName("enumtype");
        LOG.info("Total no of enumtypes : " + enumTypeList.getLength());
        for(int ii = 0; ii < enumTypeList.getLength(); ii++)
        {
            Node enumType = enumTypeList.item(ii);
            String enumTypeCode = getAttributeFromNode(enumType, "code");
            out.println();
            out.println("### Localization for type " + enumTypeCode + " values");
            out.println();
            StringBuffer prefix = new StringBuffer();
            prefix.append("type.").append(enumTypeCode);
            String value = getValue(properties, keyMap, prefix.toString(), "name");
            println("" + prefix + ".name", value, false, out);
            NodeList valueList = enumType.getChildNodes();
            for(int jj = 0; jj < valueList.getLength(); jj++)
            {
                Node node = valueList.item(jj);
                if(node.getNodeType() == 1)
                {
                    String valueCode = getAttributeFromNode(node, "code");
                    prefix = new StringBuffer();
                    prefix.append("type.").append(enumTypeCode).append('.').append(valueCode);
                    value = getValue(properties, keyMap, prefix.toString(), "name");
                    println("" + prefix + ".name", value, false, out);
                    value = getValue(properties, keyMap, prefix.toString(), "description");
                    println("" + prefix + ".description", value, false, out);
                }
            }
        }
    }


    private void handleItemsTypes(Properties properties, Document doc, PrintWriter out)
    {
        Hashtable<String, String> keyMap = getPropertiesKeyMap(properties);
        NodeList itemTypeList = doc.getElementsByTagName("itemtype");
        LOG.info("Total no of itemtypes : " + itemTypeList.getLength());
        for(int ii = 0; ii < itemTypeList.getLength(); ii++)
        {
            Node itemType = itemTypeList.item(ii);
            String itemTypeCode = getAttributeFromNode(itemType, "code");
            out.println();
            out.println("### Localization for " + itemTypeCode + " item");
            out.println();
            StringBuffer prefix = new StringBuffer();
            prefix.append("type.").append(itemTypeCode);
            String value = getValue(properties, keyMap, prefix.toString(), "name");
            println("" + prefix + ".name", value, false, out);
            value = getValue(properties, keyMap, prefix.toString(), "description");
            println("" + prefix + ".description", value, false, out);
            out.println();
            NodeList childList = itemType.getChildNodes();
            for(int kk = 0; kk < childList.getLength(); kk++)
            {
                Node child = childList.item(kk);
                if(child.getNodeType() == 1 && "attributes".equals(child.getNodeName()))
                {
                    NodeList attributeList = child.getChildNodes();
                    for(int jj = 0; jj < attributeList.getLength(); jj++)
                    {
                        Node node = attributeList.item(jj);
                        if(node.getNodeType() == 1)
                        {
                            String qualifier = getAttributeFromNode(node, "qualifier");
                            prefix = new StringBuffer();
                            prefix.append("type.").append(itemTypeCode).append('.').append(
                                            StringUtils.substring(qualifier, 0, 1).toUpperCase()).append(StringUtils.substring(qualifier, 1));
                            value = getValue(properties, keyMap, prefix.toString(), "name");
                            println("" + prefix + ".name", value, false, out);
                            value = getValue(properties, keyMap, prefix.toString(), "description");
                            println("" + prefix + ".description", value, false, out);
                            value = getValue(properties, keyMap, prefix.toString(), "defaultvalue");
                            println("" + prefix + ".defaultvalue", value, true, out);
                        }
                    }
                }
            }
            for(String qualifier : additionalQualifiers)
            {
                prefix = new StringBuffer();
                prefix.append("type.").append(itemTypeCode).append('.').append(StringUtils.substring(qualifier, 0, 1).toUpperCase())
                                .append(StringUtils.substring(qualifier, 1));
                value = getValue(properties, keyMap, prefix.toString(), "name");
                println("" + prefix + ".name", value, true, out);
                value = getValue(properties, keyMap, prefix.toString(), "description");
                println("" + prefix + ".description", value, true, out);
                value = getValue(properties, keyMap, prefix.toString(), "defaultvalue");
                println("" + prefix + ".defaultvalue", value, true, out);
            }
        }
    }


    private void println(String key, String value, boolean onlyNonEmptyValues, PrintWriter out)
    {
        if(!onlyNonEmptyValues || StringUtils.isNotEmpty(value))
        {
            out.println(key + "=" + key);
        }
    }


    private String getValue(Properties properties, Hashtable<String, String> keyMap, String prefix, String suffix)
    {
        String ret = "";
        String key = prefix + "." + suffix;
        if(keyMap.containsKey(key.toLowerCase()))
        {
            ret = properties.getProperty(keyMap.get(key.toLowerCase()));
            this.usedPrefixes.add(key.toLowerCase());
        }
        return ret;
    }


    private Hashtable<String, String> getPropertiesKeyMap(Properties properties)
    {
        Hashtable<String, String> ret = new Hashtable<>();
        for(Object obj : properties.keySet())
        {
            String key = (String)obj;
            ret.put(key.toLowerCase(), key);
        }
        return ret;
    }


    private void handleNotUsedProperties(Properties properties)
    {
        SortedSet<String> set = new TreeSet<>();
        for(Object obj : properties.keySet())
        {
            String key = (String)obj;
            if(!this.usedPrefixes.contains(key.toLowerCase()))
            {
                set.add(key + "=" + key);
            }
        }
        for(String output : set)
        {
            LOG.info(output);
        }
    }


    private String getAttributeFromNode(Node node, String attributeName)
    {
        return node.getAttributes().getNamedItem(attributeName).getTextContent();
    }
}
