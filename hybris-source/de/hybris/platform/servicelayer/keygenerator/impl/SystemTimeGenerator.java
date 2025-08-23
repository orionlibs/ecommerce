package de.hybris.platform.servicelayer.keygenerator.impl;

import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

public class SystemTimeGenerator implements KeyGenerator
{
    public Object generate()
    {
        return Long.valueOf(System.currentTimeMillis());
    }


    public Object generateFor(Object object)
    {
        throw new UnsupportedOperationException("Not supported, please call generate().");
    }


    public void reset()
    {
        throw new UnsupportedOperationException("A reset of system time is not supported.");
    }
}
