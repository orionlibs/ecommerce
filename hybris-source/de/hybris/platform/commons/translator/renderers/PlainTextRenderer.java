package de.hybris.platform.commons.translator.renderers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;

public class PlainTextRenderer extends AbstractRenderer
{
    public String renderTextFromNode(AbstractNode node, Translator translator)
    {
        String text = "";
        if(node.getNodeName().equals("newLine"))
        {
            text = "\n";
        }
        else if(node.getNodeName().equals("div") || node.getNodeName().equals("paragraph") || node
                        .getNodeName().startsWith("heading"))
        {
            text = "\n" + translator.renderContent(node) + "\n";
        }
        else if(node.getNodeName().equals("bold") || node.getNodeName().equals("italic") || node.getNodeName()
                        .equals("underline") || node
                        .getNodeName().equals("strike") || node.getNodeName().equals("strong") || node
                        .getNodeName().equals("superscript") || node.getNodeName().equals("subscript") || node
                        .getNodeName().equals("span"))
        {
            text = translator.renderContent(node);
        }
        return text;
    }
}
