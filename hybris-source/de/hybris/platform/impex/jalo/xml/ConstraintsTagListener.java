package de.hybris.platform.impex.jalo.xml;

import de.hybris.bootstrap.xml.DefaultTagListener;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ConstraintsTagListener extends DefaultTagListener
{
    public ConstraintsTagListener(ImpExHeaderTagListener parent)
    {
        super((DefaultTagListener)parent);
    }


    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new Object[] {new UniqueConstraintTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        List<UniqueKeyConstraint> ret = new ArrayList();
        UniqueKeyConstraint ukc = (UniqueKeyConstraint)getSubTagValue("unique-key");
        if(ukc != null)
        {
            ret.add(ukc);
        }
        return ret;
    }


    public String getTagName()
    {
        return "constraints";
    }
}
