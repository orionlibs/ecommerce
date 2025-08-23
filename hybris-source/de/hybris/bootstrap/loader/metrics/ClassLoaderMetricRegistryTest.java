package de.hybris.bootstrap.loader.metrics;

import com.google.common.collect.ImmutableList;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.bootstrap.loader.metrics.internal.ClasspathEfficiencyEvaluator;
import de.hybris.bootstrap.loader.metrics.internal.ResourceInfo;
import de.hybris.bootstrap.loader.metrics.internal.YURLClasspathProvider;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

@UnitTest
public class ClassLoaderMetricRegistryTest
{
    private ClassLoaderMetricRegistry metricRegistry;
    private final ResourceInfo fooClassFromSourceJar = new ResourceInfo("com.foo", "source.jar", "classloader", ResourceType.CLASS);
    private final ResourceInfo barClassFromSourceJar = new ResourceInfo("com.bar", "source.jar", "classloader", ResourceType.CLASS);
    private final ResourceInfo bazClassFromBazingaJar = new ResourceInfo("com.baz", "bazinga.jar", "classloader", ResourceType.CLASS);
    private final ResourceInfo fooResource = new ResourceInfo("fooResource", "bazinga.jar", "classloader", ResourceType.RESOURCE);


    @Before
    public void setup()
    {
        YURLClasspathProvider mock = (YURLClasspathProvider)Mockito.mock(YURLClasspathProvider.class);
        Mockito.when(mock.getClasspath()).thenReturn(ImmutableList.of("source.jar", "bazinga.jar"));
        this.metricRegistry = new ClassLoaderMetricRegistry(new ClasspathEfficiencyEvaluator(), mock);
    }


    @Test
    public void shouldRecordClassLoaderMetricEvents()
    {
        ClassLoaderMetricEvent foundEvent = buildEvent(this.fooClassFromSourceJar, EventType.FOUND_ON_CLASSPATH);
        ClassLoaderMetricEvent notFoundEvent = buildEvent(this.fooClassFromSourceJar, EventType.NOT_FOUND_ON_CLASSPATH);
        publishEvents(new ClassLoaderMetricEvent[] {notFoundEvent, notFoundEvent, foundEvent});
        List<ClassLoaderMetric> metrics = this.metricRegistry.findMetrics(MetricQueryCriteria.FIND_ALL);
        Assertions.assertThat(metrics).hasSize(1);
        ClassLoaderMetric metric = metrics.get(0);
        Assertions.assertThat(metric.getName()).isEqualTo(this.fooClassFromSourceJar.getName());
        Assertions.assertThat(metric.getClassloader()).isEqualTo(this.fooClassFromSourceJar.getClassLoader());
        Assertions.assertThat(metric.getSource()).isEqualTo(this.fooClassFromSourceJar.getLocation());
        Assertions.assertThat((Comparable)metric.getResourceType()).isEqualTo(this.fooClassFromSourceJar.getType());
        Map<EventType, Integer> eventTypeCount = metric.getEventTypeCount();
        Assertions.assertThat(eventTypeCount.get(EventType.FOUND_ON_CLASSPATH)).isEqualTo(1);
        Assertions.assertThat(eventTypeCount.get(EventType.NOT_FOUND_ON_CLASSPATH)).isEqualTo(2);
    }


    @Test
    public void shouldOrderMetrics()
    {
        ClassLoaderMetricEvent fooNotFound = buildEvent(this.fooClassFromSourceJar, EventType.NOT_FOUND_ON_CLASSPATH);
        ClassLoaderMetricEvent barNotFound = buildEvent(this.barClassFromSourceJar, EventType.NOT_FOUND_ON_CLASSPATH);
        ClassLoaderMetricEvent bazNotFound = buildEvent(this.bazClassFromBazingaJar, EventType.NOT_FOUND_ON_CLASSPATH);
        ClassLoaderMetricEvent bazFound = buildEvent(this.bazClassFromBazingaJar, EventType.FOUND_ON_CLASSPATH);
        publishEvents(new ClassLoaderMetricEvent[] {fooNotFound, fooNotFound, fooNotFound});
        publishEvents(new ClassLoaderMetricEvent[] {barNotFound, barNotFound});
        publishEvents(new ClassLoaderMetricEvent[] {bazNotFound, bazFound});
        List<ClassLoaderMetric> metricsByNotFoundAscending = this.metricRegistry.findMetrics(MetricQueryCriteria.query().forEventType(EventType.NOT_FOUND_ON_CLASSPATH).sortAsc().build());
        List<ClassLoaderMetric> metricsByNotFoundDescending = this.metricRegistry.findMetrics(MetricQueryCriteria.query().forEventType(EventType.NOT_FOUND_ON_CLASSPATH).sortDesc().build());
        Assertions.assertThat(metricsByNotFoundAscending).extracting(i -> i.getName()).containsExactly((Object[])new String[] {bazNotFound.getName(), barNotFound
                        .getName(), fooNotFound
                        .getName()});
        Assertions.assertThat(metricsByNotFoundDescending).extracting(i -> i.getName()).containsExactly((Object[])new String[] {fooNotFound.getName(), barNotFound
                        .getName(), bazNotFound
                        .getName()});
    }


    @Test
    public void shouldFindSelectedResourceType()
    {
        ClassLoaderMetricEvent fooNotFound = buildEvent(this.fooClassFromSourceJar, EventType.NOT_FOUND_ON_CLASSPATH);
        ClassLoaderMetricEvent barNotFound = buildEvent(this.barClassFromSourceJar, EventType.NOT_FOUND_ON_CLASSPATH);
        ClassLoaderMetricEvent fooResourceFound = buildEvent(this.fooResource, EventType.FOUND_ON_CLASSPATH);
        publishEvents(new ClassLoaderMetricEvent[] {fooNotFound, fooNotFound, barNotFound, fooResourceFound});
        List<ClassLoaderMetric> classMetrics = this.metricRegistry.findMetrics(
                        MetricQueryCriteria.query().forResourceType(ResourceType.CLASS).build());
        List<ClassLoaderMetric> resourceMetrics = this.metricRegistry.findMetrics(
                        MetricQueryCriteria.query().forResourceType(ResourceType.RESOURCE).build());
        List<ClassLoaderMetric> allMetrics = this.metricRegistry.findMetrics(MetricQueryCriteria.query().build());
        Assertions.assertThat(classMetrics).hasSize(2);
        Assertions.assertThat(resourceMetrics).hasSize(1);
        Assertions.assertThat(allMetrics).hasSize(3);
    }


    private void publishEvents(ClassLoaderMetricEvent... events)
    {
        for(ClassLoaderMetricEvent event : events)
        {
            this.metricRegistry.onEvent(event);
        }
    }


    private ClassLoaderMetricEvent buildEvent(ResourceInfo resource, EventType foundOnClasspath)
    {
        return ClassLoaderMetricEvent.forType(resource.getType())
                        .withName(resource.getName())
                        .withClassloader(resource.getClassLoader())
                        .withSource(resource.getLocation())
                        .ofEventType(foundOnClasspath)
                        .build();
    }
}
