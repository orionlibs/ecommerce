package com.hybris.encryption.asymmetric;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface AsymmetricEncryptor
{
    String encrypt(String paramString, PublicKey paramPublicKey);


    String decrypt(String paramString, PrivateKey paramPrivateKey);
}
