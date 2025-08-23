package de.hybris.bootstrap.loader.metrics.internal;

import de.hybris.bootstrap.loader.metrics.ClasspathLocationUsage;
import de.hybris.bootstrap.loader.metrics.ClasspathReorderingResult;
import de.hybris.bootstrap.loader.metrics.EventType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;

public class ClasspathEfficiencyEvaluator
{
    public List<ClasspathLocationUsage> findLoadedByLocation(Map<EventCounterKey, LongAdder> events, Map<String, ResourceInfo> resourcesInfo)
    {
        Map<String, Integer> classesLoadedFromLocation = new HashMap<>();
        for(Map.Entry<EventCounterKey, LongAdder> event : events.entrySet())
        {
            if(((EventCounterKey)event.getKey()).getType() == EventType.FOUND_ON_CLASSPATH)
            {
                String name = ((EventCounterKey)event.getKey()).getName();
                String location = ((ResourceInfo)resourcesInfo.get(name)).getLocation();
                Integer loadedFromLocation = classesLoadedFromLocation.getOrDefault(location, Integer.valueOf(0));
                classesLoadedFromLocation.put(location, Integer.valueOf(loadedFromLocation.intValue() + 1));
            }
        }
        List<ClasspathLocationUsage> classpathLocationUsages = convertToLocationUsage(classesLoadedFromLocation);
        classpathLocationUsages.sort(Comparator.<ClasspathLocationUsage>comparingInt(ClasspathLocationUsage::getUsage).reversed());
        return classpathLocationUsages;
    }


    private List<ClasspathLocationUsage> convertToLocationUsage(Map<String, Integer> classesLoadedFromLocation)
    {
        List<ClasspathLocationUsage> locationUsages = new ArrayList<>();
        for(Map.Entry<String, Integer> e : classesLoadedFromLocation.entrySet())
        {
            locationUsages.add(new ClasspathLocationUsage(e.getKey(), ((Integer)e.getValue()).intValue()));
        }
        return locationUsages;
    }


    public int calculateTraversalCost(Map<EventCounterKey, LongAdder> events, Map<String, ResourceInfo> resourcesInfo, List<String> orderedClasspath)
    {
        int traversalCost = 0;
        Map<String, Integer> classpathOrder = getClasspathLocationsOrder(orderedClasspath);
        int classPathSize = classpathOrder.size();
        for(Map.Entry<EventCounterKey, LongAdder> event : events.entrySet())
        {
            if(((EventCounterKey)event.getKey()).getType() == EventType.FOUND_ON_CLASSPATH)
            {
                String name = ((EventCounterKey)event.getKey()).getName();
                String location = ((ResourceInfo)resourcesInfo.get(name)).getLocation();
                traversalCost += ((Integer)classpathOrder.get(location)).intValue();
                continue;
            }
            if(((EventCounterKey)event.getKey()).getType() == EventType.NOT_FOUND_ON_CLASSPATH)
            {
                traversalCost += classPathSize * ((LongAdder)event.getValue()).intValue();
            }
        }
        return traversalCost;
    }


    private Map<String, Integer> getClasspathLocationsOrder(List<String> classpath)
    {
        Map<String, Integer> entriesOrder = new HashMap<>();
        int order = 0;
        for(String entry : classpath)
        {
            entriesOrder.put(entry, Integer.valueOf(order));
            order++;
        }
        return entriesOrder;
    }


    public ClasspathReorderingResult reorderClasspath(List<String> classpath, Map<EventCounterKey, LongAdder> events, Map<String, ResourceInfo> resourcesInfo)
    {
        int currentTraversalCost = calculateTraversalCost(events, resourcesInfo, classpath);
        List<ClasspathLocationUsage> loadedByLocation = findLoadedByLocation(events, resourcesInfo);
        Collections.reverse(loadedByLocation);
        List<String> reorderedClasspath = reorderClasspath(classpath, loadedByLocation);
        int reorderedTraversalCost = calculateTraversalCost(events, resourcesInfo, reorderedClasspath);
        return new ClasspathReorderingResult(classpath, currentTraversalCost, reorderedClasspath, reorderedTraversalCost);
    }


    private List<String> reorderClasspath(List<String> classpath, List<ClasspathLocationUsage> loadedByLocation)
    {
        List<String> copiedClasspath = new ArrayList<>(classpath);
        copiedClasspath.removeAll((Collection)loadedByLocation.stream().map(i -> i.getLocation()).collect(Collectors.toList()));
        for(ClasspathLocationUsage location : loadedByLocation)
        {
            copiedClasspath.add(0, location.getLocation());
        }
        return copiedClasspath;
    }
}
