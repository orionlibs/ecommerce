package de.hybris.platform.persistence.security;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.annotation.Required;

public class GenericSaltedPasswordEncoder implements PasswordEncoder
{
    private String algorithm = null;
    private DigestCalculator digestCalculator;
    private SaltEncodingPolicy saltEncodingPolicy;


    @PostConstruct
    private void init()
    {
        if(!DigestCalculator.isAlgorithmSupported(this.algorithm))
        {
            throw new BeanInitializationException("Error creating bean 'GenericSaltedPasswordEncoder' - Algorithm '" + this.algorithm + "' is not supported");
        }
        this.digestCalculator = DigestCalculator.getInstance(this.algorithm);
    }


    public String encode(String uid, String password)
    {
        String saltedPassword = this.saltEncodingPolicy.saltify(uid, password);
        return this.digestCalculator.calculateDigest(saltedPassword);
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


    public String getAlgorithm()
    {
        return this.algorithm;
    }


    public void setAlgorithm(String algorithm)
    {
        this.algorithm = algorithm;
    }


    @Required
    public void setSaltEncodingPolicy(SaltEncodingPolicy saltEncodingPolicy)
    {
        this.saltEncodingPolicy = saltEncodingPolicy;
    }
}
