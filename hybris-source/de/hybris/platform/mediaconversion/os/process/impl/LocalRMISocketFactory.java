package de.hybris.platform.mediaconversion.os.process.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.AccessException;
import java.rmi.server.RMISocketFactory;
import org.apache.log4j.Logger;

class LocalRMISocketFactory extends RMISocketFactory
{
    private static final Logger LOG = Logger.getLogger(LocalRMISocketFactory.class);
    private final InetAddress loopbackAddress;


    LocalRMISocketFactory(String loopbackName) throws AccessException, UnknownHostException
    {
        this(InetAddress.getByName(loopbackName));
    }


    LocalRMISocketFactory(InetAddress loopbackAddress) throws AccessException
    {
        this.loopbackAddress = loopbackAddress;
        if(!this.loopbackAddress.isLoopbackAddress())
        {
            throw new IllegalArgumentException("'" + this.loopbackAddress + "' is not a loopback InetAddress.");
        }
    }


    public Socket createSocket(String host, int port) throws IOException
    {
        LOG.debug("Client request for '" + host + "'.");
        InetAddress hostInet = InetAddress.getByName(host);
        if(getLoopbackAddress().equals(hostInet))
        {
            return socketFactory().createSocket(host, port);
        }
        throw new AccessException("Connection for host '" + host + "' not allowed.");
    }


    public ServerSocket createServerSocket(int port) throws IOException
    {
        LOG.info("Creating server socket at '" + getLoopbackAddress() + ":" + port + "'.");
        return new ServerSocket(port, 50, getLoopbackAddress());
    }


    private RMISocketFactory socketFactory()
    {
        RMISocketFactory ret = RMISocketFactory.getSocketFactory();
        LOG.debug("SocketFactory: " + ret);
        return (ret == null) ? RMISocketFactory.getDefaultSocketFactory() : ret;
    }


    InetAddress getLoopbackAddress()
    {
        return this.loopbackAddress;
    }
}
