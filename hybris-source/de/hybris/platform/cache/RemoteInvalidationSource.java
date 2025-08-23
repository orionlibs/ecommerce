package de.hybris.platform.cache;

import java.io.Serializable;
import java.net.InetAddress;

public class RemoteInvalidationSource implements Serializable
{
    private final byte[] address;


    public RemoteInvalidationSource(InetAddress address)
    {
        this.address = (address != null) ? address.getAddress() : new byte[0];
    }


    public byte[] getAddress()
    {
        return this.address;
    }


    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < this.address.length; i++)
        {
            int addressByte = this.address[i] & 0xFF;
            stringBuilder.append(addressByte);
            if(i + 1 < this.address.length)
            {
                stringBuilder.append(".");
            }
        }
        return stringBuilder.toString();
    }
}
