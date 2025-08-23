package de.hybris.platform.persistence.security;

import de.hybris.platform.jalo.JaloSystemException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import org.apache.log4j.Logger;

public class SaltedPasswordEncoder implements PasswordEncoder
{
    private static final Logger log = Logger.getLogger(SaltedPasswordEncoder.class.getName());
    private static final String DEFAULT_SYSTEM_SPECIFIC_SALT = "hybris blue pepper can be used for creating delicious noodle meals";
    private static final String DELIMITER = "::";
    private String salt = null;
    private String algorithmn;


    protected String calculateHash(String plain)
    {
        StringBuilder result = new StringBuilder(32);
        try
        {
            MessageDigest sh256 = MessageDigest.getInstance(getAlgorithmn());
            sh256.update(plain.getBytes());
            Formatter formatter = new Formatter(result);
            for(byte b : sh256.digest())
            {
                formatter.format("%02x", new Object[] {Byte.valueOf(b)});
            }
            formatter.close();
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new JaloSystemException(e, "!!", 0);
        }
        return result.toString();
    }


    public String encode(String uid, String password)
    {
        if(!isSaltedAlready(password))
        {
            String userSpecificSalt = generateUserSpecificSalt(uid);
            return calculateHash(getSystemSpecificSalt().concat("::").concat((password == null) ? "" : password)
                            .concat("::").concat(userSpecificSalt));
        }
        return calculateHash(password);
    }


    public boolean check(String uid, String encoded, String plain)
    {
        if(encoded == null)
        {
            encoded = "";
        }
        if(plain == null)
        {
            plain = "";
        }
        return encoded.equalsIgnoreCase(encode(uid, plain));
    }


    public String decode(String encoded) throws EJBCannotDecodePasswordException
    {
        throw new EJBCannotDecodePasswordException(new Throwable("You cannot decode a hashed password!!!"), "Cannot decode", 0);
    }


    protected String generateUserSpecificSalt(String uid)
    {
        return uid;
    }


    private boolean isSaltedAlready(String password)
    {
        if(password == null)
        {
            return false;
        }
        return password.startsWith(getSystemSpecificSalt().concat("::"));
    }


    protected String getSystemSpecificSalt()
    {
        return (getSalt() != null) ? getSalt() : "hybris blue pepper can be used for creating delicious noodle meals";
    }


    public String getSalt()
    {
        return this.salt;
    }


    public void setSalt(String salt)
    {
        this.salt = salt;
    }


    public String getAlgorithmn()
    {
        return this.algorithmn;
    }


    public void setAlgorithmn(String algorithmn)
    {
        this.algorithmn = algorithmn;
    }
}
