package de.hybris.platform.servicelayer;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class MockTestCleanupListener extends AbstractTestExecutionListener
{
    public void afterTestMethod(TestContext testContext) throws Exception
    {
        testContext.markApplicationContextDirty(null);
    }
}
