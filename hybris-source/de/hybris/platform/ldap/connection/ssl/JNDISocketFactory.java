package de.hybris.platform.ldap.connection.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import javax.naming.NamingException;
import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.apache.log4j.Logger;

public class JNDISocketFactory extends SSLSocketFactory
{
    private static final Logger LOG = Logger.getLogger(JNDISocketFactory.class.getName());
    private static SSLSocketFactory factory = null;
    private static JNDISocketFactory default_factory = null;
    private static KeyStore clientKeystore;
    private static final String DEFAULT_KEYSTORE_TYPE = "JKS";
    private static final String PKI_INTERNAL_TYPE = "KSE";
    private static ClassLoader myClassLoader = null;


    public static void setClassLoader(ClassLoader newLoader)
    {
        myClassLoader = newLoader;
    }


    private static ClassLoader getClassLoader()
    {
        if(myClassLoader == null)
        {
            myClassLoader = ClassLoader.getSystemClassLoader();
        }
        return myClassLoader;
    }


    public static void setDebugOn()
    {
        System.setProperty("javax.net.debug", "ssl handshake verbose");
    }


    public static void init(String caKeystoreFile, String clientKeystoreFile, char[] caPassphrase, char[] clientPassphrase, String caKeystoreType, String clientKeystoreType) throws NamingException
    {
        if(LOG.isDebugEnabled())
        {
            LOG.debug("caKeystoreFile: " + caKeystoreFile);
            LOG.debug("clientKeystoreFile: " + clientKeystoreFile);
            LOG.debug("caKeystoreType: " + caKeystoreType);
            LOG.debug("clientKeystoreType: " + clientKeystoreType);
        }
        if(default_factory != null)
        {
            return;
        }
        try
        {
            checkFileSanity(caKeystoreFile, clientKeystoreFile, clientPassphrase);
            if(caKeystoreFile == null)
            {
                caKeystoreFile = clientKeystoreFile;
            }
            String protocol = System.getProperty("sslversion", "TLS");
            if(!"TLS".equals(protocol))
            {
                LOG.warn("SECURITY: Using non-standard ssl version: '" + protocol + "'");
            }
            SSLContext sslctx = SSLContext.getInstance(protocol);
            KeyManagerFactory clientKeyManagerFactory = null;
            if(clientPassphrase != null && clientPassphrase.length > 0)
            {
                if("KSE".equals(clientKeystoreType))
                {
                    try
                    {
                        Class<?> clazz = getClassLoader().loadClass("de.hybris.platform.ldap.connection.ssl.ParsePkcs12");
                        if(clazz == null)
                        {
                            LOG.error("PKI internal error");
                            return;
                        }
                        Constructor<?> constructor = clazz.getConstructor(new Class[] {String.class, byte[].class});
                        int size = clientPassphrase.length;
                        byte[] password = new byte[size];
                        for(int i = 0; i < size; i++)
                        {
                            password[i] = (byte)clientPassphrase[i];
                        }
                        Object pkcs12Parser = constructor.newInstance(new Object[] {clientKeystoreFile, password});
                        Method getSunKeyStore = clazz.getMethod("getSunKeyStore", new Class[] {String.class});
                        clientKeystore = (KeyStore)getSunKeyStore.invoke(pkcs12Parser, new Object[] {"MyFriend"});
                        for(int j = 0; j < size; j++)
                        {
                            password[j] = 0;
                        }
                    }
                    catch(Exception e)
                    {
                        LOG.error("unable to load pkcs12 parser (not in class path?)", e);
                        return;
                    }
                }
                else
                {
                    if(clientKeystoreType == null)
                    {
                        clientKeystoreType = "JKS";
                    }
                    clientKeystore = KeyStore.getInstance(clientKeystoreType);
                    if(clientKeystoreFile != null)
                    {
                        clientKeystore.load(new FileInputStream(clientKeystoreFile), clientPassphrase);
                    }
                }
                clientKeyManagerFactory = KeyManagerFactory.getInstance("SunX509");
                clientKeyManagerFactory.init(clientKeystore, clientPassphrase);
            }
            KeyManager[] keyManagers = null;
            if(clientKeyManagerFactory != null)
            {
                keyManagers = clientKeyManagerFactory.getKeyManagers();
            }
            if(caKeystoreType == null)
            {
                caKeystoreType = "JKS";
            }
            KeyStore caKeystore = KeyStore.getInstance(caKeystoreType);
            if(caKeystoreFile != null)
            {
                caKeystore.load(new FileInputStream(caKeystoreFile), caPassphrase);
            }
            TrustManagerFactory caTrustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            caTrustManagerFactory.init(caKeystore);
            TrustManager[] trustManagers = caTrustManagerFactory.getTrustManagers();
            sslctx.init(keyManagers, trustManagers, null);
            synchronized(JNDISocketFactory.class)
            {
                factory = sslctx.getSocketFactory();
                default_factory = new JNDISocketFactory();
            }
        }
        catch(GeneralSecurityException e)
        {
            NamingException namingException = new NamingException("security error: unable to initialise JNDISocketFactory");
            namingException.initCause(e);
            throw namingException;
        }
        catch(IOException e)
        {
            NamingException namingException = new NamingException("file access error: unable to initialise JNDISocketFactory");
            namingException.initCause(e);
            throw namingException;
        }
    }


    private static void checkFileSanity(String caKeystoreFile, String clientKeystoreFile, char[] clientPassphrase) throws NamingException
    {
        if(clientKeystoreFile == null && caKeystoreFile == null)
        {
            throw new NamingException("SSL Initialisation error: No valid keystore files available.");
        }
        if(caKeystoreFile != null)
        {
            if(!(new File(caKeystoreFile)).exists())
            {
                throw new NamingException("SSL Initialisation error: file '" + caKeystoreFile + "' does not exist.");
            }
        }
        if(clientKeystoreFile != null && clientPassphrase != null)
        {
            if(!(new File(clientKeystoreFile)).exists())
            {
                throw new NamingException("SSL Initialisation error: file '" + clientKeystoreFile + "' does not exist.");
            }
        }
    }


    public static SocketFactory getDefault()
    {
        synchronized(JNDISocketFactory.class)
        {
            if(default_factory == null)
            {
                default_factory = new JNDISocketFactory();
            }
        }
        return default_factory;
    }


    public static KeyStore getClientKeyStore()
    {
        return clientKeystore;
    }


    public Socket createSocket() throws IOException, UnknownHostException
    {
        return factory.createSocket();
    }


    public Socket createSocket(String host, int port) throws IOException, UnknownHostException
    {
        return factory.createSocket(host, port);
    }


    public Socket createSocket(InetAddress host, int port) throws IOException, UnknownHostException
    {
        return factory.createSocket(host, port);
    }


    public Socket createSocket(InetAddress host, int port, InetAddress client_host, int client_port) throws IOException, UnknownHostException
    {
        return factory.createSocket(host, port, client_host, client_port);
    }


    public Socket createSocket(String host, int port, InetAddress client_host, int client_port) throws IOException, UnknownHostException
    {
        return factory.createSocket(host, port, client_host, client_port);
    }


    public Socket createSocket(Socket socket, String host, int port, boolean autoclose) throws IOException, UnknownHostException
    {
        return factory.createSocket(socket, host, port, autoclose);
    }


    public String[] getDefaultCipherSuites()
    {
        return factory.getDefaultCipherSuites();
    }


    public String[] getSupportedCipherSuites()
    {
        return factory.getSupportedCipherSuites();
    }
}
