package de.hybris.platform.commons.translator.nodes;

import java.util.Map;

public class SimpleNode extends AbstractNode
{
    public SimpleNode(String nodeName, String nodeText)
    {
        super(nodeName, nodeText);
    }


    public SimpleNode(String nodeName, String nodeText, Map<String, String> generateAttributes)
    {
        super(nodeName, nodeText, generateAttributes);
    }
}
