package de.hybris.platform.persistence.security;

import de.hybris.platform.jalo.JaloSystemException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;

@Deprecated(since = "ages", forRemoval = true)
public class SaltedMD5PasswordEncoder extends MD5PasswordEncoder
{
    private static final Logger LOG = Logger.getLogger(SaltedMD5PasswordEncoder.class.getName());
    private static final String DEFAULT_SYSTEM_SPECIFIC_SALT = "hybris blue pepper can be used for creating delicious noodle meals";
    private static final String DELIMITER = "::";
    private String salt = null;
    private SaltEncodingPolicy saltEncodingPolicy;


    public String encode(String password)
    {
        throw new JaloSystemException("Unsupported method call! You have to use #encode( String uid, String password )");
    }


    public String encode(String uid, String password)
    {
        if(!isSaltedAlready(password))
        {
            String userSpecificSalt = generateUserSpecificSalt(uid);
            return calculateMD5(getSystemSpecificSalt().concat("::").concat((password == null) ? "" : password)
                            .concat("::").concat(userSpecificSalt));
        }
        return calculateMD5(password);
    }


    public boolean check(String encoded, String password)
    {
        throw new JaloSystemException("Unsupported method call! You have to use #check( String uid, String encoded, String password )");
    }


    public boolean check(String uid, String encoded, String password)
    {
        if(encoded == null)
        {
            encoded = "";
        }
        if(password == null)
        {
            return false;
        }
        return encoded.equalsIgnoreCase(encode(uid, password));
    }


    public final String decode(String encoded) throws EJBCannotDecodePasswordException
    {
        throw new EJBCannotDecodePasswordException(new Throwable("You cannot decode a md5 password!!!"), "Cannot decode", 0);
    }


    protected String generateUserSpecificSalt(String uid)
    {
        return this.saltEncodingPolicy.generateUserSalt(uid);
    }


    private boolean isSaltedAlready(String password)
    {
        return this.saltEncodingPolicy.isSaltedAlready(password);
    }


    protected String getSystemSpecificSalt()
    {
        return this.saltEncodingPolicy.getSystemSpecificSalt();
    }


    public String getSalt()
    {
        return this.salt;
    }


    public void setSalt(String salt)
    {
        this.salt = salt;
    }


    @Required
    public void setSaltEncodingPolicy(SaltEncodingPolicy saltEncodingPolicy)
    {
        this.saltEncodingPolicy = saltEncodingPolicy;
    }
}
