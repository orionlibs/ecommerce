package de.hybris.platform.cms2.cmsitems.service;

import de.hybris.platform.cms2.data.CMSItemSearchData;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.contents.CMSItemModel;
import de.hybris.platform.servicelayer.search.SearchResult;
import java.util.List;

public interface CMSItemSearchService
{
    SearchResult<CMSItemModel> findCMSItems(CMSItemSearchData paramCMSItemSearchData, PageableData paramPageableData);


    boolean hasCommonAncestorForTypeCodes(List<String> paramList) throws IllegalArgumentException;
}
