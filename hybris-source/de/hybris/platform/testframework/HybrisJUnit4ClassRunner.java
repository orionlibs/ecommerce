package de.hybris.platform.testframework;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

public class HybrisJUnit4ClassRunner extends BlockJUnit4ClassRunner implements UnifiedHybrisTestRunner
{
    private final HybrisTestClassRunnerLogic testClassRunnerLogic;


    public HybrisJUnit4ClassRunner(Class<?> clazz) throws InitializationError
    {
        super(clazz);
        this.testClassRunnerLogic = new HybrisTestClassRunnerLogic(this, (Runner)this);
    }


    public void filter(Filter filter) throws NoTestsRemainException
    {
        this.testClassRunnerLogic.filter(filter);
    }


    public void run(RunNotifier notifier)
    {
        this.testClassRunnerLogic.run(notifier);
    }


    protected Description describeChild(FrameworkMethod method)
    {
        Collection<Annotation> annos = new ArrayList<>();
        annos.addAll(Arrays.asList(method.getAnnotations()));
        annos.addAll(Arrays.asList(getTestClass().getAnnotations()));
        return Description.createTestDescription(getTestClass().getJavaClass(), testName(method), annos
                        .<Annotation>toArray(new Annotation[annos.size()]));
    }


    protected Statement withBeforeClasses(Statement statement)
    {
        List<FrameworkMethod> beforeMethod = getTestClass().getAnnotatedMethods(BeforeClass.class);
        if(beforeMethod == null)
        {
            return super.withBeforeClasses(statement);
        }
        return (Statement)new TenantActivationNotAllowedRunBefores(this, statement, beforeMethod);
    }


    public Class getCurrentTestClass()
    {
        return getTestClass().getJavaClass();
    }


    public void superFilter(Filter filter) throws NoTestsRemainException
    {
        super.filter(filter);
    }


    public void superRun(RunNotifier notifier)
    {
        super.run(notifier);
    }
}
