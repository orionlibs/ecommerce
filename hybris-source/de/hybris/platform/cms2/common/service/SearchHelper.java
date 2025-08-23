package de.hybris.platform.cms2.common.service;

import de.hybris.platform.cms2.enums.SortDirection;
import de.hybris.platform.cms2.namedquery.Sort;
import java.util.List;

public interface SearchHelper
{
    List<Sort> convertSort(String paramString, SortDirection paramSortDirection);


    Sort convertSortBlock(String paramString, SortDirection paramSortDirection);
}
