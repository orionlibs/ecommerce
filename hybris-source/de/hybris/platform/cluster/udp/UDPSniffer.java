package de.hybris.platform.cluster.udp;

import de.hybris.platform.cluster.RawMessage;
import de.hybris.platform.core.Log4JUtils;
import de.hybris.platform.core.Registry;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class UDPSniffer
{
    static
    {
        Log4JUtils.startup();
    }

    private static final Logger log = Logger.getLogger(UDPSniffer.class);
    private long clusterIslandPK;
    private String interfaceName;
    private String networkInterfaceName;
    private String multicastaddress;
    private int port;


    public UDPSniffer()
    {
        readConfig();
    }


    protected void start()
    {
        InetAddress group = null;
        int buffersize = 1000;
        MulticastSocket multicastSocket = null;
        try
        {
            group = InetAddress.getByName(this.multicastaddress);
            multicastSocket = new MulticastSocket(this.port);
            if(!StringUtils.isEmpty(this.interfaceName))
            {
                try
                {
                    multicastSocket.setInterface(InetAddress.getByName(this.interfaceName));
                }
                catch(UnknownHostException e)
                {
                    log.error("invalid upd interface address '" + this.interfaceName + "' (error:" + e.getMessage() + ")", e);
                }
                catch(SocketException e)
                {
                    log.error("cannot set upd interface address '" + this.interfaceName + "' (error:" + e.getMessage() + ")", e);
                }
            }
            if(!StringUtils.isEmpty(this.networkInterfaceName))
            {
                try
                {
                    multicastSocket.setNetworkInterface(NetworkInterface.getByName(this.networkInterfaceName));
                }
                catch(SocketException e)
                {
                    log.error("invalid upd network interface '" + this.networkInterfaceName + "' (error:" + e.getMessage() + ")", e);
                }
            }
            log.info("UDP Multicast Receiver (udpsniff) configured with the following parameter: ");
            log.info("Interface: " + multicastSocket.getInterface() + ", NetworkInterface: " + multicastSocket
                            .getNetworkInterface().getDisplayName());
            log.info("Receiving packets from group " + group);
            multicastSocket.joinGroup(group);
            while(true)
            {
                byte[] buffer = new byte[1000];
                DatagramPacket datagramPacket = new DatagramPacket(buffer, buffer.length);
                multicastSocket.receive(datagramPacket);
                RawMessage msg = new RawMessage(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength());
                showStatus(datagramPacket, msg);
            }
        }
        catch(IOException e)
        {
            log.warn(e.getMessage(), e);
        }
        finally
        {
            if(multicastSocket != null)
            {
                try
                {
                    multicastSocket.leaveGroup(group);
                }
                catch(IOException e)
                {
                    log.debug(e.getMessage(), e);
                }
                multicastSocket.close();
            }
        }
    }


    protected void readConfig()
    {
        DefaultUDPBroadcastConfiguration defaultUDPBroadcastConfiguration = new DefaultUDPBroadcastConfiguration();
        this.multicastaddress = defaultUDPBroadcastConfiguration.getMulticastAddress();
        this.port = defaultUDPBroadcastConfiguration.getPort();
        this.interfaceName = defaultUDPBroadcastConfiguration.getInterfaceName();
        this.networkInterfaceName = defaultUDPBroadcastConfiguration.getNetworkInterfaceName();
        this.clusterIslandPK = Registry.getMasterTenant().getClusterIslandPK();
    }


    protected void showStatus(DatagramPacket datagramPacket, RawMessage msg)
    {
        try
        {
            if(this.clusterIslandPK == msg.getClusterIslandPK())
            {
                if(msg.getVersion() != 67174656)
                {
                    log.info("PACKET WITH WRONG VERSION: " + msg);
                }
                else
                {
                    log.info("" + datagramPacket.getAddress() + ":" + datagramPacket.getAddress());
                }
            }
            else if(log.isDebugEnabled())
            {
                log.debug("MESSAGE FROM OTHER SYSTEM: " + msg);
            }
        }
        catch(Exception e)
        {
            log.warn(e.getMessage(), e);
        }
    }


    public static void main(String[] args)
    {
        Registry.activateStandaloneMode();
        Registry.activateMasterTenant();
        (new UDPSniffer()).start();
    }
}
