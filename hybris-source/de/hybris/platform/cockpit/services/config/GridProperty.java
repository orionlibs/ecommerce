package de.hybris.platform.cockpit.services.config;

import java.util.Map;

public interface GridProperty
{
    String getPrefix();


    String getQualifier();


    Map<String, String> getParameters();
}
