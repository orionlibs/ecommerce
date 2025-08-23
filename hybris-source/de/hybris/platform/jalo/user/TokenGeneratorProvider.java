package de.hybris.platform.jalo.user;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;
import org.apache.commons.lang.StringUtils;

public class TokenGeneratorProvider
{
    private static final TokenGeneratorProvider INSTANCE = new TokenGeneratorProvider();
    private static final String TOKEN_GENERATOR_BEAN_NAME = StringUtils.uncapitalize(TokenGenerator.class.getSimpleName());
    private static final String BASE_TOKEN_GENERATOR_BEAN_NAME = StringUtils.uncapitalize(BaseTokenGenerator.class
                    .getSimpleName());


    public static TokenGeneratorProvider getInstance()
    {
        return INSTANCE;
    }


    public TokenGenerator getTokenGenerator()
    {
        if(Config.getBoolean("login.token.extended", true))
        {
            return (TokenGenerator)Registry.getCoreApplicationContext().getBean(TOKEN_GENERATOR_BEAN_NAME, TokenGenerator.class);
        }
        return (TokenGenerator)Registry.getCoreApplicationContext().getBean(BASE_TOKEN_GENERATOR_BEAN_NAME, TokenGenerator.class);
    }
}
