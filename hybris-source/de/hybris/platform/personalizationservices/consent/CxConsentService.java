package de.hybris.platform.personalizationservices.consent;

import de.hybris.platform.core.model.user.UserModel;

public interface CxConsentService
{
    boolean userHasActiveConsent(UserModel paramUserModel);


    default boolean userHasActiveConsent()
    {
        return false;
    }
}
