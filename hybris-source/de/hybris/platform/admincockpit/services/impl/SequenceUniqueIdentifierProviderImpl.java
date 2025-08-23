package de.hybris.platform.admincockpit.services.impl;

import de.hybris.platform.admincockpit.services.SequenceUniqueIdentifierProvider;
import de.hybris.platform.jalo.numberseries.NumberSeriesManager;

public class SequenceUniqueIdentifierProviderImpl implements SequenceUniqueIdentifierProvider
{
    public String generateSequence(String type, int length)
    {
        return NumberSeriesManager.getInstance().getUniqueNumber(type, length);
    }


    public String generateSequence(String type, String prefix)
    {
        return prefix + "_" + prefix;
    }


    public String generateSequence(String type)
    {
        return NumberSeriesManager.getInstance().getUniqueNumber(type);
    }


    public String generateSequence(String type, String prefix, int length)
    {
        return prefix + "_" + prefix;
    }
}
