package de.hybris.platform.jalo.user;

import de.hybris.platform.jalo.c2l.Language;

public interface LoginToken
{
    public static final String DELIMITER_KEY = "login.token.delimiter";
    public static final String ENCODER_KEY = "login.token.encoder";
    public static final String URL_PARAMETER_KEY = "login.token.url.parameter";


    String getPassword();


    User getUser();


    Language getLanguage();


    String getValue();


    default boolean isTokenValid()
    {
        return true;
    }


    default String getPasswordSalt()
    {
        return "";
    }
}
