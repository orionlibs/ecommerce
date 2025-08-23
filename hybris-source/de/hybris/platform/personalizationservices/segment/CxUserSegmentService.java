package de.hybris.platform.personalizationservices.segment;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import java.util.Collection;

public interface CxUserSegmentService
{
    Collection<CxUserToSegmentModel> getUserSegments(UserModel paramUserModel);


    Collection<CxUserToSegmentModel> getUserSegments(UserModel paramUserModel, BaseSiteModel paramBaseSiteModel);


    void setUserSegments(UserModel paramUserModel, Collection<CxUserToSegmentModel> paramCollection);


    void setUserSegments(UserModel paramUserModel, BaseSiteModel paramBaseSiteModel, Collection<CxUserToSegmentModel> paramCollection);


    void setUserSegments(UserModel paramUserModel, BaseSiteModel paramBaseSiteModel, Collection<CxUserToSegmentModel> paramCollection, CxCalculationContext paramCxCalculationContext);


    void addUserSegments(UserModel paramUserModel, Collection<CxUserToSegmentModel> paramCollection);


    void removeUserSegments(UserModel paramUserModel, Collection<CxUserToSegmentModel> paramCollection);
}
