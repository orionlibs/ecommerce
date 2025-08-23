package de.hybris.platform.commons.translator.parsers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;

public abstract class AbstractParser
{
    protected String start;
    protected String end;
    protected String name;


    public AbstractParser()
    {
    }


    public AbstractParser(String name, String start, String end)
    {
        this.name = name;
        this.start = start;
        this.end = end;
    }


    public String getEnd()
    {
        return this.end;
    }


    public void setEnd(String end)
    {
        this.end = end;
    }


    public String getStart()
    {
        return this.start;
    }


    public void setStart(String start)
    {
        this.start = start;
    }


    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public abstract String getStartExpression();


    public abstract String getStartEndExpression();


    public abstract String getEndExpression(String paramString);


    public abstract AbstractNode createNode(String paramString1, String paramString2, String paramString3, Translator paramTranslator);
}
