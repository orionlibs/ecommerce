package de.hybris.platform.persistence.security;

import org.apache.log4j.Logger;

@Deprecated(since = "ages", forRemoval = true)
public class MD5PasswordEncoder implements PasswordEncoder
{
    private static final Logger LOG = Logger.getLogger(MD5PasswordEncoder.class.getName());
    private final DigestCalculator digestCalculator = DigestCalculator.getInstance("MD5");


    protected String calculateMD5(String plain)
    {
        return this.digestCalculator.calculateDigest(plain);
    }


    public String encode(String uid, String plain)
    {
        return calculateMD5(plain);
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
        throw new EJBCannotDecodePasswordException(null, "You cannot decode an md5 password!!!", 0);
    }
}
