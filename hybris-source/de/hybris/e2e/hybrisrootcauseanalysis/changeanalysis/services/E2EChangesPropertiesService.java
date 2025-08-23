package de.hybris.e2e.hybrisrootcauseanalysis.changeanalysis.services;

import java.util.Properties;

public interface E2EChangesPropertiesService
{
    public static final String FULLSTOP = ".";


    Properties getInfo();


    String getNameFile();


    boolean isSorted();
}
