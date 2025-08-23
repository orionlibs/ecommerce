package de.hybris.platform.commons.translator;

import de.hybris.platform.commons.translator.renderers.AbstractRenderer;
import de.hybris.platform.commons.translator.renderers.EntityRenderer;
import de.hybris.platform.commons.translator.renderers.VelocityRenderer;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RenderersFactoryFromFile implements RenderersFactory
{
    private static final Logger LOG = Logger.getLogger(RenderersFactoryFromFile.class);
    private static final String NAME_ELEMENT = "name";
    private static final String RENDERER_START = "start";
    private static final String RENDERER_END = "end";
    private static final String RENDERER_ELEMENT = "renderer";
    private static final String VELOCITY_RENDERER_ELEMENT = "velocityRenderer";
    private static final String RENDERER_CLASS_ELEMENT = "rendererClass";
    private static final String RENDERER_TEMPLATE_NAME = "templateName";
    private static final String RENDERER_ENTITY_FILE_NAME = "entityFile";
    private static final String RENDERER_STRING_NAME = "template";
    private final Map<String, AbstractRenderer> renderersMap;
    InputStream renderersFileStream;
    InputStream propertiesFileStream;
    InputStream entitiesReplaceFileStream;


    public RenderersFactoryFromFile(InputStream renderersFileStream, InputStream propertiesFileStream)
    {
        this.renderersFileStream = renderersFileStream;
        this.propertiesFileStream = propertiesFileStream;
        this.renderersMap = createRenderersMap();
    }


    public RenderersFactoryFromFile(InputStream renderersFileStream, InputStream propertiesFileStream, InputStream entitiesReplaceFileStream)
    {
        this.renderersFileStream = renderersFileStream;
        this.propertiesFileStream = propertiesFileStream;
        this.entitiesReplaceFileStream = entitiesReplaceFileStream;
        this.renderersMap = createRenderersMap();
    }


    public Map<String, AbstractRenderer> createRenderersMap()
    {
        Map<String, AbstractRenderer> map = new HashMap<>();
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            dbf.setFeature("http://xml.org/sax/features/external-general-entities", false);
            DocumentBuilder documentBuilder = dbf.newDocumentBuilder();
            Document doc = documentBuilder.parse(this.renderersFileStream);
            doc.getDocumentElement().normalize();
            NodeList nodeLst = doc.getElementsByTagName("renderer");
            for(int s = 0; s < nodeLst.getLength(); s++)
            {
                String rendererClass = null;
                String name = null;
                String start = null;
                String end = null;
                String entityFile = null;
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
                    NodeList entityFileElementList = element.getElementsByTagName("entityFile");
                    Element entityFileElement = (Element)entityFileElementList.item(0);
                    if(entityFileElement != null)
                    {
                        NodeList entityFileNodeList = entityFileElement.getChildNodes();
                        entityFile = entityFileNodeList.item(0).getNodeValue();
                    }
                    rendererClass = element.getAttribute("rendererClass");
                    if("".equals(rendererClass))
                    {
                        NodeList rendererElementList = element.getElementsByTagName("rendererClass");
                        Element rendererElement = (Element)rendererElementList.item(0);
                        if(rendererElement != null)
                        {
                            NodeList rendererNodeList = rendererElement.getChildNodes();
                            rendererClass = rendererNodeList.item(0).getNodeValue();
                        }
                    }
                    AbstractRenderer abstractRenderer = createRenderer(rendererClass);
                    if(entityFile != null && abstractRenderer instanceof EntityRenderer)
                    {
                        InputStream entitiesReplaceFileStream = Translator.class.getClassLoader().getResourceAsStream(entityFile);
                        ((EntityRenderer)abstractRenderer).setEntitiesReplaceFileStream(entitiesReplaceFileStream);
                    }
                    if(abstractRenderer == null)
                    {
                        throw new IllegalStateException("abstractRenderer must not be null");
                    }
                    abstractRenderer.setStart(start);
                    abstractRenderer.setEnd(end);
                    map.put(name, abstractRenderer);
                }
            }
            nodeLst = doc.getElementsByTagName("velocityRenderer");
            Properties properties = new Properties();
            if(this.propertiesFileStream != null)
            {
                properties.load(this.propertiesFileStream);
            }
            for(int i = 0; i < nodeLst.getLength(); i++)
            {
                String name = null;
                String templateName = null;
                String templateString = null;
                Node fstNode = nodeLst.item(i);
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
                    NodeList templateNameElementList = element.getElementsByTagName("templateName");
                    Element templateNameElement = (Element)templateNameElementList.item(0);
                    if(templateNameElement != null)
                    {
                        NodeList templateNameNodeList = templateNameElement.getChildNodes();
                        templateName = templateNameNodeList.item(0).getNodeValue();
                    }
                    NodeList templateStringElementList = element.getElementsByTagName("template");
                    Element templateStringElement = (Element)templateStringElementList.item(0);
                    if(templateStringElement != null)
                    {
                        NodeList templateStringNodeList = templateStringElement.getChildNodes();
                        templateString = templateStringNodeList.item(0).getNodeValue();
                    }
                    VelocityRenderer velocityRenderer = new VelocityRenderer();
                    velocityRenderer.setTemplateName(templateName);
                    velocityRenderer.setProperties(properties);
                    velocityRenderer.setTemplate(templateString);
                    map.put(name, velocityRenderer);
                }
            }
        }
        catch(Exception e)
        {
            LOG.warn(e.getMessage(), e);
        }
        return map;
    }


    private AbstractRenderer createRenderer(String rendererClass)
    {
        if(rendererClass != null)
        {
            try
            {
                AbstractRenderer renderer = (AbstractRenderer)Class.forName(rendererClass).newInstance();
                return renderer;
            }
            catch(Exception ex)
            {
                LOG.error(ex.getMessage(), ex);
            }
        }
        return null;
    }


    public AbstractRenderer get(String name)
    {
        return this.renderersMap.get(name);
    }


    public Set<String> keySet()
    {
        return this.renderersMap.keySet();
    }
}
