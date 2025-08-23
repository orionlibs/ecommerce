package de.hybris.platform.oauth2.jwt.util;

import de.hybris.platform.oauth2.jwt.exceptions.KeyStoreProcessingException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import org.springframework.security.jwt.crypto.sign.Signer;

public interface KeyStoreHelper
{
    Signer getSigner(String paramString1, InputStream paramInputStream, String paramString2, String paramString3) throws KeyStoreProcessingException;


    KeyStore getKeyStore(InputStream paramInputStream, String paramString) throws KeyStoreProcessingException;


    PrivateKey getPrivateKey(KeyStore paramKeyStore, String paramString1, String paramString2) throws KeyStoreProcessingException;


    PublicKey getPublicKey(KeyStore paramKeyStore, String paramString) throws KeyStoreProcessingException;


    KeyStore getKeyStore(String paramString1, InputStream paramInputStream, String paramString2) throws KeyStoreProcessingException;


    String getModulusAsBase64String(byte[] paramArrayOfbyte);


    String getPublicExponentAsBase64String(byte[] paramArrayOfbyte);
}
