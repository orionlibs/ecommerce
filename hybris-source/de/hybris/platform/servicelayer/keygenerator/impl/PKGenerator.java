package de.hybris.platform.servicelayer.keygenerator.impl;

import de.hybris.platform.core.PK;
import de.hybris.platform.servicelayer.keygenerator.KeyGenerator;

public class PKGenerator implements KeyGenerator
{
    public Object generate()
    {
        throw new UnsupportedOperationException("A PK can only be generated with a provided typecode. Please use the generateFor method passing a valid typecode.");
    }


    public Object generateFor(Object object)
    {
        if(!(object instanceof Integer))
        {
            throw new IllegalArgumentException("Given typecode format is not supported, please provide a typecode as Integer or Long.");
        }
        return PK.createCounterPK(((Integer)object).intValue());
    }


    public void reset()
    {
        throw new UnsupportedOperationException("PK generation can not be reseted");
    }
}
