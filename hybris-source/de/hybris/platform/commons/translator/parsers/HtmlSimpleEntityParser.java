package de.hybris.platform.commons.translator.parsers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;
import de.hybris.platform.commons.translator.nodes.SimpleNode;

public class HtmlSimpleEntityParser extends AbstractParser
{
    public AbstractNode createNode(String start, String end, String text, Translator translator)
    {
        SimpleNode simpleNode = new SimpleNode(this.name, start);
        return (AbstractNode)simpleNode;
    }


    public String getEndExpression(String start)
    {
        return null;
    }


    public String getStartEndExpression()
    {
        return this.start;
    }


    public String getStartExpression()
    {
        return this.start;
    }
}
