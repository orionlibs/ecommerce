package com.hybris.encryption.asymmetric.impl;

import com.google.common.base.Preconditions;
import com.hybris.encryption.asymmetric.KeyPairManager;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

public class RSAKeyPairManager implements KeyPairManager
{
    private static final Logger LOG = Logger.getLogger(RSAKeyPairManager.class);
    private static final String ALGORITHM = "RSA";


    public RSAPrivateKey getPrivateKey(InputStream privateKey)
    {
        Preconditions.checkArgument((privateKey != null), "privateKey input stream required");
        try
        {
            byte[] keyBytes = IOUtils.toByteArray(privateKey);
            PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return (RSAPrivateKey)keyFactory.generatePrivate(spec);
        }
        catch(InvalidKeySpecException | NoSuchAlgorithmException | java.io.IOException e)
        {
            LOG.error("Problem with getting private key  [reason: " + e.getMessage() + "]");
            return null;
        }
    }


    public RSAPublicKey getPublicKey(InputStream publicKey)
    {
        Preconditions.checkArgument((publicKey != null), "publicKey input stream required");
        try
        {
            byte[] keyBytes = IOUtils.toByteArray(publicKey);
            X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPublicKey pubKey = (RSAPublicKey)factory.generatePublic(publicKeySpec);
            return pubKey;
        }
        catch(NoSuchAlgorithmException | InvalidKeySpecException | java.io.IOException e)
        {
            LOG.error("Problem with getting public key [reason: " + e.getMessage() + "]");
            return null;
        }
    }


    public RSAPrivateKey getPrivateKey(String privateKeyFileName)
    {
        Preconditions.checkArgument((privateKeyFileName != null), "privateKeyFileName is required");
        InputStream inputStream = RSAKeyPairManager.class.getResourceAsStream(privateKeyFileName);
        Preconditions.checkState((inputStream != null), "Cannot locate " + privateKeyFileName + " in the classloader");
        return getPrivateKey(inputStream);
    }


    public RSAPublicKey getPublicKey(String publicKeyFileName)
    {
        Preconditions.checkArgument((publicKeyFileName != null), "publicKeyFileName is required");
        InputStream inputStream = RSAKeyPairManager.class.getResourceAsStream(publicKeyFileName);
        Preconditions.checkState((inputStream != null), "Cannot locate " + publicKeyFileName + " in the classloader");
        return getPublicKey(inputStream);
    }
}
