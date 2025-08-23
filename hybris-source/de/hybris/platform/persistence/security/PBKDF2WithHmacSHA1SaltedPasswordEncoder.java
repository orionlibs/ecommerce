package de.hybris.platform.persistence.security;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import org.springframework.beans.factory.InitializingBean;

public class PBKDF2WithHmacSHA1SaltedPasswordEncoder implements PasswordEncoder, InitializingBean
{
    private SecureRandom saltGenerator;
    private SecretKeyFactory keyFactory;
    private int iterations = 1000;
    private int keyLength = 512;
    private String keyAlgorithm = "PBKDF2WithHmacSHA1";
    private int saltLength = 16;
    private String saltAlgorithm = "SHA1PRNG";


    public void afterPropertiesSet() throws NoSuchAlgorithmException
    {
        this.saltGenerator = SecureRandom.getInstance(this.saltAlgorithm);
        this.keyFactory = SecretKeyFactory.getInstance(this.keyAlgorithm);
    }


    public String encode(String uid, String password)
    {
        byte[] salt = generateSalt();
        byte[] hash = calculateHash(password, salt, this.iterations, this.keyLength);
        return (new EncodedHash(this.iterations, salt, hash)).toString();
    }


    public boolean check(String uid, String encoded, String plain)
    {
        EncodedHash parsedEncodedHash = new EncodedHash(encoded);
        byte[] hash = calculateHash(plain, parsedEncodedHash.salt, parsedEncodedHash.iterations, parsedEncodedHash.hash.length * 8);
        return Arrays.equals(parsedEncodedHash.hash, hash);
    }


    public String decode(String encoded) throws EJBCannotDecodePasswordException
    {
        throw new EJBCannotDecodePasswordException(new Throwable("You cannot decode a PBKDF2WithHmacSHA1 hash!"), "Cannot decode", 0);
    }


    protected byte[] calculateHash(String password, byte[] salt, int iterations, int keyLength)
    {
        String _password = (password == null) ? "" : password;
        try
        {
            PBEKeySpec spec = new PBEKeySpec(_password.toCharArray(), salt, iterations, keyLength);
            return this.keyFactory.generateSecret(spec).getEncoded();
        }
        catch(InvalidKeySpecException e)
        {
            throw new IllegalArgumentException(e);
        }
    }


    private byte[] generateSalt()
    {
        byte[] salt = new byte[this.saltLength];
        this.saltGenerator.nextBytes(salt);
        return salt;
    }


    public void setIterations(int iterations)
    {
        this.iterations = iterations;
    }


    public void setKeyLength(int keyLength)
    {
        this.keyLength = keyLength;
    }


    public void setKeyAlgorithm(String keyAlgorithm)
    {
        this.keyAlgorithm = keyAlgorithm;
    }


    public void setSaltAlgorithm(String saltAlgorithm)
    {
        this.saltAlgorithm = saltAlgorithm;
    }


    public void setSaltLength(int saltLength)
    {
        this.saltLength = saltLength;
    }
}
