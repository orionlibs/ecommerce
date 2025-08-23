package com.hybris.encryption.asymmetric.impl;

import com.google.common.base.Preconditions;
import com.hybris.encryption.EncryptionUtils;
import com.hybris.encryption.asymmetric.AsymmetricEncryptor;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import javax.crypto.Cipher;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.UrlBase64;

public class RSAEncryptor implements AsymmetricEncryptor
{
    private static final Logger LOG = Logger.getLogger(RSAEncryptor.class);


    public RSAEncryptor()
    {
        EncryptionUtils.registerBouncyCastlePrvider();
    }


    public String encrypt(String plainText, PublicKey key)
    {
        Preconditions.checkArgument((plainText != null), "plainText is required");
        Preconditions.checkArgument((key != null), "key is required");
        try
        {
            Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1PADDING", "BC");
            cipher.init(1, key);
            byte[] bytes = cipher.doFinal(plainText.getBytes("UTF-8"));
            return new String(UrlBase64.encode(bytes));
        }
        catch(NoSuchAlgorithmException | java.security.NoSuchProviderException | javax.crypto.NoSuchPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.BadPaddingException | java.io.UnsupportedEncodingException | java.security.InvalidKeyException e)
        {
            LOG.error("cannot encrypt provided plain text due to: " + e.getMessage());
            return null;
        }
    }


    public String decrypt(String cryptedText, PrivateKey key)
    {
        Preconditions.checkArgument((cryptedText != null), "cryptedText is required");
        Preconditions.checkArgument((key != null), "key is required");
        try
        {
            Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1PADDING", "BC");
            cipher.init(2, key);
            byte[] bytes = cipher.doFinal(UrlBase64.decode(cryptedText));
            return new String(bytes);
        }
        catch(NoSuchAlgorithmException | java.security.InvalidKeyException | javax.crypto.BadPaddingException | javax.crypto.IllegalBlockSizeException | javax.crypto.NoSuchPaddingException | java.security.NoSuchProviderException e)
        {
            LOG.error("cannot decrypt provided crypted text due to: " + e.getMessage(), e);
            return null;
        }
    }
}
