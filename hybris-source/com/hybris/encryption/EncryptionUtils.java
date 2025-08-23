package com.hybris.encryption;

import java.security.Provider;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class EncryptionUtils
{
    private static final String BC_PROVIDER_CLASS = "org.bouncycastle.jce.provider.BouncyCastleProvider";


    public static void registerBouncyCastlePrvider()
    {
        try
        {
            if(Security.getProvider("org.bouncycastle.jce.provider.BouncyCastleProvider") == null)
            {
                Security.insertProviderAt((Provider)new BouncyCastleProvider(), 0);
            }
        }
        catch(Exception e)
        {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
