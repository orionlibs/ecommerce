package com.hybris.encryption.asymmetric;

import java.io.InputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

public interface KeyPairManager
{
    RSAPrivateKey getPrivateKey(InputStream paramInputStream);


    RSAPublicKey getPublicKey(InputStream paramInputStream);


    RSAPrivateKey getPrivateKey(String paramString);


    RSAPublicKey getPublicKey(String paramString);
}
