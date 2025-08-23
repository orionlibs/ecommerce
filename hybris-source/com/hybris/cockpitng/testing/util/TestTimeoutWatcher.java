/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.testing.util;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.stream.Stream;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.junit.runners.model.TestTimedOutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A test watcher that assumes possibility of threads being locked in process. A thread dump will be generated in case
 * of test timeout.
 */
public class TestTimeoutWatcher extends TestWatcher
{
    private static final Logger LOG = LoggerFactory.getLogger(TestTimeoutWatcher.class);


    @Override
    protected void failed(final Throwable e, final Description description)
    {
        if(e instanceof TestTimedOutException)
        {
            LOG.error("Test failed with timeout ({}). Check threads dump below for details.", description);
            final ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();
            Stream.of(threadMXBean.dumpAllThreads(true, true)).map(ThreadInfo::toString).forEach(LOG::error);
        }
    }
}
