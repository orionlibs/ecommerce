package de.hybris.platform.personalizationservices.segment.dao;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import de.hybris.platform.servicelayer.internal.dao.Dao;

public interface CxUserToSegmentDao extends Dao
{
    SearchPageData<CxUserToSegmentModel> findUserToSegmentRelations(UserModel paramUserModel, CxSegmentModel paramCxSegmentModel, BaseSiteModel paramBaseSiteModel, SearchPageData<?> paramSearchPageData);
}
