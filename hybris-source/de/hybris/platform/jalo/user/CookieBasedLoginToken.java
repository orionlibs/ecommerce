package de.hybris.platform.jalo.user;

import de.hybris.platform.core.Registry;
import de.hybris.platform.util.Config;
import java.time.Instant;
import javax.servlet.http.Cookie;
import org.apache.log4j.Logger;

public class CookieBasedLoginToken extends StringBasedLoginToken
{
    public static final String NAME_KEY = "login.token.name";
    public static final String TTL_KEY = "login.token.ttl";
    public static final String PATH_KEY = "login.token.path";
    public static final String DOMAIN_KEY = "login.token.domain";
    public static final String DELIMITER_KEY = "login.token.delimiter";
    public static final String SECURE_KEY = "login.token.secure";
    public static final String EXTENDED = "login.token.extended";
    private static final Logger log = Logger.getLogger(CookieBasedLoginToken.class.getName());
    private final String path;
    private final String domain;
    private final int ttl;
    private final String name;
    private final String passwordSalt;
    private final Instant ttlTimestamp;
    private final String tokenDbPart;


    public CookieBasedLoginToken(Cookie cookie)
    {
        super(Registry.getMasterTenant().getValueEncryptor().decrypt(cookie.getValue()));
        this.path = cookie.getPath();
        this.domain = cookie.getDomain();
        this.name = cookie.getName();
        this.ttl = cookie.getMaxAge();
        this.ttlTimestamp = getTTLTimestampInternal();
        this.passwordSalt = getPasswordSaltInternal();
        this.tokenDbPart = getTokenDbPartInternal();
    }


    public String getPasswordSalt()
    {
        return this.passwordSalt;
    }


    public String getName()
    {
        return this.name;
    }


    public String getDomain()
    {
        return this.domain;
    }


    public String getPath()
    {
        return this.path;
    }


    public int getTTL()
    {
        return this.ttl;
    }


    public String getTokenDbPart()
    {
        return this.tokenDbPart;
    }


    private Instant getTTLTimestampInternal()
    {
        String ttlTimestamp = getStringValueFromToken(TokenPart.TTLTIMESTAMP.getKey());
        try
        {
            return Instant.ofEpochMilli(Long.valueOf(ttlTimestamp).longValue());
        }
        catch(Exception ex)
        {
            return null;
        }
    }


    public Instant getTtlTimestamp()
    {
        return this.ttlTimestamp;
    }


    private String getPasswordSaltInternal()
    {
        return getStringValueFromToken(TokenPart.SALT.getKey());
    }


    private String getTokenDbPartInternal()
    {
        return getStringValueFromToken(TokenPart.RANDOM_TOKEN.getKey());
    }


    public boolean isTokenValid()
    {
        if(getUser() == null)
        {
            return false;
        }
        if(Config.getBoolean("login.token.extended", true))
        {
            if(getTtlTimestamp() == null)
            {
                return false;
            }
            if(Instant.now().isAfter(getTtlTimestamp()))
            {
                if(log.isDebugEnabled())
                {
                    log.debug("Expired ttl " + this.ttlTimestamp);
                }
                return false;
            }
            if(checkIfTokenDbPartIsInvalid(getUser(), getTokenDbPartInternal()))
            {
                if(log.isDebugEnabled())
                {
                    log.debug("Invalid db token");
                }
                return false;
            }
        }
        return true;
    }


    private boolean checkIfTokenDbPartIsInvalid(User user, String tokenDbPart)
    {
        TokenService tokenService = (TokenService)Registry.getCoreApplicationContext().getBean("tokenService", TokenService.class);
        return !tokenService.checkIfTokenIsValid(user.getUid(), tokenDbPart);
    }


    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append(getClass().getName()).append(": ");
        buf.append(getName()).append("; ");
        buf.append(getPath()).append("; ");
        buf.append(getDomain()).append("; ");
        buf.append(getValue()).append("; ");
        buf.append(getTTL()).append("; ");
        return buf.toString();
    }
}
