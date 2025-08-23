package de.hybris.bootstrap.typesystem.xml;

import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import java.util.Collection;
import java.util.Collections;

class CustomPropsTagListener extends AbstractTypeSystemTagListener
{
    CustomPropsTagListener(AttributeTagListener parent, String tagName)
    {
        super((AbstractTypeSystemTagListener)parent, tagName);
    }


    CustomPropsTagListener(ItemTypeTagListener parent, String tagName)
    {
        super((AbstractTypeSystemTagListener)parent, tagName);
    }


    CustomPropsTagListener(RelationTypeTagListener.RelationEndTagListener parent, String tagName)
    {
        super((AbstractTypeSystemTagListener)parent, tagName);
    }


    protected Collection createSubTagListeners()
    {
        return Collections.singleton(new CustomPropertyTagListener(this, "property"));
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        return getSubTagValue("property");
    }
}
