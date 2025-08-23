package de.hybris.platform.cms2.version.service;

import de.hybris.platform.cms2.data.CMSVersionSearchData;
import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.cms2.model.CMSVersionModel;
import de.hybris.platform.servicelayer.search.SearchResult;

public interface CMSVersionSearchService
{
    SearchResult<CMSVersionModel> findVersions(CMSVersionSearchData paramCMSVersionSearchData, PageableData paramPageableData);
}
