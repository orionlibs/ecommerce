package de.hybris.platform.commons.translator;

import de.hybris.platform.commons.translator.parsers.AbstractParser;
import de.hybris.platform.commons.translator.parsers.HtmlSimpleParser;
import de.hybris.platform.commons.translator.prerenderers.Prerenderer;
import de.hybris.platform.commons.translator.renderers.AbstractRenderer;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class TranslatorConfiguration
{
    Map<String, AbstractParser> parsersMap;
    RenderersFactory renderersFactory;
    List<Prerenderer> prerendersList = new ArrayList<>();
    private static final String DEFAULT_RENDERER = "defaultRenderer";
    private static final Logger LOG = Logger.getLogger(TranslatorConfiguration.class);
    private static final String EXPRESSION_ELEMENT = "expression";
    private static final String NAME_ELEMENT = "name";
    private static final String START_ELEMENT = "start";
    private static final String END_ELEMENT = "end";
    private static final String PARSE_CLASS_ELEMENT = "parseClass";


    public TranslatorConfiguration(InputStream parsersConfiguration, RenderersFactory renderersFactory)
    {
        this.parsersMap = createParsersMap(parsersConfiguration);
        this.renderersFactory = renderersFactory;
    }


    public TranslatorConfiguration(Map<String, AbstractParser> parsersConfiguration, RenderersFactory renderersFactory)
    {
        this.parsersMap = parsersConfiguration;
        this.renderersFactory = renderersFactory;
    }


    private Map createParsersMap(InputStream inputStream)
    {
        if(inputStream == null)
        {
            return null;
        }
        Map<String, AbstractParser> map = new HashMap<>();
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document doc = documentBuilder.parse(inputStream);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("expression");
            for(int s = 0; s < nodeLst.getLength(); s++)
            {
                String name = null;
                String parseClass = null;
                String start = null;
                String end = null;
                Node fstNode = nodeLst.item(s);
                if(fstNode.getNodeType() == 1)
                {
                    Element element = (Element)fstNode;
                    name = element.getAttribute("name");
                    if("".equals(name))
                    {
                        NodeList nameElementList = element.getElementsByTagName("name");
                        Element nameElement = (Element)nameElementList.item(0);
                        NodeList nameNodeList = nameElement.getChildNodes();
                        name = nameNodeList.item(0).getNodeValue();
                    }
                    NodeList startElementList = element.getElementsByTagName("start");
                    Element startElement = (Element)startElementList.item(0);
                    if(startElement != null)
                    {
                        NodeList startNodeList = startElement.getChildNodes();
                        start = startNodeList.item(0).getNodeValue();
                    }
                    NodeList endElementList = element.getElementsByTagName("end");
                    Element endElement = (Element)endElementList.item(0);
                    if(endElement != null)
                    {
                        NodeList endNodeList = endElement.getChildNodes();
                        end = endNodeList.item(0).getNodeValue();
                    }
                    if(end == null && start != null)
                    {
                        end = start;
                    }
                    parseClass = element.getAttribute("parseClass");
                    if("".equals(parseClass))
                    {
                        NodeList parseElementList = element.getElementsByTagName("parseClass");
                        Element parseElement = (Element)parseElementList.item(0);
                        if(parseElement != null)
                        {
                            NodeList parseNodeList = parseElement.getChildNodes();
                            parseClass = parseNodeList.item(0).getNodeValue();
                        }
                    }
                    AbstractParser abstractParser = createParser(parseClass, name, start, end);
                    map.put(abstractParser.getStartExpression(), abstractParser);
                }
            }
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
        }
        return map;
    }


    private AbstractParser createParser(String parseClass, String name, String start, String end)
    {
        if(parseClass != null && !parseClass.equals(""))
        {
            try
            {
                AbstractParser parser = (AbstractParser)Class.forName(parseClass).newInstance();
                parser.setName(name);
                parser.setStart(start);
                parser.setEnd(end);
                return parser;
            }
            catch(InstantiationException | IllegalAccessException e)
            {
                throw new IllegalArgumentException("Cannot create parser " + parseClass + " due to " + e.getMessage(), e);
            }
            catch(ClassNotFoundException e)
            {
                throw new IllegalArgumentException("Unknown parser class " + parseClass, e);
            }
        }
        return (AbstractParser)new HtmlSimpleParser(name, start, end);
    }


    public AbstractParser getParser(String name)
    {
        return this.parsersMap.get(name);
    }


    public AbstractRenderer getRenderer(String name)
    {
        return this.renderersFactory.get(name);
    }


    public Set<String> getParsers()
    {
        return this.parsersMap.keySet();
    }


    public Set<String> getRenderers()
    {
        return this.renderersFactory.keySet();
    }


    public AbstractRenderer getDefaultRenderer()
    {
        return getRenderer("defaultRenderer");
    }


    public void addPrerenderer(Prerenderer prerenderer)
    {
        this.prerendersList.add(prerenderer);
    }


    public List<Prerenderer> getPrerendersList()
    {
        return this.prerendersList;
    }


    public void setPrerendersList(List<Prerenderer> prerendersList)
    {
        this.prerendersList = prerendersList;
    }
}
