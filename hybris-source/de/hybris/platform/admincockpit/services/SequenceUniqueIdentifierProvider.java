package de.hybris.platform.admincockpit.services;

public interface SequenceUniqueIdentifierProvider
{
    String generateSequence(String paramString);


    String generateSequence(String paramString, int paramInt);


    String generateSequence(String paramString1, String paramString2);


    String generateSequence(String paramString1, String paramString2, int paramInt);
}
