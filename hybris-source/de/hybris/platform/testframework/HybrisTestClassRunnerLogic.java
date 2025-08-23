package de.hybris.platform.testframework;

import de.hybris.platform.core.Log4JUtils;
import de.hybris.platform.core.threadregistry.HybrisJUnitUtility;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.InitializationError;

public class HybrisTestClassRunnerLogic
{
    private final UnifiedHybrisTestRunner unifiedHybrisTestRunner;
    private final Runner runner;
    private final Class<?> classUnderTest;

    static
    {
        Log4JUtils.startup();
    }

    private static final Logger LOG = Logger.getLogger(HybrisTestClassRunnerLogic.class);
    private final Map<Class<? extends RunListener>, ? extends RunListener> listeners;
    private final Result result = new Result();
    private Filter nonAnnotationFilter = null;


    public HybrisTestClassRunnerLogic(UnifiedHybrisTestRunner unifiedHybrisTestRunner, Runner runner) throws InitializationError
    {
        this(unifiedHybrisTestRunner, runner, null);
    }


    public HybrisTestClassRunnerLogic(UnifiedHybrisTestRunner unifiedHybrisTestRunner, Runner runner, Class<?> classUnderTest) throws InitializationError
    {
        this.unifiedHybrisTestRunner = unifiedHybrisTestRunner;
        this.runner = runner;
        this.classUnderTest = classUnderTest;
        this.listeners = determineListeners();
    }


    public void filter(Filter filter) throws NoTestsRemainException
    {
        if(!(filter instanceof DefaultAnnotationFilter))
        {
            this.nonAnnotationFilter = filter;
        }
        this.unifiedHybrisTestRunner.superFilter(filter);
    }


    public void run(RunNotifier notifier)
    {
        DefaultAnnotationFilter defaultAnnotationFilter = new DefaultAnnotationFilter(this.nonAnnotationFilter);
        try
        {
            filter((Filter)defaultAnnotationFilter);
        }
        catch(NoTestsRemainException e)
        {
            return;
        }
        ChainingRunNotifierWrapper wrappedNotifier = new ChainingRunNotifierWrapper(notifier, this.classUnderTest);
        for(RunListener listener : this.listeners.values())
        {
            wrappedNotifier.addListener(listener);
            if(LOG.isDebugEnabled())
            {
                LOG.debug("Registered " + listener.getClass().getName() + " as run listener");
            }
        }
        try
        {
            HybrisJUnitUtility.registerJUnitMainThread();
            wrappedNotifier.fireTestRunStarted(this.runner.getDescription());
            this.unifiedHybrisTestRunner.superRun((RunNotifier)wrappedNotifier);
        }
        finally
        {
            wrappedNotifier.fireTestRunFinished(this.result);
            wrappedNotifier.removeAllListeners();
        }
    }


    private Map<Class<? extends RunListener>, RunListener> determineListeners() throws InitializationError
    {
        Map<Class<? extends RunListener>, RunListener> resultingListeners = new LinkedHashMap<>();
        RunListener resultListener = this.result.createListener();
        resultingListeners.put(RunListener.class, resultListener);
        Class curClass = this.unifiedHybrisTestRunner.getCurrentTestClass();
        while(curClass != null)
        {
            RunListeners listenerAnno = (RunListeners)curClass.getAnnotation(RunListeners.class);
            if(listenerAnno != null)
            {
                for(Class<? extends RunListener> listener : listenerAnno.value())
                {
                    try
                    {
                        resultingListeners.put(listener, listener.newInstance());
                    }
                    catch(InstantiationException e)
                    {
                        throw new InitializationError(Collections.singletonList(e));
                    }
                    catch(IllegalAccessException e)
                    {
                        throw new InitializationError(Collections.singletonList(e));
                    }
                }
            }
            curClass = curClass.getSuperclass();
        }
        return resultingListeners;
    }
}
