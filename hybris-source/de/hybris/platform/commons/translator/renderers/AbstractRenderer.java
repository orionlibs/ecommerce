package de.hybris.platform.commons.translator.renderers;

import de.hybris.platform.commons.translator.Translator;
import de.hybris.platform.commons.translator.nodes.AbstractNode;
import java.util.Properties;

public abstract class AbstractRenderer
{
    protected String start;
    protected String end;
    protected Properties properties;


    public void setEnd(String end)
    {
        this.end = end;
    }


    public void setStart(String start)
    {
        this.start = start;
    }


    public abstract String renderTextFromNode(AbstractNode paramAbstractNode, Translator paramTranslator);


    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }
}
