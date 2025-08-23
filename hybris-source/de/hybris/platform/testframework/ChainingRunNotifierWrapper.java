package de.hybris.platform.testframework;

import com.google.common.collect.Sets;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runner.notification.StoppedByUserException;

public class ChainingRunNotifierWrapper extends RunNotifier
{
    private final List<RunListener> fListeners = Collections.synchronizedList(new ArrayList<>());
    private final RunNotifier delegate;
    private final Class<?> classUnderTest;


    public ChainingRunNotifierWrapper(RunNotifier delegate)
    {
        this(delegate, null);
    }


    public ChainingRunNotifierWrapper(RunNotifier delegate, Class<?> classUnderTest)
    {
        this.delegate = delegate;
        this.classUnderTest = classUnderTest;
    }


    public void addFirstListener(RunListener listener)
    {
        this.fListeners.add(this.fListeners.size(), listener);
    }


    public void addListener(RunListener listener)
    {
        this.fListeners.add(0, listener);
    }


    public void removeListener(RunListener listener)
    {
        this.delegate.removeListener(listener);
        this.fListeners.remove(listener);
    }


    public void fireTestRunStarted(Description description)
    {
        Description enrichedTestDescription = tryToEnrichDescriptionWithAnnotations(description);
        Description recreatedTestDescription = recreateTestDescription(enrichedTestDescription);
        (new Object(this, recreatedTestDescription))
                        .run();
    }


    public void fireTestRunFinished(Result result)
    {
        (new Object(this, result))
                        .run();
    }


    public void fireTestStarted(Description description) throws StoppedByUserException
    {
        Description enrichedTestDescription = tryToEnrichDescriptionWithAnnotations(description);
        Description recreatedTestDescription = recreateTestDescription(enrichedTestDescription);
        this.delegate.fireTestStarted(recreatedTestDescription);
        (new Object(this, recreatedTestDescription))
                        .run();
    }


    public void fireTestFailure(Failure failure)
    {
        this.delegate.fireTestFailure(failure);
        (new Object(this, failure))
                        .run();
    }


    public void fireTestAssumptionFailed(Failure failure)
    {
        this.delegate.fireTestAssumptionFailed(failure);
        (new Object(this, failure))
                        .run();
    }


    public void fireTestIgnored(Description description)
    {
        Description enrichedTestDescription = tryToEnrichDescriptionWithAnnotations(description);
        Description recreatedTestDescription = recreateTestDescription(enrichedTestDescription);
        this.delegate.fireTestIgnored(recreatedTestDescription);
        (new Object(this, recreatedTestDescription))
                        .run();
    }


    public void fireTestFinished(Description description)
    {
        Description enrichedTestDescription = tryToEnrichDescriptionWithAnnotations(description);
        Description recreatedTestDescription = recreateTestDescription(enrichedTestDescription);
        (new Object(this, recreatedTestDescription))
                        .run();
        this.delegate.fireTestFinished(recreatedTestDescription);
    }


    private Description tryToEnrichDescriptionWithAnnotations(Description description)
    {
        return (this.classUnderTest == null) ? description : enrichDescriptionWithAnnotations(description);
    }


    private Description enrichDescriptionWithAnnotations(Description description)
    {
        return Description.createTestDescription(this.classUnderTest, description.getMethodName(), this.classUnderTest.getAnnotations());
    }


    protected Description recreateTestDescription(Description description)
    {
        if(description == null || description.getTestClass() == null || description.getTestClass().getAnnotations() == null)
        {
            return description;
        }
        Set<Annotation> annotations = new HashSet<>(description.getAnnotations());
        annotations.addAll(Sets.newHashSet((Object[])description.getTestClass().getAnnotations()));
        Annotation[] annotationArray = (Annotation[])annotations.stream().toArray(x$0 -> new Annotation[x$0]);
        return Description.createTestDescription(description.getTestClass(), description.getMethodName(), annotationArray);
    }


    public void pleaseStop()
    {
        this.delegate.pleaseStop();
    }


    public void removeAllListeners()
    {
        this.fListeners.clear();
    }
}
