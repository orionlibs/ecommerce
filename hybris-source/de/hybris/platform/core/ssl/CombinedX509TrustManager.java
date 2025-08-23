package de.hybris.platform.core.ssl;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Objects;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

class CombinedX509TrustManager implements X509TrustManager
{
    private final X509TrustManager mainManager;
    private final X509TrustManager fallbackManager;


    public static CombinedX509TrustManager create(TrustManagerFactory mainFactory, TrustManagerFactory fallbackFactory)
    {
        X509TrustManager mainManager = getFirstX509TrustManager(mainFactory);
        X509TrustManager fallbackManager = getFirstX509TrustManager(fallbackFactory);
        if(Objects.isNull(fallbackManager))
        {
            throw new IllegalArgumentException("fallbackFactory must contain X509TrustManager");
        }
        return new CombinedX509TrustManager(mainManager, fallbackManager);
    }


    CombinedX509TrustManager(X509TrustManager mainManager, X509TrustManager fallbackManager)
    {
        this.mainManager = mainManager;
        this.fallbackManager = Objects.<X509TrustManager>requireNonNull(fallbackManager, "fallbackManager mustn't be null");
    }


    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
        try
        {
            this.fallbackManager.checkClientTrusted(chain, authType);
        }
        catch(CertificateException e)
        {
            if(this.mainManager == null)
            {
                throw e;
            }
            this.mainManager.checkClientTrusted(chain, authType);
        }
    }


    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
        try
        {
            this.fallbackManager.checkServerTrusted(chain, authType);
        }
        catch(CertificateException e)
        {
            if(this.mainManager == null)
            {
                throw e;
            }
            this.mainManager.checkServerTrusted(chain, authType);
        }
    }


    public X509Certificate[] getAcceptedIssuers()
    {
        return (X509Certificate[])Utils.mergeArrays(X509Certificate.class, X509TrustManager::getAcceptedIssuers, (Object[])new X509TrustManager[] {this.mainManager, this.fallbackManager});
    }


    private static X509TrustManager getFirstX509TrustManager(TrustManagerFactory trustManagerFactory)
    {
        return (X509TrustManager)Utils.getFirstInstanceOfOrNull(X509TrustManager.class, (Object[])trustManagerFactory.getTrustManagers());
    }
}
