package de.hybris.bootstrap.loader;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public interface LocationInfoAnalyzer
{
    void addClassContainerLocationInfo(ClassContainerLocationInfo paramClassContainerLocationInfo);


    List<ClassContainerLocationInfo> getLoaderInfos(String paramString, Pattern paramPattern);


    Map<String, Set<String>> getLocations(String paramString, Pattern paramPattern);


    ClassContainerLocationInfo createClassLocationInfo(String paramString, ClassLoader paramClassLoader, URL paramURL);


    boolean isInitialized();
}
