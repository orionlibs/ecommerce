package de.hybris.platform.cms2.cmsitems.service.impl;

import de.hybris.platform.cms2.cmsitems.service.SortStatementFormatter;
import de.hybris.platform.cms2.namedquery.Sort;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

public class DefaultSortStatementFormatter implements SortStatementFormatter
{
    public boolean isApplicable(AttributeDescriptorModel attributeDescriptor)
    {
        return true;
    }


    public String formatSortStatement(Sort sort)
    {
        return "{c." + sort.getParameter() + "}";
    }
}
