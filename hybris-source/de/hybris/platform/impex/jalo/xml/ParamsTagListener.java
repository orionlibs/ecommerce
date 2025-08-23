package de.hybris.platform.impex.jalo.xml;

import de.hybris.bootstrap.xml.DefaultTagListener;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import java.util.Collection;
import java.util.Collections;

public class ParamsTagListener extends DefaultTagListener
{
    public ParamsTagListener(DefaultTagListener parent)
    {
        super(parent);
    }


    protected Collection createSubTagListeners()
    {
        return Collections.singletonList(new ParamTagListener(this));
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        return getSubTagValueMap("param");
    }


    public String getTagName()
    {
        return "params";
    }
}
