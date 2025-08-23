package de.hybris.platform.cms2.usergroups.service;

import de.hybris.platform.cms2.data.PageableData;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.servicelayer.search.SearchResult;

public interface UserGroupSearchService
{
    SearchResult<UserGroupModel> findUserGroups(String paramString, PageableData paramPageableData);
}
