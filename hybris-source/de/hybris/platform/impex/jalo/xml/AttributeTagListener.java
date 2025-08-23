package de.hybris.platform.impex.jalo.xml;

import de.hybris.bootstrap.xml.DefaultTagListener;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class AttributeTagListener extends DefaultTagListener
{
    public AttributeTagListener(DefaultTagListener parent)
    {
        super(parent);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new Object[] {new TranslationTagListener(this), new ParamsTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        String type = getAttribute("type");
        String qualifier = getAttribute("qualifier");
        Collection<?> translationAttributes = getSubTagValueCollection("translation");
        (new java.util.List[1])[0] = new ArrayList(translationAttributes);
        AbstractDescriptor.ColumnParams ret = new AbstractDescriptor.ColumnParams((type != null) ? (type + "." + type) : qualifier, (translationAttributes != null && !translationAttributes.isEmpty()) ? new java.util.List[1] : null);
        ret.addAllModifier(getSubTagValueMap("params"));
        return ret;
    }


    public String getTagName()
    {
        return "attribute";
    }
}
