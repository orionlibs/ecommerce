package de.hybris.bootstrap.xml;

import java.util.HashMap;
import java.util.Map;

public abstract class SimpleValueTagListener extends DefaultTagListener
{
    public static final String UNSPECIFIED_TYPE = "unspecified";
    private final String tagName;
    private final String valueAttribute;
    private final String mappingAttribute;


    public SimpleValueTagListener(TagListener parent, String tagName)
    {
        this(parent, tagName, null, null);
    }


    public SimpleValueTagListener(TagListener parent, String tagName, String valueAttribute, String mappingAttribute)
    {
        super((DefaultTagListener)parent);
        if(tagName == null)
        {
            throw new IllegalArgumentException("TagName can not be null!");
        }
        this.tagName = tagName;
        this.valueAttribute = valueAttribute;
        this.mappingAttribute = mappingAttribute;
    }


    public String getTagName()
    {
        return this.tagName;
    }


    public boolean isMapped()
    {
        return (this.mappingAttribute != null);
    }


    public abstract Object getValue() throws ParseAbortException;


    public String getCharacters()
    {
        return (this.valueAttribute != null) ? getAttribute(this.valueAttribute) : super.getCharacters();
    }


    public Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        if(isMapped())
        {
            Map<Object, Object> values = new HashMap<>();
            values.put((getAttribute(this.mappingAttribute) == null) ? "unspecified" : getAttribute(this.mappingAttribute), getValue());
            return values;
        }
        return getValue();
    }
}
