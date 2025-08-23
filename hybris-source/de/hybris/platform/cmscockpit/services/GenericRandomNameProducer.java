package de.hybris.platform.cmscockpit.services;

public interface GenericRandomNameProducer
{
    String generateSequence(String paramString);


    String generateSequence(String paramString, int paramInt);


    String generateSequence(String paramString1, String paramString2);


    String generateSequence(String paramString1, String paramString2, int paramInt);
}
