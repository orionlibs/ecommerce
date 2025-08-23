package de.hybris.platform.cms2.cmsitems.service.impl;

import de.hybris.platform.cms2.cmsitems.service.SortStatementFormatter;
import de.hybris.platform.cms2.namedquery.Sort;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;
import org.apache.log4j.Logger;

public class StringSortStatementFormatter implements SortStatementFormatter
{
    private static Logger LOG = Logger.getLogger(StringSortStatementFormatter.class);


    public boolean isApplicable(AttributeDescriptorModel attributeDescriptor)
    {
        String attributeType = attributeDescriptor.getAttributeType().getCode();
        try
        {
            Class<?> classType = Class.forName(attributeType);
            return String.class.isAssignableFrom(classType);
        }
        catch(ClassNotFoundException e)
        {
            LOG.debug("Unable to find a Class called: " + attributeType, e);
            return false;
        }
    }


    public String formatSortStatement(Sort sort)
    {
        return "LOWER({c." + sort.getParameter() + "})";
    }
}
