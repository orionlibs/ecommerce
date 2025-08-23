package de.hybris.bootstrap.loader.metrics.internal;

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.loader.metrics.ClasspathLocationUsage;
import de.hybris.bootstrap.loader.metrics.ClasspathReorderingResult;
import de.hybris.bootstrap.loader.metrics.EventType;
import de.hybris.bootstrap.loader.metrics.ResourceType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.LongAdder;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

@UnitTest
public class ClasspathEfficiencyEvaluatorTest
{
    private ClasspathEfficiencyEvaluator classpathEfficiencyEvaluator;
    private Map<String, ResourceInfo> resourceInfoMap;


    @Before
    public void setup()
    {
        this.classpathEfficiencyEvaluator = new ClasspathEfficiencyEvaluator();
        this.resourceInfoMap = new HashMap<>();
    }


    @Test
    public void shouldCalculateClasspathUsage()
    {
        registerClassInLocation("jar1_available", "jar1.jar");
        registerClassInLocation("jar1_available2", "jar1.jar");
        registerClassInLocation("jar2_available", "jar2.jar");
        registerClassInLocation("jar1_not_available", "jar1.jar");
        Map<EventCounterKey, LongAdder> events = new HashMap<>();
        events.put(new EventCounterKey("jar1_available", EventType.FOUND_ON_CLASSPATH), asAdder(1L));
        events.put(new EventCounterKey("jar1_available2", EventType.FOUND_ON_CLASSPATH), asAdder(1L));
        events.put(new EventCounterKey("jar2_available", EventType.FOUND_ON_CLASSPATH), asAdder(1L));
        events.put(new EventCounterKey("jar1_not_available", EventType.NOT_FOUND_ON_CLASSPATH), asAdder(1L));
        List<ClasspathLocationUsage> classpathLocationUsages = this.classpathEfficiencyEvaluator.findLoadedByLocation(events, this.resourceInfoMap);
        Assertions.assertThat(classpathLocationUsages).hasSize(2);
        Assertions.assertThat(classpathLocationUsages).extracting(i -> Integer.valueOf(i.getUsage())).containsExactly((Object[])new Integer[] {Integer.valueOf(2), Integer.valueOf(1)});
        Assertions.assertThat(classpathLocationUsages).extracting(i -> i.getLocation()).containsExactly((Object[])new String[] {"jar1.jar", "jar2.jar"});
    }


    @Test
    public void shouldCalculateTraversalCost()
    {
        registerClassInLocation("jar1_available", "jar1.jar");
        registerClassInLocation("jar1_not_available", "jar1.jar");
        Map<EventCounterKey, LongAdder> events = new HashMap<>();
        events.put(new EventCounterKey("jar1_available", EventType.FOUND_ON_CLASSPATH), asAdder(1L));
        int costWithoutMiss = this.classpathEfficiencyEvaluator.calculateTraversalCost(events, this.resourceInfoMap,
                        (List)ImmutableList.of("jar1.jar", "jar2.jar"));
        Assertions.assertThat(costWithoutMiss).isEqualTo(0);
        events.put(new EventCounterKey("jar1_not_available", EventType.NOT_FOUND_ON_CLASSPATH), asAdder(1L));
        int costWithMiss = this.classpathEfficiencyEvaluator.calculateTraversalCost(events, this.resourceInfoMap,
                        (List)ImmutableList.of("jar1.jar", "jar2.jar"));
        Assertions.assertThat(costWithMiss).isEqualTo(2);
    }


    @Test
    public void shouldCalculateTraversalCostForResourcesLaterInClasspath()
    {
        registerClassInLocation("jar1_available", "jar1.jar");
        registerClassInLocation("jar2_available", "jar2.jar");
        Map<EventCounterKey, LongAdder> events = new HashMap<>();
        events.put(new EventCounterKey("jar2_available", EventType.FOUND_ON_CLASSPATH), asAdder(1L));
        int costWithoutMiss = this.classpathEfficiencyEvaluator.calculateTraversalCost(events, this.resourceInfoMap,
                        (List)ImmutableList.of("jar1.jar", "jar2.jar"));
        Assertions.assertThat(costWithoutMiss).isEqualTo(1);
    }


    @Test
    public void shouldReorderClasspath()
    {
        registerClassInLocation("jar1_available", "jar1.jar");
        registerClassInLocation("jar2_available", "jar2.jar");
        registerClassInLocation("jar2_available2", "jar2.jar");
        Map<EventCounterKey, LongAdder> events = new HashMap<>();
        events.put(new EventCounterKey("jar2_available", EventType.FOUND_ON_CLASSPATH), asAdder(1L));
        events.put(new EventCounterKey("jar2_available2", EventType.FOUND_ON_CLASSPATH), asAdder(1L));
        events.put(new EventCounterKey("jar1_available", EventType.FOUND_ON_CLASSPATH), asAdder(1L));
        ClasspathReorderingResult reorderingResult = this.classpathEfficiencyEvaluator.reorderClasspath(
                        (List)ImmutableList.of("jar1.jar", "jar2.jar"), events, this.resourceInfoMap);
        Assertions.assertThat(reorderingResult.getClasspath()).containsExactly((Object[])new String[] {"jar1.jar", "jar2.jar"});
        Assertions.assertThat(reorderingResult.getClasspathTraversalCost()).isEqualTo(2);
        Assertions.assertThat(reorderingResult.getOptimizedClasspath()).containsExactly((Object[])new String[] {"jar2.jar", "jar1.jar"});
        Assertions.assertThat(reorderingResult.getOptimizedClasspathTraversalCost()).isEqualTo(1);
    }


    private LongAdder asAdder(long i)
    {
        LongAdder adder = new LongAdder();
        adder.add(i);
        return adder;
    }


    private void registerClassInLocation(String clazz, String location)
    {
        this.resourceInfoMap.put(clazz, new ResourceInfo(clazz, location, "classloader", ResourceType.CLASS));
    }
}
