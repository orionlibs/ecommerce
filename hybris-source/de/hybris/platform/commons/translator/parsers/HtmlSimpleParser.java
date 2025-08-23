package de.hybris.platform.commons.translator.parsers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;
import de.hybris.platform.commons.translator.nodes.SimpleNode;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlSimpleParser extends AbstractParser
{
    public HtmlSimpleParser()
    {
    }


    public HtmlSimpleParser(String name, String start, String end)
    {
        super(name, start, end);
    }


    public AbstractNode createNode(String start, String end, String text, Translator translator)
    {
        SimpleNode simpleNode = new SimpleNode(this.name, text);
        simpleNode.setAttributes(generateAttributes(start));
        simpleNode.addChildNodes(translator.parseText(simpleNode.getNodeText()));
        return (AbstractNode)simpleNode;
    }


    public String getStartExpression()
    {
        return "<\\s*" + this.start + "(\\s([^>])*)?/?>";
    }


    public String getStartEndExpression()
    {
        return "<\\s*" + this.start + "(\\s([^>])*)?/>";
    }


    public String getEndExpression(String start)
    {
        return "</\\s*" + this.end + "\\s*>";
    }


    public Map generateAttributes(String tag)
    {
        Map<Object, Object> attributesMap = new HashMap<>();
        String attributePattern = "\\s(([.[^\"]])+)\\s*=\\s*\"(([.[^\"]])*)\"";
        Pattern pattern = Pattern.compile("\\s(([.[^\"]])+)\\s*=\\s*\"(([.[^\"]])*)\"", 42);
        Matcher matcher = pattern.matcher(tag);
        while(matcher.find())
        {
            attributesMap.put(matcher.group(1).trim(), (matcher.group(3) == null) ? "" : matcher.group(3).trim());
        }
        return attributesMap;
    }
}
