package de.hybris.platform.servicelayer.config;

import java.util.Map;
import java.util.Properties;

public interface ConfigurationViewService
{
    Properties readSystemProperties();


    Map<String, String> readEnvVariables();


    Map<String, String> readConfigParameters();
}
