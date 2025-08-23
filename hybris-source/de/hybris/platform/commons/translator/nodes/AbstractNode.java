package de.hybris.platform.commons.translator.nodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractNode
{
    public static final String OPEN_TAG = "openTag";
    String nodeName;
    String nodeText;
    AbstractNode parentNode;
    List<AbstractNode> childNodes = new ArrayList<>();
    Map<String, String> attributes;


    public AbstractNode(String nodeName, String nodeText)
    {
        this.nodeName = nodeName;
        this.nodeText = nodeText;
        this.attributes = new HashMap<>();
    }


    public AbstractNode(String nodeName, String nodeText, Map<String, String> generateAttributes)
    {
        this.nodeName = nodeName;
        this.nodeText = nodeText;
        this.attributes = generateAttributes;
    }


    public String getNodeName()
    {
        return this.nodeName;
    }


    public void setNodeName(String nodeName)
    {
        this.nodeName = nodeName;
    }


    public String getNodeText()
    {
        return this.nodeText;
    }


    public void setNodeText(String nodeText)
    {
        this.nodeText = nodeText;
    }


    public AbstractNode getParentNode()
    {
        return this.parentNode;
    }


    public void setParentNode(AbstractNode parentNode)
    {
        this.parentNode = parentNode;
    }


    public List<AbstractNode> getChildNodes()
    {
        return this.childNodes;
    }


    public AbstractNode getChildNodeByName(String nodeName)
    {
        for(AbstractNode childNode : this.childNodes)
        {
            String childNodeName = childNode.getNodeName();
            if(childNodeName != null && childNodeName.equals(nodeName))
            {
                return childNode;
            }
        }
        return null;
    }


    public void setChildNodes(List<AbstractNode> childNodes)
    {
        this.childNodes.clear();
        addChildNodes(childNodes);
    }


    public void addChildNodes(List<AbstractNode> childNodes)
    {
        if(childNodes != null && !childNodes.isEmpty())
        {
            this.nodeText = null;
            for(AbstractNode child : childNodes)
            {
                addChildNode(child);
            }
        }
    }


    public void addChildNode(AbstractNode child)
    {
        if(child != null)
        {
            this.nodeText = null;
            this.childNodes.add(child);
            child.setParentNode(this);
        }
    }


    public Map<String, String> getAttributes()
    {
        return this.attributes;
    }


    public String getAttributeByName(String attributeName)
    {
        return this.attributes.get(attributeName);
    }


    public void setAttributes(Map<String, String> attributes)
    {
        this.attributes = attributes;
    }


    public void addAttributes(Map<String, String> attributes)
    {
        if(attributes != null && !attributes.isEmpty())
        {
            for(Map.Entry<String, String> entry : attributes.entrySet())
            {
                addAttribute(entry.getKey(), entry.getValue());
            }
        }
    }


    public void addAttribute(String attributeName, String attributeValue)
    {
        this.attributes.put(attributeName, attributeValue);
    }


    public String getStyleClass()
    {
        String styleClass = "";
        if(this.attributes != null && this.attributes.containsKey("class"))
        {
            styleClass = this.attributes.get("class");
        }
        return styleClass;
    }


    public boolean isOpenTag()
    {
        return Boolean.parseBoolean(this.attributes.get("openTag"));
    }
}
