package de.hybris.platform.servicelayer.keygenerator;

public interface KeyGenerator
{
    Object generate();


    Object generateFor(Object paramObject);


    void reset();
}
