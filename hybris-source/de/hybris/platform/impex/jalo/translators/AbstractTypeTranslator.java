package de.hybris.platform.impex.jalo.translators;

import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.DocumentIDColumnDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderDescriptor;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import de.hybris.platform.impex.jalo.header.SpecialColumnDescriptor;
import de.hybris.platform.impex.jalo.header.StandardColumnDescriptor;
import de.hybris.platform.impex.jalo.util.ImpExUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public abstract class AbstractTypeTranslator implements HeaderCellTranslator
{
    public List<AbstractColumnDescriptor> translateColumnDescriptors(HeaderDescriptor header, List<String> columnExpressions) throws HeaderValidationException
    {
        return translateColumnDescriptors(header, columnExpressions, true);
    }


    public List<AbstractColumnDescriptor> translateColumnDescriptors(HeaderDescriptor header, List<String> columnExpressions, boolean rethrowException) throws HeaderValidationException
    {
        List<AbstractColumnDescriptor> ret;
        if(columnExpressions == null || columnExpressions.isEmpty())
        {
            if(ImpExUtils.isStrictMode(header.getReader().getValidationMode()))
            {
                throw new IllegalArgumentException("No attributes specified for header with type " + header
                                .getConfiguredComposedType().getCode());
            }
            ret = new ArrayList<>(getAutoColumnExpressions(header));
        }
        else
        {
            ret = new ArrayList(columnExpressions);
        }
        int index = 1;
        int lastNonNullPos = -1;
        for(ListIterator<AbstractColumnDescriptor> iter = ret.listIterator(); iter.hasNext(); index++)
        {
            String expr = (String)iter.next();
            if(expr != null && expr.length() > 0)
            {
                lastNonNullPos = index;
                try
                {
                    iter.set(translatorColumnDescriptor(header, expr, lastNonNullPos));
                }
                catch(Exception ex)
                {
                    if(rethrowException)
                    {
                        throw ex;
                    }
                    iter.set(null);
                }
            }
            else
            {
                iter.set(null);
            }
        }
        return (lastNonNullPos != -1 && lastNonNullPos < ret.size()) ? ret.subList(0, lastNonNullPos) : ret;
    }


    protected AbstractColumnDescriptor translatorColumnDescriptor(HeaderDescriptor header, String expr, int pos) throws HeaderValidationException
    {
        if(isSpecialColumn(expr))
        {
            return (AbstractColumnDescriptor)new SpecialColumnDescriptor(pos, header, expr);
        }
        if(isDocumentIDColumn(expr))
        {
            return (AbstractColumnDescriptor)new DocumentIDColumnDescriptor(pos, header, expr);
        }
        return (AbstractColumnDescriptor)new StandardColumnDescriptor(pos, header, expr);
    }


    protected boolean isSpecialColumn(String expr)
    {
        return expr.startsWith("@");
    }


    protected boolean isDocumentIDColumn(String expr)
    {
        return expr.startsWith("&");
    }


    protected List<AbstractColumnDescriptor> getAutoColumnExpressions(HeaderDescriptor header)
    {
        return Collections.EMPTY_LIST;
    }
}
