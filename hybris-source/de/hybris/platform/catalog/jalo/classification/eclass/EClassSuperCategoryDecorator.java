package de.hybris.platform.catalog.jalo.classification.eclass;

import de.hybris.platform.impex.jalo.header.AbstractColumnDescriptor;
import de.hybris.platform.impex.jalo.header.AbstractImpExCSVCellDecorator;
import de.hybris.platform.impex.jalo.header.HeaderValidationException;
import java.util.Map;
import org.apache.log4j.Logger;

public class EClassSuperCategoryDecorator extends AbstractImpExCSVCellDecorator
{
    private static final Logger LOG = Logger.getLogger(EClassSuperCategoryDecorator.class);
    public static final String SRC_CELL = "srcCell";
    private Integer srcPos = null;


    public void init(AbstractColumnDescriptor column) throws HeaderValidationException
    {
        super.init(column);
        String modifier = getColumnDescriptor().getDescriptorData().getModifier("srcCell");
        if(modifier == null)
        {
            throw new HeaderValidationException("missing srcCell for eclass supercategory descorator", 0);
        }
        try
        {
            this.srcPos = Integer.valueOf(modifier);
        }
        catch(NumberFormatException e)
        {
            throw new HeaderValidationException("invalid srcCell for eclass supercategory descorator : '" + modifier + "' due to " + e
                            .getMessage(), 0);
        }
    }


    public String decorate(int position, Map srcLine)
    {
        String ret, mySrc = (String)srcLine.get(Integer.valueOf(position));
        if(mySrc != null && mySrc.length() > 0)
        {
            if(LOG.isDebugEnabled())
            {
                LOG.debug("got super preset number '" + mySrc + "' - no need to extract");
            }
            return mySrc;
        }
        String number = (String)srcLine.get(this.srcPos);
        if(number == null)
        {
            LOG.warn("class number was NULL");
            return null;
        }
        if(number.endsWith("000000"))
        {
            ret = null;
        }
        else if(number.endsWith("0000"))
        {
            ret = number.substring(0, 2) + "000000";
        }
        else if(number.endsWith("00"))
        {
            ret = number.substring(0, 4) + "0000";
        }
        else
        {
            ret = number.substring(0, 6) + "00";
        }
        if(LOG.isDebugEnabled())
        {
            LOG.debug("extracted super class '" + ret + "' from class number '" + number);
        }
        return ret;
    }
}
