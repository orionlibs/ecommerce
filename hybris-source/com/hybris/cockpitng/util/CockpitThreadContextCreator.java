/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.util;

import com.hybris.cockpitng.engine.operations.LongOperationThreadExecutor;
import java.util.Map;

/**
 *
 */
public interface CockpitThreadContextCreator extends LongOperationThreadExecutor
{
    /**
     * @deprecated since 6.5 use {@link LongOperationThreadExecutor#execute(Runnable)}
     */
    @Deprecated(since = "6.5", forRemoval = true)
    Map<String, Object> createThreadContext();


    /**
     * @deprecated since 6.5 use {@link LongOperationThreadExecutor#execute(Runnable)}
     */
    @Deprecated(since = "6.5", forRemoval = true)
    void initThreadContext(Map<String, Object> ctx);
}
