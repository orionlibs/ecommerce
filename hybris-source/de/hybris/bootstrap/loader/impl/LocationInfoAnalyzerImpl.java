package de.hybris.bootstrap.loader.impl;

import de.hybris.bootstrap.loader.ClassContainerLocationInfo;
import de.hybris.bootstrap.loader.ClassLocationInfo;
import de.hybris.bootstrap.loader.LocationInfoAnalyzer;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Pattern;

public final class LocationInfoAnalyzerImpl implements LocationInfoAnalyzer
{
    private final Map<String, List<ClassContainerLocationInfo>> jarInfos = new LinkedHashMap<>();
    private final Map<String, Set<ClassLocationInfo>> classInfos = new LinkedHashMap<>();
    private ClassLoaderInfoFactory factory = null;
    private static LocationInfoAnalyzer instance;
    private boolean initialized = false;


    public static synchronized LocationInfoAnalyzer getInstance()
    {
        if(instance == null)
        {
            instance = new LocationInfoAnalyzerImpl();
        }
        return instance;
    }


    private ClassLoaderInfoFactory getFactory()
    {
        if(this.factory == null)
        {
            try
            {
                this.factory = new ClassLoaderInfoFactory(this);
            }
            catch(Exception e)
            {
                throw new IllegalStateException("Could not instanciate factory " + e.getMessage(), e);
            }
        }
        return this.factory;
    }


    public void addClassContainerLocationInfo(ClassContainerLocationInfo info)
    {
        mergeClassLoaderScopes(this.jarInfos, this.classInfos, info);
        if(!this.initialized)
        {
            this.initialized = true;
        }
    }


    private synchronized void mergeClassLoaderScopes(Map<String, List<ClassContainerLocationInfo>> classContainerInfos, Map<String, Set<ClassLocationInfo>> classInfo, ClassContainerLocationInfo info)
    {
        if(info == null)
        {
            return;
        }
        List<ClassContainerLocationInfo> existingInfos = null;
        if(classContainerInfos.get(info.getWebAppName()) == null)
        {
            classContainerInfos.put(info.getWebAppName(), new ArrayList<>());
            existingInfos = classContainerInfos.get(info.getWebAppName());
        }
        else
        {
            existingInfos = classContainerInfos.get(info.getWebAppName());
        }
        if(!existingInfos.contains(info))
        {
            existingInfos.add(info);
            Set<ClassLocationInfo> classContent = classInfo.get(info.getWebAppName());
            if(classContent == null)
            {
                classContent = new LinkedHashSet<>(100);
            }
            classContent.addAll(info.getProvidedClasses());
            classInfo.put(info.getWebAppName(), classContent);
        }
    }


    public List<ClassContainerLocationInfo> getLoaderInfos(String app, Pattern pattern)
    {
        List<ClassContainerLocationInfo> arrayList = null;
        List<ClassContainerLocationInfo> resultArrayList = null;
        if(this.jarInfos.get(app) == null)
        {
            arrayList = new ArrayList<>();
        }
        else
        {
            arrayList = new ArrayList<>(((List)this.jarInfos.get(app)).size());
            for(ClassContainerLocationInfo singleLoaderInfo : this.jarInfos.get(app))
            {
                arrayList.add(singleLoaderInfo);
            }
        }
        if(!"platform".equals(app) && this.jarInfos.get("platform") != null)
        {
            arrayList.addAll(this.jarInfos.get("platform"));
        }
        resultArrayList = new ArrayList<>(arrayList.size());
        Set<String> jarNames = new LinkedHashSet<>();
        Map<String, Integer> duplicatedJarInfoCount = new LinkedHashMap<>();
        for(ClassContainerLocationInfo clInfo : arrayList)
        {
            if(!resultArrayList.contains(clInfo) && (pattern == null || pattern.matcher(clInfo.getJarName()).matches()))
            {
                if(jarNames.contains(clInfo.getJarName()))
                {
                    duplicatedJarInfoCount.put(clInfo.getJarName(), Integer.valueOf(0));
                }
                jarNames.add(clInfo.getJarName());
            }
        }
        for(ClassContainerLocationInfo clInfo : arrayList)
        {
            if(!resultArrayList.contains(clInfo) && (pattern == null || pattern.matcher(clInfo.getJarName()).matches()))
            {
                if(duplicatedJarInfoCount.keySet().contains(clInfo.getJarName()))
                {
                    Integer count = duplicatedJarInfoCount.get(clInfo.getJarName());
                    ClassContainerLocationInfo duplicated = getFactory().markDuplicatedClassLocationInfo(clInfo, count);
                    resultArrayList.add(duplicated);
                    duplicatedJarInfoCount.put(clInfo.getJarName(), Integer.valueOf(duplicated.getOccurrences()));
                    continue;
                }
                resultArrayList.add(clInfo);
            }
        }
        return resultArrayList;
    }


    public Map<String, Set<String>> getLocations(String app, Pattern pattern)
    {
        Set<ClassLocationInfo> preresult = new LinkedHashSet<>(this.classInfos.get("platform"));
        Map<String, Set<String>> mergedMap = new TreeMap<>();
        if(!"platform".equalsIgnoreCase(app) && this.classInfos.containsKey(app))
        {
            preresult.addAll(this.classInfos.get(app));
        }
        for(ClassLocationInfo single : preresult)
        {
            if(pattern == null || pattern.matcher(single.getClassName()).matches())
            {
                Set<String> location = null;
                if(mergedMap.containsKey(single.getClassName()))
                {
                    location = mergedMap.get(single.getClassName());
                }
                else
                {
                    location = new LinkedHashSet<>();
                }
                location.add(single.getJarLocation().toString());
                mergedMap.put(single.getClassName(), location);
            }
        }
        return mergedMap;
    }


    public ClassContainerLocationInfo createClassLocationInfo(String webAppName, ClassLoader loader, URL url)
    {
        if(this.factory == null)
        {
            try
            {
                this.factory = getFactory();
            }
            catch(Exception e)
            {
                throw new IllegalStateException("Could not instanciate factory " + e.getMessage(), e);
            }
        }
        return this.factory.createClassLoaderInfo(webAppName, loader, url);
    }


    public boolean isInitialized()
    {
        return this.initialized;
    }
}
