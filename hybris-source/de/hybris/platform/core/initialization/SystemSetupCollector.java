package de.hybris.platform.core.initialization;

import java.util.List;
import java.util.Map;

public interface SystemSetupCollector
{
    boolean hasProjectData(String paramString);


    boolean hasEssentialData(String paramString);


    boolean hasParameter(String paramString);


    List<SystemSetupParameter> getParameterMap(String paramString);


    Map<String, String[]> getDefaultParameterMap(String paramString);


    void executeMethods(SystemSetupContext paramSystemSetupContext);


    List<SystemSetupCollectorResult> getApplicablePatches(String paramString);
}
