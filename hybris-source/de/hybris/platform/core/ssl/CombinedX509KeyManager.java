package de.hybris.platform.core.ssl;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Objects;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.X509KeyManager;

class CombinedX509KeyManager implements X509KeyManager
{
    private final X509KeyManager mainManager;
    private final X509KeyManager fallbackManager;


    public static CombinedX509KeyManager create(KeyManagerFactory mainFactory, KeyManagerFactory fallbackFactory)
    {
        X509KeyManager mainManager = getFirstX509KeyManager(mainFactory);
        X509KeyManager fallbackManager = getFirstX509KeyManager(fallbackFactory);
        if(Objects.isNull(fallbackManager))
        {
            throw new IllegalArgumentException("fallbackFactory must contain X509KeyManager");
        }
        return new CombinedX509KeyManager(mainManager, fallbackManager);
    }


    CombinedX509KeyManager(X509KeyManager mainManager, X509KeyManager fallbackManager)
    {
        this.mainManager = mainManager;
        this.fallbackManager = Objects.<X509KeyManager>requireNonNull(fallbackManager, "fallbackManager mustn't be null");
    }


    public String[] getServerAliases(String keyType, Principal[] issuers)
    {
        return (String[])Utils.mergeArrays(String.class, m -> m.getServerAliases(keyType, issuers), (Object[])new X509KeyManager[] {this.mainManager, this.fallbackManager});
    }


    public PrivateKey getPrivateKey(String alias)
    {
        return (PrivateKey)Utils.getFirstOrNull(m -> m.getPrivateKey(alias), (Object[])new X509KeyManager[] {this.mainManager, this.fallbackManager});
    }


    public String[] getClientAliases(String keyType, Principal[] issuers)
    {
        return (String[])Utils.mergeArrays(String.class, m -> m.getClientAliases(keyType, issuers), (Object[])new X509KeyManager[] {this.mainManager, this.fallbackManager});
    }


    public X509Certificate[] getCertificateChain(String alias)
    {
        return (X509Certificate[])Utils.mergeArrays(X509Certificate.class, m -> m.getCertificateChain(alias), (Object[])new X509KeyManager[] {this.mainManager, this.fallbackManager});
    }


    public String chooseServerAlias(String keyType, Principal[] issuers, Socket socket)
    {
        return (String)Utils.getFirstOrNull(m -> m.chooseServerAlias(keyType, issuers, socket), (Object[])new X509KeyManager[] {this.mainManager, this.fallbackManager});
    }


    public String chooseClientAlias(String[] keyType, Principal[] issuers, Socket socket)
    {
        return (String)Utils.getFirstOrNull(m -> m.chooseClientAlias(keyType, issuers, socket), (Object[])new X509KeyManager[] {this.mainManager, this.fallbackManager});
    }


    private static X509KeyManager getFirstX509KeyManager(KeyManagerFactory keyManagerFactory)
    {
        return (X509KeyManager)Utils.getFirstInstanceOfOrNull(X509KeyManager.class, (Object[])keyManagerFactory.getKeyManagers());
    }
}
