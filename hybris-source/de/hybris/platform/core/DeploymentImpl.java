package de.hybris.platform.core;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

public interface DeploymentImpl extends Serializable
{
    Map<String, String> getJavaTypeMapping(String paramString);


    String getColumnDefinition(String paramString1, String paramString2);


    ItemDeployment getItemDeployment(String paramString);


    Set<String> getBeanIDs();
}
