package de.hybris.platform.testframework;

import org.junit.runner.manipulation.Filter;
import org.junit.runner.manipulation.NoTestsRemainException;
import org.junit.runner.notification.RunNotifier;

public interface UnifiedHybrisTestRunner
{
    Class getCurrentTestClass();


    void superFilter(Filter paramFilter) throws NoTestsRemainException;


    void superRun(RunNotifier paramRunNotifier);
}
