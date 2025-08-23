package de.hybris.platform.personalizationservices.strategies;

import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.personalizationservices.CxCalculationContext;

public interface UpdateUserSegmentStrategy
{
    void updateUserSegments(UserModel paramUserModel);


    void updateUserSegments(UserModel paramUserModel, CxCalculationContext paramCxCalculationContext);
}
