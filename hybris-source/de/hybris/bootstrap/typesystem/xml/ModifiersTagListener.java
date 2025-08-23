package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import java.util.HashMap;
import java.util.Map;

class ModifiersTagListener extends AbstractTypeSystemTagListener
{
    public static final String INTEGER_MODIFIERS = "modifiers";
    public static final String UNIQUE_FLAG = "uniqueflag";
    public static final int DEFAULT_MODIFIERS = 31;


    ModifiersTagListener(RelationTypeTagListener.RelationEndTagListener parent, String tagName)
    {
        super((AbstractTypeSystemTagListener)parent, tagName);
    }


    ModifiersTagListener(AttributeTagListener parent, String tagName)
    {
        super((AbstractTypeSystemTagListener)parent, tagName);
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        Map<String, Object> retmap = new HashMap<>(2);
        int modifiers = 0;
        if(getBooleanAttribute("read", true))
        {
            modifiers++;
        }
        if(getBooleanAttribute("write", true))
        {
            modifiers += 2;
        }
        if(getBooleanAttribute("search", true))
        {
            modifiers += 16;
        }
        if(getBooleanAttribute("optional", true))
        {
            modifiers += 8;
        }
        if(getBooleanAttribute("initial", false))
        {
            modifiers += 2048;
        }
        if(getBooleanAttribute("private", false))
        {
            modifiers += 128;
        }
        if(getBooleanAttribute("removable", true))
        {
            modifiers += 4;
        }
        if(getBooleanAttribute("partof", false))
        {
            modifiers += 32;
        }
        if(getBooleanAttribute("dontOptimize", false))
        {
            modifiers += 8192;
        }
        if(getBooleanAttribute("encrypted", false))
        {
            modifiers += 16384;
        }
        retmap.put("modifiers", Integer.valueOf(modifiers));
        retmap.put("uniqueflag", Boolean.valueOf(getBooleanAttribute("unique", false)));
        return retmap;
    }
}
