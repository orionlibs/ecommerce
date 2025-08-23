package de.hybris.platform.core.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.util.Objects;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class AdditionalTrustStore
{
    private final KeyStore trustStore;
    private final char[] passwordChars;


    public static AdditionalTrustStore createFromFile(File file, String password) throws GeneralSecurityException, IOException
    {
        Objects.requireNonNull(file, "file mustn't be null");
        char[] passwordChars = (password == null) ? null : password.toCharArray();
        KeyStore trustStore = KeyStore.getInstance("JKS");
        FileInputStream fileInputStream = new FileInputStream(file);
        try
        {
            trustStore.load(fileInputStream, passwordChars);
            fileInputStream.close();
        }
        catch(Throwable throwable)
        {
            try
            {
                fileInputStream.close();
            }
            catch(Throwable throwable1)
            {
                throwable.addSuppressed(throwable1);
            }
            throw throwable;
        }
        return new AdditionalTrustStore(trustStore, passwordChars);
    }


    private AdditionalTrustStore(KeyStore trustStore, char[] passwordChars)
    {
        this.trustStore = trustStore;
        this.passwordChars = passwordChars;
    }


    public SSLContext mergeWithTheDefaultSSLContext() throws GeneralSecurityException
    {
        Factories defaultFactories = getDefaultFactories();
        Factories trustStoreFactories = getFactoriesForTheTrustStore();
        CombinedX509KeyManager combinedX509KeyManager = CombinedX509KeyManager.create(defaultFactories.getKeyManagerFactory(), trustStoreFactories
                        .getKeyManagerFactory());
        CombinedX509TrustManager combinedX509TrustManager = CombinedX509TrustManager.create(defaultFactories.getTrustManagerFactory(), trustStoreFactories
                        .getTrustManagerFactory());
        SSLContext mergedCtx = SSLContext.getInstance("SSL");
        mergedCtx.init(new KeyManager[] {(KeyManager)combinedX509KeyManager}, new TrustManager[] {(TrustManager)combinedX509TrustManager}, null);
        return mergedCtx;
    }


    private Factories getDefaultFactories() throws GeneralSecurityException
    {
        return getFactories(null, null);
    }


    private Factories getFactoriesForTheTrustStore() throws GeneralSecurityException
    {
        return getFactories(this.trustStore, this.passwordChars);
    }


    private Factories getFactories(KeyStore keyStore, char[] passwordChars) throws GeneralSecurityException
    {
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, passwordChars);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                        TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        return new Factories(trustManagerFactory, keyManagerFactory);
    }
}
