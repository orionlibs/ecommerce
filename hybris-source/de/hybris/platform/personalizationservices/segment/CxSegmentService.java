package de.hybris.platform.personalizationservices.segment;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.servicelayer.data.SearchPageData;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.CxUpdateSegmentContext;
import de.hybris.platform.personalizationservices.model.CxSegmentModel;
import de.hybris.platform.personalizationservices.model.CxUserToSegmentModel;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public interface CxSegmentService
{
    Optional<CxSegmentModel> getSegment(String paramString);


    SearchPageData<CxSegmentModel> getSegments(Map<String, String> paramMap, SearchPageData<?> paramSearchPageData);


    Collection<UserModel> getUsersFromSegment(CxSegmentModel paramCxSegmentModel);


    Collection<CxSegmentModel> getSegmentsFromUser(UserModel paramUserModel);


    Collection<CxUserToSegmentModel> getUserToSegmentForUser(UserModel paramUserModel);


    Collection<CxUserToSegmentModel> getUserToSegmentForCalculation(UserModel paramUserModel);


    void saveUserToSegments(Collection<CxUserToSegmentModel> paramCollection);


    void removeUserToSegments(Collection<CxUserToSegmentModel> paramCollection);


    SearchPageData<CxUserToSegmentModel> getUserToSegmentModel(UserModel paramUserModel, CxSegmentModel paramCxSegmentModel, BaseSiteModel paramBaseSiteModel, SearchPageData<?> paramSearchPageData);


    void updateUserSegments(UserModel paramUserModel);


    void updateUserSegments(UserModel paramUserModel, CxCalculationContext paramCxCalculationContext);


    Collection<CxSegmentModel> getSegmentsForCodes(Collection<String> paramCollection);


    void updateSegments(CxUpdateSegmentContext paramCxUpdateSegmentContext);
}
