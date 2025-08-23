package de.hybris.platform.oauth2.services;

import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import java.util.List;

public interface HybrisOpenIDTokenServices
{
    List<String> getTokenEndpointAuthMethods();


    List<String> getSubjectTypes();


    List<String> getResponseTypes();


    List<String> getSupportedScopes();


    List<OAuthClientDetailsModel> getAllOpenIDClientDetails();
}
