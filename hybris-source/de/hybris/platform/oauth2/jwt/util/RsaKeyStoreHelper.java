package de.hybris.platform.oauth2.jwt.util;

import de.hybris.platform.oauth2.jwt.exceptions.KeyStoreProcessingException;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.Signer;

public class RsaKeyStoreHelper implements KeyStoreHelper
{
    public static final String DEFAULT_INSTANCE_TYPE = "JKS";


    public KeyStore getKeyStore(InputStream inputStream, String password) throws KeyStoreProcessingException
    {
        return getKeyStore("JKS", inputStream, password);
    }


    public KeyStore getKeyStore(String instanceType, InputStream inputStream, String password) throws KeyStoreProcessingException
    {
        KeyStore keyStore;
        if(inputStream == null)
        {
            throw new KeyStoreProcessingException(" file not found");
        }
        try
        {
            keyStore = KeyStore.getInstance(StringUtils.isEmpty(instanceType) ? "JKS" : instanceType);
            char[] pass = password.toCharArray();
            keyStore.load(inputStream, pass);
        }
        catch(KeyStoreException | NoSuchAlgorithmException | java.security.cert.CertificateException | IOException e)
        {
            throw new KeyStoreProcessingException(e.getLocalizedMessage(), e);
        }
        finally
        {
            try
            {
                inputStream.close();
            }
            catch(IOException iOException)
            {
            }
        }
        return keyStore;
    }


    public String getModulusAsBase64String(byte[] modulus)
    {
        return Base64.encodeBase64URLSafeString(modulus);
    }


    public String getPublicExponentAsBase64String(byte[] publicExponent)
    {
        return Base64.encodeBase64URLSafeString(publicExponent);
    }


    public RSAPrivateKey getPrivateKey(KeyStore keyStore, String alias, String password) throws KeyStoreProcessingException
    {
        KeyStore.PrivateKeyEntry keyEnt;
        char[] pass = password.toCharArray();
        try
        {
            keyEnt = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, new KeyStore.PasswordProtection(pass));
        }
        catch(NoSuchAlgorithmException | java.security.UnrecoverableEntryException | KeyStoreException e)
        {
            throw new KeyStoreProcessingException(e.getLocalizedMessage(), e);
        }
        PrivateKey privateKey = keyEnt.getPrivateKey();
        if(privateKey instanceof RSAPrivateKey)
        {
            return (RSAPrivateKey)privateKey;
        }
        throw new KeyStoreProcessingException("Not a RSA Key");
    }


    public RSAPublicKey getPublicKey(KeyStore keyStore, String alias) throws KeyStoreProcessingException
    {
        PublicKey publicKey;
        try
        {
            publicKey = keyStore.getCertificate(alias).getPublicKey();
        }
        catch(KeyStoreException e)
        {
            throw new KeyStoreProcessingException(e.getLocalizedMessage(), e);
        }
        if(publicKey instanceof RSAPublicKey)
        {
            return (RSAPublicKey)publicKey;
        }
        throw new KeyStoreProcessingException("Not a RSA Key");
    }


    public Signer getSigner(String instanceType, InputStream inputStream, String alias, String password) throws KeyStoreProcessingException
    {
        KeyStore keyStore;
        if(StringUtils.isEmpty(instanceType))
        {
            keyStore = getKeyStore(inputStream, password);
        }
        else
        {
            keyStore = getKeyStore(instanceType, inputStream, password);
        }
        RSAPrivateKey privateKey = getPrivateKey(keyStore, alias, password);
        return (Signer)new RsaSigner(privateKey);
    }
}
