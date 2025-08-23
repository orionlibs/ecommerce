package de.hybris.platform.jalo.user;

public interface TokenService
{
    String getOrCreateTokenForUser(String paramString);


    boolean revokeTokenForUser(String paramString);


    boolean checkIfTokenIsValid(String paramString1, String paramString2);
}
