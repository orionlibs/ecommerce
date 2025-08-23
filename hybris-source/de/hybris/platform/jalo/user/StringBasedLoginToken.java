package de.hybris.platform.jalo.user;

import de.hybris.platform.core.PK;
import de.hybris.platform.jalo.JaloItemNotFoundException;
import de.hybris.platform.jalo.JaloSession;
import de.hybris.platform.jalo.c2l.Language;
import de.hybris.platform.util.Config;
import java.io.Serializable;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class StringBasedLoginToken implements LoginToken, Serializable
{
    private static final Logger log = Logger.getLogger(StringBasedLoginToken.class.getName());
    private User user;
    private Language lang;
    private String password;
    private final String value;
    private String delimiter;
    private Map<String, String> dividedTokenMap;


    public StringBasedLoginToken(String value)
    {
        this.value = value;
        init();
    }


    protected void init()
    {
        setDelimiter(Config.getParameter("login.token.delimiter"));
        this.dividedTokenMap = TokenGeneratorProvider.getInstance().getTokenGenerator().decodeToken(getValue(), getDelimiter());
        this.user = getUserInternal();
        if(this.user == null)
        {
            this.password = null;
            this.lang = null;
            return;
        }
        this.password = getPasswordInternal();
        this.lang = getLanguageInternal();
    }


    protected void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
    }


    protected String getDelimiter()
    {
        return this.delimiter;
    }


    public Language getLanguage()
    {
        return this.lang;
    }


    public String getLogin()
    {
        return this.user.getLogin();
    }


    public User getUser()
    {
        return this.user;
    }


    public String getPassword()
    {
        return this.password;
    }


    public String getValue()
    {
        return this.value;
    }


    protected String getPasswordInternal()
    {
        return getStringValueFromToken(TokenPart.PASSWORD.getKey());
    }


    protected User getUserInternal()
    {
        String pk = getStringValueFromToken(TokenPart.USER.getKey());
        if(StringUtils.isEmpty(pk))
        {
            return null;
        }
        try
        {
            return (User)JaloSession.getCurrentSession().getItem(PK.fromLong(Long.parseLong(pk)));
        }
        catch(NumberFormatException e)
        {
            if(log.isDebugEnabled())
            {
                log.debug("Error parsing LoginToken ");
            }
            return null;
        }
        catch(JaloItemNotFoundException e)
        {
            log.warn(e.getMessage());
            log.warn("PK '" + pk + "' is no longer valid!");
            return null;
        }
    }


    protected Language getLanguageInternal()
    {
        String langPK = getStringValueFromToken(TokenPart.LANGUAGE.getKey());
        try
        {
            return (Language)JaloSession.getCurrentSession().getItem(PK.fromLong(Long.parseLong(langPK)));
        }
        catch(Exception e)
        {
            log.warn(e.getMessage());
            return null;
        }
    }


    public String toString()
    {
        return getClass().getName() + ": " + getClass().getName();
    }


    protected String getStringValueFromToken(String key)
    {
        return this.dividedTokenMap.get(key);
    }
}
