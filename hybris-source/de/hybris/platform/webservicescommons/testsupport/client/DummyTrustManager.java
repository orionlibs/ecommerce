package de.hybris.platform.webservicescommons.testsupport.client;

import java.net.Socket;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;

public class DummyTrustManager extends X509ExtendedTrustManager
{
    private static final X509Certificate[] ACCEPTED_ISSUERS = new X509Certificate[0];


    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
    {
    }


    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
    {
    }


    public X509Certificate[] getAcceptedIssuers()
    {
        return ACCEPTED_ISSUERS;
    }


    public void checkClientTrusted(X509Certificate[] arg0, String arg1, Socket arg2)
    {
    }


    public void checkClientTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2)
    {
    }


    public void checkServerTrusted(X509Certificate[] arg0, String arg1, Socket arg2)
    {
    }


    public void checkServerTrusted(X509Certificate[] arg0, String arg1, SSLEngine arg2)
    {
    }
}
