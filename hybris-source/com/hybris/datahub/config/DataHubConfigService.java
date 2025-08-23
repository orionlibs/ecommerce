package com.hybris.datahub.config;

public interface DataHubConfigService
{
    boolean isBatchMode();


    DataHubConfigMode getConfigMode();
}
