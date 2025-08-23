package com.hybris.encryption.symmetric;

public interface SymmetricEncryptor
{
    String encrypt(String paramString1, String paramString2);


    String decrypt(String paramString1, String paramString2);
}
