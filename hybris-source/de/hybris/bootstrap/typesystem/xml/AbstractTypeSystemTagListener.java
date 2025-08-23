package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.DefaultTagListener;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.TagListener;
import de.hybris.bootstrap.xml.UnknownParseError;
import java.util.Iterator;

public abstract class AbstractTypeSystemTagListener extends DefaultTagListener
{
    private final String tagName;
    private final HybrisTypeSystemParser parser;


    AbstractTypeSystemTagListener(HybrisTypeSystemParser parser, String tagName)
    {
        super(null);
        this.tagName = tagName;
        this.parser = parser;
    }


    AbstractTypeSystemTagListener(AbstractTypeSystemTagListener parent, String tagName)
    {
        super(parent);
        this.tagName = tagName;
        this.parser = null;
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        return null;
    }


    protected void processError(Exception exception) throws ParseAbortException
    {
        exception.printStackTrace(System.err);
        throw new UnknownParseError(exception, "error parsing system " + getParser().getCurrentExtensionName() + " at lines [" +
                        getStartLineNumber() + "-" + getEndLineNumber() + "] : " + exception.getMessage());
    }


    protected TagListener getParent(Class parentClass)
    {
        for(Iterator<TagListener> it = getParents().iterator(); it.hasNext(); )
        {
            TagListener tagListener = it.next();
            if(parentClass.isInstance(tagListener))
            {
                return tagListener;
            }
        }
        throw new IllegalArgumentException("no parent of class " + parentClass + " found within " + getParents());
    }


    public String getAttribute(String qname)
    {
        String result = super.getAttribute(qname);
        if(result == null || result.length() == 0)
        {
            return null;
        }
        return result.trim();
    }


    protected HybrisTypeSystemParser getParser()
    {
        if(this.parser != null)
        {
            return this.parser;
        }
        AbstractTypeSystemTagListener parent = (AbstractTypeSystemTagListener)getParent(AbstractTypeSystemTagListener.class);
        return (parent != null) ? parent.getParser() : null;
    }


    protected int getIntegerAttribute(String qname, int defValue)
    {
        String result = getAttribute(qname);
        return (result != null && result.length() > 0) ? Integer.parseInt(result) : defValue;
    }


    protected boolean getBooleanAttribute(String qname, boolean defValue)
    {
        String result = getAttribute(qname);
        return (result != null) ? "true".equalsIgnoreCase(result) : defValue;
    }


    protected boolean isAutocreate()
    {
        return getBooleanAttribute("autocreate", true);
    }


    protected boolean isGenerate()
    {
        return getBooleanAttribute("generate", true);
    }


    protected String getCode()
    {
        return getAttribute("code");
    }


    public final String getTagName()
    {
        return this.tagName;
    }
}
