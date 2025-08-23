package de.hybris.platform.persistence.security;

import de.hybris.platform.jalo.JaloSystemException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = true)
public class SHA1PasswordEncoder implements PasswordEncoder
{
    private static final Logger log = Logger.getLogger(SHA1PasswordEncoder.class.getName());


    protected String calculateSHA1(String plain)
    {
        StringBuilder result = new StringBuilder(32);
        try
        {
            MessageDigest sh1 = MessageDigest.getInstance("SHA1");
            sh1.update(plain.getBytes());
            Formatter formatter = new Formatter(result);
            for(byte b : sh1.digest())
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


    public String encode(String uid, String plain)
    {
        return calculateSHA1(plain);
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
        throw new EJBCannotDecodePasswordException(new Throwable("You cannot decode an sha1 password!!!"), "Cannot decode", 0);
    }
}
