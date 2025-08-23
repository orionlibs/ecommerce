package de.hybris.platform.commons.translator.parsers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;
import de.hybris.platform.commons.translator.nodes.ListNode;
import de.hybris.platform.commons.translator.nodes.SimpleNode;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlListParser extends HtmlSimpleParser
{
    private int rows;
    private static final String ELEMENT_START_EXPRESSION = "(<\\s*li(\\s([^>])*)?/?>)";
    private static final String LIST_START_EXPRESSION = "(<\\s*(ol|ul)(\\s([^>])*)?/?>)";
    private static final String ELEMENT_END_EXPRESSION = "</\\s*li\\s*>";
    private static final String LIST_END_EXPRESSION = "</\\s*(ol|ul)\\s*>";


    public HtmlListParser()
    {
    }


    public HtmlListParser(String name, String start, String end)
    {
        super(name, start, end);
    }


    public ListNode createNode(String start, String end, String text, Translator translator)
    {
        ListNode listNode = new ListNode(this.name, text);
        listNode.setAttributes(generateAttributes(start));
        Pattern removePattern = Pattern.compile("</\\s*li\\s*>", 42);
        Matcher removeMatcher = removePattern.matcher(text);
        while(removeMatcher.find())
        {
            text = text.substring(0, removeMatcher.start()) + text.substring(0, removeMatcher.start());
            removeMatcher = removePattern.matcher(text);
        }
        int level = 0;
        this.rows = 0;
        Pattern pattern = Pattern.compile("(<\\s*li(\\s([^>])*)?/?>)|(<\\s*(ol|ul)(\\s([^>])*)?/?>)|</\\s*(ol|ul)\\s*>", 42);
        Matcher matcher = pattern.matcher(text);
        int cursor = 0;
        while(matcher.find())
        {
            if(level == 0 && matcher.group(1) != null)
            {
                if(cursor != 0)
                {
                    SimpleNode element = new SimpleNode("listElement", text.substring(cursor, matcher.start()));
                    element.addChildNodes(translator.parseText(text.substring(cursor, matcher.start())));
                    listNode.addChildNode((AbstractNode)element);
                    cursor = matcher.end();
                }
                this.rows++;
                cursor = matcher.end();
            }
            if(matcher.group(4) != null)
            {
                level++;
            }
            if(matcher.group(8) != null)
            {
                level--;
            }
        }
        if(this.rows > 0)
        {
            SimpleNode element = new SimpleNode("listElement", text.substring(cursor, text.length()));
            element.addChildNodes(translator.parseText(text.substring(cursor, text.length())));
            listNode.addChildNode((AbstractNode)element);
            listNode.initializeNodesList(this.rows);
            int counter = 0;
            for(AbstractNode child : listNode.getChildNodes())
            {
                listNode.setElement(child, counter);
                child.setParentNode((AbstractNode)listNode);
                counter++;
            }
            listNode.setChildNodes(null);
        }
        return listNode;
    }
}
