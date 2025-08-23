package de.hybris.platform.persistence.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class DigestCalculator
{
    private final MessageDigest messageDigest;


    public static boolean isAlgorithmSupported(String algorithm)
    {
        if(algorithm == null)
        {
            return false;
        }
        try
        {
            MessageDigest.getInstance(algorithm);
        }
        catch(NoSuchAlgorithmException e)
        {
            return false;
        }
        return true;
    }


    public static DigestCalculator getInstance(String algorithm)
    {
        DigestCalculator result = null;
        try
        {
            result = new DigestCalculator(MessageDigest.getInstance(algorithm));
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new RuntimeException(e);
        }
        return result;
    }


    private DigestCalculator(MessageDigest messageDigest)
    {
        this.messageDigest = messageDigest;
    }


    public synchronized String calculateDigest(String plain)
    {
        StringBuilder result = new StringBuilder(this.messageDigest.getDigestLength());
        this.messageDigest.update(plain.getBytes());
        Formatter formatter = new Formatter(result);
        for(byte b : this.messageDigest.digest())
        {
            formatter.format("%02x", new Object[] {Byte.valueOf(b)});
        }
        formatter.close();
        return result.toString();
    }
}
