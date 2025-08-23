/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved
 */
package com.hybris.cockpitng.engine.operations.impl;

import com.hybris.cockpitng.engine.operations.LongOperationThreadExecutor;

/**
 * This class is to satisfy Spring and offer standard implementation of the executor to be injected. Should be replace
 * by alias-overriding in applications using cockpitng in order to provide thread-pool based implementation. This sample
 * implementation is not production-ready.
 */
public class DefaultLongOperationThreadExecutor implements LongOperationThreadExecutor
{
}
