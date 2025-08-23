package de.hybris.bootstrap.testclasses;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class ByExecutionTimeSplitter
{
    private static final long DEFAULT_TIME = 1000L;
    final Map<String, Long> knownExecutionTimes;
    final Collection<Class> allTests;
    final int numberOfBuckets;


    ByExecutionTimeSplitter(Map<String, Long> knownExecutionTimes, Collection<Class<?>> allTests, int numberOfBuckets)
    {
        this.knownExecutionTimes = Objects.<Map<String, Long>>requireNonNull(knownExecutionTimes);
        this.allTests = Objects.<Collection<Class>>requireNonNull(allTests);
        this.numberOfBuckets = numberOfBuckets;
    }


    List<Class> getTestsFromBucket(int bucketNumber)
    {
        Queue<Bucket> heapOfBuckets = new PriorityQueue<>((Collection<? extends Bucket>)IntStream.range(1, this.numberOfBuckets + 1).mapToObj(n -> new Bucket(n)).collect(Collectors.toList()));
        this.allTests.stream().map(this::createTestClassInfo).sorted().forEachOrdered(c -> {
            Bucket bucket = heapOfBuckets.remove();
            bucket.addTestClass(c);
            heapOfBuckets.add(bucket);
        });
        return ((Bucket)heapOfBuckets.stream().filter(b -> (b.number == bucketNumber)).findAny().orElseThrow()).testClasses;
    }


    TestClassInfo createTestClassInfo(Class testClass)
    {
        Long knownExecutionTime = this.knownExecutionTimes.get(testClass.getName());
        long executionTime = (knownExecutionTime == null) ? 1000L : knownExecutionTime.longValue();
        return new TestClassInfo(testClass, executionTime);
    }
}
