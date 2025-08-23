package de.hybris.bootstrap.loader.metrics;

import de.hybris.bootstrap.loader.metrics.internal.ClasspathEfficiencyEvaluator;
import de.hybris.bootstrap.loader.metrics.internal.EventCounterKey;
import de.hybris.bootstrap.loader.metrics.internal.RejectionRuleCounterKey;
import de.hybris.bootstrap.loader.metrics.internal.ResourceInfo;
import de.hybris.bootstrap.loader.metrics.internal.YURLClasspathProvider;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClassLoaderMetricRegistry implements ClassLoaderMetricEventListener
{
    private final Map<EventCounterKey, LongAdder> eventCounter = new ConcurrentHashMap<>();
    private final Map<RejectionRuleCounterKey, LongAdder> ruleCounter = new ConcurrentHashMap<>();
    private final Map<String, ResourceInfo> resourcesInfo = new ConcurrentHashMap<>();
    private final ClasspathEfficiencyEvaluator classpathEvaluator;
    private final YURLClasspathProvider classpathProvider;
    private final AtomicReference<List<String>> classpath = new AtomicReference<>();


    public ClassLoaderMetricRegistry()
    {
        this.classpathEvaluator = new ClasspathEfficiencyEvaluator();
        this.classpathProvider = new YURLClasspathProvider();
    }


    public ClassLoaderMetricRegistry(ClasspathEfficiencyEvaluator classpathEvaluator, YURLClasspathProvider yurlClasspathProvider)
    {
        this.classpathEvaluator = classpathEvaluator;
        this.classpathProvider = yurlClasspathProvider;
    }


    public void onEvent(ClassLoaderMetricEvent event)
    {
        storeResourceInfo(event);
        incrementEventCounter(event);
        incrementIgnoreRuleCounter(event);
    }


    private void storeResourceInfo(ClassLoaderMetricEvent event)
    {
        String normalizedSource = tryToMatchLocation(event.getSource());
        this.resourcesInfo.putIfAbsent(event.getName(), new ResourceInfo(event
                        .getName(), normalizedSource, event.getClassloader(), event
                        .getResourceType()));
    }


    private String tryToMatchLocation(String location)
    {
        if(location == null)
        {
            return null;
        }
        if(this.classpath.get() == null)
        {
            this.classpath.compareAndSet(null, this.classpathProvider.getClasspath());
        }
        String strippedLocation = stripLocation(location);
        for(String entry : this.classpath.get())
        {
            if(strippedLocation.startsWith(entry))
            {
                return entry;
            }
        }
        return null;
    }


    private String stripLocation(String location)
    {
        if(location.startsWith("file:"))
        {
            return location.substring("file:".length());
        }
        return location;
    }


    private void incrementIgnoreRuleCounter(ClassLoaderMetricEvent event)
    {
        if(event.getEventType() == EventType.REJECTED)
        {
            RejectionRuleCounterKey rejectionKey = new RejectionRuleCounterKey(event.getName(), event.getRejectedByRule());
            ((LongAdder)this.ruleCounter.computeIfAbsent(rejectionKey, i -> new LongAdder())).increment();
        }
    }


    private void incrementEventCounter(ClassLoaderMetricEvent event)
    {
        EventCounterKey eventKey = new EventCounterKey(event.getName(), event.getEventType());
        ((LongAdder)this.eventCounter.computeIfAbsent(eventKey, i -> new LongAdder())).increment();
    }


    public List<ClassLoaderMetric> findMetrics(MetricQueryCriteria criteria)
    {
        List<ClassLoaderMetric> metrics = findAllMetrics();
        List<ClassLoaderMetric> filteredMetrics = (List<ClassLoaderMetric>)metrics.stream().filter(i -> (criteria.getResourceType() == null || i.getResourceType() == criteria.getResourceType())).filter((Predicate)new ExcludedSuffixes(criteria.getExcludedSuffixes()))
                        .filter(i -> (i.getEventTypeCount(criteria.getEventType()) >= criteria.getMinimumMatching())).collect(Collectors.toList());
        filteredMetrics.sort(Comparator.comparingInt(i -> i.getEventTypeCount(criteria.getEventType())));
        if(criteria.isSortDesc())
        {
            Collections.reverse(filteredMetrics);
        }
        return filteredMetrics;
    }


    private List<ClassLoaderMetric> findAllMetrics()
    {
        List<ClassLoaderMetric> metrics = new ArrayList<>();
        Map<String, Map<EventType, Integer>> eventTypeCountForMetricMap = getEventTypeCountForMetricMap();
        Map<String, Map<String, Integer>> ignoredByRulesCountMap = getIgnoredByRulesCountMap();
        for(ResourceInfo info : this.resourcesInfo.values())
        {
            Map<EventType, Integer> eventTypeCount = eventTypeCountForMetricMap.get(info.getName());
            Map<String, Integer> ignoredByRulesCount = ignoredByRulesCountMap.get(info.getName());
            ClassLoaderMetric metric = ClassLoaderMetric.builder().withName(info.getName()).withSource(info.getLocation()).withClassloader(info.getClassLoader()).withResourceType(info.getType()).withEventTypeCount(eventTypeCount).withIgnoredByRulesCount(ignoredByRulesCount).build();
            metrics.add(metric);
        }
        return metrics;
    }


    private Map<String, Map<EventType, Integer>> getEventTypeCountForMetricMap()
    {
        Map<String, Map<EventType, Integer>> eventTypeCountMap = new HashMap<>();
        for(Map.Entry<EventCounterKey, LongAdder> event : this.eventCounter.entrySet())
        {
            EventCounterKey key = event.getKey();
            Map<EventType, Integer> eventTypeCount = eventTypeCountMap.getOrDefault(key.getName(), new HashMap<>());
            eventTypeCount.put(key.getType(), Integer.valueOf(((LongAdder)event.getValue()).intValue()));
            eventTypeCountMap.put(key.getName(), eventTypeCount);
        }
        return eventTypeCountMap;
    }


    private Map<String, Map<String, Integer>> getIgnoredByRulesCountMap()
    {
        Map<String, Map<String, Integer>> ignoredRulesCountMap = new HashMap<>();
        for(Map.Entry<RejectionRuleCounterKey, LongAdder> event : this.ruleCounter.entrySet())
        {
            RejectionRuleCounterKey key = event.getKey();
            Map<String, Integer> ignoredRulesCount = ignoredRulesCountMap.getOrDefault(key.getName(), new HashMap<>());
            ignoredRulesCount.put(key.getRule(), Integer.valueOf(((LongAdder)event.getValue()).intValue()));
            ignoredRulesCountMap.put(key.getName(), ignoredRulesCount);
        }
        return ignoredRulesCountMap;
    }


    public List<ClasspathLocationUsage> findLoadedByClasspathLocation()
    {
        return this.classpathEvaluator.findLoadedByLocation(this.eventCounter, this.resourcesInfo);
    }


    public ClasspathReorderingResult reorderClasspath()
    {
        return this.classpathEvaluator.reorderClasspath(this.classpath.get(), this.eventCounter, this.resourcesInfo);
    }
}
