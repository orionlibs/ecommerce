package de.hybris.platform.cms2.cmsitems.service;

import de.hybris.platform.cms2.namedquery.Sort;
import de.hybris.platform.core.model.type.AttributeDescriptorModel;

public interface SortStatementFormatter
{
    boolean isApplicable(AttributeDescriptorModel paramAttributeDescriptorModel);


    String formatSortStatement(Sort paramSort);
}
