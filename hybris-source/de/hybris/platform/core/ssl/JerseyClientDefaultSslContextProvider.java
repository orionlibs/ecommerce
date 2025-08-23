package de.hybris.platform.core.ssl;

import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.client.spi.DefaultSslContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JerseyClientDefaultSslContextProvider implements DefaultSslContextProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(JerseyClientDefaultSslContextProvider.class);
    private static volatile boolean returnJVMDefaultSSLContext = false;


    public static void useJvmDefaultSslContext()
    {
        LOG.info("Jersey client will use jvm default SSLContext");
        returnJVMDefaultSSLContext = true;
    }


    public SSLContext getDefaultSslContext()
    {
        if(returnJVMDefaultSSLContext)
        {
            return getJvmDefaultSslContext();
        }
        return getJerseyDefaultSslContext();
    }


    private SSLContext getJvmDefaultSslContext()
    {
        try
        {
            return SSLContext.getDefault();
        }
        catch(NoSuchAlgorithmException e)
        {
            throw new IllegalStateException(e);
        }
    }


    private SSLContext getJerseyDefaultSslContext()
    {
        return SslConfigurator.getDefaultContext();
    }
}
