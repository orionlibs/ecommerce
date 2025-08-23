package com.hybris.encryption.symmetric.impl;

import com.google.common.base.Preconditions;
import com.hybris.encryption.EncryptionUtils;
import com.hybris.encryption.symmetric.SymmetricEncryptor;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.UrlBase64;

public class AESEncryptor implements SymmetricEncryptor
{
    private static final Logger LOG = Logger.getLogger(AESEncryptor.class);
    private static final SecureRandom random = new SecureRandom();
    private final int keySpecIterCount;


    public AESEncryptor()
    {
        this(1024);
    }


    public AESEncryptor(int keySpecIterCount)
    {
        EncryptionUtils.registerBouncyCastlePrvider();
        this.keySpecIterCount = keySpecIterCount;
    }


    public String encrypt(String plainText, String key)
    {
        Preconditions.checkArgument((plainText != null), "plainText is required");
        Preconditions.checkArgument((key != null), "key is required");
        try
        {
            byte[] iv = new byte[16];
            random.nextBytes(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            cipher.init(1, deriveKey(key, iv), new IvParameterSpec(iv));
            byte[] ciphertext = cipher.doFinal(plainText.getBytes("UTF-8"));
            String saltString = new String(UrlBase64.encode(iv));
            String ciphertextString = new String(UrlBase64.encode(ciphertext));
            StringBuilder sb = new StringBuilder();
            sb.append(saltString).append(ciphertextString);
            return sb.toString();
        }
        catch(Exception e)
        {
            LOG.error("cannot encrypt provided plain text due to: " + e.getMessage());
            return null;
        }
    }


    public String decrypt(String cryptedText, String key)
    {
        Preconditions.checkArgument((cryptedText != null), "cryptedText is required");
        Preconditions.checkArgument((key != null), "key is required");
        try
        {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
            byte[] salt = UrlBase64.decode(cryptedText.substring(0, 24));
            byte[] cryptedMessage = UrlBase64.decode(cryptedText.substring(24, cryptedText.length()));
            cipher.init(2, deriveKey(key, salt), new IvParameterSpec(salt));
            return new String(cipher.doFinal(cryptedMessage));
        }
        catch(Exception e)
        {
            LOG.error("cannot decrypt provided crypted text due to: " + e.getMessage(), e);
            return null;
        }
    }


    private SecretKey deriveKey(String key, byte[] salt) throws NoSuchAlgorithmException, InvalidKeySpecException
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        KeySpec spec = new PBEKeySpec(key.toCharArray(), salt, this.keySpecIterCount, 128);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }
}
