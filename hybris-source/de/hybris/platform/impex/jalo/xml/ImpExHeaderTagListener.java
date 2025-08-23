package de.hybris.platform.impex.jalo.xml;

import de.hybris.bootstrap.xml.DefaultTagListener;
import de.hybris.bootstrap.xml.ObjectProcessor;
import de.hybris.bootstrap.xml.ParseAbortException;
import de.hybris.bootstrap.xml.UnknownParseError;
import de.hybris.platform.impex.jalo.header.AbstractDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ImpExHeaderTagListener extends DefaultTagListener
{
    protected Collection createSubTagListeners()
    {
        return Arrays.asList(new Object[] {new AttributesTagListener(this), new ParamsTagListener(this), new ConstraintsTagListener(this)});
    }


    protected Object processEndElement(ObjectProcessor processor) throws ParseAbortException
    {
        try
        {
            HeaderDescriptor ret = new HeaderDescriptor(null, "<xml>", createParams(getSubTagValueMap("params"),
                            getAttribute("type")), "no location", null);
            ret.setColumns(createColumnDescriptors(ret, getSubTagValueCollection("attributes"),
                            getSubTagValueCollection("constraints")));
            return ret;
        }
        catch(HeaderValidationException e)
        {
            e.printStackTrace(System.err);
            throw new UnknownParseError(e, "header error " + e.getMessage());
        }
    }


    protected List createColumnDescriptors(HeaderDescriptor header, Collection columParams, Collection constraints) throws HeaderValidationException
    {
        List<StandardColumnDescriptor> ret = new ArrayList();
        int index = 1;
        ConstraintsTagListener.UniqueKeyConstraint ukc = extractUniqueKeyConstraint(constraints);
        for(Iterator<AbstractDescriptor.ColumnParams> iter = columParams.iterator(); iter.hasNext(); index++)
        {
            AbstractDescriptor.ColumnParams columnParameters = iter.next();
            StandardColumnDescriptor columnDescriptor = new StandardColumnDescriptor(index, header, "<xml>", (AbstractDescriptor.DescriptorParams)columnParameters);
            if(isUnique(ukc, columnParameters))
            {
                columnDescriptor.getDescriptorData().addModifier("unique", "true");
            }
            ret.add(columnDescriptor);
        }
        return ret;
    }


    protected boolean isUnique(ConstraintsTagListener.UniqueKeyConstraint ukc, AbstractDescriptor.ColumnParams column)
    {
        if(ukc == null)
        {
            return false;
        }
        for(Iterator<AbstractDescriptor.ColumnParams> iter = ukc.getUniueAttributes().iterator(); iter.hasNext(); )
        {
            AbstractDescriptor.ColumnParams uniqueCol = iter.next();
            if(uniqueCol.getQualifier().equalsIgnoreCase(column.getQualifier()))
            {
                return true;
            }
        }
        return false;
    }


    protected ConstraintsTagListener.UniqueKeyConstraint extractUniqueKeyConstraint(Collection constraints)
    {
        if(constraints == null || constraints.isEmpty())
        {
            return null;
        }
        for(Iterator iter = constraints.iterator(); iter.hasNext(); )
        {
            Object object = iter.next();
            if(object instanceof ConstraintsTagListener.UniqueKeyConstraint)
            {
                return (ConstraintsTagListener.UniqueKeyConstraint)object;
            }
        }
        return null;
    }


    protected AbstractDescriptor.DescriptorParams createParams(Map params, String type)
    {
        AbstractDescriptor.HeaderParams headerParams = new AbstractDescriptor.HeaderParams(null, type);
        headerParams.addAllModifier(params);
        return (AbstractDescriptor.DescriptorParams)headerParams;
    }


    public String getTagName()
    {
        return "header";
    }


    public HeaderDescriptor getHeader()
    {
        return (HeaderDescriptor)getResult();
    }
}
