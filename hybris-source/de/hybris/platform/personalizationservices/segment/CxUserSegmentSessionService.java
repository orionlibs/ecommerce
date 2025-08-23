package de.hybris.platform.personalizationservices.segment;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.CxCalculationContext;
import de.hybris.platform.personalizationservices.data.UserToSegmentData;
import java.util.Collection;

public interface CxUserSegmentSessionService
{
    boolean isUserSegmentStoredInSession(UserModel paramUserModel);


    Collection<UserToSegmentData> getUserSegmentsFromSession(UserModel paramUserModel);


    void setUserSegmentsInSession(UserModel paramUserModel, Collection<? extends UserToSegmentData> paramCollection);


    void setUserSegmentsInSession(UserModel paramUserModel, Collection<? extends UserToSegmentData> paramCollection, CxCalculationContext paramCxCalculationContext);


    void addUserSegmentsInSession(UserModel paramUserModel, Collection<? extends UserToSegmentData> paramCollection);


    void removeUserSegmentsFromSession(UserModel paramUserModel, Collection<? extends UserToSegmentData> paramCollection);


    void loadUserSegmentsIntoSession(UserModel paramUserModel);
}
