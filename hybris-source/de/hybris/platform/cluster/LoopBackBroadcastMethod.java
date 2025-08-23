package de.hybris.platform.cluster;

import java.net.InetAddress;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

public class LoopBackBroadcastMethod extends AbstractBroadcastMethod
{
    private static final Logger LOG = Logger.getLogger(LoopBackBroadcastMethod.class);
    private InetAddress localAddr;


    public void init(BroadcastService service)
    {
        try
        {
            this.localAddr = InetAddress.getLocalHost();
        }
        catch(UnknownHostException e)
        {
            LOG.warn("cannot get address for local host", e);
        }
    }


    public void send(RawMessage message)
    {
        message.setRemoteAddress(this.localAddr);
        notifyMessgageReceived(message);
    }
}
